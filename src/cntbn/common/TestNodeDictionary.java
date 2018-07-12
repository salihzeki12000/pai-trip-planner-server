package cntbn.common;

import cntbn.exception.NoSuchVariableException;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA. User: wykwon Date: 12. 12. 26 Time: 오후 9:51 To
 * change this template use File | Settings | File Templates.
 */
public class TestNodeDictionary {
    private static Logger logger = Logger.getLogger(TestNodeDictionary.class);

    public static void main(String args[]) {
        NodeDictionary dict = NodeDictionary.getInstance();
        try {
            dict.putValues("D", "bus", "subway");
            dict.putValues("A", "true", "false");
            dict.putValues("B", "true", "false");
            // logger.info(dict);
        } catch (NoSuchVariableException e) {
            e.printStackTrace(); // To change body of catch statement use File |
            // Settings | File Templates.
        }
        logger.info(dict);
    }

    public static NodeDictionary makeSample() {
        NodeDictionary dict = NodeDictionary.getInstance();
        dict.putValues("X", "true", "false");
        dict.putValues("Y", "true", "false");
        dict.putValues("Z", "true", "false");
        logger.info(dict);
        return dict;
    }
}
