package com.example.linebot.repository;

import com.example.linebot.value.ReminderItem;
import com.example.linebot.value.ReminderSlot;
import com.example.linebot.value.ReminderTuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

// リマインダー情報をh2データベース上でやり取りするRepository
@Repository
public class ReminderRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public ReminderRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // 過去のリマインダーを取得する
    public List<ReminderTuple> findPreviousItems() {
        //language=sql
        String sql = "select user_id, push_at, push_text " +
                "from reminder_item " +
                "where push_at <= ? ";

        LocalTime now = LocalTime.now();
        List<ReminderTuple> list =
                jdbc.query(sql,
                        new DataClassRowMapper(ReminderTuple.class),
                        now);
        return list;
    }

    // 過去のリマインダーを削除する
    public void deletePreviousItems() {
        //language=sql
        String sql = "delete from reminder_item " +
                "where push_at <= ? ";

        LocalTime now = LocalTime.now();
        jdbc.update(sql, now);
    }

    // リマインダーを挿入する
    public void insert(ReminderItem item) {
        // language=sql
        String sql = "insert into reminder_item "
                + "(user_id, push_at, push_text) "
                + "values (?, ?, ?)";

        String userId = item.getUserId();
        ReminderSlot slot = item.getSlot();
        jdbc.update(sql, userId, slot.getPushAt(), slot.getPushText());
    }
}
