package database;

import java.util.Objects;

class Route {
    private int fromId;
    private int toId;
    private String routeType;
    private double distance;
    private double time;
    private double taxiFare;
    private double tollFare;
    private Coord[] points;

    Route(int fromId, int toId, String routeType, double distance, double time, double taxiFare, Coord[] points) {
        this.fromId = fromId;
        this.toId = toId;
        this.routeType = routeType;
        this.distance = distance;
        this.time = time;
        this.taxiFare = taxiFare;
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
}
