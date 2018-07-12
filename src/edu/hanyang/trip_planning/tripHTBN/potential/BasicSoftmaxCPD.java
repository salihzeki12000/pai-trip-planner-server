package edu.hanyang.trip_planning.tripHTBN.potential;


import cntbn.common.NodeDictionary;
import cntbn.common.NodeUtil;
import edu.hanyang.trip_planning.tripHTBN.potential.util.ArrayKey;
import org.apache.log4j.Logger;
import wykwon.common.array.SortArrays;

import java.util.Arrays;

public class BasicSoftmaxCPD implements InterfaceSoftmaxCPD {
    private static Logger logger = Logger.getLogger(BasicSoftmaxCPD.class);


    private int theNodeIdx;
    private int theNodeCardinality;
    private int discreteParentIndices[];
    private int continuousParentIndex;
    private int discreteParentCardinalities[];
    private int totalDiscreteParentCardinality;
    private SoftmaxFunction potentials[];
    private ArrayKey parentKey;


    public BasicSoftmaxCPD(int theNodeIdx, int discreteParentIndices[], int continuouParentIdx) {
        this.theNodeIdx = theNodeIdx;
        this.discreteParentIndices = discreteParentIndices.clone();
        this.continuousParentIndex = continuouParentIdx;
        init();
    }

    public BasicSoftmaxCPD(String theNodeName, String discreteParentStr, String
            continuouParentNodeName) {
        String discreteParentNodeNames[] = discreteParentStr.split(",");
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        discreteParentIndices = NodeUtil.nodeIndices(discreteParentStr);
        continuousParentIndex = NodeDictionary.getInstance().nodeIdx(continuouParentNodeName);
        init();
    }


    public void init() {
        NodeDictionary nd = NodeDictionary.getInstance();
        theNodeCardinality = nd.cardinality(theNodeIdx);
        discreteParentCardinalities = new int[discreteParentIndices.length];
        totalDiscreteParentCardinality = 1;
        for (int i = 0; i < discreteParentIndices.length; i++) {
            int parentIdx = discreteParentCardinalities[i];
            int tmpCar = nd.cardinality(parentIdx);
            totalDiscreteParentCardinality *= tmpCar;
            discreteParentCardinalities[i] = tmpCar;
        }

        potentials = new SoftmaxFunction[totalDiscreteParentCardinality];
        parentKey = new ArrayKey(discreteParentCardinalities);
        logger.debug("cardinality = " + theNodeCardinality + "\t parentCardinalities=" + Arrays
                .toString(discreteParentCardinalities));
    }

    /**
     * parameter를 설정한다.
     *
     * @param argDiscreteParentNodes
     * @param argDiscreteParentValues
     * @param weights
     * @param biases
     */
    public void setPotentials(int argDiscreteParentNodes[], int argDiscreteParentValues[], double
            weights[], double biases[]) {

        SortArrays.sort(argDiscreteParentNodes, argDiscreteParentValues);
        if (!Arrays.equals(this.discreteParentIndices, argDiscreteParentNodes)) {
            throw new RuntimeException("parent Indices mismatch " + Arrays.toString(this.discreteParentIndices) + " " +
                    "and " + Arrays.toString(argDiscreteParentNodes));
        }
        int pKey = parentKey.key(argDiscreteParentValues);
        potentials[pKey] = new SoftmaxFunction(weights, biases);
    }

    @Override
    public double[] getDistribution(int[] argDiscreteParentIndices, int[] argDiscreteParentValues, double continuousParentValue) {

        SortArrays.sort(argDiscreteParentIndices, argDiscreteParentValues);
        if (!Arrays.equals(this.discreteParentIndices, argDiscreteParentIndices)) {
            throw new RuntimeException("parent Indices mismatch " + Arrays.toString(this.discreteParentIndices) + " " +
                    "and " + Arrays.toString(argDiscreteParentIndices));
        }
        int pKey = parentKey.key(argDiscreteParentValues);
        SoftmaxFunction func = potentials[pKey];
        return func.distribution(continuousParentValue);
    }

    public void setPotentials(String discreteParentNodeStr, String discreteParentValueStr, double
            weights[], double biases[]) {
        int discreteNodeIndices[] = NodeUtil.nodeIndices(discreteParentNodeStr);
        int discreteValueIndices[] = NodeUtil.valueIndices(discreteNodeIndices,
                discreteParentValueStr);
        logger.debug(Arrays.toString(discreteNodeIndices));
        setPotentials(discreteNodeIndices, discreteValueIndices, weights, biases);
    }

    @Override
    public double getProbability(int theValue, int[] discreteParentIndices, int[]
            discreteParentValues, double continuousParentValue) {
        double dist[] = getDistribution(discreteParentIndices, discreteParentValues, continuousParentValue);
        return dist[theValue];
    }

    @Override
    public double[] getDistributionFromSuperset(int[] argDiscreteParentIndices, int[]
            argDiscreteParentValues,
                                                double continuousParentValue) {
        int foundParentValues[] = new int[discreteParentIndices.length];
        int cnt = 0;
        for (int i = 0; i < argDiscreteParentIndices.length; i++) {
            int parentLocus = Arrays.binarySearch(discreteParentIndices,
                    argDiscreteParentIndices[i]);
            if (parentLocus >= 0) {
                foundParentValues[cnt] = argDiscreteParentValues[parentLocus];
                cnt++;
            }

        }
        return getDistribution(this.discreteParentIndices, foundParentValues, continuousParentValue);
    }

    public double[] getDistributionFromSuperset(String discreteParentNodeStr,
                                                String discreteParentValueStr,
                                                double continuousValue) {
        int discreteNodeIndices[] = NodeUtil.nodeIndices(discreteParentNodeStr);
        int discreteValueIndices[] = NodeUtil.valueIndices(discreteNodeIndices,
                discreteParentValueStr);
        return getDistributionFromSuperset(discreteNodeIndices, discreteValueIndices, continuousValue);
    }

    public double[] getDistribution(String discreteParentNodeStr, String
            discreteParentValueStr, double continuousValue) {
        int discreteNodeIndices[] = NodeUtil.nodeIndices(discreteParentNodeStr);
        int discreteValueIndices[] = NodeUtil.valueIndices(discreteNodeIndices,
                discreteParentValueStr);

        return getDistribution(discreteNodeIndices, discreteValueIndices, continuousValue);
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();


        return strbuf.toString();
    }

    public static void test() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putValues("X", "T", "F");
        nd.putValues("U", "T", "F");
        nd.putValues("V", "T", "F");
        nd.putValues("W", "T", "F");
        nd.putNode("C");

        BasicSoftmaxCPD basicCPD = new BasicSoftmaxCPD("X", "U,V", "C");
        double weight[] = {1, 2};
        double biases[] = {2, 3};
        basicCPD.setPotentials("U,V", "T,T", weight, biases);

        logger.debug(Arrays.toString(basicCPD.getDistribution("U,V", "T,T", 0.2)));
        logger.debug(Arrays.toString(basicCPD.getDistributionFromSuperset("V,W,U", "T,T,T", 0.2)));
    }

    public static void main(String[] args) {
        test();
    }

}
