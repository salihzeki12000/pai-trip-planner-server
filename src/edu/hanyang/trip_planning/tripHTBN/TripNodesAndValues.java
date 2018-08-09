package edu.hanyang.trip_planning.tripHTBN;

import cntbn.common.NodeDictionary;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import util.TimeStrHelper;
import org.joda.time.DateTime;
import util.MyCollections;

import java.util.ArrayList;
import java.util.List;

/**
 * Node와 value의 이름을 integer로 찾기 쉽게 만드는 클래스
 * Application Dependent하게 만들어져야함
 * NodeDictionary와 연동됨
 */
public class TripNodesAndValues {
    public int TMP;
    public int X1;
    public int X2;
    public int T1;
    public int T2;
    public int T0;

    public int E1;
    public int E2;

    public int DT;
    public int DE;

    public int M;
    public int D0;
    public int D;

    private SubsetPOIs subsetPOIs;
    private int discreteTimeWidth;
    private String discreteTimeValues[];


    public TripNodesAndValues(SubsetPOIs subsetPOIs, int discreteTimeWidth) {
        this.subsetPOIs = subsetPOIs;
        this.discreteTimeWidth = discreteTimeWidth;
        discreteTimeValues = generateDiscreteTime(this.discreteTimeWidth);
        generateNodes();
    }

    private void generateNodes() {
        NodeDictionary nd = NodeDictionary.getInstance();
        nd.putNode("TMP");
        TMP = nd.nodeIdx("TMP");


        nd.putValues("X1", subsetPOIs.getTitles());
        nd.putValues("X2", subsetPOIs.getTitles());
        X1 = nd.nodeIdx("X1");
        X2 = nd.nodeIdx("X2");

        nd.putNode("T1");
        T1 = nd.nodeIdx("T1");
        nd.putNode("T2");
        T2 = nd.nodeIdx("T2");
        nd.putNode("T0");
        T0 = nd.nodeIdx("T0");

        nd.putNode("E1");
        E1 = nd.nodeIdx("E1");
        nd.putNode("E2");
        E2 = nd.nodeIdx("E2");

        nd.putValues("DT", discreteTimeValues);
        DT = nd.nodeIdx("DT");

        nd.putValues("DE", discreteTimeValues);
        DE = nd.nodeIdx("DE");

        nd.putNode("D0");
        D0 = nd.nodeIdx("D0");

        nd.putNode("D");
        D = nd.nodeIdx("D");


        nd.putNode("M");
        M = nd.nodeIdx("M");
        nd.putNode("E");
    }

    public int getDiscreteTimeWidth() {
        return discreteTimeWidth;
    }

    private String[] generateDiscreteTime(int dMin) {
        if (dMin % 10 != 0) {
            throw new RuntimeException("minute must be divided by 10");
        }
        DateTime curDateTime = TimeStrHelper.parseFullDate("2000-01-01 00:00");
        int startDay = curDateTime.getDayOfYear();
        List<String> timeStrList = new ArrayList<>();
        while (curDateTime.getDayOfYear() == startDay) {
            timeStrList.add(TimeStrHelper.printHourMin(curDateTime));
            curDateTime = curDateTime.plusMinutes(dMin);
        }
        String retArray[] = MyCollections.toArray(timeStrList);
        return retArray;
    }

    public int hourToIdx(double hourOfDay) {
        int minOfDay = (int) (hourOfDay * 60);
        int idx = minOfDay / discreteTimeWidth;
        return idx;
    }

    public String[] getDiscreteTimeValue() {
        return discreteTimeValues;
    }
}
