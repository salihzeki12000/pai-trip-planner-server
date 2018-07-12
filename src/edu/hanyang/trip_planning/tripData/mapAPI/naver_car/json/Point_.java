
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class Point_ {

    private String name;
    private String key;
    private Integer x;
    private Integer y;
    private Guide guide;
    private Road road;
    private Panorama panorama;
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
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
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
     *     The guide
     */
    public Guide getGuide() {
        return guide;
    }

    /**
     * 
     * @param guide
     *     The guide
     */
    public void setGuide(Guide guide) {
        this.guide = guide;
    }

    /**
     * 
     * @return
     *     The road
     */
    public Road getRoad() {
        return road;
    }

    /**
     * 
     * @param road
     *     The road
     */
    public void setRoad(Road road) {
        this.road = road;
    }

    /**
     * 
     * @return
     *     The panorama
     */
    public Panorama getPanorama() {
        return panorama;
    }

    /**
     * 
     * @param panorama
     *     The panorama
     */
    public void setPanorama(Panorama panorama) {
        this.panorama = panorama;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
