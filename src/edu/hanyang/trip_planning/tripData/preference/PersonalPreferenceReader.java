package edu.hanyang.trip_planning.tripData.preference;

import au.com.bytecode.opencsv.CSVReader;
import edu.hanyang.trip_planning.tripData.dataType.ActivityType;
import edu.hanyang.trip_planning.tripData.dataType.TimeAndDuration;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 26
 * Time: 오후 5:13
 * To change this template use File | Settings | File Templates.
 */
public class PersonalPreferenceReader {
    private static Logger logger = Logger.getLogger(PersonalPreferenceReader.class);

    public static PersonalFoodPreferenceTable readFoodPreference(String userName, String filename) {
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


    public static PersonalLocationPreferenceTable readLocationPreference(String userName, String fileName) {
        PersonalLocationPreferenceTable personallocationPreferenceTable = new PersonalLocationPreferenceTable(userName);
        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName));
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
//                    personallocationPreferenceTable.addPrefeence(typeStr, subTypeStr, value);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return personallocationPreferenceTable;
    }

    /**
     * 파일 포멧 좀 다르다.
     * <p/>
     * 첫번째 열은 header여야 함.
     * <p/>
     * 첫번째 열의 첫번째 행은 시간간격(interval)
     *
     * @param userName
     * @param fileName
     * @return
     */
    public static PersonalStartTimePreferenceTable readTimePreference(String userName, String fileName) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader(fileName), '\t');
            String headers[] = csvReader.readNext();

            int timeInterval = Integer.parseInt(headers[0]);

            logger.debug("time interval = " + timeInterval + "\t" + Arrays.toString(headers));
            PersonalStartTimePreferenceTable timePreferenceTable = new PersonalStartTimePreferenceTable(userName, timeInterval);


            List<String[]> rowList = csvReader.readAll();
            for (String rowStr[] : rowList) {
                if (rowList.size() == 0) {
                    continue;
                } else if (rowStr[0].length() == 0) {
                    continue;
                } else if (rowStr[0].charAt(0) == '#') {
                    continue;
                } else {
                    String timeStr = rowStr[0];
                    DateTime time = timePreferenceTable.parseDateTime(timeStr);
                    int hourOfDay = time.getHourOfDay();
                    int minuteOfHour = time.getMinuteOfHour();
//                    double values[] = new double[rowStr.length - 1];

                    for (int i = 1; i < rowStr.length; i++) {
                        double value = Double.parseDouble(rowStr[i]);
                        timePreferenceTable.addTimePreference(headers[i], hourOfDay, minuteOfHour, value);
                    }
//                    logger.debug("timeStr=" + timeStr + "values=" + Arrays.toString(values));
                }
            }

            return timePreferenceTable;

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        throw new RuntimeException("Error in reading file " + fileName);
    }

    public static void main(String[] args) {
        PersonalFoodPreferenceTable foodTable = readFoodPreference("권우영", "documents/foodPreference_wykwon.csv");
        PersonalStartTimePreferenceTable timeTable = readTimePreference("권우영,", "documents/timePreference_wykwon.csv");

        logger.debug(timeTable.timePreference(new TimeAndDuration("12:30", 60), ActivityType.Eat));
//        logger.debug(foodTable);
        //      logger.debug(timeTable);
    }
}
