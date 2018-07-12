package services.qna;

import edu.hanyang.trip_planning.tripData.dataType.AddressCode;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 5. 8
 * Time: 오후 7:17
 * To change this template use File | Settings | File Templates.
 */
public interface InterfaceSpatialConstraints {
    /**
     * 주소기준 공간 제약조건
     *
     * @return
     */
    Set<AddressCode> symbolicSpatialConstraints();

    /**
     * InterfacePOI 및 좌표기준 공간 제약조건
     *
     * @return
     */
    Set<SpatialStatementByLocation> metricSpatialConstraints();
    // 위 세가지 조건은 모두 and임
    // 각 list안에서는 or 조건임
}
