package com.example.linebot.service;

import com.example.linebot.replier.DogCatClassifyReport;
import com.example.linebot.repository.DogCatClassifyRepository;
import com.example.linebot.value.DogCatItem;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

// 画像メッセージから、犬猫分析結果のReplierを返す
@Service
public class DogCatClassifyService {

    private final DogCatClassifyRepository dogCatClassifyRepository;

    @Autowired
    public DogCatClassifyService(DogCatClassifyRepository dogCatClassifyRepository) {
        this.dogCatClassifyRepository = dogCatClassifyRepository;
    }

    public DogCatClassifyReport ClassifyImage(MessageEvent<ImageMessageContent> event)  throws ExecutionException, InterruptedException, IOException, RestClientException {
        // メッセージIDを取得
        String messageId = event.getMessage().getId();
        // 画像を分析
        DogCatItem item = dogCatClassifyRepository.ClassifyImg(messageId);
        // 返信クラスを返す
        return new DogCatClassifyReport(item);
    }

}
