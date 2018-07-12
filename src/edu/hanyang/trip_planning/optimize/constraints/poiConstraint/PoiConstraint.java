package edu.hanyang.trip_planning.optimize.constraints.poiConstraint;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;

// mgkim:
public class PoiConstraint {
    private static Logger logger = Logger.getLogger(CategoryConstraint.class);
    private String poiId;
    private String poiTitle;
    private boolean visitOrNot;

    public PoiConstraint(String inputString, boolean visitOrNot) {
        POIManager poiManager = POIManager.getInstance();
        BasicPOI basicPOI;
        if (inputString.contains("daum")) {
            basicPOI = poiManager.getPOIByID(inputString);
        } else {
            basicPOI = poiManager.getPOIByTitle(inputString);
        }

        if (basicPOI != null) {
            this.poiId  = basicPOI.getId();
            this.poiTitle  = basicPOI.getTitle();
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

    public static void test() {
        PoiConstraint poiConstraint1 = new PoiConstraint("제주국제공항",true);
        PoiConstraint poiConstraint2 = new PoiConstraint("daum.8577982", false);
//        PoiConstraint poiConstraint3 = new PoiConstraint("daum.123456", false);
        logger.debug(poiConstraint1);
        logger.debug(poiConstraint2);
    }

    public static void main(String[] args) {
        test();
    }
}
