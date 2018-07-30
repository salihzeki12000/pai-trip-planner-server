package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class Coord {
    private double x;
    private double y;

    Coord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }
}

class KakaoPoi {
    private int id;
    private String place_name;
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String phone;
    private String place_url;
    private String road_address_name;
    private double x;
    private double y;

    boolean isAddressValid(String area) {
        return address_name.contains(area);
    }

    List<String> parseCategory() {
        List<String> parsedCategory = new ArrayList<>(Arrays.asList(category_name.split(" > ")));
        if (parsedCategory.size() < 3) {
            System.out.println(place_name);
        }
        List<String> completeParsedCategory = new ArrayList<>(parsedCategory);
        for (int i = 0; i < 4 - parsedCategory.size(); i++) {
            completeParsedCategory.add("없음");
        }
        return completeParsedCategory;
//        return parsedCategory;
    }

    boolean isCategoryValid(String[] invalidCategories) {
//        if (category_name.split(" > ").length < 3) {
//            System.out.println(place_name); //관광/숙박/음식점/
//            return false;
//        }
        for (String invalidCategory : invalidCategories) {
            if (category_name.contains(invalidCategory)) {
                return false;
            }
        }
        return true;
    }

    boolean isCategoryValid(String[] validCategories, String[] invalidCategories) {
//        if (category_name.split(" > ").length < 3) {
//            System.out.println(place_name); //관광/숙박/음식점/ETC  시장은 안됨
//            return false;
//        }
        for (String validCategory : validCategories) {
            if (!category_name.contains(validCategory)) {
                return false;
            }
        }
        for (String invalidCategory : invalidCategories) {
            if (category_name.contains(invalidCategory)) {
                return false;
            }
        }
        return true;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
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
        return "{" +
                "id:" + id +
                ", address_name:" + address_name +
                ", category_group_code:" + category_group_code +
                ", category_group_name:" + category_group_name +
                ", category_name:" + category_name +
                ", distance:" + distance +
                ", phone:" + phone +
                ", place_name:" + place_name +
                ", place_url:" + "'" + place_url + "'" +
                ", road_address_name:" + road_address_name +
                ",x:" + x +
                ",y:" + y +
                "}";
    }
}

class Poi {

}

class TranscoordResponse {
    private Coord[] documents;
    private Meta meta;

    Coord getCoord() {
        return documents[0];
    }

    private class Meta {
        private int total_count;
    }
}

class SearchResponse {
    private KakaoPoi[] documents;
    private Meta meta;

    KakaoPoi[] getPois() {
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
