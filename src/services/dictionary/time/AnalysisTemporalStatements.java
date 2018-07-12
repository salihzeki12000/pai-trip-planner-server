package services.dictionary.time;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import services.dictionary.InterfaceDictionary;
import services.dummy.ServiceManager;
import services.schedule.DateTimeFormatStr;
import wykwon.common.Pair;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 2. 13
 * Time: 오후 3:59
 * To change this template use File | Settings | File Templates.
 */
public class AnalysisTemporalStatements {

    /**
     * 임시규칙 1
     * <p/>
     * 시작시간은 무조건적으로 현재 (대책이 안나옴)
     * 종료시간은 default 1주
     * 그외에는 여기 잡힌데로.
     */
    private static Logger logger = Logger.getLogger(AnalysisTemporalStatements.class);

    private String startTime;
    private String endTime;
//    Map<String, String> statements;

    String today[] = {"오늘"};
    String thisWeekdays[] = {"이번주", "금주"};
    String thisWeekend[] = {"주말", "이번주말", "이번 주말"};
    String nextWeekdays[] = {"다음주"};
    String nextWeekend[] = {"다음주말", "다음 주말"};
    String thisMonth[] = {"이번달"}; // 이번달 - 오늘부터 이번달 말일까지
    String nextMonth[] = {"다음달"}; // 이번달 - 오늘부터 이번달 말일까지

    String oneWeek[] = {"한주", "1주"};
    String twoWeek[] = {"2주", "이주"};
    String tenDays[] = {"10일", "열흘"};
    String threeWeek[] = {"3주", "삼주"};
    String oneMonth[] = {"1달", "한달"}; //    지금부터 30일 뒤

    DateTime fakeTime = null;
    String questioner;

    public AnalysisTemporalStatements(String questioner) {
        this.questioner = questioner;

    }

    public AnalysisTemporalStatements(String questioner, String fakeToday) {
        this.questioner = questioner;
        DateTime fakeDay = DateTimeFormatStr.parseDate(fakeToday);
        DateTime dateTime = new DateTime();
        this.fakeTime = new DateTime(fakeDay.getYear(), fakeDay.getMonthOfYear(), fakeDay.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());

    }

    public void setFakeTime(String fakeToday) {
        DateTime fakeDay = DateTimeFormatStr.parseDate(fakeToday);
        DateTime dateTime = new DateTime();
        this.fakeTime = new DateTime(fakeDay.getYear(), fakeDay.getMonthOfYear(), fakeDay.getDayOfMonth(), dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
    }

//    public AnalysisTemporalStatements(Map<String, String> statements) {
//        this.statements = statements;
//
//        if (statements == null || statements.keySet().size() == 0) {
//            defaultSetting();
//        } else {
//            analysis();
//        }
//    }


    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    private void defaultSetting() {
        // 지금부터 1주이내.
        DateTime dateTime_start = new DateTime();
        DateTime dateTime_end = dateTime_start.plusWeeks(1);
        startTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_start);    // 지금
        endTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_end);        // 지금부터 1주일후

    }
