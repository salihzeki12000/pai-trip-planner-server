
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class RoadSummary {

    private Point point;
    private String name;
    private Integer length;
    private Integer congestion;
    private Integer speed;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * 
     * @param point
     *     The point
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * 
     * @param length
     *     The length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * 
     * @return
     *     The congestion
     */
    public Integer getCongestion() {
        return congestion;
    }

    /**
     * 
     * @param congestion
     *     The congestion
     */
    public void setCongestion(Integer congestion) {
        this.congestion = congestion;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
