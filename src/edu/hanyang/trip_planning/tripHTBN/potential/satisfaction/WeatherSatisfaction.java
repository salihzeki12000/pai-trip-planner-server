package edu.hanyang.trip_planning.tripHTBN.potential.satisfaction;

import cntbn.common.NodeDictionary;
import cntbn.terms_factors.SimpleGaussian;
import edu.hanyang.trip_planning.tripHTBN.potential.BasicHybridCPD;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceHybridCPD;
import org.apache.log4j.Logger;

/**
 * Created by wykwon on 2015-10-20.
 */
public class WeatherSatisfaction {

    private static Logger logger = Logger.getLogger(WeatherSatisfaction.class);

    public static InterfaceHybridCPD makeCPD(int order) {

        // 1. Node Generation
        NodeDictionary nd = NodeDictionary.getInstance();
        String str_SW = "SW" + order;
        String str_W = "W" + order;
        String str_AT = "AT" + order;
        nd.putNode(str_SW);
        nd.putValues(str_W, "WeatherProbability", "Clear");
        nd.putValues(str_AT, "Indoor", "Outdoor");

//        logger.debug(nd);
        String discreteParents[] = {str_W, str_AT};
        BasicHybridCPD basicCPD = new BasicHybridCPD(str_SW, discreteParents);
//        logger.debug(basicCPD);
        String nodeString = str_W + "," + str_AT;
        basicCPD.setDistribution(nodeString, "Clear,Indoor", new SimpleGaussian(1.0, str_SW, 0.7, 0.1));
        basicCPD.setDistribution(nodeString, "WeatherProbability,Indoor", new SimpleGaussian(1.0, str_SW, 0.7, 0.1));
        basicCPD.setDistribution(nodeString, "Clear,Outdoor", new SimpleGaussian(1.0, str_SW, 1.0, 0.1));
        basicCPD.setDistribution(nodeString, "WeatherProbability,Outdoor", new SimpleGaussian(1.0, str_SW, 0.3, 0.1));
//        logger.debug(basicCPD);
        return basicCPD;
    }

    public static void main(String[] args) {
        logger.debug(makeCPD(1));
        // activiy는 POI를 넣으면 나온다. From ActivityUtils 에서
    }
}
