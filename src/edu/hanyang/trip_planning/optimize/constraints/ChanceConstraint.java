package edu.hanyang.trip_planning.optimize.constraints;

import org.apache.log4j.Logger;
import wykwon.common.Erf;

/**
 * Created by wykwon on 2015-09-22.
 */
public class ChanceConstraint {
    private static Logger logger = Logger.getLogger(ChanceConstraint.class);

    /**
     * $P(T_i < open_i |\mathcal{I},t_S ) \le \epsilon$
     * 등의 확률적인 제약조건을 따지는 클래스임
     * <p/>
     * open_i : referenceValue
     * <p/>
     * \epsilon: confidence interval
     * g_i(X) < 0 인 constraint를 만들어냄.
     */

    public enum LimitType {
        Upper, Lower
    }

    LimitType limitType;
    private double mean;
    private double variance;
    private double referenceValue;
    private double confidenceLevel;


    public ChanceConstraint(double mean, double variance, double referenceValue, LimitType limitType) {
        this.mean = mean;
        this.variance = variance;
        // 부등호 방향 결정 $P(T<t) < epsilon$ 인지 $P(T<t) > epsilon$ 인지를 결정. 전자가 bGreater=true임
        this.limitType = limitType;
        this.referenceValue = referenceValue;
        this.confidenceLevel = 0.95;
    }


    public ChanceConstraint(double mean, double variance, double referenceValue, LimitType limitType, double confidenceLevel) {
        this.mean = mean;
        this.variance = variance;
        this.limitType = limitType;
        this.referenceValue = referenceValue;
        this.confidenceLevel = confidenceLevel;
    }

    /**
     * Debbs 의 constraint 함수
     * 0보다 작으면 constraint 해당됨
     * @param referenceValue
     * @param limitType
     * @param confidenceLevel
     * @return
     */
    public static double inequalityValue(double dist[], double referenceValue, LimitType limitType, double confidenceLevel) {
        double mean = dist[0];
        double variance = dist[1];
        double culminativeProbability = 0.5 * (1 + Erf.erf((referenceValue - mean) / Math.sqrt(2 * variance)));
        double value = 0.0;

        if (limitType == LimitType.Lower) {
            // $P(T<t) >= epsilon$ 인 경우
            // $P(T<t) - epsilon >=  0 $ 으로 치환됨
//            value =  1- culminativeProbability - confidenceLevel;
            value = (culminativeProbability - confidenceLevel) / (1 - confidenceLevel);
        } else {
            // $P(T>t) >= epsilon$ 인 경우
            // $ 1 -P(T<t) >= epsilon$ 으로 치환된다음에
            value = (1 - confidenceLevel - culminativeProbability) / (1 - confidenceLevel);
        }
        return value;
    }

    public static double inequalityValue(double mean, double variance, double referenceValue, LimitType limitType, double confidenceLevel) {
        //        logger.debug(NodeDictionary.getInstance());
        logger.debug("mean="+mean + "\treference value="+referenceValue);

        double culminativeProbability = 0.5 * (1 + Erf.erf((referenceValue - mean) / Math.sqrt(2 * variance)));

        logger.debug(culminativeProbability);
        logger.debug(confidenceLevel);
        double value;
        if (limitType == LimitType.Lower) {
            // $P(T<t) >= epsilon$ 인 경우
            // $P(T<t) - epsilon >=  0 $ 으로 치환됨
//            value =  1- culminativeProbability - confidenceLevel;
            value = (culminativeProbability - confidenceLevel) / (1 - confidenceLevel);

            logger.debug(value);
        } else {
            // $P(T>t) >= epsilon$ 인 경우
            // $ 1 -P(T<t) >= epsilon$ 으로 치환된다음에
            value = (1 - confidenceLevel - culminativeProbability) / (1 - confidenceLevel);
            logger.debug(value);
        }

        return value;
    }

//    /**
//     * @param nodeIdx         node이름
//     * @param referenceValue
//     * @param confidenceLevel 확률값.
//     */
//    public void setUpperLimit(int nodeIdx, double referenceValue, double confidenceLevel) {
//        this.nodeIdx = nodeIdx;
//        this.referenceValue = referenceValue;
//        this.confidenceLevel = confidenceLevel;
//        // 부등호 방향 결정 $P(T<t) < epsilon$ 인지 $P(T<t) > epsilon$ 인지를 결정. 전자가 bGreater=true임
//        this.bUpper = true;
//    }
//
//    /**
//     * @param nodeIdx         node이름
//     * @param referenceValue
//     * @param confidenceLevel 확률값.
//     */
//    public void setLowerLimit(int nodeIdx, double referenceValue, double confidenceLevel) {
//        this.nodeIdx = nodeIdx;
//        this.referenceValue = referenceValue;
//        this.confidenceLevel = confidenceLevel;
//        // 부등호 방향 결정 $P(T<t) < epsilon$ 인지 $P(T<t) > epsilon$ 인지를 결정. 전자가 bGreater=true임
//        this.bUpper = false;
//    }

    //    public void setUpperLimit(String nodeName, double referenceValue, double confidenceLevel) {
//        this.nodeIdx = NodeDictionary.getInstance().nodeIdx(nodeName);
//        this.referenceValue = referenceValue;
//        this.confidenceLevel = confidenceLevel;
//        this.bUpper = true;
//    }
//
//    public void setLowerLimit(String nodeName, double referenceValue, double confidenceLevel) {
//
//        this.nodeIdx = NodeDictionary.getInstance().nodeIdx(nodeName);
//        logger.debug(nodeName + "=" + nodeIdx);
//        this.referenceValue = referenceValue;
//        this.confidenceLevel = confidenceLevel;
//        this.bUpper = false;
//    }
    public double inequalityValue() {

//        logger.debug(NodeDictionary.getInstance());
        double culminativeProbability = 0.5 * (1 + Erf.erf((referenceValue - mean) / Math.sqrt(2 * variance)));
        double value = 0.0;

        logger.debug(culminativeProbability);
        logger.debug(confidenceLevel);
        if (limitType == LimitType.Upper) {
            // $P(T<t) >= epsilon$ 인 경우
            // $P(T<t) - epsilon >=  0 $ 으로 치환됨
//            value =  1- culminativeProbability - confidenceLevel;
            value = (culminativeProbability - confidenceLevel) / (1 - confidenceLevel);
        } else {
            // $P(T>t) >= epsilon$ 인 경우
            // $ 1 -P(T<t) >= epsilon$ 으로 치환된다음에
            value = (1 - confidenceLevel - culminativeProbability) / (1 - confidenceLevel);


        }

        return value;
    }

    public static void timeWindowTest() {

    }
}
