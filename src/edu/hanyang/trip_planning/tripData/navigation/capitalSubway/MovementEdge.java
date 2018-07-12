package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 14
 * Time: 오후 2:15
 * To change this template use File | Settings | File Templates.
 */
public class MovementEdge {
    double distance;
    double time;

    String src;
    String dest;

    public MovementEdge(String src, String dest, double distance, double time) {
        this.src = src;
        this.dest = dest;
        this.time = time;
        this.distance = distance;
    }

    public String toString() {
        return src + "->" + dest + "[" + time + "m " + distance + "km]";

    }
}
