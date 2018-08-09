package edu.hanyang.trip_planning.tripData.dataType;

public class POIType {
    public String category;         // 장소 대분류
    public String subCategory;      // 장소 중분류
    public String subSubCategory;   // 장소 소분류

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POIType)) return false;

        POIType poiType = (POIType) o;
        if (!category.equals(poiType.category)) {
            return false;
        }
        if (subCategory == null && poiType.subCategory != null) {
            return false;
        } else if (subCategory != null && poiType.subCategory == null) {
            return false;
        } else if (subCategory != null && poiType.subCategory != null) {
            if (!subCategory.equals(poiType.subCategory)) {
                return false;
            }
        } else {
            return true;
        }
        if (subSubCategory == null && poiType.subSubCategory != null) {
            return false;
        } else if (subSubCategory != null && poiType.subSubCategory == null) {
            return false;
        } else if (subSubCategory != null && poiType.subSubCategory != null) {
            return subSubCategory.equals(poiType.subSubCategory);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        if (subCategory != null) {
            result = 31 * result + subCategory.hashCode();
        } else if (subCategory != null) {
            result = 31 * result + subSubCategory.hashCode();
        }
        return result;
    }

    public POIType(POIType poiType) {
        this.category = poiType.category;
        this.subCategory = poiType.subCategory;
        this.subSubCategory = poiType.subSubCategory;
    }

    public POIType(String category) {
        this.category = category;
        this.subCategory = null;
        this.subSubCategory = null;
    }

    public POIType(String category, String subCategory) {
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = null;
    }

    public POIType(String category, String subCategory, String subSubCategory) {
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
    }

    public boolean contain(POIType type) {
        if (this.category.equals(type.category)) {
            if (this.subCategory == null || this.subCategory.length() == 0) {
                return true;
            } else if (this.subCategory.equals(type.subCategory)) {
                if (this.subSubCategory == null || this.subSubCategory.length() == 0) {
                    return true;
                } else return this.subSubCategory.equals(type.subSubCategory);

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public POIType deepCopy() {
        return new POIType(this.category, this.subCategory, this.subSubCategory);
    }

    @Override
    public String toString() {
        if (subCategory == null) {
            return "POIType:" + category;
        } else if (subSubCategory == null) {
            return "POIType:" + category + "." + subCategory;
        } else {
            return "POIType:" + category + "." + subCategory + "." + subSubCategory;
        }

    }
}
