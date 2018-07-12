package edu.hanyang.trip_planning.tripData.navigation;

import au.com.bytecode.opencsv.CSVReader;
import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 27
 * Time: 오후 4:02
 * To change this template use File | Settings | File Templates.
 */
public class DomesticNavigationTable {


    private String filename = "datafiles/movements/domestic_navigation.csv";
    private List<DomesticNavigationTableEntry> entryList = new ArrayList<DomesticNavigationTableEntry>();
    private DomesticNavigationPOIsManager domesticNavigationPOIsManager = new DomesticNavigationPOIsManager();

    class DomesticNavigationTableEntry {
        DomesticNavigationTableEntry(String strArray[]) {
            this.srcRegion = strArray[0];
            this.destRegion = strArray[1];
            this.srcPOIName = strArray[2];
            this.destPOIName = strArray[3];
            this.movementWay = strArray[4];
            this.distance = Integer.parseInt(strArray[5]);
            this.time = Integer.parseInt(strArray[6]);
            this.timeSD = Integer.parseInt(strArray[7]);
            this.cost = Integer.parseInt(strArray[8]);
        }

        public String srcRegion;
        public String destRegion;
        public String srcPOIName;
        public String destPOIName;
        public String movementWay;
        public int distance;
        public int time;
        public int timeSD;
        public int cost;

        @Override
        public String toString() {
            return "DomesticNavigationTableEntry{" +
                    "srcRegion='" + srcRegion + '\'' +
                    ", destRegion='" + destRegion + '\'' +
                    ", srcPOIName='" + srcPOIName + '\'' +
                    ", destPOIName='" + destPOIName + '\'' +
                    ", movementWay='" + movementWay + '\'' +
                    ", distance=" + distance +
                    ", time=" + time +
                    ", timeSD=" + timeSD +
                    ", cost=" + cost +
                    '}';
        }
    }


    public DomesticNavigationTable() {
        try {
            load(filename);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public DomesticNavigationTable(String filename) {
        this.filename = filename;
        try {
            load(filename);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void load(String filename) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
        List<String[]> strArrayList = csvReader.readAll();
        for (String strs[] : strArrayList) {
//            logger.debug(Arrays.toString(strs));
            if (strs[0].charAt(0) == '#') {
                continue;
            }
            if (strs.length != 9) {
                throw new RuntimeException("column size must be 10");
            }
            DomesticNavigationTableEntry entry = new DomesticNavigationTableEntry(strs);
            entryList.add(entry);

        }
        csvReader.close();
    }

    public UnitMovement getBaseMovement(String srcRegion, String destRegion) {
        // 양방향을 찾아야 하니까..
        // 1. 정방향
        for (DomesticNavigationTableEntry entry : entryList) {
//            logger.debug(entry);
            if (srcRegion.equals(entry.srcRegion) && destRegion.equals(entry.destRegion)) {
                ProbabilisticDuration pDuration = new ProbabilisticDuration((double) entry.time, (double) entry.timeSD);

                MinimalPOI srcPOI = domesticNavigationPOIsManager.getPOI(entry.srcPOIName);
                MinimalPOI destPOI = domesticNavigationPOIsManager.getPOI(entry.destPOIName);
//                logger.debug(entry.srcPOIName+ "-> "+ srcPOI);
//                logger.debug(destPOI);
                UnitMovement unitMovement = new UnitMovement(srcPOI.getTitle(), srcPOI.getLocation(), destPOI.getTitle(), destPOI.getLocation(), entry.movementWay, pDuration, entry.cost);
//                logger.debug(unitMovement);
                return unitMovement;
            }
        }
        // 2. 역방향
        for (DomesticNavigationTableEntry entry : entryList) {
//            logger.debug(entry);
            if (srcRegion.equals(entry.destRegion) && destRegion.equals(entry.srcRegion)) {
                ProbabilisticDuration pDuration = new ProbabilisticDuration((double) entry.time, (double) entry.timeSD);

                MinimalPOI srcPOI = domesticNavigationPOIsManager.getPOI(entry.destPOIName);
                MinimalPOI destPOI = domesticNavigationPOIsManager.getPOI(entry.srcPOIName);
                UnitMovement unitMovement = new UnitMovement(srcPOI.getTitle(), srcPOI.getLocation(), destPOI.getTitle(), destPOI.getLocation(), entry.movementWay, pDuration, entry.cost);
//                logger.debug(unitMovement);
                return unitMovement;
            }
        }

        throw new RuntimeException("Cannot find baseMovement from " + srcRegion + " to " + destRegion);
    }

    public static void main(String[] args) {
        DomesticNavigationTable table = new DomesticNavigationTable();
        table.getBaseMovement("수도권", "제주");
        table.getBaseMovement("제주", "수도권");

    }
}
