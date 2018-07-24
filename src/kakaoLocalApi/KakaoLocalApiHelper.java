package kakaoLocalApi;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;

import com.google.gson.Gson;


public class KakaoLocalApiHelper {
    private static final Gson GSON = new Gson();
    private static final String API_SERVER_HOST = "https://dapi.kakao.com";
    private static final String SEARCH_CATEGORY_PATH = "/v2/local/search/category.json?";
    private static final String TRANSCOORD_PATH = "/v2/local/geo/transcoord.json?";
    private static final String API_KEY = "4de6f3195493e6e3110c34c2b34d7c8a";    // 이거 이대로 github 올라가면 보안 이슈 있음, 해결 필요
    private static final HashMap<String, String> categoryGroupCodes = createCategoryGroupCodes();

    private static HashMap<String, String> createCategoryGroupCodes() {
        HashMap<String, String> categoryGroupCodes = new HashMap<>();
        categoryGroupCodes.put("대형마트", "MT1");
        categoryGroupCodes.put("편의점", "CS2");
        categoryGroupCodes.put("어린이집, 유치원", "PS3");
        categoryGroupCodes.put("학교", "SC4");
        categoryGroupCodes.put("학원", "AC5");
        categoryGroupCodes.put("주차장", "PK6");
        categoryGroupCodes.put("주유소, 충전소", "OL7");
        categoryGroupCodes.put("지하철역", "SW8");
        categoryGroupCodes.put("은행", "BK9");
        categoryGroupCodes.put("문화시설", "CT1");
        categoryGroupCodes.put("중개업소", "AG2");
        categoryGroupCodes.put("공공기관", "PO3");
        categoryGroupCodes.put("관광명소", "AT4");
        categoryGroupCodes.put("숙박", "AD5");
        categoryGroupCodes.put("음식점", "FD6");
        categoryGroupCodes.put("카페", "CE7");
        categoryGroupCodes.put("병원", "HP8");
        categoryGroupCodes.put("약국", "PM9");
        return categoryGroupCodes;
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static String mapToParams(HashMap<String, String> map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(map.get(key))));
        }
        return paramBuilder.toString();
    }

    private static String request(String apiPath, HashMap<String, String> params) {
        String requestUrl = API_SERVER_HOST + apiPath + mapToParams(params);

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
            // System.out.println(String.format("\nSending GET request to URL : %s", requestUrl));
            // System.out.println("Response Code : " + responseCode);

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
            // System.out.println(buffer.toString());
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

    public static Coord transcoord(double x, double y, String input_coord, String output_coord) {
        // x	        x 좌표로 경위도인 경우 longitude	    double(ex: 127.108212)
        // y	        y 좌표로 경위도인 경우 latitude	       	double(ex: 37.402056)
        // input_coord	x, y 로 입력되는 값에 대한 좌표 체계	String(WGS84 or WCONGNAMUL or CONGNAMUL or WTM or TM)
        // output_coord	결과에 출력될 좌표 체계	                String(WGS84 or WCONGNAMUL or CONGNAMUL or WTM or TM)
        HashMap<String, String> params = new HashMap<>();
        params.put("x", String.format("%.6f", x));
        params.put("y", String.format("%.6f", y));
        params.put("input_coord", input_coord);
        params.put("output_coord", output_coord);

        String jsonStr = request(TRANSCOORD_PATH, params);
        jsonStr = jsonStr.substring(jsonStr.indexOf("["), jsonStr.indexOf("]") + 1);

        return GSON.fromJson(jsonStr, Coord[].class)[0];
    }

    public static void getPoisByCategory(String categoryName, String aria) {
        String rect = aria.equals("제주도") ? "126.110534,33.575816,126.953735,33.188224" : "0,0,0,0";

        HashMap<String, String> params = new HashMap<>();
        params.put("category_group_code", categoryGroupCodes.get(categoryName));
        params.put("rect", rect);
        params.put("sort", "accuracy");
        params.put("size", "15");

        for (int page = 1; page <= 45; page++) {
            params.put("page", String.valueOf(page));
            String jsonStr = request(SEARCH_CATEGORY_PATH, params);
            jsonStr = jsonStr.substring(jsonStr.indexOf("["), jsonStr.indexOf("]") + 1);
            System.out.println(jsonStr);
//
//            KakaoPoi[] kakaoPoiList = GSON.fromJson(jsonStr, KakaoPoi[].class);
//
//            System.out.println(kakaoPoiList[1].toString());
        }

    }

    public static void main(String[] args) {
        // transcoord example
        Coord result = KakaoLocalApiHelper.transcoord(127.108212, 37.402056, "WGS84", "WCONGNAMUL");
        System.out.println(result.toString());

        getPoisByCategory("편의점", "제주도");


//        String apiPath = SEARCH_CATEGORY_PATH;
//        Map<String, String> params = Map.ofEntries(
//                entry("category_group_code", "AT4"),
//                entry("rect", "126.110534,33.575816,126.953735,33.188224"),
//                entry("sort", "accuracy"),
//                entry("page", "1"),
//                entry("size", "15")
//        );
//
//        KakaoLocalApiHelper helper = new KakaoLocalApiHelper();
//        String jsonString = helper.request(apiPath, helper.mapToParams(params));
//        jsonString = jsonString.substring(jsonString.indexOf("["), jsonString.indexOf("]") + 1);
//
//        System.out.println(jsonString);
//
//        KakaoPoi[] kakaoPoiList = GSON.fromJson(jsonString, KakaoPoi[].class);
//
//        System.out.println(kakaoPoiList[1].toString());


    }
}
