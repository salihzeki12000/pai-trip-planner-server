package edu.hanyang.trip_planning.tripData.mapAPI.naver_car;

/**
 * Created by wykwon on 2015-12-28.
 */
public class CarRouteInfo {
    String srcName;
    String destName;
    public int dayOfWeek;
    public int hour;
    public int minute;
    public int speed;
    public int totalDistance;
    public int totalTime;
    public int taxiCost;

    public CarRouteInfo(String srcName, String destName, int dayOfWeek, int hour, int minute, int speed, int totalDistance, int totalTime, int taxiCost) {
        this.srcName = srcName;
        this.destName = destName;
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.minute = minute;
        this.speed = speed;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.taxiCost = taxiCost;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarRouteInfo)) return false;

        CarRouteInfo that = (CarRouteInfo) o;

        if (dayOfWeek != that.dayOfWeek) return false;
        if (hour != that.hour) return false;
        if (!srcName.equals(that.srcName)) return false;
        return destName.equals(that.destName);

    }

    @Override
    public int hashCode() {
        int result = srcName.hashCode();
        result = 31 * result + destName.hashCode();
        result = 31 * result + dayOfWeek;
        result = 31 * result + hour;
        return result;
    }
}
