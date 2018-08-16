package tripPlanning.tripHTBN.potential;

import cntbn.terms_factors.ContinuousFactor;

/**
 * Created by wykwon on 2015-09-25.
 */
public interface InterfaceCLGCPD {

    /**
     * CLG CPD의 분포를 획득한다.
     * discrete 부모 노드와 continuous 부모 노드들을 입력으로..
     *
     * @return
     */
    ContinuousFactor getDistribution();

}
