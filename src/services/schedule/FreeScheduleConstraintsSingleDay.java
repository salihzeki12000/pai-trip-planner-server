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
 * Created by wykwon on 2015-11-11.
 */
public class FreeScheduleConstraintsSingleDay {
    private static Logger logger = Logger.getLogger(FreeScheduleConstraintsSingleDay.class);

    public enum ConstraintType {Less, Greater}

    private static DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");


    /**
     * 여러날에 걸친 일정을 매일 매일의 일정으로 변환한다.
     * 00:00 부터 23:59 까지로 바꾼다.
     * 최소단 위는 분
     *
     * @param freeTimeList
     * @return
     */
    public static List<Pair<DateTime, DateTime>> spiltDailyTime(List<Pair<DateTime, DateTime>> freeTimeList) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> freeTime : freeTimeList) {
            DateTime freeStart = freeTime.first();
            DateTime freeEnd = freeTime.second();
            int startYear = freeStart.getYear();
            int startMonth = freeStart.getMonthOfYear();
            int startDay = freeStart.getDayOfMonth();
            int endYear = freeEnd.getYear();
            int endMonth = freeEnd.getMonthOfYear();
            int endDay = freeEnd.getDayOfMonth();

            if (startDay != endDay) {
                Pair<DateTime, DateTime> firstDay = new Pair<DateTime, DateTime>(freeStart, new DateTime(startYear, startMonth, startDay, 23, 59));
                retList.add(firstDay);

                for (int i = 1; i < (endDay - startDay); i++) {
                    DateTime dailyStart = new DateTime(startYear, startMonth, startDay, 0, 1);
                    DateTime dailyEnd = new DateTime(startYear, startMonth, startDay, 23, 59);
                    dailyStart = dailyStart.plusDays(i);
                    dailyEnd = dailyEnd.plusDays(i);
                    Pair<DateTime, DateTime> eachDay = new Pair<DateTime, DateTime>(dailyStart, dailyEnd);
                    retList.add(eachDay);
                }
                Pair<DateTime, DateTime> lastDay = new Pair<DateTime, DateTime>(new DateTime(endYear, endMonth, endDay, 00, 01), freeEnd);
                retList.add(lastDay);

//                logger.debug("시작과 끝 날짜가 달라요. 짤라냅시다. " + dateTimeFormat.print(freeStart) + " -- " + dateTimeFormat.print(freeEnd));
//                logger.debug(retList);
            } else {
                retList.add(freeTime);
            }
        }
        return retList;
    }


    // 1. 여유시간이 몇시간이상,
    public static List<Pair<DateTime, DateTime>> durationConstraint(List<Pair<DateTime, DateTime>> freeDailyTimeList, int duration_hour, int duration_min) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> pair : freeDailyTimeList) {
            DateTime startTime = new DateTime(pair.first());
            DateTime endTime = new DateTime(pair.second());
            DateTime refTime = startTime.plusHours(duration_hour).plusMinutes(duration_min);
            // 특정 시간간격보다 커야 함. 예: 1시간 이상
            // duration시간을 더한 기준시간이 종료시간 보다 앞서야 한다.
            if (refTime.isBefore(endTime)) {
                // constrain에 맞음
                retList.add(pair);
            }
        }
        return retList;
    }


    /**
     * 특정 요일만 포함되도록 제한함
     *
     * @param freeDailyTimeList
     * @param dayOfWeeks        월요일 1, 화요일 2, ... ,일요일 7
     */
    public static List<Pair<DateTime, DateTime>> restrictWeekDay(List<Pair<DateTime, DateTime>> freeDailyTimeList, int... dayOfWeeks) {

        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
            int dayOfWeek = freeDailyTime.first().getDayOfWeek();
            for (int ref : dayOfWeeks) {
                if (ref == dayOfWeek) {
                    retList.add(freeDailyTime);
                }
            }
        }
        return retList;
    }

    /**
     * Daily Time에 대해서 몇시부터 몇시까지는 안된다는걸 보이는 constraints
     *
     * @return
     */
    public static List<Pair<DateTime, DateTime>> restrictDailyTime(List<Pair<DateTime, DateTime>> freeDailyTimeList, int startHour, int startMin, int endHour, int endMin) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
            Pair<DateTime, DateTime> ret = restrictDailyTime(freeDailyTime.first(), freeDailyTime.second(), startHour, startMin, endHour, endMin);
            retList.add(ret);
        }
        return retList;
    }


    // 1. 여유시간의 시작과 끝이, 제한 구간에 포함되어야 함
    public static Pair<DateTime, DateTime> restrictDailyTime(DateTime freeStart, DateTime freeEnd, int startHour, int startMin, int endHour, int endMin) {
        int cs = startHour * 60 + startMin;
        int ce = endHour * 60 + endMin;
        int fs = freeStart.minuteOfDay().get();
        int fe = freeEnd.minuteOfDay().get();
        int year = freeStart.getYear();
        int monthOfYear = freeStart.getMonthOfYear();
        int dayOfMonth = freeStart.getDayOfMonth();
        int retHourStart, retHourEnd, retMinuteStart, retMinuteEnd;
        if (cs < fs) {
            if (ce < fe) {
//                logger.debug("fs,ce");
                retHourStart = fs / 60;
                retMinuteStart = fs % 60;
                retHourEnd = ce / 60;
                retMinuteEnd = ce % 60;

            } else {
//                logger.debug("fs,fe");
                retHourStart = fs / 60;
                retMinuteStart = fs % 60;
                retHourEnd = fe / 60;
                retMinuteEnd = fe % 60;
            }

        } else {
            if (ce < fe) {
//                logger.debug("cs,ce");
                retHourStart = cs / 60;
                retMinuteStart = cs % 60;
                retHourEnd = ce / 60;
                retMinuteEnd = ce % 60;

            } else {
//                logger.debug("cs,fe");
                retHourStart = cs / 60;
                retMinuteStart = cs % 60;
                retHourEnd = fe / 60;
                retMinuteEnd = fe % 60;
            }
        }
        DateTime newStart = new DateTime(year, monthOfYear, dayOfMonth, retHourStart, retMinuteStart);
        DateTime newEnd = new DateTime(year, monthOfYear, dayOfMonth, retHourEnd, retMinuteEnd);
        return new Pair<DateTime, DateTime>(newStart, newEnd);
    }


    public static String dump(List<Pair<DateTime, DateTime>> pairList) {
        StringBuffer strbuf = new StringBuffer();

        for (Pair<DateTime, DateTime> pair : pairList) {
            strbuf.append("[" + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(pair.first()) + " ~~ " + DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(pair.second()) + "]\n");
        }
        return strbuf.toString();
    }


}
