package edu.hanyang.trip_planning.tripData.dataType;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 개인일정
 */
public class PersonalScheduleEntryJoda {

    /**
     * 약속장소 InterfacePOI identifier
     */
    public String locationName;

    /**
     * 일정의 시작시간
     */
    public DateTime startTime;



    /**
     * 일정의 기간
     */
    public ProbabilisticDuration duration;

    /**
     * 일정의 종류
     */
    public String activityType;
    private DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    public PersonalScheduleEntryJoda(String poiTitle, String startTimeStr, ProbabilisticDuration duration, String activityType) {
        this.locationName = poiTitle;
        this.startTime = dateTimeFormat.parseDateTime(startTimeStr);
        this.duration = duration;
        this.activityType = activityType;
    }

    public PersonalScheduleEntryJoda(PersonalScheduleEntry personalScheduleEntry) {
        this.locationName = personalScheduleEntry.locationName;
        this.startTime = dateTimeFormat.parseDateTime( personalScheduleEntry.startTime);
        this.duration = personalScheduleEntry.duration;
        this.activityType = personalScheduleEntry.activityType;
    }
    @Override
    public String toString() {
        return null;
//        if (activityType == ActivityType.Unknown) {
//            return startTime + "부터 " + duration + "동안 " + locationName + "에서 " + activityType + "일정이 있다.";
//        } else {
//            return startTime + "부터 " + duration + "동안 " + locationName + "에서 " + activityType + "을 한다.";
//        }
    }

    public static PersonalScheduleEntryJoda dummy() {
        return null;
//        return new PersonalScheduleEntryJoda(DummyPOIIdentifiers.getHanyang().getTitle(), "2015-06-08 13:00", new ProbabilisticDuration(120, 10), ActivityType.Meeting);
    }

    public static void test() {
        System.out.println("dummy() = " + dummy());
    }

    public static void main(String[] args) {
        test();
    }
}
