package com.example.linebot.repository;

import com.example.linebot.value.DogCatItem;
import com.example.linebot.value.ImageMessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

// メッセージの画像の分析結果を自作のWebAPIから取得して返すRepository（WebAPI構築は別でする）
@Repository
public class DogCatClassifyRepository {

    // 犬猫分析APIのURL
    private final String URL = "http://localhost:5000/classify/dogcat/";//;"http://<<SERVER_URL>>:5000/classify/dogcat/"


    private final RestTemplate restTemplate;
    private final ImageMessageEncoder encoder;

    @Autowired
    public DogCatClassifyRepository(RestTemplateBuilder templateBuilder, ImageMessageEncoder encoder) {
        this.restTemplate = templateBuilder.build();
        this.encoder = encoder;
    }

    // メッセージの画像データを分析する
    public DogCatItem ClassifyImg(String messageId) throws ExecutionException, InterruptedException, IOException, RestClientException {
        // 画像をbase64形式でエンコード
        String base64 = encoder.encodeImageMessage(messageId);
        // リクエスト用のJsonデータを作成
        Map<String, String> img = new HashMap<>();
        img.put("img", base64);
        // 犬猫分析APIから分析結果を取得
        DogCatItem item = restTemplate.postForObject(URL, img, DogCatItem.class);
        return item;
    }
}
