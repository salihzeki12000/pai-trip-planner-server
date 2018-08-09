package edu.hanyang.trip_planning.optimize.aco.dummyProblem;

import edu.hanyang.trip_planning.optimize.aco.ACOProblem;

import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;
import util.Erf;
import util.WeightedRandomSelector;

import java.util.*;

/**
 * Created by dummyProblem on 2016-10-27.
 */
public class AdvancedDummyOrienteeringProblem implements ACOProblem {

    private static Logger logger = Logger.getLogger(AdvancedDummyOrienteeringProblem.class);
    private int numFreeNode;
    private int numNodes = 6;
    private int startNodeIdx = 0;
    private int endNodeIdx = 0;
    private double totalTime = 0.0;
    private double totalValue = 0.0;
    // constraints
    private double timeLimit = 8.0;

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

    public AdvancedDummyOrienteeringProblem(int startNodeIdx, int endNodeIdx) {
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
        curNodeIdx = startNodeIdx;
        for (int i = 0; i < numNodes; i++) {
            if (i != startNodeIdx && i != endNodeIdx) {
                availableNodes.add(i);
            }
        }
        this.totalTime = 0.0;
        this.totalValue = 0.0;
        terminalCondition = false;
    }

    @Override
    public double[] heuristicValue() {
        // 선택 가능한 노드들은?
        double sum = 0.0;
//        logger.debug("curNode="+curNodeIdx + "\tavaliable nodes: " + availableNodes);

        double retValues[] = new double[numNodes];
        for (int destNodeIdx : availableNodes) {
            double value = profits[destNodeIdx];
//            double expectedTotalTime = totalTime + spendingHour[destNodeIdx] + movementTime(curNodeIdx, destNodeIdx);
//            logger.debug(curNodeIdx + "->"+ destNodeIdx);
            if (returnTimeConstraintViolation(destNodeIdx)) {
                value = 0.0;
            }
//            logger.debug(trail + "->" + destNodeIdx + "\tvalue=" + value);
            retValues[destNodeIdx] = value;
            sum += value;
        }
        if (sum < 1E-10) {
            terminalCondition = true;
        }

        logger.debug(Arrays.toString(retValues));
        return retValues;
    }

    @Override
    public void addNodeToTrail(int destNodeIdx) {
        logger.debug("add node " + destNodeIdx);
        trail.add(destNodeIdx);
        availableNodes.remove(destNodeIdx);
        if (availableNodes.size() == 0) {
            terminalCondition = true;
        }
        totalTime += movementTime(curNodeIdx, destNodeIdx) + spendingHour[destNodeIdx];
        totalValue += profits[destNodeIdx];
        logger.debug("available nodes = " + availableNodes);
        curNodeIdx = destNodeIdx;

    }

    @Override
    public int getCurNode() {
        return curNodeIdx;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }

    @Override
    public int[] getPath() {
        return Erf.MyArrays.toIntArray(trail);
    }

    @Override
    public DetailItinerary result(int[] path) {

        /**
         *
         public void addEntry(String poiTitle, double arrivalTime, double departureTime, double cost) {
         */
        DetailItinerary detailItinerary = new DetailItinerary(null, 0.0, new BasicPOI(Integer.toString(startNodeIdx)), new BasicPOI(Integer.toString(endNodeIdx)));
//        DetailItinerary detailItinerary = new DetailItinerary(null, 0.0, Integer.toString(startNodeIdx), Integer.toString(endNodeIdx));
        int curNode = startNodeIdx;
        int nextNode = 0;

        double arrivalTime;
        double departureTime = 0.0;

        for (int t = 0; t < path.length; t++) {
            nextNode = path[t];
            arrivalTime = departureTime + movementTime(curNode, nextNode);
            departureTime = arrivalTime + spendingHour[nextNode];
            logger.debug("movement time " + curNode + "->" + nextNode + "=" + movementTime(curNode, nextNode));
            detailItinerary.addEntry(new BasicPOI(Integer.toString(path[t])), arrivalTime, 0.0, departureTime, 0.0, 0.0);
//            detailItinerary.addEntry(Integer.toString(path[t]), arrivalTime, departureTime, 0.0,0.0,0.0);
            curNode = nextNode;
        }

        double endTime = departureTime + movementTime(curNode, endNodeIdx);
        logger.debug("movement time " + curNode + "->" + endNodeIdx + "=" + movementTime(curNode, endNodeIdx));
        double tmp[] = new double[2];
        tmp[0] = endTime;
        detailItinerary.setEndTime(tmp);

//        detailItinerary.addEntry(Integer.toString(endNodeIdx), lastTime, lastTime, 0.0);
        return detailItinerary;
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


    public int getNumNodes() {
        return numNodes;
    }


    /**
     * 시작노드부터 해서 endNode까지 돌아오는 시간 계산할것
     *
     * @param path
     * @return
     */
    private double computeTotalTime(int... path) {
        // 시작 노드부터 돌아오는 노드까지의 소요시간 표현할것
        int srcNode = startNodeIdx;
        int destNode = endNodeIdx;
        double totalTime = 0;
        for (int t = 0; t < path.length; t++) {
            destNode = path[t];
            totalTime = totalTime + spendingHour[destNode] + movementTime(srcNode, destNode);
//            logger.debug("t=" + totalTime);
            srcNode = destNode;
        }

        double returnTime = movementTime(destNode, endNodeIdx);
        logger.debug("compute Total Time=" + (totalTime + returnTime));
        return totalTime + returnTime;
    }

    public boolean isTerminalCondition() {
        return terminalCondition;
    }

    public boolean constraintViolation(double finalTime) {
        return finalTime > timeLimit;
    }

    /**
     * 현재 노드부터 Dest node들러서 머문다음에 마지막 목적지까지 오는 시간이 Time limit 안에 드는지 여부를 확인함
     *
     * @param destNodeIdx
     * @return
     */
    public boolean returnTimeConstraintViolation(int destNodeIdx) {

        double expectedReturnTime = totalTime + movementTime(curNodeIdx, destNodeIdx) + spendingHour[destNodeIdx] + movementTime(destNodeIdx, endNodeIdx);

        if (expectedReturnTime > timeLimit) {
            logger.debug("constraint violation " + expectedReturnTime);
            return true;
        } else {
            logger.debug("No constraint violation " + expectedReturnTime);
            return false;
        }
    }

    private double movementTime(int srcNodeIdx, int destNodeIdx) {
        double distance = distanceMatrix[srcNodeIdx][destNodeIdx];
//        logger.debug("movement from " + srcNodeIdx + " to " + destNodeIdx + " = " + distance * 0.015);
        return distance * 0.02;
    }


    public static void localSearchSolution() {
        WeightedRandomSelector selection = new WeightedRandomSelector();
        AdvancedDummyOrienteeringProblem op = new AdvancedDummyOrienteeringProblem(0, 0);
        for (int t = 0; t < 10; t++) {
            if (op.isTerminalCondition()) {
                break;
            }
            double p[] = op.heuristicValue();
            int selectedNode = selection.select(p);
            op.addNodeToTrail(selectedNode);
            logger.debug(Arrays.toString(op.heuristicValue()));
        }
        logger.debug("path=" + Arrays.toString(op.getPath()) + "\ttotal value=" + op.getTotalValue());
        logger.debug("detail itinerary=" + op.result(op.getPath()));

    }

    public static void main(String[] args) {
        localSearchSolution();
//        exhaustiveSolution();
    }

}
