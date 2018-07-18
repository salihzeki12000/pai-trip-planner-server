package edu.hanyang.trip_planning.tripHTBN.poi;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;

import edu.hanyang.trip_planning.trip_question.DailyTripEntry;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.util.*;

public class SubsetPOIs {
    private static Logger logger = Logger.getLogger(DailyTripEntry.class);
    List<BasicPOI> poiList = new ArrayList<BasicPOI>();
    String titleArray[];
    Map<String, Integer> nameIndexMap = new FastMap<>();

    public SubsetPOIs(){};

    public SubsetPOIs(BasicPOI pois[]){
        for (int i=0; i<pois.length;i++){
            poiList.add(pois[i]);
        }
    }

    public SubsetPOIs(String... titles){
        makeSubsetPOIsByTitle(titles);
    }

    public void makeSubsetPOIsByTitle(String... titles) {
        POIManager poiManager = POIManager.getInstance();
        int i = 0;
        for (String title : titles) {
            BasicPOI basicPOI = poiManager.getPOIByTitle(title);
            if (basicPOI == null) {
                throw new RuntimeException("cannot find " + title);
            }
            poiList.add(basicPOI);
            nameIndexMap.put(basicPOI.getTitle(), i);
            i++;
        }
        titleArray = new String[titles.length];
        for (int j = 0; j < poiList.size(); j++) {
            titleArray[j] = poiList.get(j).getTitle();
        }
    }
    public void makeSubsetPOIsByTitle(Collection<String>  titles) {
//        logger.debug(titles);
        POIManager poiManager = POIManager.getInstance();
        int i = 0;
        for (String title : titles) {
            BasicPOI basicPOI = poiManager.getPOIByTitle(title);
            if (basicPOI == null) {
                throw new RuntimeException("cannot find " + title);
            }
            poiList.add(basicPOI);
            nameIndexMap.put(basicPOI.getTitle(), i);
            i++;
        }
        titleArray = new String[titles.size()];
        for (int j = 0; j < poiList.size(); j++) {
            titleArray[j] = poiList.get(j).getTitle();
        }
    }
    public void makeSubsetPOIsByIdentifiers(String... identifiers) {
        POIManager poiManager = POIManager.getInstance();
        int i = 0;
        for (String identifier : identifiers) {
            BasicPOI basicPOI = poiManager.getPOIByID(identifier);
            if (basicPOI == null) {
                throw new RuntimeException("cannot find " + identifier);
            }
            poiList.add(basicPOI);
            nameIndexMap.put(basicPOI.getTitle(), i);
            i++;
        }
        titleArray = new String[identifiers.length];
        for (int j = 0; j < poiList.size(); j++) {
            titleArray[j] = poiList.get(j).getTitle();
        }
    }
    public void makeSubsetPOIsByIndices(int indices[]) {
        POIManager poiManager = POIManager.getInstance();
        int i = 0;
        for (int idx : indices) {
            BasicPOI basicPOI = poiManager.getPOIByIndex(idx);
            if (basicPOI == null) {
                throw new RuntimeException("cannot find " + idx);
            }
            poiList.add(basicPOI);
            nameIndexMap.put(basicPOI.getTitle(), i);
            i++;
        }
        titleArray = new String[indices.length];
        for (int j = 0; j < poiList.size(); j++) {
            titleArray[j] = poiList.get(j).getTitle();
        }
    }
    // mgkim:
    public void makeSubsetPOIsByAreas(String... areas) {
        // {서귀포시, 부산}->서귀포시+부산 / {성산읍,제주시}->성산읍+제주시 / {성산읍,서귀포시}->서귀포시
        POIManager poiManager = POIManager.getInstance();
        poiList.addAll(poiManager.getPoiByAddresses(areas));
        titleArray = new String[poiList.size()];
        nameIndexMap = new FastMap<>();
        for (int i = 0; i < poiList.size(); i++) {
            nameIndexMap.put(poiList.get(i).getTitle(), i);
            titleArray[i] = poiList.get(i).getTitle();
        }
    }
    // mgkim:
    public void addSubsetPOIsBytitle(String... titles) {
        ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(titleArray));
        for (String title:titles) {
            if (!titleList.contains(title)) {
                poiList.add(POIManager.getInstance().getPOIByTitle(title));
                nameIndexMap.put(poiList.get(poiList.size()-1).getTitle(), poiList.size()-1);
                titleList.add(title);
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }
    // mgkim:
    public void reduceSubsetPoisByTitleList(Collection<String> titles){
        ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(titleArray));
        for (String title : titles) {
            if (!titleList.contains(title)){
                int poiIdx = getPOIIdx(title);
                poiList.remove(poiIdx);
                nameIndexMap.remove(title);
                titleList.remove(poiIdx);
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }
    // mgkim:
    public void reduceSubsetPoisByTitles(String... titles){
        ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(titleArray));
        for (String title : titles) {
            if (titleList.contains(title)){
                int poiIdx = getPOIIdx(title);
                poiList.remove(poiIdx);
                nameIndexMap.remove(title);
                titleList.remove(poiIdx);
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }
    // mgkim:
    public void reduceSubsetPoisByIdxes(int... idxes){
        Arrays.sort(idxes);
        ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(titleArray));
        for (int i = idxes.length; i > 0; i--) {
            poiList.remove(idxes[i-1]);
            nameIndexMap.remove(titleList.get(idxes[i-1]));
            titleList.remove(idxes[i-1]);
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }
    // mgkim:
    public void reduceSubsetPoisByIdList(Collection<String> idList) {
        ArrayList<String> titleList = new ArrayList<String>(Arrays.asList(titleArray));
        for (String id : idList) {
            for (int poiIdx = 0; poiIdx < poiList.size(); poiIdx++) {
                BasicPOI basicPOI = poiList.get(poiIdx);
                if (id.equals(basicPOI.getId())) {
                    String poiTitle = getPOI(poiIdx).getTitle();
                    poiList.remove(poiIdx);
                    nameIndexMap.remove(poiTitle);
                    titleList.remove(poiIdx);
                }
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }
    // mgkim:
    public void reduceSubsetPoisByScore(int NumSubsetPois) {
        Collections.sort(poiList, new CompareBasicPoiByScore());
        int poiListSize = poiList.size();
        for (int i = NumSubsetPois; i < poiListSize; i++) {
            poiList.remove(NumSubsetPois);
        }
        nameIndexMap = new FastMap<>();
        titleArray = new String[NumSubsetPois];
        for (int i = 0; i < NumSubsetPois; i++) {
            titleArray[i] = poiList.get(i).getTitle();
            nameIndexMap.put(poiList.get(i).getTitle(), i);
        }
    }
    // mgkim:
    public void reduceSubsetPoisByScoreAndConstraint(int numTotalPoi, int numConstrainedTypePoi, List<CategoryConstraint> categoryConstraintList) {
        Collections.sort(poiList, new CompareBasicPoiByScore());
        int numCategoryConstraint = categoryConstraintList.size();

        List<BasicPOI> newPoiList = new ArrayList<>();
        for (int i = 0; i < numCategoryConstraint+1; i++) {
            Set<POIType> poiTypeSet = new HashSet<>();
            if (i < numCategoryConstraint) {                    // constrainedTypePoi 추가
                CategoryConstraint categoryConstraint = categoryConstraintList.get(i);
                poiTypeSet = categoryConstraint.getPOIType();

                for (POIType argPoiType : poiTypeSet) {                         // constraint 안의 모든 poiType에 대해서
                    List<Integer> addedPoiIndex = new ArrayList<>();
                    for (int j = 0; j < poiList.size(); j++) {                  // 각각의 poiList 내 basicPoi에 대해서
                        if (argPoiType.contain(poiList.get(j).getPoiType())) {  // poiType이 같으면
                            newPoiList.add(poiList.get(j));                     // 새로운 list에 추가하고
                            addedPoiIndex.add(j);                               // poiList상의 인덱스를 기억해 두었다가
                        }
                        if (addedPoiIndex.size() == numConstrainedTypePoi) {    // numConstrainedTypePoi만큼 추가됬으면
                            for (int k = numConstrainedTypePoi-1; k > -1; k--) {
                                poiList.remove(addedPoiIndex.get(k).intValue());           // 기존 poiList에서 제거 (중복 추가 방지)
                            }
                            break;                                              // break
                        }
                    }
                }
            } else {                                    // 일반 poi 추가
                POIType normalPoiType = new POIType("여행");
                POIType accommodationPoiType = new POIType("여행","숙박");
                poiTypeSet.add(normalPoiType);

                for (POIType argPoiType : poiTypeSet) {                         // poiTypeSet 안의 모든 poiType에 대해서
                    List<Integer> addedPoiIndex = new ArrayList<>();
                    for (int j = 0; j < poiList.size(); j++) {
                        if (argPoiType.contain(poiList.get(j).getPoiType()) && !accommodationPoiType.contain(poiList.get(j).getPoiType())) {// && accommodationPoiType.contain(poiList.get(j).getPoiType())
                            newPoiList.add(poiList.get(j));
                            addedPoiIndex.add(j);
                        }
                        if (addedPoiIndex.size() == (numTotalPoi-numConstrainedTypePoi*numCategoryConstraint)) {
                            for (int k = numConstrainedTypePoi-1; k > -1; k--) {
                                poiList.remove(addedPoiIndex.get(k));
                            }
                            break;                                              // break
                        }
                    }
                }
            }
        }

        poiList = newPoiList;
        nameIndexMap = new FastMap<>();
        titleArray = new String[numTotalPoi];
        for (int i = 0; i < numTotalPoi; i++) {
            titleArray[i] = poiList.get(i).getTitle();
            nameIndexMap.put(poiList.get(i).getTitle(), i);
//            System.out.println(poiList.get(i).getTitle()+": "+poiList.get(i).getPoiType());
        }
    }

    public String[] getTitles() {
        return titleArray;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (BasicPOI basicPOI : poiList) {
            stringBuffer.append("###ID=" + i + "\n");
            stringBuffer.append(basicPOI);
            i++;
        }
        return stringBuffer.toString();
    }

    public int size() {
        return poiList.size();
    }

    public BasicPOI getPOI(int i) {
        return poiList.get(i);
    }

    public int getPOIIdx(String poiTitle){
        for (int i=0; i<poiList.size();i++){
            if (poiTitle.equals(poiList.get(i).getTitle())){
                return i;
            }
        }
        throw new RuntimeException(poiTitle + " is not found");
    }

    public static SubsetPOIs dummy() {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByTitle("분당서울대학교병원", "교보문고 광화문점", "뚝섬한강공원", "창동주공4단지아파트",
                "한양대학교 정보통신관");
        return subsetPOIs;
    }

    public static void main(String[] args) {
        SubsetPOIs subsetPOIs = new SubsetPOIs();
        subsetPOIs.makeSubsetPOIsByAreas("서귀포시");
        subsetPOIs.addSubsetPOIsBytitle("메종글래드제주");
    }
}

class CompareBasicPoiByScore implements Comparator<BasicPOI> {
    public int compare(BasicPOI basicPOI1, BasicPOI basicPOI2) {
//        if (basicPOI1.getScore() > basicPOI2.getScore()) {
//            return -1;
//        } else if (basicPOI1.getScore() < basicPOI2.getScore()) {
//            return 1;
//        } else {
//            return 0;
//        }
        return basicPOI1.getScore() > basicPOI2.getScore()? -1 : (basicPOI1.getScore() == basicPOI2.getScore() ? 0 : 1);
    }
}