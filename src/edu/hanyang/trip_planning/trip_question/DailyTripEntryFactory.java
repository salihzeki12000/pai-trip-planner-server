package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraintFactory;
import edu.hanyang.trip_planning.optimize.constraints.poiConstraint.PoiConstraintFactory;

/**
 * Created by mgkim on 2016-12-22.
 */
public class DailyTripEntryFactory {
    public static DailyTripEntry dailyTripEntryExample1() {
        String startTimeStr = "2017-01-15 15:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-15 20:00";      // 여정 종료시간
        String startPOITitle = "제주국제공항";          // 여정 시작장소
        String endPOITitle = "켄싱턴제주호텔";          // 여정 종료장소
        String[] area = {"제주특별자치도"};             // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample2() {
        String startTimeStr = "2017-01-16 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-16 20:00";      // 여정 종료시간
        String startPOITitle = "켄싱턴제주호텔";        // 여정 시작장소
        String endPOITitle = "제주퍼시픽호텔";          // 여정 종료장소
        String[] area = {"서귀포시"};                   // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample3() {
        String startTimeStr = "2017-01-17 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-17 15:00";      // 여정 종료시간
        String startPOITitle = "제주퍼시픽호텔";        // 여정 시작장소
        String endPOITitle = "제주국제공항";            // 여정 종료장소
        String[] area = {"제주시"};                     // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample4() {
        String startTimeStr = "2017-01-15 15:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-15 20:00";      // 여정 종료시간
        String startPOITitle = "제주국제공항";          // 여정 시작장소
        String endPOITitle = "켄싱턴제주호텔";          // 여정 종료장소
        String[] area = {"제주특별자치도"};             // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping
        dailyTripEntry.addPoiConstraint(PoiConstraintFactory.poiConstraintExample1());                  // 정방폭포
        dailyTripEntry.addPoiConstraint(PoiConstraintFactory.poiConstraintExample2());                  // X제주미니랜드

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample5() {
        String startTimeStr = "2017-01-16 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-16 20:00";      // 여정 종료시간
        String startPOITitle = "켄싱턴제주호텔";        // 여정 시작장소
        String endPOITitle = "제주퍼시픽호텔";          // 여정 종료장소
        String[] area = {"서귀포시"};                   // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner
        dailyTripEntry.addPoiConstraint(PoiConstraintFactory.poiConstraintExample4());                  // 한림공원

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample6() {
        String startTimeStr = "2017-01-17 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-17 15:00";      // 여정 종료시간
        String startPOITitle = "제주퍼시픽호텔";        // 여정 시작장소
        String endPOITitle = "제주국제공항";            // 여정 종료장소
        String[] area = {"제주시"};                     // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping
        dailyTripEntry.addPoiConstraint(PoiConstraintFactory.poiConstraintExample5());                  // 테디베어뮤지엄 제주점

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample7() {
        String startTimeStr = "2017-01-15 15:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-15 20:00";      // 여정 종료시간
        String startPOITitle = "제주국제공항";          // 여정 시작장소
        String endPOITitle = "켄싱턴제주호텔";          // 여정 종료장소
        String[] area = {"제주특별자치도"};             // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한

        return new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);
    }
    public static DailyTripEntry dailyTripEntryExample8() {
        String startTimeStr = "2017-01-16 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-16 20:00";      // 여정 종료시간
        String startPOITitle = "켄싱턴제주호텔";        // 여정 시작장소
        String endPOITitle = "제주퍼시픽호텔";          // 여정 종료장소
        String[] area = {"서귀포시"};                   // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한

        return new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);
    }
    public static DailyTripEntry dailyTripEntryExample9() {
        String startTimeStr = "2017-01-17 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-17 15:00";      // 여정 종료시간
        String startPOITitle = "제주퍼시픽호텔";        // 여정 시작장소
        String endPOITitle = "제주국제공항";            // 여정 종료장소
        String[] area = {"제주시"};                     // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한

        return new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);
    }
    public static DailyTripEntry dailyTripEntryExample10() {
        String startTimeStr = "2017-01-17 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-17 20:00";      // 여정 종료시간
        String startPOITitle = "제주국제공항";          // 여정 시작장소
        String endPOITitle = "제주국제공항";            // 여정 종료장소
        String[] area = {"제주특별자치도"};             // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한

        return new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);
    }
    public static DailyTripEntry dailyTripEntryExample11() {
        String startTimeStr = "2017-01-15 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-15 20:00";      // 여정 종료시간
        String startPOITitle = "제주국제공항";          // 여정 시작장소
        String endPOITitle = "켄싱턴제주호텔";          // 여정 종료장소
        String[] area = {"제주특별자치도"};             // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample12() {
        String startTimeStr = "2017-01-16 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-16 20:00";      // 여정 종료시간
        String startPOITitle = "켄싱턴제주호텔";        // 여정 시작장소
        String endPOITitle = "제주퍼시픽호텔";          // 여정 종료장소
        String[] area = {"서귀포시"};                   // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner

        return dailyTripEntry;
    }
    public static DailyTripEntry dailyTripEntryExample13() {
        String startTimeStr = "2017-01-17 10:00";       // 여정 시작시간
        String returnTimeStr = "2017-01-17 20:00";      // 여정 종료시간
        String startPOITitle = "제주퍼시픽호텔";        // 여정 시작장소
        String endPOITitle = "제주국제공항";            // 여정 종료장소
        String[] area = {"제주시"};                     // 여정 지역
        Double physicalActivityLimit = 2000.0;          // 활동량 상한
        DailyTripEntry dailyTripEntry = new DailyTripEntry(startTimeStr,returnTimeStr,area,startPOITitle,endPOITitle,physicalActivityLimit);

        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createLunchConstraint());        // Lunch
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createDinnerConstraint());       // Dinner
        dailyTripEntry.addCategoryConstraint(CategoryConstraintFactory.createShoppingConstraint());     // Shopping

        return dailyTripEntry;
    }
}
