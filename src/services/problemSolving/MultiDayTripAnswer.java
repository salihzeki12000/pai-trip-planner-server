package services.problemSolving;


import edu.hanyang.trip_planning.optimize.DetailItinerary;
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

    List<DetailItinerary> itineraryList = new ArrayList<DetailItinerary>();

    public int size(){
        return itineraryList.size();
    }
    public DetailItinerary getItinerary(int idx){
        return itineraryList.get(idx);
    }
    private static Logger logger = Logger.getLogger(MultiDayTripAnswer.class);
//    public MultiDayTripAnswer(String srcPOIName, String sinkPOIName, String hotel) {
//        this.srcPOIName = srcPOIName;
//        this.sinkPOIName = sinkPOIName;
//        this.hotel = hotel;
//        itineraryList = new ArrayList<OneDayItinerary>();
//    }

    public void addItinerary(DetailItinerary itinerary) {
        itineraryList.add(itinerary);
    }


    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (DetailItinerary itinerary : itineraryList) {
            strbuf.append(itinerary.toDetailHTML() + "\n");
        }
        return strbuf.toString();
    }


    public static void main(String[] args) {

//        OneDayItinerary oneDayItinerary = new OneDayItinerary("2016-04-25", POIManager.getInstance().getPOIByTitle("제주신라호텔"), 9.00);
//        oneDayItinerary.addEvent(POIManager.getInstance().getPOIByTitle("한화아쿠아플라넷 제주"), 10.0, 1.0);
//        oneDayItinerary.addEvent(POIManager.getInstance().getPOIByTitle("환상숲곶자왈공원"), 12.0, 1.0);
//        oneDayItinerary.addEvent(POIManager.getInstance().getPOIByTitle("제주신라호텔"), 15.0, 0);
//
//
//        OneDayItinerary oneDayItinerary1 = new OneDayItinerary("2016-04-26", POIManager.getInstance().getPOIByTitle("제주신라호텔"), 9.00);
//        oneDayItinerary1.addEvent(POIManager.getInstance().getPOIByTitle("한화아쿠아플라넷 제주"), 10.0, 1.0);
//        oneDayItinerary1.addEvent(POIManager.getInstance().getPOIByTitle("환상숲곶자왈공원"), 12.0, 1.0);
//        oneDayItinerary1.addEvent(POIManager.getInstance().getPOIByTitle("제주국제공항"), 15.0, 0);
//
//
//        MultiDayTripAnswer multiDayTripAnswer = new MultiDayTripAnswer();
//        multiDayTripAnswer.addItinerary(oneDayItinerary);
//        multiDayTripAnswer.addItinerary(oneDayItinerary1);
//        logger.debug(multiDayTripAnswer);

    }

}
