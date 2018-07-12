package edu.hanyang.trip_planning.tripData.navigation;


import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 4:40
 * To change this template use File | Settings | File Templates.
 */
public interface InterfacePathFinder {
    List<UnitMovement> findPath(InterfacePOI src, InterfacePOI dest);

    List<UnitMovement> findPath(MinimalPOI src, InterfacePOI dest);

    List<UnitMovement> findPath(InterfacePOI src, MinimalPOI dest);

    List<UnitMovement> findPath(MinimalPOI src, MinimalPOI dest);
}
