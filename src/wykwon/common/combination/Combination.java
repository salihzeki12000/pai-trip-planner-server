package wykwon.common.combination;

import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 4. 9
 * Time: 오후 12:58
 * To change this template use File | Settings | File Templates.
 */
public class Combination {

    public static long combinations(int n, int k) {
        BigInteger factorialN = factorial(n);
        BigInteger factorialK = factorial(k);
        BigInteger factorialNMinusK = factorial(n - k);
        return factorialN.divide(factorialK.multiply(factorialNMinusK)).longValue();

    }

    private static BigInteger factorial(int n) {
        BigInteger ret = BigInteger.ONE;
        for (int i = 1; i <= n; ++i) ret = ret.multiply(BigInteger.valueOf(i));
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(combinations(25, 10));
    }

}
