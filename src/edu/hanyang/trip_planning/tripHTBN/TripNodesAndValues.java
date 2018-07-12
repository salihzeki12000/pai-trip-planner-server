package edu.hanyang.trip_planning.tripHTBN;

import cntbn.common.NodeDictionary;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIGen;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.utils.DateTimeFormatStr;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import wykwon.common.MyCollections;
import wykwon.common.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Node와 value의 이름을 integer로 찾기 쉽게 만드는 클래스
 * Application Dependent하게 만들어져야함
 * NodeDictionary와 연동됨
 */
public class TripNodesAndValues {
//    private static Logger logger = Logger.getLogger(TripNodesAndValues.class);
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
    public int Co;
    public int PT;
    public int Te;
    public int Ra;
    public int TS;
    public int W;
    public int S;
    public int Gr;
    public int C;
    public int Ca;
    public int Pa;

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
//        logger.debug(nd);


//        public int T1;
//        public int T2;
//        public int E1;
//        public int E2;
//        public int DT1;
//        public int DT2;
//
//        public int M;
//        public int E;
//        public int Co;
//        public int PT;
//        public int Te;
//        public int Ra;
//        public int TS;
//        public int W;
//        public int S;
//        public int Gr;
//        public int C;
//        public int Ca;
//        public int Pa;

//
//        nd.putNode("M-");
//        nd.putNode("M+");
//
//        nd.putNode("T-");
//        nd.putNode("T+");
//
//        nd.putNode("D-");
//        nd.putNode("D+");
//
//        nd.putValues("DT-", generateDiscreteTime(discreteTimeWidth));
//        nd.putValues("DT+", generateDiscreteTime(discreteTimeWidth));

//        logger.debug(nd);

//        // 활동량 레벨 변수
//        for (int i = 1; i < maximumSequence + 2; i++) {
//            String values[] = {"VeryLow", "Low", "Normal","High","VeryHigh"};
//            nd.putValues(("AL" + i), values);
//        }
//
//        for (int i = 1; i < subsetPOIs.size(); i++) {
//            String values[] = {"g", "mid", "bad"};
//            nd.putValues(("J" + i ), values);
//        }
    }

    public int getDiscreteTimeWidth() {
        return discreteTimeWidth;
    }

    public int getNumDiscreteTimeSlots() {
        return discreteTimeValues.length;
    }

    private String[] generateDiscreteTime(int dMin) {
        if (dMin % 10 != 0) {
            throw new RuntimeException("minute must be divided by 10");
        }
        DateTime curDateTime = DateTimeFormatStr.parseFullDateTime("2000-01-01 00:00");
        int startDay = curDateTime.getDayOfYear();
        List<String> timeStrList = new ArrayList<>();
        while (true) {
            if (curDateTime.getDayOfYear() != startDay) {
                break;
            }
            timeStrList.add(DateTimeFormatStr.printTime(curDateTime));
//            logger.debug(DateTimeFormatStr.printTime(curDateTime));
            curDateTime = curDateTime.plusMinutes(dMin);
        }
        String retArray[] = MyCollections.toArray(timeStrList);
        return retArray;
    }

    public int hourMinToIdx(int hourOfDay, int minOfHour) {
        int minOfDay = hourOfDay * 60 + minOfHour;
        int idx = minOfDay / discreteTimeWidth;
//        logger.debug(idx);
        return idx;
    }


    public int hourToIdx(double hourOfDay) {
        int minOfDay = (int) (hourOfDay * 60);
        int idx = minOfDay / discreteTimeWidth;
//        logger.debug(idx);
        return idx;
    }

    private Pair<Integer, Integer> idxToHourMin(int idx) {
        int minOfDay = idx * discreteTimeWidth;
        int hourOfDay = minOfDay / 60;
        int minOfHour = minOfDay % 60;
        return new Pair<Integer, Integer>(hourOfDay, minOfHour);
    }

    public String[] getDiscreteTimeValue() {
        return discreteTimeValues;
    }

    public static void main(String[] args) {

        TripNodesAndValues tripNodesAndValues = new TripNodesAndValues(SubsetPOIGen.getJeju10_(), 30);
        for (int i = 0; i < tripNodesAndValues.discreteTimeValues.length; i++) {
//            logger.debug(tripNodesAndValues.discreteTimeValues[i]);
            Pair<Integer, Integer> hourmin = tripNodesAndValues.idxToHourMin(i);
//            logger.debug(tripNodesAndValues.hourMinToIdx(hourmin.first(), hourmin.second()) + "\t" + hourmin + "\n");


        }

    }
}
