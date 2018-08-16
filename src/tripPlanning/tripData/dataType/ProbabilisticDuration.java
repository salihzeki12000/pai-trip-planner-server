package tripPlanning.tripData.dataType;

import java.text.DecimalFormat;

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
}
