package tripPlanning.tripHTBN;

import tripPlanning.tripData.poi.BasicPoi;
import tripPlanning.tripHTBN.dynamicPotential.DiscreteTimeCPD;
import tripPlanning.tripHTBN.poi.SubsetPois;
import tripPlanning.tripHTBN.potential.InterfaceCLGCPD;
import tripPlanning.tripHTBN.potential.InterfaceHybridCPD;

public class TripCPDs {
    public InterfaceHybridCPD movementCPDs;
    public InterfaceHybridCPD costCPDs;
    public InterfaceHybridCPD durationCPDs;
    public InterfaceCLGCPD timeCPDs;
    public DiscreteTimeCPD discreteTimeCPD;

    public InterfaceCLGCPD endTimeCPDs;
    public InterfaceHybridCPD weatherCPDs;

    // 보조변수들
    private SubsetPois subsetPois;
    private TripNodesAndValues tripNodesAndValues;

    public TripCPDs(SubsetPois subsetPois, TripNodesAndValues tripNodesAndValues) {
        this.subsetPois = subsetPois;
        this.tripNodesAndValues = tripNodesAndValues;
    }

    public BasicPoi getPoi(int idx) {
        return subsetPois.getPoi(idx);
    }

    public String[] getTitleOfPois() {
        return subsetPois.getTitles();
    }

    public SubsetPois getSubsetPois() {
        return subsetPois;
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
