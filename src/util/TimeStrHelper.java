package util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeStrHelper {

    public static final String fullDate = "yyyy-MM-dd HH:mm";
    public static final String yearMonthDay = "yyyy-MM-dd";
    public static final String hourMin = "HH:mm";
    public static final String dayHourMin = "dd HH:mm";

    public static DateTimeFormatter fullDateFormatter = DateTimeFormat.forPattern(TimeStrHelper.fullDate);
    public static DateTimeFormatter yearMonthDayFormatter = DateTimeFormat.forPattern(TimeStrHelper.yearMonthDay);
    public static DateTimeFormatter hourMinFormatter = DateTimeFormat.forPattern(TimeStrHelper.hourMin);
    public static DateTimeFormatter dayHourMinFormatter = DateTimeFormat.forPattern(TimeStrHelper.dayHourMin);

    public static DateTime parseFullDate(String str){
        return fullDateFormatter.parseDateTime(str);
    }
    public static DateTime parseDate(String str){
        return yearMonthDayFormatter.parseDateTime(str);
    }
    public static DateTime parseHourMin(String str){
        return hourMinFormatter.parseDateTime(str);
    }
    public static DateTime parseDayHourMin(String str) {
        return dayHourMinFormatter.parseDateTime(str);
    }

    public static String printFullDate(DateTime dateTime){
        return fullDateFormatter.print(dateTime);
    }
    public static String printDate(DateTime dateTime){
        return yearMonthDayFormatter.print(dateTime);
    }
    public static String printHourMin(DateTime dateTime){
        return hourMinFormatter.print(dateTime);
    }
    public static String printDayHourMin(DateTime dateTime){
        return dayHourMinFormatter.print(dateTime);
    }

    public static int[] fullDateStr2IntArray(String str){
        DateTime dateTime = TimeStrHelper.parseFullDate(str);
        int ret[] = new int[5];
        ret[0] = dateTime.getYear();            // year
        ret[1] = dateTime.getMonthOfYear();     // month
        ret[2] = dateTime.getDayOfMonth();      // day
        ret[3] = dateTime.getHourOfDay();       // hour
        ret[4] = dateTime.getMinuteOfHour();    // min
        return ret;
    }

}
