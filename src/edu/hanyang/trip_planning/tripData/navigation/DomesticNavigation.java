package edu.hanyang.trip_planning.tripData.navigation;


import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.navigation.capitalSubway.CapitalPathFinder;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
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
public class DomesticNavigation implements InterfacePathFinder {
    private static Logger logger = Logger.getLogger(DomesticNavigation.class);

    private Map<String, InterfacePathFinder> intraRegionPathFinderMap = new HashMap<String, InterfacePathFinder>();
    DistanceBasedPathFinder defaultPathFinder = new DistanceBasedPathFinder();
    //    private InterRegionPathFinder interRegionPathFinder = new InterRegionPathFinder();
    private DomesticRegion domesticRegion;
    private DomesticNavigationPOIsManager domesticNavigationPOIsManager = new DomesticNavigationPOIsManager();

    private DomesticNavigationTable domesticNavigationTable;

    private static DomesticNavigation instance = null;

    private DomesticNavigation() {
        init();
    }

    public static DomesticNavigation getInstance() {

        if (instance == null) {
            instance = new DomesticNavigation();
        }
        return instance;
    }

    public void init() {
        domesticRegion = new DomesticRegion();
        domesticNavigationTable = new DomesticNavigationTable();
        intraRegionPathFinderMap.put("수도권", new CapitalPathFinder());
        intraRegionPathFinderMap.put("제주", new DistanceBasedPathFinder());
    }

    @Override
    public List<UnitMovement> findPath(InterfacePOI src, InterfacePOI dest) {
//        logger.debug("pathFinding from " + src.getTitle() + " to " + dest.getTitle());

        if (domesticRegion.isSameRegion(src.getAddress().addressCode, dest.getAddress().addressCode)) {
//            logger.debug("same region");
            return findPathInSameRegion(src, dest);
        } else {
//            logger.debug("different region");
            return findPathInDifferentRegion(src, dest);
        }
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, InterfacePOI dest) {
        logger.debug("pathFinding from " + src.getTitle() + " to " + dest.getTitle());

        if (domesticRegion.isSameRegion(src.getAddressCode(), dest.getAddress().addressCode)) {
            logger.debug("same region");
            return findPathInSameRegion(src, dest);
        } else {
            logger.debug("different region");
            return findPathInDifferentRegion(src, dest);
        }
    }

    @Override
    public List<UnitMovement> findPath(InterfacePOI src, MinimalPOI dest) {
        logger.debug("pathFinding from " + src.getTitle() + " to " + dest.getTitle());

        if (domesticRegion.isSameRegion(src.getAddress().addressCode, dest.getAddressCode())) {
            logger.debug("same region");
            return findPathInSameRegion(src, dest);
        } else {
            logger.debug("different region");
            return findPathInDifferentRegion(src, dest);
        }
    }

    @Override
    public List<UnitMovement> findPath(MinimalPOI src, MinimalPOI dest) {
        logger.debug("pathFinding from " + src.getTitle() + " to " + dest.getTitle());

        if (domesticRegion.isSameRegion(src.getAddressCode(), dest.getAddressCode())) {
            logger.debug("same region");
            return findPathInSameRegion(src, dest);
        } else {
            logger.debug("different region");
            return findPathInDifferentRegion(src, dest);
        }
    }

    private List<UnitMovement> findPathInDifferentRegion(InterfacePOI src, InterfacePOI dest) {
        String srcRegion = domesticRegion.getRegion(src.getAddress().addressCode);
        String destRegion = domesticRegion.getRegion(dest.getAddress().addressCode);
//        logger.debug("srcRegion = " + srcRegion);
//        logger.debug("destRegion = " + destRegion);
//        domesticNavigationTable.getBase
        UnitMovement baseMovement = domesticNavigationTable.getBaseMovement(srcRegion, destRegion);
        // Base 는 당연히 minimalPOI인게지

        MinimalPOI srcBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.srcTitle);
        MinimalPOI destBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.destTitle);

//        logger.debug(baseMovement);


