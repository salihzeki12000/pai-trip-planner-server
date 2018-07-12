package edu.hanyang.trip_planning.tripData.dataType;



import edu.hanyang.trip_planning.tripData.poi.POIUtil;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 2
 * Time: 오후 3:08
 * To change this template use File | Settings | File Templates.
 */
public class POIIdentifier {
    public String name;
    public double latitude;
    public double longitude;

    public POIIdentifier(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * 100m 이내면 같은 위치로 봐야할듯 하다.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POIIdentifier)) return false;

        POIIdentifier that = (POIIdentifier) o;

//        if (distance < ItemConverter.POI_SAME_THRESHOLD) {
//            return true;
//        }

        if (!name.equals(that.name)) {
            return false;
        }
        double distance = distance(that);
        return !(distance > POIUtil.POI_NEAR_THRESHOLD);
    }


    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("###.######");
        return name + "(" + df.format(latitude) + ',' + df.format(longitude) + ")";
    }

    private double distance(POIIdentifier poiIdentifier) {
        return POIUtil.distance(this.latitude, this.longitude, poiIdentifier.latitude, poiIdentifier.longitude);
    }

    public static POIIdentifier dummy_hanyang() {
        return new POIIdentifier("한양대", 37.557338, 127.045681);
    }

    public static void test() {
        POIIdentifier hanyang = new POIIdentifier("한양대", 37.557338, 127.045681);
        POIIdentifier office = new POIIdentifier("한양대", 37.555848, 127.049416);
        System.out.println("dist = " + hanyang.equals(office));
        System.out.println("office = " + office);
    }

    public static void main(String[] args) {
        test();
    }
}
