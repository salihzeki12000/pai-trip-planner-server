package cntbn.terms_factors;

import cntbn.common.GlobalParameters;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Mixture distributions Factor (A+B)
 *
 * @author roland
 */
public class SimpleMixtureFactor implements ContinuousFactor {
    private Logger logger = Logger.getLogger(this.getClass());
    private List<SimpleTerm> simpleTermList;
    private Set<Integer> paramSet;
    private double sumOfWeights = 0.0;

    public SimpleMixtureFactor() {
        simpleTermList = new FastList<SimpleTerm>();
        paramSet = new HashSet<Integer>();
    }

    public SimpleMixtureFactor(SimpleTerm... terms) {
        simpleTermList = new FastList<SimpleTerm>();
        paramSet = new HashSet<Integer>();
        for (SimpleTerm simpleTerm : terms) {
            simpleTermList.add(simpleTerm);
            paramSet.addAll(simpleTerm.getParameters());
        }

    }

    public SimpleMixtureFactor(List<SimpleTerm> terms) {
        simpleTermList = new FastList<SimpleTerm>();
        paramSet = new HashSet<Integer>();
        for (SimpleTerm simpleTerm : terms) {
            simpleTermList.add(simpleTerm);
            paramSet.addAll(simpleTerm.getParameters());
        }
    }

    public void addSimpleTerm(SimpleTerm term) {
        paramSet.addAll(term.getParameters());
        simpleTermList.add(term);
        sumOfWeights += term.getWeight();
    }

    public List<SimpleTerm> getMixtureOfSimpleTerm() {
        return simpleTermList;
    }

    public void normalize(double basis) {
        sumOfWeights = basis;
        double sum = 0.0;
        for (int i = 0; i < simpleTermList.size(); i++) {
            SimpleTerm term = simpleTermList.get(i);
            sum += term.getWeight();
        }

        double normalizeFactor;
        if (sum < GlobalParameters.THRESHOLD_ZERO) {
            normalizeFactor = basis / (double) simpleTermList.size();
            for (int i = 0; i < simpleTermList.size(); i++) {
                SimpleTerm term = simpleTermList.get(i);
                term.setWeight(normalizeFactor);
            }

        } else {
            normalizeFactor = basis / sum;
            for (int i = 0; i < simpleTermList.size(); i++) {
                SimpleTerm term = simpleTermList.get(i);
                double newWeight = term.getWeight() * normalizeFactor;
                term.setWeight(newWeight);
            }
        }
        removeZeroWeightedTerm(simpleTermList);
    }

    @Override
    public boolean hasMixture() {
        return true;
    }

    private void removeZeroWeightedTerm(List<SimpleTerm> simpleTermList) {
        Iterator<SimpleTerm> it = simpleTermList.iterator();
        while (it.hasNext()) {
            SimpleTerm simpleTerm = it.next();
            if (simpleTerm.getWeight() < GlobalParameters.THRESHOLD_ZERO) {
                it.remove();
            }
        }
    }

    public String toString() {
        if (simpleTermList.size() == 0) {
            throw new RuntimeException("this factor is empty!!");
        }
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("[");
        Iterator<SimpleTerm> itSimple = simpleTermList.iterator();

        while (itSimple.hasNext()) {
            strBuf.append("s:" + itSimple.next());
            strBuf.append(" + ");
        }

        strBuf.deleteCharAt(strBuf.length() - 1);
        strBuf.deleteCharAt(strBuf.length() - 1);
        strBuf.append("]");
        return strBuf.toString();
    }

    public SimpleMixtureFactor deepCopy() {

        SimpleMixtureFactor md = new SimpleMixtureFactor();
        for (SimpleTerm term : simpleTermList) {
            md.addSimpleTerm(term.deepCopy());
        }
        return md;
    }

    @Override
    public Set<Integer> getParameters() {
        return paramSet;
    }

    @Override
    public double getMean() {
        double weight = 0.0;
        double mean = 0.0;

        for (int i = 0; i < simpleTermList.size(); i++) {
            SimpleGaussian gaussian = (SimpleGaussian) simpleTermList.get(i);

            if (gaussian.getVariance() < GlobalParameters.THRESHOLD_INF) {
                weight += gaussian.getWeight();
                mean += gaussian.getWeight() * gaussian.getMean();
            }
        }
        return mean / weight;
    }

    @Override
    public double getVariance() {

        /**
         * variance of mixture var = sum_i( w_i * (mu_i - mu)^2 + var_i^2)
         */
        double mean = getMean();
        double variance = 0.0;
        for (int i = 0; i < simpleTermList.size(); i++) {
            SimpleGaussian gaussian = (SimpleGaussian) simpleTermList.get(i);
            double w_i = gaussian.getWeight();
            double mu_i = gaussian.getMean();
            double var_i = gaussian.getVariance();
            variance = variance + w_i * ((mu_i - mean) * (mu_i - mean) + var_i);

        }
        return variance;
    }

    @Override
    public double getWeight() {
        return sumOfWeights;
    }

    @Override
    public void shift(double offset) {
        for (SimpleTerm simpleTerm : simpleTermList) {
            simpleTerm.shift(offset);
        }
    }

}
