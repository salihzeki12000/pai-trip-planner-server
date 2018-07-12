package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated("org.jsonschema2pojo")
public class Result {

    @Expose
    private List<Path> path = new ArrayList<Path>();
    @Expose
    private String searchType;
    @Expose
    private String startRadius;
    @Expose
    private String endRadius;
    @Expose
    private String subwayCount;
    @Expose
    private String busCount;
    @Expose
    private String subwayBusCount;
    @Expose
    private String pointDistance;
    @Expose
    private String outTrafficCheck;
    @Expose
    private String apiVersion;
    @Expose
    private String walkEngineState;
    @Expose
    private String start;
    @Expose
    private String end;
    @Expose
    private String walkTime;

    /**
     * @return The path
     */
    public List<Path> getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    public void setPath(List<Path> path) {
        this.path = path;
    }

    /**
     * @return The searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType The searchType
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return The startRadius
     */
    public String getStartRadius() {
        return startRadius;
    }

    /**
     * @param startRadius The startRadius
     */
    public void setStartRadius(String startRadius) {
        this.startRadius = startRadius;
    }

    /**
     * @return The endRadius
     */
    public String getEndRadius() {
        return endRadius;
    }

    /**
     * @param endRadius The endRadius
     */
    public void setEndRadius(String endRadius) {
        this.endRadius = endRadius;
    }

    /**
     * @return The subwayCount
     */
    public String getSubwayCount() {
        return subwayCount;
    }

    /**
     * @param subwayCount The subwayCount
     */
    public void setSubwayCount(String subwayCount) {
        this.subwayCount = subwayCount;
    }

    /**
     * @return The busCount
     */
    public String getBusCount() {
        return busCount;
    }

    /**
     * @param busCount The busCount
     */
    public void setBusCount(String busCount) {
        this.busCount = busCount;
    }

    /**
     * @return The subwayBusCount
     */
    public String getSubwayBusCount() {
        return subwayBusCount;
    }

    /**
     * @param subwayBusCount The subwayBusCount
     */
    public void setSubwayBusCount(String subwayBusCount) {
        this.subwayBusCount = subwayBusCount;
    }

    /**
     * @return The pointDistance
     */
    public String getPointDistance() {
        return pointDistance;
    }

    /**
     * @param pointDistance The pointDistance
     */
    public void setPointDistance(String pointDistance) {
        this.pointDistance = pointDistance;
    }

    /**
     * @return The outTrafficCheck
     */
    public String getOutTrafficCheck() {
        return outTrafficCheck;
    }

    /**
     * @param outTrafficCheck The outTrafficCheck
     */
    public void setOutTrafficCheck(String outTrafficCheck) {
        this.outTrafficCheck = outTrafficCheck;
    }

    /**
     * @return The apiVersion
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * @param apiVersion The apiVersion
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    /**
     * @return The walkEngineState
     */
    public String getWalkEngineState() {
        return walkEngineState;
    }

    /**
     * @param walkEngineState The walkEngineState
     */
    public void setWalkEngineState(String walkEngineState) {
        this.walkEngineState = walkEngineState;
    }

    /**
     * @return The start
     */
    public String getStart() {
        return start;
    }

    /**
     * @param start The start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * @return The end
     */
    public String getEnd() {
        return end;
    }

    /**
     * @param end The end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * @return The walkTime
     */
    public String getWalkTime() {
        return walkTime;
    }

    /**
     * @param walkTime The walkTime
     */
    public void setWalkTime(String walkTime) {
        this.walkTime = walkTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}