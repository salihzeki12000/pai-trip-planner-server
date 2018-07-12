package edu.hanyang.trip_planning.trip_question;

import edu.hanyang.trip_planning.trip_question.TripQuestion;

/**
 * Created by wykwon on 2016. 12. 10..
 */
public class AnalysisTripQuestion {
    TripQuestion tripQuestion;
    public AnalysisTripQuestion(TripQuestion tripQuestion){
        this.tripQuestion = tripQuestion;
    }
    /**
     * return hour를 해석함
     * 임시로 18.00 으로 선정
     *
     * TODO multiday issue처리할것
     * @param question
     * @return
     */
    public double analysisReturnHour(){
        return 18.0;
    }
}
