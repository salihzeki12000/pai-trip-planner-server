package tripPlanning.tripData.poi;

import com.google.gson.Gson;
import tripPlanning.tripData.dataType.PoiType;
import tripPlanning.tripData.daumLocalAPI.*;

import java.io.*;
import java.util.*;

// POI instance 들 관리하는 클래스
public class PoiManager {
    private static PoiManager poiManager = new PoiManager();
    private List<BasicPoi> poiList = new ArrayList<>();
    private Map<Integer, BasicPoi> poiMapById = new HashMap<>();
    private Map<String, BasicPoi> poiMapByTitle = new HashMap<>();
    private Set<PoiType> poiTypeSet = new HashSet<>();
    private Gson GSON = new Gson();

    private PoiManager() {
        List<String> filenames = new ArrayList<>();
        filenames.add("datafiles/180813142600_제주특별자치도_basicpoi_new.json");  //TODO: needs edition
        for (String filename : filenames) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(filename));
                BasicPoi[] basicPois = GSON.fromJson(br, BasicPoi[].class);
                for (BasicPoi basicPoi : basicPois) {
                    poiMapById.put(basicPoi.getId(), basicPoi);
                    poiMapByTitle.put(basicPoi.getName(), basicPoi);
                    poiList.add(basicPoi);
                    poiTypeSet.add(basicPoi.getPoiType());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static PoiManager getInstance() {
        return poiManager;
    }

    public Collection<BasicPoi> getAll() {
        return poiMapByTitle.values();
    }

    public int size() {
        return poiList.size();
    }

    public List<BasicPoi> getPoiListByAddresses(String... addresses) {
        List<BasicPoi> matchedPoiList = new ArrayList<>();
        for (BasicPoi poi : poiList) {
            for (String address : addresses) {
                if (poi.getAddress().contains(address)) {
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

    public BasicPoi getPoiByTitle(String title) {
        BasicPoi basicPoi = poiMapByTitle.get(title);
        if (basicPoi == null) {
            Item item = DaumLocalAPI.getPoi(title);
            basicPoi = ItemConverter.getPoi(item);
            if (basicPoi == null) {
                throw new RuntimeException("cannot find such poi " + title);
            } else {
                poiMapById.put(basicPoi.getId(), basicPoi);
                poiMapByTitle.put(basicPoi.getName(), basicPoi);
                poiList.add(basicPoi);
            }
        }
        return basicPoi;
    }

    public double distance(BasicPoi src, BasicPoi dest) {
        return VincentyDistanceCalculator.getDistance(src.getLocation().latitude, src.getLocation().longitude,
                dest.getLocation().latitude, dest.getLocation().longitude);
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
