package services.locations;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;

/**
 * Created by wykwon on 2015-11-12.
 */
public class OrderedLocation implements Comparable<OrderedLocation> {
    public BasicPOI poi;
    public Double order;

    public OrderedLocation(BasicPOI poiName) {
        this.poi = poiName;
        this.order = Math.random();
    }

    public OrderedLocation(BasicPOI poiName, double order) {
        this.poi = poiName;
        this.order = order;
    }

    public void setOrder(double order) {
        this.order = order;
    }
    public String toString() {
        return poi.getTitle();
    }

    @Override
    public int compareTo(OrderedLocation o) {
        return order.compareTo(o.order);
    }


    public static void main(String[] args) {

    }

}
