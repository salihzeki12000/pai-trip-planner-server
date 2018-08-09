package edu.hanyang.trip_planning.tripData.poi;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import edu.hanyang.trip_planning.tripData.dataType.BusinessHour;
import edu.hanyang.trip_planning.tripData.dataType.ClosingDays;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.daumLocalAPI.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * POI instance 들 관리하는 클래스
 */
public class POIManager {
    private static Logger logger = Logger.getLogger(POIManager.class);
    private static POIManager poiManager = new POIManager();
    private Map<String, BasicPOI> poiMapById = new HashMap<>();
    private Map<String, BasicPOI> poiMapByTitle = new HashMap<>();
    private List<BasicPOI> poiList = new ArrayList<>();
    private String outFilename = "datafiles/pois/updated.csv";

    private Set<POIType> poiTypeSet = new HashSet<>();

    private POIManager() {
        try {
            // PathPOI라는 파일안에 pois.csv 파일의 경로및 이름이 있음.
            // 복수 갯수로 가능함
            List<String> filenames = Files.readAllLines(Paths.get("datafiles/pois/TestPathPois"));
//            List<String> strList = Files.readAllLines(Paths.get("datafiles/pois/PathPOI"));

            for (String filename : filenames) {
                readBasicPOIFromCSV(filename, '\t');
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static POIManager getInstance() {
        return poiManager;
    }

    public Collection<BasicPOI> getAll() {
        return poiMapByTitle.values();
    }

    public void addDaumItemList(ItemList itemList) {
        Set<BasicPOI> poiSet = ItemConverter.getPOISet(itemList);
        for (BasicPOI basicPOI : poiSet) {
            String placeURL = basicPOI.getURL("place");
            UpdatePlaceURLInfo updatePlaceInfo = new UpdatePlaceURLInfo(placeURL);
            double satisfication = updatePlaceInfo.getSatisfication();
            basicPOI.setScore(satisfication);
//            logger.debug(poi.getIdentifier().name + " \t별점="+satisfication);
            BusinessHour businessHour = updatePlaceInfo.businessTime();
            if (businessHour != null) {
//                logger.debug("영업시간:" + businessHour);
                basicPOI.setBusinessHour(businessHour);
            }
            ClosingDays closingDays = updatePlaceInfo.cLosingDays();
            if (closingDays != null) {
//                logger.debug("휴무일:" + closingDays);
                basicPOI.setClosingDays(closingDays);
            }
            poiMapById.put(basicPOI.getId(), basicPOI);
            poiMapByTitle.put(basicPOI.getTitle(), basicPOI);
            for (String otherName : basicPOI.getNames()) {
                poiMapByTitle.put(otherName, basicPOI);
            }
            poiList.add(basicPOI);
        }
    }

    public void addDaumItem(Item item) {
        BasicPOI basicPOI = ItemConverter.getPOI(item);
        String placeURL = basicPOI.getURL("place");
        UpdatePlaceURLInfo updatePlaceInfo = new UpdatePlaceURLInfo(placeURL);
        double satisfication = updatePlaceInfo.getSatisfication();
        basicPOI.setScore(satisfication);
//            logger.debug(poi.getIdentifier().name + " \t별점="+satisfication);
        BusinessHour businessHour = updatePlaceInfo.businessTime();
        if (businessHour != null) {
//                logger.debug("영업시간:" + businessHour);
            basicPOI.setBusinessHour(businessHour);
        }
        ClosingDays closingDays = updatePlaceInfo.cLosingDays();
        if (closingDays != null) {
//      logger.debug("휴무일:" + closingDays);
            basicPOI.setClosingDays(closingDays);
        }
        poiMapById.put(basicPOI.getId(), basicPOI);
        poiMapByTitle.put(basicPOI.getTitle(), basicPOI);
        for (String otherName : basicPOI.getNames()) {
            poiMapByTitle.put(otherName, basicPOI);
        }
        poiList.add(basicPOI);

    }

    public void writeBasicPOItoCSV(String filename, char separator) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), separator, CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        Iterator<BasicPOI> it = poiMapById.values().iterator();
        while (it.hasNext()) {
            BasicPOI poi = it.next();
            String strArray[] = poi.toStrArray();
            csvWriter.writeNext(strArray);
        }
        csvWriter.close();
    }

    public void readBasicPOIFromCSV(String filename, char separator) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filename), separator);
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }
            BasicPOI basicPOI = BasicPOI.parse(strArray);

            poiMapById.put(basicPOI.getId(), basicPOI);
            poiMapByTitle.put(basicPOI.getTitle(), basicPOI);
            for (String otherName : basicPOI.getNames()) {
                poiMapByTitle.put(otherName, basicPOI);
            }
            poiList.add(basicPOI);
            poiTypeSet.add(basicPOI.getPoiType());
        }
    }

    /**
     * 만족도 재수정
     */
    public int size() {
        return poiList.size();
    }

    public BasicPOI getPOIByIndex(int idx) {
        return poiList.get(idx);
    }

    // mgkim
    public List<BasicPOI> getPoiByAddresses(String... addresses) {
        List<BasicPOI> matchedPoiList = new ArrayList<>();
        for (BasicPOI poi : poiList) {
            boolean match = false;  //false
            for (String add : addresses) {
                if (add.equals(poi.getAddress().addressCode.countryCode)) {
                    match = true;
                    break;
                } else if (add.equals(poi.getAddress().addressCode.provinceCode)) {
                    match = true;
                    break;
                } else if (add.equals(poi.getAddress().addressCode.cityCode)) {
                    match = true;
                    break;
                }
            }
            if (match) {
                matchedPoiList.add(poi);
            }
        }
        return matchedPoiList;
    }

    // mgkim
    public List<BasicPOI> getPoiByType(POIType poiType) {
        List<BasicPOI> newPoiList = new ArrayList<>();
        for (int j = 0; j < poiList.size(); j++) {                  // 각각의 poiList 내 basicPoi에 대해서
            if (poiType.contain(poiList.get(j).getPoiType())) {     // poiType이 같으면
                newPoiList.add(poiList.get(j));                     // 새로운 list에 추가
            }
        }
        return newPoiList;
    }

    private void getFromDaum() {
        DaumPoiIO daumPoiIO = new DaumPoiIO();
        ItemList itemList = daumPoiIO.readItemList();
        poiMapById = ItemConverter.getPOIMap(itemList);
    }

    public void write2CSV(String csvFilename) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        for (BasicPOI poi : poiMapById.values()) {
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();

    }

    public void update2CSV() throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(this.outFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());
        for (BasicPOI poi : poiMapById.values()) {
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();

    }

    public static void convertItemlist2CSV(String daumPOIFilename, String csvFilename) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPOI.csvHeader());

        DaumPoiIO daumPoiIO = new DaumPoiIO(daumPOIFilename);
        ItemList itemList = daumPoiIO.readItemList();
        logger.debug(itemList.getItemList().size());
        Set<BasicPOI> poiSet = ItemConverter.getPOISet(itemList);

        for (BasicPOI poi : poiSet) {
            logger.debug(poi.getURL("place"));
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();
    }

    public BasicPOI getPOIByID(String id) {
        return poiMapById.get(id);
    }

    public BasicPOI getPOIByTitle(String title) {
        BasicPOI basicPOI = poiMapByTitle.get(title);
        if (basicPOI == null) {
            Item item = DaumLocalAPI.getPOI(title);
            addDaumItem(item);
            title = item.getTitle();
            basicPOI = poiMapByTitle.get(title);
            if (basicPOI == null) {
                throw new RuntimeException("cannot find such poi " + title);
            } else {
                try {
                    write2CSV(this.outFilename);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return basicPOI;
    }

    public double distance(BasicPOI src, BasicPOI dest) {
        return VincentyDistanceCalculator.getDistance(src.getLocation().latitude, src.getLocation().longitude,
                dest.getLocation().latitude, dest.getLocation().longitude);
    }

    public void updatePlaceURLinfo() {
        Collection<BasicPOI> pois = poiMapById.values();
        for (BasicPOI poi : pois) {
            String placeURL = poi.getURL("place");
//            logger.debug(placeURL);
            UpdatePlaceURLInfo updatePlaceInfo = new UpdatePlaceURLInfo(placeURL);
            double satisfaction = updatePlaceInfo.getSatisfication();
            poi.setScore(satisfaction);
//            logger.debug(poi.getIdentifier().name + " \t별점="+satisfaction);
            BusinessHour businessHour = updatePlaceInfo.businessTime();
            if (businessHour != null) {
                logger.debug("영업시간:" + businessHour);
                poi.setBusinessHour(businessHour);
            }
            ClosingDays closingDays = updatePlaceInfo.cLosingDays();
            if (closingDays != null) {
                logger.debug("휴무일:" + closingDays);
                poi.setClosingDays(closingDays);
            }
        }
    }

    /**
     * daum에서 키워드 검색을 통해 POI를 업데이트 함
     *
     * @param keywords      장소 키워드
     * @param numCandidates 키워드당 장소 후보 갯수
     */
    public void updatePOIsFromDaum(int numCandidates, String... keywords) {
        for (String keyword : keywords) {
            List<Item> itemList = DaumLocalAPI.getPOIs(keyword, numCandidates);
            for (Item item : itemList) {
                addDaumItem(item);
            }
        }
    }

    public static void updatePOIsFromFile(String filename) {
        Set<String> titleSet = new HashSet<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String thisLine;
            while ((thisLine = br.readLine()) != null) {
                if (!thisLine.isEmpty()) {
                    System.out.println(thisLine);
                    POIManager.getInstance().getPOIByTitle(thisLine);
                    titleSet.add(thisLine);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.debug(titleSet.size());
    }

    public POIType getPOIType(String name) {
        // 1st category
        for (POIType poiType : poiTypeSet) {
            if (poiType.category.equals(name)) {
                return poiType;
            }
        }
        for (POIType poiType : poiTypeSet) {
            if (poiType.subCategory.equals(name)) {
                return poiType;
            }
        }

        for (POIType poiType : poiTypeSet) {
            if (poiType.subSubCategory.equals(name)) {
                return poiType;
            }
        }

        return null;
    }

    public static void main(String[] args) {
        POIManager.updatePOIsFromFile("datafiles/pois/제주식당갱신.txt");
        Set<POIType> poiTypes = new HashSet<POIType>();
        for (BasicPOI basicPOI : POIManager.getInstance().getAll()) {
            if (basicPOI.getPoiType().category.equals("음식점")) {
                poiTypes.add(basicPOI.getPoiType());
            }
        }

        for (POIType poiType : poiTypes) {
            System.out.println(poiType.subCategory + "\t" + poiType.subSubCategory);
        }
    }
}
