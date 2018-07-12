package edu.hanyang.protocol;



/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 8
 * Time: 오후 5:25
 * To change this template use File | Settings | File Templates.
 */
public class SpatialStatementByLocation {

    public Location location;
    public double distance;

    public SpatialStatementByLocation(Location location, double distance) {
        this.location = location;
        this.distance = distance;
    }


    public String toString() {
        return location + "로부터 " + distance + "km 이내";
    }
}
