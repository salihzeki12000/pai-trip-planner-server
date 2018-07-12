package edu.hanyang.trip_planning.tripData.mapAPI.googleMap;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import edu.hanyang.trip_planning.optimize.DetailItinerary;
import org.apache.log4j.Logger;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by wykwon on 2015-12-20.
 */
public class GenerateMultiMapPathHTML {
    private String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private MultiDayTripAnswer multiDayTripAnswer;
    private static Logger logger = Logger.getLogger(GenerateMultiMapPathHTML.class);

//
//    private String str2 = "</script>\n" +
//            "<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyD1kXkYitdbTZWd8glOSMoeChJo-gTsK_g&signed_in=true&callback=initMap\"></script>";
//    private String closeStr = "</body>\n" +
//            "</html>";

    public String generateHead() {
        StringBuffer strbuf = new StringBuffer();
        String headStr = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Trip Planner</title>\n" +
                "\n" +
                "    <style>\n" +
                "        html, body {\n" +
                "        height: 100%;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "        }\n";

        strbuf.append(headStr);

        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            strbuf.append(" #map_canvas" + (i + 1) + "{\n");
            strbuf.append(
                    "\t\t\t\twidth: 800px;\n" +
                            "        height: 600px;\n" +
                            "        }\n");
        }
        strbuf.append("    </style>\n" +
                "</head>\n");
        return strbuf.toString();
    }

    public String generateBody() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("<body>\n");
        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            strbuf.append("<h4>" + (i + 1) + "일차  </h4>\n");
            strbuf.append("<div id=\"map_canvas" + (i + 1) + "\" ></div>\n");
            strbuf.append(multiDayTripAnswer.getItinerary(i).toDetailHTML());
        }

        return strbuf.toString();
    }

    public String generateScript() {
        StringBuffer strbuf = new StringBuffer();

        strbuf.append("<script>\n");
        strbuf.append("function initmap() {\n");

        for(int i=0; i<multiDayTripAnswer.size();i++){
            DetailItinerary itinerary = multiDayTripAnswer.getItinerary(i);
            strbuf.append(generateScriptForDay(i,itinerary));
        }

        strbuf.append("}\n");
        strbuf.append("</script>\n");
        strbuf.append("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyD1kXkYitdbTZWd8glOSMoeChJo-gTsK_g&signed_in=true&callback=initmap\"></script>\n");

        return strbuf.toString();
    }


    public String generateScriptForDay(int idx, DetailItinerary itinerary) {
        StringBuffer strbuf = new StringBuffer();
        BasicPOI startPOI = itinerary.getStartPOI();

        strbuf.append("var poi"+(idx+1) +"1 = {lat:"+ Double.toString(startPOI.getLocation().latitude) + ", lng:"+Double.toString(startPOI.getLocation().longitude) + "};\n"   );
        int size = itinerary.getPoiTitles().size();
        for (int i=1; i<size; i++){
            BasicPOI poi= POIManager.getInstance().getPOIByTitle(itinerary.getPoiTitles().get(i));
            double lat = poi.getLocation().latitude;
            double lng = poi.getLocation().longitude;
            strbuf.append("var poi"+(idx+1) +(i+1)+" = {lat:"+ Double.toString(lat) + ", lng:"+Double.toString(lng) + "};\n"   );
        }


        strbuf.append("  var map"+(idx+1)+" = new google.maps.Map(document.getElementById('map_canvas"+(idx+1)+"'), {\n" +
                "    zoom: 10,\n" +
                "    center: poi"+(idx+1)+"1\n" +
                "  });\n");


        for (int i=0; i<size; i++){
            strbuf.append("  var marker"+ (idx+1) +(i+1)+" = new google.maps.Marker({\n" +
                    "    position: poi"+(idx+1) +(i+1)+",\n" +
                    "    map: map"+(idx+1)+",\n" +
                    "    label: '"+alphabet[i]+"',\n" +
                    "    title: '"+ itinerary.getPoiTitles().get(i)+"'\n" +
                    "  });\n");
        }

        strbuf.append("var pathCoord"+(idx+1)+" = [");
        for (int i=0; i<size; i++){
            strbuf.append("poi"+(idx+1) +(i+1) + ",");
        }
        strbuf.append("];\n");
        strbuf.append("  var path"+(idx+1)+" = new google.maps.Polyline({\n" +
                "    path: pathCoord"+(idx+1)+",\n" +
                "    geodesic: true,\n" +
                "    strokeColor: '#FF0000',\n" +
                "    strokeOpacity: 1.0,\n" +
                "    strokeWeight: 2\n" +
                "  });\n" +
                "  path"+(idx+1)+".setMap(map"+(idx+1)+");\n");

        return strbuf.toString();
    }

    public String generateEndStr() {
        return "</body>\n<html>\n";
    }

    public GenerateMultiMapPathHTML(MultiDayTripAnswer multiDayTripAnswer) {
        this.multiDayTripAnswer = multiDayTripAnswer;
    }

    public String generateMapHTMLStr() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(generateHead());
        strbuf.append(generateBody());
        strbuf.append(generateScript());

        strbuf.append(generateEndStr());
        return strbuf.toString();
    }


    public void generateMapHTMLFile(String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(generateMapHTMLStr());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
////        logger.debug(multiDayTripAnswer);
//
//        GenerateMultiMapPathHTML generateMultiMapPathHTML = new GenerateMultiMapPathHTML(multiDayTripAnswer);
//        logger.debug(generateMultiMapPathHTML.generateMapHTMLStr());

    }
}
