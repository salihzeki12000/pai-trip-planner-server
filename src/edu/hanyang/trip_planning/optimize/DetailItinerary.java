package edu.hanyang.trip_planning.optimize;



import edu.hanyang.trip_planning.tripData.poi.BasicPOI;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by wykwon on 2016-02-04.
 */
public class DetailItinerary {
    private BasicPOI startPOI;
    private BasicPOI endPOI;
    private String date;
    private double startTime;
    private double endTime[];

    private List<BasicPOI> poiList = new ArrayList<BasicPOI>();
    private List<double[]> arrivalTimes = new ArrayList<>();
    private List<double[]> durations = new ArrayList<>();
    private List<double[]> departureTimes = new ArrayList<>();
    private List<double[]> costs = new ArrayList<>();
    private List<double[]> PAs = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("00");
    public double value;

    public DetailItinerary(String date, double startTime, BasicPOI startPOI, BasicPOI endPOI) {
        this.date = date;
        this.startTime = startTime;
        this.startPOI = startPOI;
        this.endPOI = endPOI;
    }
    public DetailItinerary(int year, int monthOfYear, int dayOfMonth, double startTime, BasicPOI startPOI, BasicPOI endPOI) {
        this.date = year+"-"+monthOfYear + "-"+dayOfMonth;
        this.startTime = startTime;
        this.startPOI = startPOI;
        this.endPOI = endPOI;
    }

    public void addEntry(BasicPOI poi, double arrivalTime[], double duration[], double departureTime[], double cost[], double pa[]) {
        poiList.add(poi);
        arrivalTimes.add(arrivalTime.clone());
        departureTimes.add(departureTime.clone());
        durations.add(duration.clone());
        costs.add(cost.clone());
        PAs.add(pa.clone());
    }

    /**
     * deterministic version
     *
     * @param poi
     * @param arrivalTime
     * @param duration
     * @param departureTime
     * @param cost
     * @param pa
     */
    public void addEntry(BasicPOI poi, double arrivalTime, double duration, double departureTime, double cost, double pa) {
        poiList.add(poi);
        double tmp[] = new double[2];
        tmp[0] = arrivalTime;
        arrivalTimes.add(tmp.clone());
        tmp[0] = departureTime;
        departureTimes.add(tmp.clone());
        tmp[0] = duration;
        durations.add(tmp.clone());
        tmp[0] = cost;
        costs.add(tmp.clone());
        tmp[0] = pa;
        PAs.add(tmp.clone());
    }

    public List<BasicPOI> getPoiList() {
        return poiList;
    }

    public BasicPOI getStartPOI() {
        return startPOI;
    }

    public BasicPOI getEndPOI() {
        return endPOI;
    }

    public List<double[]> getArrivalTimes() {
        return arrivalTimes;
    }

    public List<double[]> getDepartureTimes() {
        return departureTimes;
    }
    // mgkim:
    public List<double[]> getDurations() {
        return durations;
    }

    public void setEndTime(double endTime[]) {
        this.endTime = endTime;
    }

    public void setEndTime(double endTimeMean, double endTimeVar) {
        this.endTime = new double[2];
        this.endTime[0] = endTimeMean;
        this.endTime[1] = endTimeVar;
    }

    public void setValue(double value) {
        this.value = value;
    }
    // mgkim:
    public void setArrivalTimes(List<double[]> arrivalTimes)  {
        this.arrivalTimes = arrivalTimes;
    }

    public double getValue() {
        return this.value;
    }

    public Set<String> getPOITitleSet() {
        Set<String> retList = new HashSet<>();
        retList.add(startPOI.getTitle());
        for (BasicPOI poi : poiList) {
            retList.add(poi.getTitle());
        }
        retList.add(endPOI.getTitle());
        return retList;
    }

    public double[] getTotalCost() {
        double totalCost[] = new double[2];
        for (double cost[] : costs) {
            totalCost[0] += cost[0];
            totalCost[1] += cost[1];
        }
        return totalCost;
    }

    public double[] getTotalPhysicalActivity() {
        double totalPA[] = new double[2];
        for (double pa[] : PAs) {
            totalPA[0] += pa[0];
            totalPA[1] += pa[1];
        }
        return totalPA;

    }

    public double[] getEndTime() {
        return endTime;
    }

    public List<String> getPoiTitles() {
        List<String> retList = new ArrayList<String>();
        retList.add(startPOI.getTitle());
        for (BasicPOI poi : poiList) {
            retList.add(poi.getTitle());
        }
        retList.add(endPOI.getTitle());
        return retList;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(startPOI.getTitle());
        strbuf.append("->");
        for (BasicPOI poi : poiList) {
            strbuf.append(poi.getTitle());
            strbuf.append("("+poi.getPoiType()+")");
            strbuf.append("->");
        }
        if (endPOI != null) {
            strbuf.append(endPOI.getTitle());
            strbuf.append("("+endPOI.getPoiType()+")");
        } else {
            strbuf.deleteCharAt(strbuf.length() - 1);
            strbuf.deleteCharAt(strbuf.length() - 1);
        }
        return strbuf.toString();
    }

