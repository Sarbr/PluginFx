package com.sarbr.plugin.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    private static final String FORMAT = "yyyy-MM-dd";
    public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static String convertDateToString(Date date) {
        return new SimpleDateFormat(FORMAT).format(date);
    }

    public static String convertTimestampToString(Timestamp date) {
        return new SimpleDateFormat(FORMAT_DATETIME).format(date);
    }

    public static String hello(){
        final LocalDateTime now = LocalDateTime.now();
        final int a = now.getHour();
        if (a <= 6) {
            return "凌晨";
        }
        if (a <= 12) {
            return "上午好！";
        }
        if (a <= 18) {
            return "下午好！";
        }
        return "晚上好！";
    }
}
