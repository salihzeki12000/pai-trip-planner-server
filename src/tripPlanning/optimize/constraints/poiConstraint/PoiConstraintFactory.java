package tripPlanning.optimize.constraints.poiConstraint;

// mgkim:
public class PoiConstraintFactory {
    // 정방폭포
    public static PoiConstraint poiConstraintExample1() {
        PoiConstraint poiConstraint = new PoiConstraint("정방폭포", true);
        return poiConstraint;
    }

    // 제주미니랜드
    public static PoiConstraint poiConstraintExample2() {
        PoiConstraint poiConstraint = new PoiConstraint("제주미니랜드", false);
        return poiConstraint;
    }

    // 한림공원
    public static PoiConstraint poiConstraintExample4() {
        PoiConstraint poiConstraint = new PoiConstraint("한림공원", true);
        return poiConstraint;
    }

    // 테디베어뮤지엄 제주점
    public static PoiConstraint poiConstraintExample5() {
        PoiConstraint poiConstraint = new PoiConstraint("테디베어뮤지엄 제주점", true);
        return poiConstraint;
    }
}
