package edu.hanyang.trip_planning.optimize.aco.dummyProblem;

import edu.hanyang.trip_planning.optimize.aco.ACOProblem;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import org.apache.log4j.Logger;
import util.Erf;
import util.WeightedRandomSelector;

import java.util.*;

/**
 * Created by dummyProblem on 2016-10-27.
 */
public class DummyOrienteeringProblem implements ACOProblem {

    private static Logger logger = Logger.getLogger(DummyOrienteeringProblem.class);
    private int numFreeNode;
    private int numNodes = 6;
    private int startNodeIdx = 0;
    private int endNodeIdx = 0;
    private double totalTime = 0.0;
    private double totalValue = 0.0;
    // constraints
    private double timeLimit = 5.0;

    private List<Integer> trail = new ArrayList<>();

    private double distanceMatrix[][] = {{0, 83, 42, 165, 90, 145},
                                         {83, 0, 52, 84, 81, 116},
                                         {42, 52, 0, 128, 54, 107},
                                         {165, 84, 128, 0, 124, 120},
                                         {90, 81, 54, 124, 0, 55},
                                         {145, 116, 107, 120, 55, 0}};
    private double profits[] = {0.0, 0.5, 0.15, 0.3, 0.4, 0.6};
    private double spendingHour[] = {0.0, 0.3, 0.4, 0.5, 0.6, 1.6};

    private Set<Integer> availableNodes = new HashSet<>();
    private int curNodeIdx;
    private boolean terminalCondition = false;

    public DummyOrienteeringProblem(int startNodeIdx, int endNodeIdx) {
        this.startNodeIdx = startNodeIdx;
        this.endNodeIdx = endNodeIdx;
        if (startNodeIdx == endNodeIdx) {
            this.numFreeNode = numNodes - 1;
        } else {
            this.numFreeNode = numNodes - 2;
        }
        init();
    }

    @Override
    public void init() {
        this.trail.clear();
        this.availableNodes.clear();
        this.trail.add(startNodeIdx);
        curNodeIdx = startNodeIdx;
        for (int i = 1; i < numNodes; i++) {
            availableNodes.add(i);
        }
        this.totalTime = 0.0;
        this.totalValue = 0.0;
        terminalCondition = false;
    }

    @Override
    public double[] heuristicValue() {
        // 선택 가능한 노드들은?
        double sum = 0.0;
//        logger.debug("avaliable nodes: " + availableNodes);
        int srcNodeIdx = curNodeIdx;
        double retValues[] = new double[numNodes];
        for (int destNodeIdx : availableNodes) {
            double value = profits[destNodeIdx];
            double expectedTotalTime = totalTime + spendingHour[destNodeIdx] + movementTime(curNodeIdx, destNodeIdx);

            if (constraintViolation(expectedTotalTime)) {
//                logger.debug("constraint violation");
                value = 0.0;
            }
//            logger.debug(trail + "->" + destNodeIdx + "\tvalue=" + value);
            retValues[destNodeIdx] = value;
            sum += value;
        }
        if (sum == 0.0) {
            terminalCondition = true;
        }
        return retValues;
    }

    @Override
    public void addNodeToTrail(int destNodeIdx) {
        trail.add(destNodeIdx);
        availableNodes.remove(new Integer(destNodeIdx));
        if (availableNodes.size() == 0) {
            terminalCondition = true;
        }
        totalTime += spendingHour[destNodeIdx] + movementTime(curNodeIdx, destNodeIdx);
        totalValue += profits[destNodeIdx];
//        logger.debug("available nodes = " + availableNodes);
        curNodeIdx = destNodeIdx;

    }

    @Override
    public int getCurNode() {
        return curNodeIdx;
    }

    public double getTotalValue() {
        return totalValue;
    }

    @Override
    public int[] getPath() {
        return Erf.MyArrays.toIntArray(trail);
    }

    @Override
    public DetailItinerary result(int[] path) {
        return null;
    }




    public double getValue(int... path) {
        if (path.length >= numNodes) {
            throw new RuntimeException("path length error");
        }


        double s = 0.0;

        for (int t = 0; t < path.length; t++) {
            s += profits[path[t]];
        }

        double finalTime = computeTotalTime(path);
        if (constraintViolation(finalTime)) {
            return -100;
        } else {
            return s;
        }
    }

    @Override
    public int getNumNodes() {
        return 0;
    }



    private double computeTotalTime(int... path) {
        int srcNode = startNodeIdx;
        int destNode = -1;
        double totalTime = 0;
        for (int t = 0; t < path.length; t++) {
            destNode = path[t];
            totalTime = totalTime + spendingHour[destNode] + movementTime(srcNode, destNode);
//            logger.debug("t=" + totalTime);
            srcNode = destNode;
        }
        return totalTime;
    }

    public boolean isTerminalCondition() {
        return terminalCondition;
    }

    public boolean constraintViolation(double finalTime) {
        return finalTime > timeLimit;
    }

    private double movementTime(int srcNodeIdx, int destNodeIdx) {
        double distance = distanceMatrix[srcNodeIdx][destNodeIdx];
//        logger.debug("movement from " + srcNodeIdx + " to " + destNodeIdx + " = " + distance * 0.015);
        return distance * 0.02;
    }

    public static void localSearchSolution() {
        WeightedRandomSelector selection = new WeightedRandomSelector();
        DummyOrienteeringProblem op = new DummyOrienteeringProblem(0, 0);
        for (int t = 0; t < 10; t++) {
            if (op.isTerminalCondition()) {
                break;
            }
            double p[] = op.heuristicValue();
            int selectedNode = selection.select(p);
            op.addNodeToTrail(selectedNode);
            logger.debug(Arrays.toString(op.heuristicValue()));
        }

        logger.debug("total value=" + op.getTotalValue());

    }

    public static void main(String[] args) {
        localSearchSolution();
    }

}
