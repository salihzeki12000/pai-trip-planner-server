package edu.hanyang.trip_planning.utils;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 26
 * Time: 오후 3:52
 * To change this template use File | Settings | File Templates.
 */
public class StringPairKey {
    String first;
    String Second;

    public StringPairKey(String first, String second) {
        this.first = first;
        Second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringPairKey)) return false;

        StringPairKey that = (StringPairKey) o;

        if (!Second.equals(that.Second)) return false;
        return first.equals(that.first);
    }

    @Override
    public int hashCode() {
        int result = first.hashCode();
        result = 31 * result + Second.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return
                '{' + first + ',' +
                        Second +
                        '}';
    }
}
