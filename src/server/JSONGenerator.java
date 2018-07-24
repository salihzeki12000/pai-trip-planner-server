package server;

import com.google.gson.Gson;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.optimize.MultiDayTripAnswer;
import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import kakaoLocalApi.KakaoLocalApiHelper;
import kakaoLocalApi.Coord;

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
            private double endTime;
            private List<SimplePOI> poiList = new ArrayList<>();
            private List<Double> arrivalTimes = new ArrayList<>();
            private List<Double> durations = new ArrayList<>();
            private List<Double> departureTimes = new ArrayList<>();
            private List<Double> costs = new ArrayList<>();

            private DailyItinerary(DetailItinerary detailItinerary) {
                date = detailItinerary.getDate();
                startTime = detailItinerary.getStartTime();
                endTime = detailItinerary.getEndTime()[0];

                poiList.add(new SimplePOI(detailItinerary.getStartPOI()));
                for (BasicPOI basicPOI : detailItinerary.getPoiList()) {
                    poiList.add(new SimplePOI(basicPOI));
                }
                poiList.add(new SimplePOI(detailItinerary.getEndPOI()));

                for (double[] arrivalTime : detailItinerary.getArrivalTimes()) {
                    arrivalTimes.add(arrivalTime[0]);
                }
                for (double[] duration : detailItinerary.getDurations()) {
                    durations.add(duration[0]);
                }
                for (double[] departureTime : detailItinerary.getDepartureTimes()) {
                    departureTimes.add(departureTime[0]);
                }
                for (double[] cost : detailItinerary.getCosts()) {
                    costs.add(cost[0]);
                }
            }

            private class SimplePOI {
                private String id;
                private String title;
                private POIType poiType;
                private Coord location;

                private SimplePOI(BasicPOI basicPOI) {
                    id = basicPOI.getId();
                    title = basicPOI.getTitle();
                    poiType = basicPOI.getPoiType();
                    location = KakaoLocalApiHelper.transcoord(basicPOI.getLocation().longitude, basicPOI.getLocation().latitude, "WGS84", "WCONGNAMUL");
                }
            }
        }
    }
}



