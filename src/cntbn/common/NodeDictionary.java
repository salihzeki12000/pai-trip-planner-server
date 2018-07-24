package cntbn.common;

import cntbn.exception.NoSuchVariableException;
import javolution.util.FastMap;

/**
 * Created with IntelliJ IDEA. User: wykwon Date: 12. 12. 26 Time: 오후 7:35 To
 * change this template use File | Settings | File Templates. singleton class
 */
public class NodeDictionary {
    private Codebook nodes;
    FastMap<String, Codebook> valuesByName;
    FastMap<Integer, Codebook> valuesByIdx;
    private static NodeDictionary instance = new NodeDictionary();

    private NodeDictionary() {
        nodes = new Codebook();
        valuesByIdx = new FastMap<Integer, Codebook>();
        valuesByName = new FastMap<String, Codebook>();
    }

    public static NodeDictionary getInstance() {
        return instance;
    }

    public boolean containNode(String name) {
        return nodes.hasName(name);
    }

    /**
     * Node의 이름과 value를 넣는다. - Discrete Random variable및 composite Random Variable까지
     *
     * @param rvName
     * @param values
     * @throws NoSuchVariableException
     */
    public void putValues(String rvName, String... values) throws NoSuchVariableException {
//        logger.debug(rvName + "={"+Arrays.toString(values) +"}");
        nodes.putNames(rvName);
        int rvIdx = nodes.getIndex(rvName);
        Codebook valuesCode = new Codebook();
        valuesCode.putNames(values);
        valuesByIdx.put(rvIdx, valuesCode);
        valuesByName.put(rvName, valuesCode);
    }

    public void putNode(String rvName)
            throws NoSuchVariableException {
        nodes.putNames(rvName);
    }

    public int nodeIdx(String nodeName) throws NoSuchVariableException {
        return nodes.getIndex(nodeName);
    }

    public String nodeName(int nodeIdx) throws NoSuchVariableException {
        return nodes.getName(nodeIdx);
    }

    public int valueIdx(int nodeIdx, String valueName)
            throws NoSuchVariableException {
        Codebook valueCode = valuesByIdx.get(nodeIdx);
        if (valueCode == null) {
            throw new NoSuchVariableException(nodeIdx);
        }
        return valueCode.getIndex(valueName);

    }

    public String valueName(int nodeIdx, int valueIdx)
            throws NoSuchVariableException {
        Codebook valueCode = valuesByIdx.get(nodeIdx);
        if (valueCode == null) {
            throw new NoSuchVariableException(nodeIdx);
        }
        return valueCode.getName(valueIdx);
    }

    public int cardinality(int nodeIdx) throws NoSuchVariableException {
        return valuesByIdx.get(nodeIdx).size();

    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();

        for (int i = 0; i < nodes.size(); i++) {

            strbuf.append("node: " + nodes.getName(i));
            strbuf.append("\t");
            Codebook valueCode = valuesByName.get(nodes.getName(i));
            if (valueCode != null) {
                strbuf.append("values: " + valueCode);
            }
            strbuf.append("\n");
        }
//        for (FastMap.Entry<String, Codebook> e = valuesByName.head(), end = valuesByName
//                .tail(); (e = e.getNext()) != end; ) {
//            String nodeName = e.getKey();
//            Codebook values = e.getValue();
//            strbuf.append("node: " + nodeName);
//            strbuf.append("\t");
//            strbuf.append("values: " + values);
//            strbuf.append("\n");
//
//        }
        return strbuf.toString();

    }
}
