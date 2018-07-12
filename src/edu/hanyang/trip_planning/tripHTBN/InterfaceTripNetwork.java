package edu.hanyang.trip_planning.tripHTBN;

import cntbn.terms_factors.SimpleGaussian;

/**
 * Created by wykwon on 2015-09-22.
 */
public interface InterfaceTripNetwork {

    int nodeSize();

    void inferenceItinerary(double startHour, int... path);

    SimpleGaussian getContinuousProbability(int nodeIdx);

    SimpleGaussian getContinuousProbability(String nodeCategoryStr, int order);


}
