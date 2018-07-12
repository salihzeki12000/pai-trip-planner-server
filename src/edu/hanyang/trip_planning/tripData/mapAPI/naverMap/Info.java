
package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@Generated("org.jsonschema2pojo")
public class Info {

    @Expose
    private String mapObj;
    @Expose
    private Integer payment;
    @Expose
    private Integer busTransitCount;
    @Expose
    private Integer subwayTransitCount;
    @Expose
    private Integer busStationCount;
    @Expose
    private Integer subwayStationCount;
    @Expose
    private Integer totalStationCount;
    @Expose
    private Integer totalTime;
    @Expose
    private Integer totalWalk;
    @Expose
    private Integer trafficDistance;
    @Expose
    private Integer totalDistance;
    @Expose
    private String firstStartStation;
    @Expose
    private String lastEndStation;
    @Expose
    private Integer totalWalkTime;

    /**
     * @return The mapObj
     */
    public String getMapObj() {
        return mapObj;
    }

    /**
     * @param mapObj The mapObj
     */
    public void setMapObj(String mapObj) {
        this.mapObj = mapObj;
    }

    /**
     * @return The payment
     */
    public Integer getPayment() {
        return payment;
    }

    /**
     * @param payment The payment
     */
    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    /**
     * @return The busTransitCount
     */
    public Integer getBusTransitCount() {
        return busTransitCount;
    }

    /**
     * @param busTransitCount The busTransitCount
     */
    public void setBusTransitCount(Integer busTransitCount) {
        this.busTransitCount = busTransitCount;
    }

    /**
     * @return The subwayTransitCount
     */
    public Integer getSubwayTransitCount() {
        return subwayTransitCount;
    }

    /**
     * @param subwayTransitCount The subwayTransitCount
     */
    public void setSubwayTransitCount(Integer subwayTransitCount) {
        this.subwayTransitCount = subwayTransitCount;
    }

    /**
     * @return The busStationCount
     */
    public Integer getBusStationCount() {
        return busStationCount;
    }

    /**
     * @param busStationCount The busStationCount
     */
    public void setBusStationCount(Integer busStationCount) {
        this.busStationCount = busStationCount;
    }

    /**
     * @return The subwayStationCount
     */
    public Integer getSubwayStationCount() {
        return subwayStationCount;
    }

    /**
     * @param subwayStationCount The subwayStationCount
     */
    public void setSubwayStationCount(Integer subwayStationCount) {
        this.subwayStationCount = subwayStationCount;
    }

    /**
     * @return The totalStationCount
     */
    public Integer getTotalStationCount() {
        return totalStationCount;
    }

    /**
     * @param totalStationCount The totalStationCount
     */
    public void setTotalStationCount(Integer totalStationCount) {
        this.totalStationCount = totalStationCount;
    }

    /**
     * @return The totalTime
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * @param totalTime The totalTime
     */
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * @return The totalWalk
     */
    public Integer getTotalWalk() {
        return totalWalk;
    }

    /**
     * @param totalWalk The totalWalk
     */
    public void setTotalWalk(Integer totalWalk) {
        this.totalWalk = totalWalk;
    }

    /**
     * @return The trafficDistance
     */
    public Integer getTrafficDistance() {
        return trafficDistance;
    }

    /**
     * @param trafficDistance The trafficDistance
     */
    public void setTrafficDistance(Integer trafficDistance) {
        this.trafficDistance = trafficDistance;
    }

    /**
     * @return The totalDistance
     */
    public Integer getTotalDistance() {
        return totalDistance;
    }

    /**
     * @param totalDistance The totalDistance
     */
    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * @return The firstStartStation
     */
    public String getFirstStartStation() {
        return firstStartStation;
    }

    /**
     * @param firstStartStation The firstStartStation
     */
    public void setFirstStartStation(String firstStartStation) {
        this.firstStartStation = firstStartStation;
    }

    /**
     * @return The lastEndStation
     */
    public String getLastEndStation() {
        return lastEndStation;
    }

    /**
     * @param lastEndStation The lastEndStation
     */
    public void setLastEndStation(String lastEndStation) {
        this.lastEndStation = lastEndStation;
    }

    /**
     * @return The totalWalkTime
     */
    public Integer getTotalWalkTime() {
        return totalWalkTime;
    }

    /**
     * @param totalWalkTime The totalWalkTime
     */
    public void setTotalWalkTime(Integer totalWalkTime) {
        this.totalWalkTime = totalWalkTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}