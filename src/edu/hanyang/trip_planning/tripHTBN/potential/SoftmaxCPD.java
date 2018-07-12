package edu.hanyang.trip_planning.tripHTBN.potential;

import cntbn.common.NodeDictionary;
import cntbn.common.NodeUtil;
import edu.hanyang.trip_planning.tripHTBN.potential.util.ArrayKey;
import javolution.util.FastSet;
import org.apache.log4j.Logger;
import wykwon.common.array.MyArrays;
import wykwon.common.array.SortArrays;

import java.util.Arrays;
import java.util.Set;

public class SoftmaxCPD {
    private static Logger logger = Logger.getLogger(SoftmaxCPD.class);
    private int nodeIndices[];
    private Set<Integer> nodeIndexSet;
    private double weights[];
    private double biases[];
    private int cardinalities[];
    private int totalCardinality;
    private int theNodeIdx;
    // child node의 배열에서의 순서

    private ArrayKey arrayKey;

    /**
     * CPD생성자
     *
     * @param nodeIndices node들의 배열임.
     *                    <p/>
     *                    node indices는 내림차순으로 정렬되어 있어야함.
     */
    public SoftmaxCPD(int theNodeIdx, int... nodeIndices) {
        this.theNodeIdx = theNodeIdx;
        this.nodeIndices = nodeIndices.clone();
        Arrays.sort(this.nodeIndices);
        nodeIndexSet = new FastSet<Integer>();
        for (int i = 0; i < nodeIndices.length; i++) {
            nodeIndexSet.add(nodeIndices[i]);
        }
        cardinalities = new int[nodeIndices.length];
        initCardinality();
        arrayKey = new ArrayKey(cardinalities);
        weights = new double[totalCardinality];
        biases = new double[totalCardinality];

    }

    public SoftmaxCPD(String theNodeName, String... nodeNames) {
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        this.nodeIndices = new int[nodeNames.length];
        for (int i = 0; i < this.nodeIndices.length; i++) {
            this.nodeIndices[i] = NodeDictionary.getInstance().nodeIdx(
                    nodeNames[i]);
        }
        nodeIndexSet = new FastSet<Integer>();
        for (int i = 0; i < nodeIndices.length; i++) {
            nodeIndexSet.add(nodeIndices[i]);
        }

        Arrays.sort(this.nodeIndices);
        cardinalities = new int[nodeIndices.length];
        initCardinality();
        arrayKey = new ArrayKey(cardinalities);
        weights = new double[totalCardinality];
        biases = new double[totalCardinality];
    }


//
//    public BasicSoftmaxCPD deepCopy() {
//        BasicSoftmaxCPD newCPT = new BasicSoftmaxCPD(theNodeIdx, nodeIndices);
//        for (int i = 0; i < weights.length; i++) {
//            newCPT.weights = weights.clone();
//        }
//        return newCPT;
//    }

    private void init() {

    }

    public void setPotential(String nodeNames[], String valuenames[],
                             double weight, double bias) {
        int tmpNodeIndices[] = NodeUtil.nodeIndices(nodeNames);
        int tmpValueIndices[] = NodeUtil.valueIndices(tmpNodeIndices, valuenames);
        SortArrays.sort(tmpNodeIndices, tmpValueIndices);
        int key = arrayKey.key(tmpValueIndices);
        weights[key] = weight;
        biases[key] = bias;
    }

    public void setPotential(int rvIndices[], int valueIndices[], double weight, double bias) {
        int rvIndicesCopy[] = rvIndices.clone();
        int valueIndicesCopy[] = valueIndices.clone();
        SortArrays.sort(rvIndicesCopy, valueIndicesCopy);
        int key = arrayKey.key(valueIndicesCopy);
        weights[key] = weight;
        biases[key] = bias;
    }

    public int size() {
        return totalCardinality;
    }

    public double getWeight(int key) {
        return weights[key];
    }

