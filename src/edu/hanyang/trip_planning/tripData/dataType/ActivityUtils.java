package edu.hanyang.trip_planning.tripData.dataType;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

/**
 * Created by wykwon on 2015-10-20.
 */
public class ActivityUtils {


    private static Logger logger = Logger.getLogger(ActivityUtils.class);


    /**
     * activity가 outdoor activity인지 확인한다.
     *
     * @param activityType
     * @return
     */
    public static boolean isOutdoorActivity(ActivityType activityType) {
        switch (activityType) {
            case OutdoorExercise:
                return true;

            case OutdoorSightSeeing:
                return true;

            case Walk:
                return true;
            default:
                return false;
        }
    }

    public static boolean isOutdoorActivity(BasicPOI poi) {
        ActivityType activityType = avaliableActivity(poi.getPoiType());
        switch (activityType) {
            case OutdoorExercise:
                return true;

            case OutdoorSightSeeing:
                return true;

            case Walk:
                return true;
            default:
                return false;
        }
    }

    public static ActivityType avaliableActivity(POIType poiType) {

        ActivityType retType = ActivityType.Unknown;
        if (poiType.category.equals("음식점")) {
            retType = ActivityType.Eat;
        } else if (poiType.category.equals("여행")) {
            if (poiType.subCategory.equals("숙박")) {
                retType = ActivityType.RestOrSleep;
            } else {
                retType = ActivityType.OutdoorSightSeeing;
            }
        } else if (poiType.category.equals("교육,학문")) {
            retType = ActivityType.Indoor;
        } else if (poiType.category.equals("문화,예술")) {
            retType = ActivityType.Show;
        } else if (poiType.category.equals("스포츠,레저")) {
            if (poiType.subCategory.equals("축구장")) {
                retType = ActivityType.OutdoorExercise;
            } else if (poiType.subCategory.equals("테니스장")) {
                retType = ActivityType.OutdoorExercise;
            } else if (poiType.subCategory.equals("수영장")) {
                retType = ActivityType.IndoorExercise;
            } else if (poiType.subCategory.equals("체육관")) {
                retType = ActivityType.IndoorExercise;
            } else if (poiType.subCategory.equals("골프장")) {
                retType = ActivityType.OutdoorExercise;
            } else {
                retType = ActivityType.OutdoorExercise;
            }
        } else if (poiType.category.equals("교통,수송")) {

        } else if (poiType.category.equals("가정,생활")) {
            retType = ActivityType.Shopping;

        } else if (poiType.category.equals("부동산")) {
            if (poiType.subCategory.equals("주거시설")) {
                retType = ActivityType.RestOrSleep;
            } else if (poiType.subCategory.equals("업무시설")) {
                retType = ActivityType.Work;
            }
        }
        return retType;
    }
}
