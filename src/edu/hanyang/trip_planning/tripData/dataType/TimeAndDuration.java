package edu.hanyang.trip_planning.tripData.dataType;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 1
 * Time: 오후 6:23
 * To change this template use File | Settings | File Templates.
 */
public class TimeAndDuration {
    /**
     * 시작시간
     * <p/>
     * format (hh:MM) 또는 (yyyy MM:dd hh:MM)
     */
    public String startTime;
    /**
     * 시간간격 [단위 분]
     */
    public int duration;

    public TimeAndDuration(String startTime, int duration) {
        this.startTime = startTime;
        this.duration = duration;
    }
}
