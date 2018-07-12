package cntbn.terms_factors;

import cntbn.common.NodeDictionary;
import org.apache.commons.math3.util.Pair;
import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 19
 * Time: 오후 2:01
 * To change this template use File | Settings | File Templates.
 */
public class GaussianProductIntegral {
    private static Logger logger = Logger
            .getLogger(GaussianProductIntegral.class);


    public static GaussianFunction productIntegral(TDGaussian tdg, SimpleGaussian simple, int integralVariableIdx) {
        if (integralVariableIdx != simple.getArgument()) {
            throw new RuntimeException("argiment mismatch with " + tdg + " and " + simple);
        } else if (simple.getArgument() == tdg.getArgument()) {
            logger.debug("case 1: product : " + tdg + " and " + simple);
            double newWeight = tdg.getWeight() * simple.getWeight();
            double newMean = simple.getMean() - tdg.getMean();
            double newVar = simple.getVariance() + tdg.getVariance();

            SimpleGaussian newSg = new SimpleGaussian(newWeight, integralVariableIdx);
            newSg.setMeanAndVariance(newMean, newVar);
            return newSg;
        } else if (simple.getArgument() == tdg.getConditionalVariableIdx()) {
            logger.debug("case 2 convolution:" + tdg + " and " + simple);
            double newWeight = tdg.getWeight() * simple.getWeight();
            double newMean = simple.getMean() + tdg.getMean();
            double newVar = simple.getVariance() + tdg.getVariance();

            SimpleGaussian newSg = new SimpleGaussian(newWeight, integralVariableIdx);
            newSg.setMeanAndVariance(newMean, newVar);
            return newSg;
        } else {
            throw new RuntimeException("argiment mismatch with " + tdg + " and " + simple);
        }

    }

    /**
     * 두 Gaussian 함수를 곱하고 적분한 결과를 얻는다. (
     * convolution과 autocorrelation의 일반적인 형태로 볼 수 있다.
     *
     * @param clg
     * @param simple
     * @param integralVariableIdx
     * @param outputVariableIdx
     * @return
     */
    public static GaussianFunction productIntegral(ConditionalLinearGaussian clg, SimpleGaussian simple, int integralVariableIdx, int outputVariableIdx) {
        if (simple.getArgument() != integralVariableIdx) {
            throw new RuntimeException(simple
                    + " does not have "
                    + NodeDictionary.getInstance()
                    .nodeName(integralVariableIdx));
        } else if (simple.getArgument() == clg.getArgument()) {
            logger.debug("product Integral of " + clg + " and " + simple);
            return productIntegralWithSameArgument(clg, simple, outputVariableIdx);
        } else {
            // clg의 argument가 integralVariableIdx가 되도록 할것
            clg.relocateParameters(integralVariableIdx);
            logger.debug("rellocated clg=" + clg);
            return productIntegralWithSameArgument(clg, simple, outputVariableIdx);
        }
    }

