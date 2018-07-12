package edu.hanyang.trip_planning.tripData.preference;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;

/**
 * Created by wykwon on 2015-10-21.
 */
public class PreferenceGenerator {
    private static Logger logger = Logger.getLogger(PreferenceGenerator.class);


    public static void main(String[] args) {

        POIManager poiManager = POIManager.getInstance();
        BasicPOI basicPOI = poiManager.getPOIByTitle("섭지코지");
        ConvertTouristAttractionType convertTouristAttractionType = new ConvertTouristAttractionType();


        for (BasicPOI poi : poiManager.getAll()){
            if (poi.getPoiType().category.equals("여행")){
                System.out.println(poi.getPoiType());
            }

        }
//        logger.debug(basicPOI);
//        logger.debug(kwon());
    }
}
