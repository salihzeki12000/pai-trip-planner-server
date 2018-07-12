package edu.hanyang.protocol;

/**
 * 활동의 종류
 */
public enum ActivityType {
    Eat("식사"), IndoorExercise("실내운동"), Meeting("회의"), IndoorSightSeeing("실내관광"), Show("공연"), Shopping("쇼핑"), Work("일"), RestOrSleep("휴식/잠"),
    OutdoorSightSeeing("실외관광"), OutdoorExercise("실외운동"), Walk("산책"), Indoor("잘모르는실내활동"), Outdoor("잘모르는실외활동"), Unknown("");

    private String value;
//        식사, 운동, 회의, 관광, 산책, 공연, 쇼핑, 일

    ActivityType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
