package edu.hanyang.trip_planning.tripData.navigation;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
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
public class DomesticNavigationPOIsManager {
    private Map<String, MinimalPOI> minimalPOIMap = new HashMap<>();
    private String filename = "datafiles/movements/domesticNavigatgionPOIs.csv";
    private static Logger logger = Logger.getLogger(DomesticNavigationPOIsManager.class);

    public DomesticNavigationPOIsManager() {
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
        }
        csvReader.close();
    }

    public MinimalPOI getPOI(String title) {
        return minimalPOIMap.get(title);
    }


    public static void main(String[] args) {
    }
}
