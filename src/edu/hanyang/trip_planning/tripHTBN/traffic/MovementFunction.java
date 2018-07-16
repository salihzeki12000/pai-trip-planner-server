package edu.hanyang.trip_planning.tripHTBN.traffic;


import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.navigation.DomesticNavigation;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import org.apache.log4j.Logger;
import util.Pair;

import java.util.List;

public class MovementFunction {
    private static Logger logger = Logger.getLogger(MovementFunction.class);
    SubsetPOIs subsetPOIs;
    DomesticNavigation navigation = DomesticNavigation.getInstance();
    List<UnitMovement> foundPath;

    public MovementFunction(SubsetPOIs subsetPOIs) {
        this.subsetPOIs = subsetPOIs;
    }

    public List<UnitMovement> findPath(int srcId, int destId) {
        BasicPOI src = subsetPOIs.getPOI(srcId);
        BasicPOI dest = subsetPOIs.getPOI(destId);
//        logger.debug("find path from " + src.getTitle() + " to " + dest.getTitle());
        foundPath = navigation.findPath(src, dest);
        return foundPath;
//        logger.debug(foundPath);
    }

    public Pair<Double, Double> getCost() {
        if (foundPath.size() == 0) {
            throw new RuntimeException("No path");
        }
        double totalCost = 0;
        for (UnitMovement unitMovement : foundPath) {
            totalCost += unitMovement.cost;
        }
        return new Pair<Double, Double>(totalCost, totalCost * 0.05);
    }

    /**
     * 시간 단위는 hour임
     * @return
     */
    public Pair<Double, Double> getTime() {
        double totalTime = 0.0;
        double totalSD = 0.0;
        if (foundPath.size() == 0) {
            throw new RuntimeException("No path");
        }
        for (UnitMovement unitMovement : foundPath) {
            totalTime = totalTime + unitMovement.spendingTime.hour;
//            logger.debug(unitMovement);
            totalSD = totalSD + unitMovement.spendingTime.standardDeviation;
        }
//        logger.debug("totlaTime="+totalTime);
        return new Pair<Double, Double>(totalTime, totalSD);
    }

    public SubsetPOIs getSubsetPOIs() {
        return subsetPOIs;
    }

    public String getName(int idx) {
        return subsetPOIs.getPOI(idx).getTitle();
    }

    public static void main(String[] args) {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("분당서울대학교병원", "교보문고 광화문점", "뚝섬한강공원", "창동주공4단지아파트", "한양대학교 정보통신관");
//        subsetPOIs.makeSubsetPOIsByTitle("분당서울대학교병원", "교보문고 광화문점", "뚝섬한강공원", "창동주공4단지아파트", "한양대학교 정보통신관");
        MovementFunction trafficCondition = new MovementFunction(subsetPOIs);
        for (int i = 1; i < 4; i++) {
            trafficCondition.findPath(0, i);
        }
        logger.debug("time=" + trafficCondition.getTime());
        logger.debug("cost=" + trafficCondition.getCost());
    }
}
