package edu.hanyang.trip_planning.trip_question;

public class PersonalInfo {
    public PreferenceOfPoiType preferenceOfPoiType;

    public void setPreferenceOfPoiType(PreferenceOfPoiType preferenceOfPoiType) {
        this.preferenceOfPoiType = preferenceOfPoiType;
    }

    public PreferenceOfPoiType getPreferenceOfPoiType() {
        return preferenceOfPoiType;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("선호도:" + preferenceOfPoiType);
        return strBuf.toString();
    }
}

