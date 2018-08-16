package tripPlanning.tripHTBN.dynamicPotential;

import tripPlanning.tripHTBN.potential.InterfaceSoftmaxCPD;
import org.apache.log4j.Logger;

/**
 * Created by wykwon on 2015-10-08.
 */
public class DiscreteTimeCPD implements InterfaceSoftmaxCPD {
    private static Logger logger = Logger.getLogger(DiscreteTimeCPD.class);
    private String nodename;
    private int discreteMinuteStep;

    public DiscreteTimeCPD(String nodename, int discreteMinuteStep) {
        this.nodename = nodename;
        this.discreteMinuteStep = discreteMinuteStep;
    }

    @Override
    public double[] getDistribution(int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        throw new RuntimeException("Not supproted");
    }

    @Override
    public double getProbability(int theValue, int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        throw new RuntimeException("Not supproted");

    }

    @Override
    public double[] getDistributionFromSuperset(int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        throw new RuntimeException("Not supproted");
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("this is discrete time CPD");
        return strbuf.toString();
    }


    public static void main(String[] args) {
    }
}
