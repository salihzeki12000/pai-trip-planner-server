package wykwon.common.combination;

import org.apache.log4j.Logger;

/**
 * Temporal Bipartition 하는 방법중 하나.
 */
public class TemporalCombination {
    private static Logger logger = Logger.getLogger(TemporalCombination.class);

    public static void test(int dimeision) {

        SubsetGenerator past = new SubsetGenerator(dimeision);


        while (past.hasNext()) {
            int pastComb[] = past.next();
        }

        SubsetGenerator future = new SubsetGenerator(dimeision);
        while (future.hasNext()) {
            int futureComb[] = future.next();
        }


    }

    public static void main(String[] args) {
        test(3);
    }
}
