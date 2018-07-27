package kakao;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;
import java.util.*;

import com.google.gson.Gson;


public class KaKaoHelper {
    private static final Gson GSON = new Gson();
    private static final String API_SERVER_HOST = "https://dapi.kakao.com";
    private static final String API_KEY = "4de6f3195493e6e3110c34c2b34d7c8a";    // 이거 이대로 github 올라가면 보안 이슈 있음, 해결 필요
    private static final String SEARCH_CATEGORY_PATH = "/v2/local/search/category.json?";
    private static final String SEARCH_KEYWORD_PATH = "/v2/local/search/keyword.json?";
    private static final String TRANSCOORD_PATH = "/v2/local/geo/transcoord.json?";
    private static final Map<String, Coord> ARIA_COORDS = createAreaCoords();
    private static final Map<String, String> CATEGORY_GROUP_CODES = createCategoryGroupCodes();

    private static Map<String, Coord> createAreaCoords() {
        Map<String, Coord> coords = new HashMap<>();
        coords.put("서울", new Coord(126.977895, 37.566388));
        coords.put("강원", new Coord(127.729794, 37.885385));
        coords.put("부산", new Coord(129.056005, 35.157694));
        coords.put("제주특별자치도", new Coord(126.498439, 33.488985));
        return coords;
    }

    private static Map<String, String> createCategoryGroupCodes() {
        Map<String, String> codes = new HashMap<>();
        codes.put("대형마트", "MT1");
        codes.put("편의점", "CS2");
        codes.put("어린이집, 유치원", "PS3");
        codes.put("학교", "SC4");
        codes.put("학원", "AC5");
        codes.put("주차장", "PK6");
        codes.put("주유소, 충전소", "OL7");
        codes.put("지하철역", "SW8");
        codes.put("은행", "BK9");
        codes.put("문화시설", "CT1");
        codes.put("중개업소", "AG2");
        codes.put("공공기관", "PO3");
        codes.put("관광명소", "AT4");
        codes.put("숙박", "AD5");
        codes.put("음식점", "FD6");
        codes.put("카페", "CE7");
        codes.put("병원", "HP8");
        codes.put("약국", "PM9");
        return codes;
    }

    @SuppressWarnings("unused")
    private class TranscoordResponse {
        private Coord[] documents;
        private Meta meta;

        private Coord getCoord() {
            return documents[0];
        }

        private class Meta {
            private int total_count;
        }
    }

    @SuppressWarnings("unused")
    private class CategoryResponse {
        private KakaoPoi[] documents;
        private Meta meta;

        private KakaoPoi[] getPois() {
            return documents;
        }

        private boolean isEnd() {
            return meta.getIsEnd();
        }

        private class Meta {
            private int total_count;
            private int pageable_count;
            private boolean is_end;
            private SameName same_name;

            private boolean getIsEnd() {
                return is_end;
            }

            private class SameName {
                private String[] region;
                private String keyword;
                private String selected_region;
            }
        }
    }

    @SuppressWarnings("unused")
    private class KakaoPoi {
        private int id;
        private String place_name;
        private String address_name;
        private String category_group_code;
        private String category_group_name;
        private String category_name;
        private String distance;
        private String phone;
        private String place_url;
        private String road_address_name;
        private double x;
        private double y;

        private boolean checkAddress(String area) {
            return address_name.contains(area);
        }

        private double getX() {
            return x;
        }

