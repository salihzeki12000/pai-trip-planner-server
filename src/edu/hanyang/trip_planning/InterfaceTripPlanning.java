package edu.hanyang.trip_planning;

import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.tripData.mapAPI.googleMap.MultiDayTripAnswer;
import edu.hanyang.trip_planning.trip_question.TripQuestion;

/**
 * Created by wykwon on 2016. 12. 8..
 */
public interface InterfaceTripPlanning {

    MultiDayTripAnswer tripPlanning(TripQuestion tripQuestion);
}
