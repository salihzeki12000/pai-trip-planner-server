package tripPlanning.tripHTBN;

import cntbn.terms_factors.ContinuousFactor;
import tripPlanning.optimize.DetailItinerary;
import tripPlanning.tripData.dataType.ProbabilisticDuration;
import tripPlanning.tripData.poi.BasicPoi;
import tripPlanning.trip_question.PersonalInfo;
import tripPlanning.tripData.preference.TouristAttractionType;
import tripPlanning.tripHTBN.dynamicPotential.PDFtoPMF;
import tripPlanning.tripHTBN.potential.domain_specific.Temperature;
import tripPlanning.tripHTBN.potential.domain_specific.WeatherEntry;
import tripPlanning.tripHTBN.potential.domain_specific.WeatherProbability;
import tripPlanning.tripHTBN.potential.satisfaction.WeatherSuitabilityCPT;
import tripPlanning.tripHTBN.potential.satisfaction.WeatherSuitabilityGen;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import util.Erf;
import util.MyArrays;
import util.Triple;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wykwon on 2016-11-02.
 */
public class BatchInferenceTripNetwork {
    private boolean bDebug = false;
    private static Logger logger = Logger.getLogger(BatchInferenceTripNetwork.class);
    private TripCPDs tripCPDs;
    private PersonalInfo personalInfo;
    private double startHour;
    private String dateStr;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;


    private int startNodeIdx;
    private int endNodeIdx;
    private TripNodesAndValues tripNodesAndValues;
    private WeatherSuitabilityCPT weatherType1_indoor = WeatherSuitabilityGen.type1_indoor();
    private WeatherSuitabilityCPT weatherType2_lessSensitive = WeatherSuitabilityGen.type2_lessSensitive();
    private WeatherSuitabilityCPT weatherType3_moreSensitive = WeatherSuitabilityGen.type3_moreSensitive();
    private WeatherSuitabilityCPT weatherType4_sensitive = WeatherSuitabilityGen.type4_sensitive();
    private double zeroDelta[];
    //  누적되는 marginal inference 결과들
    private double previousDepartureTime[] = new double[2];
    private double totalPA[] = new double[2];
    private double totalCost[] = new double[2];
    private double totalSatisfaction[] = new double[2];


    public BatchInferenceTripNetwork(TripCPDs tripCPDs, PersonalInfo personalInfo, String dateStr, int startNodeIdx, int endNodeIdx, double startHour) {
        this.tripCPDs = tripCPDs;
        this.dateStr = dateStr;
        this.personalInfo = personalInfo;
        DateTime datetime = (DateTimeFormat.forPattern("yyyy-MM-dd")).parseDateTime(dateStr);
        this.year = datetime.getYear();
        this.monthOfYear = datetime.getMonthOfYear();
        this.dayOfMonth = datetime.getDayOfMonth();
        this.startNodeIdx = startNodeIdx;
        this.endNodeIdx = endNodeIdx;
        this.startHour = startHour;
        this.tripNodesAndValues = tripCPDs.getTripNodesAndValues();
        zeroDelta = new double[2];
        Arrays.fill(zeroDelta, 0);
        previousDepartureTime[0] = startHour;
        previousDepartureTime[1] = 0.01;
    }


    public BatchInferenceTripNetwork(TripCPDs tripCPDs, PersonalInfo personalInfo, int year, int monthOfYear, int dayOfMonth, int startNodeIdx, int endNodeIdx, double startHour) {
        this.tripCPDs = tripCPDs;
        this.dateStr = year + "-" + monthOfYear + "-" + dayOfMonth;
        this.personalInfo = personalInfo;

        this.year = year;
        this.monthOfYear = monthOfYear;
        this.dayOfMonth = dayOfMonth;
        this.startNodeIdx = startNodeIdx;
        this.endNodeIdx = endNodeIdx;
        this.startHour = startHour;
        this.tripNodesAndValues = tripCPDs.getTripNodesAndValues();
        zeroDelta = new double[2];
        Arrays.fill(zeroDelta, 0);
        previousDepartureTime[0] = startHour;
        previousDepartureTime[1] = 0.01;
    }

