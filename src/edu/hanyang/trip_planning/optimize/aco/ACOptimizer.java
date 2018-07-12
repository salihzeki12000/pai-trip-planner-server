package edu.hanyang.trip_planning.optimize.aco;

import edu.hanyang.trip_planning.optimize.aco.dummyProblem.AdvancedDummyOrienteeringProblem;
import org.apache.log4j.Logger;
import org.math.array.DoubleArray;
import wykwon.common.MyFunction;
import wykwon.common.array.MyArrays;
import wykwon.common.selection.RouletteWheelSelection;

import java.util.*;

/**
 * Created by dummyProblem on 2016-10-28.
 */
public class ACOptimizer {
    private static Logger logger = Logger.getLogger(ACOptimizer.class);
    private RouletteWheelSelection selection = new RouletteWheelSelection();
    private ACOProblem agent;
    private ACOParameters acoParameters;
    private int nodeSize;
    private double pheromone[][];

    public ACOptimizer(ACOProblem acoProblem, ACOParameters acoParameters) {
        this.agent = acoProblem;
        this.acoParameters = acoParameters;
        this.nodeSize = acoProblem.getNumNodes();
        pheromone = DoubleArray.fill(nodeSize, nodeSize, 0.5);
    }

    List<ScoredPath> results = new ArrayList<>();

    //top K의 solution을 반환
    public ScoredPath[] optimize(int topK) {
        TopKScoredPaths topKScoredPaths = new TopKScoredPaths(topK);
        for (int i = 0; i < acoParameters.getNumberOfIteration(); i++) {
            ScoredPath solutions = generateSolutions();
//            logger.debug(solutions.getValue());
            analyzeSolution(solutions);
            topKScoredPaths.add(solutions);
        }
        return topKScoredPaths.getPaths();
    }

    public void analyzeSolution(ScoredPath solution) {
        int path[] = solution.getPath();
        // decrease all pheromone
        for (int i = 0; i < nodeSize; i++) {
            for (int j = 0; j < nodeSize; j++) {
                pheromone[i][j] = pheromone[i][j] * (1 - acoParameters.getEvaporation());
            }
        }
        // update pheromone
        double value = agent.getTotalValue();
        int curNode = path[0];
        int nextNode = -1;
        for (int i = 1; i < path.length; i++) {
            nextNode = path[i];
            pheromone[curNode][nextNode] += value;
            pheromone[curNode][nextNode] = MyFunction.clamp(pheromone[curNode][nextNode], 0, 1);
            curNode = nextNode;
        }
    }

    private ScoredPath generateSolutions() {
        agent.init();
        for (int t = 0; t < agent.getNumNodes(); t++) {
            double eta[] = agent.heuristicValue();
            double tau[] = pheromone[agent.getCurNode()];
            double p[] = getProbabilityOfChoosing(eta, tau);
            if (agent.isTerminalCondition()) {
                break;
            }
            int selectedNode = selection.select(p);
            agent.addNodeToTrail(selectedNode);
        }
        return new ScoredPath(agent.getPath(), agent.getTotalValue());
    }

    private double[] getProbabilityOfChoosing(double eta[], double tau[]) {
        double p[] = new double[eta.length];
        for (int i = 0; i < eta.length; i++) {
            p[i] = Math.pow(eta[i], acoParameters.getAlpha()) * Math.pow(tau[i], acoParameters.getBeta());
        }
        double sum = MyArrays.sum(p);
        for (int i = 0; i < eta.length; i++) {
            p[i] = p[i] / sum;
        }
        return p;
    }

    public static void main(String[] args) {
        AdvancedDummyOrienteeringProblem op = new AdvancedDummyOrienteeringProblem(0, 1);
        ACOParameters acoParameters = ACOParameterFactory.simpleParamGen();
        ACOptimizer ants = new ACOptimizer(op, acoParameters);

        ScoredPath solutions[] = ants.optimize(10);

        for (ScoredPath p : solutions) {
//            logger.debug(p);
            logger.debug(op.result(p.getPath()).toDetailString());
        }
    }
}
