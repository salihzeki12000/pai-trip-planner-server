package cntbn.terms_factors;


/**
 * Gaussian term or factor
 *
 * @author wykwon
 */
public interface GaussianFunction extends SimpleFactor, SimpleTerm {

    int getArgument();

    double getWeight();

    void setWeight(double weight);

    boolean isSameForm(GaussianFunction tFunction);

    GaussianType type();

    GaussianFunction deepCopy();

}
