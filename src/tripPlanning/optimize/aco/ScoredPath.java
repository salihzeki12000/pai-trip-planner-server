package tripPlanning.optimize.aco;

import java.util.Arrays;

/**
 * Created by dummyProblem on 2016-10-28.
 */
public class ScoredPath implements Comparable<ScoredPath> {
    private int path[];
    private Double value;

    public ScoredPath(int path[], double value) {
        this.path = path.clone();
        this.value = value;
    }

    public ScoredPath(double value, int... path) {
        this.path = path.clone();
        this.value = value;
    }

    public int[] getPath() {
        return this.path;
    }

    public double getValue() {
        return this.value;
    }

    public String toString() {
        return Arrays.toString(path) + "\t=" + value;
    }

    @Override
    public int compareTo(ScoredPath o) {
        return -value.compareTo(o.value);
    }
}
