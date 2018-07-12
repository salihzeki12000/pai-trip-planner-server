package edu.hanyang.trip_planning.tripData.dataType;



/**
 * 개인일정
 */
public class PersonalScheduleEntry {

    /**
     * 약속장소 InterfacePOI identifier
     */
    public String locationName;

    /**
     * 일정의 시작시간
     */
    public String startTime;



    /**
     * 일정의 기간
     */
    public ProbabilisticDuration duration;

    /**
     * 일정의 종류
     */
    public String activityType;

    public PersonalScheduleEntry(String poiTitle, String startTime, ProbabilisticDuration duration, String activityType) {
        this.locationName = poiTitle;
        this.startTime = startTime;
        this.duration = duration;
        this.activityType = activityType;
    }

    @Override
    public String toString() {
        if (duration.hour<1.5) {
            return locationName + "\t" + startTime + "," + (int) (duration.hour * 60.0) + "분," + activityType+"약속";
        }
        else if (duration.hour<24) {
            return locationName + "\t" + startTime + "," + (int) (duration.hour ) + "시간," + activityType+"약속";
        }
        else  {
            return locationName + "\t" + startTime + "," + (int) (duration.hour/24 ) + "일," + activityType+"약속";
        }

//        if (activityType == ActivityType.Unknown) {
//            return startTime + "부터 " + duration + "동안 " + locationName + "에서 " + activityType + "일정이 있다.";
//        } else {
//            return startTime + "부터 " + duration + "동안 " + locationName + "에서 " + activityType + "을 한다.";
//        }
    }




}
