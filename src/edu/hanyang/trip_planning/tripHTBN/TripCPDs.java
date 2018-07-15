package edu.hanyang.trip_planning.tripHTBN;


import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripHTBN.dynamicPotential.DiscreteTimeCPD;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceCLGCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceHybridCPD;
import wykwon.common.DateTimeFormatStr;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import wykwon.common.MyCollections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2015-10-08.
 */
public class TripCPDs {
    private static Logger logger = Logger.getLogger(TripCPDs.class);
    public InterfaceHybridCPD movementCPDs;
    public InterfaceHybridCPD costCPDs;
    public InterfaceHybridCPD durationCPDs;
    public InterfaceCLGCPD timeCPDs;
    public DiscreteTimeCPD discreteTimeCPD;


    public InterfaceCLGCPD endTimeCPDs;
    public InterfaceHybridCPD weatherCPDs;
    public InterfaceHybridCPD physicalActivity;

    // 보조변수들
    private SubsetPOIs subsetPOIs;
    private TripNodesAndValues tripNodesAndValues;

    public TripCPDs(SubsetPOIs subsetPOIs, TripNodesAndValues tripNodesAndValues) {
        this.subsetPOIs = subsetPOIs;
        this.tripNodesAndValues= tripNodesAndValues;
    }

    public TripCPDs(String titleOfPOIs[], TripNodesAndValues tripNodesAndValues) {
        this.subsetPOIs = new SubsetPOIs(titleOfPOIs);
        this.tripNodesAndValues= tripNodesAndValues;
    }

    public BasicPOI getPOI(int idx) {
        return subsetPOIs.getPOI(idx);
    }

    public String[] getTitleOfPOIs() {
        return subsetPOIs.getTitles();
    }

    public SubsetPOIs getSubsetPOIs() {
        return subsetPOIs;
    }

    public InterfaceHybridCPD getMovementCPDs() {
        return movementCPDs;
    }

    public void setMovementCPDs(InterfaceHybridCPD movementCPDs) {
        this.movementCPDs = movementCPDs;
    }

    public InterfaceHybridCPD getCostCPDs() {
        return costCPDs;
    }

    public void setCostCPDs(InterfaceHybridCPD costCPDs) {
        this.costCPDs = costCPDs;
    }


    public DiscreteTimeCPD getDiscreteTimeCPD() {
        return discreteTimeCPD;
    }

    public void setDiscreteTimeCPD(DiscreteTimeCPD discreteTimeCPD) {
        this.discreteTimeCPD = discreteTimeCPD;
    }

    public void setEndTimeCPDs(InterfaceCLGCPD endTimeCPDs) {
        this.endTimeCPDs = endTimeCPDs;
    }

    public InterfaceCLGCPD getEndTimeCPDs() {
        return endTimeCPDs;
    }

    public InterfaceHybridCPD getDurationCPDs() {
        return durationCPDs;
    }

    public void setDurationCPDs(InterfaceHybridCPD durationCPDs) {
        this.durationCPDs = durationCPDs;
    }

    public InterfaceCLGCPD getTimeCPDs() {
        return timeCPDs;
    }

    public void setTimeCPDs(InterfaceCLGCPD timeCPDs) {
        this.timeCPDs = timeCPDs;
    }

    public InterfaceHybridCPD getWeatherCPDs() {
        return weatherCPDs;
    }

    public void setWeatherCPDs(InterfaceHybridCPD weatherCPDs) {
        this.weatherCPDs = weatherCPDs;
    }

    public InterfaceHybridCPD getPhysicalActivity() {
        return physicalActivity;
    }

    public void setPhysicalActivity(InterfaceHybridCPD physicalActivity) {
        this.physicalActivity = physicalActivity;
    }

    public TripNodesAndValues getTripNodesAndValues() {
        return tripNodesAndValues;
    }

    /**
     * 분 간격으로 시간 Sequence를 만들어냄
     *
     * @param dMin
     * @return
     */
    private String[] generateDiscreteTime(int dMin) {
        DateTime curDateTime = DateTimeFormatStr.parseFullDateTime("2000-01-01 00:00");
        int startDay = curDateTime.getDayOfYear();
        List<String> timeStrList = new ArrayList<>();
        while (true) {
            if (curDateTime.getDayOfYear() != startDay) {
                break;
            }
            timeStrList.add(DateTimeFormatStr.printTime(curDateTime));
            logger.debug(DateTimeFormatStr.printTime(curDateTime));
            curDateTime = curDateTime.plusMinutes(dMin);
        }

        return MyCollections.toArray(timeStrList);
    }

    public String toString(){
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("movement CPDs\n"+movementCPDs);
        strbuf.append("duration CPDs\n"+durationCPDs);
        strbuf.append("time CPDs\n"+timeCPDs);
        strbuf.append("end time CPDs\n"+endTimeCPDs);
        strbuf.append("discrete time CPDs\n"+ discreteTimeCPD);


        return strbuf.toString();
    }
}
