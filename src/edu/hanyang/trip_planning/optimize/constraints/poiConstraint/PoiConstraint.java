package edu.hanyang.trip_planning.optimize.constraints.poiConstraint;

import edu.hanyang.trip_planning.tripData.poi.BasicPoi;
import edu.hanyang.trip_planning.tripData.poi.PoiManager;

public class PoiConstraint {
    private String poiId;
    private String poiTitle;
    private boolean visitOrNot;

    public PoiConstraint(String inputString, boolean visitOrNot) {
        PoiManager poiManager = PoiManager.getInstance();
        BasicPoi basicPoi = poiManager.getPoiByTitle(inputString);

        if (basicPoi != null) {
            this.poiId = basicPoi.getId();
            this.poiTitle = basicPoi.getTitle();
            this.visitOrNot = visitOrNot;
        } else {
            throw new RuntimeException("The corresponding POI does not exist:" + inputString);
        }
    }

    public String getPoiId() {
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
