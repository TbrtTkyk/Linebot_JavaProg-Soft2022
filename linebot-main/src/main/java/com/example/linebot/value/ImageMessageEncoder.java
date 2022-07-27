package com.example.linebot.value;

import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineBlobClientBuilder;
import com.linecorp.bot.client.MessageContentResponse;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

// 画像メッセージをbase64文字列にエンコードするクラス
@Component
public class ImageMessageEncoder {

    private final LineBlobClient client;

    @Autowired
    public ImageMessageEncoder(LineBlobClient client) {
        this.client = client;
    }

    public String encodeImageMessage(String messageId) throws ExecutionException, InterruptedException, IOException {
        // 画像データを取得
        MessageContentResponse response = client.getMessageContent(messageId).get();

        // base64形式に変換
        InputStream responseInputStream = response.getStream();
        byte[] byteArray = responseInputStream.readAllBytes();
        String base64 = Base64.encodeBase64String(byteArray);
        return base64;
    }
}
