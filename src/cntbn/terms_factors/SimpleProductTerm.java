package cntbn.terms_factors;

import cntbn.exception.TermAndFactorException;
import javolution.util.FastList;
import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Product term such as AB
 *
 * @author roland
 */
public class SimpleProductTerm implements ContinuousTerm {
    private Logger logger = Logger.getLogger(this.getClass());
    private List<SimpleFactor> simpleFactors;
    private Set<Integer> paramSet = new FastSet<Integer>();
    private boolean bHasTDGaussian = false;
    private int tdGaussianCnt = 0;

    public SimpleProductTerm() {
        simpleFactors = new FastList<SimpleFactor>();
    }

    public SimpleProductTerm(List<SimpleFactor> factors) {
        simpleFactors = new FastList<SimpleFactor>();
        this.simpleFactors.addAll(factors);
        Iterator<SimpleFactor> it = factors.iterator();

        while (it.hasNext()) {
            SimpleFactor simpleFactor = it.next();
            if (simpleFactor instanceof TDGaussian) {
                bHasTDGaussian = true;
                tdGaussianCnt++;
            }
            paramSet.addAll(simpleFactor.getParameters());
        }
        if (tdGaussianCnt > 1) {
            throw new RuntimeException(
                    "the number of TDGaussian must not be exceed 1");
        }
    }

    public SimpleProductTerm(SimpleProductTerm productTerm) {
        simpleFactors = new FastList<SimpleFactor>(productTerm.simpleFactors);
        this.paramSet.addAll(productTerm.paramSet);
        this.bHasTDGaussian = productTerm.bHasTDGaussian;
    }

    public void addSimpleFactor(SimpleFactor factor) {
        if (factor instanceof TDGaussian) {
            bHasTDGaussian = true;
            tdGaussianCnt++;
        }
        simpleFactors.add(factor);
        paramSet.addAll(factor.getParameters());
        if (tdGaussianCnt > 1) {
            throw new RuntimeException(
                    "the number of TDGaussian must not be exceed 1");
        }

    }

    public void addAll(SimpleProductTerm productTerm) {
        simpleFactors.addAll(productTerm.simpleFactors);
        paramSet.addAll(productTerm.getParameters());
    }

    public List<SimpleFactor> getSimpleFactorProduct() {
        return simpleFactors;
    }

    public SimpleFactor convertSimpleFactor() throws TermAndFactorException {
        if (simpleFactors.size() == 1) {
            return simpleFactors.get(0);
        }
        throw new TermAndFactorException(
                this.toString()
                        + " Cannot convert productTerm to simpleFactor: factor size is not 1");
    }

    public SimpleProductTerm deepCopy() {
        SimpleProductTerm productTerm = new SimpleProductTerm();
        Iterator<SimpleFactor> itFactor = simpleFactors.iterator();
        while (itFactor.hasNext()) {
            productTerm.addSimpleFactor(itFactor.next());
        }
        return productTerm;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        Iterator<SimpleFactor> itSimple = simpleFactors.iterator();
        while (itSimple.hasNext()) {
            strBuf.append(itSimple.next());
        }
        return strBuf.toString();
    }

    @Override
    public Set<Integer> getParameters() {
        return paramSet;
    }

    @Override
    public void shift(double offset) {
        for (SimpleFactor simpleFactor : simpleFactors) {
            simpleFactor.shift(offset);
        }
    }

    public boolean hasTDGaussian() {
        return bHasTDGaussian;
    }
}
