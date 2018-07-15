package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraintFactory;
import edu.hanyang.trip_planning.optimize.constraints.poiConstraint.PoiConstraint;
import wykwon.common.DateTimeFormatStr;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 하루 일정에 대한 질문 구조체
public class DailyTripEntry {
    private String startTimeStr;        // 여정 시작시간
    private String returnTimeStr;       // 여정 종료시간
    private String startPOITitle;       // 여정 시작장소
    private String endPOITitle;         // 여정 종료장소
    private String[] areas;               // 여정 지역
    private Double physicalActivityLimit; // 활동량 상한
    private List<CategoryConstraint> categoryConstraintList = new ArrayList<>();
    private List<PoiConstraint> poiConstraintList = new ArrayList<>();
    private static Logger logger = Logger.getLogger(DailyTripEntry.class);

    public DailyTripEntry(String startTimeStr, String returnTimeStr, String[] areas, String startPOITitle, String endPOITitle, double physicalActivityLimit) {
        this.startTimeStr = startTimeStr;
        this.returnTimeStr = returnTimeStr;
        this.areas = areas;
        this.startPOITitle = startPOITitle;
        this.endPOITitle = endPOITitle;
        this.physicalActivityLimit = physicalActivityLimit;
    }

    public void addCategoryConstraint(CategoryConstraint categoryConstraint) {
        categoryConstraintList.add(categoryConstraint);
    }
    public void addPoiConstraint(PoiConstraint poiConstraint) {
        poiConstraintList.add(poiConstraint);
    }

    public int[] getStartTime() {
        DateTime dateTime = DateTimeFormatStr.parseFullDateTime(startTimeStr);
        int ret[] = new int[5];
        ret[0] = dateTime.getYear();            // year
        ret[1] = dateTime.getMonthOfYear();     // month
        ret[2] = dateTime.getDayOfMonth();      // day
        ret[3] = dateTime.getHourOfDay();       // hour
        ret[4] = dateTime.getMinuteOfHour();    // min
        return ret;
    }
    public int[] getReturnTime() {
        DateTime dateTime = DateTimeFormatStr.parseFullDateTime(returnTimeStr);
        int ret[] = new int[5];
        ret[0] = dateTime.getYear();            // year
        ret[1] = dateTime.getMonthOfYear();     // month
        ret[2] = dateTime.getDayOfMonth();      // day
        ret[3] = dateTime.getHourOfDay();       // hour
        ret[4] = dateTime.getMinuteOfHour();    // min
        return ret;
    }
    public String getStartPOITitle() {
        return startPOITitle;
    }
    public String getEndPOITitle() {
        return endPOITitle;
    }
    public String[] getAreas() {return areas;}
    public double getPhysicalActivityLimit() {
        return physicalActivityLimit;
    }
    public List<CategoryConstraint> getCategoryConstraintList() {
        return categoryConstraintList;
    }
    public List<PoiConstraint> getPoiConstraintList() {
        return poiConstraintList;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("daily itinerary [" + startTimeStr + "--" + returnTimeStr + "]\n");
        strbuf.append("\tareas=" + areas + " from " + startPOITitle + " to " + endPOITitle + "\n");
        strbuf.append("\tphysical activity limit=" + physicalActivityLimit + "\n");
        for (CategoryConstraint categoryConstraint : categoryConstraintList) {
            strbuf.append("\t" + categoryConstraint + "\n");
        }
        return strbuf.toString();
    }

    public static void test() {
        String[] Areas = {"서귀포시"};
        DailyTripEntry dailyTripEntry = new DailyTripEntry("2016-12-15 09:00", "2016-12-15 18:00",
                Areas, "켄싱턴제주호텔", "켄싱턴제주호텔", 2000);

        CategoryConstraint lunchConstraint = CategoryConstraintFactory.createLunchConstraint();
        CategoryConstraint shoppingConstraint = CategoryConstraintFactory.createShoppingConstraint();
        dailyTripEntry.addCategoryConstraint(lunchConstraint);
        dailyTripEntry.addCategoryConstraint(shoppingConstraint);
        logger.debug(dailyTripEntry);
        logger.debug(Arrays.toString(dailyTripEntry.getStartTime()));
        logger.debug(Arrays.toString(dailyTripEntry.getReturnTime()));
        logger.debug(dailyTripEntry.getStartPOITitle());
        logger.debug(dailyTripEntry.getEndPOITitle());
        logger.debug(dailyTripEntry.getPhysicalActivityLimit());
    }

    public static void main(String[] args) {
        test();
    }

}
