package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

// WebAPIから取得したコロナの感染者数JSONデータをマッピングするクラス
public class CovidItem {

    private  final List<CovidItemElement> itemList;

    @JsonCreator
    public CovidItem(List<CovidItemElement> itemList){
        this.itemList = itemList;
    }

    public List<CovidItemElement> getItemList() {
        return itemList;
    }
}
