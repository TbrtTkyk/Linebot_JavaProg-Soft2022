package com.example.linebot.service;

import com.example.linebot.replier.RemindOn;
import com.example.linebot.repository.ReminderRepository;
import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.example.linebot.value.ReminderTuple;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// リマインダー情報を管理してReplierを返すService
@Service
public class ReminderService {

    private final ReminderRepository repository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository) {
        this.repository = reminderRepository;
    }

    // リマインダーを登録する
    public RemindOn doReplyOfNewItem(MessageEvent<TextMessageContent> event) {
        // テキストメッセージからslotを作成
        String userId = event.getSource().getUserId();
        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();
        ReminderSlot slot = new ReminderSlot(text);
        ReminderItem item = new ReminderItem(userId, slot);

        // リマインダー情報をデータベースに挿入
        repository.insert(item);

        return new RemindOn(text);
    }

    // 時間になったリマインダーを知らせる
    public List<PushMessage> doPushReminderItems() {
        // 時間になったリマインダーを取得
        List<ReminderTuple> ReminderTuples =
                repository.findPreviousItems();

        // メッセージを作成
        List<PushMessage> pushMessages = ReminderTuples.stream()
                .map(tuple -> toPushMessage(tuple))
                .toList();

        // 対象のリマインダーを削除
        repository.deletePreviousItems();

        return pushMessages;
    }

    // リマインダー情報をメッセージに変換
    private PushMessage toPushMessage(ReminderTuple tuple) {
        String userId = tuple.getUserId();
        String pushText = tuple.getPushText();
        String body = String.format("%s の時間です!", pushText);
        return new PushMessage(userId, new TextMessage(body));
    }
}
