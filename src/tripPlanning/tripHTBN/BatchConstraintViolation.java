package tripPlanning.tripHTBN;


import tripPlanning.optimize.DetailItinerary;
import tripPlanning.optimize.constraints.ChanceConstraint;
import tripPlanning.optimize.constraints.categoryConstraint.CategoryConstraint;
import tripPlanning.tripData.poi.BasicPoi;
import tripPlanning.tripHTBN.poi.SubsetPois;
import tripPlanning.tripHTBN.potential.TripACOParameters;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wykwon on 2016-11-11.
 */
public class BatchConstraintViolation {
    private SubsetPois subsetPois;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;

    private double returnHour;
    private IncrementalInferenceResults incrementalInferenceResults;
    private boolean bConstraintViolation = false;
    private static Logger logger = Logger.getLogger(BatchConstraintViolation.class);

    public BatchConstraintViolation(SubsetPois subsetPois, double returnHour, int year, int monthOfYear, int dayOfMonth) {
        this.subsetPois = subsetPois;
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

    //TODO: businessHours & closedDays Constraint
//    private double openTimePenalty(double arrivalTime[], BasicPoi poi) {
//        double openHour = poi.getBusinessHour().getOpenHour(this.year, this.monthOfYear, this.dayOfMonth);
//        return ChanceConstraint.inequalityValue(arrivalTime, openHour, ChanceConstraint.LimitType.Upper, TripACOParameters.openTimeConfidenceInterval);
//    }
//    private double closeTimePenalty(double departureTime[], BasicPoi poi) {
//        double closeHour = poi.getBusinessHour().getCloseHour(this.year, this.monthOfYear, this.dayOfMonth);
//        return ChanceConstraint.inequalityValue(departureTime, closeHour, ChanceConstraint.LimitType.Lower, TripACOParameters.closeTimeConfidenceInterval);
//    }

    private double costPenalty(double totalCost[]) {
        return ChanceConstraint.inequalityValue(totalCost, TripACOParameters.costLimit, ChanceConstraint.LimitType.Lower, TripACOParameters.costLimitConfidenceInterval);
    }

    private double physicalActivityUpperPenalty(double totalPA[]) {
        return ChanceConstraint.inequalityValue(totalPA, TripACOParameters.physicalActivityUpperLimit, ChanceConstraint.LimitType.Lower, TripACOParameters.physicalActivityLimitConfidenceLevel);
    }

    private boolean categoryConstraintsViolation(DetailItinerary detailItinerary, List<CategoryConstraint> categoryConstraintList) {
        List<double[]> arrivalTimes = detailItinerary.getArrivalTimes();
        List<double[]> departuraTimes = detailItinerary.getDepartureTimes();
        List<BasicPoi> poiList = detailItinerary.getPoiList();

        return false;
    }
}

