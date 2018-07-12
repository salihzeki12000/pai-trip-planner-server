package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class SubPath {

    @Expose
    private String trafficType;
    @Expose
    private String distance;
    @Expose
    private String sectionTime;
    @Expose
    private String guide;
    @Expose
    private List<Lane> lane = new ArrayList<Lane>();
    @Expose
    private String stationCount;
    @Expose
    private String startX;
    @Expose
    private String startY;
    @Expose
    private String startID;
    @Expose
    private String startName;
    @Expose
    private String endX;
    @Expose
    private String endY;
    @Expose
    private String endID;
    @Expose
    private String endName;
    @Expose
    private String startARSID;
    @Expose
    private String endARSID;
    @Expose
    private String liveUpdate;

    /**
     * @return The trafficType
     */
    public String getTrafficType() {
        return trafficType;
    }

    /**
     * @param trafficType The trafficType
     */
    public void setTrafficType(String trafficType) {
        this.trafficType = trafficType;
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
     * @return The sectionTime
     */
    public String getSectionTime() {
        return sectionTime;
    }

    /**
     * @param sectionTime The sectionTime
     */
    public void setSectionTime(String sectionTime) {
        this.sectionTime = sectionTime;
    }

    /**
     * @return The guide
     */
    public String getGuide() {
        return guide;
    }

    /**
     * @param guide The guide
     */
    public void setGuide(String guide) {
        this.guide = guide;
    }

    /**
     * @return The lane
     */
    public List<Lane> getLane() {
        return lane;
    }

    /**
     * @param lane The lane
     */
    public void setLane(List<Lane> lane) {
        this.lane = lane;
    }

    /**
     * @return The stationCount
     */
    public String getStationCount() {
        return stationCount;
    }

    /**
     * @param stationCount The stationCount
     */
    public void setStationCount(String stationCount) {
        this.stationCount = stationCount;
    }

    /**
     * @return The startX
     */
    public String getStartX() {
        return startX;
    }

    /**
     * @param startX The startX
     */
    public void setStartX(String startX) {
        this.startX = startX;
    }

    /**
     * @return The startY
     */
    public String getStartY() {
        return startY;
    }

    /**
     * @param startY The startY
     */
    public void setStartY(String startY) {
        this.startY = startY;
    }

    /**
     * @return The startID
     */
    public String getStartID() {
        return startID;
    }

    /**
     * @param startID The startID
     */
    public void setStartID(String startID) {
        this.startID = startID;
    }

    /**
     * @return The startName
     */
    public String getStartName() {
        return startName;
    }

    /**
     * @param startName The startName
     */
    public void setStartName(String startName) {
        this.startName = startName;
    }

    /**
     * @return The endX
     */
    public String getEndX() {
        return endX;
    }

    /**
     * @param endX The endX
     */
    public void setEndX(String endX) {
        this.endX = endX;
    }

    /**
     * @return The endY
     */
    public String getEndY() {
        return endY;
    }

    /**
     * @param endY The endY
     */
    public void setEndY(String endY) {
        this.endY = endY;
    }

    /**
     * @return The endID
     */
    public String getEndID() {
        return endID;
    }

    /**
     * @param endID The endID
     */
    public void setEndID(String endID) {
        this.endID = endID;
    }

    /**
     * @return The endName
     */
    public String getEndName() {
        return endName;
    }

    /**
     * @param endName The endName
     */
    public void setEndName(String endName) {
        this.endName = endName;
    }

    /**
     * @return The startARSID
     */
    public String getStartARSID() {
        return startARSID;
    }

    /**
     * @param startARSID The startARSID
     */
    public void setStartARSID(String startARSID) {
        this.startARSID = startARSID;
    }

    /**
     * @return The endARSID
     */
    public String getEndARSID() {
        return endARSID;
    }

    /**
     * @param endARSID The endARSID
     */
    public void setEndARSID(String endARSID) {
        this.endARSID = endARSID;
    }

    /**
     * @return The liveUpdate
     */
    public String getLiveUpdate() {
        return liveUpdate;
    }

    /**
     * @param liveUpdate The liveUpdate
     */
    public void setLiveUpdate(String liveUpdate) {
        this.liveUpdate = liveUpdate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}