package edu.hanyang.trip_planning.tripData.navigation;


import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.dataType.WayOfMovement;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 5:51
 * To change this template use File | Settings | File Templates.
 */
public class DistanceBasedPathFinder {
    public List<UnitMovement> findPath(BasicPOI src, BasicPOI dest) {
        return findPath(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation());
    }

    public List<UnitMovement> findPath(BasicPOI src, MinimalPOI dest) {
        return findPath(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation());
    }

    public List<UnitMovement> findPath(MinimalPOI src, BasicPOI dest) {
        return findPath(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation());
    }

    private List<UnitMovement> findPath(String srcTitle, Location srcLocation, String destTitle, Location destLocation) {
        double distance = POIUtil.distance(srcLocation, destLocation);
        ProbabilisticDuration dur = calculateTime(srcLocation, destLocation);
        int cost = (int) (distance * 1.3 * 400) + 3000;
        UnitMovement unitMovement = new UnitMovement(srcTitle, srcLocation, destTitle, destLocation, WayOfMovement.Taxi, dur, cost);
        List<UnitMovement> list = new ArrayList<UnitMovement>();
        list.add(unitMovement);
        return list;

    }

    private ProbabilisticDuration calculateTime(Location srcLocation, Location destLocation) {
        double distance = POIUtil.distance(srcLocation, destLocation);

        double dist_1 = 0.4;
        double dist_2 = 4;


        double speed_1 = 4;
        double speed_2 = 30;
        double speed_3 = 45;

        double walk = 4 / 60.0;
        /**
         * dist/ (D km/h)
         * 1. 진출입 시간 - 앞뒤로 500 m 씩 빼보자고,  4km/h
         * 2. worming up - 2km, 2km                 14km/h
         * 3.
         */

        double hour ;
        double hour_sd = 0.0;
        if (distance < dist_1) {
            hour = distance / speed_1 + walk;
            hour_sd = hour * 0.15;
        } else if (distance < dist_1 + dist_2) {
            hour = dist_1 / speed_1 + walk;
            hour_sd += hour * 0.15;
            hour += (distance - dist_1) / speed_2;
            hour_sd += hour * 0.3;
        } else {
            hour = dist_1 / speed_1 + walk;
            hour_sd += hour * 0.15;
//            logger.debug("firsttime+" + hour);
            hour += (dist_1 + dist_2) / speed_2;
            hour_sd += hour * 0.3;
//            logger.debug("secondtime+" + ((distance - dist_1) / speed_2));
            hour += (distance - dist_1 - dist_2) / speed_3;
            hour_sd += hour * 0.15;
//            logger.debug("thrid=" + ((distance - dist_1 - dist_2) / speed_3));
        }
        return new ProbabilisticDuration(hour , hour_sd );
    }
}
