package wykwon.common.combination;

import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 4. 9
 * Time: 오후 1:14
 * To change this template use File | Settings | File Templates.
 * https://msdn.microsoft.com/en-us/library/aa289166%28v=vs.71%29.aspx
 */
public class CombinationGenOrdered {
    private int n, r;
    private long totalNumber;

    private static Logger logger = Logger.getLogger(CombinationGenOrdered.class);

    public CombinationGenOrdered(int n, int r) {
        this.n = n;
        this.r = r;
        totalNumber = Combination.combinations(n, r);
        logger.debug("totalNumber=" + totalNumber);
    }

    /**
     * 몇 번째인 combination을 반환함
     *
     * @param idx
     * @return
     */
    public int[] combination(int idx) {
        return null;
    }

    public static void main(String[] args) {
        CombinationGenOrdered c = new CombinationGenOrdered(5, 3);
    }
}
