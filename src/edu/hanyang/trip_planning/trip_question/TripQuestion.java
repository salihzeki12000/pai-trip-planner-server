package edu.hanyang.trip_planning.trip_question;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Trip Planning에 필요한 질문 객체
 */
public class TripQuestion {
    private PersonalInfo personalInfo;                                      // POI type에 대한 constraints
    private List<DailyTripEntry> dailyTripEntryList = new ArrayList<>();    // 하루하루의 trip plan을 위한 변수들
    private static Logger logger = Logger.getLogger(TripQuestion.class);

    public TripQuestion(PersonalInfo personalInfo){
        this.personalInfo= personalInfo;
    }

    public void addDailyTripEntryList(DailyTripEntry dailyTripEntry){
        dailyTripEntryList.add(dailyTripEntry);
    }

    public PersonalInfo getPersonalInfo(){
        return personalInfo;
    }
    public List<DailyTripEntry> getDailyTripEntryList(){
        return dailyTripEntryList;
    }

    public String toString(){
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("====Trip Question=====\n");
        strbuf.append(personalInfo);
        for (int i=0; i< dailyTripEntryList.size();i++){
            strbuf.append("Itinerary of "+(i+1)+"th day \n");
            strbuf.append(dailyTripEntryList.get(i));
        }
        return strbuf.toString();
    }

    public static String changeJsonToHttpGet(String str){
        String newStr= str;
        newStr = newStr.replaceAll("\\{","<");
        newStr = newStr.replaceAll( "}",">");
        return newStr;
    }

    // mgkim:
    public static String tripQuestion2HttpGet (TripQuestion tripQuestion) {
        Gson gson = new Gson();
        return "get?type=trip_json_question&body="+changeJsonToHttpGet(gson.toJson(tripQuestion));
    }

    public static void test(){
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample1();
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample1());
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample2());
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample3());

//        logger.debug(tripQuestion);
        logger.debug(tripQuestion2HttpGet(tripQuestion));
    }

    public static void main(String[] args) {
//        test_encoding_decoding();
        test();
    }
}
