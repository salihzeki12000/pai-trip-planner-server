package cntbn.terms_factors;

import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Factor들의 product들로 구성된 term
 * <p/>
 * (A+B)C(D+E) 같은 mixture와 simple들이 결합된 product term 임
 *
 * @author roland
 */
public class ComplexProductTerm implements ContinuousTerm {
    private Logger logger = Logger.getLogger(this.getClass());
    List<SimpleFactor> simpleFactors;
    List<SimpleMixtureFactor> mixtureFactors;
    ConstantValue constantValue = null;
    Set<Integer> paramSet = new HashSet<Integer>();

    public ComplexProductTerm() {
        simpleFactors = new FastList<SimpleFactor>();
        mixtureFactors = new FastList<SimpleMixtureFactor>();
    }

    public ComplexProductTerm(ComplexProductTerm productTerm) {
        simpleFactors = new FastList<SimpleFactor>(productTerm.simpleFactors);
        mixtureFactors = new FastList<SimpleMixtureFactor>(
                productTerm.mixtureFactors);
        this.paramSet.addAll(productTerm.paramSet);
    }

    public void addFactor(ContinuousFactor factor) {
        if (factor instanceof ConstantValue) {
            addConstantValue((ConstantValue) factor);
        } else if (factor instanceof SimpleFactor) {
            addSimpleFactor((SimpleFactor) factor);
        } else if (factor instanceof SimpleMixtureFactor) {
            addMixtureFactor((SimpleMixtureFactor) factor);
        } else if (factor instanceof ComplexMixtureFactor) {
            throw new RuntimeException("This is complexMixturFactor:" + factor);
        } else {
            throw new RuntimeException("ERROR in addFactor:" + factor);
        }

    }

    /**
     * ProductTerm에 constant value를 추가함.
     * <p/>
     * 기존에 있던 constantFactor와 merge를 수행함
     */
    public void addConstantValue(ConstantValue value) {
        if (this.constantValue == null) {
            this.constantValue = value;
            simpleFactors.add(value);
        } else {
            double newValue = this.constantValue.getWeight()
                    * value.getWeight();
            this.constantValue.setWeight(newValue);
        }
    }

    public void addSimpleFactor(SimpleFactor factor) {

        simpleFactors.add(factor);
    }

    public void addMixtureFactor(SimpleMixtureFactor factor) {
        mixtureFactors.add(factor);
    }


    public void addAll(ComplexProductTerm productTerm) {
        simpleFactors.addAll(productTerm.simpleFactors);
        mixtureFactors.addAll(productTerm.mixtureFactors);
        paramSet.addAll(productTerm.getParameters());
    }

    public List<SimpleFactor> getSimpleFactorProduct() {
        return simpleFactors;
    }

    public List<SimpleMixtureFactor> getMixtureFactorProduct() {
        return mixtureFactors;
    }

    /**
     * ProductTerm안에 mixture가 포함되어 있는지를 파악하는 함수 decompose나 merge를 할때 검사용으로 사용됨
     * <p/>
     * mixture를가지고 있으면 분해해야 할 대상이 되고, 그렇지 않으면 합쳐야 할 대상이 됨
     *
     * @return
     */
    public boolean hasMixture() {
        return mixtureFactors.size() != 0;
    }

    public ComplexProductTerm deepCopy() {
        ComplexProductTerm newProductTerm = new ComplexProductTerm();
        newProductTerm.addAll(this);
        return newProductTerm;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<");
        Iterator<SimpleFactor> itSimple = simpleFactors.iterator();
        while (itSimple.hasNext()) {
            strBuf.append(itSimple.next());
        }
        Iterator<SimpleMixtureFactor> itMixture = mixtureFactors.iterator();
        while (itMixture.hasNext()) {
            strBuf.append(itMixture.next());
        }

        strBuf.append(">");
        return strBuf.toString();
    }

    @Override
    public Set<Integer> getParameters() {
        return paramSet;
    }

    public SimpleProductTerm convertToSimpleProductTerm() {
        if (hasMixture()) {
            throw new RuntimeException("This has mixture Term: "
                    + this.toString());
        }
        return new SimpleProductTerm(simpleFactors);
    }

    @Override
    public void shift(double offset) {
        throw new RuntimeException("Not support");

    }
}
