package edu.hanyang.trip_planning.tripData.poi;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.trip_question.PersonalInfo;
import edu.hanyang.trip_planning.tripData.preference.TouristAttractionType;
import edu.hanyang.trip_planning.trip_question.PreferenceOfPOIType;
import util.Pair;

import java.util.*;

/**
 * 장소(Point Of Interest) 클래스
 * <p/>
 * 장소의 선호도나, 다른 것들은 평균적인 선호도나 평점을 이용한다.
 */
public class BasicPOI {
    private static Logger logger = Logger.getLogger(BasicPOI.class);
    private String id;                                  // 1. 위치 식별자
    private String title;
    private Set<String> names;                          // 2. 이름 (별칭 포함 1번째가 대표이름임)
    private Address address;                            // 3. 주소
    private POIType poiType;                            // 4. 장소의 종류
    private Set<ActivityType> availableActivities;      // 5. 해당 장소에서 할수 있는 활동들                      ?
    private Location location;                          // 6. 위치 (경위도)
    private BusinessHour businessHour;                  // 7. 영업시간
    private ClosingDays closingDays;                    // 8. 휴일
    private int hasParkingLot = -1;                     // 9. 주차가능 여부   (-1: Unknwon, 0: false, 1: true)    ?
    private int averageCostPerPerson = -1;              // 10. 평균 비용
    private Set<AdjacentPOI> publicTransportationAccess;// 11. 대중교통 접근성 (예: XX 지하철 역에서 몇분거리)    ?
    private ProbabilisticDuration spendingTime;         // 12. 머무는 시간
    private double score = -1;                          // 13. 사용자 만족도
    private Map<String, String> urlList = new HashMap<String, String>();
    private TouristAttractionType touristAttractionType = null;
    private boolean bRestaurant = false;

    public BasicPOI(String title) {
        this.title = title;
    }

    public BasicPOI(String id, String title, Location location) {
        this.id = id;
        this.title = title;
        this.location = location.deepCopy();
        names = new HashSet<>();
        availableActivities = new HashSet<>();
        publicTransportationAccess = new HashSet<>();
        address = new Address();
        // 기본 소요시간은 1시간  +- 10분
        spendingTime = new ProbabilisticDuration(0.5, 0.1);
    }

    public void addName(String name) {
        names.add(name);
    }

    public void addNames(String nameArray[]) {
        for (String name : nameArray) {
            this.names.add(name);
        }
    }

    public void setAddress(Address address) {
        this.address = address.deepCopy();
    }

    public boolean isbRestaurant() {
        return bRestaurant;
    }

    public void setPoiType(POIType poiType) {
        this.poiType = poiType.deepCopy();
        POIType restaurantType = new POIType("음식점");
        bRestaurant = restaurantType.contain(poiType);
    }

    public void setLocation(double latitude, double longtitude) {
        this.location = new Location(latitude, longtitude);
    }

    public void addActivity(ActivityType activity) {
        this.availableActivities.add(activity);
    }

