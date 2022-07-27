package com.example.linebot.service;

import com.example.linebot.replier.CovidIncreaseReport;
import com.example.linebot.replier.CovidReport;
import com.example.linebot.repository.CovidGovRepository;
import com.example.linebot.value.CovidItem;
import com.example.linebot.value.CovidRateSlot;
import com.example.linebot.value.CovidSlot;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// コロナ感染者数に関するメッセージのReplierを返すService
@Service
public class CovidGovService {

    private final CovidGovRepository covidGovRepository;

    @Autowired
    public CovidGovService(CovidGovRepository covidGovRepository){
        this.covidGovRepository = covidGovRepository;
    }

    public CovidReport doReplyWithCovid(
            MessageEvent<TextMessageContent> event){
        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();
        CovidSlot covidSlot = new CovidSlot(text);
        String region = covidSlot.getRegion();
        CovidItem covidItem =
                covidGovRepository.findCovidGovAIP(region);
        return new CovidReport(covidItem);
    }

    public CovidIncreaseReport doReplyRateWithCovid(
            MessageEvent<TextMessageContent> event){
        TextMessageContent tmc = event.getMessage();
        String text = tmc.getText();
        CovidRateSlot covidRateSlot = new CovidRateSlot(text);
        String region = covidRateSlot.getRegion();
        CovidItem covidItem =
                covidGovRepository.findCovidGovAIP(region);
        return new CovidIncreaseReport(covidItem);
    }
}
