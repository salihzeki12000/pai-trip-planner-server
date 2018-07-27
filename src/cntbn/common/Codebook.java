package cntbn.common;

import cntbn.exception.NoSuchVariableException;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.Collection;

/**
 * Codebook class
 * <p/>
 * convert strings to indices
 *
 * @author wykwon
 * @created 2012-12-27
 */
public class Codebook {

    private static Logger logger = Logger.getLogger(Codebook.class);
    private FastMap<String, Integer> nameMap = new FastMap<>();
    private FastList<String> idxMap = new FastList<String>();
    int count = 0;

    public void addName(String name) {
        //
        if (idxMap.contains(name)) {
            // 노드가 이미 정의되어 있으면, 기존 노드를 날리고, 새로 만들어
//            logger.debug("Node " + name + " is redefined !");
//            throw new RuntimeException("Node " + name + " is redefined !");
        } else {
            idxMap.add(name);
            nameMap.put(name, count);
            count++;
        }

    }

    public void putNames(String... names) {
        for (String string : names) {
            addName(string);
        }
    }

    public boolean hasName(String name) {
        return nameMap.containsKey(name);
    }

    public int size() {
        return idxMap.size();
    }

    public String getName(int idx) throws NoSuchVariableException {
        String name = idxMap.get(idx);
        if (name == null) {
            throw new NoSuchVariableException(idx);
        }
        return idxMap.get(idx);
    }

    public int getIndex(String name) throws NoSuchVariableException {
        Integer idx = nameMap.get(name);
        if (idx == null) {
            logger.fatal(nameMap);
            throw new NoSuchVariableException(name);
        }

        return idx;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        for (FastMap.Entry<String, Integer> e = nameMap.head(), end = nameMap
                .tail(); (e = e.getNext()) != end; ) {
            String key = e.getKey();
            Integer value = e.getValue();
            strBuf.append(key);
            strBuf.append("=");
            strBuf.append(value);
            strBuf.append(" ");
        }
        return strBuf.toString();
    }
}
