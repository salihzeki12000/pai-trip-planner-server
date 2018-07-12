package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.exception.NoSuchVariableException;
import cntbn.terms_factors.*;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * ConstantValue를 포함하는 SimpleProductTerm을 간소화시킨다.
 *
 * @author wykwon
 */
public class ReduceSimpleProductTerm {
    private static Logger logger = Logger.getLogger("ReduceSimpleProductTerm");

    /**
     * ConstantValue나 uniformFunction 을 포함하는 SimpleProductTerm을 간소화시킨다.
     * NormalDistribution들의 곱도 처리할수 있어야 한다.
     *
     * @param productTerm
     * @return
     */
    public static SimpleProductTerm reduceConstantFactor(
            SimpleProductTerm productTerm) {
        SimpleProductTerm newProductTerm = new SimpleProductTerm();
        List<SimpleFactor> factorList = productTerm.getSimpleFactorProduct();
        double lump = 1.0;
        for (int i = 0; i < factorList.size(); i++) {
            SimpleFactor simpleFactor = factorList.get(i);
            if (simpleFactor instanceof ConstantValue) {
                lump = lump * simpleFactor.getWeight();
            } else {
                newProductTerm.addSimpleFactor(simpleFactor);
            }
        }

        SimpleFactor firstSimpleFactor = newProductTerm
                .getSimpleFactorProduct().get(0);
        double newWeight = firstSimpleFactor.getWeight() * lump;
        firstSimpleFactor.setWeight(newWeight);

        // if (firstSimpleFactor instanceof GaussianFunction) {
        // GaussianFunction sg = (GaussianFunction) firstSimpleFactor;
        // double newWeight = sg.getWeight() * lump;
        // sg.setWeight(newWeight);
        // } else if (firstSimpleFactor instanceof TDGaussian) {
        // TDGaussian cg = (TDGaussian) firstSimpleFactor;
        // double newWeight = cg.getWeight() * lump;
        // cg.setWeight(newWeight);
        // } else {
        // throw new RuntimeException("error with term: " + productTerm);
        // }
        return newProductTerm;
    }

    /**
     * simpleVariable을 가진 simpleFactor들을 하나로 합친다.
     *
     * @param simpleProductTerm
     * @return
     */
    public static ContinuousTerm reduceFactor(
            SimpleProductTerm simpleProductTerm) {
        if (simpleProductTerm.getSimpleFactorProduct().size() == 1) {
            return (ContinuousTerm) simpleProductTerm.convertSimpleFactor();
        }
        // 1. 새로운 productTerm을 준비
        SimpleProductTerm resultTerm = new SimpleProductTerm();

        // 처리할 대상
        // A. constantValue
        // B. Univariate function
        // C. conditional Gaussian

        // 2. constant factor들의 곱을 담기위한 변수준비
        double constantValue = popConstantValue(simpleProductTerm);

        // 3. sameVariable을 갖는 UnivariateFunction들의 목록을 획득
        Map<Integer, List<GaussianFunction>> sameVariabledFactorMap = popSameVariabledUnivariateFunction(simpleProductTerm);
        Iterator<Entry<Integer, List<GaussianFunction>>> itSameVariabledFactor = sameVariabledFactorMap
                .entrySet().iterator();
        // 3-1 sameVariable 을 갖는 Univariate들을 합친후에 resultTerm에 반영
        // sameVaribledFucntion이 없을수도 있다.
        while (itSameVariabledFactor.hasNext()) {
            Entry<Integer, List<GaussianFunction>> entry = itSameVariabledFactor
                    .next();
            List<GaussianFunction> simpleFactorList = entry.getValue();
            if (simpleFactorList.size() > 0) {
                SimpleFactor mergedFactor = mergeSameVariabledSimpleFactor(simpleFactorList);
                resultTerm.addSimpleFactor(mergedFactor);
            }
        }
        // 5. constantValue를 weight에 반영
        setSimpleProductTermWeight(constantValue, resultTerm);
        // 5. 만약 productTerm안에 Factor가 하나라면 이건 SimpleTerm이라고...
        if (resultTerm.getSimpleFactorProduct().size() == 1) {
            // 6. constantValue를 반영
            return (ContinuousTerm) resultTerm.getSimpleFactorProduct().get(0);

        } else {
            // 6. constantValue를 반영
            return resultTerm;
        }
    }

    private static SimpleFactor mergeSameVariabledSimpleFactor(
            List<GaussianFunction> sameVariabledSimpleFactorList) {
        if (sameVariabledSimpleFactorList.size() == 0) {
            throw new RuntimeException("factorList empty");
        }
        SimpleFactor lump = sameVariabledSimpleFactorList.get(0);
        for (int i = 1; i < sameVariabledSimpleFactorList.size(); i++) {
            SimpleFactor segment = sameVariabledSimpleFactorList.get(i);
            lump = ProductTwoSimpleFactors
                    .productTwoSimpleFactor(lump, segment);
        }
        return lump;
    }

