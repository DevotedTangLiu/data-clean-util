package com.gzcb.creditcard.data.clean.uitls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author tangliu
 */
public class DateUtils {

    /**
     * 将当前时间偏移再格式化
     *
     * @param pattern
     * @param count
     * @param unit
     * @return
     */
    public static String getTimeByOffset(String pattern, Long count, ChronoUnit unit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tar = now.plus(count, unit);
        return tar.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将Date类型对象按格式转为字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getTimeString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * change date string into target pattern
     *
     * @param source
     * @param sourcePattern
     * @param targetPattern
     * @return
     */
    public static String getTimeString(String source, String sourcePattern, String targetPattern) {
        if (source == null || source.trim().equals("")) {
            return "";
        }
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.parse(source, DateTimeFormatter.ofPattern(sourcePattern)), LocalDateTime.MIN.toLocalTime());
        return dateTime.format(DateTimeFormatter.ofPattern(targetPattern));
    }

    /**
     * 获取当前时间
     *
     * @param pattern
     * @return
     */
    public static String getCurrentTime(String pattern) {
        LocalDateTime dateTime = LocalDateTime.now();
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static void main(String[] args) {
        System.out.println(getTimeString("2019-06-13 18:01:31", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd"));
    }

}
