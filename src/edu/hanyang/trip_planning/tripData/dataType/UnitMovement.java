package edu.hanyang.trip_planning.tripData.dataType;


/**
 * 장소에서 장소로 이동하는 요소
 * 일단 지식 베이스 기준임.
 */
public class UnitMovement {

    /**
     * 출발 위치
     */
    public Location srcLocation;
    public String srcTitle;

    /**
     * 도착위치
     */
    public Location destLocation;
    public String destTitle;

    /**
     * 소요시간
     */
    public ProbabilisticDuration spendingTime;

    /**
     * 이동방법
     */
    public WayOfMovement way;

    /**
     * 소요비용
     */
    public int cost;

    /**
     * @param srcLocation  출발지 POI identifier
     * @param destLocation 도착지 POI identifier
     * @param way          이동방법
     * @param spendingTime 소요시간
     * @param cost         비용
     */
    public UnitMovement(String srcTitle, Location srcLocation, String destTitle, Location destLocation, WayOfMovement way, ProbabilisticDuration spendingTime, int cost) {
        this.srcTitle = srcTitle;
        this.srcLocation = srcLocation;
        this.destTitle = destTitle;
        this.destLocation = destLocation;
        this.spendingTime = spendingTime;
        this.way = way;
        this.cost = cost;
    }

    public UnitMovement(String srcTitle, Location srcLocation, String destTitle, Location destLocation, String wayStr, ProbabilisticDuration spendingTime, int cost) {
        this.srcTitle = srcTitle;
        this.srcLocation = srcLocation;
        this.destTitle = destTitle;
        this.destLocation = destLocation;
        this.spendingTime = spendingTime;
        this.way = parseWay(wayStr);
        this.cost = cost;
    }

    private WayOfMovement parseWay(String wayStr) {
        if (wayStr.equals("Car")) {
            return WayOfMovement.Car;
        } else if (wayStr.equals("Taxi")) {
            return WayOfMovement.Taxi;
        } else if (wayStr.equals("Bus")) {
            return WayOfMovement.Bus;
        } else if (wayStr.equals("Expressbus")) {
            return WayOfMovement.Expressbus;
        } else if (wayStr.equals("Subway")) {
            return WayOfMovement.Subway;
        } else if (wayStr.equals("Train")) {
            return WayOfMovement.Train;
        } else if (wayStr.equals("Airway")) {
            return WayOfMovement.Airway;
        } else if (wayStr.equals("Walk")) {
            return WayOfMovement.Walk;
        } else if (wayStr.equals("Composite")) {
            return WayOfMovement.Composite;
        } else {
            throw new RuntimeException("Not handle for " + wayStr);
        }


    }

    @Override
    public String toString() {

        return srcTitle + " 에서 " + destTitle + " 로 " + way + "를 이용해서 " + spendingTime + "동안 이동하고 비용은 " + cost + "원임";
    }

}
