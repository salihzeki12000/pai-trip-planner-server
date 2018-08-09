package edu.hanyang.trip_planning.optimize.aco;

import util.Erf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dummyProblem on 2016-10-31.
 */
public abstract class ItineraryPlanning implements ACOProblem {

    protected int numNodes = 0;
    protected int startNodeIdx = 0;
    protected int endNodeIdx = 0;
    protected List<Integer> trail = new ArrayList<>();
    protected Set<Integer> avaliableNodes = new HashSet<>();
    protected int curNodeIdx;
    protected boolean bTerminalCondition = false;


    public ItineraryPlanning(int startNodeIdx, int endNodeIdx, int numNodes) {
        this.startNodeIdx = startNodeIdx;
        this.endNodeIdx = endNodeIdx;
        this.numNodes = numNodes;
        init();
    }

    /**
     * numNode를 나중에 초기화할때 사용됨
     *
     * @param startNodeIdx
     * @param endNodeIdx
     */
    public ItineraryPlanning(int startNodeIdx, int endNodeIdx) {
        this.startNodeIdx = startNodeIdx;
        this.endNodeIdx = endNodeIdx;
        init();
    }

    public void init() {
        this.trail.clear();
        this.avaliableNodes.clear();
        curNodeIdx = startNodeIdx;
        for (int i = 0; i < numNodes; i++) {
            if (i != startNodeIdx && i != endNodeIdx) {
                avaliableNodes.add(i);
            }
        }
        bTerminalCondition = false;
        problemDependentInit();
    }

    protected abstract void problemDependentInit();

    @Override
    public int getCurNode() {
        return curNodeIdx;
    }

    @Override
    public int[] getPath() {
        return Erf.MyArrays.toIntArray(trail);
    }

    @Override
    public int getNumNodes() {
        return numNodes;
    }


    @Override
    public abstract double getTotalValue();


}
