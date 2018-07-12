package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import org.apache.commons.collections15.Transformer;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 14
 * Time: 오후 3:28
 * To change this template use File | Settings | File Templates.
 */
public class NodePaintTransformer implements Transformer<String, Paint> {
    @Override
    public Paint transform(String s) {
        return Color.GREEN;
    }
}
