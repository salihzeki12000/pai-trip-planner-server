package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import javolution.util.FastSet;

import org.apache.log4j.Logger;
import wykwon.common.Erf;

import java.text.DecimalFormat;
import java.util.Set;

public class SimpleGaussian implements GaussianFunction {
    private static Logger logger = Logger.getLogger(SimpleGaussian.class);
    private double variance;
    private double mean;
    private double weight;
    private int variableIdx;
    FastSet<Integer> paramSet;

    public SimpleGaussian(double weight, String variableName) {
        int variableIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        if (weight < 0) {
            try {
                throw new RuntimeException(" minus weight("
                        + NodeDictionary.getInstance().nodeName(variableIdx)
                        + ")=" + weight);
            } catch (NoSuchVariableException e) {
                e.printStackTrace();
            }
        }
        this.variableIdx = variableIdx;
        this.weight = weight;
        paramSet = new FastSet<Integer>();
        paramSet.add(variableIdx);
        if (Double.isNaN(weight)) {
            throw new RuntimeException("NaN error:" + this);
        }

    }

    public SimpleGaussian(double weight, String variableName, double mean, double variance) {
//        logger.debug("Create " + variableName);
        int variableIdx = NodeDictionary.getInstance().nodeIdx(variableName);
        if (weight < 0) {
            try {
                throw new RuntimeException(" minus weight("
                        + NodeDictionary.getInstance().nodeName(variableIdx)
                        + ")=" + weight);
            } catch (NoSuchVariableException e) {
                e.printStackTrace();
            }
        }
        this.variableIdx = variableIdx;
        this.weight = weight;
        paramSet = new FastSet<Integer>();
        paramSet.add(variableIdx);
        if (Double.isNaN(weight)) {
            throw new RuntimeException("NaN error:" + this);
        }
        setMeanAndVariance(mean, variance);
    }

    public SimpleGaussian(double weight, int variableIdx) {

        if (weight < 0) {
            try {
                throw new RuntimeException(" minus weight("
                        + NodeDictionary.getInstance().nodeName(variableIdx)
                        + ")=" + weight);
            } catch (NoSuchVariableException e) {
                e.printStackTrace();
            }
        }
        this.variableIdx = variableIdx;
        this.weight = weight;
        paramSet = new FastSet<Integer>();
        paramSet.add(variableIdx);
        if (Double.isNaN(weight)) {
            throw new RuntimeException("NaN error:" + this);
        }

    }

    public SimpleGaussian(double weight, int variableIdx, double mean, double variance) {
//        logger.debug("Create " + variableName);

        if (weight < 0) {
            try {
                throw new RuntimeException(" minus weight("
                        + NodeDictionary.getInstance().nodeName(variableIdx)
                        + ")=" + weight);
            } catch (NoSuchVariableException e) {
                e.printStackTrace();
            }
        }
        this.variableIdx = variableIdx;
        this.weight = weight;
        paramSet = new FastSet<Integer>();
        paramSet.add(variableIdx);
        if (Double.isNaN(weight)) {
            throw new RuntimeException("NaN error:" + this);
        }
        setMeanAndVariance(mean, variance);
    }

    public double culminativeProbability(double referenceValue) {
        return 0.5 * (1 + Erf.erf((referenceValue - mean) / Math.sqrt(2 * variance)));

    }


    public void setVariable(String variable) {
        this.variableIdx = NodeDictionary.getInstance().nodeIdx(variable);
    }

    public void setMeanAndVariance(double mean, double variance) {
        if (Double.isNaN(variance)) {
            throw new RuntimeException("NaN variance error" + this);
        }
        if (Double.isNaN(mean)) {
            throw new RuntimeException("NaN mean error:" + this);
        }

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
        return variableIdx;
    }

    public String toString() {
        try {
            String variableName = NodeDictionary.getInstance().nodeName(
                    variableIdx);
            if (variance > GlobalParameters.THRESHOLD_INF) {
                DecimalFormat df = new DecimalFormat("#.###");
                return df.format(weight) + "N(" + variableName + ";"
                        + df.format(mean) + ", INF)";

            } else {
                DecimalFormat df = new DecimalFormat("#.###");
                return df.format(weight) + "N(" + variableName + ";"
                        + df.format(mean) + "," + df.format(variance) + ")";

            }
        } catch (NoSuchVariableException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public SimpleGaussian deepCopy() {
        SimpleGaussian newG = new SimpleGaussian(this.weight, this.variableIdx);
        newG.setMeanAndVariance(this.mean, this.variance);
        return newG;
    }

    @Override
    public boolean isSameForm(GaussianFunction tFunction) {
        if (!(tFunction instanceof SimpleGaussian)) {
            return false;
        }

        SimpleGaussian g = (SimpleGaussian) tFunction;

        if (g.getArgument() != this.getArgument()) {
            return false;
        }

        if (Math.abs(mean - g.getMean()) > GlobalParameters.SAMEFORM_ERROR_BOUND) {
            return false;
        }
        return !(Math.abs(variance - g.getVariance()) > GlobalParameters.SAMEFORM_ERROR_BOUND);
    }

    @Override
    public GaussianType type() {
        return GaussianType.SimpleGaussian;
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

    public static SimpleGaussian dummy(String variableName) {
        NodeDictionary nd = NodeDictionary.getInstance();
        if (!nd.containNode(variableName)) {
            nd.putValues(variableName, "true");
        }

        SimpleGaussian sg = new SimpleGaussian(1.0, variableName, 1.5, 2.5);
        return sg;
    }

}