    /**
     * CLG와 SimpleGaussian의 product integral을 구하는데 두 함수의 인자가 적분변수와 같을때 사용된다.
     * <p/>
     * 예 Int_X N(X;,)N(X;Y1+Y2,)dX 정도?
     *
     * @return
     */
    private static GaussianFunction productIntegralWithSameArgument(ConditionalLinearGaussian clg, SimpleGaussian simple, int outputVariableIdx) {
        // 새로 만들 CLG의 argument 는 outputVariableIdx 라고
        int newArgumentIdx = outputVariableIdx;

        // weight는 outputVariable의 conditionalWeight;
        int condVariableIndices[] = clg.getConditionalVariableIndices();
        double condWeights[] = clg.getConditionalWeights();
        double theCondWeight = condWeights[searchIndexByValue(condVariableIndices, outputVariableIdx)];
        logger.debug("theConditionalWeight=" + theCondWeight);
        double newWeight = Math.abs(clg.getWeight() * theCondWeight);

        // 적분되서 conditional variable갯수 하나 줄어듬
        double newMean = (simple.getMean() - clg.getMean()) / theCondWeight;
        double newVar = Math.abs((simple.getVariance() + clg.getVariance()) / theCondWeight);
        logger.debug("simpleVar=" + simple.getVariance());
        logger.debug("clgVar=" + clg.getVariance());
        logger.debug("newMean=" + newMean);
        logger.debug("newVar=" + newVar);
        Pair<int[], double[]> newCondPair = makeNewConditionalVariables(condVariableIndices, condWeights, outputVariableIdx);

        logger.debug("new cond Pair = " + Arrays.toString(newCondPair.getFirst()) + "\n" + Arrays.toString(newCondPair.getSecond()));
        int newConditionalIndices[] = newCondPair.getFirst();
        double newConditionalWeights[] = innerproduct(newCondPair.getSecond(), -1.0 / theCondWeight);


        ConditionalLinearGaussian newCLG = new ConditionalLinearGaussian(newWeight, newArgumentIdx, newConditionalIndices, newConditionalWeights, newMean, newVar);
        logger.debug("newClg=" + newCLG);
        return newCLG;


    }

    private static Pair<int[], double[]> makeNewConditionalVariables(int fullVariableIndices[], double fullWeights[], int theIndices) {

        if (fullVariableIndices.length != fullWeights.length) {
            throw new RuntimeException("size mismatch!");
        }
        int newIdx = 0;
        // 하나씩 크기를 줄여야 한다.
        int retIndices[] = new int[fullVariableIndices.length - 1];
        double retWeights[] = new double[fullWeights.length - 1];
        for (int i = 0; i < fullVariableIndices.length; i++) {
            int index = fullVariableIndices[i];
            if (index != theIndices) {
                logger.debug("add new cond variable " + fullVariableIndices[i] + "=" + NodeDictionary.getInstance().nodeName(fullVariableIndices[i]));
                retIndices[newIdx] = fullVariableIndices[i];
                retWeights[newIdx] = fullWeights[i];
                newIdx++;
            }
        }
        return new Pair<int[], double[]>(retIndices, retWeights);
    }

