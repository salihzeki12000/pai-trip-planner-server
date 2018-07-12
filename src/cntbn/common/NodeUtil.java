package cntbn.common;

import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: wykwon Date: 13. 2. 12 Time: 오후 3:35 To
 * change this template use File | Settings | File Templates.
 */
public class NodeUtil {
    public static String[] nodeNames(int nodeIndices[]) {
        String nodeNames[] = new String[nodeIndices.length];
        for (int i = 0; i < nodeIndices.length; i++) {
            nodeNames[i] = NodeDictionary.getInstance()
                    .nodeName(nodeIndices[i]);
        }
        return nodeNames;
    }

    public static int[] nodeIndices(String nodeNames[]) {
        int nodeIndices[] = new int[nodeNames.length];
        for (int i = 0; i < nodeNames.length; i++) {
            nodeIndices[i] = NodeDictionary.getInstance()
                    .nodeIdx(nodeNames[i]);
        }
        return nodeIndices;
    }

    public static int[] nodeIndices(String nodeNamesStr) {
        String nodeNames[] = nodeNamesStr.split(",");
        int nodeIndices[] = new int[nodeNames.length];
        for (int i = 0; i < nodeNames.length; i++) {
            nodeIndices[i] = NodeDictionary.getInstance()
                    .nodeIdx(nodeNames[i]);
        }
        return nodeIndices;
    }

    public static int[] valueIndices(int nodeIndices[], String valueNames[]) {
        int valueIndices[] = new int[nodeIndices.length];
        if (nodeIndices.length != valueNames.length) {
            throw new RuntimeException("Size mismatch!!");
        }

        for (int i = 0; i < nodeIndices.length; i++) {
            valueIndices[i] = NodeDictionary.getInstance().valueIdx(
                    nodeIndices[i], valueNames[i]);
        }
        return valueIndices;
    }

    public static int[] valueIndices(int nodeIndices[], String valueNamesStr) {
        String valueName[] = valueNamesStr.split(",");
        return valueIndices(nodeIndices, valueName);
    }

    public static String[] valueNames(int nodeIndices[], int valueIndices[]) {
        String valueNames[] = new String[nodeIndices.length];
        if (nodeIndices.length != valueIndices.length) {
            throw new RuntimeException("Size mismatch!!");
        }

        for (int i = 0; i < nodeIndices.length; i++) {
            valueNames[i] = NodeDictionary.getInstance().valueName(
                    nodeIndices[i], valueIndices[i]);
        }
        return valueNames;
    }

    public static String nodeSetToString(Set<Integer> nodeSet) {
        StringBuffer strbuf = new StringBuffer();
        for (Integer nodeIdx : nodeSet) {
            strbuf.append(NodeDictionary.getInstance().nodeName(nodeIdx));
            strbuf.append(",");
        }
        strbuf.deleteCharAt(strbuf.length() - 1);
        return strbuf.toString();
    }

    public static int[] cardinalities(int nodeIndices[]) {
        NodeDictionary dict = NodeDictionary.getInstance();
        int cardinalities[] = new int[nodeIndices.length];
        for (int i = 0; i < nodeIndices.length; i++) {
            cardinalities[i] = dict.cardinality(nodeIndices[i]);
        }
        return cardinalities;
    }

}
