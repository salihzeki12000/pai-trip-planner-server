package edu.hanyang.protocol;

/**
 * Created by wykwon on 2015-10-28.
 */
public class CalendarRequest {
    public String nameOfPerson;
    public String startTime;
    public String endTime;

    public CalendarRequest(String nameOfPerson, String startTime, String endTime) {
        this.nameOfPerson = nameOfPerson;
        this.startTime = startTime;
        this.endTime = endTime;
    }


}
