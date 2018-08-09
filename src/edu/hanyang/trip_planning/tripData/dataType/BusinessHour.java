package edu.hanyang.trip_planning.tripData.dataType;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * 영업시간
 * <p/>
 * 시간 표현방법
 * HH:mm
 */
public class BusinessHour {
    private static Logger logger = Logger.getLogger(BusinessHour.class);
    /**
     * 주중에 문여는 시간
     */
    public String weekDayOpen;
    public double weekDayOpenHour;

    /**
     * 주중에 문닫는 시간
     */
    public String weekDayClose;
    public double weekDayCloseHour;
    /**
     * 토요일 문여는 시간
     */
    public String saturdayOpen;
    public double saturdayOpenHour;
    /**
     * 토요일 문닫는 시간
     */
    public String saturdayClose;
    public double saturdayCloseHour;
    /**
     * 일요일 문여는 시간
     */
    public String sundayOpen;
    public double sundayOpenHour;
    /**
     * 일요일 문닫는 시간
     */
    public String sundayClose;
    public double sundayCloseHour;


    public BusinessHour() {

    }

    public BusinessHour(String open, String close) {
        setWeekDay(open, close);
        setSunday(open, close);
        setSaturday(open, close);
    }

    public BusinessHour(String open_close) {
        setWeekDay(open_close);
        setSaturday(open_close);
        setSunday(open_close);
    }

    public BusinessHour(BusinessHour arg) {
        this.weekDayOpen = arg.weekDayOpen;
        this.weekDayClose = arg.weekDayClose;
        this.saturdayOpen = arg.saturdayOpen;
        this.saturdayClose = arg.saturdayClose;
        this.sundayOpen = arg.sundayOpen;
        this.sundayClose = arg.sundayClose;
        this.weekDayOpenHour = arg.weekDayOpenHour;
        this.weekDayCloseHour = arg.weekDayCloseHour;
        this.saturdayOpenHour = arg.saturdayOpenHour;
        this.saturdayCloseHour = arg.saturdayCloseHour;
        this.sundayOpenHour = arg.sundayOpenHour;
        this.sundayCloseHour = arg.sundayCloseHour;
    }

    public void boot() {
        if (weekDayOpen != null) {
            if (!weekDayOpen.isEmpty()) {
                DateTime openTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(weekDayOpen);
                weekDayOpenHour = openTime.getHourOfDay() + ((double) openTime.getMinuteOfHour()) / 60.0;
            }
        }
        if (weekDayClose != null) {
            if (!weekDayClose.isEmpty()) {
                if (weekDayClose.equals("24:00")) {
                    weekDayClose = "23:59";
                }
                DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(weekDayClose);
                weekDayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
            }
        }
        if (saturdayOpen != null) {
            if (!saturdayOpen.isEmpty()) {
                DateTime openTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(saturdayOpen);
                saturdayOpenHour = openTime.getHourOfDay() + ((double) openTime.getMinuteOfHour()) / 60.0;
            }
        }
        if (saturdayClose != null) {
            if (!saturdayClose.isEmpty()) {
                if (saturdayClose.equals("24:00")) {
                    saturdayClose = "23:59";
                }
                DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(saturdayClose);
                saturdayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
            }
        }

        if (sundayOpen != null) {
            if (!saturdayOpen.isEmpty()) {
                DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(sundayOpen);
                sundayOpenHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
            }
        }
        if (sundayClose != null) {
            if (!sundayClose.isEmpty()) {
                if (sundayClose.equals("24:00")) {
                    sundayClose = "23:59";
                }

                DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(sundayClose);
                sundayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
            }
        }
    }

    public void setWeekDay(String open, String close) {
        this.weekDayOpen = open;
        if (close.equals("24:00")) {
            close = "23:59";
        }
        this.weekDayClose = close;

        DateTime openTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(open);
        DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(close);
        weekDayOpenHour = openTime.getHourOfDay() + ((double) openTime.getMinuteOfHour()) / 60.0;
        weekDayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
    }

    public void setSaturday(String open, String close) {
        this.saturdayOpen = open;
        this.saturdayClose = close;
        if (close.equals("24:00")) {
            close = "23:59";
        }
        DateTime openTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(open);
        DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(close);
        this.saturdayOpenHour = openTime.getHourOfDay() + ((double) openTime.getMinuteOfHour()) / 60.0;
        this.saturdayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
    }

