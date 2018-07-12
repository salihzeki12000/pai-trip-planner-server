package edu.hanyang.trip_planning.optimize.constraints;

import edu.hanyang.trip_planning.tripHTBN.InterfaceTripNetwork;

/**
 * Created by wykwon on 2015-09-23.
 */
public class SatisfactionUtility {

    InterfaceTripNetwork tripNetwork;

    public SatisfactionUtility(InterfaceTripNetwork tripNetwork) {
        this.tripNetwork = tripNetwork;
    }

    public double utility() {
        return 0;
    }


    public static void test() {

    }
}
