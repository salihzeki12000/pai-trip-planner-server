package edu.hanyang.trip_planning.tripHTBN.dynamicPotential;

import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceSoftmaxCPD;
import org.apache.log4j.Logger;
import wykwon.common.Triple;

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

    /**
     * 이거 연속 분포의 PDF를 Discretize해서 PMF로 만드는 클래스
     *
     * @param hourMean
     * @param
     * @return Triple : 첫번째 확률분포, 0이아닌 확률분포의 시작, 0이하닌 확률분포의 끝
     */
    public Triple<double[], Integer, Integer> inferenceDistribution(double hourMean, double hourSd) {
        return PDFtoPMF.getGaussianPMFDayTime(hourMean, hourSd, discreteMinuteStep);
    }

    private double[] getTableEntry(double refTime) {
           throw new RuntimeException("Not surpported");
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("this is discrete time CPD");
        return strbuf.toString();
    }


    public static void main(String[] args) {
//        test();

    }
}
