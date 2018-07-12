package cntbn.common;

public class DoublePair {
    private double first;
    private double second;

    public DoublePair(double first, double second) {
        this.first = first;
        this.second = second;
    }

    public DoublePair(DoublePair pair) {
        this.first = pair.first;
        this.second = pair.second;
    }

    public double getFirst() {
        return first;
    }

    public double getSecond() {
        return second;
    }

    public String toString() {
        return new String(first + "," + second);
    }
}
