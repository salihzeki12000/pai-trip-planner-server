package services.server;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.hanyang.trip_planning.TripPlanner;
import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.tripData.mapAPI.googleMap.GenerateMultiMapPathHTML;
import edu.hanyang.trip_planning.tripData.mapAPI.googleMap.MultiDayTripAnswer;
import edu.hanyang.trip_planning.trip_question.TripQuestion;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.*;

public class PlanningHttpServer {
    private static Logger logger = Logger.getLogger(PlanningHttpServer.class);
    private static String oldBodyStr;
    private static String fakeToday = null;

//    static class InfoHandler implements HttpHandler {
//        public void handle(HttpExchange httpExchange) throws IOException {
//            String response = "/get?identifier=01045598193&query_handling=내일 엑소브레인 실무자 회의를 하려고 하는데 추천좀 해줘";
//            PlanningHttpServer.writeResponse(httpExchange, response.toString());
//        }
//    }

    public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        writer.write(response);
        writer.flush();
        writer.close();
    }

    public static Map<String, String> parseQuestion(String query) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> pairList = new ArrayList<String>();
        StringTokenizer stringTokenizer = new StringTokenizer(query);
        while (stringTokenizer.hasMoreTokens()) {
            pairList.add(stringTokenizer.nextToken("&"));
        }
        for (String pairStr : pairList) {
            StringTokenizer pairTokennizer = new StringTokenizer(pairStr);
            String key = pairTokennizer.nextToken("=");
            String value = pairTokennizer.nextToken("=");
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        int serverPort = 8088;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/get", new GetHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("This is Planning HTTP server with port " + serverPort);
    }

    static class GetHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {

            long start = System.currentTimeMillis();

            Gson gson = new Gson();
            TripPlanner tripPlanner = new TripPlanner();
            MultiDayTripAnswer multiDayTripAnswer;
            StringBuilder response = new StringBuilder();

            String str = httpExchange.getRequestURI().getQuery();
            String utfStr = URLDecoder.decode(str, "UTF-8");
            Map<String, String> params = parseQuestion(utfStr);
            String typeStr = params.get("type");
            String bodyStr = params.get("body");

            if (typeStr.equals("trip_json_question")) {
                bodyStr = bodyStr.replaceAll("<", "{");
                bodyStr = bodyStr.replaceAll(">", "}");
                TripQuestion tripQuestion = gson.fromJson(bodyStr, TripQuestion.class);

                long plannerStart = System.currentTimeMillis();
                multiDayTripAnswer = tripPlanner.tripPlanning(tripQuestion);
                long plannerEnd = System.currentTimeMillis();
                System.out.println("실행 시간: "+(plannerEnd-plannerStart)/1000.0);

                logger.debug(multiDayTripAnswer);
                for (int i = 0; i < multiDayTripAnswer.size(); i++) {
                    logger.debug(multiDayTripAnswer.getItinerary(i));
                    System.out.println(multiDayTripAnswer.getItinerary(i).value);
                }
                for (int i = 0; i < multiDayTripAnswer.size(); i++) {
                    System.out.println(multiDayTripAnswer.getItinerary(i).getPoiTitles());
                }

                GenerateMultiMapPathHTML genHTML = new GenerateMultiMapPathHTML(multiDayTripAnswer);
                response.append(genHTML.generateMapHTMLStr());
            } else if (typeStr.equals("natural_question")) {
                response.append("Not implemented");
            } else {
                response.append("정의되지 않은 요청입니다. ");
            }

            PlanningHttpServer.writeResponse(httpExchange, response.toString());

            long end = System.currentTimeMillis();
            System.out.println("실행 시간: "+(end-start)/1000.0);

        }
    }
}