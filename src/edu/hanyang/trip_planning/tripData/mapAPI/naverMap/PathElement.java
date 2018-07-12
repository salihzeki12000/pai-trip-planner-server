package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 1. 2
 * Time: 오후 4:31
 * To change this template use File | Settings | File Templates.
 */
public class PathElement implements Comparable<PathElement> {

    private double srcLongtitude;
    private double srcLatitude;
    private double destLongtitude;
    private double destLatitude;
    String way;
    private int cost;
    private int time;


    public double getSrcLongtitude() {
        return srcLongtitude;
    }

    public void setSrcCoordinate(double srcLongtitude, double srcLatitude) {
        this.srcLongtitude = srcLongtitude;
        this.srcLatitude = srcLatitude;
    }

    public double getSrcLatitude() {
        return srcLatitude;
    }


    public double getDestLongtitude() {
        return destLongtitude;
    }

    public void setDestCoordinate(double destLongtitude, double destLatitude) {
        this.destLongtitude = destLongtitude;
        this.destLatitude = destLatitude;
    }

    public double getDestLatitude() {
        return destLatitude;
    }


    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDuration() {
        return time;
    }

    public void setDuration(int time) {
        this.time = time;
    }

    public double getDurationSD() {
        if (way == null) {
            return (double) time / 8.0 + 5;
        } else if (way.equals("KTX")) {
            return 10;
        } else {
            return (double) time / 8.0 + 5;
        }
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int compareTo(PathElement o) {

        Double value = ((double) time) / ((double) cost);
        Double argValue = ((double) o.time) / ((double) o.cost);
        return value.compareTo(argValue);
    }
}