//        logger.debug(src.getTitle() + "==>" + srcBasePOI);
//        logger.debug(dest.getTitle() + "==>" + destBasePOI);
        List<UnitMovement> srcToBase = findPathInSameRegion(src, srcBasePOI);
        List<UnitMovement> baseToDest = findPathInSameRegion(destBasePOI, dest);

        List<UnitMovement> finalList = new ArrayList<UnitMovement>();
        finalList.addAll(srcToBase);
        finalList.add(baseMovement);
        finalList.addAll(baseToDest);

        return finalList;
    }


    private List<UnitMovement> findPathInDifferentRegion(InterfacePOI src, MinimalPOI dest) {
        String srcRegion = domesticRegion.getRegion(src.getAddress().addressCode);
        String destRegion = domesticRegion.getRegion(dest.getAddressCode());
        logger.debug("srcRegion = " + srcRegion);
        logger.debug("destRegion = " + destRegion);
//        domesticNavigationTable.getBase
        UnitMovement baseMovement = domesticNavigationTable.getBaseMovement(srcRegion, destRegion);
        // Base 는 당연히 minimalPOI인게지

        MinimalPOI srcBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.srcTitle);
        MinimalPOI destBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.destTitle);

        logger.debug(baseMovement);


        logger.debug(src.getTitle() + "==>" + srcBasePOI);
        logger.debug(dest.getTitle() + "==>" + destBasePOI);
        List<UnitMovement> srcToBase = findPathInSameRegion(src, srcBasePOI);
        List<UnitMovement> baseToDest = findPathInSameRegion(destBasePOI, dest);

        List<UnitMovement> finalList = new ArrayList<UnitMovement>();
        finalList.addAll(srcToBase);
        finalList.add(baseMovement);
        finalList.addAll(baseToDest);

        return finalList;
    }

    private List<UnitMovement> findPathInDifferentRegion(MinimalPOI src, InterfacePOI dest) {
        String srcRegion = domesticRegion.getRegion(src.getAddressCode());
        String destRegion = domesticRegion.getRegion(dest.getAddress().addressCode);
        logger.debug("srcRegion = " + srcRegion);
        logger.debug("destRegion = " + destRegion);
//        domesticNavigationTable.getBase
        UnitMovement baseMovement = domesticNavigationTable.getBaseMovement(srcRegion, destRegion);
        // Base 는 당연히 minimalPOI인게지

        MinimalPOI srcBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.srcTitle);
        MinimalPOI destBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.destTitle);

        logger.debug(baseMovement);


        logger.debug(src.getTitle() + "==>" + srcBasePOI);
        logger.debug(dest.getTitle() + "==>" + destBasePOI);
        List<UnitMovement> srcToBase = findPathInSameRegion(src, srcBasePOI);
        List<UnitMovement> baseToDest = findPathInSameRegion(destBasePOI, dest);

        List<UnitMovement> finalList = new ArrayList<UnitMovement>();
        finalList.addAll(srcToBase);
        finalList.add(baseMovement);
        finalList.addAll(baseToDest);

        return finalList;
    }

    private List<UnitMovement> findPathInDifferentRegion(MinimalPOI src, MinimalPOI dest) {
        String srcRegion = domesticRegion.getRegion(src.getAddressCode());
        String destRegion = domesticRegion.getRegion(dest.getAddressCode());
        logger.debug("srcRegion = " + srcRegion);
        logger.debug("destRegion = " + destRegion);
//        domesticNavigationTable.getBase
        UnitMovement baseMovement = domesticNavigationTable.getBaseMovement(srcRegion, destRegion);
        // Base 는 당연히 minimalPOI인게지

        MinimalPOI srcBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.srcTitle);
        MinimalPOI destBasePOI = domesticNavigationPOIsManager.getPOI(baseMovement.destTitle);

        logger.debug(baseMovement);


        logger.debug(src.getTitle() + "==>" + srcBasePOI);
        logger.debug(dest.getTitle() + "==>" + destBasePOI);
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
    private List<UnitMovement> findPathInSameRegion(InterfacePOI src, InterfacePOI dest) {
        String region = domesticRegion.getRegion(src.getAddress().addressCode);
        InterfacePathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    private List<UnitMovement> findPathInSameRegion(MinimalPOI src, InterfacePOI dest) {
        String region = domesticRegion.getRegion(src.getAddressCode());
        InterfacePathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    private List<UnitMovement> findPathInSameRegion(InterfacePOI src, MinimalPOI dest) {
        String region = domesticRegion.getRegion(src.getAddress().addressCode);
        InterfacePathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }

    private List<UnitMovement> findPathInSameRegion(MinimalPOI src, MinimalPOI dest) {
        String region = domesticRegion.getRegion(src.getAddressCode());
        InterfacePathFinder pathFinder = intraRegionPathFinderMap.get(region);
        if (pathFinder == null) {
            return defaultPathFinder.findPath(src, dest);
        }
        return pathFinder.findPath(src, dest);
    }
/*

    private List<UnitMovement> findPathInDifferentRegion(InterfacePOI src, InterfacePOI dest) {
        DomesticRegion srcRegion = DomesticRegion.getRegion(src);
        DomesticRegion destRegion = DomesticRegion.getRegion(dest);
        Pair<InterfacePOI,InterfacePOI> basePair = getBase(srcRegion, destRegion);
        InterfacePOI srcBasePOI =basePair.first();
        InterfacePOI destBasePOI =basePair.second();
        logger.debug(src.getTitle() + "==>" + srcBasePOI);
        logger.debug(dest.getTitle() + "==>" + destBasePOI);
        List<UnitMovement> srcToBase =  findPathInSameRegion(src,srcBasePOI);
        List<UnitMovement> interRegion = interRegionPathFinder.findPath(srcBasePOI, destBasePOI);
        List<UnitMovement> baseToDest =  findPathInSameRegion(destBasePOI,dest);
        List<UnitMovement> finalList = new ArrayList<UnitMovement>();
        finalList.addAll(srcToBase);
        finalList.addAll(interRegion);
        finalList.addAll(baseToDest);

        return finalList;
    }
    */

    /**
     * 다른 지역을 지나가기 위한 거점을 찾는다.
     * <p/>
     * //     * @param srcRegion
     * //     * @param destRegion
     *
     * @return
     */
//    private Pair<InterfacePOI, InterfacePOI> getBase(DomesticRegion srcRegion, DomesticRegion destRegion) {
//        if (srcRegion == DomesticRegion.수도권 && destRegion == DomesticRegion.제주도) {
//            InterfacePOI srcBasePOI = POIManager.getInstance().getPOIByID(ID_김포국제공항_국내선);
//            InterfacePOI destBasePOI = POIManager.getInstance().getPOIByID(ID_제주국제공항);
//            return new Pair<InterfacePOI, InterfacePOI>(srcBasePOI,destBasePOI);
//        } else if (srcRegion == DomesticRegion.제주도 && destRegion == DomesticRegion.수도권) {
//            InterfacePOI srcBasePOI = POIManager.getInstance().getPOIByID(ID_제주국제공항);
//            InterfacePOI destBasePOI = POIManager.getInstance().getPOIByID(ID_김포국제공항_국내선);
//            return new Pair<InterfacePOI, InterfacePOI>(srcBasePOI,destBasePOI);
//        }
//        throw new RuntimeException("Not implemented for " + srcRegion + "-->"+ destRegion);
//
//    }
    public static void testSameRegion() {

        InterfacePOI srcPOI = POIManager.getInstance().getPOIByTitle("창동주공4단지아파트");
        InterfacePOI destPOI = POIManager.getInstance().getPOIByTitle("한양대학교 정보통신관");
        DomesticNavigation domesticNavigation = new DomesticNavigation();
        List<UnitMovement> list = domesticNavigation.findPath(srcPOI, destPOI);
        for (UnitMovement unitMovement : list) {
            logger.debug(unitMovement);
        }
    }

    public static void testDifferentRegion() {

        InterfacePOI srcPOI = POIManager.getInstance().getPOIByTitle("창동주공4단지아파트");
        InterfacePOI destPOI = POIManager.getInstance().getPOIByTitle("한국 산업기술평가관리원 대전사무소");
        DomesticNavigation domesticNavigation = new DomesticNavigation();
        List<UnitMovement> list = domesticNavigation.findPath(srcPOI, destPOI);
        for (UnitMovement unitMovement : list) {
            logger.debug(unitMovement);
        }
    }

    public static void main(String[] args) {

        testSameRegion();
        testDifferentRegion();
//        logger.debug(poi1);

    }
}
