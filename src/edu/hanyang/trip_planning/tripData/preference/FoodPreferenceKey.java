package edu.hanyang.trip_planning.tripData.preference;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 25
 * Time: 오후 5:47
 * To change this template use File | Settings | File Templates.
 */
public class FoodPreferenceKey {
    private String majorCategory;
    private String subCategory;

    public FoodPreferenceKey(String firstEntry, String secondEntry) {
        this.majorCategory = firstEntry;
        this.subCategory = secondEntry;
    }

    public String getMajorCategory() {
        return majorCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    @Override

    public String toString() {
        return "FoodPreferenceKey{" +
                "majorCategory='" + majorCategory + '\'' +
                ", subCategory='" + subCategory + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodPreferenceKey)) return false;

        FoodPreferenceKey key = (FoodPreferenceKey) o;

        if (!majorCategory.equals(key.majorCategory)) return false;
        return subCategory.equals(key.subCategory);
    }

    @Override
    public int hashCode() {
        int result = majorCategory.hashCode();
        result = 31 * result + subCategory.hashCode();
        return result;
    }
}
