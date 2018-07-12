package services.qna;

/**
 * 시간적인 constraint
 */
public class TemporalConstraint {
    // 기호로 표현된 시간 {후보 날짜 고려해야 할듯함}
    public String symbolicTime;

    // 시작일
    public String afterTime;
    // 종료일
    public String beforeTime;

    public TemporalConstraint(String symbolicTime) {
        this.symbolicTime = symbolicTime;
    }

    public TemporalConstraint(String afterTime, String beforeTime) {
        this.afterTime = afterTime;
        this.beforeTime = beforeTime;
    }

    public String toString() {
        if (symbolicTime==null){
            return afterTime + "~" + beforeTime;
        }
        else {
            return symbolicTime;
        }
    }
}
