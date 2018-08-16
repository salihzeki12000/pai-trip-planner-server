package tripPlanning.tripData.poi;

import tripPlanning.tripData.dataType.*;
import tripPlanning.trip_question.PersonalInfo;
import tripPlanning.tripData.preference.TouristAttractionType;
import tripPlanning.trip_question.PreferenceOfPoiType;
import util.Pair;

import java.util.Objects;

public class BasicPoi {
    private int id;                                             // ID
    private String name;                                        // 이름
    private PoiType poiType;                                    // 장소의 종류                                        ? poiType class 필요한가?
    private Location location;                                  // 위치 (경위도)                                      ? location class 필요한가?
    private int averageCostPerPerson = -1;                      // 평균 비용
    private ProbabilisticDuration spendingTime;                 // 머무는 시간
    private double score;                                       // 사용자 만족도
    private String address;                                     // 주소
    private TouristAttractionType touristAttractionType = null; // ?
    private boolean isRestaurant;

    public BasicPoi(int id, String name, String address, PoiType poiType, Location location, double score) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.poiType = poiType;
        this.location = location;
        this.score = score;
        this.spendingTime = new ProbabilisticDuration(1.0, 0.05); // default spendingTime = 1hour +- 10min = 95%
        this.isRestaurant = this.poiType.category.equals("음식점");
        this.initTouristAttractionType();
    }

    public boolean getIsRestaurant() {
        return isRestaurant;
    }

    public double getScore() {
        return this.score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public PoiType getPoiType() {
        return poiType;
    }

    public ProbabilisticDuration getSpendingTime() {
        return spendingTime;
    }

    public Location getLocation() {
        return location;
    }

    public Double getSatisfaction(PersonalInfo personalInfo) {
        double defaultValue = 0.5;
        PreferenceOfPoiType preferenceOfPoiType = personalInfo.getPreferenceOfPoiType();

        for (int i = 0; i < preferenceOfPoiType.size(); i++) {
            Pair<PoiType, Double> preferredPoiTypePair = preferenceOfPoiType.getPoiTypePreference(i);
            if (preferredPoiTypePair.first().contain(this.poiType)) {
                return preferredPoiTypePair.second();
            }
        }
        return defaultValue;
    }

    public Integer getAverageCostPerPerson() {
        return averageCostPerPerson;
    }

    public TouristAttractionType getTouristAttractionType() {
        if (this.touristAttractionType != null) {
            return this.touristAttractionType;
        } else {
            return null;
        }
    }

    public double[] getPhysicalActivity() {
        double bodyWeight = 70;
        // 1MET = 1 * kCal / (kg * h)
        // kCal = MET*kg * h

        double ret[] = new double[2];
        if (this.touristAttractionType == null) {
            ret[0] = 0.0;
            ret[1] = 1.0;
            return ret;
        }

        double mean;
        double var;
        switch (this.touristAttractionType) {
            case MountaineeringTrail:
                mean = 6.5;
                var = 1.0;
                break;
            case Fall_Vally:
                mean = 5.0;
                var = 0.5;
                break;
            case HikingTrail:
                mean = 3.3;
                var = 0.33;
                break;
            case Cave:
            case Island:
            case Beach:
            case Themepark:
            case Market_Themestreet:
            case HistoricalLandmark:
            case NaturalLandmark:
            case Farm:
            case BotanicalGarden:
                mean = 2.5;
                var = 0.2;
                break;
            case Zoo:
            case Aquarium:
            case Temple:
            case Architecture:
            case Palace:
            case Museum:
                mean = 2.3;
                var = 0.15;
                break;
            case Park:
                mean = 2.0;
                var = 0.1;
                break;
            case SightseeingWithVehicle:
            case HotSpring:
                mean = 1.1;
                var = 0.1;
                break;
            default:
                mean = 0.0;
                var = 1.0;
                break;
        }
        Pair<Double, Double> met = new Pair<>(mean, var);

        double newMean = spendingTime.hour * met.first() * bodyWeight;
        // 새로운 SD는 mean 에 대한 비율로 처리하는게 나을것 같군.
        double metSDRatio = met.second() / met.first();
        double durationRatio = spendingTime.standardDeviation / spendingTime.hour;
        double sdRatio = Math.sqrt((metSDRatio * durationRatio) / (metSDRatio + spendingTime.hour));

        double newSD = newMean * sdRatio;
        ret[0] = newMean;
        ret[1] = newSD * newSD;
        return ret;
    }

    private void initTouristAttractionType() {
        if (poiType.subSubCategory == null || poiType.subSubCategory.isEmpty()) {
            return;
        }
        this.touristAttractionType = TouristAttractionType.parse(poiType.subSubCategory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPoi basicPoi = (BasicPoi) o;
        return id == basicPoi.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BasicPoi{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", poiType=" + poiType +
                ", location=" + location +
                ", averageCostPerPerson=" + averageCostPerPerson +
                ", spendingTime=" + spendingTime +
                ", score=" + score +
                ", touristAttractionType=" + touristAttractionType +
                ", isRestaurant=" + isRestaurant +
                '}';
    }

    public static void main(String[] args) {
////1. ID로 이름 만들고
//        BasicPoi poi = new BasicPoi("daum.111", "하동관", new Location(111, 222));
////3.  주소
//        poi.setAddress(new Address("대한민국", "서울특별시", "중구", "명동9길 12"));
////4. 장소종류
//        poi.setPoiType(new PoiType("음식점", "한식", "곰탕"));
////5. 가능한 활동
//        poi.addActivity(ActivityType.Eat);
//// 8. 휴일
//        ClosingDays closingDays = new ClosingDays();
//        closingDays.addMonthlyClosingDay(1, DayOfWeek.Sunday);
//        closingDays.addMonthlyClosingDay(3, DayOfWeek.Sunday);
//        poi.setClosingDays(closingDays);
//        // 10. 평균비용
//        poi.setAverageCostPerPerson(11000);
//        // 소요시간
//        poi.setSpendingHour(40, 10);
//        System.out.println(poi);
//
//        for (String str : poi.toStrArray()) {
//            logger.debug(str);
//        }
    }
}
