package edu.hanyang.trip_planning.tripHTBN.dynamicPotential;


import org.apache.commons.math3.util.FastMath;
import org.apache.log4j.Logger;
import util.Triple;

/**
 * PDF를 histogram으로 변환한 다음에 PMF로 바꾸는 클래수
 */
public class PDFtoPMF {
    private static Logger logger = Logger.getLogger(PDFtoPMF.class);

    /**
     * Gaussian Distribution을 PMF로 변환함
     *
     * @param
     * @param
     * @return
     */
    public static Triple<double[], Integer, Integer> getGaussianPMFDayTime(double mean, double sd, int minuteStep) {

//        logger.debug("mean="+mean + "\tsd="+sd + "minStep="+minuteStep);
        // confidence interval
        double min = 0.0;
        double max = 24.0;

        double lower99 = mean - 3 * sd;
        double upper99 = mean + 3 * sd;
//        NormalDistribution normalDistribution = new NormalDistribution(mean, sd);

        double hourStep = (double) (minuteStep) / 60.0;
        int slotSize = (int) (24.0 / hourStep);
        double retValues[] = new double[slotSize];
        int idx = 0;

        double maxValue = Double.NEGATIVE_INFINITY;
        double sum = 0.0;

        int lowerIdx = -1;
        int upperIdx = -1;

//        logger.debug("lower99="+lower99);
//        logger.debug("upper99="+upper99);
        for (double t = min; t < max; t = t + hourStep) {
            if (t > lower99 && t < upper99) {
                if (lowerIdx == -1) {
                    lowerIdx = idx;
                }
                upperIdx = idx;
//                logger.debug(t + "\t" + normalDistribution.density(t));
                double prob = normalDensity(mean, sd, t) * hourStep;
                retValues[idx] = prob;
                sum += prob;
                if (prob > maxValue) {
                    maxValue = prob;
                }
            }
            idx++;
        }

//        logger.debug("lowerIdx="+lowerIdx+ "\tupperIdx="+upperIdx);

        // 범위가 너무 좁을때 :max값 1개만 1.0으로 할당
        if (lowerIdx == -1) {
            idx = 0;
            for (double t = min; t < max; t = t + hourStep) {
//                logger.debug("t="+t + "\tmean="+mean);
                if (t > mean) {
                    retValues[idx] = 1.0;
                    return new Triple<>(retValues, idx, idx);
                }
                idx++;
            }
        }
        // normalize
        double factor = (1.0 - sum) / sum;
        for (int i = lowerIdx; i <= upperIdx; i++) {
            retValues[i] *= 1.0 + factor;
        }
//        logger.debug(Arrays.toString(retValues));
//        logger.debug(DoubleArray.sum(retValues));
//        logger.debug(DoubleArray.toString("%3.3f",retValues));

//        logger.debug(lowerIdx + ","+upperIdx);
        return new Triple<>(retValues, lowerIdx, upperIdx);
    }

    public static double normalDensity(double mean, double sd, double x) {
        double x0 = x - mean;
        double x1 = x0 / sd;
        double lsdphl2Pi = FastMath.log(sd) + 0.5D * FastMath.log(6.283185307179586D);
        double logD = -0.5D * x1 * x1 - lsdphl2Pi;
        return FastMath.exp(logD);


    }


    public static void main(String[] args) {
        getGaussianPMFDayTime(5, 1, 30);
    }
}
