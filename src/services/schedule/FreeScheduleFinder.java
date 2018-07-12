package services.schedule;


import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import services.datatype.PersonalScheduleEntry;
import services.datatype.PersonalScheduleEntryJoda;
import wykwon.common.Pair;
import wykwon.common.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2015-11-10.
 */
public class FreeScheduleFinder {
    private static Logger logger = Logger.getLogger(FreeScheduleFinder.class);
    // 기본 시간 단위 (분)
    private int defaultStep = 20;
    // 기본 마진 단위 (분)
    private int defaultMargin = 5;
    // 최소 확보되는 시간
    private int minumumDuration = 180;


    // Internally, the class holds two pieces of data. Firstly, it holds the datetime as milliseconds from the Java epoch of 1970-01-01T00:00:00Z. Secondly, it holds a Chronology which determines how the millisecond instant value is converted into the date time fields. The default Chronology is ISOChronology which is the agreed international standard and compatible with the modern Gregorian calendar.

    private DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
//    this.startHour = dateFormatter.parseDateTime(startHour);

    /**
     * @param
     * @param startTime
     * @param endTime
     */
    public List<Pair<DateTime, DateTime>> findFreeSchedule(List<List<PersonalScheduleEntryJoda>> scheduleListOfList, DateTime startTime, DateTime endTime) {

        List<Triple<DateTime, DateTime, Boolean>> edgeList = new ArrayList<Triple<DateTime, DateTime, Boolean>>();
        DateTime tmpStartTime = startTime;
        DateTime tmpEndTime = startTime;
        boolean curOverlapped , oldOverlapped = false;

        // 첫번째 시간구간을 검토했을때  검토해 볼것


        boolean bStart = true;
        while (true) {
            tmpEndTime = tmpEndTime.plusMinutes(defaultStep);
            if (tmpEndTime.isAfter(endTime)) {
                break;
            }

            curOverlapped = checkMultipleOverlappedSchedule(scheduleListOfList, tmpStartTime, tmpEndTime);

            if (bStart) {
                bStart = false;
            } else if (curOverlapped != oldOverlapped) {
//                logger.debug("edge detected" + dateTimeFormat.print(tmpStartTime) + " -- " + dateTimeFormat.print(tmpEndTime) + "\t edgetype=" + curOverlapped);
                edgeList.add(new Triple<DateTime, DateTime, Boolean>(tmpStartTime, tmpEndTime, curOverlapped));
            }
//            logger.debug(dateTimeFormat.print(tmpStartTime) + " -- " + dateTimeFormat.print(tmpEndTime) + " = " + curOverlapped);
            tmpStartTime = tmpEndTime;
            oldOverlapped = curOverlapped;
        }
        // edge list가 작으면 전체가 free time이다.
        if (edgeList.size()<=1){
            List<Pair<DateTime, DateTime>> retList = new ArrayList<Pair<DateTime, DateTime>>();
            retList.add(new Pair<DateTime, DateTime>(startTime,endTime));
            return retList;
        }
        return edgeToFreeScheduleList(edgeList, startTime, endTime);
    }

    public List<Pair<DateTime, DateTime>> findFreeSchedule(List<List<PersonalScheduleEntryJoda>> scheduleListOfList, String startTimeStr, String endTimeStr) {
        DateTime startTime = dateTimeFormat.parseDateTime(startTimeStr);
        DateTime endTime = dateTimeFormat.parseDateTime(endTimeStr);
        return findFreeSchedule(scheduleListOfList, startTime, endTime);
    }

    public static List<PersonalScheduleEntryJoda> convertList(List<PersonalScheduleEntry> list) {
        List<PersonalScheduleEntryJoda> retList = new ArrayList<PersonalScheduleEntryJoda>();
        if (list==null){
            logger.fatal("list is null");
            return retList;
        }

        for (PersonalScheduleEntry entry : list) {
            retList.add(new PersonalScheduleEntryJoda(entry));
        }
        return retList;
    }

    /**
     * 어느 기간동안의 비는 일정을 찾아낸다. 한사람꺼를 말이다.
     *
     * @param scheduleList
     * @param startTime
     * @param endTime
     */
    public List<Pair<DateTime, DateTime>> findSingleFreeSchedule(List<PersonalScheduleEntryJoda> scheduleList, DateTime startTime, DateTime endTime) {
        // startTime부터 endTime까지 주어진 간격 단위로 돌려라.
        List<Triple<DateTime, DateTime, Boolean>> edgeList = new ArrayList<Triple<DateTime, DateTime, Boolean>>();
        DateTime tmpStartTime = startTime;
        DateTime tmpEndTime = startTime;
        boolean curOverlapped, oldOverlapped = false;

        // 첫번째 시간구간을 검토했을때  검토해 볼것


        boolean bStart = true;
        while (true) {
            tmpEndTime = tmpEndTime.plusMinutes(defaultStep);
            if (tmpEndTime.isAfter(endTime)) {
                break;
            }

            curOverlapped = checkOverlappedSchedule(scheduleList, tmpStartTime, tmpEndTime);

            if (bStart) {
                 bStart = false;
            } else if (curOverlapped != oldOverlapped) {
                logger.debug("edge detected" + dateTimeFormat.print(tmpStartTime) + " -- " + dateTimeFormat.print(tmpEndTime) + "\t edgetype=" + curOverlapped);
                edgeList.add(new Triple<DateTime, DateTime, Boolean>(tmpStartTime, tmpEndTime, curOverlapped));
            }
//            logger.debug(dateTimeFormat.print(tmpStartTime) + " -- " + dateTimeFormat.print(tmpEndTime) + " = " + curOverlapped);
            tmpStartTime = tmpEndTime;
            oldOverlapped = curOverlapped;
        }
        return edgeToFreeScheduleList(edgeList, startTime, endTime);
    }


