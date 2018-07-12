package wykwon.common.tools;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 13. 8. 13
 * Time: 오후 1:39
 * To change this template use File | Settings | File Templates.
 */
public class KeyValuePair {
    private int key;
    private double value;

    public KeyValuePair(int key, double value) {
        this.key = key;
        this.value = value;
    }

    public int key() {
        return key;
    }

    public double value() {
        return value;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("###.####");
        return new String(key + "=" + df.format(value));
    }
}
