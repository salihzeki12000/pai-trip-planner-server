package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import org.apache.log4j.Logger;

import java.util.List;


public class ProductOfTwoMixtureFactors {
    private static Logger logger = Logger
            .getLogger(ProductOfTwoMixtureFactors.class);

    /**
     * 두 factor 를 곱한다. 인자는 계산과정에서 변한다.
     * <p/>
     * 재사용이 필요하면 복사해서 인자로 집어넣어야 함.
     *
     * @param f1
     * @param f2
     * @return
     */
    public static ComplexMixtureFactor product(ComplexMixtureFactor f1,
                                               ComplexMixtureFactor f2) {

        // (AB+C)
        // 1. 어느 한쪽이 비어있을때 체크
        if (f1.empty() & !f2.empty()) {
            return f2;
        } else if (!f1.empty() && f2.empty()) {
            return f1;
        } else {
            return productNonEmpty(f1, f2);
        }
        // 2. 둘다 꽉 차있을? 계산할것
    }

    private static ComplexMixtureFactor productNonEmpty(ComplexMixtureFactor f1,
                                                        ComplexMixtureFactor f2) {
        /**
         * (AB +C)(D+EF)
         *
         * simple x simple : CD
         *
         * simple x product : CEF
         *
         * product x simple : ABD
         *
         * product x product : ABEF
         */
        List<SimpleTerm> list_s1 = f1.getMixtureOfSimpleTerm();
        List<SimpleTerm> list_s2 = f2.getMixtureOfSimpleTerm();
        List<SimpleProductTerm> list_p1 = f1.getMixtureOfSimpleProductTerm();
        List<SimpleProductTerm> list_p2 = f2.getMixtureOfSimpleProductTerm();
        ComplexMixtureFactor result = new ComplexMixtureFactor();
        for (SimpleTerm s1 : list_s1) {
            for (SimpleTerm s2 : list_s2) {
                SimpleProductTerm p = new SimpleProductTerm();
                p.addSimpleFactor((SimpleFactor) s1);
                p.addSimpleFactor((SimpleFactor) s2);
                result.addProductTerm(p);
            }
        }
        for (SimpleTerm s1 : list_s1) {
            for (SimpleProductTerm p2 : list_p2) {
                SimpleProductTerm p = new SimpleProductTerm();
                p.addSimpleFactor((SimpleFactor) s1);
                p.addAll(p2);
                result.addProductTerm(p);
            }
        }
        for (SimpleProductTerm p1 : list_p1) {
            for (SimpleTerm s2 : list_s2) {
                SimpleProductTerm p = new SimpleProductTerm();
                p.addAll(p1);
                p.addSimpleFactor((SimpleFactor) s2);
                result.addProductTerm(p);
            }
        }
        for (SimpleProductTerm p1 : list_p1) {
            for (SimpleProductTerm p2 : list_p2) {
                SimpleProductTerm p = new SimpleProductTerm();
                p.addAll(p1);
                p.addAll(p2);
                result.addProductTerm(p);
            }
        }
        return result;
    }

    public static void main(String args[]) {
        // (AB+C)(D+EF)  = ABD + CD + ABEF+ CEF
        try {
            NodeDictionary nd = NodeDictionary.getInstance();
            nd.putValues("A", "true", "false");
            nd.putValues("B", "true", "false");
            nd.putValues("C", "true", "false");
            nd.putValues("D", "true", "false");
            nd.putValues("E", "true", "false");
            nd.putValues("F", "true", "false");

            SimpleGaussian g1 = new SimpleGaussian(0.5, nd.nodeIdx("A"));

            g1.setMeanAndVariance(2, 1);
            SimpleGaussian g2 = new SimpleGaussian(0.5, nd.nodeIdx("B"));
            g2.setMeanAndVariance(5, 1);
            SimpleGaussian g3 = new SimpleGaussian(0.5, nd.nodeIdx("C"));
            g3.setMeanAndVariance(5, 1);
            SimpleGaussian g4 = new SimpleGaussian(0.5, nd.nodeIdx("D"));
            SimpleGaussian g5 = new SimpleGaussian(0.5, nd.nodeIdx("E"));
            SimpleGaussian g6 = new SimpleGaussian(0.5, nd.nodeIdx("F"));

            SimpleProductTerm p1 = new SimpleProductTerm();
            p1.addSimpleFactor(g1);
            p1.addSimpleFactor(g2);

            ComplexMixtureFactor cf1 = new ComplexMixtureFactor();
            cf1.addProductTerm(p1);
            cf1.addSimpleTerm(g3);
            ComplexMixtureFactor cf2 = new ComplexMixtureFactor();
            cf2.addSimpleTerm(g4);
            SimpleProductTerm p2 = new SimpleProductTerm();
            p2.addSimpleFactor(g5);
            p2.addSimpleFactor(g6);

            cf2.addProductTerm(p2);

            logger.info(cf1);
            logger.info(cf2);
            logger.info(ProductOfTwoMixtureFactors.product(cf1, cf2));
        } catch (NoSuchVariableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
