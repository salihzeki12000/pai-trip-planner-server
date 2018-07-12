package edu.hanyang.trip_planning.tripData.preference;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wykwon on 2015-10-21.
 */
public class PersonalPreference {

    private static Logger logger = Logger.getLogger(PersonalPreference.class);
    /**
     * 선호도는 0에서 1사이의 값
     */
    private final double defaultValue = 0.5;
    private final double worstValue = 0.0;
    private final double badValue = 0.2;
    private final double goodValue = 0.8;
    private final double bestValue = 1.0;
    Map<TouristAttractionType, Double> touristAttractionPreferenceMap = new HashMap<TouristAttractionType, Double>();
    Map<RestaurantType, Double> foodPreferenceMap = new HashMap<RestaurantType, Double>();
    ConvertTouristAttractionType convertTouristAttractionType = new ConvertTouristAttractionType();

    public void addWorstTouristAttractions(TouristAttractionType... attractionTypes) {
        for (TouristAttractionType type : attractionTypes) {
            touristAttractionPreferenceMap.put(type, worstValue);
        }
    }

    public void addBadTouristAttractions(TouristAttractionType... attractionTypes) {
        for (TouristAttractionType type : attractionTypes) {
            touristAttractionPreferenceMap.put(type, badValue);
        }
    }

    public void addGoodTouristAttractions(TouristAttractionType... attractionTypes) {
        for (TouristAttractionType type : attractionTypes) {
            touristAttractionPreferenceMap.put(type, goodValue);
        }
    }

    public void addBestTouristAttractions(TouristAttractionType... attractionTypes) {
        for (TouristAttractionType type : attractionTypes) {
            touristAttractionPreferenceMap.put(type, goodValue);
        }
    }

    public void addWorstRestaurantTypes(RestaurantType... restaurantTypes) {
        for (RestaurantType type : restaurantTypes) {
            foodPreferenceMap.put(type, worstValue);
        }
    }

    public void addBadRestaurantTypes(RestaurantType... restaurantTypes) {
        for (RestaurantType type : restaurantTypes) {
            foodPreferenceMap.put(type, badValue);
        }
    }

    public void addGoodRestaurantTypes(RestaurantType... restaurantTypes) {
        for (RestaurantType type : restaurantTypes) {
            foodPreferenceMap.put(type, goodValue);
        }
    }

    public void addBestRestaurantTypes(RestaurantType... restaurantTypes) {
        for (RestaurantType type : restaurantTypes) {
            foodPreferenceMap.put(type, bestValue);
        }
    }

    public double getAttractionPreference(TouristAttractionType type) {
        Double value = touristAttractionPreferenceMap.get(type);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }


    public double getAttractionPreference(BasicPOI poi) {
        TouristAttractionType type = convertTouristAttractionType.getAttractionType(poi);
        Double value = touristAttractionPreferenceMap.get(type);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }


    public double getFoodPreference(RestaurantType type) {
        Double value = foodPreferenceMap.get(type);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }


    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<TouristAttractionType, Double> entry : touristAttractionPreferenceMap.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + '\n');
        }
        for (Map.Entry<RestaurantType, Double> entry : foodPreferenceMap.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + '\n');
        }

        return stringBuffer.toString();
    }

    public static void test() {

//        PersonalPreference kwonsPreference = PreferenceGenerator.kwon();
//        POIManager poiManager = POIManager.getInstance();
//        logger.debug(kwonsPreference.getAttractionPreference(poiManager.getPOIByTitle("섭지코지")));
//        logger.debug(kwonsPreference.getAttractionPreference(poiManager.getPOIByTitle("사려니숲길")));
//        logger.debug(poiManager.getPOIByTitle("사려니숲길").getSatisifaction(null, null));


    }

    public static void main(String[] args) {
        test();
    }
}
