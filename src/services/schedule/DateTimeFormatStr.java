package services.schedule;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by wykwon on 2016-01-06.
 */
public class DateTimeFormatStr {

    public static final String fullDataTime = "yyyy-MM-dd HH:mm";
    public static final String dateOnly= "yyyy-MM-dd";
    public static final String timeOnly= "HH:mm";
    public static DateTimeFormatter fullDateTimeFormatter= DateTimeFormat.forPattern(DateTimeFormatStr.fullDataTime);
    public static DateTimeFormatter dateOnlyFormatter= DateTimeFormat.forPattern(DateTimeFormatStr.dateOnly);
    public static DateTimeFormatter timeOnlyFormatter= DateTimeFormat.forPattern(DateTimeFormatStr.timeOnly);


    public static DateTime parseFullDateTime(String str){
        return fullDateTimeFormatter.parseDateTime(str);
    }

    public static DateTime parseDate(String str){
        return dateOnlyFormatter.parseDateTime(str);
    }

    public static DateTime parseTime(String str){
        return timeOnlyFormatter.parseDateTime(str);
    }

    public static String print(DateTime dateTime){
        return fullDateTimeFormatter.print(dateTime);
    }
    public static String printDate(DateTime dateTime){
        return dateOnlyFormatter.print(dateTime);
    }
    public static String printTime(DateTime dateTime){
        return timeOnlyFormatter.print(dateTime);
    }
}