    //    public String toHTMLString() {
//        StringBuffer strbuf = new StringBuffer();
//        strbuf.append("날짜:" + date+ "<br>\n");
//        strbuf.append(startPOITitle + "에서 " + timeStr(startTime) + "에 출발<br>\n");
//        for (int i = 0; i < poiTitles.size()-1; i++) {
//            strbuf.append((i+1) + ": "+poiTitles.get(i) + "에 " +timeStr(arrivalTimes.get(i))+"에 도착, " + durationStr(durations.get(i)) +"동안 방문<br>\n" );
//        }
//
//        strbuf.append((poiTitles.size()) + ": "+poiTitles.get(poiTitles.size()-1) + "에" +durationStr(poiTitles.size()-1)+"에 도착<br>\n ");
//
//
//        return strbuf.toString();
//    }

    public void trimDetailItinerary(double returnHour) {
//        System.out.printf("\t\t%.2f",startTime);
//        System.out.println();
//        for (int i = 0; i < arrivalTimes.size(); i++) {
//            System.out.printf("%.2f\t%.2f",arrivalTimes.get(i)[0],departureTimes.get(i)[0]);
//            System.out.println();
//        }
//        System.out.printf("%.2f\n\n",endTime[0]);

        // remainTime, travelTime, totalTravelTime
        double remainTime = returnHour - endTime[0];
        double[] travelTime = new double[arrivalTimes.size()+1];
        double totalTravelTime = 0;
        travelTime[0] = arrivalTimes.get(0)[0]-startTime;
        totalTravelTime += travelTime[0];
        for (int i = 1; i < travelTime.length-1; i++) {
            travelTime[i] = arrivalTimes.get(i)[0] - departureTimes.get(i-1)[0];
            totalTravelTime += travelTime[i];
        }
        travelTime[travelTime.length-1] = endTime[0] - departureTimes.get(travelTime.length-2)[0];
        totalTravelTime += travelTime[travelTime.length-1];

        // newEndTime, newArrivalTimes, newDepartureTimes
        double[] newEndTime = {returnHour, endTime[1]};
        List<double[]> newArrivalTimes = new ArrayList<>();
        List<double[]> newDepartureTimes = new ArrayList<>();
        double[] temp;
        for (int i = 0; i < arrivalTimes.size(); i++) {
            temp = new double[2];
            if (i == 0) {
                temp[0] = startTime + travelTime[0] + remainTime * travelTime[0] / totalTravelTime;
                temp[1] = arrivalTimes.get(0)[1];
            } else {
                temp[0] = newDepartureTimes.get(i-1)[0] + travelTime[i] + remainTime * travelTime[i] / totalTravelTime;
                temp[1] = arrivalTimes.get(i)[1];
            }
            newArrivalTimes.add(temp);

            temp = new double[2];
            temp[0] = newArrivalTimes.get(i)[0] + durations.get(i)[0];
            temp[1] = newArrivalTimes.get(i)[1];
            newDepartureTimes.add(temp);
        }

        // Set newEndTime, newArrivalTimes, newDepartureTimes
        arrivalTimes = newArrivalTimes;
        departureTimes = newDepartureTimes;
        endTime = newEndTime;

//        System.out.printf("\t\t%.2f",startTime);
//        System.out.println();
//        for (int i = 0; i < arrivalTimes.size(); i++) {
//            System.out.printf("%.2f\t%.2f",arrivalTimes.get(i)[0],departureTimes.get(i)[0]);
//            System.out.println();
//        }
//        System.out.printf("%.2f\n\n",endTime[0]);
    }

    private String timeStr(double timeAsHour) {
        int hour = (int) timeAsHour;
        int minute = (int) ((timeAsHour - hour) * 60);

        return df.format(hour) + ":" + df.format(minute);
    }

    private String durationStr(double durationHour) {
        int durationMin = (int) (durationHour * 60);
        return Integer.toString(durationMin);
    }

    public String toDetailString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(startPOI.getTitle() + "에서 " + timeStr(startTime) + "에 출발\n");

        for (int i = 0; i < poiList.size(); i++) {
            strbuf.append(poiList.get(i).getTitle() + "에서 " + timeStr(arrivalTimes.get(i)[0]) + "에 도착 해서 " + durationStr(durations.get(i)[0]) + "분 동안 머물고 " + timeStr(departureTimes.get(i)[0]) + "에 출발\n");
        }


        strbuf.append(endPOI.getTitle() + "에 " + timeStr(endTime[0]) + "에 도착\n");
        return strbuf.toString();
    }

    public String toDetailHTML() {
        StringBuffer strbuf = new StringBuffer();

        strbuf.append("<p>\n");
        strbuf.append("날짜:" + date + "<br>\n");
        strbuf.append("1. " + startPOI.getTitle() + "에서 " + timeStr(startTime) + "에 출발<br>\n");

        for (int i = 0; i < poiList.size(); i++) {
            strbuf.append((i + 2) + ". " + poiList.get(i).getTitle() + "에서 " + timeStr(arrivalTimes.get(i)[0]) + "에 도착해서 " + durationStr(durations.get(i)[0]) + "분 동안 머물고 " + timeStr(departureTimes.get(i)[0]) + "에 출발<br>\n");
        }

        strbuf.append((poiList.size() + 2) + ". " + endPOI.getTitle() + "에 " + timeStr(endTime[0]) + "에 도착<br>\n");
        strbuf.append("</p>\n");
        return strbuf.toString();
    }


}
