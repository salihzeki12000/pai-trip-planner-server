package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import cntbn.common.TestNodeDictionary;
import javolution.util.FastSet;
import org.apache.log4j.Logger;
import wykwon.common.array.MyArrays;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Set;

/**
 * Conditional linear Gaussian을 모델링하는 클래스 w*N(x; u+b1*y1 + ... + bn*yn;
 * sigma^2)같이 모델링함
 *
 * @author roland
 */
public class ConditionalLinearGaussian implements GaussianFunction {
    private static Logger logger = Logger
            .getLogger(ConditionalLinearGaussian.class);
    private double variance = Double.MAX_VALUE;
    private double mean = 0.0;
    private double weight = 1.0;
    private int argumentIdx;

    private int conditionalVariableIndices[];
    private double conditionalWeights[];
    FastSet<Integer> paramSet = new FastSet<Integer>();

    /**
     * @param weight
     * @param variableName
     * @param conditionalVariableNames
     */
    public ConditionalLinearGaussian(double weight,
                                     String variableName, String... conditionalVariableNames) {
        int variableIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        int conditionalVariableIndices[] = new int[conditionalVariableNames.length];
        for (int i = 0; i < conditionalVariableNames.length; i++) {
            conditionalVariableIndices[i] = NodeDictionary.getInstance()
                    .nodeIdx(conditionalVariableNames[i]);
        }
        this.argumentIdx = variableIdx;
        this.conditionalVariableIndices = conditionalVariableIndices.clone();
        this.weight = weight;
        paramSet.add(variableIdx);
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            paramSet.add(conditionalVariableIndices[i]);
        }
        conditionalWeights = new double[conditionalVariableIndices.length];
        Arrays.fill(conditionalWeights, 1.0);

    }

    /**
     *
     * @param weight
     * @param variableName
     * @param conditionalVariableNames
     * @param conditionalWeights
     * @param mean
     * @param variance
     */
    public ConditionalLinearGaussian(double weight, String variableName,
                                     String conditionalVariableNames[], double conditionalWeights[], double mean, double variance) {
        this.weight = weight;
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        int size = conditionalVariableNames.length;
        conditionalVariableIndices = new int[size];

        for (int i = 0; i < conditionalVariableNames.length; i++) {
            conditionalVariableIndices[i] = NodeDictionary.getInstance().nodeIdx(conditionalVariableNames[i]);
        }
        this.conditionalWeights = conditionalWeights.clone();

        paramSet.add(this.argumentIdx);
        for (int cond : conditionalVariableIndices) {
            paramSet.add(cond);
        }
        setMeanAndVariance(mean, variance);
    }

    public ConditionalLinearGaussian(double weight, int variableIdx,
                                     int... conditionalVariableIndices) {
        if (conditionalVariableIndices.length == 0) {
            throw new RuntimeException("Empty conditional variables");
        }
        this.argumentIdx = variableIdx;
        this.conditionalVariableIndices = conditionalVariableIndices.clone();
        this.weight = weight;
        paramSet.add(variableIdx);
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            paramSet.add(conditionalVariableIndices[i]);
        }
        conditionalWeights = new double[conditionalVariableIndices.length];
        Arrays.fill(conditionalWeights, 1.0);
    }

    public ConditionalLinearGaussian(double weight, int variableIdx,
                                     int conditionalVariableIndices[], double conditionalWeights[], double mean, double variance) {
        if (conditionalVariableIndices.length == 0) {
            throw new RuntimeException("Empty conditional variables");
        }
        this.argumentIdx = variableIdx;
        this.conditionalVariableIndices = conditionalVariableIndices.clone();
        this.weight = weight;
        paramSet.add(variableIdx);
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            paramSet.add(conditionalVariableIndices[i]);
        }
        this.conditionalWeights = conditionalWeights.clone();
        setMeanAndVariance(mean, variance);
    }

    public ConditionalLinearGaussian(
            ConditionalLinearGaussian clg) {
        this.argumentIdx = clg.argumentIdx;
        this.conditionalVariableIndices = clg.conditionalVariableIndices
                .clone();
        this.conditionalWeights = clg.conditionalWeights.clone();
        this.weight = clg.weight;
        paramSet.addAll(clg.paramSet);
        conditionalWeights = new double[conditionalVariableIndices.length];
        Arrays.fill(conditionalWeights, 1.0);

    }

    /**
     * parameter setting하는것
     *
     * @param mean               mean value (beta_0)
     * @param variance           variance
     * @param conditionalWeights (weights => beta_1,..., beta_n)
     */
    public void setParams(double mean, double variance,
                          double... conditionalWeights) {
        if (variance > GlobalParameters.THRESHOLD_INF) {
            this.variance = GlobalParameters.NEAR_INF;

        } else {
            this.variance = variance;
        }
        this.mean = mean;
        if (conditionalWeights.length != conditionalVariableIndices.length) {
            throw new RuntimeException("conditionalWeights size mismatch");
        }
        this.conditionalWeights = conditionalWeights.clone();
        checkAbnormalConditionalWeight();
    }

    public double getVariance() {
        return variance;
    }

    public double getMean() {
        return mean;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public int getArgument() {
        return argumentIdx;
    }

    public int[] getConditionalVariableIndices() {
        return conditionalVariableIndices;
    }

    public double[] getConditionalWeights() {
        return conditionalWeights;
    }

    /**
     * conditional variable중 하나를 argument와 바꿈
     *
     * @param conditionalVariableIdx
     */
    public void relocateParameters(int conditionalVariableIdx) {
        // 1. 일단 conditional variable이 있는 idx를 찾고
        int theIdx = -1;
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            if (conditionalVariableIdx == conditionalVariableIndices[i]) {
                theIdx = i;
                break;
            }
        }
        if (theIdx < 0) {
            throw new RuntimeException(
                    "this CLG has not conditional variable "
                            + NodeDictionary.getInstance().nodeName(
                            conditionalVariableIdx));
        }
        double theWeight = conditionalWeights[theIdx];
        // 2. argument 상호 교체
        conditionalVariableIndices[theIdx] = this.argumentIdx;
        this.argumentIdx = conditionalVariableIdx;

        // 3. weight, mean 과 variancce 변경
        weight = Math.abs(weight * theWeight);
        mean /= -theWeight;
        variance = Math.abs(variance / theWeight);

        // 4. conditional weight 변경
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            conditionalWeights[i] /= -theWeight;
        }
        conditionalWeights[theIdx] = 1.0 / theWeight;
        checkAbnormalConditionalWeight();
    }

    private void checkAbnormalConditionalWeight() {
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            if (Math.abs(conditionalWeights[i]) < GlobalParameters.THRESHOLD_ZERO) {
                throw new RuntimeException("zero conditional weight in " + this);
            } else if (Math.abs(conditionalWeights[i]) > GlobalParameters.THRESHOLD_INF) {
                throw new RuntimeException("INF conditional weight in " + this);
            }

        }
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public ConditionalLinearGaussian deepCopy() {
        ConditionalLinearGaussian newG = new ConditionalLinearGaussian(
                this.weight, this.argumentIdx, this.conditionalVariableIndices);
        newG.setParams(this.mean, this.variance,
                this.conditionalWeights.clone());
        return newG;
    }

    public String toString() {

        String argumentName = NodeDictionary.getInstance().nodeName(
                argumentIdx);
        String condArgmentNames[] = new String[conditionalVariableIndices.length];
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            condArgmentNames[i] = NodeDictionary.getInstance().nodeName(
                    conditionalVariableIndices[i]);
        }

        DecimalFormat df = new DecimalFormat("#.###");
        String varianceStr;
        if (variance > GlobalParameters.THRESHOLD_INF) {
            varianceStr = "INF";
        } else {
            varianceStr = df.format(variance);
        }

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(df.format(weight) + "N(" + argumentName + ";");
        if (mean != 0.0)
            strBuf.append(df.format(mean));

        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            if (conditionalWeights[i] > 0)
                strBuf.append("+");
            strBuf.append(df.format(conditionalWeights[i])
                    + condArgmentNames[i]);
        }
        strBuf.append(", " + varianceStr + ")");

        return strBuf.toString();

    }

    public boolean isSameForm(GaussianFunction tFunction) {
        return false;
    }

    @Override
    public GaussianType type() {
        return GaussianType.CLG;
    }

    @Override
    public Set<Integer> getParameters() {
        return paramSet;
    }

    @Override
    public int compareTo(SimpleTerm arg) {
        if (weight < arg.getWeight()) {
            return 1;
        } else if (weight > arg.getWeight()) {
            return -1;
        }
        return 0;
    }


    @Override
    public void normalize(double basis) {
        this.weight = basis;

    }

    @Override
    public boolean hasMixture() {
        return false;
    }

    @Override
    public void shift(double offset) {
        this.mean += offset;

    }

    /**
     * parameter setting하는것
     *
     * @param mean     mean value (beta_0)
     * @param variance variance
     */
    public void setMeanAndVariance(double mean, double variance) {
        if (variance > GlobalParameters.THRESHOLD_INF) {
            this.variance = GlobalParameters.NEAR_INF;

        } else {
            this.variance = variance;
        }
        this.mean = mean;
    }

    public static ConditionalLinearGaussian dummy1() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "true");
        nd.putValues("Y", "true");
        nd.putValues("W", "true");
        nd.putValues("Z", "true");
        double condParams[] = {0.5, 1, 1.5};
        ConditionalLinearGaussian clg = new ConditionalLinearGaussian(1, "X", MyArrays.makeString("Y,W,Z"), MyArrays.makeDouble("0.5,1,1.5"), 10, 1);
