package cntbn.terms_factors;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 8. 18
 * Time: 오후 12:00
 * To change this template use File | Settings | File Templates.
 */
public enum GaussianType {
    CLG, NonWeightedCLG, TDGaussian, SimpleGaussian

    /**
     * 구분빙법-
     *
     * CLG: 그냥 _ConditionalLinearGaussian
     * GaussianFunction: 일반적인 1번수 Gaussian
     * NonWeightedCLG: CLG에서 conditional vairable의 weight가 모두 1인거
     * TDGaussian: conditional variable이 1개이고, weight가 1인거
     */
}
