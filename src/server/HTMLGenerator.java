package server;

import edu.hanyang.trip_planning.optimize.MultiDayTripAnswer;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.hanyang.trip_planning.optimize.DetailItinerary;

public class HTMLGenerator {
    private static final String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private MultiDayTripAnswer multiDayTripAnswer;

    HTMLGenerator(MultiDayTripAnswer multiDayTripAnswer) {
        this.multiDayTripAnswer = multiDayTripAnswer;
    }

    private String generateHead() {
        StringBuilder head = new StringBuilder();
        head.append("<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">" +
                "    <meta charset=\"utf-8\">" +
                "    <title>Trip Planner</title>" +
                "    <style>" +
                "        html, body {height: 100%;margin: 0; padding: 0;}");
        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            head.append(" #map_canvas").append(i + 1).append("{width: 800px;height: 600px;}");
        }
        head.append("</style></head>");
        return head.toString();
    }

    private String generateBody() {
        StringBuilder body = new StringBuilder();
        body.append("<body>\n");
        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            body.append("<h4>").append(i + 1).append("일차  </h4>\n");
            body.append("<div id=\"map_canvas").append(i + 1).append("\" ></div>\n");
            body.append(multiDayTripAnswer.getItinerary(i).toDetailHTML());
        }
        body.append(generateScript()).append("</body>\n<html>\n");
        return body.toString();
    }

    private String generateScript() {
        StringBuilder script = new StringBuilder();
        script.append("<script>\n" +
                "function initmap() {\n");
        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            DetailItinerary itinerary = multiDayTripAnswer.getItinerary(i);
            script.append(generateScriptForDay(i, itinerary));
        }
        script.append("}\n");
        script.append("</script>\n");
        script.append("<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyD1kXkYitdbTZWd8glOSMoeChJo-gTsK_g&signed_in=true&callback=initmap\"></script>\n");
        return script.toString();
    }

    private String generateScriptForDay(int idx, DetailItinerary itinerary) {
        StringBuilder scriptForDay = new StringBuilder();
        BasicPOI startPOI = itinerary.getStartPOI();

        scriptForDay.append("var poi" + (idx + 1) + "1 = {lat:" + Double.toString(startPOI.getLocation().latitude) + ", lng:" + Double.toString(startPOI.getLocation().longitude) + "};\n");
        int size = itinerary.getPoiTitles().size();
        for (int i = 1; i < size; i++) {
            BasicPOI poi = POIManager.getInstance().getPOIByTitle(itinerary.getPoiTitles().get(i));
            double lat = poi.getLocation().latitude;
            double lng = poi.getLocation().longitude;
            scriptForDay.append("var poi" + (idx + 1) + (i + 1) + " = {lat:" + Double.toString(lat) + ", lng:" + Double.toString(lng) + "};\n");
        }
        scriptForDay.append("  var map" + (idx + 1) + " = new google.maps.Map(document.getElementById('map_canvas" + (idx + 1) + "'), {\n" +
                "    zoom: 10,\n" +
                "    center: poi" + (idx + 1) + "1\n" +
                "  });\n");
        for (int i = 0; i < size; i++) {
            scriptForDay.append("  var marker" + (idx + 1) + (i + 1) + " = new google.maps.Marker({\n" +
                    "    position: poi" + (idx + 1) + (i + 1) + ",\n" +
                    "    map: map" + (idx + 1) + ",\n" +
                    "    label: '" + alphabet[i] + "',\n" +
                    "    title: '" + itinerary.getPoiTitles().get(i) + "'\n" +
                    "  });\n");
        }
        scriptForDay.append("var pathCoord" + (idx + 1) + " = [");
        for (int i = 0; i < size; i++) {
            scriptForDay.append("poi" + (idx + 1) + (i + 1) + ",");
        }
        scriptForDay.append("];\n");
        scriptForDay.append("  var path" + (idx + 1) + " = new google.maps.Polyline({\n" +
                "    path: pathCoord" + (idx + 1) + ",\n" +
                "    geodesic: true,\n" +
                "    strokeColor: '#FF0000',\n" +
                "    strokeOpacity: 1.0,\n" +
                "    strokeWeight: 2\n" +
                "  });\n" +
                "  path" + (idx + 1) + ".setMap(map" + (idx + 1) + ");\n");
        return scriptForDay.toString();
    }

    String generateHTML() {
        return generateHead() + generateBody();
    }
}
