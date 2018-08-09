package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.tripData.dataType.POIType;
import util.Pair;

import java.util.*;

public class PreferenceOfPOIType {
    List<POIType> poiTypeList = new ArrayList<>();
    List<Double> preferenceValueList = new ArrayList<>();

    public void addPreference(POIType poiType, double preference) {
        poiTypeList.add(poiType);
        preferenceValueList.add(preference);
    }

    public int size() {
        return poiTypeList.size();
    }

    public Pair<POIType, Double> getPOITypePreference(int idx) {
        if (idx >= poiTypeList.size()) {
            throw new RuntimeException("No such element with index " + idx);
        }
        POIType poiType = poiTypeList.get(idx);
        Double value = preferenceValueList.get(idx);
        return new Pair<>(poiType, value);
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("Preference of POIs\n");
        for (int i = 0; i < poiTypeList.size(); i++) {
            strbuf.append("\t" + poiTypeList.get(i) + "=" + preferenceValueList.get(i) + "\n");
        }
        return strbuf.toString();
    }
}
