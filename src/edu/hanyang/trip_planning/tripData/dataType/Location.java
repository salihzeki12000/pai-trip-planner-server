package edu.hanyang.trip_planning.tripData.dataType;

/**
 * 위치 좌표를 나타내는 인터페이스 (경위도 사용함)
 */
public class Location {
    public double latitude; // 위도
    public double longitude; // 경도

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Location deepCopy() {
        return new Location(latitude, longitude);
    }
}
