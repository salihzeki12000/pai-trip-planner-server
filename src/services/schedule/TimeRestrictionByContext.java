package services.schedule;


import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import services.datatype.PersonalScheduleEntryJoda;
import wykwon.common.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 상황에 따라 다른 시간적 제약조건의 결과를 구한다.
 */
public class TimeRestrictionByContext {
    private static Logger logger = Logger.getLogger(TimeRestrictionByContext.class);
    /**
     *     //    SingleEvent("단일일정"),
     SingleEventEat("식사"), SingleEventExercise("운동"), SingleEventMeeting("회의"), SingleEventSightSeeing("관광"),
     SingleEventStroll("산책"), SingleEventShow("공연"), SingleEventShopping("쇼핑"), SingleEventWork("일"),
     SingleEventRestOrSleep("휴식/잠"),

     Travel("여행"),

     OnedayTravel("당일여행"), MultidayTravel("여행"),
     SoloTravel("혼자가는여행"), FamilyTravel("가족여행"), ParentTravel("효도관광"), CoupleTravel("커플여행"), GroupTravel("단체여행"),

     OnedaySoloTravel("당일,혼자가는여행"), OnedayFamilyTravel("당일,가족여행"), OnedayarentTravel("당일,효도관광"), OnedayCoupleTravel("당일,커플여행"), OnedayGroupTravel("당일,단체여행"),
     MultidaySoloTravel("여러날,혼자가는여행"), MultidayFamilyTravel("여러날,가족여행"), MultidayParentTravel("여러날,효도관광"), MultidayCoupleTravel("여러날,커플여행"), MultidayGroupTravel("여러날,단체여행"),


     BusinessTrip("출장"),
     OnedayBusinessTrip("당일출장"), MultidayBusinessTrip("여러날출장"),

     CompositeTrip("여러날복합일정");


     일단 보자고,

     식사, 회의

     */


    /**
     * 적절한 회의시간을 잡아보자
     *
     * @param freeTimeList
     * @return
     */
    public static List<Pair<DateTime, DateTime>> businessMeetingLong(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> weekhour = FreeScheduleConstraintsSingleDay.restrictWeekDay(freeDailyTimeList, 1, 2, 3, 4, 5);
        List<Pair<DateTime, DateTime>> workhours = FreeScheduleConstraintsSingleDay.restrictDailyTime(weekhour, 9, 0, 18, 0);
        List<Pair<DateTime, DateTime>> morethanthreehours = FreeScheduleConstraintsSingleDay.durationConstraint(workhours, 6, 0);
        return morethanthreehours;
        /**
         *  당연히 주중이고,
         *  시간은 9시부터 18시까지 (업무시간내)
         *  2시간 이상.
         *  식사시간은 빼자
         */
    }

    public static List<Pair<DateTime, DateTime>> businessMeeting(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> weekhour = FreeScheduleConstraintsSingleDay.restrictWeekDay(freeDailyTimeList, 1, 2, 3, 4, 5);
        List<Pair<DateTime, DateTime>> morning = FreeScheduleConstraintsSingleDay.restrictDailyTime(weekhour, 9, 0, 12, 0);
        List<Pair<DateTime, DateTime>> afternoon = FreeScheduleConstraintsSingleDay.restrictDailyTime(weekhour, 13, 15, 18, 0);
        List<Pair<DateTime, DateTime>> workday = new ArrayList<Pair<DateTime, DateTime>>();
        workday.addAll(morning);
        workday.addAll(afternoon);


        List<Pair<DateTime, DateTime>> morethanthreehour = FreeScheduleConstraintsSingleDay.durationConstraint(workday, 2, 0);
        return morethanthreehour;
        /**
         *  당연히 주중이고,
         *  시간은 9시부터 18시까지 (업무시간내)
         *  2시간 이상.
         */
    }

    public static List<Pair<DateTime, DateTime>> businessMeetingShort(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> weekdays = FreeScheduleConstraintsSingleDay.restrictWeekDay(freeDailyTimeList, 1, 2, 3, 4, 5);
        List<Pair<DateTime, DateTime>> workingdays = FreeScheduleConstraintsSingleDay.restrictDailyTime(weekdays, 9, 0, 18, 0);
        List<Pair<DateTime, DateTime>> morethanthreehour = FreeScheduleConstraintsSingleDay.durationConstraint(workingdays, 1, 30);
        return morethanthreehour;
        /**
         *  당연히 주중이고,
         *  시간은 9시부터 18시까지 (업무시간내)
         *  2시간 이상.
         */
    }

    public static List<Pair<DateTime, DateTime>> lunch(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> lunchdays = FreeScheduleConstraintsSingleDay.restrictDailyTime(freeDailyTimeList, 11, 50, 13, 30);
        List<Pair<DateTime, DateTime>> result = FreeScheduleConstraintsSingleDay.durationConstraint(lunchdays, 1, 30);
        return result;
    }

    public static List<Pair<DateTime, DateTime>> dinner(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> dinnerhour = FreeScheduleConstraintsSingleDay.restrictDailyTime(freeDailyTimeList, 17, 50, 20, 30);
        List<Pair<DateTime, DateTime>> result = FreeScheduleConstraintsSingleDay.durationConstraint(dinnerhour, 1, 30);
        return result;
    }

    public static List<Pair<DateTime, DateTime>> eat(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> freeDailyTimeList = FreeScheduleConstraintsSingleDay.spiltDailyTime(freeTimeList);
        List<Pair<DateTime, DateTime>> lunchhour = FreeScheduleConstraintsSingleDay.restrictDailyTime(freeDailyTimeList, 11, 50, 13, 30);
        List<Pair<DateTime, DateTime>> dinnerhour = FreeScheduleConstraintsSingleDay.restrictDailyTime(freeDailyTimeList, 17, 50, 20, 30);
        List<Pair<DateTime, DateTime>> eayhour = new ArrayList<Pair<DateTime, DateTime>>();
        eayhour.addAll(lunchhour);
        eayhour.addAll(dinnerhour);
        List<Pair<DateTime, DateTime>> result = FreeScheduleConstraintsSingleDay.durationConstraint(eayhour, 1, 30);
        return result;
    }

    public static List<Pair<DateTime, DateTime>> travel(List<Pair<DateTime, DateTime>> freeTimeList, int day) {

        List<Pair<DateTime, DateTime>> fridayToSunday = FreeScheduleConstraintsMultiDays.restrictWeeklyDay(freeTimeList,day);
        return fridayToSunday;
    }



}
