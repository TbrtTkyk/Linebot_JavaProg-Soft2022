package com.example.linebot;

import com.example.linebot.service.ReminderService;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import com.linecorp.bot.model.response.BotApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.scheduling.annotation.Scheduled;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class Push {

    private static final Logger log = LoggerFactory.getLogger(Push.class);

    // push先のユーザID
    private String userId = "<<LINE_USER_ID>>";

    private final LineMessagingClient messagingClient;
    private final ReminderService reminderService;

    @Autowired
    public Push(LineMessagingClient lineMessagingClient,
                ReminderService reminderService) {
        this.messagingClient = lineMessagingClient;
        this.reminderService = reminderService;
    }

    //テスト
    @GetMapping("test")
    public String hello(HttpServletRequest request) {
        return "Get from " + request.getRequestURL();
    }

    // 時報をpushする
    @GetMapping("timetone")
    // @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
    public String pushTimeTone() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("a K:mm");
        String text = dtf.format(LocalDateTime.now());
        try {
            PushMessage pMsg =
                    new PushMessage(userId, new TextMessage(text));
            BotApiResponse resp = messagingClient.pushMessage(pMsg).get();
            log.info("Sent messages: {}", resp);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    // 1分おきに対象リマインダーの通知処理を行う
    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Tokyo")
    public void pushReminder() {
        try {
            List<PushMessage> messages =
                    reminderService.doPushReminderItems();
            for (PushMessage message : messages) {
                BotApiResponse resp =
                        messagingClient.pushMessage(message).get();
                log.info("Sent messages: {}", resp);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
