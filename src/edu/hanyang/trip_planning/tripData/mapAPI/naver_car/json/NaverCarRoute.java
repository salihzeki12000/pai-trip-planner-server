
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import javax.annotation.processing.Generated;
import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Generated("org.jsonschema2pojo")
public class NaverCarRoute {

    private Version version;
    private Summary summary;
    private java.lang.Error error;
    private List<Route> route = new ArrayList<Route>();
    private String path;
    private String bound;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * @return The summary
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * @param summary The summary
     */
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    /**
     * @return The route
     */
    public List<Route> getRoute() {
        return route;
    }

    /**
     * @param route The route
     */
    public void setRoute(List<Route> route) {
        this.route = route;
    }

    /**
     * @return The path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path The path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public java.lang.Error getError() {return this.error;}
    /**
     *
     * @return The bound
     */
    public String getBound() {
        return bound;
    }

    /**
     * @param bound The bound
     */
    public void setBound(String bound) {
        this.bound = bound;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