    /**
     * 단계별 추론 시작
     */
    public DetailItinerary inference(List<Integer> trail) {
        DetailItinerary detailItinerary = new DetailItinerary(dateStr, startHour, tripCPDs.getPoi(startNodeIdx), tripCPDs.getPoi(endNodeIdx));
        int srcNodeIdx, destNodeIdx;
        previousDepartureTime = new double[2];
        previousDepartureTime[0] = startHour;
        previousDepartureTime[1] = 0.0;
        srcNodeIdx = startNodeIdx;


        for (int t = 0; t < trail.size(); t++) {
            destNodeIdx = trail.get(t);
            double duration[] = inferenceDuration(destNodeIdx);
            double movement[] = inferenceMovement(srcNodeIdx, destNodeIdx, null);
            double arrivalTime[] = inferenceArrivalTime(previousDepartureTime, movement);
            double departureTime[] = inferenceDepartureTime(arrivalTime, duration);
            Triple<double[], Integer, Integer> discretizedTime = PDFtoPMF.getGaussianPMFDayTime(arrivalTime[0], arrivalTime[1], tripNodesAndValues.getDiscreteTimeWidth());
            double satisfaction[] = inferenceSatisfaction(destNodeIdx, discretizedTime.first());
            double pa[] = inferencePhysicalActivity(destNodeIdx);
            double cost[] = inferenceCost(destNodeIdx);
//        public void addEntry(String poiTitle, double arrivalTime[], double duration[], double departureTime[], double cost[], double pa[])


            detailItinerary.addEntry(tripCPDs.getPoi(destNodeIdx), arrivalTime, duration, departureTime, cost, pa);
            previousDepartureTime = departureTime.clone();

            totalPA[0] += pa[0];
            totalPA[1] += pa[1];
            totalCost[0] += cost[0];
            totalCost[1] += cost[1];
            totalSatisfaction[0] += satisfaction[0];
            totalSatisfaction[1] += satisfaction[1];
            srcNodeIdx = destNodeIdx;
        }


        double returnMovement[] = inferenceMovement(trail.get(trail.size() - 1), endNodeIdx, null);

        detailItinerary.setEndTime(previousDepartureTime[0] + returnMovement[0], previousDepartureTime[1] + returnMovement[1]);
        detailItinerary.setValue(totalSatisfaction[0]);
        return detailItinerary;

    }


    private double[] inferenceArrivalTime(double movement[], double endtime[]) {

        double ret[] = new double[2];
        ret[0] = movement[0] + endtime[0];
        ret[1] = movement[1] + endtime[1];
        return ret;
    }

    private double[] inferenceDepartureTime(double arrivalTime[], double duration[]) {
        double ret[] = new double[2];
        ret[0] = arrivalTime[0] + duration[0];
        ret[1] = arrivalTime[1] + duration[1];
        return ret;
    }

    /**
     * @param destNodeIdx
     * @return 1st element: mean, 2nd element: var
     */
    private double[] inferenceDuration(int destNodeIdx) {
        ProbabilisticDuration pd = tripCPDs.getPoi(destNodeIdx).getSpendingTime();
        double ret[] = new double[2];
        ret[0] = pd.hour;
        ret[1] = pd.standardDeviation * pd.standardDeviation;
        return ret;
    }

    public double[] inferenceMovement(int srcNodeIdx, int destNodeIdx, double discreteTimes[]) {
        int discreteParentIdx[] = new int[3];
        discreteParentIdx[0] = tripCPDs.getTripNodesAndValues().X1;
        discreteParentIdx[1] = tripCPDs.getTripNodesAndValues().X2;
        discreteParentIdx[2] = tripCPDs.getTripNodesAndValues().DE;

        int discreteParentValueIdx[] = new int[3];
        discreteParentValueIdx[0] = srcNodeIdx;
        discreteParentValueIdx[1] = destNodeIdx;
        discreteParentValueIdx[2] = tripNodesAndValues.hourToIdx(startHour);


        ContinuousFactor factor = tripCPDs.getMovementCPDs().getDistribution(discreteParentIdx, discreteParentValueIdx);
        double ret[] = new double[2];
        ret[0] = factor.getMean();
        ret[1] = factor.getVariance();
//        logger.debug("src="+srcNodeIdx+ "\tdest="+destNodeIdx + "\t"+String.format("%2.2f",ret[0])) ;
        return ret;
    }

