package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 29
 * Time: 오후 4:18
 * To change this template use File | Settings | File Templates.
 */
public class POIViewer {


    public static void test() {
        DaumPoiIO daumPOIWriter = new DaumPoiIO("datafiles/pois/daum/daum_pois.json");
        ItemList itemList = daumPOIWriter.readItemSet("datafiles/pois/daum/daum_pois.json");
        Set<BasicPOI> poiSet = ItemConverter.getPOISet(itemList);

//        Set<Item> itemSet = itemList.getItemList();
        int i = 0;
        for (InterfacePOI poi : poiSet) {


            if (poi.getPoiType().category.equals("교통,수송")) {
                System.out.println(i + ',' + poi.getTitle() + '\t' + poi.getPoiType());
                i++;
            }
        }
    }

    public static void main(String[] args) {
        test();
    }
}
