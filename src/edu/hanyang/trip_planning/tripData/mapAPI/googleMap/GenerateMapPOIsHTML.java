package edu.hanyang.trip_planning.tripData.mapAPI.googleMap;

import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.hanyang.trip_planning.tripHTBN.poi.POITitleList;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2015-12-20.
 */
public class GenerateMapPOIsHTML {
    private String alphabet[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final String str1 = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\">\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>Simple markers</title>\n" +
            "    \n" +
            "    <style>\n" +
            "      html, body {\n" +
            "        height: 100%;\n" +
            "        margin: 0;\n" +
            "        padding: 0;\n" +
            "      }\n" +
            "      #map {\n" +
            "        height: 80%;\n" +
            "      }\n" +
            "    </style>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div id=\"map\"></div>\n" ;

    private String str2 = "</script>\n" +
            "<script async defer src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyD1kXkYitdbTZWd8glOSMoeChJo-gTsK_g&signed_in=true&callback=initMap\"></script>";
    private String str3 = "\n</body>\n" +
            "</html>";
    private List<Marker> markerList = new ArrayList<Marker>();

    private List<String> textList = new ArrayList<String>();

    private String ttsText = null;

    public void addMarker(Marker marker) {
        markerList.add(marker);
    }

    public void addText(String text) {
        textList.add(text);
    }

    public void addMarkers(String... poiTitles) {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < poiTitles.length; i++) {
            if (i == poiTitles.length - 1 && poiTitles[0].equals(poiTitles[poiTitles.length - 1])) {
//                break;
            }
            markerList.add(new Marker(POIManager.getInstance().getPOIByTitle(poiTitles[i])));
            strbuf.append(poiTitles[i] + "->");
        }
        strbuf.deleteCharAt(strbuf.length() - 1);
        strbuf.deleteCharAt(strbuf.length() - 1);
//        textList.add(strbuf.toString());
    }

    public void addTTSText(String ttsText) {
        this.ttsText = ttsText;
    }

    public void addMarkers(List<String> poiTitles) {
        StringBuffer strbuf = new StringBuffer();
        for (int i = 0; i < poiTitles.size(); i++) {

            if (i == poiTitles.size() - 1 && poiTitles.get(0).equals(poiTitles.get(poiTitles.size() - 1))) {
//                break;
            }
            markerList.add(new Marker(POIManager.getInstance().getPOIByTitle(poiTitles.get(i))));
            strbuf.append(poiTitles.get(i) + "->");
        }
        strbuf.deleteCharAt(strbuf.length() - 1);
        strbuf.deleteCharAt(strbuf.length() - 1);
//        textList.add(strbuf.toString());
    }

    private String generateFunctionStr() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("    <script>\n");
        strbuf.append("function initMap() {\n");
        for (int i = 0; i < markerList.size(); i++) {
            strbuf.append("var poi" + i + "={lat: " + markerList.get(i).latitude + ", lng:" + markerList.get(i).longitude + "};\n");
        }

        strbuf.append("// map 초기화\n" +
                "  var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "    zoom: 10,\n" +
                "    center: poi1\n" +
                "  });\n");

        for (int i = 0; i < markerList.size(); i++) {
            strbuf.append("  var marker" + i + " = new google.maps.Marker({\n" +
                    "    position: poi" + i + ",\n" +
                    "    map: map,\n" +
                    "    label: '" + (i + 1) + "',\n" +
                    "    title: '" + markerList.get(i).name + "'\n" +
                    "  });\n");
        }

        strbuf.append("var pathCoord = [");
        for (int i = 0; i < markerList.size(); i++) {
            strbuf.append("poi" + i + ",");
        }
        strbuf.append("];\n");
        strbuf.append("  var path = new google.maps.Polyline({\n" +
                "    path: pathCoord,\n" +
                "    geodesic: true,\n" +
                "    strokeColor: '#FF0000',\n" +
                "    strokeOpacity: 1.0,\n" +
                "    strokeWeight: 2\n" +
                "  });\n" +
//                "  path.setMap(map);\n" +
                "}\n");
        return strbuf.toString();
    }

    private String generateTTSStr(){
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("<script>\n");
        strbuf.append("var ttsText = \""+ttsText+ "\";\n");
        strbuf.append("window.console.log(ttsText);\n");
        strbuf.append("Android.convertTextToSpeech(ttsText);\n");
        strbuf.append("</script>");
        return strbuf.toString();
    }

    public String generateMapHTMLStr() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(str1);
        strbuf.append(generateFunctionStr());
        strbuf.append(str2);

        if (ttsText!=null) {
            strbuf.append(generateTTSStr());
        }
        strbuf.append("\n <p>\n");
        for (String text : textList) {
            strbuf.append("\n"+text + "<br>\n");
        }
        strbuf.append("</p>\n");
        strbuf.append(str3);
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
        test1();
//
//        POIManager poiManager = POIManager.getInstance();
//        GenerateMapPathHTML gen = new GenerateMapPathHTML();
//
//        ;
//        gen.addMarker(new Marker(poiManager.getPOIByTitle(POITitleList.jeju25[0])));
//        gen.addMarker(new Marker(poiManager.getPOIByTitle(POITitleList.jeju25[1])));
//        gen.addMarker(new Marker(poiManager.getPOIByTitle(POITitleList.jeju25[2])));
//        gen.addText("텍스트");
//
//        System.out.println(gen.generateMapHTMLStr());
    }

    public static void test1() {


        String strs[] = {"제주신라호텔", "제주신라호텔", "외돌개", "새섬", "중문색달해변", "대포주상절리", "테디베어뮤지엄 제주점", "퍼시픽랜드 돌고래쇼", "산방산사랑의유람선", "제주신라호텔"};

        GenerateMapPOIsHTML generateHTML = new GenerateMapPOIsHTML();
        generateHTML.addMarkers(POITitleList.randomTitle(5));
        generateHTML.generateMapHTMLFile("d:/map.html");

    }
}
