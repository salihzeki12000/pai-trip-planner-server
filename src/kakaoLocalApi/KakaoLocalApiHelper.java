package kakaoLocalApi;

import edu.hanyang.trip_planning.tripData.dataType.Location;

import java.util.Map;

import static java.util.Map.entry;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;


public class KakaoLocalApiHelper {
    private static final String API_SERVER_HOST = "https://dapi.kakao.com";
    private static final String SEARCH_CATEGORY_PATH = "/v2/local/search/category.json?";
    private static final String API_KEY = "4de6f3195493e6e3110c34c2b34d7c8a";    // 이거 이대로 github 올라가면 보안 이슈 있음, 해결 필요
    private static final Map<String, String> categoryGroupCodes = Map.ofEntries(
            entry("대형마트", "MT1"),
            entry("편의점", "CS2"),
            entry("어린이집, 유치원", "PS3"),
            entry("학교", "SC4"),
            entry("학원", "AC5"),
            entry("주차장", "PK6"),
            entry("주유소, 충전소", "OL7"),
            entry("지하철역", "SW8"),
            entry("은행", "BK9"),
            entry("문화시설", "CT1"),
            entry("중개업소", "AG2"),
            entry("공공기관", "PO3"),
            entry("관광명소", "AT4"),
            entry("숙박", "AD5"),
            entry("음식점", "FD6"),
            entry("카페", "CE7"),
            entry("병원", "HP8"),
            entry("약국", "PM9")
    );
    private static final Gson GSON = new Gson();

    void getPois(Location location) {

    }

    private String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private String mapToParams(Map<String, String> map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(map.get(key))));
        }
        return paramBuilder.toString();
    }

    private String request(String apiPath, String params) {
        String requestUrl = API_SERVER_HOST + apiPath + params;

        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + API_KEY);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");

            int responseCode = con.getResponseCode();
            System.out.println(String.format("\nSending GET request to URL : %s", requestUrl));
            System.out.println("Response Code : " + responseCode);

            if (responseCode == 200)
                isr = new InputStreamReader(con.getInputStream());
            else
                isr = new InputStreamReader(con.getErrorStream());

            reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Exception ignore) {
            }
            if (isr != null) try {
                isr.close();
            } catch (Exception ignore) {
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String apiPath = SEARCH_CATEGORY_PATH;
        Map<String, String> params = Map.ofEntries(
                entry("category_group_code", "AT4"),
                entry("rect", "126.110534,33.575816,126.953735,33.188224"),
                entry("sort", "accuracy"),
                entry("page", "1"),
                entry("size", "15")
        );

        KakaoLocalApiHelper helper = new KakaoLocalApiHelper();
        String jsonString = helper.request(apiPath, helper.mapToParams(params));
        jsonString = jsonString.substring(jsonString.indexOf("["), jsonString.indexOf("]") + 1);

        System.out.println(jsonString);

        KakaoPoi[] kakaoPoiList = GSON.fromJson(jsonString, KakaoPoi[].class);

        System.out.println(kakaoPoiList[1].toString());
    }
}
