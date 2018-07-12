package edu.hanyang.trip_planning.tripHTBN.potential;

import cntbn.common.NodeDictionary;
import cntbn.common.NodeUtil;

import edu.hanyang.trip_planning.tripHTBN.potential.util.ArrayKey;
import org.apache.log4j.Logger;
import wykwon.common.array.MyArrays;
import wykwon.common.array.SortArrays;

import java.util.Arrays;

public class IsolatedSoftmaxCPD implements UnivariateFunction {
    private static Logger logger = Logger.getLogger(IsolatedSoftmaxCPD.class);
    private int theNodeIdx;
    private int parentNodeIndices[];
    /**
     * weight의 2차원 배열
     */
    private double weights[][];
    private double biases[][];
    private int theNodeCardinality;
    private int parentCardinalities[];
    private int numCombinationOfParents;
    // child node의 배열에서의 순서
    private ArrayKey arrayKey;


    private int confChildValueIdx = -1;
    private int confParentValueIndices[] = null;

    public IsolatedSoftmaxCPD(int theNodeIdx, int... parentNodeIndices) {
        init(theNodeIdx, parentNodeIndices);

    }

    public IsolatedSoftmaxCPD(String theNodeName, String... parentNodeNames) {
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        this.parentNodeIndices = NodeUtil.nodeIndices(parentNodeNames);
        Arrays.sort(parentNodeNames);
        init(theNodeIdx, parentNodeIndices);
    }

    private void init(int theNodeIdx, int... parentNodeIndices) {
        this.theNodeIdx = theNodeIdx;
        this.parentNodeIndices = new int[parentNodeIndices.length];

        // check if nodeIndices are sorted
        int oldNodeIdx = Integer.MIN_VALUE;
        for (int i = 0; i < parentNodeIndices.length; i++) {
            this.parentNodeIndices[i] = parentNodeIndices[i];
            if (parentNodeIndices[i] <= oldNodeIdx) {
                throw new RuntimeException(" Node Sequences should be sorted by ascending: " + Arrays.toString(parentNodeIndices));
            }
            oldNodeIdx = parentNodeIndices[i];
        }

        // cardinality setting
        NodeDictionary nd = NodeDictionary.getInstance();
        parentCardinalities = new int[parentNodeIndices.length];
        theNodeCardinality = nd.cardinality(theNodeIdx);
        numCombinationOfParents = 1;
        for (int i = 0; i < parentNodeIndices.length; i++) {
            parentCardinalities[i] = nd.cardinality(parentNodeIndices[i]);
            numCombinationOfParents *= parentCardinalities[i];
        }

        arrayKey = new ArrayKey(parentCardinalities);
        weights = new double[theNodeCardinality][numCombinationOfParents];
        biases = new double[theNodeCardinality][numCombinationOfParents];

        logger.debug("init");
    }


    public void setPotential(String theNodeValueName, String parentNodeNames[], String parentValueNames[],
                             double weight, double bias) {
        int theValueIndex = NodeDictionary.getInstance().valueIdx(theNodeIdx, theNodeValueName);
        int parentNodeIndices[] = NodeUtil.nodeIndices(parentNodeNames);
        int parentValueIndices[] = NodeUtil.valueIndices(parentNodeIndices, parentValueNames);
        SortArrays.sort(parentNodeIndices, parentValueIndices);
        int parentKey = arrayKey.key(parentValueIndices);
        weights[theValueIndex][parentKey] = weight;
        biases[theValueIndex][parentKey] = bias;
    }

    public void setPotential(int theValueIndex, int parentNodeIndices[], int parentValueIndices[], double weight, double bias) {
        SortArrays.sort(parentNodeIndices, parentValueIndices);
        int parentKey = arrayKey.key(parentValueIndices);
        weights[theValueIndex][parentKey] = weight;
        biases[theValueIndex][parentKey] = bias;
    }


    /**
     * softmax probability를 반환한다.
     * <p/>
     * CPD에 정의된 Node index만 사용함
     * 나름 간략하게 찾을 수 있는 방법이라고 볼수 있음
     *
     * @param childValue      childNode의 value
     * @param parentValues    sort 되어 있는 parent value index 배열
     * @param continuousValue
     * @return
     */
    public double getSoftmaxProbability(int childValue, int parentValues[], double continuousValue) {
        if (parentValues.length != this.parentNodeIndices.length) {
            throw new RuntimeException("value size mismatch");
        }
        int parentKey = arrayKey.key(parentValues);
        double sum = 0.0;
        double theValue = 0.0;
        for (int c = 0; c < theNodeCardinality; c++) {
            double weight = weights[c][parentKey];
            double bias = biases[c][parentKey];
            double value = Math.exp(weight * continuousValue + bias);
            if (c == childValue) {
                theValue = value;
            }
            sum += value;
        }
        return theValue / sum;
    }

    public double getSoftmaxProbability(String childValue, String parentNodeNames[], String parentValueNames[], double continuousValue) {
        int theValueIndex = NodeDictionary.getInstance().valueIdx(theNodeIdx, childValue);
        int parentNodeIndices[] = NodeUtil.nodeIndices(parentNodeNames);
        int parentValueIndices[] = NodeUtil.valueIndices(parentNodeIndices, parentValueNames);
        SortArrays.sort(parentNodeIndices, parentValueIndices);
        return getSoftmaxProbability(theValueIndex, parentValueIndices, continuousValue);
    }