    public double getBias(int key) {
        return biases[key];
    }
//
//    public double getPotential(String nodeNames[], String valuenames[]) {
//        int tmpNodeIndices[] = NodeUtil.nodeIndices(nodeNames);
//        int tmpValueIndices[] = NodeUtil.valueIndices(tmpNodeIndices, valuenames);
//        SortArrays.sort(tmpNodeIndices, tmpValueIndices);
//        return factors[indices2Key(tmpValueIndices)];
//    }
//
//    public double getPotential(int nodeIndices[], int valueIndices[]) {
//        SortArrays.sort(nodeIndices, valueIndices);
//        if (!Arrays.equals(nodeIndices, this.nodeIndices)) {
//            throw new RuntimeException("Node indices do not match");
//        }
//        return factors[indices2Key(valueIndices)];
//    }

    public int[] getNodeIndices() {
        return nodeIndices;
    }


    public Set<Integer> getNodeIndexSet() {

        return nodeIndexSet;
    }

    private void initCardinality() {
        NodeDictionary nd = NodeDictionary.getInstance();
        totalCardinality = 1;
        for (int i = 0; i < nodeIndices.length; i++) {
            cardinalities[i] = nd.cardinality(nodeIndices[i]);
            totalCardinality *= cardinalities[i];
        }
    }

    /**
     * //     * value Indices들을 key로 변환함
     * //     *
     * //     * @param valueIndices
     * //     * @return
     * //
     */
//    public int indices2Key(int... valueIndices) {
//        /**
//         * Big endian을 따름. valueIndices[0]이 가장 앞 자리임
//         *
//         */
//        int key = valueIndices[0];
//        int digits = 1;
//        for (int i = 0; i < valueIndices.length - 1; i++) {
//            digits *= cardinalities[i];
//            key += valueIndices[i + 1] * digits;
//        }
//        return key;
//    }
//
//    public int[] key2Indices(int key) {
//        /**
//         * valueIndices[0]이 가장 앞 자리임
//         *
//         */
//        int valueIndices[] = new int[cardinalities.length];
//        for (int i = 0; i < cardinalities.length; i++) {
//            int divisior = key / cardinalities[i];
//            int remains = key % cardinalities[i];
//            valueIndices[i] = remains;
//            key = divisior;
//        }
//        return valueIndices;
//    }
    private int[] makeValueIndices(String nodeNames[], String valueNames[]) {

        if (nodeNames.length != valueNames.length) {
            throw new RuntimeException("Size mismatch");
        }
        int tmpNodeIndices[] = NodeUtil.nodeIndices(nodeNames);
        int valueIndices[] = new int[nodeNames.length];

        for (int i = 0; i < nodeNames.length; i++) {
            tmpNodeIndices[i] = NodeDictionary.getInstance().nodeIdx(
                    nodeNames[i]);
            valueIndices[i] = NodeDictionary.getInstance().valueIdx(
                    nodeNames[i], valueNames[i]);
        }
        logger.debug("input nodeNames = " + Arrays.toString(nodeNames));
        logger.debug("input valueNames = " + Arrays.toString(valueNames));
        logger.debug("input nodeIndices= " + Arrays.toString(tmpNodeIndices));
        logger.debug("input valueIndices = " + Arrays.toString(valueIndices));


        SortArrays.sort(tmpNodeIndices, valueIndices);
        if (!Arrays.equals(nodeIndices, this.nodeIndices)) {
            logger.fatal(Arrays.toString(nodeIndices));
            logger.fatal(Arrays.toString(this.nodeIndices));
            throw new RuntimeException("NodeNames does not match:"
                    + Arrays.toString(nodeNames) + " values="
                    + Arrays.toString(valueNames));

        }
        return valueIndices;
    }

    public double getProbability(String nodeNames[], String valuenames[], double continuousValue) {
        int tmpNodeIndices[] = NodeUtil.nodeIndices(nodeNames);
        int tmpValueIndices[] = NodeUtil.valueIndices(tmpNodeIndices, valuenames);
        SortArrays.sort(tmpNodeIndices, tmpValueIndices);
        return getProbability(tmpNodeIndices, tmpValueIndices, continuousValue);
    }

