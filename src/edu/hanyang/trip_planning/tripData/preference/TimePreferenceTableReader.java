package edu.hanyang.trip_planning.tripData.preference;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 26
 * Time: 오후 5:13
 * To change this template use File | Settings | File Templates.
 */
public class TimePreferenceTableReader {
    private static Logger logger = Logger.getLogger(TimePreferenceTableReader.class);

    public static PersonalFoodPreferenceTable read(String userName, String filename) {
        PersonalFoodPreferenceTable personalFoodPreferenceTable = new PersonalFoodPreferenceTable(userName);
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filename));
            List<String[]> rowList = csvReader.readAll();
            for (String rowStr[] : rowList) {
                if (rowList.size() == 0) {
                    continue;
                } else if (rowStr[0].length() == 0) {
                    continue;
                } else if (rowStr[0].charAt(0) == '#') {
                    continue;
                } else {
                    String typeStr = rowStr[0];
                    String subTypeStr = rowStr[1];
                    double value = Double.parseDouble(rowStr[2]);
                    personalFoodPreferenceTable.addPreference(typeStr, subTypeStr, value);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return personalFoodPreferenceTable;
    }


    public static void main(String[] args) {
        PersonalFoodPreferenceTable table = read("권우영", "documents/foodPreference_wykwon.csv");
        logger.debug(table);
    }
}
