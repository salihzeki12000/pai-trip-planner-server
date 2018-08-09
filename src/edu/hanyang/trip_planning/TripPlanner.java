package edu.hanyang.trip_planning;

import edu.hanyang.trip_planning.optimize.DetailItinerary;
import edu.hanyang.trip_planning.optimize.aco.ACOParameters;
import edu.hanyang.trip_planning.optimize.aco.ACOptimizer;
import edu.hanyang.trip_planning.optimize.aco.ScoredPath;
import edu.hanyang.trip_planning.optimize.constraints.categoryConstraint.CategoryConstraint;
import edu.hanyang.trip_planning.optimize.constraints.poiConstraint.PoiConstraint;
import edu.hanyang.trip_planning.tripData.dataType.POIType;
import edu.hanyang.trip_planning.optimize.MultiDayTripAnswer;
import edu.hanyang.trip_planning.trip_question.PersonalInfo;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.hanyang.trip_planning.tripData.poi.VincentyDistanceCalculator;
import edu.hanyang.trip_planning.tripHTBN.GenerateTripCPDs;
import edu.hanyang.trip_planning.tripHTBN.TripACOProblem;
import edu.hanyang.trip_planning.tripHTBN.TripCPDs;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.trip_question.DailyTripEntry;
import edu.hanyang.trip_planning.trip_question.TripQuestion;
import edu.hanyang.trip_planning.trip_question.TripQuestionFactory;
import org.apache.log4j.Logger;

import java.util.*;

public class TripPlanner {
    private static Logger logger = Logger.getLogger(TripPlanner.class);
    private int minuteTime = 30;
    private int numAcoSolution = 1;
    private int numPlan = 1;
    private int numTotalPoi = 50;
    private int numConstrainedTypePoi = 5;
    private ACOParameters acoParameters = new ACOParameters(1.0, 0.5, 0.1, 1000);

    public TripPlanner() {
    }

    public TripPlanner(int minuteTime, int numAcoSolution, int numPlan, int numTotalPoi, int numConstrainedTypePoi) {
        this.minuteTime = minuteTime;
        this.numAcoSolution = numAcoSolution;
        this.numPlan = numPlan;
        this.numTotalPoi = numTotalPoi;
        this.numConstrainedTypePoi = numConstrainedTypePoi;
    }

    public MultiDayTripAnswer tripPlanning(TripQuestion tripQuestion) {
        // mgkim: input arguments assignment
        PersonalInfo personalInfo = tripQuestion.getPersonalInfo();
        List<DailyTripEntry> dailyTripEntryList = tripQuestion.getDailyTripEntryList();
        // mgkim: output argument declaration
        MultiDayTripAnswer multiDayTripAnswer = new MultiDayTripAnswer();

        List<String> visitedPoiIdList = new ArrayList<>();         // mgkim: for visitedPoiId
        for (DailyTripEntry dailyTripEntry : dailyTripEntryList) {
            /* mgkim: planner input arguments*/
            // mgkim: time
            int startTimeArray[] = dailyTripEntry.getStartTime();
            int returnTimeArray[] = dailyTripEntry.getReturnTime();
            // mgkim: constraints
            List<CategoryConstraint> categoryConstraintList = dailyTripEntry.getCategoryConstraintList();
            List<PoiConstraint> poiConstraintList = dailyTripEntry.getPoiConstraintList();
            // mgkim: start & end PoiTitle
            String startPoiTitle = dailyTripEntry.getStartPOITitle();
            String endPoiTitle = dailyTripEntry.getEndPOITitle();

            /* mgkim: planner setup & execution*/
            // mgkim: tripCPDs
            // mgkim: subsetPOIs
            SubsetPOIs subsetPOIs = new SubsetPOIs();
            subsetPOIs.makeSubsetPOIsByAreas(dailyTripEntry.getAreas());                // mgkim: 해당 areas 전체
            subsetPOIs.reduceSubsetPoisByIdList(visitedPoiIdList);                      // mgkim: 들렀던 곳 제외
            subsetPOIs.reduceSubsetPoisByScoreAndConstraint(numTotalPoi, numConstrainedTypePoi, categoryConstraintList); // mgkim: (numTotalPoi-5*numConstraint) 여행typePoi + (5*numConstraint) 각 constraintPoiType 남기고 줄이기
            for (PoiConstraint poiConstraint : poiConstraintList) {                     // mgkim: poiConstraint 처리
                if (poiConstraint.isVisitOrNot()) {
                    subsetPOIs.addSubsetPOIsByTitle(poiConstraint.getPoiTitle());
                } else {
                    subsetPOIs.reduceSubsetPoisByTitles(poiConstraint.getPoiTitle());
                }
            }
            subsetPOIs.addSubsetPOIsByTitle(new String[]{startPoiTitle, endPoiTitle});  // mgkim: 출발, 도착 장소 추가
            GenerateTripCPDs generateTripCPDs = new GenerateTripCPDs(subsetPOIs, minuteTime);
            TripCPDs tripCPDs = generateTripCPDs.generate();
            // mgkim: start & end PoiIdx
            int startNodeIdx = subsetPOIs.getPOIIdx(startPoiTitle);
            int endNodeIdx = subsetPOIs.getPOIIdx(endPoiTitle);
            // Problem 생성
            TripACOProblem tripACOProblem = new TripACOProblem(startTimeArray, returnTimeArray, personalInfo, categoryConstraintList, poiConstraintList, tripCPDs, startNodeIdx, endNodeIdx);
            // Optimizer 생성
            ACOptimizer acOptimizer = new ACOptimizer(tripACOProblem, acoParameters);
            // Solution 생성
            ScoredPath solutions[] = acOptimizer.optimize(numAcoSolution);
            Arrays.sort(solutions);

            /* mgkim: 결과 정리*/
            DetailItinerary detailItinerary = tripACOProblem.result(solutions[0].getPath());
            int return_hour = returnTimeArray[3];
            int return_minute = returnTimeArray[4];
            double returnHour = return_hour + (double) return_minute / 60.0;
            detailItinerary.trimDetailItinerary(returnHour);    // 시간 맞추기
            multiDayTripAnswer.addItinerary(detailItinerary);
            for (BasicPOI visitedPoi : detailItinerary.getPoiList()) {
                visitedPoiIdList.add(visitedPoi.getId());
            }
        }

        multiDayTripAnswer = setNearbyPois(multiDayTripAnswer);
        return multiDayTripAnswer;
    }

