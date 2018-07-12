package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@Generated("org.jsonschema2pojo")
public class Lane {

    @Expose
    private String busNo;
    @Expose
    private String type;
    @Expose
    private String busID;

    /**
     * @return The busNo
     */
    public String getBusNo() {
        return busNo;
    }

    /**
     * @param busNo The busNo
     */
    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The busID
     */
    public String getBusID() {
        return busID;
    }

    /**
     * @param busID The busID
     */
    public void setBusID(String busID) {
        this.busID = busID;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}