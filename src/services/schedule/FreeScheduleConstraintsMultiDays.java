package services.schedule;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import wykwon.common.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2015-11-11.
 */
public class FreeScheduleConstraintsMultiDays {
    private static Logger logger = Logger.getLogger(FreeScheduleConstraintsMultiDays.class);


    /**
     * Daily Time에 대해서 몇시부터 몇시까지는 안된다는걸 보이는 constraints
     * 주중에 비는시간 다 고르는 넘
     *
     * @return
     */
    public static List<Pair<DateTime, DateTime>> restrictWeeklyDay(List<Pair<DateTime, DateTime>> freeDailyTimeList, int numberOfDays) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
            retList.addAll(restrictWeeklyDay(freeDailyTime.first(), freeDailyTime.second(), numberOfDays));
        }
        return retList;
    }

    public static List<Pair<DateTime, DateTime>> preferWeekends(List<Pair<DateTime, DateTime>> freeDailyTimeList, int numberOfDays) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
            retList.addAll(preferWeekends(freeDailyTime.first(), freeDailyTime.second(), numberOfDays));
        }
        return retList;
    }

//    /**
//     * Daily Time에 대해서 몇시부터 몇시까지는 안된다는걸 보이는 constraints
//     *
//     * @return
//     */
//    public static List<Pair<DateTime, DateTime>> restrictWeeklyDay(List<Pair<DateTime, DateTime>> freeDailyTimeList) {
//        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
//        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
//            retList.addAll(restrictWeeklyDay(freeDailyTime.first(), freeDailyTime.second()));
//        }
//        return retList;
//    }

    /**
     * 주말낀 날짜로 앞뒤를 확보할것
     *
     * @param startTime
     * @param endTime
     * @param dayDuration
     * @return
     */
    public static List<Pair<DateTime, DateTime>> preferWeekends(DateTime startTime, DateTime endTime, int dayDuration) {

        // 날짜를 하나씩 증가시킬것

        DateTime curStartTime = startTime;
        DateTime curEndTime = curStartTime.plusDays(dayDuration-1);
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        if (dayDuration > 7) {
            logger.fatal("too large");
            return retList;
        }

        while (true) {
            if (containWeekends(curStartTime,curEndTime)==2){
                retList.add(new Pair<DateTime, DateTime>(curStartTime,curEndTime));
            }

            curStartTime = curStartTime.plusDays(1);
            curEndTime = curStartTime.plusDays(dayDuration-1);
            if (curEndTime.isAfter(endTime)) {
                break;
            }
        }
        logger.debug("free days=" + retList);
        return retList;
    }

    /**
     * 7일짜리만 측정함
     * @param startTime
     * @param endTime
     * @return
     */
    private static int containWeekends(DateTime startTime, DateTime endTime) {

        int w1= startTime.getDayOfWeek();
        int w2= endTime.getDayOfWeek();

        if (w1==1){
            if (w2==6){
                return 1;
            }
            else if (w2==7){
                return 2;
            }
            else {
                return 0;
            }

        }
        else if (w1==2){
            if (w2==6){
                return 1;
            }
            else if (w2==7 || w2==1){
                return 2;
            }
            else {
                return 0;
            }

        }
        else if (w1==3){
            if (w2==6){
                return 1;
            }
            else if (w2==7 || w2==1 || w2==3 || w2==2 ){
                return 2;
            }
            else {
                return 0;
            }

        }
        else if (w1==4){
            if (w2==6){
                return 1;
            }
            else if (w2==7 || w2==1 || w2==3 || w2==2 || w2==4){
                return 2;
            }
            else {
                return 0;
            }

        }
        else if (w1==5){
            if (w2==6){
                return 1;
            }
            else {
                return 2;
            }
        }
        else if (w1==6){
                return 2;
        }
        else if (w1==7){
            if (w2==6){
                return 2;
            }
            else {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 시작날짜부터 종료날짜까지 주중을 반환하는데
     * 최소 dayDuration만큼의 날짜가 확보되도록 할것
     *
     * @return
     */
    public static List<Pair<DateTime, DateTime>> restrictWeeklyDay(DateTime startTime, DateTime endTime, int dayDuration) {
        DateTime curTime = startTime;

        boolean oldFlag = isWeekEnd(curTime);// 주말인지 아닌지 확인
        boolean curFlag = isWeekEnd(curTime);// 주말인지 아닌지 확인
        DateTime localStartDay = curTime;

        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        while (true) {
            logger.debug(DateTimeFormatStr.print(curTime));
            if (!oldFlag && curFlag) {
                logger.debug("주말 시작이다.");
                DateTime tmp = curTime.minusDays(1);
                DateTime localEndDay = new DateTime(tmp.getYear(), tmp.getMonthOfYear(), tmp.getDayOfMonth(), 23, 59);
                logger.debug(DateTimeFormatStr.print(localStartDay) + "부터" + DateTimeFormatStr.print(localEndDay) + " 까지가 주중이다");

                if (hasFreeDays(localStartDay, localEndDay, dayDuration)) {
                    DateTime tmpStartTime = localStartDay;
                    DateTime tmpEndTime = localEndDay;
                    if (tmpStartTime.getHourOfDay() >= 9) {
                        tmpStartTime = localStartDay.plusDays(1).minusMinutes(localStartDay.getMinuteOfDay());
                    }
                    if (tmpEndTime.getHourOfDay() <= 19) {
                        tmpEndTime = endTime.minusDays(1).plusMinutes(24 * 60 - endTime.getMinuteOfDay() - 1);
                    }
                    retList.add(new Pair<DateTime, DateTime>(tmpStartTime, tmpEndTime));
                }

            } else if (oldFlag && !curFlag) {
                logger.debug("주말 끝났다.");
                localStartDay = new DateTime(curTime.getYear(), curTime.getMonthOfYear(), curTime.getDayOfMonth(), 0, 0);
            }
            oldFlag = curFlag;// 주말인지 아닌지 확인
            curTime = curTime.plusDays(1);
            curFlag = isWeekEnd(curTime);// 주말인지 아닌지 확인


            if (dayBefore(curTime, endTime)) {
                logger.debug("마지막 날임");
                if (hasFreeDays(localStartDay, endTime, dayDuration)) {
                    logger.debug(dayDuration + "동안 비는시간 \n " + DateTimeFormatStr.print(localStartDay) + "--" + DateTimeFormatStr.print(endTime));

                    DateTime tmpStartTime = localStartDay;
                    DateTime tmpEndTime = endTime;
                    if (tmpStartTime.getHourOfDay() >= 9) {
                        tmpStartTime = localStartDay.plusDays(1).minusMinutes(localStartDay.getMinuteOfDay());
                    }
                    if (tmpEndTime.getHourOfDay() <= 19) {
                        tmpEndTime = endTime.minusDays(1).plusMinutes(24 * 60 - endTime.getMinuteOfDay() - 1);
                    }
                    retList.add(new Pair<DateTime, DateTime>(tmpStartTime, tmpEndTime));

                }
                break;
            }
        }
        logger.debug("free days=" + retList);
        return retList;
    }

    private static boolean isWeekEnd(DateTime dateTime) {
        return dateTime.getDayOfWeek() == 6 || dateTime.getDayOfWeek() == 7;
    }

    public static boolean hasFreeDays(DateTime startTime, DateTime endTime, int numOfDays) {
        logger.debug("has free days \n" + DateTimeFormatStr.print(startTime) + "-->" + DateTimeFormatStr.print(endTime) + " " + numOfDays);
        if (startTime.getYear() == endTime.getYear()) {
            int dayCnt = endTime.getDayOfYear() - startTime.getDayOfYear() + 1;
            if (startTime.getHourOfDay() >= 9) {
                logger.debug("시작시간 " + DateTimeFormatStr.print(startTime));
                dayCnt--;
            }
            if (endTime.getHourOfDay() <= 19) {
                logger.debug("종료시간 " + DateTimeFormatStr.print(endTime));

                dayCnt--;
            }

            return dayCnt >= numOfDays;
        } else {
            throw new RuntimeException("내부오류. 비는 시간이 두해에 걸쳐있음");
        }
    }

    /**
     * 첫번째보다 두번째가 뒤의 날짜인지 확인
     * first보다 second가 뒤인지?
     *
     * @param first
     * @param second
     * @return
     */
    private static boolean dayAfterEqual(DateTime first, DateTime second) {
        logger.debug(first.getYear());
        logger.debug(second.getYear());
        logger.debug(first.getDayOfYear());
        logger.debug(second.getDayOfYear());
        if (first.getYear() == second.getYear() && first.getDayOfYear() == second.getDayOfYear()) {
            return true;
        } else return second.isAfter(first);
    }

    /**
     * 첫번째보다 두번째가 뒤의 날짜인지 확인
     * first보다 second가 앞인지?
     *
     * @param first
     * @param second
     * @return
     */
    private static boolean dayBeforeEqual(DateTime first, DateTime second) {
//        logger.debug(first.getYear());
//        logger.debug(second.getYear());
//        logger.debug(first.getDayOfYear());
//        logger.debug(second.getDayOfYear());
        if (first.getYear() == second.getYear() && first.getDayOfYear() == second.getDayOfYear()) {
            return true;
        } else return second.isBefore(first);
    }

    /**
     * 첫번째보다 두번째가 뒤의 날짜인지 확인
     * first보다 second가 앞인지?
     *
     * @param first
     * @param second
     * @return
     */
    private static boolean dayBefore(DateTime first, DateTime second) {
//        logger.debug(first.getYear());
//        logger.debug(second.getYear());
//        logger.debug(first.getDayOfYear());
//        logger.debug(second.getDayOfYear());
        second = second.plusDays(1);
        if (first.getYear() == second.getYear() && first.getDayOfYear() == second.getDayOfYear()) {
            return true;
        } else return second.isBefore(first);
    }

    /**
     * Daily Time에 대해서 몇시부터 몇시까지는 안된다는걸 보이는 constraints
     *
     * @return
     */
    public static List<Pair<DateTime, DateTime>> restrictWeeklyDay(DateTime startTime, DateTime endTime, int startDayOfWeek, int numberOfDays) {
        List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
        int year = startTime.getYear();
        DateTime firstDayOfYear = new DateTime(year, 1, 1, 0, 0);
        int firstDayOfWeek = firstDayOfYear.getDayOfWeek();
        DateTime firstMondayOfYear = firstDayOfYear.plusDays(-firstDayOfWeek);
        int startWeek = startTime.getWeekOfWeekyear();
        int endWeek = endTime.getWeekOfWeekyear();

        for (int i = startWeek; i <= endWeek; i++) {
            // 음.. xxxx년 yth 주 금요일이 몇일이고?
            DateTime thisMonday = firstMondayOfYear.plusWeeks(i - 1);
            DateTime thisStartDay = thisMonday.plusDays(startDayOfWeek);
            DateTime thisEndDay = thisStartDay.plusDays(numberOfDays);

            if (thisStartDay.dayOfYear().get() >= startTime.dayOfYear().get() && thisEndDay.dayOfYear().get() <= endTime.dayOfYear().get()) {
                retList.add(new Pair<DateTime, DateTime>(thisStartDay, thisEndDay));

            }


//  이전꺼
//            if (( thisStartDay.isAfter(startHour) && thisEndDay.isBefore(endHour)) {
//                retList.add(new Pair<DateTime, DateTime>(thisStartDay, thisEndDay));
//
//            }
        }
//        for (Pair<DateTime, DateTime> freeDailyTime : freeDailyTimeList) {
//            Pair<DateTime, DateTime> ret = restrictDailyTime(freeDailyTime.first(), freeDailyTime.second(), startHour, startMin, endHour, endMin);
//            retList.add(ret);
//        }
//        logger.debug(retList);
        return retList;
    }

    public static void test() {

//        boolean ret= dayAfterEqual(DateTimeFormatStr.parseFullDateTime("2016-1-2 13:00"), DateTimeFormatStr.parseFullDateTime("2016-1-3 9:00"));
//        logger.debug(ret);
//
//        ret= dayBeforeEqual(DateTimeFormatStr.parseFullDateTime("2016-1-2 13:00"), DateTimeFormatStr.parseFullDateTime("2016-1-3 9:00"));
//        logger.debug(ret);
//        logger.debug(restrictWeeklyDay(DateTimeFormatStr.parseFullDateTime("2016-2-2 13:00"), DateTimeFormatStr.parseFullDateTime("2016-2-25 9:00"), 3));

        logger.debug(preferWeekends(DateTimeFormatStr.parseFullDateTime("2016-4-1 00:00"), DateTimeFormatStr.parseFullDateTime("2016-4-20 9:00"), 3));
    }

    public static void main(String[] args) {
        test();
//        List<Pair<DateTime, DateTime>> freeTimeList = new ArrayList<Pair<DateTime, DateTime>>();
//        freeTimeList.add(new Pair<DateTime, DateTime>(new DateTime(2015, 12, 1, 00, 00), new DateTime(2015, 12, 31, 00, 00)));
//
//        restrictWeeklyDay(new DateTime(2015, 11, 15, 00, 00), new DateTime(2015, 12, 31, 00, 00), 5, 3);
    }
}
