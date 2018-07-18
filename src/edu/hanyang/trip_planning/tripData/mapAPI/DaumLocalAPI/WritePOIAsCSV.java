package edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI;

import au.com.bytecode.opencsv.CSVWriter;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 20
 * Time: 오후 1:13
 * To change this template use File | Settings | File Templates.
 */
public class WritePOIAsCSV {
    private static Logger logger = Logger.getLogger(WritePOIAsCSV.class);

    public static void writeCSV(Collection<BasicPOI> pois, String filename, char separator) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), separator, CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        Iterator<BasicPOI> it = pois.iterator();
        while (it.hasNext()) {
            InterfacePOI poi = it.next();
            if (poi instanceof BasicPOI) {
                BasicPOI basicPOI = (BasicPOI) poi;
                String strArray[] = basicPOI.toStrArray();
                csvWriter.writeNext(strArray);
            }
        }
        csvWriter.close();
    }

    public static String[] header() {
        return null;
    }


    public static void main(String[] args) {
        Collection<BasicPOI> poiCollection = POIManager.getInstance().getAll();
        try {
            writeCSV(poiCollection, "g:/old_pois.csv", '\t');
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
