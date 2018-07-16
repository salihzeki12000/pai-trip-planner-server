package edu.hanyang.trip_planning.tripHTBN;

import cntbn.common.NodeDictionary;
import cntbn.terms_factors.ConditionalLinearGaussian;
import cntbn.terms_factors.SimpleGaussian;

import edu.hanyang.trip_planning.tripHTBN.traffic.MovementFunction;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripHTBN.dynamicPotential.DiscreteTimeCPD;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIGen;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicCLGCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicHybridCPD;
import org.apache.log4j.Logger;
import util.Pair;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 10. 7
 * Time: 오전 5:30
 * DBN node기준은 현재가 X2 이전이 X1 이런식으로 정의됨
 */
public class GenerateTripCPDs {

    //    private SubsetPOIs subsetPOIs;
    private static Logger logger = Logger.getLogger(GenerateTripCPDs.class);
    private TripNodesAndValues tripNodesAndValues;
    private int discreteTimeWidth;
    private int nodeSize;

    // CPD와 marginal 두개로 나누어서 해야함 .
    private TripCPDs tripCPDs;

    public GenerateTripCPDs(SubsetPOIs subsetPOIs, int discreteTimeWidth) {
        tripNodesAndValues = new TripNodesAndValues(subsetPOIs, discreteTimeWidth);
        tripCPDs = new TripCPDs(subsetPOIs,tripNodesAndValues );
        this.nodeSize = subsetPOIs.size();

        this.discreteTimeWidth = discreteTimeWidth;
    }

    public GenerateTripCPDs(String poiTitles[], int discreteTimeWidth) {
        SubsetPOIs subsetPOIs = new SubsetPOIs(poiTitles);
        this.nodeSize = subsetPOIs.size();
        tripNodesAndValues = new TripNodesAndValues(subsetPOIs, discreteTimeWidth);
        tripCPDs = new TripCPDs(poiTitles,tripNodesAndValues );
    }

    public TripCPDs generate() {
        // 1. Node 이름들 만들어서 ND에 쳐박을것.!!
        generateCPDs();
        return tripCPDs;
    }


    private void generateCPDs() {
        generateCPD_Movement();
        generateCPD_Duration();
        generateCPD_Time();
        generateCPD_EndTime();
        generateCPD_DiscreteTime();


/*
        logger.debug(durationCPDs[1]);
        logger.debug(timeCPDs[1]);
        logger.debug(timeCPDs[2]);
*/
//        logger.debug(movementCPDs[1]);
//        logger.debug(movementCPDs[1]);
//        for (int i=0; i<movementCPDs.length; i++){
//            logger.debug(movementCPDs);
//
//        }
    }

//    private void generateCPD_PhysicalActivity(int orderIdx) {
//        if (orderIdx == 0) {
//            return;
//        } else if (orderIdx >= tripNetwork.getSubsetPOIs().size()) {
//            return;
//        }
//
//        String parentsNames[] = new String[1];
//        parentsNames[0] = "D" + (orderIdx);
//        BasicHybridCPD duration = new BasicHybridCPD("D" + orderIdx, parentsNames);
//
//        int pNodes[] = new int[1];
//        pNodes[0] = NodeDictionary.getInstance().nodeIdx(parentsNames[0]);
//        int pValues[] = new int[1];
//        for (int i = 0; i < tripNetwork.getSubsetPOIs().size(); i++) {
//            pValues[0] = i;
//            ProbabilisticDuration pDur = tripNetwork.getSubsetPOIs().getPOI(i).getSpendingTime(null, null);
//            duration.setDistribution(pNodes, pValues, new SimpleGaussian(1, "D" + orderIdx, pDur.hour, pDur.standardDeviation));
//        }
//        tripNetwork.durationCPDs[orderIdx] = duration;
//    }

