package edu.hanyang.trip_planning.tripData.dataType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 19
 * Time: 오후 5:30
 * To change this template use File | Settings | File Templates.
 */
public class ClosingDays {
    private Set<DayOfWeek> weeklyClosingDays = new HashSet<>();
    private Set<DayOfWeekInMonth> monthlyClosingDayOfWeeks = new HashSet<>();
    private Set<String> yearlyClosingDays = new HashSet<>();


    public void addWeeklyClosingDay(DayOfWeek dayOfWeek) {
        weeklyClosingDays.add(dayOfWeek);
    }


    public void addMonthlyClosingDay(int weekOfMonth, DayOfWeek dayOfWeek) {
        monthlyClosingDayOfWeeks.add(new DayOfWeekInMonth(weekOfMonth, dayOfWeek));
    }

    public void addYearlyClosingDay(String dayOfYear) {
        yearlyClosingDays.add(dayOfYear);
    }

    public Set<DayOfWeek> getWeeklyClosingDays() {
        return weeklyClosingDays;
    }

    public Set<DayOfWeekInMonth> getMonthlyClosingDays() {
        return monthlyClosingDayOfWeeks;
    }

    public Set<String> getYearlyClosingDays() {
        return yearlyClosingDays;
    }

    public ClosingDays deepCopy() {
        ClosingDays newClosingDays = new ClosingDays();

        for (DayOfWeek dayOfWeek : weeklyClosingDays) {
            newClosingDays.addWeeklyClosingDay(dayOfWeek);
        }
        for (DayOfWeekInMonth dayOfWeekInMonth : monthlyClosingDayOfWeeks) {
            newClosingDays.addMonthlyClosingDay(dayOfWeekInMonth.weekOfMonth, dayOfWeekInMonth.dayOfWeek);
        }
        for (String yearlyClosingDay : yearlyClosingDays) {
            newClosingDays.addYearlyClosingDay(yearlyClosingDay);
        }

        return newClosingDays;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        if (weeklyClosingDays.size() != 0) {
            strBuf.append(weeklyClosingDays + "\n");
        }
        if (monthlyClosingDayOfWeeks.size() != 0) {
            strBuf.append(monthlyClosingDayOfWeeks + "\n");
        }
        if (yearlyClosingDays.size() != 0) {
            strBuf.append(yearlyClosingDays + "\n");
        }

        return strBuf.toString();
    }

    public static ClosingDays parse(String strs[]) {
        ClosingDays closingDays = new ClosingDays();
        for (String str : strs) {
            if (str.equals("매주 일요일") || str.equals("격주 일요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Sunday);
            } else if (str.equals("매주 월요일") || str.equals("격주 월요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Monday);
            } else if (str.equals("매주 화요일") || str.equals("격주 화요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Tuesday);
            } else if (str.equals("매주 수요일") || str.equals("격주 수요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Wednesday);
            } else if (str.equals("매주 목요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Thursday);
            } else if (str.equals("매주 금요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Friday);
            } else if (str.equals("매주 토요일")) {
                closingDays.addWeeklyClosingDay(DayOfWeek.Saturday);
            } else if (str.equals("매주 1,3주 화요일")) {
                closingDays.addMonthlyClosingDay(1, DayOfWeek.Tuesday);
                closingDays.addMonthlyClosingDay(3, DayOfWeek.Tuesday);
            } else if (str.equals("2,4주 월요일")) {
                closingDays.addMonthlyClosingDay(2, DayOfWeek.Monday);
                closingDays.addMonthlyClosingDay(4, DayOfWeek.Monday);
            } else if (str.equals("둘째주 화요일")) {
                closingDays.addMonthlyClosingDay(2, DayOfWeek.Tuesday);
            } else if (str.equals("공휴일")) {
                closingDays.addYearlyClosingDay("공휴일");
            } else if (str.equals("명절")) {
                closingDays.addYearlyClosingDay("명절");
            } else if (str.equals("명절당일")) {
                closingDays.addYearlyClosingDay("명절당일");
            } else if (str.equals("설날 및 추석연휴")) {
                closingDays.addYearlyClosingDay("설날 및 추석연휴");
            } else if (str.equals("연중무휴")) {
                // NOOP
            } else if (str.equals("일,공휴일은 오후 2시이후 전화확인")) {
                // NOOP
            } else if (str.equals("명절 당일")) {
                closingDays.addYearlyClosingDay("설날 및 추석연휴");
            } else if (str.equals("1월1일")) {
                closingDays.addYearlyClosingDay("1월1일");
            } else if (str.equals("설")) {
                closingDays.addYearlyClosingDay("설날 및 추석연휴");
            } else if (str.equals("추석") || str.equals("추석 휴무")) {
                closingDays.addYearlyClosingDay("추석");
            } else {
                closingDays.addYearlyClosingDay(str);
            }
        }
        return closingDays;
    }

    public static void test() {
        ClosingDays closingDays = new ClosingDays();
        closingDays.addMonthlyClosingDay(1, DayOfWeek.Sunday);
        closingDays.addMonthlyClosingDay(3, DayOfWeek.Sunday);
//        closingDays.addWeeklyClosingDay(DayOfWeek.Sunday);
        System.out.println("closingDays = " + closingDays);
    }

    public static void main(String[] args) {
        test();
    }

}
