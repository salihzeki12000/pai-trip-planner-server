package server;

import com.google.gson.Gson;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.optimize.MultiDayTripAnswer;
import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;

import java.util.ArrayList;
import java.util.List;

class JSONGenerator {
    private MultiDayTripAnswer multiDayTripAnswer;

    JSONGenerator(MultiDayTripAnswer multiDayTripAnswer) {
        this.multiDayTripAnswer = multiDayTripAnswer;
    }

    String generateJSON() {
        Gson gson = new Gson();
        return gson.toJson(new TripPlan(multiDayTripAnswer));
    }


    class TripPlan {
        private List<DailyItinerary> dailyItineraries = new ArrayList<>();

        private TripPlan(MultiDayTripAnswer multiDayTripAnswer) {
            for (DetailItinerary detailItinerary : multiDayTripAnswer.getItineraryList()) {
                dailyItineraries.add(new DailyItinerary(detailItinerary));
            }
        }

        class DailyItinerary {
            private String date;
            private double startTime;
            private double endTime[];
            private SimplePOI startPOI;
            private SimplePOI endPOI;
            private List<SimplePOI> poiList = new ArrayList<>();
            private List<double[]> arrivalTimes;
            private List<double[]> durations;
            private List<double[]> departureTimes;
            private List<double[]> costs;

            private DailyItinerary(DetailItinerary detailItinerary) {
                date = detailItinerary.getDate();
                startPOI = new SimplePOI(detailItinerary.getStartPOI());
                endPOI = new SimplePOI(detailItinerary.getEndPOI());
                startTime = detailItinerary.getStartTime();
                endTime = detailItinerary.getEndTime();
                for (BasicPOI basicPOI : detailItinerary.getPoiList()) {
                    poiList.add(new SimplePOI(basicPOI));
                }
                arrivalTimes = detailItinerary.getArrivalTimes();
                durations = detailItinerary.getDurations();
                departureTimes = detailItinerary.getDepartureTimes();
                costs = detailItinerary.getCosts();
            }

            private class SimplePOI {
                private String id;
                private String title;
                private POIType poiType;
                private Location location;

                private SimplePOI(BasicPOI basicPOI) {
                    id = basicPOI.getId();
                    title = basicPOI.getTitle();
                    poiType = basicPOI.getPoiType();
                    location = basicPOI.getLocation();
                }
            }
        }
    }
}



