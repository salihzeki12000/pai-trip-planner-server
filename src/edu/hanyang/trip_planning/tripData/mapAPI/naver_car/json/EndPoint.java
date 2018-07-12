
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class EndPoint {

    private String name;
    private Integer x;
    private Integer y;
    private Integer px;
    private Integer py;
    private Integer dist;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     *     The x
     */
    public Integer getX() {
        return x;
    }

    /**
     * 
     * @param x
     *     The x
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * 
     * @return
     *     The y
     */
    public Integer getY() {
        return y;
    }

    /**
     * 
     * @param y
     *     The y
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * 
     * @return
     *     The px
     */
    public Integer getPx() {
        return px;
    }

    /**
     * 
     * @param px
     *     The px
     */
    public void setPx(Integer px) {
        this.px = px;
    }

    /**
     * 
     * @return
     *     The py
     */
    public Integer getPy() {
        return py;
    }

    /**
     * 
     * @param py
     *     The py
     */
    public void setPy(Integer py) {
        this.py = py;
    }

    /**
     * 
     * @return
     *     The dist
     */
    public Integer getDist() {
        return dist;
    }

    /**
     * 
     * @param dist
     *     The dist
     */
    public void setDist(Integer dist) {
        this.dist = dist;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
