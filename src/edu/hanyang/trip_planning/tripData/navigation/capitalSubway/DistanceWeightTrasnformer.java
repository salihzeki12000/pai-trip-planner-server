package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import org.apache.commons.collections15.Transformer;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 14
 * Time: 오후 3:35
 * To change this template use File | Settings | File Templates.
 */
public class DistanceWeightTrasnformer implements Transformer<MovementEdge, Double> {
    @Override
    public Double transform(MovementEdge movementEdge) {
        return movementEdge.distance;
    }
}
