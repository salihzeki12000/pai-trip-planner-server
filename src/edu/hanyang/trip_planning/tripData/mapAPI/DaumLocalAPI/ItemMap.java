package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 15
 * Time: 오후 3:43
 * To change this template use File | Settings | File Templates.
 */
public class ItemMap {
    private static Logger logger = Logger.getLogger(DaumPoiIO.class);
    private Map<String, Item> itemMap = new HashMap<String, Item>();


    public Map<String, Item> getItemSet() {
        return itemMap;
    }

    public void addItem(Item item) {
        itemMap.put(item.getTitle(), item);
    }

    public void addItems(Collection<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    @Override
    public String toString() {
        return "ItemList{" +
                "itemMap=" + itemMap +
                '}';
    }

    public static void main(String[] args) {
        ItemMap itemList = new ItemMap();
        for (int i = 0; i < 10; i++) {
            itemList.addItem(DaumLocalAPI.dummyItem());
        }
        Gson gson = new Gson();
        String str = gson.toJson(itemList);
        logger.debug(str);
    }
}
