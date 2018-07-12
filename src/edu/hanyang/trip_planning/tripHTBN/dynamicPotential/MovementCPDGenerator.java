package edu.hanyang.trip_planning.tripHTBN.dynamicPotential;

import edu.hanyang.trip_planning.tripHTBN.traffic.MovementFunction;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicHybridCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceHybridCPD;
import org.apache.log4j.Logger;
import wykwon.common.Pair;

import java.util.Arrays;

/**
 * movement와 관련된 확률 분포를 반환함
 * <p/>
 * 하나의 요청으로 두개 이상의 cpd요청을 반환해야 함.
 * <p/>
 * CPD를 찾을때 최종 요청 인자를 기억하고 있다가 똑같은 인자에 대한 다른 CPD를 호출하면 계산된 결과를 반환할것
 */
public class MovementCPDGenerator {

    BasicHybridCPD movementTimeCPD;
    BasicHybridCPD movementCostCPD;
    MovementFunction trafficCondition;
    SubsetPOIs subsetPOIs;
    int curSrcIdx = -1;
    int curDestIdx = -1;

    private static Logger logger = Logger.getLogger(MovementCPDGenerator.class);

    public MovementCPDGenerator(SubsetPOIs subsetPOIs) {
        this.subsetPOIs = subsetPOIs;
        init();
    }

    public void init() {
        trafficCondition = new MovementFunction(subsetPOIs);
        logger.debug(Arrays.toString(subsetPOIs.getTitles()));
    }

    public void dummy() {
    }

    public InterfaceHybridCPD getMovementTimeCPD() {
        return movementTimeCPD;
    }

    public InterfaceHybridCPD getMovementDurationCPD() {
        return movementTimeCPD;
    }

    public void generate(int parentIndices[]) {
        for (int i = 0; i < subsetPOIs.size(); i++) {
            for (int j = 0; j < subsetPOIs.size(); j++) {
                if (i == j) {
                    logger.debug(subsetPOIs.getPOI(i).getTitle() + "=>" + subsetPOIs.getPOI(j).getTitle() + ", time=" + 0 + ", cost=" + 0);
                } else {
                    trafficCondition.findPath(i, j);
                    Pair<Double, Double> cost = trafficCondition.getCost();
                    Pair<Double, Double> time = trafficCondition.getTime();
                    logger.debug(subsetPOIs.getPOI(i).getTitle() + "=>" + subsetPOIs.getPOI(j).getTitle() + ", time=" + time.first() + ", cost=" + cost);
                }
            }
        }
    }


    public static void main(String[] args) {
//        test();
    }
}
