package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import edu.hanyang.trip_planning.tripData.poi.POIManager;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 26
 * Time: 오후 7:47
 * To change this template use File | Settings | File Templates.
 */
public class GatheringPOIsFromDaum {

    public String keywords[] = {"성산일출봉", "만장굴", "사려니숲길", "섭지코지", "용두암", "삼양검은모래해변"};

    public void findAndSave() {
        POIManager poiManager = POIManager.getInstance();
        poiManager.updatePOIsFromDaum(1, keywords);
        try {
            poiManager.write2CSV("datafiles/pois/pois.csv");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        GatheringPOIsFromDaum g = new GatheringPOIsFromDaum();
        g.findAndSave();
    }
}