    public void setParking(int parkingFlag) {
        this.hasParkingLot = parkingFlag;
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

    public void addPublicTransportationAccess(String poiIdentifier, int walkTime) {
        this.publicTransportationAccess.add(new AdjacentPOI(poiIdentifier, walkTime));
    }

    public void addPublicTransportationAccess(AdjacentPOI adjacentPOI) {
        this.publicTransportationAccess.add(adjacentPOI);
    }

    public void setSpendingHour(double hour, double standardDeviation) {
        this.spendingTime = new ProbabilisticDuration(hour, standardDeviation);
    }

    public void setSpendingMinutes(double minute, double standardDeviation) {
        this.spendingTime = new ProbabilisticDuration(minute / 60.0, standardDeviation / 60.0);
    }

    public void setTouristAttractionType(String type) {
        logger.debug(this.title);
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

    public Set<String> getNames() {
        return names;
    }

    public Address getAddress() {
        return address;
    }

    public POIType getPoiType() {
        return poiType;
    }

    public ProbabilisticDuration getSpendingTime(PersonalInfo personalInfo, String startTime) {
        return spendingTime;
    }

    public Location getLocation() {
        return location;
    }

    public BusinessHour getBusinessHour() {
        return businessHour;
    }

    public Integer getParkingLotInfo() {
        return hasParkingLot;
    }

    public Double getSatisfaction(PersonalInfo personalInfo, TimeAndDuration timeAndDuration) {
        double defaultValue = 0.5;
        PreferenceOfPOIType preferenceOfPOIType = personalInfo.getPreferenceOfPOIType();

        for (int i = 0; i < preferenceOfPOIType.size(); i++) {
            Pair<POIType, Double> preferedPOITypePair = preferenceOfPOIType.getPOITypePreference(i);

            // poiType 이 match 되면?
            if (preferedPOITypePair.first().contain(this.poiType)) {
                return preferedPOITypePair.second();
            }
        }
        return defaultValue;
    }

    
    public Integer getAverageCostPerPerson() {
        return averageCostPerPerson;
    }

    public Set<String> getAmbiences() {
        return null;
    }

    /**
     * additional URL을 반환한다.
     *
     * @param key
     * @return
     */
    public String getURL(String key) {
        return urlList.get(key);
    }

    public Set<String> urlKeySet() {
        return urlList.keySet();
    }

    public void addURL(String key, String url) {
        urlList.put(key, url);
    }

    public String toString() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("id: " + id + '\n');
        strBuf.append("Other names : " + names + '\n');
        strBuf.append("Address: " + address.toString() + '\n');
        strBuf.append("Poi type : " + poiType + '\n');
        strBuf.append("Available Activity: " + availableActivities + "\n");
        strBuf.append("location: " + location + '\n');
        if (businessHour != null) {
            strBuf.append(businessHour.toString() + '\n');
        }
        strBuf.append("휴무일: " + closingDays + "\n");
        strBuf.append("주차여부: " + getParkingLotInfo() + "\n");
        strBuf.append("평균비용: " + averageCostPerPerson + "\n");
        strBuf.append("대중교통 접근여부: " + publicTransportationAccess + "\n");
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
        array[4] = toString(names);
        array[5] = poiType.category;
        array[6] = poiType.subCategory;
        array[7] = poiType.subSubCategory;
        array[8] = address.addressCode.countryCode;
        array[9] = address.addressCode.provinceCode;
        array[10] = address.addressCode.cityCode;
        array[11] = address.detailedAddress;
        array[12] = gson.toJson(businessHour);
        array[13] = gson.toJson(closingDays);
        array[14] = Integer.toString(hasParkingLot);
        array[15] = Integer.toString(averageCostPerPerson);
        array[16] = gson.toJson(publicTransportationAccess);
        array[17] = Double.toString(score);
        if (spendingTime != null) {
            array[18] = Double.toString(spendingTime.hour);
            array[19] = Double.toString(spendingTime.standardDeviation);
        }
        array[20] = urlList.get("place");

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

    private void initRestaurantType() {
        this.poiType = poiType.deepCopy();
        POIType restaurantType = new POIType("음식점");
        bRestaurant = restaurantType.contain(poiType);
    }

    public static BasicPOI parse(String array[]) {
        Gson gson = new Gson();
        String id = array[0];
        String title = array[1];
        double latitude = Double.parseDouble(array[2]);
        double longitude = Double.parseDouble(array[3]);

        BasicPOI basicPOI = new BasicPOI(id, title, new Location(latitude, longitude));

        basicPOI.addNames(array[4].split(","));

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

        int hasParkingLot = Integer.parseInt(array[14]);
        basicPOI.setParking(hasParkingLot);
        int averageCostPerPerson = Integer.parseInt(array[15]);
        basicPOI.setAverageCostPerPerson(averageCostPerPerson);
        if (array[16].length() > 3) {
            logger.debug(array[16] + "\t size=" + array[16].length());
            AdjacentPOI adjacentPOIs[] = gson.fromJson(gson.toJson(array[16]), AdjacentPOI[].class);
            for (AdjacentPOI adjacentPOI : adjacentPOIs) {
                basicPOI.addPublicTransportationAccess(adjacentPOI);
            }
        }

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
            basicPOI.addURL("place", array[20]);
        }

        basicPOI.initTouristAttractionType();
        basicPOI.initRestaurantType();

        return basicPOI;
    }

    public static void main(String[] args) {
//1. ID로 이름 만들고
        BasicPOI poi = new BasicPOI("daum.111", "하동관", new Location(111, 222));
//2. 이름들 넣고
        poi.addName("하동관");
        poi.addName("하동관 본점");
//3.  주소
        poi.setAddress(new Address("대한민국", "서울특별시", "중구", "명동9길 12"));
//4. 장소종류
        poi.setPoiType(new POIType("음식점", "한식", "곰탕"));
//5. 가능한 활동
        poi.addActivity(ActivityType.Eat);
//6. 좌표
        poi.setLocation(39.564380, 126.984978);
//7. 영업시간
        poi.setBusinessHour(new BusinessHour("07:00", "16:30"));
// 8. 휴일
        ClosingDays closingDays = new ClosingDays();
        closingDays.addMonthlyClosingDay(1, DayOfWeek.Sunday);
        closingDays.addMonthlyClosingDay(3, DayOfWeek.Sunday);
        poi.setClosingDays(closingDays);
//9. 주차가능여부
        poi.setParking(1);
        // 10. 평균비용
        poi.setAverageCostPerPerson(11000);
        // 11 대중교통 접근성
        poi.addPublicTransportationAccess("을지로입구역", 5);
        poi.addPublicTransportationAccess("을지로3가역", 10);
        poi.addPublicTransportationAccess("종각역", 15);
        // 12. 만족도
        poi.setScore(5.5);
        // 소요시간
        poi.setSpendingHour(40, 10);
        System.out.println(poi);

        for (String str : poi.toStrArray()) {
            logger.debug(str);
        }

    }

    private String toString(Set<String> strSet) {
        StringBuilder builder = new StringBuilder();
        for (String str : strSet) {
            builder.append(str);
            builder.append(',');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