        private double getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KakaoPoi kakaoPoi = (KakaoPoi) o;
            return id == kakaoPoi.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "{" +
                    "id:" + id +
                    ", address_name:" + address_name +
                    ", category_group_code:" + category_group_code +
                    ", category_group_name:" + category_group_name +
                    ", category_name:" + category_name +
                    ", distance:" + distance +
                    ", phone:" + phone +
                    ", place_name:" + place_name +
                    ", place_url:" + "'" + place_url + "'" +
                    ", road_address_name:" + road_address_name +
                    ",x:" + x +
                    ",y:" + y +
                    "}";
        }
    }

    private static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static String mapToParams(Map<String, String> map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key), urlEncodeUTF8(map.get(key))));
        }
        return paramBuilder.toString();
    }

    private static String request(String apiPath, Map<String, String> params) {
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

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            // System.out.println(buffer.toString());
            return builder.toString();
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
        Map<String, String> params = new HashMap<>();
        params.put("x", String.format("%.6f", x));
        params.put("y", String.format("%.6f", y));
        params.put("input_coord", input_coord);
        params.put("output_coord", output_coord);

        String jsonStr = request(TRANSCOORD_PATH, params);

        return GSON.fromJson(jsonStr, TranscoordResponse.class).getCoord();
    }

    private static Set<KakaoPoi> addNewPoisToSet(Set<KakaoPoi> poiSet, Map<String, String> params, String apiPath) {
        boolean isEnd = false;
        int page = 1;
        while (!isEnd) {
            params.put("page", String.valueOf(page++));
            String jsonStr = request(apiPath, params);
            CategoryResponse response = GSON.fromJson(jsonStr, CategoryResponse.class);
            poiSet.addAll(Arrays.asList(response.getPois()));
            isEnd = response.isEnd();
        }
        return poiSet;
    }

    private static Set<KakaoPoi> recursivelyAddNewPoisToSet(Set<KakaoPoi> poiSet, Map<String, String> params, String apiPath, String area) {
        Set<KakaoPoi> newPoiSet = new HashSet<>();
        while (!poiSet.isEmpty()) {
            List<KakaoPoi> tempPois = new ArrayList<>(poiSet);
            for (KakaoPoi curPoi : tempPois) {
                if (curPoi.checkAddress(area)) {
                    params.put("x", String.format("%.6f", curPoi.getX()));
                    params.put("y", String.format("%.6f", curPoi.getY()));

                    poiSet = addNewPoisToSet(poiSet, params, apiPath);
                    System.out.println("unchecked: " + poiSet.size() + ", checked: " + newPoiSet.size());
                }
            }
            newPoiSet.addAll(tempPois);
            poiSet.removeAll(newPoiSet);
            System.out.println("unchecked: " + poiSet.size() + ", checked: " + newPoiSet.size());
        }
        return newPoiSet;
    }

    public static Set<KakaoPoi> getPoiSetByCategory(String categoryName, String area) {
        String categoryGroupCode = CATEGORY_GROUP_CODES.get(categoryName);
        Coord ariaCoord = ARIA_COORDS.get(area);

        Map<String, String> params = new HashMap<>();
        params.put("category_group_code", categoryGroupCode);
        params.put("x", String.format("%.6f", ariaCoord.getX()));
        params.put("y", String.format("%.6f", ariaCoord.getY()));
        params.put("radius", String.valueOf(20000));
        params.put("sort", "accuracy");
        params.put("size", String.valueOf(15));

        Set<KakaoPoi> poiSet = addNewPoisToSet(new HashSet<>(), params, SEARCH_CATEGORY_PATH);
        poiSet = recursivelyAddNewPoisToSet(poiSet, params, SEARCH_CATEGORY_PATH, area);
        return poiSet;
    }

    public static Set<KakaoPoi> getPoiSetByKeyword(String keyword, String area) {
        Coord ariaCoord = ARIA_COORDS.get(area);

        Map<String, String> params = new HashMap<>();
        params.put("query", keyword);
        params.put("x", String.format("%.6f", ariaCoord.getX()));
        params.put("y", String.format("%.6f", ariaCoord.getY()));
        params.put("radius", String.valueOf(20000));
        params.put("sort", "accuracy");
        params.put("size", String.valueOf(15));

        Set<KakaoPoi> poiSet = addNewPoisToSet(new HashSet<>(), params, SEARCH_KEYWORD_PATH);
        poiSet = recursivelyAddNewPoisToSet(poiSet, params, SEARCH_KEYWORD_PATH, area);
        return poiSet;
    }

    public static void poisToJsonFile(Set<KakaoPoi> pois, String fileName) {
        String json = GSON.toJson(pois);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KakaoPoi[] jsonFileToPois(String fileName) {
        KakaoPoi[] pois = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            pois = GSON.fromJson(br, KakaoPoi[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pois;
    }


    public static void main(String[] args) {
        /* transcoord example */
//        Coord result = KakaoHelper.transcoord(127.108212, 37.402056, "WGS84", "WCONGNAMUL");
//        System.out.println(result.toString());

        /* getPoisByCategory */
//        Set<KakaoPoi> poiSet = getPoiSetByCategory("관광명소","제주특별자치도");
//        for (KakaoPoi poi : poiSet) {
//            System.out.println(poi);
//        }

        /* getPoisByKeyword */
        Set<KakaoPoi> poiSet = getPoiSetByKeyword("관광", "제주특별자치도");
//        for (KakaoPoi poi : poiSet) {
//            System.out.println(poi);
//        }

        /* poisToJsonFile */
        poisToJsonFile(poiSet, "poiTestKeyword.json");

        // agi? -> 사업구조? 매출은 어디서?
        // ai 벤처로서 어려운 점
    }
}

