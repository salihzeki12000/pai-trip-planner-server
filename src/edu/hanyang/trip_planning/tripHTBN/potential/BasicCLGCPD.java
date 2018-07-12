package edu.hanyang.trip_planning.tripHTBN.potential;

import cntbn.common.NodeDictionary;
import cntbn.terms_factors.ContinuousFactor;
import org.apache.log4j.Logger;

/**
 *
 */
public class BasicCLGCPD implements InterfaceCLGCPD {

    private int theNodeIdx;
    private static Logger logger = Logger.getLogger(BasicCLGCPD.class);
    /**
     * cpt element들임.  처번째 indices는 parentNode에 의한 key들,  두번째  indices는 theNodeValue,
     */
    private ContinuousFactor factor;

    public BasicCLGCPD(int theNodeIdx, ContinuousFactor factor) {
        this.theNodeIdx = theNodeIdx;
        this.factor = factor;
    }
    public BasicCLGCPD(String theNodeName, ContinuousFactor factor) {
        this.theNodeIdx = NodeDictionary.getInstance().nodeIdx(theNodeName);
        this.factor = factor;
    }


    @Override
    public ContinuousFactor getDistribution() {
        return factor;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        NodeDictionary nd = NodeDictionary.getInstance();
        strbuf.append("CPD of " + nd.nodeName(theNodeIdx) + "\n");
        strbuf.append( factor + "\n");

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

    }

    public static void main(String[] args) {
        test();
    }


}
