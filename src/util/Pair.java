package util;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 7. 30
 * Time: 오후 4:22
 * To change this template use File | Settings | File Templates.
 */
public class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public String toString() {
        return new String("<" + first + "," + second + ">");
    }

}