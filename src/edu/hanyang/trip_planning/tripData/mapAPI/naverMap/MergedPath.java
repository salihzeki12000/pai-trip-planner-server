package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 1. 2
 * Time: 오후 5:44
 * To change this template use File | Settings | File Templates.
 */
public class MergedPath implements Comparable<MergedPath> {
    private static Logger logger = Logger.getLogger(MergedPath.class);
    List<Integer> costList = new ArrayList<Integer>();
    List<Integer> durationList = new ArrayList<Integer>();
    List<Double> durationSDList = new ArrayList<Double>();

    public void add(int duration, double durationSD, int cost) {
        durationList.add(duration);
        durationSDList.add(durationSD);
        costList.add(cost);
    }

    public int getDuration() {
        int sum = 0;
        for (int duration : durationList) {
            sum += duration;
        }
        return sum;
    }

    public int getCost() {
        int sum = 0;
        for (int cost : costList) {
            sum += cost;
        }
        return sum;
    }

    public double getDurationSD() {
//    logger.debug(durationSDList);
        int sum = 0;
        for (double durationSD : durationSDList) {
            sum += durationSD;
        }
        return sum;
    }

    @Override
    public int compareTo(MergedPath o) {
        Double value = ((double) getDuration()) / ((double) getCost());
        Double argValue = ((double) o.getDuration()) / ((double) o.getCost());
        return value.compareTo(argValue);
    }

    public String toString() {
        return "duration=" + getDuration() + " Sd=" + getDurationSD() + " cost=" + getCost();
    }

}
