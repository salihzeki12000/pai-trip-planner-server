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
        foundPath = navigation.findPath(src, dest);
        return foundPath;
    }

    public Pair<Double, Double> getCost() {
        if (foundPath.size() == 0) {
            throw new RuntimeException("No path");
        }
        double totalCost = 0;
        for (UnitMovement unitMovement : foundPath) {
            totalCost += unitMovement.cost;
        }
        return new Pair<>(totalCost, totalCost * 0.05);
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
            totalSD = totalSD + unitMovement.spendingTime.standardDeviation;
        }
        return new Pair<>(totalTime, totalSD);
    }
}