    private List<Pair<DateTime, DateTime>> edgeToFreeScheduleList(List<Triple<DateTime, DateTime, Boolean>> edgeList, DateTime startTime, DateTime endTime) {
        List<Pair<DateTime, DateTime>> freeTimeList = new ArrayList<Pair<DateTime, DateTime>>();
        // 일정 자체가 없으면 그대로 반환
        if (edgeList==null ){

            freeTimeList.add(new Pair<DateTime, DateTime>(startTime, endTime));
            return freeTimeList;
        }
        else if (edgeList.size()==0){
            freeTimeList.add(new Pair<DateTime, DateTime>(startTime, endTime));
            return freeTimeList;
        }


        DateTime s1, s2, e1, e2;
        boolean b1, b2;

        if (edgeList.size() == 1) {
            throw new RuntimeException("error");
        }
        s1 = edgeList.get(0).first();
        e1 = edgeList.get(0).second();
        b1 = edgeList.get(0).third();

        if (b1) {
            freeTimeList.add(new Pair<DateTime, DateTime>(startTime, s1));
        }

        for (int i = 1; i < edgeList.size(); i++) {
            Triple<DateTime, DateTime, Boolean> entry = edgeList.get(i);
            s2 = entry.first();
            e2 = entry.second();
            b2 = entry.third();
            if (b1 == false && b2 == true) {
                freeTimeList.add(new Pair<DateTime, DateTime>(s1, s2));
            }
            s1 = s2;
            e1 = e2;
            b1 = b2;
        }

        if (!b1) {
            freeTimeList.add(new Pair<DateTime, DateTime>(e1, endTime));
        }

//        logger.debug(freeTimeList);
        return freeTimeList;
    }

    /**
     * 어느 기간동안의 비는 일정을 찾아낸다. 한사람꺼를 말이다.
     *
     * @param scheduleList
     * @param startTimeStr
     * @param endTimeStr
     */
    public void findSingleFreeSchedule(List<PersonalScheduleEntryJoda> scheduleList, String startTimeStr, String endTimeStr) {
        DateTime startTime = dateTimeFormat.parseDateTime(startTimeStr);
        DateTime endTime = dateTimeFormat.parseDateTime(endTimeStr);
        findSingleFreeSchedule(scheduleList, startTime, endTime);
    }


    /**
     * 일정중에 하나라도 겹치는게 있으면 그만둔다.
     *
     * @param scheduleList
     * @param startTime
     * @param endTime
     * @return
     */
    private boolean checkOverlappedSchedule(List<PersonalScheduleEntryJoda> scheduleList,
                                            DateTime startTime, DateTime endTime) {
        for (PersonalScheduleEntryJoda entry : scheduleList) {
            DateTime s1 = entry.startTime;
            DateTime e1 = entry.startTime.plusMinutes((int) (60 * entry.duration.hour));
            if (checkOverlappedSchedule(s1, e1, startTime, endTime)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMultipleOverlappedSchedule(List<List<PersonalScheduleEntryJoda>> scheduleListOfList, DateTime startTime, DateTime endTime) {
        for (List<PersonalScheduleEntryJoda> scheduleList : scheduleListOfList) {
            for (PersonalScheduleEntryJoda entry : scheduleList) {
                DateTime s1 = entry.startTime;
                DateTime e1 = entry.startTime.plusMinutes((int) (60 * entry.duration.hour));
                if (checkOverlappedSchedule(s1, e1, startTime, endTime)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkOverlappedSchedule(DateTime s1, DateTime e1, DateTime s2, DateTime e2) {
        if (s1.isBefore(s2) && e1.isBefore(e2) && s1.isBefore(e2) && s2.isAfter(e1)) {
            return false;
        } else if (s1.isAfter(s2) && e1.isAfter(e2) && s1.isAfter(e2) && s2.isBefore(e1)) {
            return false;
        }
        return true;
    }

    private boolean checkOverlappedSchedule(String scheduleStartStr, String scheduleEndStr, String startTimeStr, String endTimeStr) {
        return checkOverlappedSchedule(dateTimeFormat.parseDateTime(scheduleStartStr),
                dateTimeFormat.parseDateTime(scheduleEndStr),
                dateTimeFormat.parseDateTime(startTimeStr),
                dateTimeFormat.parseDateTime(endTimeStr)
        );
    }



}
