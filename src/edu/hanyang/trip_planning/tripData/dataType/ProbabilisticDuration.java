package edu.hanyang.trip_planning.tripData.dataType;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 20
 * Time: 오전 10:23
 * To change this template use File | Settings | File Templates.
 */
public class ProbabilisticDuration {
    public double hour;
    public double standardDeviation;

    public ProbabilisticDuration(double hour, double standardDeviation) {
        this.hour = hour;
        this.standardDeviation = standardDeviation;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("##.##");
        return "(" + df.format(hour) + "시간±" + df.format(standardDeviation) + ")";
    }

    public static void main(String[] args) {
        System.out.println(new ProbabilisticDuration(10, 5));
    }
}