    /**
     * SimpleProductTerm안에 포함된 univariate function들을 argument별로 구분해서 반환함
     *
     * @param simpleProductTerm simpleFactor들로 구성된 productTerm
     *                          <p/>
     *                          factor들이 추출되면 추출된 factor는 productTerm에서 없어짐
     * @return Map<String, UnivariateFunction>의 형태로서 argument별로 구분되는
     *         UnivariateFunction들
     */
    private static Map<Integer, List<GaussianFunction>> popSameVariabledUnivariateFunction(
            SimpleProductTerm simpleProductTerm) {
        // 1. 새로운 map을 준비
        Map<Integer, List<GaussianFunction>> sameVariabledFactorMap = new FastMap<Integer, List<GaussianFunction>>();
        // 2. SimpleProductTerm들의 목록을 획득
        List<SimpleFactor> simpleFactorList = simpleProductTerm
                .getSimpleFactorProduct();

        // 3. productTerm내의 모든 argument 에 대해 iteration 시작
        Iterator<Integer> argumentIt = simpleProductTerm.getParameters()
                .iterator();
        while (argumentIt.hasNext()) {
            Integer argument = argumentIt.next();
            // 3-1. argument별로 factor를 담을 arrayList를 준비
            List<GaussianFunction> functionList = new FastList<GaussianFunction>();
            Iterator<SimpleFactor> factorIt = simpleFactorList.iterator();
            while (factorIt.hasNext()) {
                SimpleFactor simpleFactor = factorIt.next();
                // 3-1. argument를 인지로 갖는 univariate function을 추출함
                if (simpleFactor instanceof GaussianFunction
                        && simpleFactor.getParameters().contains(argument)) {
                    functionList.add((GaussianFunction) simpleFactor);
                    // 3-2. 추출이 끝난 factor를 iterator에서 삭제
                    factorIt.remove();
                }
                // 3-3. functionList의 크기가 1이상인 경우 map에 담을것
                sameVariabledFactorMap.put(argument, functionList);
            }
        }
        return sameVariabledFactorMap;
    }

    /**
     * @param simpleProductTerm
     * @return
     */
    private static double popConstantValue(SimpleProductTerm simpleProductTerm) {
        // 1. 결과를 담을 그릇을 준비
        double lump = 1.0;
        // 2. SimpleProductTerm들의 목록을 획득
        Iterator<SimpleFactor> simpleFactorIt = simpleProductTerm
                .getSimpleFactorProduct().iterator();
        // 3. 각각의 simpleFactor에 대해 iteration
        while (simpleFactorIt.hasNext()) {
            SimpleFactor simpleFactor = simpleFactorIt.next();
            if (simpleFactor instanceof ConstantValue) {
                // 3-1. ConstantValue타입의 factor를 추출후 lump에 반영
                lump = lump * simpleFactor.getWeight();
                // 3-2. 추출한 factor를 productTerm에서 삭제
                simpleFactorIt.remove();
            }
        }
        return lump;
    }

    private static void setSimpleProductTermWeight(double weight,
                                                   SimpleProductTerm simpleProductTerm) {
        if (simpleProductTerm.getSimpleFactorProduct().size() == 0) {

            logger.fatal("weight=" + weight + "term=" + simpleProductTerm);
            throw new RuntimeException("weight=" + weight + "  term="
                    + simpleProductTerm);
        }
        SimpleFactor factor = simpleProductTerm.getSimpleFactorProduct().get(0);
        double newWeight = factor.getWeight() * weight;
        factor.setWeight(newWeight);
    }

    public static void test() {
        /**
         * test case N(x)N(x)U(x)N(y)(0.5)N(x-y)N(x-y)N(x-z)N(x-z)
         */
        SimpleProductTerm pTerm = new SimpleProductTerm();
        // pTerm.addFactor(new SimpleUniform("X", 0.5));
        // pTerm.addFactor(new SimpleUniform("X", 1.5));
        pTerm.addSimpleFactor(new ConstantValue(1.0));
        pTerm.addSimpleFactor(new ConstantValue(0.1));
    }

    public static void test1() throws NoSuchVariableException {
        /**
         * test case N(x)N(x)U(x)N(y)(1.0)(0.5)
         */
        ReduceSimpleProductTerm reduceSimpleProductTerm = new ReduceSimpleProductTerm();
        SimpleProductTerm pTerm = new SimpleProductTerm();
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "true", "false");
        nd.putValues("Y", "true", "false");
        SimpleGaussian x1 = new SimpleGaussian(1.0, nd.nodeIdx("X"));
        x1.setMeanAndVariance(1.0, 1.0);
        SimpleGaussian x2 = new SimpleGaussian(1.0, nd.nodeIdx("X"));
        x2.setMeanAndVariance(2.0, 2.0);

        pTerm.addSimpleFactor(x1);
        pTerm.addSimpleFactor(x2);
        SimpleGaussian y = new SimpleGaussian(1.0, nd.nodeIdx("Y"));
        y.setMeanAndVariance(2.0, 2.0);
        pTerm.addSimpleFactor(y);
        pTerm.addSimpleFactor(new ConstantValue(1.0));
        pTerm.addSimpleFactor(new ConstantValue(0.5));
        Logger.getRootLogger().info(
                popSameVariabledUnivariateFunction(pTerm));
        Logger.getRootLogger().info("remaining=" + pTerm);
        // ProductTerm result = (new ReduceSimpleProductTerm())
        // .reduceFactor(pTerm);
        // Logger.getRootLogger().info(result);

    }

    public static void main(String args[]) {
        try {
            test1();
        } catch (NoSuchVariableException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }

        // ProductTerm pTerm = new ProductTerm();
        // pTerm.addFactor(new SimpleUniform("X", 0.5));
        // pTerm.addFactor(new SimpleUniform("X", 1.5));
        // pTerm.addFactor(new ConstantValue(1.0));
        // pTerm.addFactor(new ConstantValue(0.1));
        //

    }
}
