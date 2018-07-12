package edu.hanyang.trip_planning.tripData.mapAPI.naver_car;

import edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json.NaverCarRoute;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import edu.hanyang.trip_planning.tripHTBN.poi.POITitleList;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by wykwon on 2015-12-28.
 */
public class BatchPathGeneration {

    public String poiTitles[];
    private static Logger logger = Logger.getLogger(BatchPathGeneration.class);

    public static void generate(String poiTitles[], String filename) {
        int cnt = 0;
        for (int i = 0; i < poiTitles.length; i++) {
            for (int j = 0; j < poiTitles.length; j++) {
                if (i == j) {
                    continue;
                }
                logger.debug(poiTitles[i] + "->" + poiTitles[j]);
                cnt++;

                BasicPOI src = null;
                BasicPOI dest = null;
                if (poiTitles[i].equals("비양도")) {
                    src = POIManager.getInstance().getPOIByTitle("한림항");
                }
                else if (poiTitles[i].equals("우도")){
                    src = POIManager.getInstance().getPOIByTitle("성산포항");
                }
                else {
                    src = POIManager.getInstance().getPOIByTitle(poiTitles[i]);
                }
                if (poiTitles[j].equals("비양도")) {
                    dest = POIManager.getInstance().getPOIByTitle("한림항");
                } else if (poiTitles[j].equals("우도"))
                {
                    dest = POIManager.getInstance().getPOIByTitle("성산포항");
                }
                else {
                    dest = POIManager.getInstance().getPOIByTitle(poiTitles[j]);
                }


                NaverCarRoute naverCarRoute = CarRouteHTTPRequest.naverCarRouteRequest(src, dest);
                logger.debug(naverCarRoute);
                if (naverCarRoute.getError() != null) {
                    writeError("datafiles/route/error.csv", src.getTitle(), dest.getTitle());
                } else {
                    int speed = naverCarRoute.getSummary().getSpeed();
                    int distance = naverCarRoute.getSummary().getTotalDistance();
                    int time = naverCarRoute.getSummary().getTotalTime();
                    int taxiCost = naverCarRoute.getSummary().getTaxi();
                    logger.debug(src.getTitle() + "->" + dest.getTitle() + "=" + naverCarRoute.getSummary());

                    DateTime dateTime = new DateTime();
                    int dayOfWeek = dateTime.getDayOfWeek();
                    int hourOfDay = dateTime.getHourOfDay();
                    int minuteOfHour = dateTime.getMinuteOfHour();

                    CarRouteInfo carRouteInfo = new CarRouteInfo(src.getTitle(), dest.getTitle(), dayOfWeek, hourOfDay, minuteOfHour, speed, distance, time, taxiCost);
                    writeCSV(filename, carRouteInfo);
                }

            }
        }
        logger.debug("totalCnt=" + cnt);


    }


    /**
     * file wrire 규칙
     * 잘 쓸것
     *
     * @param
     */

    private static void writeCSV(String filename, CarRouteInfo carRouteInfo) {
        try {

            BufferedWriter fw = new BufferedWriter(
                    new FileWriter(filename, true));

            String str = carRouteInfo.srcName + "\t" + carRouteInfo.destName + "\t" + carRouteInfo.speed + "\t" + carRouteInfo.totalDistance + "\t" + carRouteInfo.totalTime + "\t" + carRouteInfo.taxiCost + "\t" + carRouteInfo.dayOfWeek + "\t" + carRouteInfo.hour + "\t" + carRouteInfo.minute + "\n";
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeError(String filename, String srcName, String destName) {
        try {

            BufferedWriter fw = new BufferedWriter(
                    new FileWriter(filename, true));

            String str = srcName + "\t" + destName + "\n";
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String hotelTitles[] = POITitleList.jejuHotels;
        String attractionsTitles[] = POITitleList.jeju100;

        String titles[] = new String[hotelTitles.length + attractionsTitles.length];
        int idxCnt = 0;
        for (int i = 0; i < hotelTitles.length; i++) {
            titles[idxCnt] = hotelTitles[i];
            idxCnt++;
        }
        for (int i = 0; i < attractionsTitles.length; i++) {
            titles[idxCnt] = attractionsTitles[i];
            idxCnt++;
        }
        long startTime = System.currentTimeMillis();
        DateTime dateTime = new DateTime();

        String dateTimeStr = DateTimeFormat.forPattern("yyyy-MM-dd-HH-mm-").print(dateTime);    // 지금
        logger.debug(dateTimeStr);
        generate(titles, "datafiles/route/" + dateTimeStr+"jeju.csv");

                logger.debug("time=" + (System.currentTimeMillis() - startTime));


    }
}