    private MultiDayTripAnswer setNearbyPois(MultiDayTripAnswer multiDayTripAnswer) {
        POIManager poiManager = POIManager.getInstance();

        List<List<List<BasicPOI>>> nearbyDiningPoiListListList = new ArrayList<>();         // Day - Pois - NearbyPois
        List<List<List<BasicPOI>>> nearbyShoppingPoiListListList = new ArrayList<>();       // Day - Pois - NearbyPois
        for (int i = 0; i < multiDayTripAnswer.size(); i++) {
            List<List<BasicPOI>> nearbyDiningPoiListList = new ArrayList<>();               // Pois - NearbyPois
            List<List<BasicPOI>> nearbyShoppingPoiListList = new ArrayList<>();             // Pois - NearbyPois
            DetailItinerary detailItinerary = multiDayTripAnswer.getItinerary(i);
            List<BasicPOI> poiList = new ArrayList<>();
            poiList.add(detailItinerary.getStartPOI());
            poiList.addAll(detailItinerary.getPoiList());
            poiList.add(detailItinerary.getEndPOI());
            for (BasicPOI srcPoi : poiList) {
                List<BasicPOI> nearbyDiningPoiList = new ArrayList<>();                     // NearbyPois
                List<BasicPOI> nearbyShoppingPoiList = new ArrayList<>();                   // NearbyPois
                for (BasicPOI destPoi : poiManager.getPoiByType(new POIType("음식점"))) {
                    double distance = VincentyDistanceCalculator.getDistance(srcPoi.getLocation().latitude, srcPoi.getLocation().longitude,
                            destPoi.getLocation().latitude, destPoi.getLocation().longitude);
                    if (distance < 1) {
                        nearbyDiningPoiList.add(destPoi);
                    }

                }
                for (BasicPOI destPoi : poiManager.getPoiByType(new POIType("쇼핑"))) {
                    double distance = VincentyDistanceCalculator.getDistance(srcPoi.getLocation().latitude, srcPoi.getLocation().longitude,
                            destPoi.getLocation().latitude, destPoi.getLocation().longitude);
                    if (distance < 1) {
                        nearbyShoppingPoiList.add(destPoi);
                    }
                }
                nearbyDiningPoiListList.add(nearbyDiningPoiList);
                nearbyShoppingPoiListList.add(nearbyShoppingPoiList);
            }
            nearbyDiningPoiListListList.add(nearbyDiningPoiListList);
            nearbyShoppingPoiListListList.add(nearbyShoppingPoiListList);
        }
        multiDayTripAnswer.setNearbyDiningPoiListListList(nearbyDiningPoiListListList);
        multiDayTripAnswer.setNearbyShoppingPoiListListList(nearbyShoppingPoiListListList);
        return multiDayTripAnswer;
    }

    public static void main(String[] args) {
        TripQuestion tripQuestion = TripQuestionFactory.tripQuestionExample4();
        TripPlanner tripPlanner = new TripPlanner(30, 1, 1, 50, 5);

        int numRun = 1;
        long start = System.currentTimeMillis();
        for (int i = 0; i < numRun; i++) {
            long start1 = System.currentTimeMillis();
            MultiDayTripAnswer multiDayTripAnswer = tripPlanner.tripPlanning(tripQuestion);
            long end1 = System.currentTimeMillis();

            logger.debug(multiDayTripAnswer);
            logger.debug("1회 실행 시간: " + (end1 - start1) / 1000.0);

//            for (int j = 0; j < multiDayTripAnswer.size(); j++) {
//                logger.debug(multiDayTripAnswer.getItinerary(j));
//                System.out.println(multiDayTripAnswer.getItinerary(j).value);
//            }
//            for (int j = 0; j < multiDayTripAnswer.size(); j++) {
//                System.out.println(multiDayTripAnswer.getItinerary(j).getPoiTitles());
//            }
//            System.out.println();
        }
        long end = System.currentTimeMillis();

        logger.debug("평균 실행 시간: " + (end - start) / 1000.0 / numRun);
    }
}
