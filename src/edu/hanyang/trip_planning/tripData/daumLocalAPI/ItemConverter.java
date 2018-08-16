package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.tripData.poi.BasicPoi;

public class ItemConverter {
    public static BasicPoi getPoi(Item item) {
        int id = item.getId();
        String title = item.getTitle();
        String address = item.getAddress();
        PoiType poiType = getType(item.getCategory());
        Location location = new Location(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
        String placeUrl = item.getPlaceUrl();

        UpdatePlaceURLInfo updatePlaceURLInfo = new UpdatePlaceURLInfo(placeUrl);
        double score = updatePlaceURLInfo.getScore();

        BasicPoi basicPoi = new BasicPoi(id, title, address, poiType, location, score, placeUrl);

        return basicPoi;
    }

    private static PoiType getType(String categoryStr) {
        String typeArray[] = categoryStr.split(" > ");
        if (typeArray.length == 1) {
            return new PoiType(typeArray[0], null, null);
        } else if (typeArray.length == 2) {
            return new PoiType(typeArray[0], typeArray[1], null);
        } else {
            return new PoiType(typeArray[0], typeArray[1], typeArray[2]);
        }
    }
}
