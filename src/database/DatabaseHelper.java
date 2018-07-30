package database;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class DatabaseHelper {
    private static String getFileName(String name, String area, Boolean isNew, Boolean isPoi) {
        LocalDateTime now = LocalDateTime.now();
        String YYMMddHHmmss = now.format(DateTimeFormatter.ofPattern("YYMMddHHmmss"));
        String newOrUpdated = isNew ? "_new" : "_updated";
        String poiOrRoute = isPoi ? "poi_" : "";
        return YYMMddHHmmss + "_" + area + "_" + poiOrRoute + name + newOrUpdated + ".json";
    }

    private static void createAttractionPoiDatabase(String area) {

        String fileName = getFileName("attraction", area, true, true);
        System.out.println(fileName);
    }

    private static void createAccommodationPoiDatabase(String area) {

        String fileName = getFileName("accommodation", area, true, true);
        System.out.println(fileName);
    }

    private static void createShoppingPoiDatabase(String area) {

        String fileName = getFileName("shopping", area, true, true);
        System.out.println(fileName);
    }

    private static void createRestaurantPoiDatabase(String area) {

        String fileName = getFileName("restaurant", area, true, true);
        System.out.println(fileName);
    }

    private static void createEtcPoiDatabase(String area) {

        String fileName = getFileName("etc", area, true, true);
        System.out.println(fileName);
    }

    private static void createRouteDatabase(String area) {

        String fileName = getFileName("route", area, true, false);
        System.out.println(fileName);
    }

    private static void createPoiDatabase(String area) {
        createAttractionPoiDatabase(area);
        createAccommodationPoiDatabase(area);
        createShoppingPoiDatabase(area);
        createRestaurantPoiDatabase(area);
        createEtcPoiDatabase(area);
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
    }
}
