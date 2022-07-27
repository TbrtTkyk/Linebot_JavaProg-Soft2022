package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;

// 自作WebAPIの画像犬猫判定JSONデータをマッピングするクラス
public class DogCatItem {
    // 犬猫の確率
    private double dog_per;
    private double cat_per;
    // 処理結果
    private String result;
    private String status;

    @JsonCreator
    public DogCatItem(double dog_per, double cat_per, String result, String status) {
        this.dog_per = dog_per;
        this.cat_per = cat_per;
        this.result = result;
        this.status = status;
    }

    public double getDog_per() {
        return dog_per;
    }

    public double getCat_per() {
        return cat_per;
    }

    public String getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }
}
