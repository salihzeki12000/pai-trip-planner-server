package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tripPlanning.TripPlanner;
import tripPlanning.optimize.MultiDayTripAnswer;
import tripPlanning.trip_question.TripQuestion;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.util.*;

public class PlanningHttpServer {
    private static Logger logger = Logger.getLogger(PlanningHttpServer.class);

    static class GetHandler implements HttpHandler {

        private static Map<String, String> parseQuery(String query) {
            query = query.replaceAll("<", "{").replaceAll(">", "}");
            StringTokenizer stringTokenizer = new StringTokenizer(query);

            List<String> pairList = new ArrayList<>();
            while (stringTokenizer.hasMoreTokens()) {
                pairList.add(stringTokenizer.nextToken("&"));
            }

            Map<String, String> params = new HashMap<>();
            for (String pairStr : pairList) {
                StringTokenizer pairTokenizer = new StringTokenizer(pairStr);
                String key = pairTokenizer.nextToken("=");
                String value = pairTokenizer.nextToken("=");
                params.put(key, value);
            }
            return params;
        }

        private static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");   // for CORS issue
            httpExchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = httpExchange.getResponseBody();
            OutputStreamWriter writer = new OutputStreamWriter(os);
            writer.write(response);
            writer.flush();
            writer.close();
        }

        public void handle(HttpExchange httpExchange) throws IOException {
            Gson gson = new Gson();

            String query = httpExchange.getRequestURI().getQuery();
//            String utfStr = URLDecoder.decode(str, "UTF-8");
            Map<String, String> params = parseQuery(query);
            String typeStr = params.get("type");
            String tripQuestionStr = params.get("tripQuestion");

            StringBuilder response = new StringBuilder();
            if (typeStr.equals("html") || typeStr.equals("json")) {
                TripQuestion tripQuestion = gson.fromJson(tripQuestionStr, TripQuestion.class);
                TripPlanner tripPlanner = new TripPlanner(30, 1, 1, 50, 5);
                MultiDayTripAnswer multiDayTripAnswer = tripPlanner.tripPlanning(tripQuestion);

                logger.debug(multiDayTripAnswer);
                for (int i = 0; i < multiDayTripAnswer.size(); i++) {
                    logger.debug(multiDayTripAnswer.getItinerary(i));
                    System.out.println(multiDayTripAnswer.getItinerary(i).value);
                }
                for (int i = 0; i < multiDayTripAnswer.size(); i++) {
                    System.out.println(multiDayTripAnswer.getItinerary(i).getPoiTitles());
                }
                if (typeStr.equals("html")) {
                    HTMLGenerator htmlGenerator = new HTMLGenerator(multiDayTripAnswer);
                    response.append(htmlGenerator.generateHTML());
                } else {
                    JSONGenerator jsonGenerator = new JSONGenerator(multiDayTripAnswer);
                    String json = jsonGenerator.generateJSON();
                    response.append(json);
                }
            } else {
                response.append("정의되지 않은 요청입니다. ");
            }

            writeResponse(httpExchange, response.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        int serverPort = 8088;

        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        server.createContext("/get", new GetHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("This is Planning HTTP server with port " + serverPort);
    }
}