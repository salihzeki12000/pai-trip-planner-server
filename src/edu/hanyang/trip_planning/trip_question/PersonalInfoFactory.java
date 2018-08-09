package edu.hanyang.trip_planning.trip_question;

import com.google.gson.Gson;
import edu.hanyang.trip_planning.tripData.dataType.PoiType;

// mgkim
public class PersonalInfoFactory {
    public static PersonalInfo personalInfoExample1() {
        PersonalInfo personalInfo = new PersonalInfo();
        PreferenceOfPoiType pref = new PreferenceOfPoiType();
        pref.addPreference(new PoiType("음식점", "한식", "해물/생선"), 0.9);
        pref.addPreference(new PoiType("여행", "관광/명소", "자연경관"), 0.9);
        pref.addPreference(new PoiType("여행", "관광/명소", "해수욕장/해변"), 0.9);
        pref.addPreference(new PoiType("여행", "관광/명소", "폭포/계곡"), 0.9);
        personalInfo.setPreferenceOfPoiType(pref);
        return personalInfo;
    }

    // mgkim
    public static PersonalInfo personalInfoExample2() {
        PersonalInfo personalInfo = new PersonalInfo();
        PreferenceOfPoiType pref = new PreferenceOfPoiType();
        pref.addPreference(new PoiType("음식점", "한식", "육류/고기"), 0.9);
        pref.addPreference(new PoiType("여행", "관광/명소", "미술관/박물관"), 0.9);
        pref.addPreference(new PoiType("여행", "문화시설", "미술관/박물관"), 0.9);
        pref.addPreference(new PoiType("여행", "관광/명소", "테마파크"), 0.9);
        personalInfo.setPreferenceOfPoiType(pref);
        return personalInfo;
    }

    public static PersonalInfo personalInfoExample3() {
        PersonalInfo personalInfo = new PersonalInfo();
        PreferenceOfPoiType pref = new PreferenceOfPoiType();
        pref.addPreference(new PoiType("음식점", "한식", "해물/생선"), 0.9);
        personalInfo.setPreferenceOfPoiType(pref);
        return personalInfo;
    }

    public static PersonalInfo personalInfoExample4() {
        PersonalInfo personalInfo = new PersonalInfo();
        PreferenceOfPoiType pref = new PreferenceOfPoiType();
        personalInfo.setPreferenceOfPoiType(pref);
        return personalInfo;
    }

    public static void main(String[] args) {
        PersonalInfo pInfo = PersonalInfoFactory.personalInfoExample1();
        Gson gson = new Gson();
        String str = gson.toJson(pInfo);
        System.out.println(str);
        PersonalInfo newPInfo = gson.fromJson(str, PersonalInfo.class);
        System.out.println(newPInfo);
    }
}


