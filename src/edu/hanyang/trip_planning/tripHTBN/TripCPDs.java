package edu.hanyang.trip_planning.tripHTBN;

import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripHTBN.dynamicPotential.DiscreteTimeCPD;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceCLGCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceHybridCPD;

public class TripCPDs {
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
        this.tripNodesAndValues = tripNodesAndValues;
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

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("movement CPDs\n" + movementCPDs);
        strbuf.append("duration CPDs\n" + durationCPDs);
        strbuf.append("time CPDs\n" + timeCPDs);
        strbuf.append("end time CPDs\n" + endTimeCPDs);
        strbuf.append("discrete time CPDs\n" + discreteTimeCPD);


        return strbuf.toString();
    }
}
