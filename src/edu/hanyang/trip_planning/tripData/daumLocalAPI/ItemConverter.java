package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.tripData.poi.BasicPoi;

public class ItemConverter {
    public static BasicPoi getPoi(Item item) {
        String id = "daum." + item.getId();
        String title = item.getTitle();
        Address address = getAddress(item.getAddress());
        PoiType poiType = getType(item.getCategory());
        Location location = new Location(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
        String placeUrl = item.getPlaceUrl();

        UpdatePlaceURLInfo updatePlaceURLInfo = new UpdatePlaceURLInfo(placeUrl);
        double score = updatePlaceURLInfo.getScore();
        BusinessHour businessHour = updatePlaceURLInfo.getBusinessTime();
        ClosingDays closingDays = updatePlaceURLInfo.getClosingDays();

        BasicPoi basicPoi = new BasicPoi(id, title, address, poiType, location, businessHour, closingDays, score, placeUrl);

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

    private static Address getAddress(String addressStr) {
        String addressStrArray[] = addressStr.split(" ");

        if (addressStrArray.length == 2) {
            AddressCode addressCode = new AddressCode("대한민국", addressStrArray[0], addressStrArray[1]);
            return new Address(addressCode, "");
        } else if (addressStrArray.length == 3) {
            AddressCode addressCode = new AddressCode("대한민국", addressStrArray[0], addressStrArray[1]);
            return new Address(addressCode, addressStrArray[2]);
        } else if (addressStrArray.length > 3) {
            AddressCode addressCode = new AddressCode("대한민국", addressStrArray[0], addressStrArray[1]);
            StringBuffer strbuf = new StringBuffer();
            for (int i = 2; i < addressStrArray.length; i++) {
                strbuf.append(addressStrArray[i] + " ");
            }
            return new Address(addressCode, strbuf.toString());
        } else {
            throw new RuntimeException("error with address " + addressStr);
        }

    }
}
