package edu.hanyang.trip_planning.tripHTBN;


import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.optimize.constraints.ChanceConstraint;
import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.TripACOParameters;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wykwon on 2016-11-11.
 */
public class BatchConstraintViolation {
    private SubsetPOIs subsetPOIs;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    private double returnHour;
    private IncrementalInferenceResults incrementalInferenceResults;
    private boolean bConstraintViolation = false;
    private static Logger logger = Logger.getLogger(BatchConstraintViolation.class);

    public BatchConstraintViolation(SubsetPOIs subsetPOIs, double returnHour, int year, int monthOfYear, int dayOfMonth) {
        this.subsetPOIs = subsetPOIs;
        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.returnHour = returnHour;
    }

    public boolean checkConstraintViolation(DetailItinerary detailItinerary, List<CategoryConstraint> categoryConstraintList) {
        // check full path constraint violations
        if (returnTimePenalty(detailItinerary.getEndTime()) < 0) {
            return true;
        }
        if (costPenalty(detailItinerary.getTotalCost()) < 0) {
            return true;
        }
        if (physicalActivityUpperPenalty(detailItinerary.getTotalPhysicalActivity()) < 0) {
            return true;
        }
        return categoryConstraintsViolation(detailItinerary, categoryConstraintList);  // mgkim: 없어도 될것으로 생각 됨
//        return false;
    }


    private double returnTimePenalty(double expectedReturnTime[]) {
        return ChanceConstraint.inequalityValue(expectedReturnTime, returnHour, ChanceConstraint.LimitType.Lower, TripACOParameters.returnTimeLimitConfidenceLevel);
    }

    private double openTimePenalty(double arrivalTime[], BasicPOI poi) {
        double openHour = poi.getBusinessHour().getOpenHour(this.year, this.monthOfYear, this.dayOfMonth);
//        logger.debug("openTimePenalty 제한=" + arrivalTime[0] + "\t" + openHour);
        return ChanceConstraint.inequalityValue(arrivalTime, openHour, ChanceConstraint.LimitType.Upper, TripACOParameters.openTimeConfidenceInterval);
//        return ChanceConstraint.inequalityValue(tripNetwork.marg_T[path.length - 1], endHour, ChanceConstraint.LimitType.Lower, returnTimeLimitConfidenceLevel);
    }

    private double closeTimePenalty(double departureTime[], BasicPOI poi) {

        double closeHour = poi.getBusinessHour().getCloseHour(this.year, this.monthOfYear, this.dayOfMonth);
//        logger.debug("closeHour 제한=" + departureTime[0] + "\t" + closeHour);
        return ChanceConstraint.inequalityValue(departureTime, closeHour, ChanceConstraint.LimitType.Lower, TripACOParameters.closeTimeConfidenceInterval);
//        return ChanceConstraint.inequalityValue(tripNetwork.marg_T[path.length - 1], endHour, ChanceConstraint.LimitType.Lower, returnTimeLimitConfidenceLevel);
    }

    private double costPenalty(double totalCost[]) {
//        logger.debug("totalCost 제한=" + totalCost[0] + "\t" + TripACOParameters.costLimit);
        return ChanceConstraint.inequalityValue(totalCost, TripACOParameters.costLimit, ChanceConstraint.LimitType.Lower, TripACOParameters.costLimitConfidenceInterval);
    }

    private double physicalActivityUpperPenalty(double totalPA[]) {
//        logger.debug("활동량제한=" + sg);
//        if (bDebug) {
//        logger.debug("활동량 UPPer 제한=" + totalPA[0] + "\t" + TripACOParameters.physicalActivityUpperLimit);
//        }
        return ChanceConstraint.inequalityValue(totalPA, TripACOParameters.physicalActivityUpperLimit, ChanceConstraint.LimitType.Lower, TripACOParameters.physicalActivityLimitConfidenceLevel);
    }

    private boolean categoryConstraintsViolation(DetailItinerary detailItinerary, List<CategoryConstraint> categoryConstraintList) {
        List<double[]> arrivalTimes = detailItinerary.getArrivalTimes();
        List<double[]> departuraTimes = detailItinerary.getDepartureTimes();
        List<BasicPOI> poiList = detailItinerary.getPoiList();

        return false;
//        int restaurantCount=0;
//        for (int i = 0; i < poiList.size(); i++) {
//            if( poiList.get(i).isbRestaurant()){
//                logger.debug(poiList.get(i).getTitle());
//                restaurantCount++;
//            }

//            if (categoryConstraintsViolation(poiList.get(i), arrivalTimes.get(i), departuraTimes.get(i))) {
//                return true;
//            }

//            logger.debug(poiList.get(i).getTitle());
//        }

//        logger.debug("restaurantCount="+restaurantCount);
//        return restaurantCount != 1;
    }
}

