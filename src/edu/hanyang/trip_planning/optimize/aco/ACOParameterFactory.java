package edu.hanyang.trip_planning.optimize.aco;

/**
 * Created by dummyProblem on 2016-10-28.
 */
public class ACOParameterFactory {
    public static ACOParameters simpleParamGen() {
        ACOParameters p = new ACOParameters();
        p.setAlpha(1.0);                // 휴리스틱 가중
        p.setBeta(0.5);                 // 페르몬 가중
        p.setEvaporation(0.1);          // 휘발정도
        p.setNumberOfIteration(1000);    // 개미 수
        return p;
    }
}
