package com.example.linebot.replier;

import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidItemElement;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.time.LocalDate;
import java.util.List;

public class CovidReport implements Replier{

    private static final String MESSAGE_FORMAT =
            "%s の %s 月 %s 日時点の感染者数は %d 人";

    private final CovidItem item;

    public CovidReport(CovidItem item) {
        this.item = item;
    }

    @Override
    public Message reply(){
        String body = "データがありません";
        List<CovidItemElement> list = item.getItemList();
        if(list.size() > 0){
            body = getMessageOfLast();
        }
        return new TextMessage(body);
    }

    private String getMessageOfLast(){
        List<CovidItemElement> list = item.getItemList();
        CovidItemElement atLast = list.get(0);
        LocalDate date = atLast.getDate();
        int month = date.getMonthValue();
        int dayOfMonth = date.getDayOfMonth();
        String region = atLast.getNameJp();
        int npatients = atLast.getNpatients();
        return String.format(
                MESSAGE_FORMAT, region, month, dayOfMonth, npatients);
    }

}
