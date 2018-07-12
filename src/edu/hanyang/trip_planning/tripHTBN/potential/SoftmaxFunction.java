package edu.hanyang.trip_planning.tripHTBN.potential;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 24
 * Time: 오후 8:03
 * To change this template use File | Settings | File Templates.
 */
public class SoftmaxFunction implements UnivariateFunction {
    private int cardinality;
    double weights[] = {1, 2, 1, 2};
    double biases[] = {1, 2, 1, 2};
    int theIdx = 0;

    public SoftmaxFunction(double weights[], double biases[]) {
        this.weights = weights.clone();
        this.biases = biases.clone();
    }

    public void setIdx(int theIdx) {
        this.theIdx = theIdx;
    }

    public double value(double x, int idx) {
        double values[] = new double[weights.length];
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {

            values[i] = Math.exp(weights[i] * x + biases[i]);
            sum += values[i];
        }
        return values[idx] / sum;
    }

    public double[] distribution(double x) {
        double values[] = new double[weights.length];
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {

            values[i] = Math.exp(weights[i] * x + biases[i]);
            sum += values[i];
        }
        for (int i = 0; i < weights.length; i++) {
            values[i] = values[i] / sum;
        }
        return values;

    }

    @Override
    public double value(double x) {
        double values[] = new double[weights.length];
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {

            values[i] = Math.exp(weights[i] * x + biases[i]);
            sum += values[i];
        }

        return values[theIdx] / sum;

    }

    @Override
    public boolean hasConfidenceInterval() {
        return false;
    }

    @Override
    public double lowerConfidenceInterval(double sigma_multiplier) {
        throw new RuntimeException("Thos function does not have confidence interval");
    }

    @Override
    public double upperConfidenceInterval(double sigma_multiplier) {
        throw new RuntimeException("Thos function does not have confidence interval");
    }

    public static void main(String[] args) {
        double weights[] = {1, 0.2};
        double biases[] = {1, 0};
        SoftmaxFunction sf = new SoftmaxFunction(weights, biases);
        sf.setIdx(0);
        PlotFunction.plot("Softmax 0 ", sf, -20, 20, 0.1);
        sf.setIdx(1);
        PlotFunction.plot("Softmax 1 ", sf, -20, 20, 0.1);
//        sf.setIdx(2);
//        PlotFunction.plot("Softmax 2 ", sf, -20, 20, 0.1);
//        sf.setIdx(3);
//        PlotFunction.plot("Softmax 2 ", sf, -20, 20, 0.1);
    }
}
