package edu.hanyang.trip_planning.tripData.daumLocalAPI;

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
public class ItemList {
    private static Logger logger = Logger.getLogger(DaumPoiIO.class);
    private Set<Item> itemList = new HashSet<Item>();


    public Set<Item> getItemList() {
        return itemList;
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void addItems(Collection<Item> items) {
        for (Item item : items) {
            itemList.add(item);
        }
    }

    @Override
    public String toString() {
        return "ItemList{" +
                "itemList=" + itemList +
                '}';
    }

    public static void main(String[] args) {
        ItemList itemList = new ItemList();
        for (int i = 0; i < 10; i++) {
            itemList.addItem(DaumLocalAPI.dummyItem());
        }
        Gson gson = new Gson();
        String str = gson.toJson(itemList);
        logger.debug(str);
    }
}
