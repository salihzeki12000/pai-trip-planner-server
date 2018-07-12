package edu.hanyang.trip_planning.tripData.mapAPI.naverMap;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 1. 2
 * Time: 오후 3:43
 * To change this template use File | Settings | File Templates.
 */
@Generated("org.jsonschema2pojo")

public class BusObject {
    @Expose
    private String startSTN;
    @Expose
    private String startID;
    @Expose
    private String SX;
    @Expose
    private String SY;
    @Expose
    private String endSTN;
    @Expose
    private String endID;
    @Expose
    private String EX;
    @Expose
    private String EY;
    @Expose
    private String payment;
    @Expose
    private String trainType;
    @Expose
    private String trainCode;
    @Expose
    private String paymentUrl;
    @Expose
    private String time;
    @Expose
    private String startCutSTN;
    @Expose
    private String endCutSTN;
    @Expose
    private String guide;

    /**
     * @return The startSTN
     */
    public String getStartSTN() {
        return startSTN;
    }

    /**
     * @param startSTN The startSTN
     */
    public void setStartSTN(String startSTN) {
        this.startSTN = startSTN;
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
     * @return The SX
     */
    public String getSX() {
        return SX;
    }

    /**
     * @param SX The SX
     */
    public void setSX(String SX) {
        this.SX = SX;
    }

    /**
     * @return The SY
     */
    public String getSY() {
        return SY;
    }

    /**
     * @param SY The SY
     */
    public void setSY(String SY) {
        this.SY = SY;
    }

    /**
     * @return The endSTN
     */
    public String getEndSTN() {
        return endSTN;
    }

    /**
     * @param endSTN The endSTN
     */
    public void setEndSTN(String endSTN) {
        this.endSTN = endSTN;
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
     * @return The EX
     */
    public String getEX() {
        return EX;
    }

    /**
     * @param EX The EX
     */
    public void setEX(String EX) {
        this.EX = EX;
    }

    /**
     * @return The EY
     */
    public String getEY() {
        return EY;
    }

    /**
     * @param EY The EY
     */
    public void setEY(String EY) {
        this.EY = EY;
    }

    /**
     * @return The payment
     */
    public String getPayment() {
        return payment;
    }

    /**
     * @param payment The payment
     */
    public void setPayment(String payment) {
        this.payment = payment;
    }

    /**
     * @return The trainType
     */
    public String getTrainType() {
        return trainType;
    }

    /**
     * @param trainType The trainType
     */
    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    /**
     * @return The trainCode
     */
    public String getTrainCode() {
        return trainCode;
    }

    /**
     * @param trainCode The trainCode
     */
    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    /**
     * @return The paymentUrl
     */
    public String getPaymentUrl() {
        return paymentUrl;
    }

    /**
     * @param paymentUrl The paymentUrl
     */
    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    /**
     * @return The time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return The startCutSTN
     */
    public String getStartCutSTN() {
        return startCutSTN;
    }

    /**
     * @param startCutSTN The startCutSTN
     */
    public void setStartCutSTN(String startCutSTN) {
        this.startCutSTN = startCutSTN;
    }

    /**
     * @return The endCutSTN
     */
    public String getEndCutSTN() {
        return endCutSTN;
    }

    /**
     * @param endCutSTN The endCutSTN
     */
    public void setEndCutSTN(String endCutSTN) {
        this.endCutSTN = endCutSTN;
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

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
