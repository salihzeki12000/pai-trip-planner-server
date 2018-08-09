package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import java.util.*;

public class ItemList {
    private Set<Item> itemList = new HashSet<>();


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
}
