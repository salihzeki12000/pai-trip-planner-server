package edu.hanyang.protocol;

/**
 * 4W중 what에 해당하는 항목
 * 질문 및 답변의 종류가 어떤것인지를 알려줌
 */
public enum ScheduleType {


    //    SingleEvent("단일일정"),
    SingleEventEat("식사"), SingleEventExercise("운동"), SingleEventMeeting("회의"), SingleEventSightSeeing("관광"),
    SingleEventStroll("산책"), SingleEventShow("공연"), SingleEventShopping("쇼핑"), SingleEventWork("일"),
    SingleEventRestOrSleep("휴식/잠"),

    Travel("여행"),

    OnedayTravel("당일여행"), MultidayTravel("여행"),
    SoloTravel("혼자가는여행"), FamilyTravel("가족여행"), ParentTravel("효도관광"), CoupleTravel("커플여행"), GroupTravel("단체여행"),

    OnedaySoloTravel("당일,혼자가는여행"), OnedayFamilyTravel("당일,가족여행"), OnedayarentTravel("당일,효도관광"), OnedayCoupleTravel("당일,커플여행"), OnedayGroupTravel("당일,단체여행"),
    MultidaySoloTravel("여러날,혼자가는여행"), MultidayFamilyTravel("여러날,가족여행"), MultidayParentTravel("여러날,효도관광"), MultidayCoupleTravel("여러날,커플여행"), MultidayGroupTravel("여러날,단체여행"),


    BusinessTrip("출장"),
    OnedayBusinessTrip("당일출장"), MultidayBusinessTrip("여러날출장"),

    CompositeTrip("여러날복합일정");

    // Single Event일 경우에는 Activity Type을 결정해주던가 해야지.
    private String value;


    ScheduleType(final String value) {
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
