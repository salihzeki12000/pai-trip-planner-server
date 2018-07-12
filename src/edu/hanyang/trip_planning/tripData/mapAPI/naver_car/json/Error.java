
package edu.hanyang.trip_planning.tripData.mapAPI.naver_car.json;

import java.util.HashMap;
import java.util.Map;

public class Error {

    private Integer routeOption;
    private Start start;
    private End end;
    private String errorMessage;
    private Integer errorCode;
    private String coordType;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The routeOption
     */
    public Integer getRouteOption() {
        return routeOption;
    }

    /**
     *
     * @param routeOption
     * The route_option
     */
    public void setRouteOption(Integer routeOption) {
        this.routeOption = routeOption;
    }

    /**
     *
     * @return
     * The start
     */
    public Start getStart() {
        return start;
    }

    /**
     *
     * @param start
     * The start
     */
    public void setStart(Start start) {
        this.start = start;
    }

    /**
     *
     * @return
     * The end
     */
    public End getEnd() {
        return end;
    }

    /**
     *
     * @param end
     * The end
     */
    public void setEnd(End end) {
        this.end = end;
    }

    /**
     *
     * @return
     * The errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     *
     * @param errorMessage
     * The error_message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     *
     * @return
     * The errorCode
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     *
     * @param errorCode
     * The error_code
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     *
     * @return
     * The coordType
     */
    public String getCoordType() {
        return coordType;
    }

    /**
     *
     * @param coordType
     * The coord_type
     */
    public void setCoordType(String coordType) {
        this.coordType = coordType;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}