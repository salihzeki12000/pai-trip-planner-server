package util;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 7. 30
 * Time: 오후 4:23
 * To change this template use File | Settings | File Templates.
 */
public class Triple<T1, T2, T3> {
    T1 first;
    T2 second;
    T3 third;

    public Triple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T1 first() {
        return first;
    }

    public T2 second() {
        return second;
    }

    public T3 third() {
        return third;
    }

    public String toString() {
        return new String("<" + first + "," + second + "," + third +">");
    }
}
