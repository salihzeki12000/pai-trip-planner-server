package database;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class KakaoPoi {
    private int id;
    private String place_name;
    private String category_name;
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String distance;
    private String phone;
    private String place_url;
    private String road_address_name;
    private double x;
    private double y;
    private String fromTitle;

    boolean isAddressValid(String area) {
        return address_name.contains(area);
    }

    boolean isCategoryValid(String[] invalidCategories) {
        for (String invalidCategory : invalidCategories) {
            if (category_name.contains(invalidCategory)) {
                return false;
            }
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return place_name;
    }

    public String getCategoryStr() {
        return category_name;
    }

    public String getAddress() {
        return address_name;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getPlaceUrl() {
        return place_url;
    }

    public String getRoadAddress() {
        return road_address_name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setFromTitle(String fromTitle) {
        this.fromTitle = fromTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoPoi kakaoPoi = (KakaoPoi) o;
        return id == kakaoPoi.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "KakaoPoi{" +
                "id=" + id +
                ", place_name='" + place_name + '\'' +
                ", category_name='" + category_name + '\'' +
                ", address_name='" + address_name + '\'' +
                ", category_group_code='" + category_group_code + '\'' +
                ", category_group_name='" + category_group_name + '\'' +
                ", distance='" + distance + '\'' +
                ", phone='" + phone + '\'' +
                ", place_url='" + place_url + '\'' +
                ", road_address_name='" + road_address_name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", fromTitle='" + fromTitle + '\'' +
                '}';
    }
}

class SearchResponse {
    private KakaoPoi[] documents;
    private Meta meta;

    KakaoPoi[] getKakaoPois() {
        return documents;
    }

    boolean isEnd() {
        return meta.getIsEnd();
    }

    private class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private SameName same_name;

        private boolean getIsEnd() {
            return is_end;
        }

        private class SameName {
            private String[] region;
            private String keyword;
            private String selected_region;
        }
    }
}

class KakaoPoiPlus {
    private int id;
    private String name;
    private String categoryStr;
    private String category;
    private String subCategory;
    private String subSubcategory;
    private String address;
    private String roadAddress;
    private String phoneNumber;
    private String placeUrl;
    private double wgsX;
    private double wgsY;
    private double wcoX;
    private double wcoY;
    private String mobX;
    private String mobY;
    private double score;
    private int numScoredReviews;
    private int numReviews;
    private String businessHours;
    private List<String> facilities;

    KakaoPoiPlus() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategoryStr() {
        return categoryStr;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public String getSubSubcategory() {
        return subSubcategory;
    }

    public String getAddress() {
        return address;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPlaceUrl() {
        return placeUrl;
    }

    public double getWgsX() {
        return wgsX;
    }

    public double getWgsY() {
        return wgsY;
    }

    public double getWcoX() {
        return wcoX;
    }

    public double getWcoY() {
        return wcoY;
    }

    public String getMobX() {
        return mobX;
    }

    public String getMobY() {
        return mobY;
    }

    public double getScore() {
        return score;
    }

    public int getNumScoredReviews() {
        return numScoredReviews;
    }

    public int getNumReviews() {
        return numReviews;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryStr(String categoryStr) {
        this.categoryStr = categoryStr;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setSubSubcategory(String subSubcategory) {
        this.subSubcategory = subSubcategory;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public void setWgsX(double wgsX) {
        this.wgsX = wgsX;
    }

    public void setWgsY(double wgsY) {
        this.wgsY = wgsY;
    }

    public void setWcoX(double wcoX) {
        this.wcoX = wcoX;
    }

    public void setWcoY(double wcoY) {
        this.wcoY = wcoY;
    }

    public void setMobX(String mobX) {
        this.mobX = mobX;
    }

    public void setMobY(String mobY) {
        this.mobY = mobY;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setNumScoredReviews(int numScoredReviews) {
        this.numScoredReviews = numScoredReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KakaoPoiPlus that = (KakaoPoiPlus) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

class Category {
    private String name;
    private Set<Category> subcategories;

    Category(String name) {
        this.name = name;
        this.subcategories = new HashSet<>();
    }

    void addSubCategory(Category subcategory) {
        this.subcategories.add(subcategory);
    }

    String getName() {
        return name;
    }

    Set<Category> getSubcategories() {
        return subcategories;
    }

    Category getSubcategory(String name) {
        for (Category subcategory : subcategories) {
            if (subcategory.getName().equals(name)) {
                return subcategory;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

