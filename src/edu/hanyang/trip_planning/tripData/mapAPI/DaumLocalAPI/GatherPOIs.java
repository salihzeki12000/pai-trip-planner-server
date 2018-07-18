package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 10. 7
 * Time: 오전 3:09
 * To change this template use File | Settings | File Templates.
 */
public class GatherPOIs {

    private static Logger logger = Logger.getLogger(GatherPOIs.class);
    Set<BasicPOI> basicPOISet = new HashSet<BasicPOI>();
    int number = 1;

    public void gatherByTitles(List<String> titles) {
        List<Item> itemList = new ArrayList<Item>();
        for (String title : titles) {
            itemList.addAll(DaumLocalAPI.getPOIs(title, number));
        }
        for (Item item : itemList) {
            logger.debug(item);
        }
        basicPOISet.addAll(ItemConverter.getPOIList(itemList));
    }

    public void gatherByTitles(String... titles) {
        List<Item> itemList = new ArrayList<Item>();
        for (String title : titles) {
            itemList.addAll(DaumLocalAPI.getPOIs(title, number));
        }
        for (Item item : itemList) {
            logger.debug(item);
        }
        basicPOISet.addAll(ItemConverter.getPOIList(itemList));
    }


    public void readFromCSV(String csvFileName) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvFileName), '\t');
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }
            BasicPOI basicPOI = BasicPOI.parse(strArray);
            basicPOISet.add(basicPOI);
        }
    }

    /**
     * 기존 파일에 이어 쓰기
     *
     * @param csvFilename
     * @throws IOException
     */
    public void add2CSV(String csvFilename) throws IOException {
        readFromCSV(csvFilename);

        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        for (BasicPOI poi : basicPOISet) {
//            logger.debug(poi);
//            logger.debug(poi.getURL("place"));
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();

    }


    /**
     * 새로운 파일에 쓰기
     *
     * @param csvFilename
     * @throws IOException
     */
    public void write2CSV(String csvFilename) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        for (BasicPOI poi : basicPOISet) {
//            logger.debug(poi);
//            logger.debug(poi.getURL("place"));
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();

    }


    public static void main(String[] args) {
        GatherPOIs gatherPOIs = new GatherPOIs();
        List<String> titles = null;
        try {
            titles = Files.readAllLines(Paths.get("datafiles/pois/jejutitle.txt"));
            gatherPOIs.gatherByTitles(titles);
            logger.debug(titles);
            gatherPOIs.add2CSV("datafiles/pois/jeju.csv");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
