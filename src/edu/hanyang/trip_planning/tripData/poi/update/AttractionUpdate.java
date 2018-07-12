package edu.hanyang.trip_planning.tripData.poi.update;

import au.com.bytecode.opencsv.CSVReader;
import edu.hanyang.trip_planning.tripData.dataType.BusinessHour;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by wykwon on 2015-12-01.
 */
public class AttractionUpdate {
    private static Logger logger = Logger.getLogger(AttractionUpdate.class);

    public static void readAttractionInfo(String filename) throws IOException {
//logger.debug("읽어");
        CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }

            String title = strArray[0];
            String category = strArray[1];
            String subCategory = strArray[2];
            String subsubCategory = strArray[3];
            Integer cost = Integer.parseInt(strArray[4]);
            Double durationMin = Double.parseDouble(strArray[5]);
            Double durationMinSD = Double.parseDouble(strArray[6]);
            String openTime = strArray[7];
            String closeTime = strArray[8];
            BasicPOI basicPOI = POIManager.getInstance().getPOIByTitle(title);

            POIType poiType = new POIType(category, subCategory, subsubCategory);
            basicPOI.setPoiType(poiType);
            basicPOI.setAverageCostPerPerson(cost);
            basicPOI.setSpendingMinutes(durationMin, durationMinSD);

            BusinessHour businessHour = new BusinessHour(openTime,closeTime);
            basicPOI.setBusinessHour(businessHour);
            logger.debug(title + "\t" + category + "\t" + subCategory + "\t" + subsubCategory);
            basicPOI.setTouristAttractionType(subsubCategory);
        }
        POIManager.getInstance().update2CSV();

    }

    public static void execute() {
        try {
            readAttractionInfo("datafiles/pois/여행지update.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        execute();
    }
}
