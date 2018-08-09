package edu.hanyang.trip_planning.tripData.dataType;

public class UnitMovement {
    public Location srcLocation;
    public String srcTitle;
    public Location destLocation;
    public String destTitle;
    public ProbabilisticDuration spendingTime;
    public WayOfMovement way;
    public int cost;

    public UnitMovement(String srcTitle, Location srcLocation, String destTitle, Location destLocation, WayOfMovement way, ProbabilisticDuration spendingTime, int cost) {
        this.srcTitle = srcTitle;
        this.srcLocation = srcLocation;
        this.destTitle = destTitle;
        this.destLocation = destLocation;
        this.spendingTime = spendingTime;
        this.way = way;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return srcTitle + " 에서 " + destTitle + " 로 " + way + "를 이용해서 " + spendingTime + "동안 이동하고 비용은 " + cost + "원임";
    }
}
