package edu.hanyang.trip_planning.optimize.aco;

import edu.hanyang.trip_planning.optimize.DetailItinerary;

/**
 * Created by dummyProblem on 2016-10-28.
 */
public interface ACOProblem {
    /**
     * initialize the problem and parameter
     */
    void init();

    /**
     * return number of nodes;
     * @return
     */
    int getNumNodes();

    /**
     * next
     *
     * @return
     */
    double[] heuristicValue();

    /**
     * add the selected node to trail
     *
     * @param nodeIdx
     */
    void addNodeToTrail(int nodeIdx);

    /**
     * return the currently selected node index
     *
     * @return
     */
    int getCurNode();



    /**
     * check the terminal condition
     *
     * @return
     */
    boolean isTerminalCondition();

    /**
     * return the utility value for full path.
     *
     * @return
     */
    double getTotalValue();

    /**
     * path를 반환함
     * 꼭 전체 path여야함.
     *
     * @return
     */
    int[] getPath();


    DetailItinerary result(int[] path);


}
