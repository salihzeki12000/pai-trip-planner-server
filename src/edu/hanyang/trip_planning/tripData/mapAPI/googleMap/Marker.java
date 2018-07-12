package edu.hanyang.trip_planning.tripData.mapAPI.googleMap;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;

/**
 * Created by wykwon on 2015-12-20.
 */
public class Marker {
    public String name;
    public double latitude;
    public double longitude;

    public Marker(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Marker(BasicPOI poi) {
        this.name = poi.getTitle();
        this.longitude = poi.getLocation().longitude;
        this.latitude = poi.getLocation().latitude;
    }
}
