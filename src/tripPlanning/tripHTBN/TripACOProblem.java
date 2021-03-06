package tripPlanning.tripHTBN;

import tripPlanning.optimize.DetailItinerary;
import tripPlanning.optimize.aco.*;
import tripPlanning.optimize.constraints.ChanceConstraint;
import tripPlanning.optimize.constraints.categoryConstraint.CategoryConstraint;
import tripPlanning.optimize.constraints.categoryConstraint.CategoryConstraintFactory;
import tripPlanning.optimize.constraints.poiConstraint.PoiConstraint;
import tripPlanning.tripData.poi.BasicPoi;
import tripPlanning.tripData.poi.VincentyDistanceCalculator;
import tripPlanning.tripData.dataType.PoiType;
import tripPlanning.tripHTBN.poi.SubsetPois;
import tripPlanning.tripHTBN.potential.TripACOParameters;
import tripPlanning.trip_question.PersonalInfo;
import tripPlanning.trip_question.PersonalInfoFactory;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.math.array.DoubleArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 제주도 Trip Network 기본
 */
public class TripACOProblem extends ItineraryPlanning {
    private static Logger logger = Logger.getLogger(TripACOProblem.class);
    private TripCPDs tripCPDs;
    private PersonalInfo personalInfo;
    private IncrementalInferenceTripNetwork inferenceOnNetwork;
    private double startHour = 9.5;
    private double returnHour = 18.0;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private IncrementalInferenceResults incrementalInferenceResults;
    private List<CategoryConstraint> categoryConstraintList;
    private List<PoiConstraint> poiConstraintList = new ArrayList<>();
    private int categoryConstraintCnt[];
    private boolean poiConstraintCnt[];     // false = not visited yet
    private double distanceRatio = 1;
    private double distanceLimit = 30;

    public TripACOProblem(String dateStr, PersonalInfo personalInfo, List<CategoryConstraint> categoryConstraintList, TripCPDs tripCPDs, int startNodeIdx, int endNodeIdx, double startHour, double returnHour) {
        super(startNodeIdx, endNodeIdx, tripCPDs.getSubsetPois().size());
        this.tripCPDs = tripCPDs;
        this.startHour = startHour;
        this.returnHour = returnHour;
        this.numNodes = tripCPDs.getSubsetPois().size();
        DateTime datetime = (DateTimeFormat.forPattern("yyyy-MM-dd")).parseDateTime(dateStr);
        this.year = datetime.getYear();
        this.monthOfYear = datetime.getMonthOfYear();
        this.dayOfMonth = datetime.getDayOfMonth();
        this.personalInfo = personalInfo;
        this.categoryConstraintList = categoryConstraintList;
        this.categoryConstraintCnt = new int[categoryConstraintList.size()];
        this.inferenceOnNetwork = new IncrementalInferenceTripNetwork(tripCPDs, personalInfo, dateStr, startNodeIdx, endNodeIdx, startHour);
    }

    public TripACOProblem(int[] startTimeArray, int[] returnTimeArray, PersonalInfo personalInfo, List<CategoryConstraint> categoryConstraintList, List<PoiConstraint> poiConstraintList, TripCPDs tripCPDs, int startNodeIdx, int endNodeIdx) {
        super(startNodeIdx, endNodeIdx, tripCPDs.getSubsetPois().size());

        int start_hour = startTimeArray[3];
        int start_minute = startTimeArray[4];
        double startHour = start_hour + (double) start_minute / 60.0;
        int return_hour = returnTimeArray[3];
        int return_minute = returnTimeArray[4];
        double returnHour = return_hour + (double) return_minute / 60.0;

        this.tripCPDs = tripCPDs;
        this.startHour = startHour;
        this.returnHour = returnHour;
        this.numNodes = tripCPDs.getSubsetPois().size();
        this.personalInfo = personalInfo;
        this.year = startTimeArray[0];
        this.monthOfYear = startTimeArray[1];
        this.dayOfMonth = startTimeArray[2];
        this.categoryConstraintList = categoryConstraintList;
        this.categoryConstraintCnt = new int[categoryConstraintList.size()];
        for (PoiConstraint poiConstraint : poiConstraintList) {
            if (poiConstraint.isVisitOrNot()) {
                this.poiConstraintList.add(poiConstraint);
            }
        }
        this.poiConstraintCnt = new boolean[this.poiConstraintList.size()];
        this.inferenceOnNetwork = new IncrementalInferenceTripNetwork(tripCPDs, personalInfo, year, monthOfYear, dayOfMonth, startNodeIdx, endNodeIdx, startHour);
    }

