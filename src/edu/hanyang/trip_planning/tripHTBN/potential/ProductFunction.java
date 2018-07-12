package edu.hanyang.trip_planning.tripHTBN.potential;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 24
 * Time: 오후 8:35
 * To change this template use File | Settings | File Templates.
 */
public class ProductFunction implements UnivariateFunction {
    public UnivariateFunction f1;
    public UnivariateFunction f2;


    public ProductFunction(UnivariateFunction u1, UnivariateFunction u2) {
        this.f1 = u1;
        this.f2 = u2;
    }

    @Override
    public double value(double x) {
        return f1.value(x) * f2.value(x);
    }

    @Override
    public boolean hasConfidenceInterval() {
        return f1.hasConfidenceInterval() || f2.hasConfidenceInterval();
    }

    @Override
    public double lowerConfidenceInterval(double sigma_multiplier) {
        if (f1.hasConfidenceInterval() && f2.hasConfidenceInterval()) {
            return Math.min(f1.lowerConfidenceInterval(sigma_multiplier), f2.lowerConfidenceInterval(sigma_multiplier));
        } else if (f1.hasConfidenceInterval() && !f2.hasConfidenceInterval()) {
            return f1.lowerConfidenceInterval(sigma_multiplier);
        } else if (!f1.hasConfidenceInterval() && f2.hasConfidenceInterval()) {
            return f2.lowerConfidenceInterval(sigma_multiplier);
        } else {
            throw new RuntimeException("this function does not have confidence interval");
        }
    }

    @Override
    public double upperConfidenceInterval(double sigma_multiplier) {
        if (f1.hasConfidenceInterval() && f2.hasConfidenceInterval()) {
            return Math.max(f1.upperConfidenceInterval(sigma_multiplier), f2.upperConfidenceInterval(sigma_multiplier));
        } else if (f1.hasConfidenceInterval() && !f2.hasConfidenceInterval()) {
            return f1.upperConfidenceInterval(sigma_multiplier);
        } else if (!f1.hasConfidenceInterval() && f2.hasConfidenceInterval()) {
            return f2.upperConfidenceInterval(sigma_multiplier);
        } else {
            throw new RuntimeException("this function does not have confidence interval");
        }

    }

    public static void main(String[] args) {
        GaussianFunction sg = new GaussianFunction(4, 1);
        double weights[] = {1, 1};
        double biases[] = {2, 1};
        SoftmaxFunction soft = new SoftmaxFunction(weights, biases);
        ProductFunction product = new ProductFunction(sg, soft);
        double lower = product.lowerConfidenceInterval(3);
        double upper = product.upperConfidenceInterval(3);
        System.out.println("upper = " + upper);
        System.out.println("lower = " + lower);
        PlotFunction.plot("product", product, lower, upper, 0.1);
        PlotFunction.plot("softmax", soft, -20, 20, 0.1);
        UnivariateIntegral integral = new UnivariateIntegral(product);
        System.out.println("integral = " + integral.integal(-lower, upper, 0.1));
    }
}
