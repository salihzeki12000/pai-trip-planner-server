package edu.hanyang.trip_planning.tripData.personalInfo;

import edu.hanyang.trip_planning.trip_question.PreferenceOfPOIType;

public class PersonalInfo {
    public PreferenceOfPOIType preferenceOfPOIType;

    public void setPreferenceOfPOIType(PreferenceOfPOIType preferenceOfPOIType){
        this.preferenceOfPOIType = preferenceOfPOIType;
    }

    public PreferenceOfPOIType getPreferenceOfPOIType() {
        return preferenceOfPOIType;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
//        strBuf.append("나이: " + age + "세\n");
//        strBuf.append("성별: " + gender + "\n");
//        strBuf.append("직업:" + job + "\n");
//        strBuf.append("자녀수:" + numChildren + "\n");
//        strBuf.append("종교:" + religion + "\n");
        strBuf.append("선호도:" + preferenceOfPOIType);
        return strBuf.toString();
    }

    public static void main(String[] args) {
        System.out.println(PersonalInfoFactory.personalInfoExample1());
    }
}

