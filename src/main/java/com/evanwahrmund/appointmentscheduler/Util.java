package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class used to format dates and times
 */
public class Util {
    /**
     * DateTimeFormatter to format Appointment times without seconds
     */
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd-uuuu HH:mm");
    /**
     * DateTimeFormatter to format precise timestamps for monitoring login activity
     */
    public static final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.nnnnn '['z']'");

    /**
     * Formats ZonedDateTime into a clean looking format with no seconds
     * @param zonedDateTime ZonedDateTime to be formatted without seconds
     * @return String of formatted ZonedDateTime
     */
    public static String formatDateTime(ZonedDateTime zonedDateTime){
        return dateTimeFormatter.format(zonedDateTime);
    }

    /**
     * Formats ZonedDateTime into a timestamp format
     * @param timestamp ZonedDateTime to be formatted as a timestamp
     * @return String of formatted ZonedDateTime as timestamp
     */
    public static String formatTimestamp(ZonedDateTime timestamp){
        return timestampFormatter.format(timestamp);
    }


}
