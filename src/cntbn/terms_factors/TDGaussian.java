package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import cntbn.common.TestNodeDictionary;
import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Set;

/**
 * 하나의 인자만을 갖는 Conditional linear Gaussian을 모델링하는 클래스
 * <p/>
 * w*N(x;mu+y; sigma^2) 같이 모델링됨
 * <p/>
 * conditional weight는 무조건 1로 설정됨
 * <p/>
 * sigma^2)같이 모델링함 conditional Variable이 하나임
 *
 * @author roland
 */
public class TDGaussian implements GaussianFunction {
    private static Logger logger = Logger.getLogger(TDGaussian.class);
    private double variance = Double.MAX_VALUE;
    private double mean = 0.0;
    private double weight = 1.0;
    private int argumentIdx;
    private int conditionalVariableIdx;
    FastSet<Integer> paramSet = new FastSet<Integer>();

    public TDGaussian(double weight, String variableName,
                      String conditionalVariableName) {
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        this.conditionalVariableIdx = NodeDictionary.getInstance().nodeIdx(
                conditionalVariableName);
        this.weight = weight;
        paramSet.add(this.argumentIdx);
        paramSet.add(conditionalVariableIdx);
    }

    public TDGaussian(double weight, String variableName,
                      String conditionalVariableName, double mean, double variance) {
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        this.conditionalVariableIdx = NodeDictionary.getInstance().nodeIdx(
                conditionalVariableName);
        this.weight = weight;
        paramSet.add(this.argumentIdx);
        paramSet.add(conditionalVariableIdx);
        this.mean = mean;
        this.variance = variance;
    }

    public TDGaussian(double weight, String variableName) {
        // TODO 이상한데?
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        this.weight = weight;
        paramSet.add(this.argumentIdx);
        paramSet.add(conditionalVariableIdx);
    }

    public TDGaussian(double weight, int variableIdx, int conditionalVariableIdx) {
        this.argumentIdx = variableIdx;
        this.conditionalVariableIdx = conditionalVariableIdx;
        this.weight = weight;
        paramSet.add(variableIdx);
        paramSet.add(conditionalVariableIdx);
    }

    public TDGaussian(TDGaussian clg) {
        this.argumentIdx = clg.argumentIdx;
        this.conditionalVariableIdx = clg.conditionalVariableIdx;
        this.weight = clg.weight;
        paramSet.addAll(clg.paramSet);
    }

    public void setVariable(String variable) {
        this.argumentIdx = NodeDictionary.getInstance().nodeIdx(variable);
    }

    public void setConditionalVariable(String conditionalVariable) {
        this.conditionalVariableIdx = NodeDictionary.getInstance().nodeIdx(conditionalVariable);
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
        return conditionalVariableIdx;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public TDGaussian deepCopy() {
        TDGaussian newG = new TDGaussian(this.weight, this.argumentIdx,
                this.conditionalVariableIdx);
        newG.setMeanAndVariance(this.mean, this.variance);
        return newG;
    }

    public String toString() {
        String argumentName = NodeDictionary.getInstance().nodeName(
                argumentIdx);
        String condArgmentName = NodeDictionary.getInstance().nodeName(
                conditionalVariableIdx);

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
        strBuf.append(condArgmentName);
        strBuf.append(", " + varianceStr + ")");

        return strBuf.toString();
    }

    public boolean isSameForm(GaussianFunction tFunction) {
        return false;
    }

    @Override
    public GaussianType type() {
        return GaussianType.TDGaussian;
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

    public static void main(String args[]) {
        TestNodeDictionary.makeSample();
        int X = NodeDictionary.getInstance().nodeIdx("X");
        int Y = NodeDictionary.getInstance().nodeIdx("Y");
        int Z = NodeDictionary.getInstance().nodeIdx("Z");
        TDGaussian cg = new TDGaussian(1.0, X, Y);

        cg.setMeanAndVariance(1.0, 2.0);
        Logger.getRootLogger().info(cg);

        // _ConditionalLinearGaussian cg = new _ConditionalLinearGaussian(1.0,
        // "X",
        // "Y1", "Y2", "Y3");
        // cg.setParams(1.0, 2.0, 1.0, 2.0, 3.0);
        // cg.relocateConditionalGaussian("Y1");

        Logger.getRootLogger().info(cg);

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
     * conditional variable 과 argument의 위치를 바꾼다.
     */


    /**
     * argument의 값을 주고 simpleGaussian으로 바꾼다.
     *
     * @param value
     * @return
     */
    public SimpleGaussian simpleGaussianFromArg(double value) {
        double newMean = value - this.mean;
        SimpleGaussian sg = new SimpleGaussian(this.weight, this.conditionalVariableIdx);
        sg.setMeanAndVariance(newMean, this.variance);
        return sg;
    }

    /**
     * conditional variable을 값을 주고 simpleGaussan으로 바꾼다.
     *
     * @param value
     * @return
     */
    public SimpleGaussian simpleGaussianFromCond(double value) {
        double newMean = value + this.mean;
        SimpleGaussian sg = new SimpleGaussian(this.weight, this.argumentIdx);
        sg.setMeanAndVariance(newMean, this.variance);
        return sg;
    }
}
