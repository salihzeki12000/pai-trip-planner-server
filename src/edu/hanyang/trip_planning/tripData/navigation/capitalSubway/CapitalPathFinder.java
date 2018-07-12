package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.dataType.WayOfMovement;
import edu.hanyang.trip_planning.tripData.navigation.InterfacePathFinder;
import edu.hanyang.trip_planning.tripData.navigation.MinimalPOI;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.hanyang.trip_planning.tripData.poi.POIUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 15
 * Time: 오후 3:43
 * To change this template use File | Settings | File Templates.
 */
public class CapitalPathFinder implements InterfacePathFinder {
    private static Logger logger = Logger.getLogger(CapitalPathFinder.class);
    CapitalSubwayGraph subwayGraph = new CapitalSubwayGraph();
    CapitalPOIsManager capitalPoiManager = new CapitalPOIsManager();

    public CapitalPathFinder() {
        subwayGraph.loadFromFiles("datafiles/movements/capital_subway.csv");
    }


    /**
     * 지하철을 이용한 이동
     *
     * @param src
     * @param dest
     * @return
     */
    public List<UnitMovement> findPathBySubway(InterfacePOI src, InterfacePOI dest) {
        // 1. src에서 가장 가까운 역을 찾고
        // 2. dest에서 가장 가까운 역을 찾는다.

        // 사실 가장 가까운거 말고, 후보지 몇개 고르는 것도 나쁘지는 않음.

// TODO
        MinimalPOI srcNearStn = capitalPoiManager.getNearestPOI(src.getLocation(), 1.0);
        MinimalPOI destNearStn = capitalPoiManager.getNearestPOI(dest.getLocation(), 1.0);
        double srcWalkDist = POIUtil.distance(src.getLocation(), srcNearStn.getLocation());
        double destWalkDist = POIUtil.distance(dest.getLocation(), destNearStn.getLocation());

        logger.debug("srcNearStn = " + srcNearStn);
        logger.debug("destNearStn = " + destNearStn);
//
        logger.debug("srcWalkDist=" + srcWalkDist);
        logger.debug("destWalkDist=" + destWalkDist);
//        public UnitMovement(POIIdentifier srcLocation, POIIdentifier destLocation, WayOfMovement way, ProbabilisticDuration spendingTime, int cost) {

        UnitMovement srcWalk = new UnitMovement(src.getTitle(), src.getLocation(), srcNearStn.getTitle(), srcNearStn.getLocation(), WayOfMovement.Walk, estimateTime(srcWalkDist), 0);
        UnitMovement subway = subwayGraph.findPath(srcNearStn, destNearStn);
        UnitMovement destWalk = new UnitMovement(destNearStn.getTitle(), destNearStn.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Walk, estimateTime(destWalkDist), 0);

//        subwayGraph.findPath(src.getIdentifier().name, dest.getIdentifier().name);

        List<UnitMovement> movementList = new ArrayList<UnitMovement>();
        movementList.add(srcWalk);
        movementList.add(subway);
        movementList.add(destWalk);
        return movementList;
    }


    /**
     * 지하철 역에서 일반적 장소까지
     *
     * @param src
     * @param dest
     * @return
     */
    public List<UnitMovement> findPathBySubway(MinimalPOI src, InterfacePOI dest) {
        // 2. dest에서 가장 가까운 역을 찾는다.
        // 사실 가장 가까운거 말고, 후보지 몇개 고르는 것도 나쁘지는 않음.
        MinimalPOI srcNearStn = src;
        MinimalPOI destNearStn = capitalPoiManager.getNearestPOI(dest.getLocation(), 1.0);
        double destWalkDist = POIUtil.distance(dest.getLocation(), destNearStn.getLocation());

        logger.debug("srcNearStn = " + srcNearStn);
        logger.debug("destNearStn = " + destNearStn);

        logger.debug("destWalkDist=" + destWalkDist);
//        public UnitMovement(POIIdentifier srcLocation, POIIdentifier destLocation, WayOfMovement way, ProbabilisticDuration spendingTime, int cost) {

        UnitMovement subway = subwayGraph.findPath(srcNearStn, destNearStn);
        UnitMovement destWalk = new UnitMovement(destNearStn.getTitle(), destNearStn.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Walk, estimateTime(destWalkDist), 0);

//        subwayGraph.findPath(src.getIdentifier().name, dest.getIdentifier().name);

        List<UnitMovement> movementList = new ArrayList<UnitMovement>();
        movementList.add(subway);
        movementList.add(destWalk);
        return movementList;
    }

    /**
     * 지하철 역에서 일반적 장소까지
     *
     * @param src
     * @param dest
     * @return
     */
    public List<UnitMovement> findPathBySubway(InterfacePOI src, MinimalPOI dest) {
        MinimalPOI srcNearStn = capitalPoiManager.getNearestPOI(src.getLocation(), 1.0);
        double srcWalkDist = POIUtil.distance(src.getLocation(), srcNearStn.getLocation());

//        logger.debug("srcNearStn = " + srcNearStn);

//        logger.debug("srcWalkDist=" + srcWalkDist);
//        public UnitMovement(POIIdentifier srcLocation, POIIdentifier destLocation, WayOfMovement way, ProbabilisticDuration spendingTime, int cost) {

        UnitMovement srcWalk = new UnitMovement(src.getTitle(), src.getLocation(), dest.getTitle(), dest.getLocation(), WayOfMovement.Walk, estimateTime(srcWalkDist), 0);
        UnitMovement subway = subwayGraph.findPath(srcNearStn, dest);


//        subwayGraph.findPath(src.getIdentifier().name, dest.getIdentifier().name);

        List<UnitMovement> movementList = new ArrayList<UnitMovement>();
        movementList.add(srcWalk);
        movementList.add(subway);
        return movementList;
    }

    public List<UnitMovement> findPathBySubway(MinimalPOI src, MinimalPOI dest) {
        UnitMovement subway = subwayGraph.findPath(src, dest);
        List<UnitMovement> movementList = new ArrayList<UnitMovement>();
        movementList.add(subway);
        return movementList;
    }

    @Override
    public List<UnitMovement> findPath(InterfacePOI src, InterfacePOI dest) {
        return findPathBySubway(src, dest);
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, InterfacePOI dest) {
        return findPathBySubway(src, dest);
    }

    @Override
    public List<UnitMovement> findPath(InterfacePOI src, MinimalPOI dest) {
        return findPathBySubway(src, dest);
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, MinimalPOI dest) {
        return findPathBySubway(src, dest);
    }

    private ProbabilisticDuration estimateTime(double distance) {
        //시속 3.5 km 가정

        double time = distance /  3.5;
        double sdTime = time * 0.1;
        logger.debug(time);
        return new ProbabilisticDuration(time, sdTime);
    }

    public static void main(String[] args) {
        CapitalPathFinder simplePathFinder = new CapitalPathFinder();
        InterfacePOI src = POIManager.getInstance().getPOIByTitle("창동주공4단지아파트");
        InterfacePOI dest = POIManager.getInstance().getPOIByTitle("김포국제공항 국내선");

        logger.debug(simplePathFinder.findPathBySubway(src, dest));

//        simplePathFinder
    }


}
