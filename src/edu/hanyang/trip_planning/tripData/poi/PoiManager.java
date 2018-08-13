package edu.hanyang.trip_planning.tripData.poi;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import edu.hanyang.trip_planning.tripData.dataType.PoiType;
import edu.hanyang.trip_planning.tripData.daumLocalAPI.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

// POI instance 들 관리하는 클래스
public class PoiManager {
    private static PoiManager poiManager = new PoiManager();
    private Map<String, BasicPoi> poiMapById = new HashMap<>();
    private Map<String, BasicPoi> poiMapByTitle = new HashMap<>();
    private List<BasicPoi> poiList = new ArrayList<>();
    private String outFilename = "datafiles/pois/updated.csv";
    private Set<PoiType> poiTypeSet = new HashSet<>();

    private PoiManager() {
        try {
            // PathPOI라는 파일안에 pois.csv 파일의 경로및 이름이 있음.
            // 복수 갯수로 가능함
            List<String> filenames = Files.readAllLines(Paths.get("datafiles/pois/TestPathPois"));
//            List<String> strList = Files.readAllLines(Paths.get("datafiles/pois/PathPOI"));

            for (String filename : filenames) {
                readBasicPoiFromCSV(filename, '\t');
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static PoiManager getInstance() {
        return poiManager;
    }

    public Collection<BasicPoi> getAll() {
        return poiMapByTitle.values();
    }

    public void addDaumItem(Item item) {
        BasicPoi basicPoi = ItemConverter.getPoi(item);

        poiMapById.put(basicPoi.getId(), basicPoi);
        poiMapByTitle.put(basicPoi.getTitle(), basicPoi);
        poiList.add(basicPoi);
    }

    public void writeBasicPoitoCSV(String filename, char separator) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), separator, CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPoi.csvHeader());
        Iterator<BasicPoi> it = poiMapById.values().iterator();
        while (it.hasNext()) {
            BasicPoi poi = it.next();
            String strArray[] = poi.toStrArray();
            csvWriter.writeNext(strArray);
        }
        csvWriter.close();
    }

    public void readBasicPoiFromCSV(String filename, char separator) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filename), separator);
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }
            BasicPoi basicPoi = BasicPoi.parse(strArray);

            poiMapById.put(basicPoi.getId(), basicPoi);
            poiMapByTitle.put(basicPoi.getTitle(), basicPoi);
            poiList.add(basicPoi);
            poiTypeSet.add(basicPoi.getPoiType());
        }
    }

    public int size() {
        return poiList.size();
    }

    public BasicPoi getPoiByIndex(int idx) {
        return poiList.get(idx);
    }

    public List<BasicPoi> getPoiListByAddresses(String... addresses) {
        List<BasicPoi> matchedPoiList = new ArrayList<>();
        for (BasicPoi poi : poiList) {
            for (String address: addresses){
                if(poi.getAddress().contains(address)){
                    matchedPoiList.add(poi);
                    break;
                }
            }
        }
        return matchedPoiList;
    }

    public List<BasicPoi> getPoiByType(PoiType poiType) {
        List<BasicPoi> newPoiList = new ArrayList<>();
        for (int j = 0; j < poiList.size(); j++) {                  // 각각의 poiList 내 basicPoi에 대해서
            if (poiType.contain(poiList.get(j).getPoiType())) {     // poiType이 같으면
                newPoiList.add(poiList.get(j));                     // 새로운 list에 추가
            }
        }
        return newPoiList;
    }

    public void write2CSV(String csvFilename) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPoi.csvHeader());
        for (BasicPoi poi : poiMapById.values()) {
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();

    }

    public void update2CSV() throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(this.outFilename), '\t', CSVWriter.NO_QUOTE_CHARACTER);
        csvWriter.writeNext(BasicPoi.csvHeader());
        for (BasicPoi poi : poiMapById.values()) {
            csvWriter.writeNext(poi.toStrArray());
        }
        csvWriter.close();
    }

    public BasicPoi getPoiById(String id) {
        return poiMapById.get(id);
    }

    public BasicPoi getPoiByTitle(String title) {
        BasicPoi basicPoi = poiMapByTitle.get(title);
        if (basicPoi == null) {
            Item item = DaumLocalAPI.getPoi(title);
            addDaumItem(item);
            title = item.getTitle();
            basicPoi = poiMapByTitle.get(title);
            if (basicPoi == null) {
                throw new RuntimeException("cannot find such poi " + title);
            } else {
                try {
                    write2CSV(this.outFilename);
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return basicPoi;
    }

    public double distance(BasicPoi src, BasicPoi dest) {
        return VincentyDistanceCalculator.getDistance(src.getLocation().latitude, src.getLocation().longitude,
                dest.getLocation().latitude, dest.getLocation().longitude);
    }

    // daum에서 키워드 검색을 통해 POI를 업데이트 함
    public void updatePoisFromDaum(int numCandidates, String... keywords) {
        for (String keyword : keywords) {
            List<Item> itemList = DaumLocalAPI.getPois(keyword, numCandidates);
            for (Item item : itemList) {
                addDaumItem(item);
            }
        }
    }

    public PoiType getPoiType(String name) {
        for (PoiType poiType : poiTypeSet) {
            if (poiType.category.equals(name)) {
                return poiType;
            }
        }
        for (PoiType poiType : poiTypeSet) {
            if (poiType.subCategory.equals(name)) {
                return poiType;
            }
        }
        for (PoiType poiType : poiTypeSet) {
            if (poiType.subSubCategory.equals(name)) {
                return poiType;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String filename = "datafiles/180813142600_제주특별자치도_basicpoi_new.json";
        List<BasicPoi> basicPoiList = PoiManager.getInstance().poiList;
        Gson gson = new Gson();
        String json = gson.toJson(basicPoiList);
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
