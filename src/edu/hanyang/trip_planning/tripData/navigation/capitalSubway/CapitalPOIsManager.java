package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import au.com.bytecode.opencsv.CSVReader;

import edu.hanyang.trip_planning.tripData.dataType.Location;
import edu.hanyang.trip_planning.tripData.navigation.MinimalPOI;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.hanyang.trip_planning.tripData.poi.POIUtil;
import org.apache.log4j.Logger;
import wykwon.common.MyCollections;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 27
 * Time: 오후 5:06
 * To change this template use File | Settings | File Templates.
 */
public class CapitalPOIsManager {
    private Map<String, MinimalPOI> minimalPOIMap = new HashMap<String, MinimalPOI>();
    private String filename = "datafiles/movements/CapitalSubwayPOIs.csv";
    private static Logger logger = Logger.getLogger(CapitalPOIsManager.class);

    public CapitalPOIsManager() {
        try {
            loadFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public CapitalPOIsManager(String filename) {
        this.filename = filename;
        try {
            loadFile();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void loadFile() throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(this.filename), '\t');
        csvReader.readNext();

        List<String[]> strArrayList = csvReader.readAll();
        for (String strArray[] : strArrayList) {
            if (strArray[0].charAt(0) == '#') {
                continue;
            }
            MinimalPOI poi = new MinimalPOI(strArray);
            minimalPOIMap.put(poi.getTitle(), poi);
//            logger.debug(poi);
        }
        csvReader.close();
    }

    public MinimalPOI getPOI(String title) {
        return minimalPOIMap.get(title);
    }

    /**
     * 인자 poi로부터 일정거리 이내에서 가장 가까운 데 고름
     *
     * @param location
     * @param range
     * @return
     */
    public MinimalPOI getNearestPOI(Location location, double range) {

        List<MinimalPOI> poiList = new ArrayList<MinimalPOI>();
        List<Double> distList = new ArrayList<Double>();
        for (MinimalPOI poi : minimalPOIMap.values()) {
//            logger.debug(poi.getIdentifier().name + " --> " + poi.getPoiType().category);

            double dist = POIUtil.distance(location.latitude, location.longitude,
                    poi.getLocation().latitude, poi.getLocation().longitude);
            if (dist < range) {
                poiList.add(poi);
                distList.add(dist);
            }
        }


        if (poiList.size() == 0) {
            return null;
        } else {
            int argMin = MyCollections.argMin(distList);
            if (argMin < 0) {
                return null;
            } else {
                return poiList.get(argMin);
            }
        }

    }

    public static void main(String[] args) {
        CapitalPOIsManager capitalPOIsManager = new CapitalPOIsManager();
        logger.debug(capitalPOIsManager.getPOI("홍대입구역 경의중앙선"));
        InterfacePOI poi = POIManager.getInstance().getPOIByTitle("창동주공4단지아파트");
        logger.debug(capitalPOIsManager.getNearestPOI(poi.getLocation(), 2));
    }
}