    @Override
    protected void problemDependentInit() {
        if (inferenceOnNetwork != null) {
            inferenceOnNetwork.reset();
        }
        if (categoryConstraintCnt != null) {
            Arrays.fill(categoryConstraintCnt, 0);
        }
        if (poiConstraintCnt != null) {
            Arrays.fill(poiConstraintCnt, false);
        }
    }

    @Override
    public double[] heuristicValue() {
        double sum = 0.0;
        double retValues[] = new double[numNodes];
        for (int destNodeIdx : avaliableNodes) {
            double value = 0.0;
            boolean isDestPoiMustVisited = false;
            boolean isSrcPoiMustVisited = false;
            // get distance
            BasicPoi src = tripCPDs.getPoi(this.curNodeIdx);
            BasicPoi dest = tripCPDs.getPoi(destNodeIdx);
            double distance = VincentyDistanceCalculator.getDistance(src.getLocation().latitude, src.getLocation().longitude,
                    dest.getLocation().latitude, dest.getLocation().longitude);

            // check constraint, get heuristicValue
            for (PoiConstraint poiConstraint : poiConstraintList) {
                if (dest.getId() == poiConstraint.getPoiId()) {
                    isDestPoiMustVisited = true;
                }
                if (src.getId() == poiConstraint.getPoiId()) {
                    isSrcPoiMustVisited = true;
                }
            }
            if (!constraintViolation(destNodeIdx) && (distance < distanceLimit || isDestPoiMustVisited || isSrcPoiMustVisited)) {
                if (isDestPoiMustVisited) {
                    value = 1;
                } else if (distanceRatio == 0) {
                    value = incrementalInferenceResults.getSatisfaction()[0];
                } else if (distanceRatio < 0) {
                    throw new RuntimeException("distanceRatio should be >= 0, distanceRatio: " + distanceRatio);
                } else {
                    value = incrementalInferenceResults.getSatisfaction()[0] / (distance * distance * distanceRatio);
                }
            }
            retValues[destNodeIdx] = value;
            sum += value;
        }
        if (sum < 0.0001) {
            bTerminalCondition = true;
        }
        return retValues;
    }

    @Override
    public void addNodeToTrail(int destNodeIdx) {
        trail.add(destNodeIdx);
        avaliableNodes.remove(destNodeIdx);
        if (avaliableNodes.size() == 0) {
            bTerminalCondition = true;
        }

        IncrementalInferenceResults argResult = inferenceOnNetwork.setNextNodeIdx(destNodeIdx);
        double[] arriveTime = argResult.getArrivalTime();                 // mgkim:
        curNodeIdx = destNodeIdx;
        BasicPoi destPoi = tripCPDs.getPoi(destNodeIdx);

        for (int i = 0; i < categoryConstraintList.size(); i++) {                        // mgkim: 모든 constraint들에 대해서
            CategoryConstraint categoryConstraint = categoryConstraintList.get(i);
            Set<PoiType> poiTypeSet = categoryConstraint.getPoiType();

            // mgkim:
            for (PoiType argPoiType : poiTypeSet) {                 // mgkim: constraint 안의 모든 poiType에 대해서
                if (argPoiType.contain(destPoi.getPoiType())) {     // mgkim: destPoi와 일치하면서
                    if (arriveTime[0] > categoryConstraint.getStartHour() && arriveTime[0] < categoryConstraint.getEndHour()) {
                        categoryConstraintCnt[i]++;
                    }
                }
            }
        }
        for (int i = 0; i < poiConstraintList.size(); i++) {                        // mgkim: 모든 constraint들에 대해서
            PoiConstraint poiConstraint = poiConstraintList.get(i);
            if (poiConstraint.getPoiId() == destPoi.getId()) {     // mgkim: destPoi와 일치하면서
                poiConstraintCnt[i] = true;
            }
        }
    }

    @Override
    public boolean isTerminalCondition() {
        return bTerminalCondition;
    }

    @Override
    public double getTotalValue() {
        for (boolean poiCC : poiConstraintCnt) {
            if (!poiCC) {
                return 0.0;
            }
        }
        int i = 0;
        for (int categoryCC : categoryConstraintCnt) {
            if (categoryCC < categoryConstraintList.get(i).getMinCount() || categoryCC > categoryConstraintList.get(i).getMaxCount()) {
                return 0.0;
            }
        }

        BatchInferenceTripNetwork batchInferenceTripNetwork = new BatchInferenceTripNetwork(tripCPDs, personalInfo, year, monthOfYear, dayOfMonth, startNodeIdx, endNodeIdx, startHour);
        DetailItinerary detailItinerary = batchInferenceTripNetwork.inference(this.trail);
        /* mgkim: 아래 부분은 휴리스틱에서 constraint를 완벽히 적용했다면 필요 없음
        // 다만, return time 등을 완벽히 막기 힘듬 & 아래 주석을 풀면 최소한 return보다 늦지는 않게 할 수 있음
        // 또한, 아래 constraint에서 cost도 체크하고 있음
        BatchConstraintViolation batchConstraintViolation = new BatchConstraintViolation(subsetPois, returnHour, year, monthOfYear, dayOfMonth);
        if (batchConstraintViolation.checkConstraintViolation(detailItinerary, categoryConstraintList)) {
            return 0.0;
        } else {
            return detailItinerary.getValue();
        }*/
        return detailItinerary.getValue();
    }