    /**
     * table요소 안에 SimpleMixture가 있는지 여부를 반환함
     *
     * @return simpleMixture가 하나라도 있으면 true, 모든 조합들이 SimpleFactor로만 구성되어 있으면
     *         false
     */

    public String toString() {
        NodeDictionary nd = NodeDictionary.getInstance();
        String theNodeName = nd.nodeName(theNodeIdx);

        StringBuffer strbuf = new StringBuffer();
        strbuf.append("BasicSoftmaxCPD  \n");

        for (int c = 0; c < theNodeCardinality; c++) {
            for (int p = 0; p < numCombinationOfParents; p++) {
                int parentValues[] = arrayKey.values(p);

                strbuf.append(theNodeName + "=" + nd.valueName(theNodeIdx, c) + ",");

                for (int j = 0; j < parentValues.length; j++) {
                    strbuf.append(nd.nodeName(parentNodeIndices[j]));
                    strbuf.append("=");
                    strbuf.append(nd.valueName(parentNodeIndices[j], parentValues[j]));
                    strbuf.append(",");
                }
                strbuf.deleteCharAt(strbuf.length() - 1);

                strbuf.append("\t\t");
                strbuf.append("w=" + weights[c][p] + "\tbias=" + biases[c][p]);
                strbuf.append("\n");
            }
        }


        return strbuf.toString();
    }


    private String[] nodeNames(int nodeIndices[]) {
        String nodeNames[] = new String[nodeIndices.length];
        NodeDictionary nd = NodeDictionary.getInstance();
        for (int i = 0; i < nodeIndices.length; i++) {
            nodeNames[i] = nd.nodeName(nodeIndices[i]);
        }
        return nodeNames;

    }

    public int getTheNodeIdx() {
        return theNodeIdx;
    }


    public void setConfiguration(String childValue, String parentNodeNames[], String parentValueNames[]) {
        this.confChildValueIdx = NodeDictionary.getInstance().valueIdx(theNodeIdx, childValue);
        int parentNodeIndices[] = NodeUtil.nodeIndices(parentNodeNames);
        int parentValueIndices[] = NodeUtil.valueIndices(parentNodeIndices, parentValueNames);
        SortArrays.sort(parentNodeIndices, parentValueIndices);
        this.confParentValueIndices = parentValueIndices;
    }

    public void setConfiguration(int childValue, int parentValues[]) {
        this.confChildValueIdx = childValue;
        this.confParentValueIndices = parentValues.clone();
    }

    @Override
    public double value(double x) {
        return getSoftmaxProbability(confChildValueIdx, confParentValueIndices, x);
    }

    @Override
    public boolean hasConfidenceInterval() {
        return false;
    }

    @Override
    public double lowerConfidenceInterval(double sigma_multiplier) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public double upperConfidenceInterval(double sigma_multiplier) {
        throw new RuntimeException("Not supported");
    }

    public static IsolatedSoftmaxCPD dummy() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("L1", "home", "office");
        nd.putValues("J2", "high", "mid", "low");
        nd.putValues("L2", "home", "office");

        IsolatedSoftmaxCPD softmaxCPD = new IsolatedSoftmaxCPD("J2", "L1");
        softmaxCPD.setPotential("high", MyArrays.makeString("L1"), MyArrays.makeString("home"), 0.1, 0.11);
        softmaxCPD.setPotential("mid", MyArrays.makeString("L1"), MyArrays.makeString("home"), 0.5, 0.0);
        softmaxCPD.setPotential("low", MyArrays.makeString("L1"), MyArrays.makeString("home"), 0.3, 0.33);

        softmaxCPD.setPotential("mid", MyArrays.makeString("L1"), MyArrays.makeString("office"), 0.2, 0.22);

        softmaxCPD.setPotential("high", MyArrays.makeString("L1"), MyArrays.makeString("office"), 0.4, 0.44);

        softmaxCPD.setPotential("low", MyArrays.makeString("L1"), MyArrays.makeString("office"), 0.6, 0.66);


//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("high,home"), 0.1, 0.11);
//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("high,office"), 0.2, 0.22);
//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("mid,home"), 0.3, 0.33);
//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("low,home"), 0.5, 0.55);
//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("low,office"), 0.6, 0.66);
//        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("mid,office"), 0.4, 0.44);
//        logger.debug(softmaxCPD);
//
//        softmaxCPD.getProbability(MyArrays.makeString("J2,L2,L1"), MyArrays.makeString("low,office,home"), 0.3);
        logger.debug(softmaxCPD);
        return softmaxCPD;
    }

    public static void main(String[] args) {
        IsolatedSoftmaxCPD softMax = dummy();
        logger.debug(softMax.getSoftmaxProbability("high", MyArrays.makeString("L1"), MyArrays.makeString("home"), 2));
        softMax.setConfiguration("high", MyArrays.makeString("L1"), MyArrays.makeString("home"));
        PlotFunction.plot("high", softMax, -20, 20, 0.1);

        softMax.setConfiguration("mid", MyArrays.makeString("L1"), MyArrays.makeString("home"));
        PlotFunction.plot("mid", softMax, -20, 20, 0.1);

        softMax.setConfiguration("low", MyArrays.makeString("L1"), MyArrays.makeString("home"));
        PlotFunction.plot("low", softMax, -20, 20, 0.1);

    }


}
