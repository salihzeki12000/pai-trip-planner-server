package wykwon.common.tools;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 13
 * Time: 오후 1:39
 * To change this template use File | Settings | File Templates.
 */
public class IntegerPair {
    private Integer first;
    private Integer second;

    public IntegerPair(Integer key, Integer value) {
        this.first = key;
        this.second = value;
    }

    public Integer first() {
        return first;
    }

    public Integer second() {
        return second;
    }

    public String toString() {

        return new String(first + "=" + second);
    }
}
