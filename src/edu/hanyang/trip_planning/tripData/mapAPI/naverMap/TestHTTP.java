package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 12. 23
 * Time: 오후 1:58
 * To change this template use File | Settings | File Templates.
 */
public class TestHTTP {
    private static Logger logger = Logger.getLogger(TestHTTP.class);
//    http://maps.google.com/maps/api/directions/json?origin=37.631886,127.0622051&destination=37.556173,127.048483&sensor=false
    //     37.631886,127.0622051
    // 37.556173, 127.048483


    public static String naverMapSearchRequest(String srcLatitude, String srcLongitude, String destLatitude, String destLongitude) {
        String str = "http://map.naver.com/findroute2/findPubTransRoute.nhn?&output=json&start=" + srcLongitude + "%2C" + srcLatitude + "%2C%20&destination=" + destLongitude + "%2C" + destLatitude + "%2C%20";
        try {
            return requestHTTP(str);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static String requestHTTP(String str) throws IOException {

//        URL url = new URL("http://naverMap.daum.net/?eX=501220&eY=1121570&sName=한양대학교");
//        URL url = new URL("http://naverMap.naver.com/findroute2/findCarRoute.nhn?via=&call=route2&output=json&car=0&mileage=12.4&start=127.0622051%2C37.556173%2C%20&destination=127.048483%2C37.556173%2C%20&search=2");
//  된다 된다.
//        URL url = new URL("http://naverMap.naver.com/findroute2/findPubTransRoute.nhn?&output=json&start=127.0622051%2C37.556173%2C%20&destination=127.048483%2C37.556173%2C%20&search=10");
    logger.debug(str);
        URL url = new URL(str);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

//        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("src/naverMap/example.json")));

//        String jsonStr = in.readLine();
        JsonReader jsonReader = new JsonReader(in);

        JsonStreamParser jsonStreamParser = new JsonStreamParser(in);

//        ParseNaverMapJson parseNaverMapJson = new ParseNaverMapJson(jsonStreamParser);

//        parseNaverMapJson.parse();
////        logger.debug(jsonStr);
//        Gson gson = new GsonBuilder().create();
////        NaverMapJson naverMapJson =gson.fromJson(in,NaverMapJson.class);;
//
////        logger.debug(naverMapJson);
//
//
//        while(jsonStreamParser.hasNext()){
//            JsonElement element = jsonStreamParser.next();
//            JsonElement result= element.getAsJsonObject().get("result");
//            JsonArray paths= result.getAsJsonObject().get("path").getAsJsonArray();
//            for (int i=0; i<paths.size();i++){
//                JsonElement path = paths.get(i);
//                logger.debug(path.getAsJsonObject());
//            }
////            logger.debug(array.size());
//
//            logger.debug(paths);
//        }
//
//            JsonObject jobject = element.getAsJsonObject();
//            logger.debug(jobject);
////            element.
//        }
//        System.out.println(jsonReader);
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
        return null;
    }

    public static String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        127.0622051%2C37.556173%2C%20&destination=127.048483%2C37.556173
        String srcLongitude = "127.0622051";
        String srcLatitude = "37.556173";
        String destLongtitude = "127.048483";
        String destLatitude = "37.556173";
        naverMapSearchRequest(srcLatitude, srcLongitude, destLatitude, destLongtitude);
    }
}
