package edu.hanyang.trip_planning.tripHTBN.potential;

import cntbn.common.NodeDictionary;
import cntbn.common.NodeUtil;

import cntbn.terms_factors.ConditionalLinearGaussian;
import cntbn.terms_factors.ContinuousFactor;
import util.ArrayKey;
import org.apache.log4j.Logger;
import util.SortArrays;

import java.util.Arrays;

/**
 * Created by wykwon on 2015-09-25.
 */
public class BasicHybridCPD implements InterfaceHybridCPD {

    private int theNodeIdx;
    private int discreteParentIndices[];
    private int continuousParentIndices[];
    private int parentCardinalities[];
    private int totalParentCardinality;
    private static Logger logger = Logger.getLogger(BasicHybridCPD.class);
    /**
     * cpt element들임.  처번째 indices는 parentNode에 의한 key들,  두번째  indices는 theNodeValue,
     */
    private ContinuousFactor factors[];
    private ArrayKey parentKey;

    /**
     * constructor if the node only have discrete parents
     *
     * @param theNodeName
     * @param discreteParentNames
     */
    public BasicHybridCPD(String theNodeName, String discreteParentNames[]) {
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        discreteParentIndices = new int[discreteParentNames.length];
        for (int i = 0; i < discreteParentNames.length; i++) {
            discreteParentIndices[i] = NodeDictionary.getInstance().nodeIdx(discreteParentNames[i]);
        }
        init();
    }

    private void init() {
        NodeDictionary nd = NodeDictionary.getInstance();
//        logger.debug("cardinaliry=" + nd.cardinality("X1"));;
        parentCardinalities = new int[discreteParentIndices.length];
        totalParentCardinality = 1;
        for (int i = 0; i < discreteParentIndices.length; i++) {
            int parentIdx = discreteParentIndices[i];
            if (NodeDictionary.getInstance().nodeName(theNodeIdx).equals("M1")) {
                if (NodeDictionary.getInstance().nodeName(theNodeIdx).equals("x1")) {
                    int x = nd.cardinality(parentIdx);
                    logger.debug("Cardinality= " + nd.cardinality(parentIdx));
                }
            }
            int tmpCar = nd.cardinality(parentIdx);
            totalParentCardinality *= tmpCar;
            parentCardinalities[i] = tmpCar;
        }
        factors = new ContinuousFactor[totalParentCardinality];
        parentKey = new ArrayKey(parentCardinalities);
//        logger.debug("parentCardinalities=" + Arrays.toString(parentCardinalities));
    }

    /**
     * distributions setting
     *
     * @param discreteParentIndices discreteParent들의 indice - sort되니까 변동되어야 함
     * @param discreteParentValues
     * @param
     */
    public void setDistribution(int[] discreteParentIndices, int[] discreteParentValues, ContinuousFactor factor) {
        SortArrays.sort(discreteParentIndices, discreteParentValues);

        if (!Arrays.equals(this.discreteParentIndices, discreteParentIndices)) {
            throw new RuntimeException("parent Indices mismatch " + Arrays.toString(this.discreteParentIndices) + " and " + Arrays.toString(discreteParentIndices));
        }
        int pKey = parentKey.key(discreteParentValues);

        factors[pKey] = factor.deepCopy();
    }

    @Override
    public ContinuousFactor getDistribution(int[] discreteParentIndices, int[] discreteParentValues) {
        if (!Arrays.equals(this.discreteParentIndices, discreteParentIndices)) {
            throw new RuntimeException("parent Indices mismatch " + Arrays.toString(this.discreteParentIndices) + " and " + Arrays.toString(discreteParentIndices)
                    + "\t" + Arrays.toString(NodeUtil.nodeNames(this.discreteParentIndices)) + "!=" +
                    Arrays.toString(NodeUtil.nodeNames(discreteParentIndices))
            );
        }
        int pKey = -1;
        try {
            pKey = parentKey.key(discreteParentValues);
        } catch (RuntimeException e) {
            logger.fatal("error in the node " + NodeDictionary.getInstance().nodeName(theNodeIdx));
            e.printStackTrace();
        }

        return factors[pKey];
    }

    @Override
    public ContinuousFactor getDistributionFromSuperset(int argDiscreteParentIndices[], int
            argDiscreteParentValues[]) {
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
        return getDistribution(this.discreteParentIndices, foundParentValues);
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        NodeDictionary nd = NodeDictionary.getInstance();
        strbuf.append("CPT of " + nd.nodeName(theNodeIdx) + "\n");

        for (int i = 0; i < totalParentCardinality; i++) {
            strbuf.append("P(" + nd.nodeName(theNodeIdx) + "|");
            int discreteParentValues[] = parentKey.values(i);

            for (int k = 0; k < discreteParentValues.length; k++) {
                String parentNodeName = nd.nodeName(discreteParentIndices[k]);
                String parentValueName = nd.valueName(discreteParentIndices[k], discreteParentValues[k]);
                strbuf.append(parentNodeName + "=" + parentValueName + ",");
            }

            if (continuousParentIndices != null) {
                for (int k = 0; k < continuousParentIndices.length; k++) {
                    String parentNodeName = nd.nodeName(continuousParentIndices[k]);
                    strbuf.append(parentNodeName + ",");
                }
            }

            strbuf.deleteCharAt(strbuf.length() - 1);
            strbuf.append(")=" + factors[i] + "\n");
        }

        return strbuf.toString();
    }
}
