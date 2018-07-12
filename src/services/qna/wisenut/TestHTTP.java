package services.qna.wisenut;

import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 12. 23
 * Time: ���� 1:58
 * To change this template use File | Settings | File Templates.
 */
public class
TestHTTP {
    private static Logger logger = Logger.getLogger(TestHTTP.class);

    //    http://maps.google.com/maps/api/directions/json?origin=37.631886,127.0622051&destination=37.556173,127.048483&sensor=false
    //     37.631886,127.0622051
    // 37.556173, 127.048483
    public static String request(String str) {

        try {
            return requestHTTP(str);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

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

//        URL url = new URL("http://map.daum.net/?eX=501220&eY=1121570&sName=�Ѿ���б�");
//        URL url = new URL("http://map.naver.com/findroute2/findCarRoute.nhn?via=&call=route2&output=json&car=0&mileage=12.4&start=127.0622051%2C37.556173%2C%20&destination=127.048483%2C37.556173%2C%20&search=2");
//  �ȴ� �ȴ�.
//        URL url = new URL("http://map.naver.com/findroute2/findPubTransRoute.nhn?&output=json&start=127.0622051%2C37.556173%2C%20&destination=127.048483%2C37.556173%2C%20&search=10");


        URL url = new URL(str);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

//        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("src/map/example.json")));

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
        StringBuffer strbuf = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            strbuf.append(inputLine);
            System.out.println(inputLine);
        in.close();
        return strbuf.toString();
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

    public static void testBatchProcess(String questionStr) throws IOException {
        String URL = "http://211.39.140.247:23890/.tmf?&session_id=01045598193&say=";
        String questionURL;
        String sessionIDURL = "&sessionId=01045598193";
        questionURL = URLEncoder.encode(questionStr, "UTF-8");
        URL = URL +  questionURL ;
        logger.debug(URL);
        String retJsonStr = requestHTTP(URL);

        logger.debug(retJsonStr);
        retJsonStr = retJsonStr.replace('}','>');
        retJsonStr = retJsonStr.replace('{','<');
        logger.debug(retJsonStr);




        retJsonStr = retJsonStr.trim();
        String taskURL = "http://166.104.36.218:8088/get";
        String prefix = "?type=formed_question&body=";
        String utfStr=  URLEncoder.encode(retJsonStr, "UTF-8");
        String finalRequestStr= taskURL + prefix + utfStr;
        logger.debug(finalRequestStr);
        logger.debug(requestHTTP(finalRequestStr));


    }

    public static void test(){
        String taskURL = "http://166.104.36.218:8088/get";
        String jsonStr = "{\t\"sessionId\":\"01045598193\",\t\"persons\":[\t\t\"엑소브레인\"\t],\t\"temporalConstraints\":[\t{\t\t\"symbolicTime\":\"다음달\",\t\t\"afterTime\":\"2016-02-01 06:00\",\t\t\"beforeTime\":\"2016-02-29 22:00\"\t}\t],\t\"spatialConstraints\":{\t\"symbolicSpatialConstraints\":[\t\t\"제주도\"\t],\t\"metricSpatialConstraints\":[]\t},\t\"objective\":\"workshop\",\t\"verb\":\"answer\",\t\"duration\":{\t\t\"day\":\"2\",\t\t\"hour\":\"0\"\t},\t\"keywords\":\"\",\t\"others\":{}} ";
        String composedTaskURL = null;
        try {
            composedTaskURL = composeTaskURL(taskURL, jsonStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("composedTaskURL = " + composedTaskURL);
    }

    /**
     *
     * @param taskURL 서버 주소
     *                예: http://166.104.36.218:8088/get
     * @param jsonStr  질의해석 결과의 json 문자열
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String composeTaskURL(String taskURL, String jsonStr) throws UnsupportedEncodingException {
        jsonStr = jsonStr.replace('}','>');
        jsonStr = jsonStr.replace('{','<');
        jsonStr.trim();
        String prefix = "?type=formed_question&body=";
        String utfStr=  URLEncoder.encode(jsonStr, "UTF-8");
        String url= taskURL + prefix + utfStr;
        return url;
    }

    public static void main(String[] args) throws IOException {

        test();
//        testBatchProcess("다음달에 1박2일로 제주도로 엑소브레인 전체 워크숍을 가려고 하는데 일정을 계획해줘");
//        String URL = "http://211.39.140.247:23890/.tmf?say=";
//        String subURL = "이번주에 엑소브레인 실무자 회의를 하려고 하는데 언제 어디서 하면 좋을까";
//        String thirdURL = "&sessionId=01012340987";
//
//        subURL  =  URLEncoder.encode(subURL, "UTF-8");
//
//        URL = URL + subURL + thirdURL;
//
//        requestHTTP(URL);


    }
}
