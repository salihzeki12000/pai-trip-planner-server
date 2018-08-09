package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.tripData.dataType.PoiType;
import util.Pair;

import java.util.*;

public class PreferenceOfPoiType {
    List<PoiType> poiTypeList = new ArrayList<>();
    List<Double> preferenceValueList = new ArrayList<>();

    public void addPreference(PoiType poiType, double preference) {
        poiTypeList.add(poiType);
        preferenceValueList.add(preference);
    }

    public int size() {
        return poiTypeList.size();
    }

    public Pair<PoiType, Double> getPoiTypePreference(int idx) {
        if (idx >= poiTypeList.size()) {
            throw new RuntimeException("No such element with index " + idx);
        }
        PoiType poiType = poiTypeList.get(idx);
        Double value = preferenceValueList.get(idx);
        return new Pair<>(poiType, value);
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("Preference of Pois\n");
        for (int i = 0; i < poiTypeList.size(); i++) {
            strbuf.append("\t" + poiTypeList.get(i) + "=" + preferenceValueList.get(i) + "\n");
        }
        return strbuf.toString();
    }
}
