package edu.hanyang.trip_planning.tripData.daumLocalAPI;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;


@Generated("org.jsonschema2pojo")
public class Item {

    @Expose
    private String phone;
    @Expose
    private String newAddress;
    @Expose
    private String imageUrl;
    @Expose
    private String direction;
    @Expose
    private String zipcode;
    @Expose
    private String placeUrl;
    @Expose
    private String id;
    @Expose
    private String title;
    @Expose
    private String category;
    @Expose
    private String distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Expose
    private String address;
    @Expose
    private String longitude;
    @Expose
    private String latitude;
    @Expose
    private String addressBCode;

    /**
     * @return The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return The newAddress
     */
    public String getNewAddress() {
        return newAddress;
    }

    /**
     * @param newAddress The newAddress
     */
    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    /**
     * @return The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * @param imageUrl The imageUrl
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * @return The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return The zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode The zipcode
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return The placeUrl
     */
    public String getPlaceUrl() {
        return placeUrl;
    }

    /**
     * @param placeUrl The placeUrl
     */
    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The addressBCode
     */
    public String getAddressBCode() {
        return addressBCode;
    }

    /**
     * @param addressBCode The addressBCode
     */
    public void setAddressBCode(String addressBCode) {
        this.addressBCode = addressBCode;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }


}