//
//    private void analysis() {
//
////        statements 분석
//
//        DateTime dateTime_start = new DateTime();
////        startHour = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_start);    // 지금
//        String endTimeStr = statements.get("endHour");
//        String retStr[] = analysisDays(endTimeStr);
//        startTime = retStr[0];
//        endTime = retStr[1];
////        defaultSetting();
//    }

    private String[] analysisDays(String endTimeStr) {
        /**
         *    String thisWeek[] = {"이번주", "금주"};
         String thisWeekend[] = {"주말", "이번주말", "이번 주말"};
         String nextWeek[] = {"다음주"};
         String oneWeek[] = {"한주", "1주"};
         String twoWeek[] = {"2주", "이주"};
         String tenDays[] = {"10일", "열흘"};
         String threeWeek[] = {"3주", "삼주"};
         String oneMonth[] = {"1달", "한달"}; //    지금부터 30일 뒤
         String thisMonth[] = {"이번달"}; // 이번달 - 오늘부터 이번달 말일까지

         */
        if (containSubstr(endTimeStr, thisWeekdays)) {
            return getThisWeekDays();
        } else if (containSubstr(endTimeStr, thisWeekend)) {
            return getThisWeekend();
        } else if (containSubstr(endTimeStr, nextWeekdays)) {
            return getNextWeekDays();
        } else if (containSubstr(endTimeStr, nextWeekend)) {
            return getNextWeekends();
        } else if (containSubstr(endTimeStr, thisMonth)) {
            return getThisMonth();
        } else if (containSubstr(endTimeStr, nextMonth)) {
            return getNextMonthStr();
        }

//        if (containSubstr(endTimeStr,oneWeek)){
//            return 7;
//        }
//        if ()

        DateTime dateTime_start = new DateTime();
        DateTime dateTime_end = dateTime_start.plusWeeks(1);
        String retStr[] = new String[2];
        retStr[0] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_start);    // 지금
        retStr[1] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_end);        // 지금부터 1주일후
        return retStr;

    }


    private static boolean containSubstr(String str, String... strArray) {
        for (String fullStr : strArray) {
            if (fullStr.indexOf(str) >= 0) {
                return true;
            }
        }
        return false;
    }

    public String[] getThisWeek() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];

        if (today.getDayOfWeek() < 6) {
            int end = 5 - today.getDayOfWeek();
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end + 2));
        } else {
            int end = 5 - today.getDayOfWeek() + 7;
            // 월요일부터
            if (today.getDayOfWeek() == 6) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(2));
            } else if (today.getDayOfWeek() == 7) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(1));
            }
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end + 2));
        }
        return days;
    }

    public String[] getThisWeekend() {
//        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(dateTime_start)
        LocalDate today = new LocalDate();
        today = today.plusDays(5);
        String days[] = new String[2];
        int start = (6 - today.getDayOfWeek());
        int end = start + 1;
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(start));
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        return days;
    }

    /**
     * 이번주중이 언제부터 언제까지냐?
     * <p/>
     * <p/>
     * 오늘부터 이번주 금요일
     * <p/>
     * 물어볼때가 주말이면? -> 다음주로 생각하자
     *
     * @return
     */
    public String[] getThisWeekDays() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];

        if (today.getDayOfWeek() < 6) {
            int end = 5 - today.getDayOfWeek();
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        } else {
            int end = 5 - today.getDayOfWeek() + 7;
            // 월요일부터
            if (today.getDayOfWeek() == 6) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(2));
            } else if (today.getDayOfWeek() == 7) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(1));
            }
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        }
        return days;
    }

    public String[] getNextTwoWeekDays() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];

        if (today.getDayOfWeek() < 6) {
            int end = 7 + 5 - today.getDayOfWeek();
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        } else {
            int end = 7 + 5 - today.getDayOfWeek() + 7;
            // 월요일부터
            if (today.getDayOfWeek() == 6) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(2));
            } else if (today.getDayOfWeek() == 7) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(1));
            }
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        }
        return days;
    }

    public String[] getNextThreeWeekDays() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];

        if (today.getDayOfWeek() < 6) {
            int end = 14 + 5 - today.getDayOfWeek();
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        } else {
            int end = 14 + 5 - today.getDayOfWeek() + 7;
            // 월요일부터
            if (today.getDayOfWeek() == 6) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(2));
            } else if (today.getDayOfWeek() == 7) {
                days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(1));
            }
            days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        }
        return days;
    }

    /**
     * 다음주 주중은?
     * <p/>
     * 다음주 월요일부터 다음주 금요일까지
     *
     * @return
     */
    public String[] getNextWeekDays() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];
        int start = 0, end = 0;

        switch (today.getDayOfWeek()) {
            case 1:
                start = 7;
                break;

            case 2:
                start = 6;
                break;
            case 3:
                start = 5;
                break;

            case 4:
                start = 4;
                break;

            case 5:
                start = 3;
                break;

            case 6:
                start = 2;
                break;

            case 7:
                start = 1;
                break;

        }
        end = start + 4;
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(start));
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        return days;
    }

    public String[] getNextWeekends() {
        LocalDate today = new LocalDate();
        String days[] = new String[2];
        int start = 0, end = 0;

        switch (today.getDayOfWeek()) {
            case 1:
                start = 12;
                break;
            case 2:
                start = 11;
                break;
            case 3:
                start = 10;
                break;
            case 4:
                start = 9;
                break;
            case 5:
                start = 8;
                break;
            case 6:
                start = 7;
                break;
            case 7:
                start = 6;
                break;
        }
        end = start + 1;
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(start)) + "00:01";
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end)) + "23:59";
        return days;
    }

    public String[] getThisMonth() {
        LocalDate today = new LocalDate();
        today.getDayOfMonth();
        String days[] = new String[2];
        int endDaysOfMonth[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int end = endDaysOfMonth[today.getMonthOfYear()] - today.getDayOfMonth();
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        return days;
    }


    public String[] getNextMonthStr() {
        LocalDate today = new LocalDate();
        today.getDayOfMonth();
        String days[] = new String[2];
        int endDaysOfMonth[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int monthIdx = today.getMonthOfYear() + 1; // 다음달 숫자

        LocalDate nextMonthDay = today.plusMonths(1);
        int year = nextMonthDay.getYear();
        int month = nextMonthDay.getMonthOfYear();
        int startDay = 1;
        int endDay = endDaysOfMonth[month];

        days[0] = year + "-" + month + "-" + startDay + " 00:01";
        days[1] = year + "-" + month + "-" + endDay + " 23:59";
        //        int end = endDaysOfMonth[+1]-today.getDayOfMonth();
//        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
//        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        return days;
    }

    public Pair<DateTime, DateTime> getNextMonth() {
        LocalDate today = new LocalDate();

        today.getDayOfMonth();
//        String days[] = new String[2];
        int endDaysOfMonth[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int monthIdx = today.getMonthOfYear() + 1; // 다음달 숫자

        LocalDate nextMonthDay = today.plusMonths(1);
        int year = nextMonthDay.getYear();
        int month = nextMonthDay.getMonthOfYear();
        int startDay = 1;
        int endDay = endDaysOfMonth[month];

//        days[0] = year + "-" + month + "-" + startDay;
//        days[1] = year + "-" + month + "-" + endDay;
        DateTime startTime = new DateTime(year, month, startDay, 0, 0);
        DateTime endTime = new DateTime(year, month, startDay, 23, 59);
//        int end = endDaysOfMonth[+1]-today.getDayOfMonth();
//        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today);
//        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd").print(today.plusDays(end));
        return new Pair<DateTime, DateTime>(startTime, endTime);
    }

    public String[] getToday() {
        DateTime curDateTime = new DateTime();
        String days[] = new String[2];
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(curDateTime);
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "23:59";
        return days;
    }

    /**
     * 오늘 5시 반 이후
     *
     * @return
     */
    public String[] getTodayEvening() {
        DateTime curDateTime = new DateTime();
        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        String days[] = new String[2];
        double curHour = curDateTime.getHourOfDay() + curDateTime.getMinuteOfHour() / 60.0;
        if (curHour > 17.5) {
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(curDateTime);
        } else {
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "17:30";
        }

        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "23:59";
        return days;
    }

    /**
     * 오늘 11시 30 이후 ~ 19:00
     *
     * @return
     */
    public String[] getTodayAfternoon() {

        DateTime curDateTime = new DateTime();

        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        String days[] = new String[2];
        double curHour = curDateTime.getHourOfDay() + curDateTime.getMinuteOfHour() / 60.0;
        if (curHour > 11.5) {
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(curDateTime);
        } else {
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "12:00";
        }

        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "19:00";
        return days;
    }

    /**
     * 오늘 11시 30 이후 ~ 19:00
     *
     * @return
     */
    public String[] getTomorrowAfternoon() {

        DateTime curDateTime = new DateTime();

        if (fakeTime != null) {
            curDateTime = fakeTime;
        }

        curDateTime = curDateTime.plusDays(1);
        String days[] = new String[2];

        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "12:00";
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "19:00";
        return days;
    }

    public String[] getTodayMorning() {

        DateTime curDateTime = new DateTime();

        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        String days[] = new String[2];
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").print(curDateTime);
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(curDateTime) + "12:00";
        return days;
    }

    public String[] getTomorrow() {
        DateTime curDateTime = new DateTime();
        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        DateTime tomorrowDateTime = curDateTime.plusDays(1);
        String days[] = new String[2];
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(tomorrowDateTime) + "00:01";
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(tomorrowDateTime) + "23:59";
        return days;

    }

    /**
     * 오늘 5시 반 이후
     *
     * @return
     */
    public String[] getTomorrowEvening() {
        DateTime curDateTime = new DateTime();
        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        DateTime tomorrowDateTime = curDateTime.plusDays(1);
        String days[] = new String[2];
            days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(tomorrowDateTime) + "17:30";

        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(tomorrowDateTime) + "23:59";
        return days;
    }

    public String[] getDayAfterTomorrow() {
        DateTime curDateTime = new DateTime();
        if (fakeTime != null) {
            curDateTime = fakeTime;
        }
        DateTime dayAfterTomorrowDateTime = curDateTime.plusDays(2);
        String days[] = new String[2];
        days[0] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(dayAfterTomorrowDateTime) + "00:01";
        days[1] = DateTimeFormat.forPattern("yyyy-MM-dd ").print(dayAfterTomorrowDateTime) + "23:59";
        return days;

    }

    public String[] contextTime(String questioner, String str) {
        InterfaceDictionary dictionary = ServiceManager.getInstance().getDictionaryService().getDictionary(questioner + "사전");
        if (dictionary != null) {
            String strs[] = dictionary.search(str);
            return strs;
        } else {
            logger.fatal(questioner + "'s 사전에 " + str + " 이 없다.");
            throw new RuntimeException(questioner + "'s 사전에 " + str + " 이 없다.");
        }

    }

    public String[] analysis(String symbol) {
        String str = symbol.replaceAll(" ", "").trim();
        if (str.equals("오늘")) {
            return getToday();
        } else if (str.equals("오늘오전") || str.equals("오전")) {
            return getTodayMorning();
        } else if (str.equals("오늘저녁") || str.equals("저녁")) {
            return getTodayEvening();
        } else if (str.equals("오늘오후") || str.equals("오후")) {
            return getTodayAfternoon();
        } else if (str.equals("내일")) {
            return getTomorrow();
        } else if (str.equals("내일오후")) {
            return getTomorrowAfternoon();
        } else if (str.equals("내일저녁")) {
            return getTomorrowEvening();
        } else if (str.equals("모레")) {
            return getDayAfterTomorrow();
        } else if (str.equals("이번주")) {
            return getThisWeek();
        } else if (str.equals("이번주말")) {
            return getThisWeekend();
        } else if (str.equals("다음주")) {
            return getNextMonthStr();
        } else if (str.equals("2주")) {
            return getNextTwoWeekDays();
        } else if (str.equals("3주")) {
            return getNextThreeWeekDays();
        } else if (str.equals("이번달")) {
            return getThisMonth();
        } else if (str.equals("다음달")) {
            return getNextMonthStr();
        } else if (str.equals("워크숍첫날")) {
            return contextTime(questioner, "워크숍첫날");
        } else if (str.equals("워크숍둘째날오후")) {
            return contextTime(questioner, "워크숍둘째날오후");
        } else {
            logger.fatal("Cannot analysisSpatial " + str);
            return getToday();
//            return getTomorrowAfternoon();
//            throw new RuntimeException("Cannot analysisSpatial " + str);
        }
    }

    public void main(String[] args) {
        logger.debug("이번주 :" + Arrays.toString(getThisWeek()));
        logger.debug("이번주 weekend:" + Arrays.toString(getThisWeekend()));
        logger.debug("this weekdays:" + Arrays.toString(getThisWeekDays()));
        logger.debug("next weekend:" + Arrays.toString(getNextWeekends()));
        logger.debug("next weekdays:" + Arrays.toString(getNextWeekDays()));
        logger.debug("this month:" + Arrays.toString(getThisMonth()));
        logger.debug("다음달:" + Arrays.toString(getNextMonthStr()));

        String str1 = "1주";
        String fullstr2 = "우와 1주 이내";

        logger.debug(containSubstr("2주", "이번주", "금주", "1주"));
        LocalDate today = new LocalDate();
// 이번주 말을 찾아봐라.
        // 이번연도 첫날
        LocalDate firstDayOfYear = (new DateTime()).minusDays(today.dayOfYear().get() - 1).toLocalDate();
        logger.debug(firstDayOfYear);
        // 주말이면, weekOfYear*7 -1 부터 weekOfYear*7  까지임
        int weekOfYear = today.weekOfWeekyear().get();
//        logger.debug(weekOfYear * 7);
        LocalDate weekendStart = firstDayOfYear.plusWeeks(weekOfYear).minusDays(1);
        LocalDate weekendEnd = firstDayOfYear.plusWeeks(weekOfYear);


        logger.debug(today.getDayOfWeek());
//        logger.debug(dateTime.weekyear().get());

    }
}


