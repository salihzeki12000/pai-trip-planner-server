package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import org.apache.log4j.Logger;

import java.util.List;


public class ProductTwoSimpleFactors {
    private static Logger logger = Logger.getLogger("ProductTwoSimpleTerms");

    /**
     * 안에 있는 SimpleFactor 들을 모두 곱해서 하나로 합친다.
     * <p/>
     * ProductTerm 안에 있는 freeParameter이 1개여야만 합칠수 있음
     * <p/>
     * 그렇지 않을 경우에는 예외처리할것
     *
     * @param simpleProductTerm
     * @return
     */
    public static SimpleFactor mergeFactors(SimpleProductTerm simpleProductTerm) {
        if (simpleProductTerm.getParameters().size() == 0) {
            return mergeSimpleFactor(simpleProductTerm.getSimpleFactorProduct());
        } else if (simpleProductTerm.getParameters().size() == 1) {
            return mergeSimpleFactor(simpleProductTerm.getSimpleFactorProduct());
        } else {
            throw new RuntimeException(
                    "This term has more than 2 free parameter");
        }
    }

    public static SimpleFactor mergeSimpleFactor(
            List<SimpleFactor> simpleFactorList) {
        SimpleFactor lump = simpleFactorList.get(0);
        for (int i = 1; i < simpleFactorList.size(); i++) {
            SimpleFactor segment = simpleFactorList.get(i);
            lump = productTwoSimpleFactor(lump, segment);
        }
        return lump;

    }

    public static SimpleFactor mergeConstantFactor(
            List<SimpleFactor> constantFactorList) {
        double lump = 1.0;
        for (int i = 0; i < constantFactorList.size(); i++) {
            ContinuousFactor factor = constantFactorList.get(i);
            double segment = factor.getWeight();
            lump = lump * segment;
        }
        return new ConstantValue(lump);
    }

    public static SimpleFactor productTwoSimpleFactor(SimpleFactor f1,
                                                      SimpleFactor f2) {
        if (f1 instanceof ConstantValue) {
            if (f2 instanceof ConstantValue) {
                return productTwoSimpleFactor((ConstantValue) f1,
                        (ConstantValue) f2);
            } else if (f2 instanceof SimpleGaussian) {
                return productTwoSimpleFactor((ConstantValue) f1,
                        (SimpleGaussian) f2);
            } else {
                throw new RuntimeException("unsupport function type: " + f2);
            }
        } else if (f1 instanceof SimpleGaussian) {
            if (f2 instanceof ConstantValue) {
                return productTwoSimpleFactor((SimpleGaussian) f1,
                        (ConstantValue) f2);
            } else if (f2 instanceof SimpleGaussian) {
                return productTwoSimpleFactor((SimpleGaussian) f1,
                        (SimpleGaussian) f2);
            } else {
                throw new RuntimeException("unsupport function type: " + f2);
            }

        } else {
            throw new RuntimeException("unsupported function type" + f1);
        }
    }

    private static SimpleFactor productTwoSimpleFactor(ConstantValue constant1,
                                                       ConstantValue constant2) {
        double v1 = constant1.getWeight();
        double v2 = constant2.getWeight();
        return new ConstantValue(v1 * v2);
    }

    private static SimpleFactor productTwoSimpleFactor(ConstantValue constant,
                                                       SimpleGaussian gaussian) {
        SimpleGaussian lump = gaussian.deepCopy();
        double newWeight = lump.getWeight() * constant.getWeight();
        lump.setWeight(newWeight);
        return lump;
    }

    private static SimpleFactor productTwoSimpleFactor(SimpleGaussian gaussian,
                                                       ConstantValue constant) {
        SimpleGaussian lump = gaussian.deepCopy();
        double newWeight = lump.getWeight() * constant.getWeight();
        lump.setWeight(newWeight);
        return lump;
    }

    private static SimpleFactor productTwoSimpleFactor(SimpleGaussian g1,
                                                       SimpleGaussian g2) {
        return GaussianUtil.productTwoSimpleGaussian(g1, g2);
    }

    public static void main(String args[]) {
        try {
            NodeDictionary nd = NodeDictionary.getInstance();
            nd.putValues("X", "true", "false");
            SimpleGaussian g1 = new SimpleGaussian(0.5, nd.nodeIdx("X"));
            g1.setMeanAndVariance(5, 10);
            SimpleGaussian g2 = new SimpleGaussian(0.5, nd.nodeIdx("X"));
            g2.setMeanAndVariance(5, 5);

            SimpleProductTerm pTerm = new SimpleProductTerm();
            pTerm.addSimpleFactor(new ConstantValue(1.0));
            pTerm.addSimpleFactor(new ConstantValue(0.5));
            pTerm.addSimpleFactor(g1);
            pTerm.addSimpleFactor(g2);
            // pTerm.addFactor(new SimpleUniform("X", 0.5));

            Logger.getRootLogger().info(
                    ProductTwoSimpleFactors.mergeFactors(pTerm));
        } catch (NoSuchVariableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
