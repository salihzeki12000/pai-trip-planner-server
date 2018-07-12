package services.schedule;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by wykwon on 2015-11-12.
 */
public class OrderedFreeTime implements Comparable<OrderedFreeTime> {
    public DateTime startTime;
    public DateTime endTime;
    public Long order;


    public OrderedFreeTime(DateTime startTime, DateTime endTime, int order) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.order = (long) order;
    }

    public OrderedFreeTime(String startTimeStr, String endTimeStr, int order) {
        this.startTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(startTimeStr);
        this.endTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(endTimeStr);
        this.order = (long) order;
    }


    public OrderedFreeTime(DateTime startTime, DateTime endTime, FreeTimeSortMethod sortMethod) {
        this.startTime = startTime;
        this.endTime = endTime;
        if (sortMethod == FreeTimeSortMethod.Time) {
            this.order = startTime.getMillis();

        } else if (sortMethod == FreeTimeSortMethod.Duration) {
            this.order =-(endTime.getMillis() - startTime.getMillis()) ;
        }

    }

    public OrderedFreeTime(String startTimeStr, String endTimeStr,FreeTimeSortMethod sortMethod) {
        this.startTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(startTimeStr);
        this.endTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(endTimeStr);
        if (sortMethod == FreeTimeSortMethod.Time) {
            this.order = startTime.getMillis();

        } else if (sortMethod == FreeTimeSortMethod.Duration) {
            this.order =-(endTime.getMillis() - startTime.getMillis()) ;
        }
    }
    public String toString(){
        return DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(startTime) + " -- "
                + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(endTime);
    }

    @Override
    public int compareTo(OrderedFreeTime o) {
        return order.compareTo(o.order);
    }


    public static void main(String[] args) {

    }

}
