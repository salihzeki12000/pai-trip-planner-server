package cntbn.terms_factors;

import cntbn.terms_factors.tools.DecomposeComplexProductTerm;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Mixture distributions 표현 (a+bc+d) 처럼 productFactor와 simpleFactor가 혼합된 형태이다.
 * <p/>
 * a+(b+c)d+e같은건 허용하지 않는다.
 *
 * @author roland
 */
public class ComplexMixtureFactor implements ContinuousFactor {
    private Logger logger = Logger.getLogger(this.getClass());
    List<SimpleTerm> simpleTermList = new FastList<SimpleTerm>();
    List<SimpleProductTerm> productTermList = new FastList<SimpleProductTerm>();

    public boolean empty() {
        return simpleTermList.size() == 0 && productTermList.size() == 0;
    }

    public void addTerm(ContinuousTerm term) {
        if (term instanceof SimpleTerm) {
            addSimpleTerm((SimpleTerm) term);
        } else if (term instanceof SimpleProductTerm) {
            addProductTerm((SimpleProductTerm) term);
        } else if (term instanceof ComplexProductTerm) {
            addComplexProductTerm((ComplexProductTerm) term);
        } else {
            throw new RuntimeException("adding complex product term:" + term
                    + " into " + this);
        }
    }

    /**
     * 같은 type의 클래스로 내용을 대치시킴 참조연산할것
     *
     * @param factor
     */
    public void replace(ComplexMixtureFactor factor) {
        simpleTermList = factor.simpleTermList;
        productTermList = factor.productTermList;
    }

    public void addSimpleTerm(SimpleTerm term) {
        simpleTermList.add(term);
    }

    public void addProductTerm(SimpleProductTerm term) {
        productTermList.add(term);
    }

    /**
     * complex product term을 분해한 후에 더한다. (a+bc+d) + [(e+f)g] 같은 연산이다.
     *
     * @param term
     */
    public void addComplexProductTerm(ComplexProductTerm term) {
        List<SimpleProductTerm> simpleTermList = DecomposeComplexProductTerm
                .decompose(term);
        productTermList.addAll(simpleTermList);

        // ComplexMixtureFactor newFactor = new ComplexMixtureFactor();
        //
        // for (SimpleProductTerm decomposedTerm : simpleTermList) {
        // newFactor.addTerm(decomposedTerm);
        // }
        // ComplexMixtureFactor result =
        // ProductOfTwoMixtureFactors.product(this,
        // newFactor);
        // this.replace(result);
        // logger.debug(this.toString());
    }

    public void addAll(ComplexMixtureFactor md) {

        simpleTermList.addAll(md.getMixtureOfSimpleTerm());
        productTermList.addAll(md.getMixtureOfSimpleProductTerm());
    }

    public List<SimpleTerm> getMixtureOfSimpleTerm() {
        return simpleTermList;
    }

    public List<SimpleProductTerm> getMixtureOfSimpleProductTerm() {
        return productTermList;
    }

    public boolean hasProductTerm() {
        return productTermList.size() != 0;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<");
        Iterator<SimpleTerm> itSimple = simpleTermList.iterator();
        while (itSimple.hasNext()) {
            strBuf.append(itSimple.next());
            strBuf.append(" + ");
        }
        Iterator<SimpleProductTerm> itProduct = productTermList.iterator();
        while (itProduct.hasNext()) {
            strBuf.append(itProduct.next());
            strBuf.append(" + ");
        }
        if (strBuf.length() != 0)
            strBuf.deleteCharAt(strBuf.length() - 1);
        if (strBuf.length() != 0)
            strBuf.deleteCharAt(strBuf.length() - 1);
        strBuf.append(">");
        return strBuf.toString();
    }

    public ComplexMixtureFactor deepCopy() {
        throw new UnsupportedOperationException("아직 구현 안했음");
    }

    @Override
    public Set<Integer> getParameters() {
        throw new UnsupportedOperationException(
                "ComplexFactor의 parameter가져갈일 없다. ");
    }

    public SimpleMixtureFactor convertToSimpleMixtureFactor() {
        if (hasProductTerm()) {
            throw new RuntimeException("This has ProductTerm:" + this);
        }
        return new SimpleMixtureFactor(simpleTermList);
    }

    @Override
    public double getMean() {
        //
        throw new UnsupportedOperationException("지원 안한다.  호출하지 마라~");
    }

    @Override
    public void normalize(double basis) {
        throw new UnsupportedOperationException("지원 안한다.  호출하지 마라~");
    }

    @Override
    public boolean hasMixture() {
        return true;
    }

    @Override
    public double getVariance() {
        throw new UnsupportedOperationException("지원 안한다.  호출하지 마라~");
    }

    @Override
    public double getWeight() {
        throw new UnsupportedOperationException("지원 안한다.  호출하지 마라~");
    }

    @Override
    public void shift(double offset) {
        // for (SimpleTerm simpleTerm : simpleTermList) {
        // simpleTerm.shift(offset);
        // }
        // for (SimpleProductTerm simpleProductTerm : productTermList) {
        // simpleProductTerm.shift(offset);
        // }
        throw new RuntimeException("Not support");
    }

}