    public void setSunday(String open, String close) {
        this.sundayOpen = open;
        if (close.equals("24:00")) {
            close = "23:59";
        }

        this.sundayClose = close;
        DateTime openTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(open);

        DateTime closeTime = DateTimeFormat.forPattern("HH:mm").parseDateTime(close);

        this.sundayOpenHour = openTime.getHourOfDay() + ((double) openTime.getMinuteOfHour()) / 60.0;
        this.sundayCloseHour = closeTime.getHourOfDay() + ((double) closeTime.getMinuteOfHour()) / 60.0;
    }

    public void setWeekDay(String open_close) {
        //11:30 ~ 21:30 이렇게 된 문장을 입력한다.
        String strs[] = open_close.split(" ");
//        logger.debug(Arrays.toString(strs));
        this.setWeekDay(strs[0].trim(), strs[2].trim());

        if (strs[2].equals("24:00")) {
            strs[2] = "23:59";
        }
        DateTime open = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[0].trim());
        DateTime close = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[2].trim());
        this.weekDayOpenHour = open.getHourOfDay() + ((double) open.getMinuteOfHour()) / 60.0;
        this.weekDayCloseHour = close.getHourOfDay() + ((double) close.getMinuteOfHour()) / 60.0;


    }

    public void setSaturday(String open_close) {
        //11:30 ~ 21:30 이렇게 된 문장을 입력한다.
        String strs[] = open_close.split(" ");
//        logger.debug(Arrays.toString(strs));
        this.setSaturday(strs[0].trim(), strs[2].trim());
        DateTime open = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[0].trim());
        if (strs[2].equals("24:00")) {
            strs[2] = "23:59";
        }
        DateTime close = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[2].trim());

        this.saturdayOpenHour = open.getHourOfDay() + ((double) open.getMinuteOfHour()) / 60.0;
        this.saturdayCloseHour = close.getHourOfDay() + ((double) close.getMinuteOfHour()) / 60.0;
    }

    public void setSunday(String open_close) {
        //11:30 ~ 21:30 이렇게 된 문장을 입력한다.
        String strs[] = open_close.split(" ");
//        logger.debug(Arrays.toString(strs));
        this.setSunday(strs[0].trim(), strs[2].trim());
        if (strs[2].equals("24:00")) {
            strs[2] = "23:59";
        }

        DateTime open = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[0].trim());
        DateTime close = DateTimeFormat.forPattern("HH:mm").parseDateTime(strs[2].trim());
        this.sundayOpenHour = open.getHourOfDay() + ((double) open.getMinuteOfHour()) / 60.0;
        this.sundayCloseHour = close.getHourOfDay() + ((double) close.getMinuteOfHour()) / 60.0;
    }

    public BusinessHour deepCopy() {
        return new BusinessHour(this);
    }

    public double getOpenHour(int year, int month, int day) {
        switch (dayOfWeek(year, month, day)) {
            case 6:
                return saturdayOpenHour;
            case 7:
                return sundayOpenHour;
            default:
                return weekDayOpenHour;
        }
    }

    public double getCloseHour(int year, int month, int day) {
        switch (dayOfWeek(year, month, day)) {
            case 6:
                return saturdayCloseHour;
            case 7:
                return sundayCloseHour;
            default:
                return weekDayCloseHour;
        }
    }

    private int dayOfWeek(int year, int month, int day) {
        if (month == 1 || month == 2) year--;
        month = (month + 9) % 12 + 1;
        int y = year % 100;
        int century = year / 100;
        int week = ((13 * month - 1) / 5 + day + y + y / 4 + century / 4 - 2 * century) % 7;
        if (week < 0) week = (week + 7) % 7;
        return week;

    }

    private String toHourMinute(double hour) {
        int h = (int) hour;
        int m = (int) (hour - h) * 60;
        return h + ":" + m;
    }

    @Override
    public String toString() {
        return "BusinessHour{" +
                "weekDayOpen='" + weekDayOpen + '\'' +
                ", weekDayClose='" + weekDayClose + '\'' +
                ", saturdayOpen='" + saturdayOpen + '\'' +
                ", saturdayClose='" + saturdayClose + '\'' +
                ", sundayOpen='" + sundayOpen + '\'' +
                ", sundayClose='" + sundayClose + '\'' +
                '}';
    }


}
