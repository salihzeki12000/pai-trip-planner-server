package edu.hanyang.trip_planning.trip_question;

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
        strBuf.append("선호도:" + preferenceOfPOIType);
        return strBuf.toString();
    }

    public static void main(String[] args) {
        System.out.println(PersonalInfoFactory.personalInfoExample1());
    }
}

