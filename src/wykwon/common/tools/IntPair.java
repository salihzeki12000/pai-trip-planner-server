package wykwon.common.tools;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 13
 * Time: 오후 1:39
 * To change this template use File | Settings | File Templates.
 */
public class IntPair {
    private int first;
    private int second;

    public IntPair(int key, int value) {
        this.first = key;
        this.second = value;
    }

    public int first() {
        return first;
    }

    public int second() {
        return second;
    }

    public String toString() {

        return new String(first + "=" + second);
    }
}
