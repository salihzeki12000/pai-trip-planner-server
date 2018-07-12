package edu.hanyang.trip_planning.tripData.navigation;



import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.dataType.WayOfMovement;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 5:14
 * To change this template use File | Settings | File Templates.
 */
public class InterRegionPathFinder implements InterfacePathFinder {
    @Override
    public List<UnitMovement> findPath(InterfacePOI src, InterfacePOI dest) {
        if (src.getTitle().equals("김포공항") && dest.getTitle().equals("제주국제공항")) {
            List<UnitMovement> list = new ArrayList<UnitMovement>();
            ProbabilisticDuration dur = new ProbabilisticDuration(130, 20);
            UnitMovement unitMovement = new UnitMovement(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Airway, dur, 90000);
            list.add(unitMovement);
            return list;
        }
        throw new RuntimeException("Not implemented for " + src + "==>" + dest);
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, InterfacePOI dest) {
        if (src.getTitle().equals("김포공항") && dest.getTitle().equals("제주국제공항")) {
            List<UnitMovement> list = new ArrayList<UnitMovement>();
            ProbabilisticDuration dur = new ProbabilisticDuration(130, 20);
            UnitMovement unitMovement = new UnitMovement(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Airway, dur, 90000);
            list.add(unitMovement);
            return list;
        }
        throw new RuntimeException("Not implemented for " + src + "==>" + dest);
    }

    @Override
    public List<UnitMovement> findPath(InterfacePOI src, MinimalPOI dest) {
        if (src.getTitle().equals("김포공항") && dest.getTitle().equals("제주국제공항")) {
            List<UnitMovement> list = new ArrayList<UnitMovement>();
            ProbabilisticDuration dur = new ProbabilisticDuration(130, 20);
            UnitMovement unitMovement = new UnitMovement(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Airway, dur, 90000);
            list.add(unitMovement);
            return list;
        }
        throw new RuntimeException("Not implemented for " + src + "==>" + dest);
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, MinimalPOI dest) {
        if (src.getTitle().equals("김포공항") && dest.getTitle().equals("제주국제공항")) {
            List<UnitMovement> list = new ArrayList<UnitMovement>();
            ProbabilisticDuration dur = new ProbabilisticDuration(130, 20);
            UnitMovement unitMovement = new UnitMovement(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Airway, dur, 90000);
            list.add(unitMovement);
            return list;
        }
        throw new RuntimeException("Not implemented for " + src + "==>" + dest);
    }
}
