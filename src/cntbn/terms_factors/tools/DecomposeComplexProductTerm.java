package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import javolution.util.FastList;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * ComplexProductTerm을 분해하는 class
 * <p/>
 * (A+B)(C+D)EF 같은 ComplexProductTerm을 분해하는 역할을 한다.
 * <p/>
 * (A+B)(C+D)EF  = ACEF + BCEF+ ADEF+BDEF 로 바꾸어줌
 *
 * @author roland
 */
public class DecomposeComplexProductTerm {
    private static Logger logger = Logger
            .getLogger("DecomposeComplexProductTerm");

    /**
     * ComplexProductTerm을 SimpleProductTerm들의 List로 분해하는 class
     * <p/>
     * (A+B)(C+D)EF 같은 ComplexProductTerm을 분해하는 역할을 한다.
     *
     * @param productTerm
     * @return
     */
    public static List<SimpleProductTerm> decompose(
            ComplexProductTerm productTerm) {
        /**
         * 1. SimpleFactor들이 2개 이상인 경우에는 모든 simpleFactor 들을 곱해서 ProductTerm 하나로
         * 만든다. 예) ABC => 변경: 수정사항 SimpleFactor들의 곱은 계산하지 않는다. 계산이 끝난후에 나중에
         * 처리한다.
         *
         * 2. MixtureFactor들이 2개 이상인 경우에는 모든 mixutreFactor 들을 곱해서
         * ArrayList<ProductTerm>로 만든다. 예) (DE + FG + HI ) 등 => 결국 하나의
         * MixtureFactor다.
         *
         * 3. 1과 2의 결과를 곱한다. MixtureFactor 와 SimpleFactor의 곱이된다.
         */
        List<SimpleMixtureFactor> mixtureFactorList = productTerm
                .getMixtureFactorProduct();
        List<SimpleFactor> simpleFactorList = productTerm
                .getSimpleFactorProduct();
        if (simpleFactorList.size() == 0 && mixtureFactorList.size() == 0) {
            throw new RuntimeException("This factor is empty");
        }
        // ex: A B C D 등
        else if (simpleFactorList.size() > 0 && mixtureFactorList.size() == 0) {
            // 하나짜리 ArrayList<ProductTerm> 을 반환할것
            List<SimpleProductTerm> resultTermList = new FastList<SimpleProductTerm>();
            SimpleProductTerm resultTerm = new SimpleProductTerm();
            for (int i = 0; i < simpleFactorList.size(); i++) {
                resultTerm.addSimpleFactor(simpleFactorList.get(i));
            }
            resultTermList.add(resultTerm);
            return resultTermList;

        } else if (simpleFactorList.size() == 0 && mixtureFactorList.size() > 0) {
            // ex: (A+B)(C+D)(E+F)등
            return productSimpleMixtures(mixtureFactorList);

        } else {
            // ex: (A+B)(C+D)EF..
            List<SimpleProductTerm> mixtureProductTermList = productSimpleMixtures(mixtureFactorList);

            for (int i = 0; i < mixtureProductTermList.size(); i++) {
                SimpleProductTerm resultProductTerm = mixtureProductTermList
                        .get(i);
                for (int j = 0; j < simpleFactorList.size(); j++) {
                    resultProductTerm.addSimpleFactor(simpleFactorList.get(j));
                }
            }
            return mixtureProductTermList;
        }
    }

    /**
     * MixtureFactor들의 곱셈을 계산한다. (A+B)(C+D) 정도
     *
     * @param factorList
     * @return
     */
    private static List<SimpleProductTerm> productSimpleMixtures(
            List<SimpleMixtureFactor> factorList) {
        if (factorList.size() == 0) {
            throw new RuntimeException("factorList is empty ");
        }
        if (factorList.size() == 1) {
            // 하나면 어떻게 하나? 그냥 반환하면 되겠군 .. MixtureFa
            SimpleMixtureFactor mixtureFactor = factorList.get(0);

            List<SimpleProductTerm> result = new FastList<SimpleProductTerm>(
                    convert(mixtureFactor.getMixtureOfSimpleTerm()));
            return result;
            // 여러개일때
        } else {
            return productManyMixtures(factorList);
        }
    }