    /**
     * value sequence 에 해당하는 확률값을 반환해줌 valueSeqience 는 table에서 필요한 R.V. 보다 더 많을
     * 수 있다.
     */
    /**
     * probability를 반환한다.
     * CPD에 정의된 Node index 보다 많은 node들을 사용함
     *
     * @param queryNodes
     * @param queryValues
     * @param continuousValue
     * @return
     */
    public double getProbability(int[] queryNodes, int[] queryValues, double continuousValue) {
        int theNodeOrder = Arrays.binarySearch(nodeIndices, theNodeIdx);
        logger.debug("theNodeOrder=" + theNodeOrder);
        int targetValueIndices[] = new int[nodeIndices.length];

        for (int i = 0; i < queryNodes.length; i++) {
            // 1. queryNodes[i]에 해당하는 Node의 index를 찾는다.
            int key = queryNodes[i];
            int matchedNodeIdx = Arrays.binarySearch(nodeIndices, key);
            if (matchedNodeIdx >= 0) {
                targetValueIndices[matchedNodeIdx] = queryValues[i];
            }
        }
        logger.debug("weight= " + weights[arrayKey.key(targetValueIndices)]);
//        return ;
        return 0.0;
    }


    /**
     * softmax probability를 반환한다.
     *
     * CPD에 정의된 Node index만 사용함
     * 나름 간략하게 찾을 수 있는 방법이라고 볼수 있음
     *
     * @param childValue      childNode의 value
     * @param parentValues    parent value index 배열
     * @param continuousValue
     * @return
     */
//    public double getSoftmaxProbability(int childValue, int parentValues[], double continuousValue) {
//
//    }

    /**
     * table요소 안에 SimpleMixture가 있는지 여부를 반환함
     *
     * @return simpleMixture가 하나라도 있으면 true, 모든 조합들이 SimpleFactor로만 구성되어 있으면
     *         false
     */

    public String toString() {
        NodeDictionary dict = NodeDictionary.getInstance();


        StringBuffer strbuf = new StringBuffer();
        strbuf.append("BasicSoftmaxCPD  \n");

        strbuf.append("NodeNames: " + Arrays.toString(nodeNames(nodeIndices)) + "\n");
        for (int i = 0; i < totalCardinality; i++) {
            int values[] = arrayKey.values(i);
            strbuf.append("[");
            for (int j = 0; j < values.length; j++) {
                strbuf.append(dict.nodeName(nodeIndices[j]));
                strbuf.append("=");
                strbuf.append(dict.valueName(nodeIndices[j], values[j]));
                strbuf.append(",");
            }
            strbuf.deleteCharAt(strbuf.length() - 1);
            strbuf.append("]");
            strbuf.append("\t\t");
            strbuf.append("w=" + weights[i] + "\tbias=" + biases[i]);
            strbuf.append("\n");
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


    public static SoftmaxCPD dummy() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("L1", "home", "office");
        nd.putValues("J2", "high", "mid", "low");
        nd.putValues("L2", "home", "office");

        SoftmaxCPD softmaxCPD = new SoftmaxCPD("J2", "J2", "L1");
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("high,home"), 0.1, 0.11);
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("high,office"), 0.2, 0.22);
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("mid,home"), 0.3, 0.33);
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("low,home"), 0.5, 0.55);
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("low,office"), 0.6, 0.66);
        softmaxCPD.setPotential(MyArrays.makeString("J2,L1"), MyArrays.makeString("mid,office"), 0.4, 0.44);
        logger.debug(softmaxCPD);

        softmaxCPD.getProbability(MyArrays.makeString("J2,L2,L1"), MyArrays.makeString("low,office,home"), 0.3);
        return null;
    }

    public static void main(String[] args) {
        dummy();
    }
}
