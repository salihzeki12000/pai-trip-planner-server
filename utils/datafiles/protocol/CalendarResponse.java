package edu.hanyang.protocol;

import edu.hanyang.scheduling.dataType.ActivityType;
import edu.hanyang.scheduling.dataType.PersonalScheduleEntry;
import edu.hanyang.scheduling.dataType.ProbabilisticDuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2015-10-28.
 */
public class CalendarResponse {

    public String personName;
    public List<PersonalScheduleEntry> scheduleList = new ArrayList<PersonalScheduleEntry>();

    public CalendarResponse(String personName) {
        this.personName = personName;
    }

    public void addScheduleEntry(PersonalScheduleEntry entry) {
        scheduleList.add(entry);
    }

    public void addScheduleEntry(String poiTitle, String startTime, ProbabilisticDuration duration, ActivityType activityType) {
        PersonalScheduleEntry entry = new PersonalScheduleEntry(poiTitle, startTime, duration, activityType);

        scheduleList.add(entry);
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(personName + "'s schedule\n");
        for (PersonalScheduleEntry entry : scheduleList) {
            strbuf.append(entry + "\n");
        }
        return strbuf.toString();

    }
}
