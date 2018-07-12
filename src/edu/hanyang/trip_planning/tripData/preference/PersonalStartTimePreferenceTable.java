package edu.hanyang.trip_planning.tripData.preference;


import edu.hanyang.trip_planning.tripData.dataType.ActivityType;
import edu.hanyang.trip_planning.tripData.dataType.TimeAndDuration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 27
 * Time: 오후 1:35
 * To change this template use File | Settings | File Templates.
 */
public class PersonalStartTimePreferenceTable {
    private static Logger logger = Logger.getLogger(PersonalStartTimePreferenceTable.class);
    private String userName;

    private Map<ActivityType, double[]> preferenceTableMap;
    private int timeInterval;
    private int sizeOfIndices;

    public PersonalStartTimePreferenceTable(String userName, int timeInterval) {
        this.userName = userName;
        this.timeInterval = timeInterval;

        this.sizeOfIndices = 24 * 60 / timeInterval;
        preferenceTableMap = new HashMap<ActivityType, double[]>();
    }

    /**
     * 언제 어떤 행동을 하는걸 좋아하는지?
     *
     * @param timeAndDuration 시작 시간 및 기간
     * @param activityType    활동의 종류
     * @return 선호도: 0-1 사이의 값
     */
    public double timePreference(TimeAndDuration timeAndDuration, ActivityType activityType) {
        DateTime dateTime = parseDateTime(timeAndDuration.startTime);
        return startTimePreference(dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), activityType);
    }

    public ActivityType toAvaliableActivity(String str) {
        if (str.equals("식사")) {
            return ActivityType.Eat;
        } else if (str.equals("운동")) {
            return ActivityType.OutdoorExercise;
        } else if (str.equals("회의")) {
            return ActivityType.Meeting;
        } else if (str.equals("관광")) {
            return ActivityType.OutdoorSightSeeing;
        } else if (str.equals("산책")) {
            return ActivityType.Walk;
        } else if (str.equals("공연")) {
            return ActivityType.Show;
        } else if (str.equals("쇼핑")) {
            return ActivityType.Shopping;
        } else if (str.equals("일")) {
            return ActivityType.Work;
        } else {
            throw new RuntimeException("no shch activity as " + str);
        }
    }

    public void addTimePreference(String activityTypeStr, int hourOfDay, int minuteOfHour, double value) {
        addTimePreference(toAvaliableActivity(activityTypeStr), hourOfDay, minuteOfHour, value);
    }

    public void addTimePreference(ActivityType activityType, int hourOfDay, int minuteOfHour, double value) {
        double preferences[] = preferenceTableMap.get(activityType);

        if (preferences == null) {
            preferences = new double[sizeOfIndices];
        }

        int index = toIndex(hourOfDay, minuteOfHour);
        preferences[index] = value;
        preferenceTableMap.put(activityType, preferences);
    }

    public String toString() {
        // table을 뿌려라.
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(Integer.toString(this.timeInterval) + '\t');
        for (ActivityType activity : preferenceTableMap.keySet()) {
            strBuf.append(activity.toString() + '\t');
        }
        strBuf.append('\n');


        for (int i = 0; i < sizeOfIndices; i++) {
            strBuf.append(fromIndex(i) + '\t');
            for (ActivityType activity : preferenceTableMap.keySet()) {
                double preferences[] = preferenceTableMap.get(activity);
                strBuf.append(preferences[i] + "\t");
            }
            strBuf.append('\n');
        }
        return strBuf.toString();
    }

    private double startTimePreference(int hourOfDay, int minuteOfHour, ActivityType activityType) {
        double preferences[] = preferenceTableMap.get(activityType);
        if (preferences == null) {
            throw new RuntimeException("empty preference for " + activityType);
        } else {
            int index = toIndex(hourOfDay, minuteOfHour);
            return preferences[index];
        }
    }

    private int toIndex(int hourOfDay, int minuteOfHour) {
        int minuteOfDay = hourOfDay * 60 + minuteOfHour;
//        logger.debug(minuteOfDay);
        int index = minuteOfDay / timeInterval;
        return index;
    }

    private String fromIndex(int index) {
        int minuteOfDay = index * timeInterval;
        int hour = minuteOfDay / 60;
        int minute = minuteOfDay % 60;
        return hour + ":" + minute;
    }

    public DateTime parseDateTime(String str) {
        /**
         * 1. hh:MM
         *
         * 2. yyyy-mm-dd hh:MM
         *
         */
        DateTime dateTime = null;

        try {
            dateTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(str);
        } catch (IllegalArgumentException e) {
            dateTime = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm").parseDateTime(str);
        }

        return dateTime;
    }


    public static void main(String[] args) {
        PersonalStartTimePreferenceTable table = new PersonalStartTimePreferenceTable("권우영", 30);

        table.addTimePreference(ActivityType.Eat, 0, 10, 0.5);
        table.addTimePreference(ActivityType.Eat, 4, 40, 0.5);
        table.addTimePreference(ActivityType.Meeting, 4, 40, 0.5);
        table.addTimePreference(ActivityType.OutdoorExercise, 4, 40, 0.5);
        logger.debug(ActivityType.Eat);
        logger.debug(table.startTimePreference(4, 30, ActivityType.Eat));
        logger.debug(table.startTimePreference(4, 25, ActivityType.Eat));

//        table.parseDateTime("2015/10/10 11:10");
//        table.addTimePreference(ActivityType.식사, );
        logger.debug(table);

//        logger.debug(table.minuteOfDay(7, 15) + ",\t" + table.toIndex(0, 30));

    }
}


