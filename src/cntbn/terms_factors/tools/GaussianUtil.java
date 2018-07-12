package cntbn.terms_factors.tools;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import org.apache.log4j.Logger;

public class GaussianUtil {
    private static Logger logger = Logger.getLogger(GaussianUtil.class);
    private static final double similarThreshold = 0.01;

    public static SimpleGaussian productTwoSimpleGaussian(SimpleGaussian g1,
                                                          SimpleGaussian g2) {
        int variableIdx = g1.getArgument();
        if (variableIdx != g2.getArgument()) {
            throw new RuntimeException(
                    "Product of two Gaussans  cannot be calculated. argument mismatch:"
                            + g1 + " , " + g2);
        }

        double variance;
        if (g1.getVariance() > GlobalParameters.THRESHOLD_INF
                && g2.getVariance() > GlobalParameters.THRESHOLD_INF) {
            // variance가 무한대일경우 0.0N(X;0,INF)인 Gaussian을 반환한다.
            SimpleGaussian g = new SimpleGaussian(GlobalParameters.NEAR_ZERO,
                    g1.getArgument());
            g.setMeanAndVariance(g1.getMean() + g2.getMean(),
                    GlobalParameters.NEAR_INF);
            return g;
        } else if (g1.getVariance() > GlobalParameters.THRESHOLD_INF) {
            SimpleGaussian g = new SimpleGaussian(GlobalParameters.NEAR_ZERO,
                    g1.getArgument());
            g.setMeanAndVariance(g2.getMean(), g2.getVariance());
            return g;
        } else if (g2.getVariance() > GlobalParameters.THRESHOLD_INF) {
            SimpleGaussian g = new SimpleGaussian(GlobalParameters.NEAR_ZERO,
                    g1.getArgument());
            g.setMeanAndVariance(g1.getMean(), g1.getVariance());
            return g;
        } else if (g1.getVariance() == 0.0 && g2.getVariance() == 0.0) {
            if (Math.abs(g1.getMean() - g2.getMean()) < GlobalParameters.THRESHOLD_ZERO) {
                SimpleGaussian g = new SimpleGaussian(g1.getWeight() * g2.getWeight(),
                        g1.getArgument());
                g.setMeanAndVariance(g1.getMean(), 0.0);
                return g;
            }
        }
        variance = g1.getVariance() + g2.getVariance();
        double mean = (g2.getVariance() * g1.getMean() + g1.getVariance()
                * g2.getMean())
                / (g1.getVariance() + g2.getVariance());


        double tmp1 = (g1.getMean() - g2.getMean())
                * (g1.getMean() - g2.getMean());

        double tmp2 = 2 * (g1.getVariance() + g2.getVariance());
        double tmp3 = 2.5067 * Math.sqrt(g1.getVariance() + g2.getVariance());
        double weight = g1.getWeight() * g2.getWeight()
                * Math.exp(-tmp1 / tmp2) / tmp3;

        if (Double.isNaN(weight)) {
            logger.debug("tmp1=" + tmp1 + " tmp2=" + tmp2 + " tmp3=" + tmp3 + " w1=" + g1.getWeight() + " w2=" + g2.getWeight());
            logger.debug("mean=" + g1.getMean() + " mean2=" + g2.getMean());
            logger.debug("varialce1=" + g1.getVariance() + " variance=" + g2.getVariance());
            throw new RuntimeException("NaN ERROR ");
        }

        SimpleGaussian result = new SimpleGaussian(weight, variableIdx);
        result.setMeanAndVariance(mean, variance);
        return result;
    }

    public static ContinuousFactor integralOfTwoSimple(SimpleGaussian g1,
                                                       SimpleGaussian g2, int integralVariableIdx) {
        if (g1.getArgument() != integralVariableIdx
                && g2.getArgument() != integralVariableIdx) {
            throw new RuntimeException(
                    "Two Factors don't have integral variable of "
                            + NodeDictionary.getInstance().nodeName(
                            integralVariableIdx) + "  in" + g1
                            + " and " + g2);
        }
        double weight = productTwoSimpleGaussian(g1, g2).getWeight();
        return new ConstantValue(weight);
    }

