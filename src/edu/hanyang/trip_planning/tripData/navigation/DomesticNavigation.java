package edu.hanyang.trip_planning.tripData.navigation;


import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 3:37
 * To change this template use File | Settings | File Templates.
 */
public class DomesticNavigation {
    private static Logger logger = Logger.getLogger(DomesticNavigation.class);

    private Map<String, DistanceBasedPathFinder> intraRegionPathFinderMap = new HashMap<>();
    DistanceBasedPathFinder defaultPathFinder = new DistanceBasedPathFinder();
    private DomesticRegion domesticRegion;
    private DomesticNavigationPOIsManager domesticNavigationPOIsManager = new DomesticNavigationPOIsManager();

    private DomesticNavigationTable domesticNavigationTable;

    private static DomesticNavigation instance = null;

    private DomesticNavigation() {
        init();
    }

    public void init() {
        domesticRegion = new DomesticRegion();
        domesticNavigationTable = new DomesticNavigationTable();
        intraRegionPathFinderMap.put("제주", new DistanceBasedPathFinder());
    }

    public static DomesticNavigation getInstance() {

        if (instance == null) {
            instance = new DomesticNavigation();
        }
        return instance;
    }

    public List<UnitMovement> findPath(BasicPOI src, BasicPOI dest) {
        if (domesticRegion.isSameRegion(src.getAddress().addressCode, dest.getAddress().addressCode)) {
            return findPathInSameRegion(src, dest);
        } else {
            return findPathInDifferentRegion(src, dest);
        }
    }

    private List<UnitMovement> findPathInDifferentRegion(BasicPOI src, BasicPOI dest) {
        String srcRegion = domesticRegion.getRegion(src.getAddress().addressCode);
        String destRegion = domesticRegion.getRegion(dest.getAddress().addressCode);
        UnitMovement baseMovement = domesticNavigationTable.getBaseMovement(srcRegion, destRegion);
        MinimalPOI srcBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.srcTitle);
        MinimalPOI destBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.destTitle);
        List<UnitMovement> srcToBase = findPathInSameRegion(src, srcBasePOI);
        List<UnitMovement> baseToDest = findPathInSameRegion(destBasePOI, dest);

        List<UnitMovement> finalList = new ArrayList<UnitMovement>();
        finalList.addAll(srcToBase);
        finalList.add(baseMovement);
        finalList.addAll(baseToDest);

        return finalList;
    }

    /**
     * 일반적인 장소에서 장소로 이동할것
     *
     * @param src
     * @param dest
     * @return
     */
    private List<UnitMovement> findPathInSameRegion(BasicPOI src, BasicPOI dest) {
        String region = domesticRegion.getRegion(src.getAddress().addressCode);
        DistanceBasedPathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    private List<UnitMovement> findPathInSameRegion(MinimalPOI src, BasicPOI dest) {
        String region = domesticRegion.getRegion(src.getAddressCode());
        DistanceBasedPathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    private List<UnitMovement> findPathInSameRegion(BasicPOI src, MinimalPOI dest) {
        String region = domesticRegion.getRegion(src.getAddress().addressCode);
        DistanceBasedPathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    public static void main(String[] args) {
    }
}
