package edu.hanyang.trip_planning.optimize;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2016-04-25.
 */
public class MultiDayTripAnswer {
//    // 도착하는 장소
//    String srcPOIName;
//    // 떠나는 장소
//    String sinkPOIName;
//    // 머무는 장소
//    String hotel;
    private static Logger logger = Logger.getLogger(MultiDayTripAnswer.class);

    List<DetailItinerary> itineraryList = new ArrayList<DetailItinerary>();
    List<List<List<BasicPOI>>> nearbyDiningPoiListListList = new ArrayList<>();         // Day - Pois - NearbyPois
    List<List<List<BasicPOI>>> nearbyShoppingPoiListListList = new ArrayList<>();       // Day - Pois - NearbyPois

    public int size(){
        return itineraryList.size();
    }
    public DetailItinerary getItinerary(int idx){
        return itineraryList.get(idx);
    }
//    public MultiDayTripAnswer(String srcPOIName, String sinkPOIName, String hotel) {
//        this.srcPOIName = srcPOIName;
//        this.sinkPOIName = sinkPOIName;
//        this.hotel = hotel;
//        itineraryList = new ArrayList<OneDayItinerary>();
//    }

    public void addItinerary(DetailItinerary itinerary) {
        itineraryList.add(itinerary);
    }

    public void setNearbyDiningPoiListListList(List<List<List<BasicPOI>>> nearbyDiningPoiListListList) {
        this.nearbyDiningPoiListListList = nearbyDiningPoiListListList;
    }

    public void setNearbyShoppingPoiListListList(List<List<List<BasicPOI>>> nearbyShoppingPoiListListList) {
        this.nearbyShoppingPoiListListList = nearbyShoppingPoiListListList;
    }

    public List<DetailItinerary> getItineraryList() {
        return itineraryList;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (DetailItinerary itinerary : itineraryList) {
            strbuf.append(itinerary.toDetailHTML() + "\n");
        }
        return strbuf.toString();
    }
}
