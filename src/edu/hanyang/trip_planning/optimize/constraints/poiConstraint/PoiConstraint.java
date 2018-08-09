package edu.hanyang.trip_planning.optimize.constraints.poiConstraint;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

public class PoiConstraint {
    private String poiId;
    private String poiTitle;
    private boolean visitOrNot;

    public PoiConstraint(String inputString, boolean visitOrNot) {
        POIManager poiManager = POIManager.getInstance();
        BasicPOI basicPOI = poiManager.getPOIByTitle(inputString);

        if (basicPOI != null) {
            this.poiId = basicPOI.getId();
            this.poiTitle = basicPOI.getTitle();
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
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("POI Id: " + poiId + "\t");
        strbuf.append("POI Title: " + poiTitle + "\t");
        strbuf.append("VisitOrNot: " + visitOrNot + "\t");
        return strbuf.toString();
    }
}