//        GaussianFunction sg = new GaussianFunction(1.0, "Y1", 1, 1);
//
//
//        logger.debug(clg);
//        logger.debug(new _ConditionalLinearGaussian(clg));
        return clg;
    }

    public static ConditionalLinearGaussian dummy2() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "true");
        nd.putValues("Y1", "true");
        nd.putValues("Y2", "true");
        nd.putValues("Y3", "true");
        ConditionalLinearGaussian clg = new ConditionalLinearGaussian(1, "X", MyArrays.makeString("Y1,Y2,Y3"), MyArrays.makeDouble("0.995,1.005,1.0"), 10, 1);
        return clg;
    }

    public static void test1() {
        TestNodeDictionary.makeSample();

        int X = NodeDictionary.getInstance().nodeIdx("X");
        int Y = NodeDictionary.getInstance().nodeIdx("Y");
        int Z = NodeDictionary.getInstance().nodeIdx("Z");
        ConditionalLinearGaussian cg = new ConditionalLinearGaussian(
                1.0, X, Y, Z);

        cg.setParams(1.0, 2.0, 3.0, 4.0);
        Logger.getRootLogger().info(cg);
        cg.relocateParameters(Z);

        // _ConditionalLinearGaussian cg = new _ConditionalLinearGaussian(1.0,
        // "X",
        // "Y1", "Y2", "Y3");
        // cg.setParams(1.0, 2.0, 1.0, 2.0, 3.0);
        // cg.relocateConditionalGaussian("Y1");

        Logger.getRootLogger().info(cg);


    }

    public static void test_rellocate() {
        ConditionalLinearGaussian clg = dummy1();
        NodeDictionary nd = NodeDictionary.getInstance();
        logger.debug(clg);
        clg.relocateParameters(nd.nodeIdx("Y1"));
        logger.debug(clg);
    }

    public static void main(String args[]) {
        //logger.debug(dummy1());
        test_rellocate();
    }
}
