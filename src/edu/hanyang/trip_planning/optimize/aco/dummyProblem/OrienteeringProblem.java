package edu.hanyang.trip_planning.optimize.aco.dummyProblem;

import au.com.bytecode.opencsv.CSVReader;
import edu.hanyang.trip_planning.optimize.aco.*;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by dummyProblem on 2016-10-27.
 */
public class OrienteeringProblem extends ItineraryPlanning {

    private static Logger logger = Logger.getLogger(OrienteeringProblem.class);


    private double spendingTimeForTrail = 0.0;
    // constraints
    private double timeLimit;
    private double totalValue = 0.0;

    private double nodePositions[][];
    private double profits[];
    private double spendingHour[];


    public OrienteeringProblem(String filename, int startNodeIdx, int endNodeIdx, double timeLimit) {
        super(startNodeIdx, endNodeIdx);
        readFile(filename);
        this.timeLimit = timeLimit;
    }


    @Override
    protected void problemDependentInit() {
        spendingTimeForTrail = 0.0;
        totalValue = 0.0;
    }


    @Override
    public double[] heuristicValue() {
        double sum = 0.0;
        double retValues[] = new double[numNodes];
        for (int destNodeIdx : avaliableNodes) {
            double value = profits[destNodeIdx];

            if (constraintViolation(destNodeIdx)) {
                value = 0.0;
            }
            retValues[destNodeIdx] = value;
            sum += value;
        }
        if (sum == 0.0) {
            bTerminalCondition = true;
        }
        return retValues;
    }

    @Override
    public void addNodeToTrail(int destNodeIdx) {
        trail.add(destNodeIdx);
        avaliableNodes.remove(new Integer(destNodeIdx));
        if (avaliableNodes.size() == 0) {
            bTerminalCondition = true;
        }
        spendingTimeForTrail += movementTime(curNodeIdx, destNodeIdx) + spendingHour[destNodeIdx];
        totalValue += profits[destNodeIdx];

        curNodeIdx = destNodeIdx;
    }


    @Override
    public boolean isTerminalCondition() {
        return bTerminalCondition;
    }

    @Override
    public double getTotalValue() {
        return totalValue;
    }

    @Override
    public DetailItinerary result(int[] path) {

        /**
         *
         public void addEntry(String poiTitle, double arrivalTime, double departureTime, double cost) {
         */

        DetailItinerary detailItinerary = new DetailItinerary(null, 0.0, new BasicPOI(Integer.toString(startNodeIdx)), new BasicPOI(Integer.toString(endNodeIdx)));
        int curNode = startNodeIdx;
        int nextNode = 0;

        double arrivalTime;
        double departureTime = 0.0;

        for (int t = 0; t < path.length; t++) {
            nextNode = path[t];
            arrivalTime = departureTime + movementTime(curNode, nextNode);
            departureTime = arrivalTime + spendingHour[nextNode];
            logger.debug("movement time " + curNode + "->" + nextNode + "=" + movementTime(curNode, nextNode));
            detailItinerary.addEntry( new BasicPOI(Integer.toString(path[t])), arrivalTime, 0.0,departureTime, 0.0,0.0);
            curNode = nextNode;
        }

        double endTime = departureTime + movementTime(curNode, endNodeIdx);
        logger.debug("movement time " + curNode + "->" + endNodeIdx + "=" + movementTime(curNode, endNodeIdx));
        double tmp[] = new double[2];
        tmp[0]=endTime;
        detailItinerary.setEndTime(tmp);
//        detailItinerary.addEntry(Integer.toString(endNodeIdx), lastTime, lastTime, 0.0);
        return detailItinerary;
    }



    /**
     * 기존 경로에 더해서 destNode를 지나서 최종 목적지에 갈때까지의 constraint violation여부를 측정한다.
     */
    protected boolean constraintViolation(int destNodeIdx) {
        double expectedTotalTime = spendingTimeForTrail + movementTime(curNodeIdx, destNodeIdx) + spendingHour[destNodeIdx] + movementTime(destNodeIdx, endNodeIdx);
        return expectedTotalTime > timeLimit;
    }

    private double movementTime(int srcNodeIdx, int destNodeIdx) {
        double speed = 40.0;
        return distance(nodePositions[srcNodeIdx], nodePositions[destNodeIdx]) / speed;
    }


    private double distance(double srcNodePos[], double destNodePos[]) {
        double dx = srcNodePos[0] - destNodePos[0];
        double dy = srcNodePos[1] - destNodePos[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void readFile(String filename) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filename));
            List<String[]> strArrayList = csvReader.readAll();
            this.numNodes = strArrayList.size();
            nodePositions = new double[numNodes][2];
            profits = new double[numNodes];
            spendingHour = new double[numNodes];
            for (int i = 0; i < numNodes; i++) {
                nodePositions[i][0] = Double.parseDouble(strArrayList.get(i)[0]);
                nodePositions[i][1] = Double.parseDouble(strArrayList.get(i)[1]);
                profits[i] = Double.parseDouble(strArrayList.get(i)[2]);
                spendingHour[i] = Double.parseDouble(strArrayList.get(i)[3]);
                logger.debug("pos=(" + nodePositions[i][0] + "," + nodePositions[i][1] + ")\tprofit" + profits[i] + "\tspendingHour" + spendingHour[i]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        OrienteeringProblem op = new OrienteeringProblem("datafile/nodes.csv", 0, 0, 6.0);
        ACOParameters acoParameters = ACOParameterFactory.simpleParamGen();
        ACOptimizer ants = new ACOptimizer(op, acoParameters);

        ScoredPath solutions[] = ants.optimize(10);
        Arrays.sort(solutions);

        for (ScoredPath p : solutions) {
            logger.debug(p);

        }
//        for (ScoredPath p : solutions) {
////            logger.debug(p);
//            logger.debug(op.result(p.getPath()).toDetailString());
//            ;
//        }


    }


}
