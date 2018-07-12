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

    public DomesticRegion(String filename) {
        this.filename = filename;
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
                addressCodeList = new ArrayList<AddressCode>();
            }
            addressCodeList.add(addressCode);
            regionMap.put(regionStr, addressCodeList);
        }

        csvReader.close();
//        logger.debug(regionMap);
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
        DomesticRegion domesticRegion = new DomesticRegion();
        List<AddressCode> addressCodeList = new ArrayList<AddressCode>();
        addressCodeList.add(new AddressCode("대한민국", "서울"));
        addressCodeList.add(new AddressCode("대한민국", "경기도", "양주시"));

        logger.debug(domesticRegion.contain(addressCodeList, new AddressCode("대한민국", "서울", "성동구")));
        logger.debug(domesticRegion.contain(addressCodeList, new AddressCode("대한민국")));

        logger.debug(domesticRegion.getRegion(new AddressCode("대한민국", "경기도", "양주시")));
//        InterfacePOI poi1 = POIManager.getInstance().getPOIByTitle("창동주공4단지아파트");
//        getRegion(poi1);
//        logger.debug(POIManager.getInstance().getPOIByTitle("양주역 1호선").getAddress());
//        logger.debug(POIManager.getInstance().getPOIByTitle("녹천역 1호선").getAddress());
//        logger.debug(POIManager.getInstance().getPOIByTitle("창동주공4단지아파트").getAddress());
//
//        logger.debug(getRegion(POIManager.getInstance().getPOIByTitle("창동주공4단지아파트")));
//        logger.debug(getRegion(POIManager.getInstance().getPOIByTitle("양주역 1호선")));
//        logger.debug(getRegion(POIManager.getInstance().getPOIByTitle("올래국수")));
//        logger.debug(getRegion(POIManager.getInstance().getPOIByTitle("제주국제공항")));
//        logger.debug(poi1);

    }
}