    @Override
    public DetailItinerary result(int[] path) {
        List<Integer> nodeList = new ArrayList<>();
        for (int n : path) {
            nodeList.add(n);
        }
        BatchInferenceTripNetwork batchInferenceTripNetwork = new BatchInferenceTripNetwork(tripCPDs, personalInfo, year, monthOfYear, dayOfMonth, startNodeIdx, endNodeIdx, startHour);
        DetailItinerary detailItinerary = batchInferenceTripNetwork.inference(nodeList);
        return detailItinerary;
    }

    // constraint가 있으면 true반환
    protected boolean constraintViolation(int destNodeIdx) {
        BasicPoi destPoi = tripCPDs.getPoi(destNodeIdx);
        this.incrementalInferenceResults = inferenceOnNetwork.inferenceNext(destNodeIdx);

        double returnTimeConstraint = returnTimePenalty(incrementalInferenceResults.getExpectedReturnTime());
        if (returnTimeConstraint < 0) {
            return true;
        }
        //TODO: businessHours & closedDays Constraint
//        double openTimeConstraint = openTimePenalty(incrementalInferenceResults.getArrivalTime(), destPoi);
//        if (openTimeConstraint < 0) {
//            return true;
//        }
//        double closeTimeConstraint = closeTimePenalty(incrementalInferenceResults.getDepartureTime(), destPoi);
//        if (closeTimeConstraint < 0) {
//            return true;
//        }
        double costConstraint = costPenalty(incrementalInferenceResults.getTotalCosts());
        if (costConstraint < 0) {
            return true;
        }
        boolean categoryConstraint = categoryConstraintsViolation(destPoi, incrementalInferenceResults.getArrivalTime(), incrementalInferenceResults.getDepartureTime());
        if (categoryConstraint) {
            return true;
        }
        return poiConstraintsViolation(destPoi, incrementalInferenceResults.getArrivalTime(), incrementalInferenceResults.getDepartureTime());
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

    private boolean categoryConstraintsViolation(BasicPoi destPoi, double arrivalTime[], double departureTime[]) {
        boolean isDestPoiConstrainedType = false;
        boolean isTypeAffordableAtThatTime = false;
        List<CategoryConstraint> sameCategoryConstraintList = new ArrayList<>();
        CategoryConstraint affordableCategoryConstraint = new CategoryConstraint();

        // destPoi는 category constrain되어야 하는 type인가?
        for (CategoryConstraint categoryConstraint : categoryConstraintList) {  // mgkim: 모든 categoryConstraint의
            for (PoiType poiType : categoryConstraint.getPoiType()) {           // mgkim: 각각의 poiType에 대해
                if (poiType.contain(destPoi.getPoiType())) {                    // mgkim: destPoi와 비교
                    isDestPoiConstrainedType = true;                            // mgkim: 일치하면 true
                    sameCategoryConstraintList.add(categoryConstraint);

                }
            }
        }

        if (isDestPoiConstrainedType) {     // mgkim: destPoi가 constrain되어야 하는 type인 경우
            for (CategoryConstraint categoryConstraint : sameCategoryConstraintList) {
                // mgkim: destPoi에 도착하는 시간이 해당 타입의 constraint들에 명시된 들를 수 있는 시간대에 속하는가 체크 (or 연산)
                if (arrivalTime[0] > categoryConstraint.getStartHour() && arrivalTime[0] < categoryConstraint.getEndHour()) {
                    // mgkim: canstrant들 중 가능한 것이 있다면 affordableCategoryConstraint에 저장하고 break (있으면 하나뿐일거라는 것)
                    affordableCategoryConstraint = categoryConstraint;
                    isTypeAffordableAtThatTime = true;
                    break;
                }
            }

            if (isTypeAffordableAtThatTime) {    // mgkim: destPoi에 도착하는 시간에 affordable한 constraint가 있으면
                int affordableCategoryConstraintIdx = categoryConstraintList.indexOf(affordableCategoryConstraint);
                // 해당 constraint type에 들를 수 있는 회수가 이미 꽉 찾으면 constraint violation
//                    logger.debug(destPoi.getName()+": "+destPoi.getPoiType()+"-"+arrivalTime[0]);
// 해당 constraint type에 들를 수 있는 회수가 남아 있으면 no violation
                return categoryConstraintCnt[affordableCategoryConstraintIdx] == affordableCategoryConstraint.getMaxCount();
            } else {
                return true;                    // mgkim: destPoi에 도착하는 시간에 affordable한 constraint가 없으면 constraint violation
            }
        } else {                            // mgkim: destPoi가 constrain되어야 하는 type이 아닌 경우
            // mgkim: destPoi에 들름으로써 시간이 흘러 만족시키지 못하게 되는 constraint가 있는가?
            for (int i = 0; i < categoryConstraintList.size(); i++) {
                CategoryConstraint categoryConstraint = categoryConstraintList.get(i);
                if (departureTime[0] > categoryConstraint.getEndHour() && categoryConstraintCnt[i] < categoryConstraint.getMinCount()) {
//                    logger.debug(destPoi.getName()+": "+destPoi.getPoiType()+"-"+arrivalTime[0]);
                    return true;    // mgkim: 있다면 constraint violation
                }
            }
            return false;           // mgkim: 없다면 no violation
        }
    }

    private boolean poiConstraintsViolation(BasicPoi destPoi, double arrivalTime[], double departureTime[]) {
        // destPoi가 poi constrain 되어야 하는 노드인데
        // 아직 안들렀으면서                                                // destPoi 후보에 올랐다는것 자체가 아직 안들른것
        // destPoi에 들름으로서 category constrint를 위반하지 않으면        // 위의 categoryConstraints를 넘어왔으면 위반하지 않는다는것
        // true

        // destPoi가 poi constrain 되어야 하는 노드가 아닌데
        // 아직 안들른 poi constrain 되어야 하는 노드가 있으면 false

        boolean isDestPoiConstrainedType = false;

        // destPoi는 constrain되어야 하는 type인가?
        for (PoiConstraint poiConstraint : poiConstraintList) {         // mgkim: 모든 poiConstraint의
            if (poiConstraint.getPoiId() == destPoi.getId()) {     // mgkim: poiConstraint와 destPoi 비교, 일치하면
                isDestPoiConstrainedType = true;                        // mgkim: true
            }
        }

        if (isDestPoiConstrainedType) {     // mgkim: destPoi가 constrain되어야 하는 type인 경우
            return false;                   // mgkim: no violation
        } else {                            // mgkim: destPoi가 constrain되어야 하는 type이 아닌 경우
            for (boolean poiCC : poiConstraintCnt) {
                if (!poiCC) {
                    return true;            // 아직 충족시키지 못한 poi constrain 되어야 하는 노드가 있으면 violation
                }
            }
            return false;                   // 없으면 no violation
        }
    }

    public static void test() {
        int minuteStep = 30;
        String dateStr = "2016-5-1";
        int startNodeIdx = 0;
        int endNodeIdx = 0;
        double startHour = 9.00;
        double returnHour = 21.00;

        SubsetPois subsetPois = new SubsetPois("제주특별자치도"); // mgkim: 해당 areas 전체
        GenerateTripCPDs generateTripCPDs = new GenerateTripCPDs(subsetPois, minuteStep);
        TripCPDs tripCPDs = generateTripCPDs.generate();
        PersonalInfo personalInfo = PersonalInfoFactory.personalInfoExample1();
        List<CategoryConstraint> categoryConstraintList = new ArrayList<>();
        categoryConstraintList.add(CategoryConstraintFactory.createDinnerConstraint());
        categoryConstraintList.add(CategoryConstraintFactory.createShoppingConstraint());
        TripACOProblem tripACOProblem = new TripACOProblem(dateStr, personalInfo, categoryConstraintList, tripCPDs, startNodeIdx, endNodeIdx, startHour, returnHour);
        double heuristic[] = tripACOProblem.heuristicValue();
        logger.debug(DoubleArray.toString("%2.2f", heuristic));

        ACOParameters acoParameters = new ACOParameters();
        ACOptimizer acOptimizer = new ACOptimizer(tripACOProblem, acoParameters);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            ScoredPath solutions[] = acOptimizer.optimize(10);
            double second = 0.001 * (System.currentTimeMillis() - startTime);
            logger.debug("time=" + second);
            Arrays.sort(solutions);
            for (ScoredPath p : solutions) {
                logger.debug(p);
                logger.debug(tripACOProblem.result(p.getPath()).toDetailString());
            }
        }
    }

    public static void main(String[] args) {
        test();
    }
}
