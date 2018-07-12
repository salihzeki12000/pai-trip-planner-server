package services.qna;


import java.util.List;

/**
 * 모든 조건은 or 처리
 * <p/>
 * 복잡한 언어표현은 해석이 끝나야 한다.
 * <p/>
 * 어렵다. ㅜㅜ
 * and
 */
public interface InterfaceTemporalConstraints {

    /**
     * 포함되어야 하는 시간
     *
     * @return
     */
    List<TemporalConstraint> includingConstraints();

    /**
     * 제외되어야 하는 시간
     *
     * @return
     */
    List<TemporalConstraint> excludingConstraints();


}

