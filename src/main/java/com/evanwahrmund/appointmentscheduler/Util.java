package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Date: MM/DD/YYYY Or MM-DD
 */
public class Util {

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-uuuu HH:mm");
    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-uuuu");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static final LocalTime START_TIME = LocalTime.of(8, 0);
    public static final LocalTime END_TIME = LocalTime.of(22,0);
    public static final int START_HOUR = START_TIME.getHour();
    public static final int START_MIN = START_TIME.getMinute();
    public static final int END_HOUR = END_TIME.getHour();
    public static final int END_MIN = END_TIME.getMinute();
    public static final ZoneId HQ_TIME_ZONE = ZoneId.of(ZoneId.SHORT_IDS.get("IET"));


    public static String formatDate(LocalDate date){
       return  dateFormatter.format(date);
    }
    public static String formatTime(LocalTime time){
        return timeFormatter.format(time);
    }
    public static String formatDateTime(ZonedDateTime localDateTime){
        return dateTimeFormatter.format(localDateTime);
    }
    public static LocalTime stringToTime(String time){
        //12:23
        int hour =Integer.valueOf(time.substring(0,2));
        int min = Integer.valueOf(time.substring(3,5));
        return LocalTime.of(hour, min);
    }
    public static LocalDate stringToDate(String date){
        //01-14-2021
        int month = Integer.valueOf(date.substring(0,2));
        int day = Integer.valueOf(date.substring(3,5));
        int year = Integer.valueOf(date.substring(6));
        return LocalDate.of(year, month, day);
    }
    /*
    public static ZonedDateTime localToZonedTime(ZonedDateTime local){
        return ZonedDateTime.of(local,ZoneId.systemDefault());
    } */

    public static ZonedDateTime toHQTimezone(ZonedDateTime zoned){
        return zoned.withZoneSameInstant(HQ_TIME_ZONE);
    }

    public static boolean validateTime(LocalTime start, LocalTime end){
        int startHour = start.getHour();
        int endHour = end.getHour();
        if((startHour < START_HOUR) || (endHour < START_HOUR))
            return false;
        if((startHour > END_HOUR) || (endHour > END_HOUR))
            return false;
        return true;
    }/*
    public static int countDaysInMonth(ZonedDateTime firstOfMonth){
        int count = 1;
        Month month = firstOfMonth.getMonth();
        while(firstOfMonth.getMonth() == month){
            count += 1;
            firstOfMonth = firstOfMonth.plusDays(1);
        }
        return count;
    }*/



}
