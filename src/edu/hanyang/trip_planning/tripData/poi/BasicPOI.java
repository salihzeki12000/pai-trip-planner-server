package edu.hanyang.trip_planning.tripData.poi;

import com.google.gson.Gson;

import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.trip_question.PersonalInfo;
import edu.hanyang.trip_planning.tripData.preference.TouristAttractionType;
import edu.hanyang.trip_planning.trip_question.PreferenceOfPOIType;
import util.Pair;

import java.util.*;

public class BasicPOI {
    private String id;                                          // ID
    private String title;                                       // 이름
    private Address address;                                    // 주소                                               ? address class 필요한가?
    private POIType poiType;                                    // 장소의 종류                                        ? poitype class 필요한가?
    private Set<ActivityType> availableActivities;              // 해당 장소에서 할수 있는 활동들                     ?
    private Location location;                                  // 위치 (경위도)                                      ?
    private BusinessHour businessHour;                          // 영업시간
    private ClosingDays closingDays;                            // 휴일
    private int averageCostPerPerson = -1;                      // 평균 비용
    private ProbabilisticDuration spendingTime;                 // 머무는 시간
    private double score = -1;                                  // 사용자 만족도
    private String placeUrl;
    private TouristAttractionType touristAttractionType = null; // ?
    private boolean isRestaurant = false;                       // ?

    public BasicPOI(String title) {
        this.title = title;
    }

    public BasicPOI(String id, String title, Location location) {
        this.id = id;
        this.title = title;
        this.location = location.deepCopy();
        availableActivities = new HashSet<>();
        address = new Address();
        // 기본 소요시간은 1시간  +- 10분
        spendingTime = new ProbabilisticDuration(0.5, 0.1);
    }

    public void setAddress(Address address) {
        this.address = address.deepCopy();
    }

    public boolean getIsRestaurant() {
        return isRestaurant;
    }

    public void setPoiType(POIType poiType) {
        this.poiType = poiType.deepCopy();
        this.isRestaurant = this.poiType.category.equals("음식점");
    }

    public void setBusinessHour(BusinessHour businessHour) {
        this.businessHour = businessHour.deepCopy();
    }

    public void setClosingDays(ClosingDays closingDays) {
        this.closingDays = closingDays.deepCopy();
    }

    public void setAverageCostPerPerson(int costPerPerson) {
        this.averageCostPerPerson = costPerPerson;
    }

    public void setSpendingHour(double hour, double standardDeviation) {
        this.spendingTime = new ProbabilisticDuration(hour, standardDeviation);
    }

    public void setSpendingMinutes(double minute, double standardDeviation) {
        this.spendingTime = new ProbabilisticDuration(minute / 60.0, standardDeviation / 60.0);
    }

    public void setTouristAttractionType(String type) {
        this.touristAttractionType = TouristAttractionType.parse(type);
    }

    public void setScore(double value) {
        this.score = value;
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

    public Address getAddress() {
        return address;
    }

    public POIType getPoiType() {
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
        PreferenceOfPOIType preferenceOfPOIType = personalInfo.getPreferenceOfPOIType();

        for (int i = 0; i < preferenceOfPOIType.size(); i++) {
            Pair<POIType, Double> preferredPOITypePair = preferenceOfPOIType.getPOITypePreference(i);

            // poiType 이 match 되면?
            if (preferredPOITypePair.first().contain(this.poiType)) {
                return preferredPOITypePair.second();
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

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public String toString() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("id: " + id + '\n');
        strBuf.append("Address: " + address.toString() + '\n');
        strBuf.append("Poi type : " + poiType + '\n');
        strBuf.append("Available Activity: " + availableActivities + "\n");
        strBuf.append("location: " + location + '\n');
        if (businessHour != null) {
            strBuf.append(businessHour.toString() + '\n');
        }
        strBuf.append("휴무일: " + closingDays + "\n");
        strBuf.append("평균비용: " + averageCostPerPerson + "\n");
        strBuf.append("만족도(평점): " + score + "\n");
        strBuf.append("소요시간:" + spendingTime + "\n");
        return strBuf.toString();
    }

    public static String[] csvHeader() {
        String header[] = {"#id", "title", "latitude", "longitude", "othernames", "category",
                "subcategory", "subsubcategory", "Addresscode_1", "AddressCode_2", "AddressCode_3", "Detailed_address", "BusinessHours", "closingdays", "parkingLot", "cost",
                "publicTransportationAccess", "satisfaction", "spendingTime", "placeURL"
        };
        return header;
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
        array[8] = address.addressCode.countryCode;
        array[9] = address.addressCode.provinceCode;
        array[10] = address.addressCode.cityCode;
        array[11] = address.detailedAddress;
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
        return getPhysicalActivity(70);
    }

    public double[] getPhysicalActivity(double bodyWeight) {
        double ret[] = new double[2];
        if (this.touristAttractionType == null) {
            ret[0] = 0.0;
            ret[1] = 1.0;
            return ret;
        }
        // 1MET = 1 * kcal / (kg * h)
        // kcal = MET*kg * h
        Pair<Double, Double> met = getMETPerHour();
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

    public Pair<Double, Double> getMETPerHour() {
        double mean = 0.0;
        double var = 1.0;

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
        }

        return new Pair<>(mean, var);
    }

    public void initTouristAttractionType() {
        if (poiType.subSubCategory == null && poiType.subSubCategory.isEmpty()) {
            return;
        }
        this.touristAttractionType = TouristAttractionType.parse(poiType.subSubCategory);
    }

    public static BasicPOI parse(String array[]) {
        Gson gson = new Gson();
        String id = array[0];
        String title = array[1];
        double latitude = Double.parseDouble(array[2]);
        double longitude = Double.parseDouble(array[3]);

        BasicPOI basicPOI = new BasicPOI(id, title, new Location(latitude, longitude));

        basicPOI.setPoiType(new POIType(array[5], array[6], array[7]));

        AddressCode addressCode = new AddressCode(array[8], array[9], array[10]);
        Address address = new Address(addressCode, array[11]);

        basicPOI.setAddress(address);

        BusinessHour businessHour = gson.fromJson(array[12], BusinessHour.class);
        businessHour.boot();
        if (businessHour != null) {
            basicPOI.setBusinessHour(businessHour);
        }

        ClosingDays closingDays = gson.fromJson(array[13], ClosingDays.class);
        if (closingDays != null) {
            basicPOI.setClosingDays(closingDays);
        }

        int averageCostPerPerson = Integer.parseInt(array[15]);
        basicPOI.setAverageCostPerPerson(averageCostPerPerson);

        if (array[17].length() > 0) {
            double satisfaction = Double.parseDouble(array[17]);
            basicPOI.setScore(satisfaction);
        }
        if (array[17].length() > 0 && array[18].length() > 0) {
            double spendingTime = Double.parseDouble(array[18]);
            double spendingSD = Double.parseDouble(array[19]);
            basicPOI.setSpendingHour(spendingTime, spendingSD);
        }
        if (array.length > 20) {
            basicPOI.setPlaceUrl(array[20]);
        }

        basicPOI.initTouristAttractionType();

        return basicPOI;
    }

    public static void main(String[] args) {
////1. ID로 이름 만들고
//        BasicPOI poi = new BasicPOI("daum.111", "하동관", new Location(111, 222));
////3.  주소
//        poi.setAddress(new Address("대한민국", "서울특별시", "중구", "명동9길 12"));
////4. 장소종류
//        poi.setPoiType(new POIType("음식점", "한식", "곰탕"));
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

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
