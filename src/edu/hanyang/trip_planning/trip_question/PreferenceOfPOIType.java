package edu.hanyang.trip_planning.trip_question;

import com.google.gson.Gson;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import org.apache.log4j.Logger;
import util.Pair;

import java.util.*;

public class PreferenceOfPOIType {
    List<POIType> poiTypeList = new ArrayList<>();
    List<Double> preferenceValueList = new ArrayList<>();
    private static Logger logger = Logger.getLogger(PreferenceOfPOIType.class);
    public void addPreference(POIType poiType, double preference){
        poiTypeList.add(poiType);
        preferenceValueList.add(preference);
    }

    public int size(){
        return poiTypeList.size();
    }
    public Pair<POIType,Double> getPOITypePreference(int idx){
        if (idx>=poiTypeList.size()){
            throw new RuntimeException("No such element with index " + idx);
        }
        POIType poiType = poiTypeList.get(idx);
        Double value = preferenceValueList.get(idx);
        return new Pair<POIType,Double>(poiType,value);
    }

    public String toString(){
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("Preference of POIs\n");
        for (int i=0; i<poiTypeList.size();i++){
            strbuf.append("\t"+poiTypeList.get(i) + "="+preferenceValueList.get(i) + "\n");
        }
        return strbuf.toString();
    }

    public static void test(){
        PreferenceOfPOIType pref = new PreferenceOfPOIType();
        pref.addPreference(new POIType("여행","관광/명소","자연경관"), 0.9);
        pref.addPreference(new POIType("음식점","한식","해물/생선"), 0.9);
        logger.debug(pref);


        Gson gson = new Gson();
        String str = gson.toJson(pref);
        logger.debug(str);

        PreferenceOfPOIType parsedPOIType = gson.fromJson(str,PreferenceOfPOIType.class);
        logger.debug(parsedPOIType);
    }

    public static void main(String[] args) {
        test();


    }
}
