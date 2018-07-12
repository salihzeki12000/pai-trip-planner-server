package edu.hanyang.protocol;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오후 7:39
 * To change this template use File | Settings | File Templates.
 */
public class Duration {
    public Integer day;
    public Integer hour;

//    public Duration(int day) {
//        this.day = day;
//        this.hour = null;
//    }

    public Duration(Integer day, Integer hour) {
        this.day = day;
        this.hour = hour;
    }


    public String toString() {
        if (hour == null) {
            return day + "일 동안";
        } else if (day == null) {
            return hour + "시간 동안";
        } else {
            return day + "일 " + hour + "시간 동안";
        }
    }
}