    private double[] inferenceSatisfaction(int destNodeIdx, double arrivalTime[]) {
        double pref[] = inferencePreference(destNodeIdx);
        double weather[] = weatherSuitability(destNodeIdx, arrivalTime);
        double values[] = {0, 1};
        if (weather == null) {
            logger.debug("왜?");
        }
        double weatherSuit = Erf.MyArrays.expectation(values, weather);
//        logger.debug("weather=" + DoubleArray.toString("%3.3f", weather) + "\t" + weatherSuit);

        double ret[] = new double[2];
        ret[0] = pref[0] * weatherSuit;
        ret[1] = 0.1;

        if (tripCPDs.getPoi(destNodeIdx).getIsRestaurant()) {
            ret[0] = ret[0] + 0.75;
        }
        return ret;
    }

    private double[] inferencePreference(int destNodeIdx) {
        BasicPoi poi = tripCPDs.getPoi(destNodeIdx);
        double ret[] = new double[2];
        ret[0] = poi.getSatisfaction(personalInfo);
        ret[1] = 0.1;
//        logger.debug("poi=" + poi.getName() + " preference=" + ret[0]);
        return ret;
    }

    private double[] inferenceCost(int destNodeIdx) {
        BasicPoi poi = tripCPDs.getPoi(destNodeIdx);
        double ret[] = new double[2];
        ret[0] = (double) poi.getCostPerPerson();
        ret[1] = 0.1;
//        logger.debug("poi=" + poi.getName() + " cost=" + ret[0]);
        return ret;


    }

    private double[] inferencePhysicalActivity(int destNodeIdx) {
        BasicPoi poi = tripCPDs.getPoi(destNodeIdx);
        double ret[] = poi.getPhysicalActivity();
//        logger.debug("poi=" + poi.getName() + " physical activity=" + ret[0]);
        return ret;

    }

    /**
     * 날씨 적합도 확률계산
     *
     * @return 결국 반환은 normal distribution으로 할것
     */
    private double[] weatherSuitability(int destNodeIdx, double arrivalTime[]) {
        // 주어진 날짜의 날짜는 위에 있고
//        double hour = tripNetwork.marg_T[order].getMean();
        BasicPoi poi = tripCPDs.getPoi(destNodeIdx);

        double hour = MyArrays.argMax(arrivalTime) * ((double) tripNodesAndValues.getDiscreteTimeWidth() / 60.0);
        WeatherEntry weatherEntry = WeatherProbability.getInstance().getWeatherEntry(year, monthOfYear, dayOfMonth, (int) hour);

        int tempCondition = Temperature.condition(weatherEntry.temperature);
        int rainCondition = MyArrays.argMax(weatherEntry.rainProbability);

//        // 시간,
//        if (bDebug) {
//        logger.debug(poi.getName() + "   tempCondition=" + tempCondition);
//        }
        TouristAttractionType type = poi.getTouristAttractionType();
        if (type == null) {

            return weatherType1_indoor.getProbability(rainCondition, tempCondition);
        }

        switch (type) {
            case Museum:
            case Aquarium:
            case HotSpring:
            case Cave:
                return weatherType1_indoor.getProbability(rainCondition, tempCondition);

            case SightseeingWithVehicle:
                return weatherType2_lessSensitive.getProbability(rainCondition, tempCondition);
            case NaturalLandmark:
            case HistoricalLandmark:
            case Market_Themestreet:
            case Architecture:
            case Temple:
            case Palace:
            case BotanicalGarden:
            case Zoo:
                return weatherType3_moreSensitive.getProbability(rainCondition, tempCondition);

            default:
                return weatherType4_sensitive.getProbability(rainCondition, tempCondition);
        }
//        logger.debug(hour);
//        logger.debug("tempCondition="+tempCondition + "\trainCondition="+rainCondition);


    }
}
