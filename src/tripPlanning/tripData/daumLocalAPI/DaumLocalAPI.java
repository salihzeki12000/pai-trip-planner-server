package tripPlanning.tripData.daumLocalAPI;

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

    public static Item getPoi(String name) {
        logger.debug("getPoi:" + name);
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

    public static List<Item> getPois(String name, int numberOfItem) {
        String str = null;
        try {
            str = "https://apis.daum.net/local/v1/search/keyword.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&query=" + URLEncoder.encode(name, "UTF-8");
            return parseLocation(str, numberOfItem);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> getPois(String keyword, double longtitude, double latitude, int radius, int numberOfItem) {
        String str = null;
        try {
            str = "https://apis.daum.net/local/v1/search/keyword.json?apikey=a1238cbf32a23df62fcdfa4ffc4ecad63ee71e45&query=" + URLEncoder.encode(keyword, "UTF-8") + "&location=" + latitude + "," + longtitude + "&radius=" + radius;
            return parseLocation(str, numberOfItem);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        throw new RuntimeException("error");
    }

    public static List<Item> getPois(double longtitude, double latitude, int radius, int numberOfItem) {
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
        List<Item> infoPoiList = new ArrayList<>();
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
                infoPoiList.add(item);
            }
        }
        return infoPoiList;
    }

    public static void main(String[] args) {
        Item item = DaumLocalAPI.getPoi("해올렛직판장 ");
        logger.debug(item);
    }
}
