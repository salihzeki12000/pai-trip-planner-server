package services.dummy;

import au.com.bytecode.opencsv.CSVReader;

import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import org.apache.log4j.Logger;
import services.datatype.PersonalScheduleEntry;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 14. 12. 10
 * Time: 오후 9:30
 * To change this template use File | Settings | File Templates.
 */
public class CalendarReader {
    private static Logger logger = Logger.getLogger(CalendarReader.class);

    public static List<PersonalScheduleEntry> readFromFile(String filename) throws IOException {
        List<PersonalScheduleEntry> scheduleTable = new ArrayList<PersonalScheduleEntry>();
        CSVReader reader = new CSVReader(new FileReader(filename), '\t');
        String headers[] = reader.readNext();
        List<String[]> rowStrList = reader.readAll();

        for (String[] rowStr : rowStrList) {
//            logger.debug(Arrays.toString(rowStr));
            if (rowStr[0].charAt(0) == '#') {
//                logger.debug("주석: " + Arrays.toString(rowStr));
                continue;
            }
            if (rowStr.length == 1) {
                continue;
            }
            String who = rowStr[0];
            String where = rowStr[1];
            String startTimeStr = rowStr[2];
            int durationMinute = Integer.parseInt(rowStr[3]);
            double duration = (double)durationMinute/60.0;
            PersonalScheduleEntry entry = new PersonalScheduleEntry(where, startTimeStr, new ProbabilisticDuration(duration, 0.1 * duration), "");
            scheduleTable.add(entry);
        }
        reader.close();
        return scheduleTable;
    }

    public static void readTest() {
        try {

            List<PersonalScheduleEntry> scheduleTable = CalendarReader.readFromFile("datafiles/calendars/권우영.csv");
            logger.debug(scheduleTable);


        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        readTest();
    }
}
