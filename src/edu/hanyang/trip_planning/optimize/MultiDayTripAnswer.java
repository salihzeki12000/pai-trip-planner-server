package edu.hanyang.trip_planning.optimize;

import edu.hanyang.trip_planning.tripData.poi.BasicPoi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2016-04-25.
 */
public class MultiDayTripAnswer {
    List<DetailItinerary> itineraryList = new ArrayList<DetailItinerary>();
    List<List<List<BasicPoi>>> nearbyDiningPoiListListList = new ArrayList<>();         // Day - Pois - NearbyPois
    List<List<List<BasicPoi>>> nearbyShoppingPoiListListList = new ArrayList<>();       // Day - Pois - NearbyPois

    public int size() {
        return itineraryList.size();
    }

    public DetailItinerary getItinerary(int idx) {
        return itineraryList.get(idx);
    }

    public void addItinerary(DetailItinerary itinerary) {
        itineraryList.add(itinerary);
    }

    public void setNearbyDiningPoiListListList(List<List<List<BasicPoi>>> nearbyDiningPoiListListList) {
        this.nearbyDiningPoiListListList = nearbyDiningPoiListListList;
    }

    public void setNearbyShoppingPoiListListList(List<List<List<BasicPoi>>> nearbyShoppingPoiListListList) {
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
