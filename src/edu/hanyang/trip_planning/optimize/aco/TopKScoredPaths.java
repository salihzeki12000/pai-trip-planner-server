package edu.hanyang.trip_planning.optimize.aco;

import java.util.*;

/**
 * Created by dummyProblem on 2016-10-29.
 */
public class TopKScoredPaths {
    private ScoredPath topKelements[];
    private int k;
    private double min;
    private int numElements = 0;

    public TopKScoredPaths(int k) {
        this.k = k;
        topKelements = new ScoredPath[k];
    }

    public void add(ScoredPath scoredPath) {
        if (numElements == 0) {
            min = scoredPath.getValue();
            topKelements[numElements] = scoredPath;
            numElements++;
        } else if (numElements < k) {
            if (checkDuplicated(scoredPath)) {
                return;
            }
            if (scoredPath.getValue() < min) {
                min = scoredPath.getValue();
            }
            topKelements[numElements] = scoredPath;
            numElements++;
        } else {
            if (checkDuplicated(scoredPath)) {
                return;
            }
            if (scoredPath.getValue() > min) {
                topKelements[minimumValuedIdx()] = scoredPath;
                min = topKelements[minimumValuedIdx()].getValue();  // mgkim:
            }
        }
    }

    private int minimumValuedIdx() {
        int argMin = -1;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < topKelements.length; i++) {
            double value = topKelements[i].getValue();
            if (value < min) {
                argMin = i;
                min = value;
            }
        }
        return argMin;
    }

    private boolean checkDuplicated(ScoredPath scoredPath) {
        for (int i = 0; i < numElements; i++) {
            int path[] = topKelements[i].getPath();
            if (Arrays.equals(scoredPath.getPath(), path)) {
                return true;
            }
        }
        return false;
    }

    public ScoredPath[] getPaths() {
        List<ScoredPath> list = new ArrayList<>();
        for (ScoredPath p : topKelements){
            if (p!=null) {
                list.add(p);
            }
        }
        ScoredPath retArray[] = new ScoredPath[list.size()];
        for (int i=0;i<list.size();i++){
            retArray[i] = list.get(i);
        }
        return retArray;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (ScoredPath scoredPath : topKelements) {
            strbuf.append(scoredPath + "\n");
        }
        return strbuf.toString();
    }

    public static void main(String[] args) {
        TopKScoredPaths x = new TopKScoredPaths(3);
        x.add(new ScoredPath(0.1, 14, 23, 53));
        x.add(new ScoredPath(0.1, 64, 21, 63 ,12));
        x.add(new ScoredPath(0.1, 13, 0, 18));
        x.add(new ScoredPath(0.3, 1, 2));       //이것
        x.add(new ScoredPath(0.4, 1, 2, 3));    //이것
        x.add(new ScoredPath(0.5, 1, 2, 3, 4)); //이것
        x.add(new ScoredPath(0.2, 15, 18, 20, 31));
        x.add(new ScoredPath(0.2, 17, 22, 48, 46));
        x.add(new ScoredPath(0.2, 15, 24, 38));
        System.out.println(Arrays.toString(x.getPaths()));
    }
}
