package edu.hanyang.trip_planning.tripData.dataType;

/**
 * 시간의 확률표현
 */
public class ProbabilisticTime {

    /**
     * 시작시간
     */
    public String time;

    /**
     * 시간의 표준편차
     */
    public int standardDeviation;

    public ProbabilisticTime(String time, int standardDeviation) {

        this.time = time;
        this.standardDeviation = standardDeviation;
    }

    public ProbabilisticTime deepCopy() {
        return new ProbabilisticTime(this.time, this.standardDeviation);
    }

    @Override
    public String toString() {
        return "ProbabilisticTime{" +
                "time='" + time + '\'' +
                ", standardDeviation=" + standardDeviation +
                '}';
    }
}
