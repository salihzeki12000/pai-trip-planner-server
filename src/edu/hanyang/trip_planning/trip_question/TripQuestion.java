package edu.hanyang.trip_planning.trip_question;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TripQuestion {
    private PersonalInfo personalInfo;                                      // POI type에 대한 constraints
    private List<DailyTripEntry> dailyTripEntryList = new ArrayList<>();    // 하루하루의 trip plan을 위한 변수들

    public TripQuestion(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public void addDailyTripEntryList(DailyTripEntry dailyTripEntry) {
        dailyTripEntryList.add(dailyTripEntry);
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public List<DailyTripEntry> getDailyTripEntryList() {
        return dailyTripEntryList;
    }

    public static String changeJsonToHttpGet(String str) {
        String newStr = str;
        newStr = newStr.replaceAll("\\{", "<");
        newStr = newStr.replaceAll("}", ">");
        return newStr;
    }

    public static String tripQuestionToHttpGet(TripQuestion tripQuestion) {
        Gson gson = new Gson();
        return "get?type=trip_json_question&body=" + changeJsonToHttpGet(gson.toJson(tripQuestion));
    }

    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("====Trip Question=====\n");
        strbuf.append(personalInfo);
        for (int i = 0; i < dailyTripEntryList.size(); i++) {
            strbuf.append("Itinerary of " + (i + 1) + "th day \n");
            strbuf.append(dailyTripEntryList.get(i));
        }
        return strbuf.toString();
    }
}
