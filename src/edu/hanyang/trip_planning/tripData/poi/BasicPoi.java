package edu.hanyang.trip_planning.tripData.poi;

import com.google.gson.Gson;

import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.trip_question.PersonalInfo;
import edu.hanyang.trip_planning.tripData.preference.TouristAttractionType;
import edu.hanyang.trip_planning.trip_question.PreferenceOfPoiType;
import util.Pair;

import java.util.Objects;

public class BasicPoi {
    private String id;                                          // ID
    private String title;                                       // 이름
    private String address;
    private PoiType poiType;                                    // 장소의 종류                                        ? poiType class 필요한가?
    private Location location;                                  // 위치 (경위도)                                      ? location class 필요한가?
    private BusinessHour businessHour;                          // 영업시간
    private ClosingDays closingDays;                            // 휴일
    private int averageCostPerPerson = -1;                      // 평균 비용
    private ProbabilisticDuration spendingTime;                 // 머무는 시간
    private double score;                                       // 사용자 만족도
    private String placeUrl;
    private TouristAttractionType touristAttractionType = null; // ?
    private boolean isRestaurant;

    public BasicPoi(String id, String title, String address, PoiType poiType, Location location, BusinessHour businessHour, ClosingDays closingDays, double score, String placeUrl) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.poiType = poiType;
        this.location = location;
        this.businessHour = businessHour;
        this.closingDays = closingDays;
        this.score = score;
        this.placeUrl = placeUrl;
        this.spendingTime = new ProbabilisticDuration(1.0, 0.05); // default spendingTime = 1hour +- 10min = 95%
        this.isRestaurant = this.poiType.category.equals("음식점");
        this.initTouristAttractionType();
    }

    public boolean getIsRestaurant() {
        return isRestaurant;
    }

    public void setAverageCostPerPerson(int costPerPerson) {
        this.averageCostPerPerson = costPerPerson;
    }

    private void setSpendingHour(double hour, double standardDeviation) {
        this.spendingTime = new ProbabilisticDuration(hour, standardDeviation);
    }

    public double getScore() {
        return this.score;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    public BusinessHour getBusinessHour() {
        return businessHour;
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

    public String getPlaceUrl() {
        return placeUrl;
    }

    public static String[] csvHeader() {
        return new String[]{"#id", "title", "latitude", "longitude", "othernames", "category",
                "subcategory", "subsubcategory", "Addresscode_1", "AddressCode_2", "AddressCode_3", "Detailed_address", "BusinessHours", "closingdays", "parkingLot", "cost",
                "publicTransportationAccess", "satisfaction", "spendingTime", "placeURL"
        };
    }

    public String[] toStrArray() {
        Gson gson = new Gson();
        String array[] = new String[22];
        array[0] = id;
        array[1] = title;
        array[2] = Double.toString(location.latitude);
        array[3] = Double.toString(location.longitude);
        array[4] = "";
        array[5] = poiType.category;
        array[6] = poiType.subCategory;
        array[7] = poiType.subSubCategory;
        array[8] = "";
        array[9] = "";
        array[10] = "";
        array[11] = "";
        array[12] = gson.toJson(businessHour);
        array[13] = gson.toJson(closingDays);
        array[14] = "";
        array[15] = Integer.toString(averageCostPerPerson);
        array[16] = "";
        array[17] = Double.toString(score);
        if (spendingTime != null) {
            array[18] = Double.toString(spendingTime.hour);
            array[19] = Double.toString(spendingTime.standardDeviation);
        }
        array[20] = placeUrl;

        return array;
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
        if (poiType.subSubCategory == null && poiType.subSubCategory.isEmpty()) {
            return;
        }
        this.touristAttractionType = TouristAttractionType.parse(poiType.subSubCategory);
    }

    public static BasicPoi parse(String array[]) {
        Gson gson = new Gson();
        String id = array[0];
        String title = array[1];
        double latitude = Double.parseDouble(array[2]);
        double longitude = Double.parseDouble(array[3]);
        Location location = new Location(latitude, longitude);
        PoiType poiType = new PoiType(array[5], array[6], array[7]);
        String address = array[9] + " " + array[10] + " " + array[11];
        BusinessHour businessHour = gson.fromJson(array[12], BusinessHour.class);
        businessHour.boot();
        ClosingDays closingDays = gson.fromJson(array[13], ClosingDays.class);
        int averageCostPerPerson = Integer.parseInt(array[15]);
        double score = Double.parseDouble(array[17]);
        double spendingTime = Double.parseDouble(array[18]);
        double spendingSD = Double.parseDouble(array[19]);
        String placeUrl = array[20];

        BasicPoi basicPoi = new BasicPoi(id, title, address, poiType, location, businessHour, closingDays, score, placeUrl);

        basicPoi.setAverageCostPerPerson(averageCostPerPerson);
        basicPoi.setSpendingHour(spendingTime, spendingSD);
        basicPoi.initTouristAttractionType();

        return basicPoi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicPoi basicPoi = (BasicPoi) o;
        return Objects.equals(id, basicPoi.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BasicPoi{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", address=" + address +
                ", poiType=" + poiType +
                ", location=" + location +
                ", businessHour=" + businessHour +
                ", closingDays=" + closingDays +
                ", averageCostPerPerson=" + averageCostPerPerson +
                ", spendingTime=" + spendingTime +
                ", score=" + score +
                ", placeUrl='" + placeUrl + '\'' +
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
////7. 영업시간
//        poi.setBusinessHour(new BusinessHour("07:00", "16:30"));
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