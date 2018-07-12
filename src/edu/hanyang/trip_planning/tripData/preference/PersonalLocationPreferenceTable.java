package edu.hanyang.trip_planning.tripData.preference;


import edu.hanyang.trip_planning.tripData.dataType.ActivityType;
import edu.hanyang.trip_planning.tripData.dataType.Address;
import edu.hanyang.trip_planning.utils.StringPairKey;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 26
 * Time: 오후 2:38
 * To change this template use File | Settings | File Templates.
 */
public class PersonalLocationPreferenceTable {

    private String userName;
    private static Logger logger = Logger.getLogger(PersonalLocationPreferenceTable.class);

    // 국가단위

    //
    Map<FoodPreferenceKey, Double> tableEntry = new HashMap<FoodPreferenceKey, Double>();
    Map<String, Double> preferenceTable = new HashMap<String, Double>();
    Map<StringPairKey, Double> preferenceSubTable = new HashMap<StringPairKey, Double>();

    public PersonalLocationPreferenceTable(String userName) {
        this.userName = userName;
    }


    public double getPreference(Address address, ActivityType avaliableActivity) {
        return 0.0;
    }
}
