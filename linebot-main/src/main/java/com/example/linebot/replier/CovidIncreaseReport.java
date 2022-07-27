package com.example.linebot.replier;

import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidItemElement;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.time.LocalDate;
import java.util.List;

//１週間でのコロナの新規感染者数の増加率を返す
public class CovidIncreaseReport implements Replier{

    private static final String PATIENTS_MESSAGE_FORMAT =
            "%s の %s 月 %s 日 ～ %s 月 %s 日 の新規感染者数は %s";
    private static final String RATE_MESSAGE_FORMAT =
            "新規感染者数の増加率は %s %%";

    private final CovidItem item;

    public CovidIncreaseReport(CovidItem item) {
        this.item = item;
    }

    @Override
    public Message reply(){
        String body = "データがありません";
        List<CovidItemElement> list = item.getItemList();
        if(list.size() > 13){
            body = getMessageRate();
        }
        return new TextMessage(body);
    }

    private String getMessageRate(){
        List<CovidItemElement> list = item.getItemList();

        //データの選択
        CovidItemElement atLast = list.get(0);
        CovidItemElement oneWeekAgo = list.get(7);
        CovidItemElement twoWeeksAgo = list.get(14);

        //情報の取得
        String region = atLast.getNameJp();
        LocalDate dateAtLast = atLast.getDate();
        LocalDate date1weekAgo = oneWeekAgo.getDate();
        LocalDate date2weekAgo = twoWeeksAgo.getDate();

        //新規感染者数の計算
        int patientsThisWeek = calcPatients(oneWeekAgo, atLast);
        int patientsLastWeek = calcPatients(twoWeeksAgo, oneWeekAgo);

        //新規感染者数を伝える文の生成
        String textThisPatients = getPatientsText(region, date1weekAgo, dateAtLast, patientsThisWeek);
        String textLastPatients = getPatientsText(region, date2weekAgo, date1weekAgo, patientsLastWeek);

        //増加率を伝える文の生成
        String textRate = "不明";
        if(patientsLastWeek != 0){
            //増加率を計算してString型にする
            float increaseRate = (float)(patientsThisWeek - patientsLastWeek) / patientsLastWeek * 100;
            textRate = String.format("%.1f", increaseRate);
        }
        String textIncreaseRate = String.format(RATE_MESSAGE_FORMAT, textRate);

        //3つの文章をまとめて返す
        return String.format("%s\n%s\n%s", textLastPatients, textThisPatients, textIncreaseRate);
    }

    //新規感染者数の計算
    private int calcPatients(CovidItemElement before, CovidItemElement after){
        int npatientsBefore = before.getNpatients();
        int npatientsAfter = after.getNpatients();
        return npatientsAfter - npatientsBefore;
    }

    //新規感染者数を通知するテキストの生成
    private String getPatientsText(String region, LocalDate dateBefore, LocalDate dateAfter, int patients) {
        //日付の取得
        int monthBefore = dateBefore.getMonthValue();
        int dayOfMonthBefore = dateBefore.getDayOfMonth();
        int monthAfter = dateAfter.getMonthValue();
        int dayOfMonthAfter = dateAfter.getDayOfMonth();

        return String.format(PATIENTS_MESSAGE_FORMAT,
                region, monthBefore, dayOfMonthBefore, monthAfter, dayOfMonthAfter, patients);
    }
}
