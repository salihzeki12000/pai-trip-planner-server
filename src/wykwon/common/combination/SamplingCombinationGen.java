package wykwon.common.combination;

import org.apache.log4j.Logger;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 2014. 8. 29.
 * Time: 오후 5:22
 * To change this template use File | Settings | File Templates.
 */
//////////////////////////////////////
// Combination
//
// You do not need to write the code below here.
// You just need to be able to USE it.
//////////////////////////////////////

// The algorithm is from Applied Combinatorics, by Alan Tucker.
// Based on code from koders.com


public class SamplingCombinationGen implements CombGen {
    private int numberOfSample;
    private long totalNumber;
    private ExhaustCombnationGen combGen;
    private static Logger logger = Logger.getLogger(SamplingCombinationGen.class);
    private int cnt = 0;
    private int divider;

    public SamplingCombinationGen(int n, int r, int numSample) {
        combGen = new ExhaustCombnationGen(n, r);
        this.numberOfSample = numSample;
        this.totalNumber = Combination.combinations(n, r);
        this.divider = (int) (0.95 * totalNumber / numberOfSample);
//        logger.debug("totalNum=" + totalNumber);
    }

    public boolean hasNext() {
        return cnt < 100;
    }

    public int[] next() {
        int skipNum = divider + (int) (Math.random() * 10);
        int ret[] = null;
        for (int i = 0; i < skipNum; i++) {
            ret = combGen.next();
        }
        cnt++;
        return ret;

    }

    public static void main(String[] args) {
        Random random = new Random();

        System.out.println(Combination.combinations(33, 15));
        System.out.println(2000000000);
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(1000000000));

        }
//        SamplingCombinationGen gen = new SamplingCombinationGen(30,5,100);
//        while(gen.hasNext()){
//            logger.debug(Arrays.toString(gen.next()));
//        }
    }
}
