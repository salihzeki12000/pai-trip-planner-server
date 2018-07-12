package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import cntbn.common.TestNodeDictionary;
import cntbn.exception.NoSuchVariableException;
import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * mean과 conditional variable의 product를 새로운 mean으로 갖는 gaussian.
 * parent갯수는 하나로 제한한다.
 *
 * @author roland
 */
public class ConditionalMeanProductGaussian implements GaussianFunction {
    private static Logger logger = Logger
            .getLogger(ConditionalMeanProductGaussian.class);
    private double variance = Double.MAX_VALUE;
    private double mean = 0.0;
    private double weight = 1.0;
    private int argumentIdx;
    private int conditionalVariableIndex;
    private double conditionalWeight;
    FastSet<Integer> paramSet = new FastSet<Integer>();


    /**
     * @param weight
     * @param variableName
     * @param conditionalVariableName
     * @param conditionalWeight
     * @param mean
     * @param variance
     */
    public ConditionalMeanProductGaussian(double weight, String variableName,
                                          String conditionalVariableName, double conditionalWeight, double mean, double variance) {
        this.weight = weight;
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        conditionalVariableIndex = NodeDictionary.getInstance().nodeIdx(conditionalVariableName);
        this.conditionalWeight = conditionalWeight;
        paramSet.add(this.argumentIdx);
        paramSet.add(conditionalVariableIndex);
        setMeanAndVariance(mean, variance);
    }


    public ConditionalMeanProductGaussian(double weight, int variableIdx,
                                          int conditionalVariableIndex, double conditionalWeight, double mean, double variance) {
        this.argumentIdx = variableIdx;
        this.conditionalVariableIndex = conditionalVariableIndex;
        this.weight = weight;
        paramSet.add(variableIdx);
        paramSet.add(conditionalVariableIndex);
        this.conditionalWeight = conditionalWeight;
        setMeanAndVariance(mean, variance);
    }

    public ConditionalMeanProductGaussian(
            ConditionalMeanProductGaussian cmp) {
        this.argumentIdx = cmp.argumentIdx;
        this.conditionalVariableIndex = cmp.conditionalVariableIndex;
        this.conditionalWeight = cmp.conditionalWeight;
        this.weight = cmp.weight;
        paramSet.addAll(cmp.paramSet);
        conditionalWeight = cmp.conditionalWeight;

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

    public int getConditionalVariableIdx() {
        return conditionalVariableIndex;
    }

    public double getConditionalWeight() {
        return conditionalWeight;
    }


    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean isSameForm(GaussianFunction tFunction) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ConditionalMeanProductGaussian deepCopy() {
        return new ConditionalMeanProductGaussian(this);
    }

    public String toString() {

        try {
            String variableName = NodeDictionary.getInstance().nodeName(
                    argumentIdx);
            String condVarName = NodeDictionary.getInstance().nodeName(
                    conditionalVariableIndex);
            if (variance > GlobalParameters.THRESHOLD_INF) {
                DecimalFormat df = new DecimalFormat("#.###");
                return df.format(weight) + "N(" + variableName + ";"
                        + conditionalWeight + "*" + df.format(mean) + condVarName + ", INF)";

            } else {
                DecimalFormat df = new DecimalFormat("#.###");
                return df.format(weight) + "N(" + variableName + ";"
                        + conditionalWeight + "*" + df.format(mean) + condVarName + "," + df.format(variance) + ")";

            }
        } catch (NoSuchVariableException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Not handled exception");
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


    public static void test1() {
        TestNodeDictionary.makeSample();

        int X = NodeDictionary.getInstance().nodeIdx("X");
        int Y = NodeDictionary.getInstance().nodeIdx("Y");

//        public ConditionalMeanProductGaussian(double weight, int variableIdx, int conditionalVariableIndex, double conditionalWeight, double mean, double variance) {
        ConditionalMeanProductGaussian cg = new ConditionalMeanProductGaussian(1.0, X, Y, 1.0, 2.0, 10.0);


        logger.debug(cg);


    }


    public static void main(String args[]) {
        test1();

    }
}