    /**
     * Convolution 또는 autocorellation 수행 convolution N(x;;)N(y;x;) => N(y)
     * autocorellation N(x;y;)N(x;;) => N(y)
     *
     * @param simple
     * @param clg
     * @param integralVariableIdx
     * @return
     */
    public static SimpleGaussian convolution(SimpleGaussian simple,
                                             TDGaussian clg, int integralVariableIdx) {

        if (simple.getArgument() != integralVariableIdx) {
            throw new RuntimeException(simple
                    + " does not have "
                    + NodeDictionary.getInstance()
                    .nodeName(integralVariableIdx));
        } else if (simple.getArgument() == clg.getArgument()) {

            // autocorellation: case Int N(x;;)N(x;y;)
            double newWeight = simple.getWeight() * clg.getWeight();
            double newMean = simple.getMean() - clg.getMean();
            double newVariance = simple.getVariance() + clg.getVariance();
            SimpleGaussian newG = new SimpleGaussian(newWeight,
                    clg.getConditionalVariableIdx());
            newG.setMeanAndVariance(newMean, newVariance);
            return newG;
        } else if (simple.getArgument() == clg.getConditionalVariableIdx()) {
            // case Int N(x;;)N(y;x;)
            double newWeight = simple.getWeight() * clg.getWeight();
            double newMean = simple.getMean() + clg.getMean();
            double newVariance = simple.getVariance() + clg.getVariance();
            SimpleGaussian newG = new SimpleGaussian(newWeight,
                    clg.getArgument());
            newG.setMeanAndVariance(newMean, newVariance);
            return newG;
        }
        throw new RuntimeException(clg + " does not have "
                + NodeDictionary.getInstance().nodeName(integralVariableIdx));
    }

    public static GaussianFunction checkAndConvert(_ConditionalLinearGaussian clg) {

        double conditionalWeights[] = clg.getConditionalWeights();
        if (conditionalWeights.length == 1 && similar(conditionalWeights[0], 1.0)) {
            TDGaussian tdGaussian = convert2TDGaussian(clg);
            logger.debug("this is TD gaussian: " + tdGaussian);
            return tdGaussian;
        } else if (similar(conditionalWeights, 1.0)) {
            NonWeightedCLG nonWeightedCLG = convert2NonWeightedCLG(clg);
            logger.debug("This is NonWeightedCLG: " + nonWeightedCLG);
            return nonWeightedCLG;
        } else {
            logger.debug("this is CLG");
            return clg;
        }
    }

    private static TDGaussian convert2TDGaussian(_ConditionalLinearGaussian clg) {
        int variableIdx = clg.getArgument();
        int conditionalVariableIdx = clg.getConditionalVariableIndices()[0];
        double condWeights[] = clg.getConditionalWeights();
        if (condWeights.length == 1 && similar(condWeights[0], 1.0)) {
            double weight = clg.getWeight();
            double mean = clg.getMean();
            double variance = clg.getVariance();
            TDGaussian tdGaussian = new TDGaussian(weight, variableIdx, conditionalVariableIdx);
            tdGaussian.setMeanAndVariance(mean, variance);
            return tdGaussian;
        } else {
            throw new RuntimeException("This " + clg + " cannot be converted as TDGaussian");
        }
    }

    private static NonWeightedCLG convert2NonWeightedCLG(_ConditionalLinearGaussian clg) {

        int variableIdx = clg.getArgument();

        double condWeights[] = clg.getConditionalWeights();
        int conditionalVariableIndices[] = clg.getConditionalVariableIndices();
        if (similar(condWeights, 1.0)) {

            double weight = clg.getWeight();
            double mean = clg.getMean();
            double variance = clg.getVariance();
            NonWeightedCLG nonWeightedCLG = new NonWeightedCLG(weight, variableIdx, conditionalVariableIndices, mean, variance);
            nonWeightedCLG.setMeanAndVariance(mean, variance);
            return nonWeightedCLG;
        } else {
            throw new RuntimeException("This " + clg + " cannot be converted as TDGaussian");
        }
    }

    public static boolean similar(double value, double ref) {
        return Math.abs(value - ref) < similarThreshold;
    }

    public static boolean similar(double value, double ref, double threshold) {
        return Math.abs(value - ref) < threshold;
    }


    public static boolean similar(double values[], double ref) {
        for (double value : values) {
            if (Math.abs(value - ref) > similarThreshold) {
                return false;
            }
        }
        return true;
    }

    public static boolean similar(double values[], double ref, double threshold) {
        for (double value : values) {
            if (Math.abs(value - ref) > threshold) {
                return false;
            }
        }
        return true;
    }

    public static boolean similar(double values[], double refs[]) {
        if (values.length != refs.length) {
            throw new RuntimeException("Array size mismatch");
        }
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i] - refs[i]) > similarThreshold) {
                return false;
            }
        }
        return true;
    }

    public static boolean similar(double values[], double refs[], double threshold) {
        if (values.length != refs.length) {
            throw new RuntimeException("Array size mismatch");
        }
        for (int i = 0; i < values.length; i++) {
            if (Math.abs(values[i] - refs[i]) > threshold) {
                return false;
            }
        }
        return true;
    }


    public static void main(String args[]) {

    }
}
