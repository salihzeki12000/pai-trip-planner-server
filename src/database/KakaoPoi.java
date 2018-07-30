package database;

import java.util.Objects;

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

    private boolean checkAddress(String area) {
        return address_name.contains(area);
    }

    private double getX() {
        return x;
    }

    private double getY() {
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
