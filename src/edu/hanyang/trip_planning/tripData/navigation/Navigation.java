package edu.hanyang.trip_planning.tripData.navigation;


import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.dataType.WayOfMovement;
import edu.hanyang.trip_planning.tripData.poi.BasicPoi;
import edu.hanyang.trip_planning.tripData.poi.PoiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 3:37
 * To change this template use File | Settings | File Templates.
 */
public class Navigation {
    private static Navigation instance = null;

    private Navigation() {}

    public static Navigation getInstance() {
        if (instance == null) {
            instance = new Navigation();
        }
        return instance;
    }

    public List<UnitMovement> findPath(BasicPoi src, BasicPoi dest) {
        return findPath(src.getName(), src.getLocation(), dest.getName(), dest.getLocation());
    }

    private List<UnitMovement> findPath(String srcTitle, Location srcLocation, String destTitle, Location destLocation) {
        double distance = PoiUtil.distance(srcLocation, destLocation);
        ProbabilisticDuration dur = calculateTime(srcLocation, destLocation);
        int cost = (int) (distance * 1.3 * 400) + 3000;
        UnitMovement unitMovement = new UnitMovement(srcTitle, srcLocation, destTitle, destLocation, WayOfMovement.Taxi, dur, cost);
        List<UnitMovement> list = new ArrayList<>();
        list.add(unitMovement);
        return list;
    }

    /**
     * dist/ (D km/h)
     * 1. 진출입 시간 - 앞뒤로 500 m 씩 빼보자고,  4km/h
     * 2. worming up - 2km, 2km                 14km/h
     * 3.
     */
    private ProbabilisticDuration calculateTime(Location srcLocation, Location destLocation) {
        double distance = PoiUtil.distance(srcLocation, destLocation);

        double dist_1 = 0.4;
        double dist_2 = 4;

        double speed_1 = 4;
        double speed_2 = 30;
        double speed_3 = 45;

        double walk = 4 / 60.0;

        double hour;
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
            hour += (dist_1 + dist_2) / speed_2;
            hour_sd += hour * 0.3;
            hour += (distance - dist_1 - dist_2) / speed_3;
            hour_sd += hour * 0.15;
        }
        return new ProbabilisticDuration(hour, hour_sd);
    }
}
