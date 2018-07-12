package cntbn.terms_factors.tools;

import cntbn.common.NodeDictionary;
import cntbn.common.NodeUtil;
import cntbn.terms_factors.SimpleFactor;
import cntbn.terms_factors.SimpleGaussian;
import cntbn.terms_factors.SimpleProductTerm;
import cntbn.terms_factors.TDGaussian;
import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TermUtils {
    private static Logger logger = Logger.getLogger("TermUtils");

    /**
     * SimpleProductTerm을 ConditionalLinearGaussian과 ArrayList<GaussianFunction>으로
     * 분할함. SimpleGaussian의 weight는 모두 CLG에 반영되며, SimpleGaussian의 weight는 1로
     * 설정된다. 인자인 simpleProductTerm 내에는 단 한개의 TD Gaussian만 존재해야함.
     */

    public static SimpleAndConditionalGaussian spiltToSimpleAndConditionalGaussian(
            SimpleProductTerm productTerm) {
        SimpleAndConditionalGaussian result = new SimpleAndConditionalGaussian();
        Iterator<SimpleFactor> it = productTerm.getSimpleFactorProduct()
                .iterator();
        int tdCnt = 0;
        double mulOfWeight = 1.0;
        while (it.hasNext()) {
            SimpleFactor simpleFactor = it.next();
            if (simpleFactor instanceof TDGaussian) {
                TDGaussian tdg = (TDGaussian) simpleFactor;
                mulOfWeight *= tdg.getWeight();
                tdg.setWeight(1.0);
                result.addClg(tdg);
                tdCnt++;
                if (tdCnt > 1) {
                    throw new RuntimeException(
                            "There are multiple td Gaussians in" + productTerm);
                }
            } else if (simpleFactor instanceof SimpleGaussian) {
                SimpleGaussian sg = (SimpleGaussian) simpleFactor;
                mulOfWeight *= sg.getWeight();
                sg.setWeight(1.0);
                result.addSimpleGaussian(sg);
            } else {
                throw new RuntimeException(
                        "SimpleGaussian도 아니고,CLG도 아니고 넌 뭔데 여기있냐? "
                                + simpleFactor + " in " + productTerm);
            }
        }
        result.getCLG().setWeight(mulOfWeight);
        return result;
    }

    /**
     * CLG의 인자와 관계없는 SimpleGaussian을 없앤다. 반환값은 없으며, 인자를 변형시킨다.
     *
     * @param simpleAndConditionalGaussian
     * @param integralVariables
     */
    public static void isolatedIntegral(
            SimpleAndConditionalGaussian simpleAndConditionalGaussian,
            Set<Integer> integralVariables) {
        Set<Integer> variablesInCLG = simpleAndConditionalGaussian.getCLG()
                .getParameters();
        Set<Integer> variablesToBeRemoved = SetTools.subtract(
                integralVariables, variablesInCLG);
        logger.debug("integralVariables="
                + NodeUtil.nodeSetToString(integralVariables));
        logger.debug("variablesInCLG="
                + NodeUtil.nodeSetToString(variablesInCLG));
        logger.debug("variablesToBeRemoved="
                + NodeUtil.nodeSetToString(variablesToBeRemoved));
        Iterator<SimpleGaussian> simpleListIt = simpleAndConditionalGaussian
                .getSimpleGaussianList().iterator();
        while (simpleListIt.hasNext()) {
            SimpleGaussian sg = simpleListIt.next();
            if (variablesToBeRemoved.contains(sg.getArgument())) {
                logger.debug("this will be removed " + sg);
                simpleListIt.remove();
            }
        }
    }

    public static void isolatedIntegral(List<SimpleGaussian> gaussianList,
                                        Set<Integer> integralVariables) {
    }

    public static void nodeInit() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "true", "false");
        nd.putValues("Y", "true", "false");
        nd.putValues("W", "true", "false");
        nd.putValues("V", "true", "false");

    }

    public static void test_isolatedIntegral() {
        NodeDictionary nd = NodeDictionary.getInstance();
        Set<Integer> integralVariables = new FastSet<Integer>();
        integralVariables.add(nd.nodeIdx("X"));
        integralVariables.add(nd.nodeIdx("V"));
        integralVariables.add(nd.nodeIdx("W"));

        SimpleGaussian sgX = new SimpleGaussian(2.0, "X");
        SimpleGaussian sgV = new SimpleGaussian(2.0, "V");
        SimpleGaussian sgW = new SimpleGaussian(2.0, "W");
        TDGaussian tdXU = new TDGaussian(1.1, "X", "U");
        SimpleProductTerm spt = new SimpleProductTerm();
        spt.addSimpleFactor(sgX);
        spt.addSimpleFactor(sgV);
        spt.addSimpleFactor(sgW);
        spt.addSimpleFactor(tdXU);

        SimpleAndConditionalGaussian simpleAndConditionalGaussian = spiltToSimpleAndConditionalGaussian(spt);

        isolatedIntegral(simpleAndConditionalGaussian, integralVariables);
        logger.info(simpleAndConditionalGaussian);
    }

    public static void test_spiltToSimpleAndConditionalGaussian() {

        SimpleGaussian sgX = new SimpleGaussian(2.0, "X");
        TDGaussian tdXU = new TDGaussian(1.1, "X", "U");
        SimpleProductTerm spt = new SimpleProductTerm();
        spt.addSimpleFactor(sgX);
        spt.addSimpleFactor(tdXU);
        SimpleAndConditionalGaussian scg = TermUtils
                .spiltToSimpleAndConditionalGaussian(spt);
        logger.info("test_spiltToSimpleAndConditionalGaussian=" + scg);
    }

    public static void main(String args[]) {
        nodeInit();
        test_spiltToSimpleAndConditionalGaussian();
        test_isolatedIntegral();
    }
}