    private void generateCPD_Movement() {
        MovementFunction movementFunction = new MovementFunction(tripCPDs.getSubsetPOIs());
        String parentsNames[] = new String[3];

        parentsNames[0] = "X1";
        parentsNames[1] = "X2";
        parentsNames[2] = "DE";
        BasicHybridCPD movement = new BasicHybridCPD("M", parentsNames);
//        BasicHybridCPD cost = new BasicHybridCPD("C" + orderIdx, parentsNames);

        int pNodes[] = new int[3];
        pNodes[0] = NodeDictionary.getInstance().nodeIdx(parentsNames[0]);
        pNodes[1] = NodeDictionary.getInstance().nodeIdx(parentsNames[1]);
        pNodes[2] = NodeDictionary.getInstance().nodeIdx(parentsNames[2]);
        int pValues[] = new int[3];
        int discreteTimeSize = tripNodesAndValues.getDiscreteTimeValue().length;
        for (int i = 0; i < nodeSize; i++) {
            for (int j = 0; j < nodeSize; j++) {
                for (int t = 0; t < discreteTimeSize; t++) {
                    if (i == j) {
//                        logger.debug(tripNetwork.subsetPOIs.getPOI(i).getTitle() + "=>" + tripNetwork.subsetPOIs.getPOI(j).getTitle() + ", time=" + 0 + ", cost=" + 0);
                        pValues[0] = i;
                        pValues[1] = j;
                        pValues[2] = t;
                        movement.setDistribution(pNodes, pValues, new SimpleGaussian(1, "M", 0, 0.01));

                    } else {
                        movementFunction.findPath(i, j);
                        Pair<Double, Double> costPair = movementFunction.getCost();
                        Pair<Double, Double> timePair = movementFunction.getTime();
//                        logger.debug(tripCPDs.getSubsetPOIs().getPOI(i).getTitle() + "=>" + tripCPDs.getSubsetPOIs().getPOI(j).getTitle() + ", time=" + timePair.first() + ", cost=" + costPair);
                        pValues[0] = i;
                        pValues[1] = j;
                        pValues[2] = t;
                        movement.setDistribution(pNodes, pValues, new SimpleGaussian(1, "M", timePair.first(), timePair.second() * timePair.second()));
//                        cost.setDistribution(pNodes, pValues, new SimpleGaussian(1, "C" + orderIdx, costPair.first(), costPair.second() * costPair.second()));
                    }
                }
            }
        }
//        logger.debug(movement);
        tripCPDs.setMovementCPDs(movement);
    }

    private void generateCPD_Duration() {
        String parentsNames[] = new String[1];
        parentsNames[0] = "X1";
        BasicHybridCPD duration = new BasicHybridCPD("D", parentsNames);

        int pNodes[] = new int[1];
        pNodes[0] = NodeDictionary.getInstance().nodeIdx(parentsNames[0]);
        int pValues[] = new int[1];
        for (int i = 0; i < tripCPDs.getSubsetPOIs().size(); i++) {
            pValues[0] = i;
            ProbabilisticDuration pDur = tripCPDs.getSubsetPOIs().getPOI(i).getSpendingTime(null, null);
            duration.setDistribution(pNodes, pValues, new SimpleGaussian(1, "D", pDur.hour, pDur.standardDeviation));
        }
        tripCPDs.setDurationCPDs(duration);
    }

    private void generateCPD_Time() {
        String theNodeName = "T2";
        String parentsNames[] = new String[3];
        parentsNames[0] = "T1";
        parentsNames[1] = "D";
        parentsNames[2] = "M";
        double condWeights[] = new double[3];
        Arrays.fill(condWeights, 1.0);
        double mean = 0;
        double var = 0.0;
        ConditionalLinearGaussian clg = new ConditionalLinearGaussian(1, theNodeName, parentsNames, condWeights, mean, var);
        BasicCLGCPD timeCPD = new BasicCLGCPD(theNodeName, clg);
        tripCPDs.setTimeCPDs(timeCPD);
    }


    private void generateCPD_EndTime() {
        String theNodeName = "E";
        String parentsNames[] = new String[2];
        parentsNames[0] = "T2";
        parentsNames[1] = "D";
        double condWeights[] = new double[2];
        Arrays.fill(condWeights, 1.0);
        double mean = 0;
        double var = 0.0;
        ConditionalLinearGaussian clg = new ConditionalLinearGaussian(1, theNodeName, parentsNames, condWeights, mean, var);
        BasicCLGCPD endTimeCPD = new BasicCLGCPD(theNodeName, clg);
        tripCPDs.setEndTimeCPDs(endTimeCPD);
    }

    private void generateCPD_DiscreteTime() {
        DiscreteTimeCPD discreteTimeCPD = new DiscreteTimeCPD("DT", this.discreteTimeWidth);
        tripCPDs.setDiscreteTimeCPD(discreteTimeCPD);
    }

    public TripCPDs getTripCPDs() {
        return tripCPDs;
    }

    public static void test() {

        GenerateTripCPDs generateTripCPDs = new GenerateTripCPDs(SubsetPOIGen.getJeju10_(), 30);
        generateTripCPDs.generate();
        logger.debug(generateTripCPDs.getTripCPDs());


    }

    public static void main(String[] args) {
        test();
    }
}

