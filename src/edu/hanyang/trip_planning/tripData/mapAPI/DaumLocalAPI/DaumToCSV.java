package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import au.com.bytecode.opencsv.CSVWriter;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;
import util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Daum local api를 저장함.
 * json object[item] 의 array형태로 저장됨
 * <p/>
 * 파일은 append형태로 이루어지며,
 */

public class DaumToCSV {
    private static Logger logger = Logger.getLogger(DaumToCSV.class);

    List<Pair<String, Integer>> keywords = new ArrayList<Pair<String, Integer>>();

    public DaumToCSV() {

    }


    public void addKeyword(String keyword, int number) {
        keywords.add(new Pair<String, Integer>(keyword, number));
    }

    public void addKeyWords(Collection<String> keywords, int number) {
        for (String keyword : keywords) {
            addKeyword(keyword, number);
        }
    }

    public void saveToCSV(String filename) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());


        List<BasicPOI> poiList = new ArrayList<BasicPOI>();
        for (Pair<String, Integer> pair : keywords) {
            List<Item> itemList = DaumLocalAPI.getPOIs(pair.first(), pair.second());
            for (Item item : itemList) {
                BasicPOI poi = ItemConverter.getPOI(item);
                poiList.add(poi);
//                logger.debug(Arrays.toString(poi.toStrArray()));
                csvWriter.writeNext(poi.toStrArray());
            }
        }
        csvWriter.close();
    }

    public static void main(String[] args) {
        DaumToCSV daumToCSV = new DaumToCSV();
        daumToCSV.addKeyword("제주 성산읍 식당", 5);
        try {
            daumToCSV.saveToCSV("datafiles/pois/tmppois.csv");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
