package server;

import com.google.gson.Gson;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.optimize.MultiDayTripAnswer;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import kakao.Coord;
import kr.hyosang.coordinate.CoordPoint;
import kr.hyosang.coordinate.TransCoord;

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
                private double x;
                private double y;

                private SimplePOI(BasicPOI basicPOI) {
                    id = basicPOI.getId();
                    title = basicPOI.getTitle();
                    poiType = basicPOI.getPoiType();
                    CoordPoint wsgCoord = new CoordPoint(basicPOI.getLocation().longitude, basicPOI.getLocation().latitude);
                    CoordPoint wcongCoord = TransCoord.getTransCoord(wsgCoord, TransCoord.COORD_TYPE_WGS84, TransCoord.COORD_TYPE_WCONGNAMUL);
                    location = new Coord(wcongCoord.x, wcongCoord.y);   //TODO: delete Coord
                    x = wcongCoord.x;
                    y = wcongCoord.y;
                }
            }
        }
    }
}



