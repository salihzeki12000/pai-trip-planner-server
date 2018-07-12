package edu.hanyang.protocol;


import java.util.*;

/**
 * 여러사람의 일정들
 */
public class WellFormedAnswer {

    //    private String personName;
    private String startLocation;
    private String endLocation;
    private String startTime;
    private String endTime;
    private ScheduleType scheduleType;
    List<PersonalScheduleEntry> detailedScheduleList = new ArrayList<PersonalScheduleEntry>();

    private String fullText;
    private String briefText;


    public WellFormedAnswer(String startLocation, String endLocation, String startTime, String endTime, ScheduleType scheduleType) {
//        this.personName = name;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.scheduleType = scheduleType;
    }


    public void addDetailedSchedule(PersonalScheduleEntry personalScheduleEntry) {
        detailedScheduleList.add(personalScheduleEntry);
    }

    public void addDetailedSchedule(String poiTitle, String startTime, ProbabilisticDuration duration, ActivityType activityType) {
        PersonalScheduleEntry entry = new PersonalScheduleEntry(poiTitle, startTime, duration, activityType);
        detailedScheduleList.add(entry);
    }

    public void generateText() {
        this.fullText = toString();
        this.briefText = toString();
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
//        if (Jaso.lastCharJongSung(personName)){
//            strbuf.append(personName + "이 ");
//        }
//        else {
//            strbuf.append(personName + "가 ");
//        }

        if (startLocation.equals(endLocation)) {
            strbuf.append(startLocation + "에서 ");
            strbuf.append(startTime + "부터 ");
            strbuf.append(endTime + "까지 ");
            if (Jaso.lastCharJongSung(scheduleType.toString())) {
                strbuf.append(scheduleType + "을 한다. ");
            } else {
                strbuf.append(scheduleType + "를 한다. ");
            }
        } else {
            strbuf.append(startLocation + "에서 ");
            strbuf.append(startTime + "에 시작해서 ");
            if (Jaso.lastCharJongSung(scheduleType.toString())) {
                strbuf.append(scheduleType + "을 하고 ");
            } else {
                strbuf.append(scheduleType + "를 하고 ");
            }
            strbuf.append(endLocation + "에 ");
            strbuf.append(endTime + "에 도착한다. ");
        }

        return strbuf.toString();
    }

    public String toDetailedString() {
        StringBuffer strBuf = new StringBuffer();

        strBuf.append(toString()+'\n');

        int i=1;
        for (PersonalScheduleEntry entry : detailedScheduleList) {
            strBuf.append( (i++) + ". "+entry + "\n");
        }
        return strBuf.toString();

    }

    public static void test() {
        WellFormedAnswer basicAnswer1 = new WellFormedAnswer("아웃벡서현점", "아웃벡서현점", "2015-11-07 18:00", "2015-11-07 20:00", ScheduleType.SingleEventEat);
        WellFormedAnswer basicAnswer2 = new WellFormedAnswer("제주도", "제주도", "2015-11-07 13:00", "2015-11-31 20:00", ScheduleType.SoloTravel);
        basicAnswer2.addDetailedSchedule("쇠소깍", "2015-11-07 16:00",new ProbabilisticDuration(2,0.2),ActivityType.Outdoor);

    }

    public static void main(String[] args) {
        test();
    }
}

