package tripPlanning.tripData.dataType;

/**
 * 위치 좌표를 나타내는 인터페이스 (경위도 사용함)
 * 서울 위도(latitude): 북위 37도
 * 서울 경도(longitude): 동경 126도
 */
public class Location {
    public double longitude; // 경도, X
    public double latitude; // 위도, Y

    public Location(double longitude,double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Location deepCopy() {
        return new Location(longitude,latitude);
    }
}
