package com.example.linebot.replier;

import com.example.linebot.value.DogCatItem;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Objects;

// メッセージ画像の分析結果からメッセージを返信するクラス
public class DogCatClassifyReport implements Replier{

    private DogCatItem item;

    public DogCatClassifyReport(DogCatItem item) {
        this.item = item;
    }

    public Message reply() {
        String text;
        if(Objects.equals(item.getStatus(), "failed")) {
            text = "この画像を分析できませんでした。\nエラーメッセージ: " + item.getResult();
        } else {
            if( Objects.equals(item.getResult(), "cat") ) text = "この画像は猫です。\n";
            else text = "この画像は犬です。\n";
            text += String.format("猫の確率: %.2f\n犬の確率: %.2f\n", item.getCat_per() * 100, item.getDog_per() * 100);
        }

        return new TextMessage(text);
    }
}
