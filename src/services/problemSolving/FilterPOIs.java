package services.problemSolving;


import edu.hanyang.trip_planning.tripData.dataType.AddressCode;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import services.qna.SpatialConstraints;
import services.qna.SpatialStatementByLocation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wykwon on 2016-01-06.
 */
public class FilterPOIs {


    public static Set<BasicPOI> filterPOIs(Collection<BasicPOI> basicPOIs, SpatialConstraints spatialConstraints, POIType poiType) {
        // 일단 주소가 딱 맞는넘 먼저 골라라.
        Set<AddressCode> addressCodeSet = spatialConstraints.symbolicSpatialConstraints();
        Set<SpatialStatementByLocation> spatialStatementByLocationSet = spatialConstraints.metricSpatialConstraints();
        if (addressCodeSet.size() > 0) {
            // 둘다 존재할 경우 and 처리
            if (spatialStatementByLocationSet.size() > 0) {
                Set<BasicPOI> firstSet = filterPOIsByAddress(basicPOIs, addressCodeSet, poiType);
                Set<BasicPOI> secondSet = filterPOIsByMetric(firstSet, spatialStatementByLocationSet, poiType);
                return secondSet;
            } else {
                return filterPOIsByAddress(basicPOIs, addressCodeSet, poiType);
            }

        } else {
            if (spatialStatementByLocationSet.size() > 0) {
                return filterPOIsByMetric(basicPOIs, spatialStatementByLocationSet, poiType);
            } else {
                // 아무것도 없으면?
                Set<BasicPOI> filteredSet = new HashSet<BasicPOI>();
                // poiType만 처리할것
                for (BasicPOI basicPOI : basicPOIs) {
                    if (poiType.contain(basicPOI.getPoiType())) {
                        filteredSet.add(basicPOI);
                    }
                }
                return filteredSet;

            }

        }
    }

    public static Set<BasicPOI> filterPOIsByAddress(Collection<BasicPOI> basicPOIs, Set<AddressCode> addressCodeSet, POIType poiType) {

        // 일단 주소가 딱 맞는넘 먼저 골라라.

        Set<BasicPOI> filteredSet = new HashSet<BasicPOI>();
        for (BasicPOI basicPOI : basicPOIs) {
            // 해당영역 내에 있으면...
            boolean bFlag = true;
            for (AddressCode addressCode : addressCodeSet) {
                if (!addressCode.contain(basicPOI.getAddress().addressCode)) {
                    bFlag = false;
                }
            }
            if (poiType != null) {
                if (!poiType.contain(basicPOI.getPoiType())) {
                    bFlag = false;
                }
            }

            if (bFlag) {
                filteredSet.add(basicPOI);
            }
        }
        return filteredSet;
    }

    public static Set<BasicPOI> filterPOIsByMetric(Collection<BasicPOI> basicPOIs, Set<SpatialStatementByLocation> metricSpatialConstraints, POIType poiType) {

        // 일단 주소가 딱 맞는넘 먼저 골라라.
        Set<BasicPOI> filteredSet = new HashSet<BasicPOI>();
        for (SpatialStatementByLocation spatialStatementByLocation : metricSpatialConstraints) {
            BasicPOI refPOI = POIManager.getInstance().getPOIByTitle(spatialStatementByLocation.poiTitle);
            Set<BasicPOI> nearBySet = getNearby(basicPOIs, refPOI, spatialStatementByLocation.distance, poiType);
            filteredSet.addAll(nearBySet);
        }

        return filteredSet;
    }

    public static Set<BasicPOI> getNearby(Set<BasicPOI> poiSet, BasicPOI refPOI, double range) {
        Set<BasicPOI> nearSet = new HashSet<BasicPOI>();
        for (BasicPOI poi : poiSet) {
            double dist = POIManager.getInstance().distance(poi, refPOI);
            if (dist < range) {
                nearSet.add(poi);
            }
        }
        return nearSet;
    }

    public static Set<BasicPOI> getNearby(Collection<BasicPOI> poiSet, BasicPOI refPOI, double range, POIType poiType) {
        Set<BasicPOI> nearSet = new HashSet<BasicPOI>();
        for (BasicPOI poi : poiSet) {
            double dist = POIManager.getInstance().distance(poi, refPOI);
            if (dist < range && poiType.contain(poi.getPoiType())) {
                nearSet.add(poi);
            }
        }
        return nearSet;
    }


}
