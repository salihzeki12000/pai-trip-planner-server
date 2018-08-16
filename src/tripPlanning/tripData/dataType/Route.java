package tripPlanning.tripData.dataType;

import java.util.List;
import java.util.Objects;

public class Route {
    private int fromId;
    private int toId;
    private String routeType;       // 'car',
    private double distance;        // '35.8km',
    private int time;               // '43분',
    private int taxiFare;           // '27,200',
    private int tollFare;           // '',
    private List<Location> points;  // wgs84

    public Route(int fromId, int toId, String routeType, double distance, int time, int taxiFare, int tollFare, List<Location> points) {
        this.fromId = fromId;
        this.toId = toId;
        this.routeType = routeType;
        this.distance = distance;
        this.time = time;
        this.taxiFare = taxiFare;
        this.tollFare = tollFare;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return fromId == route.fromId &&
                toId == route.toId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId);
    }

    @Override
    public String toString() {
        return "Route{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", routeType='" + routeType + '\'' +
                ", distance=" + distance +
                ", time=" + time +
                ", taxiFare=" + taxiFare +
                ", tollFare=" + tollFare +
                '}';
    }
}
