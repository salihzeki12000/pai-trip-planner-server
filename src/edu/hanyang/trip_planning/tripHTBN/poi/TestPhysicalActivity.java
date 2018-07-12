package edu.hanyang.trip_planning.tripHTBN.poi;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import org.apache.log4j.Logger;

/**
 * Created by wykwon on 2015-12-03.
 */
public class TestPhysicalActivity {
    private static Logger logger = Logger.getLogger(TestPhysicalActivity.class);

    public static void main(String[] args) {
        for (String title : POITitleList.jeju100) {
            BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(title);
            logger.debug(title + "'s PA="+basicPOI.getPhysicalActivity());
        }
    }
}
