package edu.hanyang.trip_planning.tripData.daumLocalAPI;


import edu.hanyang.trip_planning.tripData.dataType.Address;
import edu.hanyang.trip_planning.tripData.dataType.AddressCode;
import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 15
 * Time: 오후 1:05
 * To change this template use File | Settings | File Templates.
 */
public class ItemConverter {

    private static Logger logger = Logger.getLogger(ItemConverter.class);

    public static BasicPOI getPOI(Item item) {

        Location location = new Location(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
        String id = "daum." + item.getId();
        BasicPOI basicPOI = new BasicPOI(id, item.getTitle(), location);
        basicPOI.setAddress(getAddress(item.getAddress()));
        POIType poiType = getType(item.getCategory());
        basicPOI.setPoiType(poiType);
        basicPOI.addURL("place", item.getPlaceUrl());

        UpdatePlaceURLInfo updatePlaceURLInfo = new UpdatePlaceURLInfo(item.getPlaceUrl());
        basicPOI.setBusinessHour(updatePlaceURLInfo.businessTime());
        basicPOI.setScore(updatePlaceURLInfo.getSatisfication());
        basicPOI.setClosingDays(updatePlaceURLInfo.cLosingDays());

        return basicPOI;
    }

    public static List<BasicPOI> getPOIList(List<Item> itemList) {
        List<BasicPOI> poiList = new ArrayList<BasicPOI>();
        for (Item item : itemList) {
            poiList.add(getPOI(item));
        }
        return poiList;
    }

    public static Set<BasicPOI> getPOISet(ItemList itemList) {

        Set<BasicPOI> poiSet = new HashSet<BasicPOI>();
        Iterator<Item> it = itemList.getItemList().iterator();


        while (it.hasNext()) {
            Item item = it.next();
            Location location = new Location(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
            String id = "daum." + item.getId();
            BasicPOI basicPOI = new BasicPOI(id, item.getTitle(), location);
            basicPOI.setAddress(getAddress(item.getAddress()));


            POIType poiType = getType(item.getCategory());
            basicPOI.setPoiType(poiType);
//            basicPOI.setLocation(item.getLatitude(), item.getLongitude());
            basicPOI.addURL("place", item.getPlaceUrl());
//            logger.debug(basicPOI);
//            logger.debug(item.getPlaceUrl());
//            logger.debug(basicPOI.getId() + "\t" + basicPOI.getTitle());
            poiSet.add(basicPOI);


        }

        return poiSet;
    }

    public static Map<String, BasicPOI> getPOIMap(ItemList itemList) {
        Map<String, BasicPOI> poiMap = new HashMap<String, BasicPOI>();
        Iterator<Item> it = itemList.getItemList().iterator();

        while (it.hasNext()) {
            Item item = it.next();
            Location location = new Location(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
            String id = "daum." + item.getId();

            BasicPOI basicPOI = new BasicPOI(id, item.getTitle(), location);

            basicPOI.setAddress(getAddress(item.getAddress()));

            POIType poiType = getType(item.getCategory());
            basicPOI.setPoiType(poiType);
            basicPOI.addURL("place", item.getPlaceUrl());
            logger.debug(basicPOI.getId() + "\t" + basicPOI.getTitle());
            poiMap.put(basicPOI.getId(), basicPOI);
        }
        return poiMap;
    }

    private static POIType getType(String categoryStr) {
        String typeArray[] = categoryStr.split(" > ");
        if (typeArray.length == 1) {
            return new POIType(typeArray[0], null, null);
        } else if (typeArray.length == 2) {
            return new POIType(typeArray[0], typeArray[1], null);
        } else {
            return new POIType(typeArray[0], typeArray[1], typeArray[2]);
        }
    }

    private static Address getAddress(String addressStr) {
        String addressStrArray[] = addressStr.split(" ");

        Address address = null;
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
//            logger.debug(strbuf.toString());
            return new Address(addressCode, strbuf.toString());

        } else {
            throw new RuntimeException("error with address " + addressStr);
        }

    }
}
