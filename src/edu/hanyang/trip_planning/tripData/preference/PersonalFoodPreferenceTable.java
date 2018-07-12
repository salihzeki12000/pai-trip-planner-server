package edu.hanyang.trip_planning.tripData.preference;

import edu.hanyang.trip_planning.utils.StringPairKey;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Time: 오후 5:47
 * To change this template use File | Settings | File Templates.
 */
public class PersonalFoodPreferenceTable {
    private static Logger logger = Logger.getLogger(PersonalFoodPreferenceTable.class);
    String userName;
    Map<FoodPreferenceKey, Double> tableEntry = new HashMap<FoodPreferenceKey, Double>();
    Map<String, Double> preferenceTable = new HashMap<String, Double>();
    Map<StringPairKey, Double> preferenceSubTable = new HashMap<StringPairKey, Double>();

    public PersonalFoodPreferenceTable(String userName) {
        this.userName = userName;
    }


    public Double getPreference(String restaurantType, String restaurantSubType) {
        if (restaurantSubType == null || restaurantSubType.length() == 0) {
            return preferenceTable.get(restaurantType);
        }
        return preferenceSubTable.get(new StringPairKey(restaurantType, restaurantSubType));
    }

    public void addPreference(String category, double value) {
        preferenceTable.put(category, value);
    }

    public void addPreference(String category, String subCategory, double value) {
        if (subCategory == null || subCategory.length() == 0) {
            preferenceTable.put(category, value);
        } else {
            preferenceSubTable.put(new StringPairKey(category, subCategory), value);
        }
    }

    /**
     * ce 반환, null 이 될수도 있음.
     *
     * @param restaurantType
     */
    private Double getPreference(String restaurantType) {
        return preferenceTable.get(restaurantType);
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(preferenceTable + "\n");
        strBuf.append(preferenceSubTable);
        return strBuf.toString();
    }

    public static void test1() {
        Map<FoodPreferenceKey, Double> table = new HashMap<FoodPreferenceKey, Double>();
        table.put(new FoodPreferenceKey("1", "2"), 10.0);
        table.put(new FoodPreferenceKey("1", "2"), 11.0);
        table.put(new FoodPreferenceKey("2", "2"), 12.0);
        System.out.println("table = " + table);
    }

    public static void test2() {
        PersonalFoodPreferenceTable foodPreferenceTable = new PersonalFoodPreferenceTable("권우영");
        foodPreferenceTable.addPreference("중화요리", 1.0);
        foodPreferenceTable.addPreference("중화요리", "탕수육", 1.1);
        logger.debug(foodPreferenceTable.getPreference("중화요리"));
        logger.debug(foodPreferenceTable.getPreference("중화요리", "탕수육"));

        logger.debug(foodPreferenceTable);
    }

    public static void main(String[] args) {
        test2();
    }
}
