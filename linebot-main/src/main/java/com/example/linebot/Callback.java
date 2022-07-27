package com.example.linebot;

import com.example.linebot.replier.*;
import com.example.linebot.service.CovidGovService;
import com.example.linebot.service.DogCatClassifyService;
import com.example.linebot.service.ReminderService;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@LineMessageHandler
public class Callback {

    private static final Logger log = LoggerFactory.getLogger(Callback.class);

    private final ReminderService reminderService;
    private final CovidGovService covidGovService;
    private final DogCatClassifyService dogCatClassifyService;

    @Autowired
    public Callback(ReminderService reminderService,
                    CovidGovService covidGovService,
                    DogCatClassifyService dogCatClassifyService) {
        this.reminderService = reminderService;
        this.covidGovService = covidGovService;
        this.dogCatClassifyService = dogCatClassifyService;
    }

    // フォローイベントに対応する
    @EventMapping
    public Message handleFollow(FollowEvent event) {
        // 実際はこのタイミングでフォロワーのユーザIDをデータベースに格納しておくなど
        Follow follow = new Follow(event);
        return follow.reply();
    }

    // 文章で話しかけられたとき（テキストメッセージのイベント）に対応する
    @EventMapping
    public Message handleMessage(MessageEvent<TextMessageContent> event) {
        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();
        // テキストメッセージを正規表現で判別して返信内容を変える
        switch (Intent.whichIntent(text)) {
            case REMINDER:
                RemindOn reminderOn = reminderService.doReplyOfNewItem(event);
                return reminderOn.reply();
            case COVID_TOTAL:
                CovidReport covidReport = covidGovService.doReplyWithCovid(event);
                return covidReport.reply();
            case COVID_INCREASE:
                CovidIncreaseReport covidIncreaseReport = covidGovService.doReplyRateWithCovid(event);
                return covidIncreaseReport.reply();
            case UNKNOWN:
            default:
                Parrot parrot = new Parrot(event);
                return parrot.reply();
        }
    }

    @EventMapping
    public Message handleImageMessage(MessageEvent<ImageMessageContent> event) {
        // 画像メッセージが送られたら必ずDogCatClassifyServiceへ繋げる
        try{
            DogCatClassifyReport report = dogCatClassifyService.ClassifyImage(event);
            return report.reply();
        } catch( ExecutionException | InterruptedException | IOException | RestClientException e ) {
            return new TextMessage("処理過程でエラーが発生しました。");
        }
    }
}