    /**
     * 두개 이상일때만 처리 (A+B)(C+D)와 같이 mixtureFactor를 곱함
     *
     * @return
     */
    private static List<SimpleProductTerm> productManyMixtures(
            List<SimpleMixtureFactor> factorList) {

        List<SimpleMixtureFactor> decomposedFactorList = new FastList<SimpleMixtureFactor>();
        for (int i = 0; i < factorList.size(); i++) {
            SimpleMixtureFactor tmpMixtureFactor = factorList.get(i);
            decomposedFactorList.add(tmpMixtureFactor);
        }
        List<SimpleProductTerm> lump = productTwoMixture(
                decomposedFactorList.get(0), decomposedFactorList.get(1));

        for (int i = 2; i < decomposedFactorList.size(); i++) {
            SimpleMixtureFactor mixtureFactor2 = decomposedFactorList.get(i);

            List<SimpleTerm> nugget = mixtureFactor2
                    .getMixtureOfSimpleTerm();

            if (nugget.size() > 0) {
                lump = productTwoMixture(lump, nugget);
            }
        }
        return lump;
    }

    /**
     * 두개의 mixture의 product를 구한다.
     *
     * @param mixture1
     * @param mixture2
     * @return
     */
    private static List<SimpleProductTerm> productTwoMixture(
            SimpleMixtureFactor mixture1, SimpleMixtureFactor mixture2) {        /*
         * (A+B)(C+D) ���
		 */
        List<SimpleProductTerm> result = new FastList<SimpleProductTerm>();
        List<SimpleTerm> termList1 = mixture1.getMixtureOfSimpleTerm();
        List<SimpleTerm> termList2 = mixture2.getMixtureOfSimpleTerm();
        for (int idx1 = 0; idx1 < termList1.size(); idx1++) {
            for (int idx2 = 0; idx2 < termList2.size(); idx2++) {
                SimpleProductTerm productTerm = new SimpleProductTerm();
                productTerm.addSimpleFactor((SimpleFactor) termList1.get(idx1));
                productTerm.addSimpleFactor((SimpleFactor) termList2.get(idx2));
                result.add(productTerm);
            }
        }
        return result;
    }

    private static List<SimpleProductTerm> productTwoMixture(
            List<SimpleProductTerm> termList1,
            List<SimpleTerm> termList2) {

        List<SimpleProductTerm> result = new FastList<SimpleProductTerm>();

        for (int idx1 = 0; idx1 < termList1.size(); idx1++) {
            for (int idx2 = 0; idx2 < termList2.size(); idx2++) {
                SimpleProductTerm productTerm = new SimpleProductTerm();
                SimpleProductTerm term1 = termList1.get(idx1);
                ContinuousTerm term2 = termList2.get(idx2);
                productTerm.addAll(term1);
                productTerm.addSimpleFactor((SimpleFactor) term2);
                result.add(productTerm);
            }
        }
        return result;
    }

    private static List<SimpleProductTerm> convert(
            List<SimpleTerm> terms) {
        List<SimpleProductTerm> result = new FastList<SimpleProductTerm>();
        for (int i = 0; i < terms.size(); i++) {
            SimpleProductTerm productTerm = new SimpleProductTerm();
            productTerm.addSimpleFactor((SimpleFactor) terms.get(i));
            result.add(productTerm);
        }
        return result;
    }

    /**
     * (A+B)(C+D)EF 를 decompose 함
     */
    public static void testComplexProductTermDecompose() throws NoSuchVariableException {
//  EF(A+B)(C+D) = ACEF+ADEF+BCEF+BDEF
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("A", "true", "false");
        nd.putValues("B", "true", "false");
        nd.putValues("C", "true", "false");
        nd.putValues("D", "true", "false");
        nd.putValues("E", "true", "false");
        nd.putValues("F", "true", "false");


        SimpleGaussian a = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("A"));
        SimpleGaussian b = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("B"));
        SimpleGaussian c = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("C"));
        SimpleGaussian d = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("D"));
        SimpleGaussian e = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("E"));
        SimpleGaussian f = new SimpleGaussian(1.0, NodeDictionary.getInstance().nodeIdx("F"));
        SimpleMixtureFactor AB = new SimpleMixtureFactor();
        AB.addSimpleTerm(a);
        AB.addSimpleTerm(b);
        SimpleMixtureFactor CD = new SimpleMixtureFactor();
        CD.addSimpleTerm(c);
        CD.addSimpleTerm(d);
        ComplexProductTerm productTerm = new ComplexProductTerm();
        productTerm.addMixtureFactor(AB);
        productTerm.addMixtureFactor(CD);
        productTerm.addSimpleFactor(e);
        productTerm.addSimpleFactor(f);
        Logger.getRootLogger().debug(productTerm);
        Logger.getRootLogger().debug(decompose(productTerm));
    }

    public static void main(String args[]) {
        try {
            testComplexProductTermDecompose();
        } catch (NoSuchVariableException e) {
            e.printStackTrace();
        }
    }
}
