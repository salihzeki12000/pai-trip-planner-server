package edu.hanyang.trip_planning.trip_question;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

// mgkim:
public class TripQuestionFactory {
    private static Logger logger = Logger.getLogger(TripQuestionFactory.class);

    public static TripQuestion tripQuestionExample1() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample1();             // 해물/생선, 자연경관, 해수욕장/해변, 폭포/계곡
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample1()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, DS
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample2()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample3()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시, LS
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample2() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample2();             // 육류/고기, 미술관/박물관, 테마파크
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample1()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, DS
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample2()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample3()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시, LS
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample3() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample1();             // 해물/생선, 자연경관, 해수욕장/해변, 폭포/계곡
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample4()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, DS, 정방폭포, X제주미니랜드
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample5()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD, 한림공원
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample6()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시, LS, 테디베어뮤지엄 제주점
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample4() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample2();             // 육류/고기, 미술관/박물관, 테마파크
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample4()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, DS, 정방폭포, X제주미니랜드
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample5()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD, 한림공원
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample6()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시, LS, 테디베어뮤지엄 제주점
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample5() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample3();             // 해물/생선
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample1()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, DS
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample2()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample3()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시, LS
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample6() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample3();             // 해물/생선
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample7()); // 15-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample8()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample9()); // 10-15, 제주퍼시픽호텔, 제주국제공항, 제주시
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample7() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample4();             //
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample10()); // 10-20, 제주국제공항, 제주국제공항, 제주특별자치도
        return tripQuestion;
    }

    public static TripQuestion tripQuestionExample8() {
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample4();             //
        TripQuestion tripQuestion = new TripQuestion(personalInfo);
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample11()); // 10-20, 제주국제공항, 켄싱턴제주호텔, 제주특별자치도, LDS
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample12()); // 10-20, 켄싱턴제주호텔, 제주퍼시픽호텔, 서귀포시, LD
        tripQuestion.addDailyTripEntryList(DailyTripEntryFactory.dailyTripEntryExample13()); // 10-20, 제주퍼시픽호텔, 제주국제공항, 제주시, LDS
        return tripQuestion;
    }

    public static String tripQuestion2HttpGet(TripQuestion tripQuestion) {
        Gson gson = new Gson();

        String str = gson.toJson(tripQuestion);
        str = str.replaceAll("\\{", "<");
        str = str.replaceAll("}", ">");

        return "get?type=trip_json_question&body=" + str;
    }

    public static void main(String[] args) {
        TripQuestion tripQuestion1 = TripQuestionFactory.tripQuestionExample1();
        TripQuestion tripQuestion2 = TripQuestionFactory.tripQuestionExample2();
        TripQuestion tripQuestion3 = TripQuestionFactory.tripQuestionExample3();
        TripQuestion tripQuestion4 = TripQuestionFactory.tripQuestionExample4();
        TripQuestion tripQuestion5 = TripQuestionFactory.tripQuestionExample5();
        TripQuestion tripQuestion6 = TripQuestionFactory.tripQuestionExample6();
        TripQuestion tripQuestion7 = TripQuestionFactory.tripQuestionExample7();
        TripQuestion tripQuestion8 = TripQuestionFactory.tripQuestionExample8();

        logger.debug("tripQuestion1\n" + tripQuestion2HttpGet(tripQuestion1));
        logger.debug("tripQuestion2\n" + tripQuestion2HttpGet(tripQuestion2));
        logger.debug("tripQuestion3\n" + tripQuestion2HttpGet(tripQuestion3));
        logger.debug("tripQuestion4\n" + tripQuestion2HttpGet(tripQuestion4));
        logger.debug("tripQuestion5\n" + tripQuestion2HttpGet(tripQuestion5));
        logger.debug("tripQuestion6\n" + tripQuestion2HttpGet(tripQuestion6));
        logger.debug("tripQuestion7\n" + tripQuestion2HttpGet(tripQuestion7));
        logger.debug("tripQuestion8\n" + tripQuestion2HttpGet(tripQuestion8));
    }
}
