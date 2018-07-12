package edu.hanyang.trip_planning.tripData.dataType;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 19
 * Time: 오후 5:45
 * To change this template use File | Settings | File Templates.
 */
public class DayOfWeekInMonth {
    public int weekOfMonth;
    public DayOfWeek dayOfWeek;

    public DayOfWeekInMonth(int weekOfMonth, DayOfWeek dayOfWeek) {
        this.weekOfMonth = weekOfMonth;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return weekOfMonth + "째주의 " + dayOfWeek;
    }
}
