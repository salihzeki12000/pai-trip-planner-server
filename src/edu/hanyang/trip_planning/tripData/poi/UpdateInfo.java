package edu.hanyang.trip_planning.tripData.poi;



import edu.hanyang.trip_planning.tripData.dataType.BusinessHour;

import java.io.IOException;

/**
 * Created by wykwon on 2015-11-21.
 */
public class UpdateInfo {

    public static void test() {
        // 섭지코지
        updateDurationAndCost("섭지코지", 50, 10, 1000);
        updateBusinessHour("섭지코지", "10:00", "18:00");

        // 비자림
        updateDurationAndCost("비자림", 60, 20, 1500);
        updateBusinessHour("비자림", "9:00", "18:00");

        updateDurationAndCost("성산일출봉", 55, 15, 2000);
        updateBusinessHour("성산일출봉", "05:30", "19:30");

        updateDurationAndCost("용두암", 15, 5, 0);
        updateBusinessHour("용두암", "05:30", "19:30");

        updateDurationAndCost("용연구름다리", 15, 5, 0);
        updateBusinessHour("용연구름다리", "05:30", "19:30");

        updateDurationAndCost("함덕서우봉해변", 45, 15, 0);
        updateBusinessHour("함덕서우봉해변", "05:30", "19:30");

        updateDurationAndCost("대포주상절리", 40, 15, 3000);
        updateBusinessHour("대포주상절리", "08:00", "18:00");

        updateDurationAndCost("쇠소깍", 40, 20, 6000);
        updateBusinessHour("쇠소깍", "09:00", "17:30");

        updateDurationAndCost("사려니숲길", 180, 60, 0);
        updateBusinessHour("사려니숲길", "08:00", "18:30");

        updateDurationAndCost("만장굴", 30, 10, 2000);
        updateBusinessHour("만장굴", "09:00", "18:00");

        updateDurationAndCost("우도", 180, 50, 5500);
        updateBusinessHour("우도", "08:00", "17:30");

        updateDurationAndCost("삼성혈", 30, 10, 1000);
        updateBusinessHour("삼성혈", "08:30", "17:30");

        updateDurationAndCost("외돌개", 30, 10, 1000);
        updateBusinessHour("삼성혈", "08:30", "17:30");
    }


    public static void updateDurationAndCost(String title, int spendingMin, int sd, int cost) {
        BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(title);
        basicPOI.setSpendingMinutes(spendingMin, sd);
        basicPOI.setAverageCostPerPerson(cost);

        try {
            POIManager.getInstance().update2CSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateBusinessHour(String title, String openHour, String closeHour) {
        BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(title);
        basicPOI.setBusinessHour(new BusinessHour(openHour, closeHour));

        try {
            POIManager.getInstance().update2CSV();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        test();
    }
}
