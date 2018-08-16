package server;

import com.google.gson.Gson;
import tripPlanning.optimize.DetailItinerary;
import tripPlanning.optimize.MultiDayTripAnswer;
import tripPlanning.tripData.dataType.PoiType;
import tripPlanning.tripData.poi.BasicPoi;
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
            private List<SimplePoi> poiList = new ArrayList<>();
            private List<Double> arrivalTimes = new ArrayList<>();
            private List<Double> durations = new ArrayList<>();
            private List<Double> departureTimes = new ArrayList<>();
            private List<Double> costs = new ArrayList<>();

            private DailyItinerary(DetailItinerary detailItinerary) {
                date = detailItinerary.getDate();
                startTime = detailItinerary.getStartTime();
                endTime = detailItinerary.getEndTime()[0];

                poiList.add(new SimplePoi(detailItinerary.getStartPoi()));
                for (BasicPoi basicPoi : detailItinerary.getPoiList()) {
                    poiList.add(new SimplePoi(basicPoi));
                }
                poiList.add(new SimplePoi(detailItinerary.getEndPoi()));

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

            private class SimplePoi {
                private int id;
                private String name;
                private PoiType poiType;
                private double x;
                private double y;

                private SimplePoi(BasicPoi basicPoi) {
                    id = basicPoi.getId();
                    name = basicPoi.getName();
                    poiType = basicPoi.getPoiType();
                    CoordPoint wsgCoord = new CoordPoint(basicPoi.getLocation().longitude, basicPoi.getLocation().latitude);
                    CoordPoint wcoCoord = TransCoord.getTransCoord(wsgCoord, TransCoord.COORD_TYPE_WGS84, TransCoord.COORD_TYPE_WCONGNAMUL);
                    x = wcoCoord.x;
                    y = wcoCoord.y;
                }
            }
        }
    }
}



