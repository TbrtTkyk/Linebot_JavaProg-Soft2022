package com.example.linebot.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

// スネークケース　abc_de　のキーと、
// キャメルケース　abcDe　のフィールド名をマッピングする
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CovidItemElement {

    private final LocalDate date;
    private final String nameJp;
    private final int npatients;

    @JsonCreator
    public CovidItemElement(
            LocalDate date, String nameJp, int npatients){
        this.date = date;
        this.nameJp = nameJp;
        this.npatients = npatients;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getNameJp() {
        return nameJp;
    }

    public int getNpatients() {
        return npatients;
    }
}
