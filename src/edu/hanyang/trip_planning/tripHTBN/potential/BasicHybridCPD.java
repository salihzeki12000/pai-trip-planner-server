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

    public BasicHybridCPD(int theNodeIdx, int discreteParentIndices[], int continuousParentIndices[]) {
        this.theNodeIdx = theNodeIdx;
        this.discreteParentIndices = discreteParentIndices.clone();
        this.continuousParentIndices = continuousParentIndices.clone();
        init();
    }

    public BasicHybridCPD(String theNodeName, String discreteParentNames[], String continuousParentNames[]) {
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        discreteParentIndices = new int[discreteParentNames.length];
        continuousParentIndices = new int[continuousParentNames.length];
        for (int i = 0; i < discreteParentNames.length; i++) {
            discreteParentIndices[i] = NodeDictionary.getInstance().nodeIdx(discreteParentNames[i]);
        }
        for (int i = 0; i < continuousParentNames.length; i++) {
            continuousParentIndices[i] = NodeDictionary.getInstance().nodeIdx(continuousParentNames[i]);
        }
        init();
    }

    /**
     * constructor if the node only have discrete parents
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

    public BasicHybridCPD(String theNodeName, String discreteParentsStr, String continuousParentStr) {
        String discreteParentNames[] = discreteParentsStr.split(",");
        String continuousParentNames[] = continuousParentStr.split(",");
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        discreteParentIndices = new int[discreteParentNames.length];
        continuousParentIndices = new int[continuousParentNames.length];
        for (int i = 0; i < discreteParentNames.length; i++) {
            discreteParentIndices[i] = NodeDictionary.getInstance().nodeIdx(discreteParentNames[i]);
        }
        for (int i = 0; i < continuousParentNames.length; i++) {
            continuousParentIndices[i] = NodeDictionary.getInstance().nodeIdx(continuousParentNames[i]);
        }
        init();
    }

    public BasicHybridCPD(String theNodeName, String discreteParentsStr) {
        String discreteParentNames[] = discreteParentsStr.split(",");
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        discreteParentIndices = new int[discreteParentNames.length];
        continuousParentIndices = null;
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
            if (NodeDictionary.getInstance().nodeName(theNodeIdx).equals("M1")){
                if(NodeDictionary.getInstance().nodeName(theNodeIdx).equals("x1")){
                    int x= nd.cardinality(parentIdx);
                    logger.debug("Cardinality= " +nd.cardinality(parentIdx));
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

    public void setDistribution(String discreteParentNodesStr, String discreteParentValuesStr, ContinuousFactor factor) {
        String discreteParentNames[] = discreteParentNodesStr.split(",");
        String discreteParentValues[] = discreteParentValuesStr.split(",");
        int discreteParentNodeIndices[] = NodeUtil.nodeIndices(discreteParentNames);
        int discreteParentValueIndices[] = NodeUtil.valueIndices(discreteParentNodeIndices, discreteParentValues);
        setDistribution(discreteParentNodeIndices, discreteParentValueIndices, factor);
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

    public ContinuousFactor getDistributionFromSuperset(String discreteParentNodeStr,
                                                        String discreteParentValueStr) {
        int discreteNodeIndices[] = NodeUtil.nodeIndices(discreteParentNodeStr);
        int discreteValueIndices[] = NodeUtil.valueIndices(discreteNodeIndices,
                discreteParentValueStr);
        SortArrays.sort(discreteNodeIndices, discreteValueIndices);
        return getDistributionFromSuperset(discreteNodeIndices, discreteValueIndices);
    }

    public ContinuousFactor getDistribution(String discreteParentNodesStr, String discreteParentValuesStr) {
        String discreteParentNames[] = discreteParentNodesStr.split(",");
        String discreteParentValues[] = discreteParentValuesStr.split(",");

        int discreteParentNodeIndices[] = NodeUtil.nodeIndices(discreteParentNames);
        int discreteParentValueIndices[] = NodeUtil.valueIndices(discreteParentNodeIndices, discreteParentValues);
        SortArrays.sort(discreteParentNodeIndices, discreteParentValueIndices);
        return getDistribution(discreteParentNodeIndices, discreteParentValueIndices);
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

    public static void test() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putNode("X");
        nd.putValues("D1", "T", "F");
        nd.putValues("D2", "T", "F");
        nd.putNode("C1");
        nd.putNode("C2");
        nd.putValues("D3", "T", "F");

        String discreteParents[] = {"D1", "D2"};
        String continuousParents[] = {"C1", "C2"};

        BasicHybridCPD basicCPD = new BasicHybridCPD("X", discreteParents, continuousParents);

        logger.debug(basicCPD);
//        int dd[]= new int[2];
//        int dd_TT[]= new int[2];
//        dd[0]=nd.nodeIdx("D1");
//        dd[1]=nd.nodeIdx("D2");
//        dd_TT[0]= nd.valueIdx("D1", "T");
//        dd_TT[1]= nd.valueIdx("D2", "T");

        ConditionalLinearGaussian clg_TT = new ConditionalLinearGaussian(1.0, "X", "C1", "C2");
        clg_TT.setParams(1.0, 2.0, 0.5, 1);
        logger.debug(clg_TT);


//        int dd_FF[] = new int[2];
//        dd_FF[0]= nd.valueIdx("D1", "F");
//        dd_FF[1]= nd.valueIdx("D2", "F");

        ConditionalLinearGaussian clg_FF = new ConditionalLinearGaussian(1.0, "X", "C1", "C2");
        clg_FF.setParams(2.0, 2.0, 1, 1);
        logger.debug(clg_TT);

//        basicCPD.setDistribution(dd, dd_TT, clg_TT);
        basicCPD.setDistribution("D1,D2", "T,T", clg_TT);
        basicCPD.setDistribution("D1,D2", "F,F", clg_FF);

        logger.debug(basicCPD);
        logger.debug(basicCPD.getDistribution("D1,D2", "T,T"));
        logger.debug(basicCPD.getDistribution("D1,D2", "F,F"));
        logger.debug(basicCPD.getDistributionFromSuperset("D2,D1,D3", "F,F,T"));
    }

    public static void main(String[] args) {
        test();
    }


}
