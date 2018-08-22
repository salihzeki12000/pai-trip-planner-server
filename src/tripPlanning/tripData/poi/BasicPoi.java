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
    private String address;                                     // 주소
    private Location location;                                  // 위치 (경위도)                                      ? location class 필요한가?
    private double score;                                       // 사용자 만족도
    private BusinessHours businessHours;                        // 영업시간
    private int costPerPerson = -1;                             // 평균 비용
    private ProbabilisticDuration spendingTime;                 // 머무는 시간
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
        this.initTouristAttractionType();
        this.isRestaurant = this.poiType.category.equals("음식점");
    }

    public BasicPoi(int id, String name, PoiType poiType, String address, Location location, double score, BusinessHours businessHours) {
        this.id = id;
        this.name = name;
        this.poiType = poiType;
        this.address = address;
        this.location = location;
        this.score = score;
        this.businessHours = businessHours;
        this.costPerPerson = -1;
        this.spendingTime = new ProbabilisticDuration(1.0, 0.05); // default spendingTime = 1hour +- 10min = 95%
        this.initTouristAttractionType();
        this.isRestaurant = this.poiType.category.equals("음식점");
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

    public Integer getCostPerPerson() {
        return costPerPerson;
    }

    public TouristAttractionType getTouristAttractionType() {
        if (this.touristAttractionType != null) {
            return this.touristAttractionType;
        } else {
            return null;
        }
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
                ", poiType=" + poiType +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", score=" + score +
                ", businessHours=" + businessHours +
                ", costPerPerson=" + costPerPerson +
                ", spendingTime=" + spendingTime +
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
