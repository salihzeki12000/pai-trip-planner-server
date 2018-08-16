package tripPlanning.tripHTBN.traffic;

import tripPlanning.tripData.dataType.UnitMovement;
import tripPlanning.tripData.navigation.Navigation;
import tripPlanning.tripData.poi.BasicPoi;
import tripPlanning.tripHTBN.poi.SubsetPois;
import util.Pair;

import java.util.List;

public class MovementFunction {
    SubsetPois subsetPois;
    Navigation navigation = Navigation.getInstance();
    List<UnitMovement> foundPath;

    public MovementFunction(SubsetPois subsetPois) {
        this.subsetPois = subsetPois;
    }

    public List<UnitMovement> findPath(int srcId, int destId) {
        BasicPoi src = subsetPois.getPoi(srcId);
        BasicPoi dest = subsetPois.getPoi(destId);
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
     *
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
