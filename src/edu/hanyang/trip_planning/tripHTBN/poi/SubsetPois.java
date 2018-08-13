package edu.hanyang.trip_planning.tripHTBN.poi;

import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.tripData.dataType.PoiType;
import edu.hanyang.trip_planning.tripData.poi.BasicPoi;
import edu.hanyang.trip_planning.tripData.poi.PoiManager;

import javolution.util.FastMap;

import java.util.*;

public class SubsetPois {
    List<BasicPoi> poiList = new ArrayList<>();
    String titleArray[];
    Map<String, Integer> nameIndexMap;

    public SubsetPois(String... areas) {
        // {서귀포시, 부산}->서귀포시+부산 / {성산읍,제주시}->성산읍+제주시 / {성산읍,서귀포시}->서귀포시
        PoiManager poiManager = PoiManager.getInstance();
        poiList.addAll(poiManager.getPoiListByAddresses(areas));
        titleArray = new String[poiList.size()];
        nameIndexMap = new FastMap<>();
        for (int i = 0; i < poiList.size(); i++) {
            titleArray[i] = poiList.get(i).getTitle();
            nameIndexMap.put(poiList.get(i).getTitle(), i);
        }
    }

    public void addSubsetPoisByTitle(String... titles) {
        ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titleArray));
        for (String title : titles) {
            if (!titleList.contains(title)) {
                poiList.add(PoiManager.getInstance().getPoiByTitle(title));
                nameIndexMap.put(poiList.get(poiList.size() - 1).getTitle(), poiList.size() - 1);
                titleList.add(title);
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }

    public void reduceSubsetPoisByTitles(String... titles) {
        ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titleArray));
        for (String title : titles) {
            if (titleList.contains(title)) {
                int poiIdx = getPoiIdx(title);
                poiList.remove(poiIdx);
                nameIndexMap.remove(title);
                titleList.remove(poiIdx);
            }
        }
        titleArray = titleList.toArray(new String[titleList.size()]);
    }

    // mgkim:
    public void reduceSubsetPoisByIdList(Collection<String> idList) {
        ArrayList<String> titleList = new ArrayList<>(Arrays.asList(titleArray));
        for (String id : idList) {
            for (int poiIdx = 0; poiIdx < poiList.size(); poiIdx++) {
                BasicPoi basicPoi = poiList.get(poiIdx);
                if (id.equals(basicPoi.getId())) {
                    String poiTitle = getPoi(poiIdx).getTitle();
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

        List<BasicPoi> newPoiList = new ArrayList<>();
        for (int i = 0; i < numCategoryConstraint + 1; i++) {
            Set<PoiType> poiTypeSet = new HashSet<>();
            if (i < numCategoryConstraint) {                    // constrainedTypePoi 추가
                CategoryConstraint categoryConstraint = categoryConstraintList.get(i);
                poiTypeSet = categoryConstraint.getPoiType();

                for (PoiType argPoiType : poiTypeSet) {                         // constraint 안의 모든 poiType에 대해서
                    List<Integer> addedPoiIndex = new ArrayList<>();
                    for (int j = 0; j < poiList.size(); j++) {                  // 각각의 poiList 내 basicPoi에 대해서
                        if (argPoiType.contain(poiList.get(j).getPoiType())) {  // poiType이 같으면
                            newPoiList.add(poiList.get(j));                     // 새로운 list에 추가하고
                            addedPoiIndex.add(j);                               // poiList상의 인덱스를 기억해 두었다가
                        }
                        if (addedPoiIndex.size() == numConstrainedTypePoi) {    // numConstrainedTypePoi만큼 추가됬으면
                            for (int k = numConstrainedTypePoi - 1; k > -1; k--) {
                                poiList.remove(addedPoiIndex.get(k).intValue());           // 기존 poiList에서 제거 (중복 추가 방지)
                            }
                            break;                                              // break
                        }
                    }
                }
            } else {                                    // 일반 poi 추가
                PoiType normalPoiType = new PoiType("여행");
                PoiType accommodationPoiType = new PoiType("여행", "숙박");
                poiTypeSet.add(normalPoiType);

                for (PoiType argPoiType : poiTypeSet) {                         // poiTypeSet 안의 모든 poiType에 대해서
                    List<Integer> addedPoiIndex = new ArrayList<>();
                    for (int j = 0; j < poiList.size(); j++) {
                        if (argPoiType.contain(poiList.get(j).getPoiType()) && !accommodationPoiType.contain(poiList.get(j).getPoiType())) {// && accommodationPoiType.contain(poiList.get(j).getPoiType())
                            newPoiList.add(poiList.get(j));
                            addedPoiIndex.add(j);
                        }
                        if (addedPoiIndex.size() == (numTotalPoi - numConstrainedTypePoi * numCategoryConstraint)) {
                            for (int k = numConstrainedTypePoi - 1; k > -1; k--) {
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
        }
    }

    public String[] getTitles() {
        return titleArray;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (BasicPoi basicPoi : poiList) {
            stringBuffer.append("###ID=" + i + "\n");
            stringBuffer.append(basicPoi);
            i++;
        }
        return stringBuffer.toString();
    }

    public int size() {
        return poiList.size();
    }

    public BasicPoi getPoi(int i) {
        return poiList.get(i);
    }

    public int getPoiIdx(String poiTitle) {
        for (int i = 0; i < poiList.size(); i++) {
            if (poiTitle.equals(poiList.get(i).getTitle())) {
                return i;
            }
        }
        throw new RuntimeException(poiTitle + " is not found");
    }
}

class CompareBasicPoiByScore implements Comparator<BasicPoi> {
    public int compare(BasicPoi basicPoi1, BasicPoi basicPoi2) {
//        if (basicPoi1.getScore() > basicPoi2.getScore()) {
//            return -1;
//        } else if (basicPoi1.getScore() < basicPoi2.getScore()) {
//            return 1;
//        } else {
//            return 0;
//        }
        return basicPoi1.getScore() > basicPoi2.getScore() ? -1 : (basicPoi1.getScore() == basicPoi2.getScore() ? 0 : 1);
    }
}