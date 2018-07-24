package edu.hanyang.trip_planning.tripData.navigation;

import au.com.bytecode.opencsv.CSVReader;

import edu.hanyang.trip_planning.tripData.dataType.AddressCode;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 19
 * Time: 오후 3:39
 * To change this template use File | Settings | File Templates.
 */
public class DomesticRegion {
    private static Logger logger = Logger.getLogger(DomesticRegion.class);
    String filename = "datafiles/movements/domestic_region.csv";
    Map<String, List<AddressCode>> regionMap = new HashMap<String, List<AddressCode>>();

    public DomesticRegion() {
        try {
            readCSVFile(filename);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void readCSVFile(String csvFileName) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(csvFileName), '\t');
        String header[] = csvReader.readNext();

        for (String strs[] : csvReader.readAll()) {
            if (strs[0].charAt(0) == '#') {
                continue;
            }
            String regionStr = strs[0];
            AddressCode addressCode;
            if (strs.length == 2) {
                addressCode = new AddressCode(strs[1]);
            } else if (strs.length == 3) {
                addressCode = new AddressCode(strs[1], strs[2]);
            } else if (strs.length == 4) {
                addressCode = new AddressCode(strs[1], strs[2], strs[3]);
            } else {
                throw new RuntimeException(Arrays.toString(strs));
            }
            List<AddressCode> addressCodeList = regionMap.get(regionStr);
            if (addressCodeList == null) {
                addressCodeList = new ArrayList<>();
            }
            addressCodeList.add(addressCode);
            regionMap.put(regionStr, addressCodeList);
        }

        csvReader.close();
    }

    public boolean isSameRegion(AddressCode code1, AddressCode code2) {
        String region1 = getRegion(code1);
        String region2 = getRegion(code2);
        return region1.equals(region2);
    }

    public String getRegion(AddressCode code) {
        for (Map.Entry<String, List<AddressCode>> entry : regionMap.entrySet()) {
            String regionStr = entry.getKey();
            List<AddressCode> addressCodeList = entry.getValue();
            if (contain(addressCodeList, code)) {
                return regionStr;
            }
        }
        throw new RuntimeException("Cannot find region for " + code);


    }

    private boolean contain(List<AddressCode> addressCodeList, AddressCode addressCode) {
        for (AddressCode code : addressCodeList) {
            if (code.contain(addressCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 1. 수도권
     * 2. 수도권외경기도
     * 3. 제주도
     */

    public static void main(String[] args) {
    }
}
