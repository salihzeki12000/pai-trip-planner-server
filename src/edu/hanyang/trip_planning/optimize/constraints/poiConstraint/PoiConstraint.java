package edu.hanyang.trip_planning.optimize.constraints.poiConstraint;

import edu.hanyang.trip_planning.tripData.poi.BasicPoi;
import edu.hanyang.trip_planning.tripData.poi.PoiManager;

public class PoiConstraint {
    private int poiId;
    private String poiTitle;
    private boolean visitOrNot;

    PoiConstraint(String inputString, boolean visitOrNot) {
        PoiManager poiManager = PoiManager.getInstance();
        BasicPoi basicPoi = poiManager.getPoiByTitle(inputString);

        if (basicPoi != null) {
            this.poiId = basicPoi.getId();
            this.poiTitle = basicPoi.getName();
            this.visitOrNot = visitOrNot;
        } else {
            throw new RuntimeException("The corresponding POI does not exist:" + inputString);
        }
    }

    public int getPoiId() {
        return poiId;
    }

    public String getPoiTitle() {
        return poiTitle;
    }

    public boolean isVisitOrNot() {
        return visitOrNot;
    }

    @Override
    public String toString() {
        return "PoiConstraint{" +
                "poiId='" + poiId + '\'' +
                ", poiTitle='" + poiTitle + '\'' +
                ", visitOrNot=" + visitOrNot +
                '}';
    }
}
