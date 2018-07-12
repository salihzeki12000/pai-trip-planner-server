
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class Summary {

    private Integer returnCode;
    private Integer speed;
    private Integer searchOption;
    private String trafficInfo;
    private Integer totalDistance;
    private Integer totalTime;
    private String mileage;
    private Integer car;
    private Integer taxi;
    private Integer gasPayPerLiter;
    private String tollgate;
    private StartPoint startPoint;
    private EndPoint endPoint;
    private List<RoadSummary> roadSummary = new ArrayList<RoadSummary>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The returnCode
     */
    public Integer getReturnCode() {
        return returnCode;
    }

    /**
     * 
     * @param returnCode
     *     The returnCode
     */
    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * 
     * @return
     *     The speed
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * 
     * @param speed
     *     The speed
     */
    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    /**
     * 
     * @return
     *     The searchOption
     */
    public Integer getSearchOption() {
        return searchOption;
    }

    /**
     * 
     * @param searchOption
     *     The searchOption
     */
    public void setSearchOption(Integer searchOption) {
        this.searchOption = searchOption;
    }

    /**
     * 
     * @return
     *     The trafficInfo
     */
    public String getTrafficInfo() {
        return trafficInfo;
    }

    /**
     * 
     * @param trafficInfo
     *     The trafficInfo
     */
    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    /**
     * 
     * @return
     *     The totalDistance
     */
    public Integer getTotalDistance() {
        return totalDistance;
    }

    /**
     * 
     * @param totalDistance
     *     The totalDistance
     */
    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * 
     * @return
     *     The totalTime
     */
    public Integer getTotalTime() {
        return totalTime;
    }

    /**
     * 
     * @param totalTime
     *     The totalTime
     */
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * 
     * @return
     *     The mileage
     */
    public String getMileage() {
        return mileage;
    }

    /**
     * 
     * @param mileage
     *     The mileage
     */
    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    /**
     * 
     * @return
     *     The car
     */
    public Integer getCar() {
        return car;
    }

    /**
     * 
     * @param car
     *     The car
     */
    public void setCar(Integer car) {
        this.car = car;
    }

    /**
     * 
     * @return
     *     The taxi
     */
    public Integer getTaxi() {
        return taxi;
    }

    /**
     * 
     * @param taxi
     *     The taxi
     */
    public void setTaxi(Integer taxi) {
        this.taxi = taxi;
    }

    /**
     * 
     * @return
     *     The gasPayPerLiter
     */
    public Integer getGasPayPerLiter() {
        return gasPayPerLiter;
    }

    /**
     * 
     * @param gasPayPerLiter
     *     The gasPayPerLiter
     */
    public void setGasPayPerLiter(Integer gasPayPerLiter) {
        this.gasPayPerLiter = gasPayPerLiter;
    }

    /**
     * 
     * @return
     *     The tollgate
     */
    public String getTollgate() {
        return tollgate;
    }

    /**
     * 
     * @param tollgate
     *     The tollgate
     */
    public void setTollgate(String tollgate) {
        this.tollgate = tollgate;
    }

    /**
     * 
     * @return
     *     The startPoint
     */
    public StartPoint getStartPoint() {
        return startPoint;
    }

    /**
     * 
     * @param startPoint
     *     The startPoint
     */
    public void setStartPoint(StartPoint startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * 
     * @return
     *     The endPoint
     */
    public EndPoint getEndPoint() {
        return endPoint;
    }

    /**
     * 
     * @param endPoint
     *     The endPoint
     */
    public void setEndPoint(EndPoint endPoint) {
        this.endPoint = endPoint;
    }

    /**
     * 
     * @return
     *     The roadSummary
     */
    public List<RoadSummary> getRoadSummary() {
        return roadSummary;
    }

    /**
     * 
     * @param roadSummary
     *     The roadSummary
     */
    public void setRoadSummary(List<RoadSummary> roadSummary) {
        this.roadSummary = roadSummary;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "returnCode=" + returnCode +
                ", speed=" + speed +
                ", searchOption=" + searchOption +
                ", trafficInfo='" + trafficInfo + '\'' +
                ", totalDistance=" + totalDistance +
                ", totalTime=" + totalTime +
                ", mileage='" + mileage + '\'' +
                ", car=" + car +
                ", taxi=" + taxi +
                ", gasPayPerLiter=" + gasPayPerLiter +
                ", tollgate='" + tollgate + '\'' +
                ", startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", roadSummary=" + roadSummary +
                ", additionalProperties=" + additionalProperties +
                '}';
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
