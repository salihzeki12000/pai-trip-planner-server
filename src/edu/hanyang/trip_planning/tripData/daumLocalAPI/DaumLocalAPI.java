package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import com.google.gson.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DaumLocalAPI {
    private static Logger logger = Logger.getLogger(DaumLocalAPI.class);

    public static Item getPOI(String name) {
        logger.debug("getPOI:" + name);
        String str = null;
        try {
            str = "https://apis.daum.net/local/v1/search/keyword.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&query=" + URLEncoder.encode(name, "UTF-8");
            List<Item> itemList = parseLocation(str, 1);
            return itemList.get(0);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> getPOIs(String name, int numberOfItem) {
        String str = null;
        try {
            str = "https://apis.daum.net/local/v1/search/keyword.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&query=" + URLEncoder.encode(name, "UTF-8");
            return parseLocation(str, numberOfItem);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> getPOIs(String keyword, double longtitude, double latitude, int radius, int numberOfItem) {
        String str = null;
        try {
            str = "https://apis.daum.net/local/v1/search/keyword.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&query=" + URLEncoder.encode(keyword, "UTF-8") + "&location=" + latitude + "," + longtitude + "&radius=" + radius;
            return parseLocation(str, numberOfItem);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> getPOIs(double longtitude, double latitude, int radius, int numberOfItem) {
        String str = "https://apis.daum.net/local/v1/search/category.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&code=FD6&location=" + latitude + "," + longtitude + "&radius=" + radius;
        try {
            return parseLocation(str, numberOfItem);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> parseLocation(String str, int numberOfItem) throws IOException {
        logger.debug(str);
        List<Item> infoPOIList = new ArrayList<>();
        URL url = new URL(str);
        URLConnection yc = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        JsonStreamParser jsonStreamParser = new JsonStreamParser(in);
        Gson gson = new GsonBuilder().create();
        while (jsonStreamParser.hasNext()) {
            JsonElement jElement = jsonStreamParser.next();
            JsonObject channelObject = (JsonObject) ((JsonObject) jElement).get("channel");
            JsonArray itemArray = channelObject.get("item").getAsJsonArray();

            int size = 0;
            if (numberOfItem <= itemArray.size()) {
                size = numberOfItem;
            } else {
                size = itemArray.size();
            }
            for (int i = 0; i < size; i++) {
                JsonElement jsonElement = itemArray.get(i);
                Item item = gson.fromJson(jsonElement, Item.class);
                logger.debug(item.getTitle() + "\t" + item);
                infoPOIList.add(item);
            }
        }
        return infoPOIList;
    }

    public static Item dummyItem() {
        Item item = new Item();
        item.setPhone("02-2220-0114");
        item.setNewAddress("서울 성동구 왕십리로 222");
        item.setAddress("서울 성동구 사근동 110");
        item.setImageUrl("http://cfile73.uf.daum.net/image/142047044AF11C84392704");
        item.setPlaceUrl("placeUrl=http://place.map.daum.net/11211235");
        item.setId("11211235");
        item.setTitle("한양대학교 서울캠퍼스");
        item.setCategory("교육,학문 > 학교 > 대학교");
        item.setLongitude("127.04532955770931");
        item.setLatitude("37.55770114763559");
        item.setAddressBCode("1120010700");
        return item;
    }

    public static void main(String[] args) {
//        List<InfoPOI> list = DaumLocalAPI.getPOIs(127.0448017838126, 37.65149801749762, 2000);
//        for (InfoPOI infoPOI : list) {
//            logger.debug(infoPOI);
//        }
        logger.debug(dummyItem());
//        List<Item> list = DaumLocalAPI.getPOIs("한양대학교");
//        for (Item infoPOI : list) {
//            logger.debug(infoPOI);
//        }
        Item item = DaumLocalAPI.getPOI("해올렛직판장 ");
        logger.debug(item);
    }
}
