package services.qna.wisenut;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
public class WisenutClient {
    private static Logger logger = Logger.getLogger(WisenutClient.class);
    private Gson gson = new Gson();


    public WisenutQuestionFormat getAnswer(String query) {
        String URL = "http://211.39.140.247:23890/.tmf?say=";
        String subURL = query;
        String thirdURL = "&sessionId=01012340987";
        String retStr = null;
        try {
            subURL = URLEncoder.encode(subURL, "UTF-8");
            URL = URL + subURL + thirdURL;
            retStr = requestHTTP(URL);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (retStr != null) {
            WisenutQuestionFormat ret = gson.fromJson(retStr, WisenutQuestionFormat.class);
            logger.debug(retStr);
            return ret;

        } else {
            return null;
        }


    }

    public static String requestHTTP(String str) throws IOException {
        URL url = new URL(str);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        StringBuffer strbuf = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            strbuf.append(inputLine +"\n") ;
//            System.out.println(inputLine);
        }
        in.close();
//        logger.debug(strbuf.toString());
        return strbuf.toString().trim();
    }

    public static void test(){
        String str ="{\n" +
                "\t\"persons\":[\n" +
                "\t\t\"엑소브레인실무자\"\n" +
                "\t],\n" +
                "\t\"temporalConstraints\":[\n" +
                "\t{\n" +
                "\t\t\"symbolicTime\":\"이번주\",\n" +
                "\t\t\"afterTime\":\"2015-11-09 06:00\",\n" +
                "\t\t\"beforeTime\":\"2015-11-15 22:00\"\n" +
                "\t}\n" +
                "\t],\n" +
                "\t\"spatialConstraints\":{\n" +
                "\t\"symbolicSpatialConstraints\":[],\n" +
                "\t\"metricSpatialConstraints\":[]\n" +
                "\t},\n" +
                "\t\"objective\":\"SingleEventMeeting\",\n" +
                "\t\"duration\":{\n" +
                "\t\t\"day\":\"0\",\n" +
                "\t\t\"hour\":\"3\"\n" +
                "\t},\n" +
                "\t\"keywords\":{},\n" +
                "\t\"others\":{}\n" +
                "} "          ;
        Gson gson = new Gson();
        WisenutQuestionFormat ret = gson.fromJson(str, WisenutQuestionFormat.class);
        logger.debug(ret);
    }

    public static void main(String[] args) throws IOException {

        WisenutClient wisenutClient = new WisenutClient();
//        test();
        logger.debug(wisenutClient.getAnswer("다음달에 아내와 3일 정도 제주도로 여행을 가고 싶은데 계획 좀 부탁해. "));
        logger.debug(wisenutClient.getAnswer("이번주 우리팀 회식을 하려고 하는데 언제 어디서 하면 좋을까? "));
        logger.debug(wisenutClient.getAnswer("다음주 주말에 가족 모임을 성남시에 있는 식당에서 하려고 하는데 괜찮은 장소와 시간을 알려줘"));

    }
}
