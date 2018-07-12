package edu.hanyang.protocol;

/**
 * 시간적인 constraint
 */
public class TemporalConstraint {
    // 시작일
    public String afterTime;
    // 종료일
    public String beforeTime;

    public TemporalConstraint(String afterTime, String beforeTime) {
        this.afterTime = afterTime;
        this.beforeTime = beforeTime;
    }

    public String toString() {
        return afterTime + "~" + beforeTime;
    }
}
