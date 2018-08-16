package tripPlanning.tripHTBN;

import java.text.DecimalFormat;

/**
 * Incremental Inference의 result를 반환함
 */
public class IncrementalInferenceResults {
    private int destNodeIdx;
    private double satisfaction[];
    private double arrivalTime[];
    private double departureTime[];
    private double expectedReturnTime[];
    private double totalCosts[];
    private double totalPA[];
    private double pa[];
    private double cost[];

    public int getDestNodeIdx() {
        return destNodeIdx;
    }

    public void setDestNodeIdx(int destNodeIdx) {
        this.destNodeIdx = destNodeIdx;
    }

    public double[] getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(double[] satisfaction) {
        this.satisfaction = satisfaction.clone();
    }

    public double[] getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(double[] arrivalTime) {
        this.arrivalTime = arrivalTime.clone();
    }

    public double[] getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double[] departureTime) {
        this.departureTime = departureTime.clone();
    }

    public double[] getExpectedReturnTime() {
        return expectedReturnTime;
    }

    public void setExpectedReturnTime(double[] expectedReturnTime) {
        this.expectedReturnTime = expectedReturnTime.clone();
    }


    public double[] getTotalCosts() {
        return totalCosts;
    }

    public void setTotalCosts(double[] totalCosts) {
        this.totalCosts = totalCosts;
    }

    public void addTotalCosts(double[] totalCosts, double[] nextCost) {
        this.totalCosts = totalCosts.clone();
        this.totalCosts[0] += nextCost[0];
        this.totalCosts[1] += nextCost[1];
        this.cost = nextCost.clone();
    }

    public double[] getTotalPA() {
        return totalPA;
    }

    public void setTotalPA(double[] totalPA) {
        this.totalPA = totalPA.clone();
    }

    public void addPA(double[] totalPA, double[] curPA) {
        this.totalPA = totalPA.clone();
        this.totalPA[0] += curPA[0];
        this.totalPA[1] += curPA[1];
        this.pa = curPA.clone();
    }

    public double[] getPa() {
        return pa;
    }

    public double[] getCost() {
        return cost;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("###.###");
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("destNodeIdx=" + destNodeIdx + "\n");
        strbuf.append("arrivalTime=" + df.format(arrivalTime[0]) + " var=" + df.format(arrivalTime[1]) + "\n");
        strbuf.append("departureTime=" + df.format(departureTime[0]) + " var=" + df.format(departureTime[1]) + "\n");
        strbuf.append("expectedReturnTime" + df.format(expectedReturnTime[0]) + " var=" + df.format(expectedReturnTime[1]) + "\n");
        strbuf.append("satisfaction=" + df.format(satisfaction[0]) + " var=" + df.format(satisfaction[1]) + "\n");
        strbuf.append("totalCosts=" + df.format(totalCosts[0]) + " var=" + df.format(totalCosts[1]) + "\n");
        strbuf.append("totalPA=" + df.format(totalPA[0]) + " var=" + df.format(totalPA[1]) + "\n");

        return strbuf.toString();
    }
}
