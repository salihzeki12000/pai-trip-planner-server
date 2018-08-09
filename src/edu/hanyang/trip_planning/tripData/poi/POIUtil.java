package edu.hanyang.trip_planning.tripData.poi;


import edu.hanyang.trip_planning.tripData.dataType.Location;

public class POIUtil {
    public static final double POI_NEAR_THRESHOLD = 1;
    public static final double POI_SAME_THRESHOLD = 0.1;

    /**
     * 경위도 좌표 km로 표현함.
     *
     * @param lat1 첫번째 위치의 latitude
     * @param lon1 첫번째 위치의 longtitude
     * @param lat2 두번째 위치의 latidude
     * @param lon2 두번째 위치의 longtitude
     * @return 두지점간 거리 [km]
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    /**
     * 거리측정 단위 km
     *
     * @param l1
     * @param l2
     * @return
     */
    public static double distance(Location l1, Location l2) {
        return distance(l1.latitude, l1.longitude, l2.latitude, l2.longitude);
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static void test() {
        System.out.println(distance(37.555404, 127.043538, 37.557565, 127.050061));
    }

    public static void main(String[] args) {
        test();
    }

}