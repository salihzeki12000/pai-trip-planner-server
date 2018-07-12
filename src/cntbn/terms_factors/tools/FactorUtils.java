package cntbn.terms_factors.tools;


import cntbn.terms_factors.*;

import java.util.List;


public class FactorUtils {
    public static double mergeConstantMixture(
            SimpleMixtureFactor mixtureOfConstantValue) {
        // 1. simpleTerm들을 모두 더함
        double sum = 0.0;
        List<SimpleTerm> simpleTermList = mixtureOfConstantValue
                .getMixtureOfSimpleTerm();
        for (int i = 0; i < simpleTermList.size(); i++) {
            ConstantValue cv = ((ConstantValue) simpleTermList.get(i));
            sum = sum + cv.getWeight();
        }
        return sum;
    }

    public static double mergeConstantMixture(
            ComplexMixtureFactor mixtureOfConstantValue) {
        // 1. simpleTerm들을 모두 더함
        double sum = 0.0;
        List<SimpleTerm> simpleTermList = mixtureOfConstantValue
                .getMixtureOfSimpleTerm();
        for (int i = 0; i < simpleTermList.size(); i++) {
            ConstantValue cv = ((ConstantValue) simpleTermList.get(i));
            sum = sum + cv.getWeight();
        }

        // 2. productTerm들을 모두 더함
        // 2.1 ProductTerm내부의 factor들을 모두 곱해서 하나의 double value로 변환함
        List<SimpleProductTerm> productTermList = mixtureOfConstantValue
                .getMixtureOfSimpleProductTerm();

        for (int i = 0; i < productTermList.size(); i++) {
            SimpleProductTerm productOfConstant = productTermList.get(i);
            ConstantValue productedValue = (ConstantValue) ProductTwoSimpleFactors
                    .mergeFactors(productOfConstant);
            sum = sum + productedValue.getWeight();
        }
        return sum;
    }

}
