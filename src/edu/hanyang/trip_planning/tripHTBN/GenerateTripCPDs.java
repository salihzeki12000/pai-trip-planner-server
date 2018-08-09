package edu.hanyang.trip_planning.tripHTBN;

import cntbn.common.NodeDictionary;
import cntbn.terms_factors.ConditionalLinearGaussian;
import cntbn.terms_factors.SimpleGaussian;

import edu.hanyang.trip_planning.tripHTBN.traffic.MovementFunction;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripHTBN.dynamicPotential.DiscreteTimeCPD;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicCLGCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicHybridCPD;
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
    private TripNodesAndValues tripNodesAndValues;
    private int discreteTimeWidth;
    private int nodeSize;
    private TripCPDs tripCPDs;

    public GenerateTripCPDs(SubsetPOIs subsetPOIs, int discreteTimeWidth) {
        tripNodesAndValues = new TripNodesAndValues(subsetPOIs, discreteTimeWidth);
        tripCPDs = new TripCPDs(subsetPOIs, tripNodesAndValues);
        this.nodeSize = subsetPOIs.size();
        this.discreteTimeWidth = discreteTimeWidth;
    }

    public TripCPDs generate() {
        generateCPDs();
        return tripCPDs;
    }

    private void generateCPDs() {
        generateCPD_Movement();
        generateCPD_Duration();
        generateCPD_Time();
        generateCPD_EndTime();
        generateCPD_DiscreteTime();
    }

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
                        pValues[0] = i;
                        pValues[1] = j;
                        pValues[2] = t;
                        movement.setDistribution(pNodes, pValues, new SimpleGaussian(1, "M", 0, 0.01));

                    } else {
                        movementFunction.findPath(i, j);
                        Pair<Double, Double> costPair = movementFunction.getCost();
                        Pair<Double, Double> timePair = movementFunction.getTime();
                        pValues[0] = i;
                        pValues[1] = j;
                        pValues[2] = t;
                        movement.setDistribution(pNodes, pValues, new SimpleGaussian(1, "M", timePair.first(), timePair.second() * timePair.second()));
                    }
                }
            }
        }
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
}

