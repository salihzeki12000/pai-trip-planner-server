package edu.hanyang.trip_planning.tripData.poi;

import edu.hanyang.trip_planning.tripData.dataType.Address;
import edu.hanyang.trip_planning.tripData.dataType.*;
import edu.hanyang.trip_planning.tripData.personalInfo.PersonalInfo;

import java.util.Set;

/**
 * 장소(Point Of Interest) 인터페이스
 */
public interface InterfacePOI {

    // 위치 식별자

    String getId();

    String getTitle();


    /**
     * 이름: 별칭 포함
     * 1번째가 대표이름임
     */
    Set<String> getNames();

    /**
     * 주소
     */
    Address getAddress();

    /**
     * 장소의 종류
     */
    POIType getPoiType();

    /**
     * 사용자의 종류 및 시간에 따라 머무는 시간이 달라짐
     *
     * @return
     */
    ProbabilisticDuration getSpendingTime(PersonalInfo personalInfo,String startTime);

    /**
     * 해당 장소에서 할수 있는 활동들
     */
    Set<ActivityType> getAvaliableActivities();

    /**
     * 위치 (경위도)
     */
    Location getLocation();


    /**
     * 영업시간
     */
    BusinessHour getBusinessHour();

    /**
     * 휴무일
     */
    ClosingDays getClosingDays();

    /**
     * 대중교통 접근성
     * <p/>
     * 예: XX 지하철 역에서 몇분거리
     */
    Set<AdjacentPOI> getPubicTransportationAccess();


    /**
     * 주차가능 여부
     */
    Integer getParkingLotInfo();

    /**
     * 사용자 만족도
     *
     * @param userType        사용자 type
     * @param timeAndDuration 이용 시작시간 및 기간
     */
    Double getSatisifaction(PersonalInfo userType, TimeAndDuration timeAndDuration);

    /**
     * 평균 비용
     */
    Integer getAverageCostPerPerson();

    /**
     * 분위기
     */
    Set<String> getAmbiences();


}
