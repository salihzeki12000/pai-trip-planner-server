package edu.hanyang.trip_planning.tripData.poi;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wykwon on 2015-11-23.
 */
public class CategoryTest {
    private static Logger logger = Logger.getLogger(CategoryTest.class);
    public static void dumpCategory(String filename) {
        Set<String> titleSet = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String thisLine = null;
            while ((thisLine = br.readLine()) != null) {
                if (!thisLine.isEmpty()) {
//                    System.out.println(thisLine);
                    BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(thisLine);
                    System.out.println(basicPOI.getTitle() + "\t"+ basicPOI.getPoiType());
                    titleSet.add(thisLine);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.debug(titleSet.size());
    }

    public static void main(String[] args) {
        dumpCategory("datafiles/pois/제주관광지목록1.txt");
    }
}
