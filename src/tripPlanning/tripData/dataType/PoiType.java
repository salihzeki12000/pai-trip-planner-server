package tripPlanning.tripData.dataType;

public class PoiType {
    public String category;         // 장소 대분류
    public String subCategory;      // 장소 중분류
    public String subSubCategory;   // 장소 소분류

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PoiType)) return false;

        PoiType poiType = (PoiType) o;
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

    public PoiType(PoiType poiType) {
        this.category = poiType.category;
        this.subCategory = poiType.subCategory;
        this.subSubCategory = poiType.subSubCategory;
    }

    public PoiType(String category) {
        this.category = category;
        this.subCategory = null;
        this.subSubCategory = null;
    }

    public PoiType(String category, String subCategory) {
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = null;
    }

    public PoiType(String category, String subCategory, String subSubCategory) {
        this.category = category;
        this.subCategory = subCategory;
        this.subSubCategory = subSubCategory;
    }

    public boolean contain(PoiType type) {
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

    public PoiType deepCopy() {
        return new PoiType(this.category, this.subCategory, this.subSubCategory);
    }

    @Override
    public String toString() {
        if (subCategory == null) {
            return "PoiType:" + category;
        } else if (subSubCategory == null) {
            return "PoiType:" + category + "." + subCategory;
        } else {
            return "PoiType:" + category + "." + subCategory + "." + subSubCategory;
        }

    }
}
