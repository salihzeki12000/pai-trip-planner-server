package cntbn.terms_factors;

import cntbn.common.GlobalParameters;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class ConstantValue implements SimpleTerm, SimpleFactor {

    private double weight;
    Set<Integer> voidParamSet = new HashSet<Integer>();

    public ConstantValue(double weight) {
        this.weight = weight;
    }

    public String toString() {
        DecimalFormat format = new DecimalFormat("#.###");
        if (weight > GlobalParameters.THRESHOLD_INF) {
            return "(INF)";
        } else {
            return new String("(" + format.format(weight) + ")");
        }
    }

    public ConstantValue deepCopy() {
        return new ConstantValue(this.weight);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public Set<Integer> getParameters() {
        return voidParamSet;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
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
    public double getMean() {
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
    public double getVariance() {
        return Double.MAX_VALUE;
    }

    @Override
    public void shift(double offset) {
        // NOOP
    }

}
