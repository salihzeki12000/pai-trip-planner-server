package database;


import com.google.gson.Gson;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DatabaseHelper {
    private static final Gson GSON = new Gson();
    private static final String API_SERVER_HOST = "https://dapi.kakao.com";
    private static final String API_KEY = "4de6f3195493e6e3110c34c2b34d7c8a";    // 이거 이대로 github 올라가면 보안 이슈 있음, 해결 필요
    private static final String SEARCH_CATEGORY_PATH = "/v2/local/search/category.json?";
    private static final String SEARCH_KEYWORD_PATH = "/v2/local/search/keyword.json?";
    private static final String TRANSCOORD_PATH = "/v2/local/geo/transcoord.json?";
    private static final Map<String, Coord> ARIA_COORDS = createAreaCoords();
    private static final Map<String, Coord[]> ARIA_RECTS = createAreaRects();

    private static Map<String, Coord[]> createAreaRects() {
        Map<String, Coord[]> rects = new HashMap<>();
        rects.put("제주특별자치도", new Coord[]{new Coord(126.977895, 37.566388), new Coord(126.977895, 37.566388)});
        return rects;
    }

    private static final String[] VALID_ATTRACTION_CATEGORIES = {"관광"};
    private static final String[] INVALID_ATTRACTION_CATEGORIES = {};
    private static final String[] VALID_ACCOMMODATION_CATEGORIES = {"숙박"};
    private static final String[] INVALID_ACCOMMODATION_CATEGORIES = {};
    private static final String[] VALID_SHOPPING_CATEGORIES = {"시장", "면세점", "백화점"};
    private static final String[] INVALID_SHOPPING_CATEGORIES = {};
    private static final String[] VALID_RESTAURANT_CATEGORIES = {"음식점"};
    private static final String[] INVALID_RESTAURANT_CATEGORIES = {"구내식당", "기사식당", "술집", "간식", "카페", "치킨", "도시락", "패스트푸드", "분식"};
    private static final String[] VALID_ETC_CATEGORIES = {"교통,수송"};
    private static final String[] INVALID_ETC_CATEGORIES = {"택시", "도로시설", "자동차", "운송", "입출구", "카셰어링", "주차장", "도심공항터미널", "화물터미널"};

    private static Map<String, Coord> createAreaCoords() {
        Map<String, Coord> coords = new HashMap<>();
        coords.put("서울", new Coord(126.977895, 37.566388));
        coords.put("강원", new Coord(127.729794, 37.885385));
        coords.put("부산", new Coord(129.056005, 35.157694));
        coords.put("제주특별자치도", new Coord(126.498439, 33.488985));
        return coords;
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

    private static Coord transcoord(double x, double y, String input_coord, String output_coord) {
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

    private static Set<KakaoPoi> addNewPoisToSet(Set<KakaoPoi> poiSet, Map<String, String> params, String apiPath, String area, String[] validCategories, String[] invalidCategories) {
        boolean isEnd = false;
        int page = 1;
        while (!isEnd) {
            params.put("page", String.valueOf(page++));
            String jsonStr = request(apiPath, params);
            try {
                SearchResponse response = GSON.fromJson(jsonStr, SearchResponse.class);
                poiSet.addAll(Arrays.asList(response.getPois()));
                isEnd = response.isEnd();
            } catch (NullPointerException npe) {
                System.out.println(jsonStr);
            }
        }

        Set<KakaoPoi> invalidPoiSet = new HashSet<>();
        for (KakaoPoi poi : poiSet) {
            if (!poi.isAddressValid(area) || !poi.isCategoryValid(validCategories, invalidCategories)) {
                invalidPoiSet.add(poi);
            }
        }
        poiSet.removeAll(invalidPoiSet);

        return poiSet;
    }

    private static Set<KakaoPoi> recursivelyAddNewPoisToSet(Set<KakaoPoi> poiSet, Map<String, String> params, String apiPath, String area, String[] validCategories, String[] invalidCategories) {
        Set<KakaoPoi> newPoiSet = new HashSet<>();
        while (!poiSet.isEmpty()) {
            List<KakaoPoi> tempPois = new ArrayList<>(poiSet);
            for (KakaoPoi curPoi : tempPois) {
                params.put("x", String.format("%.6f", curPoi.getX()));
                params.put("y", String.format("%.6f", curPoi.getY()));

                poiSet = addNewPoisToSet(poiSet, params, apiPath, area, validCategories, invalidCategories);
                System.out.println("unchecked: " + poiSet.size() + ", checked: " + newPoiSet.size());
            }
            newPoiSet.addAll(tempPois);
            poiSet.removeAll(newPoiSet);
            System.out.println("unchecked: " + poiSet.size() + ", checked: " + newPoiSet.size());
        }
        return newPoiSet;
    }

    private static Set<KakaoPoi> getPoiSetByKeyword(String keyword, String area, String[] validCategories, String[] invalidCategories) {
        Coord ariaCoord = ARIA_COORDS.get(area);

        Map<String, String> params = new HashMap<>();
        params.put("query", keyword);
        params.put("x", String.format("%.6f", ariaCoord.getX()));
        params.put("y", String.format("%.6f", ariaCoord.getY()));
        params.put("radius", String.valueOf(20000));
        params.put("sort", "accuracy");
        params.put("size", String.valueOf(15));

        Set<KakaoPoi> poiSet = addNewPoisToSet(new HashSet<>(), params, SEARCH_KEYWORD_PATH, area, validCategories, invalidCategories);
        poiSet = recursivelyAddNewPoisToSet(poiSet, params, SEARCH_KEYWORD_PATH, area, validCategories, invalidCategories);
        return poiSet;
    }

    private static String getFileName(String name, String area, Boolean isNew, Boolean isPoi) {
        LocalDateTime now = LocalDateTime.now();
        String YYMMddHHmmss = now.format(DateTimeFormatter.ofPattern("YYMMddHHmmss"));
        String newOrUpdated = isNew ? "_new" : "_updated";
        String poiOrRoute = isPoi ? "poi_" : "";
        return YYMMddHHmmss + "_" + area + "_" + poiOrRoute + name + newOrUpdated + ".json";
    }

    private static void poiSetToJsonFile(Set<KakaoPoi> poiSet, String fileName) {
        String json = GSON.toJson(poiSet);
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static KakaoPoi[] jsonFileToPois(String fileName) {
        KakaoPoi[] pois = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            pois = GSON.fromJson(br, KakaoPoi[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pois;
    }

    private static void createAttractionPoiDatabase(String area) {
        Set<KakaoPoi> poiSet = getPoiSetByKeyword("관광", area, VALID_ATTRACTION_CATEGORIES, INVALID_ATTRACTION_CATEGORIES);
        String fileName = getFileName("attraction", area, true, true);
        poiSetToJsonFile(poiSet, fileName);
    }

    private static void createAccommodationPoiDatabase(String area) {
        Set<KakaoPoi> poiSet = getPoiSetByKeyword("숙박", area, VALID_ACCOMMODATION_CATEGORIES, INVALID_ACCOMMODATION_CATEGORIES);
        String fileName = getFileName("accommodation", area, true, true);
        poiSetToJsonFile(poiSet, fileName);
    }

    private static void createShoppingPoiDatabase(String area) {
        Set<KakaoPoi> poiSet = new HashSet<>();
        poiSet.addAll(getPoiSetByKeyword("백화점", area, VALID_SHOPPING_CATEGORIES, INVALID_SHOPPING_CATEGORIES));
        poiSet.addAll(getPoiSetByKeyword("시장", area, VALID_SHOPPING_CATEGORIES, INVALID_SHOPPING_CATEGORIES));
        poiSet.addAll(getPoiSetByKeyword("면세점", area, VALID_SHOPPING_CATEGORIES, INVALID_SHOPPING_CATEGORIES));
        String fileName = getFileName("shopping", area, true, true);
        poiSetToJsonFile(poiSet, fileName);
    }

    private static void createRestaurantPoiDatabase(String area) {
        Set<KakaoPoi> poiSet = getPoiSetByKeyword("음식점", area, VALID_RESTAURANT_CATEGORIES, INVALID_RESTAURANT_CATEGORIES);
        String fileName = getFileName("restaurant", area, true, true);
        poiSetToJsonFile(poiSet, fileName);
    }

    private static void createEtcPoiDatabase(String area) {
        Set<KakaoPoi> poiSet = new HashSet<>();
        poiSet.addAll(getPoiSetByKeyword("공항", area, VALID_ETC_CATEGORIES, INVALID_ETC_CATEGORIES));
        poiSet.addAll(getPoiSetByKeyword("터미널", area, VALID_ETC_CATEGORIES, INVALID_ETC_CATEGORIES));
        poiSet.addAll(getPoiSetByKeyword("지하철", area, VALID_ETC_CATEGORIES, INVALID_ETC_CATEGORIES));
        poiSet.addAll(getPoiSetByKeyword("기차역", area, VALID_ETC_CATEGORIES, INVALID_ETC_CATEGORIES));
        String fileName = getFileName("etc", area, true, true);
        poiSetToJsonFile(poiSet, fileName);
    }

    private static void createPoiDatabase(String area) {
        createAttractionPoiDatabase(area);
        createAccommodationPoiDatabase(area);
        createShoppingPoiDatabase(area);
        createRestaurantPoiDatabase(area);
        createEtcPoiDatabase(area);
    }

    private static void createRouteDatabase(String area) {
        String fileName = getFileName("route", area, true, false);
        System.out.println(fileName);
    }


    private static void createDatabase(String area) {
        createPoiDatabase(area);
        createRouteDatabase(area);
    }


    public static void main(String[] args) {
//        input: area         - 제주특별자치도
//        Output: database    - 180728184620_제주특별자치도_poi_attractions_new.json
//                            - 180728184620_제주특별자치도_poi_accommodation_new.json
//                            - 180728184620_제주특별자치도_poi_shopping_new.json
//                            - 180728184620_제주특별자치도_poi_restaurant_new.json
//                            - 180728184620_제주특별자치도_poi_etc_new.json (공항,터미널,지하철,...)
//                            - 180728184620_제주특별자치도_route_new.json
        createDatabase("제주특별자치도");
//        createRestaurantPoiDatabase("제주특별자치도");

//        String fileName = "180730151819_서울_poi_attraction_new.json";
//        String fileName = "180730152557_서울_poi_accommodation_new.json";
//        String fileName = "180730152627_서울_poi_shopping_new.json";    //체크필요
//        String fileName = "180730161646_서울_poi_restaurant_new.json";
//        String fileName = "180730161740_서울_poi_etc_new.json";
//        KakaoPoi[] pois = jsonFileToPois(fileName);
//        Set<String> category1 = new HashSet<>();
//        Set<String> category2 = new HashSet<>();
//        Set<String> category3 = new HashSet<>();
//        Set<String> category4 = new HashSet<>();
//        String[] test = {""};
//        for (KakaoPoi poi : pois) {
//            if (poi.isCategoryValid(VALID_ETC_CATEGORIES, INVALID_ETC_CATEGORIES)) {
//                List<String> categoryList = poi.parseCategory();
//                category1.add(categoryList.get(0));
//                category2.add(categoryList.get(1));
//                category3.add(categoryList.get(2));
//                category4.add(categoryList.get(3));
//            }
//        }
//        System.out.println("done");
    }
}