    private static double[] innerproduct(double array[], double multiplier) {
        double retarray[] = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            retarray[i] = array[i] * multiplier;
        }
        return retarray;
    }

    public static GaussianFunction convolution(ConditionalLinearGaussian clg, SimpleGaussian simple, int integralVariableIdx) {
        if (simple.getArgument() != integralVariableIdx) {
            throw new RuntimeException(simple
                    + " does not have "
                    + NodeDictionary.getInstance()
                    .nodeName(integralVariableIdx));
        } else if (simple.getArgument() == clg.getArgument()) {

            logger.debug("DDDFsdafasfasd");
            // autocorellation: case Int N(x;;)N(x;y+z;)
            double newWeight = simple.getWeight() * clg.getWeight();
            double newMean = simple.getMean() - clg.getMean();
            double newVariance = simple.getVariance() + clg.getVariance();
            logger.debug("convolution of " + clg + " and " + simple);
//            GaussianFunction newG = new GaussianFunction(newWeight,
//                    clg.get());
//            newG.setMeanAndVariance(newMean, newVariance);
//            return newG;
            return null;
        } else if (contain(clg.getConditionalVariableIndices(), simple.getArgument())) {
            // case Int N(x;;)N(y;x+z;)
            double newWeight = simple.getWeight() * clg.getWeight();
            double newMean = simple.getMean() + clg.getMean();
            double newVariance = simple.getVariance() + clg.getVariance();


            SimpleGaussian newG = new SimpleGaussian(newWeight,
                    clg.getArgument());
            newG.setMeanAndVariance(newMean, newVariance);
            return newG;
        }
        throw new RuntimeException(clg + " does not have "
                + NodeDictionary.getInstance().nodeName(integralVariableIdx));
    }

    public static GaussianFunction convolution(TDGaussian tdg, SimpleGaussian simple, int integralVariableIdx) {
        if (simple.getArgument() != integralVariableIdx) {
            throw new RuntimeException(simple
                    + " does not have "
                    + NodeDictionary.getInstance()
                    .nodeName(integralVariableIdx));
        } else if (simple.getArgument() == tdg.getArgument()) {

            // autocorellation: case Int N(x;;)N(x;y;)
            double newWeight = simple.getWeight() * tdg.getWeight();
            double newMean = simple.getMean() - tdg.getMean();
            double newVariance = simple.getVariance() + tdg.getVariance();
            SimpleGaussian newG = new SimpleGaussian(newWeight,
                    tdg.getConditionalVariableIdx());
            newG.setMeanAndVariance(newMean, newVariance);
            return newG;
        } else if (simple.getArgument() == tdg.getConditionalVariableIdx()) {
            // case Int N(x;;)N(y;x;)
            double newWeight = simple.getWeight() * tdg.getWeight();
            double newMean = simple.getMean() + tdg.getMean();
            double newVariance = simple.getVariance() + tdg.getVariance();
            SimpleGaussian newG = new SimpleGaussian(newWeight,
                    tdg.getArgument());
            newG.setMeanAndVariance(newMean, newVariance);
            return newG;
        }
        throw new RuntimeException(tdg + " does not have "
                + NodeDictionary.getInstance().nodeName(integralVariableIdx));
    }

    private static boolean contain(int array[], int arg) {
        for (int v : array) {
            if (v == arg) {
                return true;
            }
        }
        return false;
    }

    private static int searchIndexByValue(int array[], int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        throw new RuntimeException("Array " + Arrays.toString(array) + " does not contain " + value);
    }

    public static void testContolution() {
        ConditionalLinearGaussian clg = ConditionalLinearGaussian.dummy1();
        NodeDictionary nd = NodeDictionary.getInstance();
        SimpleGaussian sg = SimpleGaussian.dummy("X");
        logger.debug(clg);
        logger.debug(sg);

        convolution(clg, sg, nd.nodeIdx("X"));
    }

    public static void testProductIntegral1() {
        /**
         * N(X;10+0.5Y1+1Y2+1.5Y3, 1) 과  N(X;1.5,2.5) 을 구해보자.
         *
         *
         */
        ConditionalLinearGaussian clg = ConditionalLinearGaussian.dummy1();
        NodeDictionary nd = NodeDictionary.getInstance();
        SimpleGaussian sg = SimpleGaussian.dummy("Y");
        logger.debug(clg);
        logger.debug(sg);

        clg.relocateParameters(nd.nodeIdx("Y"));
        logger.debug("111  " + clg);

        clg.relocateParameters(nd.nodeIdx("W"));
        logger.debug("222  " + clg);

        clg.relocateParameters(nd.nodeIdx("X"));
        logger.debug("XXX  " + clg);

        GaussianFunction result = productIntegral(clg, sg, nd.nodeIdx("Y"), nd.nodeIdx("W"));

        logger.debug("result=" + result);
        ConditionalLinearGaussian r = (ConditionalLinearGaussian) result;

        for (int nodeIdx : r.getConditionalVariableIndices()) {
            logger.debug(nd.nodeName(nodeIdx));
        }

    }

    public static void testProductIntegral2() {
        /**
         * N(X;10+0.5Y1+1Y2+1.5Y3, 1) 과  N(X;1.5,2.5) 을 구해보자.
         *
         *
         */
        NodeDictionary.getInstance().putValues("X", "true");
        NodeDictionary.getInstance().putValues("Y", "true");
        TDGaussian tdg = new TDGaussian(1.0, "Y", "X");
        tdg.setMeanAndVariance(1.0, 2.0);
        logger.debug("tdg=" + tdg);
        SimpleGaussian sg = SimpleGaussian.dummy("X");

        logger.debug(productIntegral(tdg, sg, NodeDictionary.getInstance().nodeIdx("X")));
    }

    public static void main(String[] args) {
        testProductIntegral2();

    }


}
