package cntbn.terms_factors.tools;

import javolution.util.FastSet;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Name: SetTools
 * User: wykwon
 * Date: 13. 2. 24
 * Time: 오후 9:40
 */
public class SetTools {
    private static Logger logger = Logger.getLogger(SetTools.class);

    public static Set<Integer> subtract(Set<Integer> first, Set<Integer> second) {
        Set<Integer> remaining = new FastSet<Integer>(first);
        remaining.removeAll(second);
        return remaining;
    }

    public static Set<Integer> makeSingleSet(int value) {
        Set<Integer> ret = new FastSet<Integer>();
        ret.add(value);
        return ret;
    }

    public static void main(String args[]) {
        Set<Integer> first = new FastSet<Integer>();
        Set<Integer> second = new FastSet<Integer>();
        first.add(1);
        first.add(2);
        first.add(3);
        second.add(3);
        second.add(4);
        logger.debug(subtract(first, second));
        logger.debug(first);
        logger.debug(second);

    }

}
