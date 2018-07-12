package edu.hanyang.trip_planning.tripData.preference;

/**
 * Created by wykwon on 2015-10-21.
 */
public enum RestaurantType {

    SightseeingWithVehicle("투어버스/유람선/관광열차"),
    Fall_Vally("폭포/계곡"),
    MountaineeringTrail("산봉우리/등산로"),
    Cave("동굴"),
    Island("섬"),
    Park("공원"),
    HikingTrail("휴양림/숲길걷기"),
    HotSpring("온천"),
    Beach("해수욕장/해변"),
    Museum("미술관/박물관"),
    Zoo("동물원"),
    Aquarium("아쿠아리움"),
    Themepark("테마파크"),
    Market_Themestreet("시장/테마거리"),
    HistoricalLandmark("역사유적"),
    Temple("종교시설"),
    Architecture("건축물"),
    Palace("고궁"),
    NaturalLandmark("자연경관"),
    Farm("관광농원"),
    BotanicalGarden("수목원/식물원");

    private String value;
//        식사, 운동, 회의, 관광, 산책, 공연, 쇼핑, 일

    RestaurantType(final String value) {
        this.value = value;
    }


    public static RestaurantType parse(String str) {
        for (RestaurantType type : RestaurantType.values()) {
            if (type.getValue().equals(str)){
                return type;
            }
        }

        return null;

    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

//    public static void main(String[] args) {
//        System.out.println(TouristAttractionType);
//    }
}

