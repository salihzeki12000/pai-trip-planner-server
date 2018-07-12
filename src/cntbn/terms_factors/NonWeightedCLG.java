package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import javolution.util.FastSet;
import org.apache.log4j.Logger;
import wykwon.common.array.MyArrays;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 13
 * Time: 오후 2:23
 * To change this template use File | Settings | File Templates.
 */
public class NonWeightedCLG implements GaussianFunction {
    private static Logger logger = Logger.getLogger(NonWeightedCLG.class);
    private double variance = Double.MAX_VALUE;
    private double mean = 0.0;
    private double weight = 1.0;
    private int argumentIdx;
    private int conditionalVariableIndices[];
    FastSet<Integer> paramSet = new FastSet<Integer>();

    public NonWeightedCLG(double weight, String variableName,
                          String conditionalVariableNames[], double mean, double variance) {
        this.weight = weight;
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        int size = conditionalVariableNames.length;
        conditionalVariableIndices = new int[size];

        for (int i = 0; i < conditionalVariableNames.length; i++) {
            conditionalVariableIndices[i] = NodeDictionary.getInstance().nodeIdx(conditionalVariableNames[i]);
        }

        paramSet.add(this.argumentIdx);
        for (int cond : conditionalVariableIndices) {
            paramSet.add(cond);
        }
        setMeanAndVariance(mean, variance);
    }

    public NonWeightedCLG(double weight, int variableIdx,
                          int conditionalVariableIndices[], double mean, double variance) {
        this.weight = weight;
        this.argumentIdx = variableIdx;
        this.conditionalVariableIndices = conditionalVariableIndices.clone();
        paramSet.add(this.argumentIdx);
        for (int cond : conditionalVariableIndices) {
            paramSet.add(cond);
        }
        setMeanAndVariance(mean, variance);
    }

    public NonWeightedCLG(NonWeightedCLG clg) {
        this.weight = clg.weight;
        this.argumentIdx = clg.argumentIdx;
        this.conditionalVariableIndices = clg.conditionalVariableIndices.clone();
        this.paramSet = new FastSet<Integer>(clg.paramSet);
        this.mean = clg.mean;
        this.variance = clg.variance;
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

    public int[] getConditionalVariableIndices() {
        return conditionalVariableIndices;
    }

    /**
     * conditional variable과 variable을 바꾼다.
     * <p/>
     * N(x;mean+y+z;var) 을 N(x;mean+y+z;var)로 바꾸는 것과 같음
     *
     * @param conditionalVariableIdx
     */
    public void changeArgumentWithConditionalVariable(int conditionalVariableIdx) {
        // 흠 이경우는 그냥 바꾸면 되는데

        // 1. conditional variable 유효성 검사
        if (!contain(this.conditionalVariableIndices, conditionalVariableIdx)) {
            throw new RuntimeException("No such conditional variable as " + NodeDictionary.getInstance().nodeName(conditionalVariableIdx));
        }


    }

    private boolean contain(int array[], int arg) {
        for (int v : array) {
            if (v == arg) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getArgument() {
        return this.argumentIdx;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void shift(double offset) {
        this.mean += offset;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public boolean isSameForm(GaussianFunction tFunction) {
        return tFunction instanceof NonWeightedCLG;
    }

    @Override
    public GaussianType type() {
        return GaussianType.NonWeightedCLG;
    }

    @Override
    public GaussianFunction deepCopy() {
        return new NonWeightedCLG(this);
    }

    @Override
    public Set<Integer> getParameters() {
        return paramSet;
    }

    @Override
    public double getMean() {
        return mean;
    }

    @Override
    public double getVariance() {
        return this.variance;
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
    public int compareTo(SimpleTerm arg) {
        throw new RuntimeException("Not implemented");
    }

    public String toString() {
        String argumentName = NodeDictionary.getInstance().nodeName(
                argumentIdx);

        DecimalFormat df = new DecimalFormat("#.###");
        String varianceStr;
        if (variance > GlobalParameters.THRESHOLD_INF) {
            varianceStr = "INF";
        } else {
            varianceStr = df.format(variance);
        }

        StringBuffer strBuf = new StringBuffer();
        strBuf.append(df.format(weight) + "N(" + argumentName + ";");
        if (mean != 0.0) {
            strBuf.append(df.format(mean));
            strBuf.append("+");
        }
        for (int i = 0; i < conditionalVariableIndices.length; i++) {
            String condArgName = NodeDictionary.getInstance().nodeName(conditionalVariableIndices[i]);

            strBuf.append(condArgName + "+");

        }
        strBuf.deleteCharAt(strBuf.length() - 1);
        strBuf.append(", " + varianceStr + ")");

        return strBuf.toString();
    }


    public static NonWeightedCLG dummy() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "true");
        nd.putValues("Y1", "true");
        nd.putValues("Y2", "true");
        nd.putValues("Y3", "true");
        double condParams[] = {0.5, 1, 1.5};
        String condArgs[] = {"Y1", "Y2", "Y3"};
        NonWeightedCLG clg = new NonWeightedCLG(1, "X", MyArrays.makeString("Y1,Y2,Y3"), 10, 1);
//        GaussianFunction sg = new GaussianFunction(1.0, "Y1", 1, 1);
//
//
        logger.debug(clg);
//        logger.debug(new _ConditionalLinearGaussian(clg));
        return clg;
    }

    public static void main(String[] args) {
        dummy();
    }

}
