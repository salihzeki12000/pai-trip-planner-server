package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import au.com.bytecode.opencsv.CSVReader;

import edu.hanyang.trip_planning.tripData.dataType.ProbabilisticDuration;
import edu.hanyang.trip_planning.tripData.dataType.UnitMovement;
import edu.hanyang.trip_planning.tripData.dataType.WayOfMovement;
import edu.hanyang.trip_planning.tripData.navigation.MinimalPOI;
import edu.hanyang.trip_planning.tripData.poi.POIManager;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 14
 * Time: 오후 2:11
 * To change this template use File | Settings | File Templates.
 */
public class CapitalSubwayGraph {
    Graph<String, MovementEdge> graph = new UndirectedSparseGraph<String, MovementEdge>();
    private static Logger logger = Logger.getLogger(CapitalSubwayGraph.class);

    public void loadFromFiles(String filename) {
        List<String[]> rowStrList = null;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
            rowStrList = csvReader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        // node adding
        for (String[] strs : rowStrList) {
            if (strs[0].charAt(0) == '#') {
                continue;
            }

            graph.addVertex(strs[0]);
            graph.addVertex(strs[1]);
        }

        //edge adding
        for (String[] strs : rowStrList) {
//            logger.debug(Arrays.toString(strArray));
            if (strs[0].charAt(0) == '#') {
                continue;
            }
            double distance = Double.parseDouble(strs[2]);
            double time = Double.parseDouble(strs[3]);
            MovementEdge edge = new MovementEdge(strs[0], strs[1], distance, time);
            graph.addEdge(edge, strs[0], strs[1]);
        }
    }

    public Graph<String, MovementEdge> getGraph() {
        return graph;
    }

    public String toString() {
        return graph.toString();
    }

    public UnitMovement findPath(MinimalPOI srcPOI, MinimalPOI destPOI) {

        DijkstraShortestPath<String, MovementEdge> timePathAlg = new DijkstraShortestPath(graph,
                new TemporalWeightTrasnformer());
        DijkstraShortestPath<String, MovementEdge> distancePathAlg = new DijkstraShortestPath(graph,
                new DistanceWeightTrasnformer());
        List<MovementEdge> distList = distancePathAlg.getPath(srcPOI.getTitle(), destPOI.getTitle());
//        logger.debug(distList);


        double duration = timePathAlg.getDistance(srcPOI.getTitle(), destPOI.getTitle()).doubleValue();
        double distance = distancePathAlg.getDistance(srcPOI.getTitle(), destPOI.getTitle()).doubleValue();
        int cost;
//        for (MovementEdge edge : list) {
////            distance += edge.distance;
//            duration += edge.time;
//        }

//        logger.debug("time=" + duration + "\tdistance=" + distance + "km");
        if (distance <= 10) {
            cost = 1250;
        } else if (distance <= 15) {
            cost = 1350;
        } else if (distance <= 20) {
            cost = 1450;
        } else if (distance < 25) {
            cost = 1550;
        } else if (distance < 30) {
            cost = 1650;
        } else if (distance < 35) {
            cost = 1750;
        } else if (distance < 45) {
            cost = 1850;
        } else if (distance < 50) {
            cost = 1950;
        } else if (distance < 60) {
            cost = 2050;
        } else if (distance < 70) {
            cost = 2150;
        } else {
            cost = 2250;
        }


//        Number dist = alg.getDistance(src, dest);
//        System.out.println("The shortest path from" + src + " to " + dest + " is:");
//        System.out.println(list.toString());
//        System.out.println("and the length of the path is: " + dist);
        POIManager poiManager = POIManager.getInstance();


        ProbabilisticDuration dur = new ProbabilisticDuration(duration/60, 0.1);
                return new UnitMovement(srcPOI.getTitle(), srcPOI.getLocation(), destPOI.getTitle(), destPOI.getLocation(), WayOfMovement.Subway, dur, cost);

    }

    public static void dummy() {
        Graph<String, String> g = new UndirectedSparseGraph<String, String>();
        List<String[]> rowStrList = null;
        try {
            CSVReader csvReader = new CSVReader(new FileReader("datafiles/movements/capital_subway.csv"), '\t');
            rowStrList = csvReader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // node adding
        for (String[] strs : rowStrList) {
            if (strs.length != 4) {
                logger.debug("ignore: " + Arrays.toString(strs));
                continue;
            }
            if (strs[0].charAt(0) == '#') {
                logger.debug("주석: " + Arrays.toString(strs));
                continue;
            }

            g.addVertex(strs[0]);
            g.addVertex(strs[1]);
            logger.debug(g);
        }

        //edge adding
        for (String[] strArray : rowStrList) {
            logger.debug(Arrays.toString(strArray));
            if (strArray[0].charAt(0) == '#') {
                continue;
            }
            g.addEdge(strArray[0] + "-" + strArray[1], strArray[0], strArray[1]);
//            logger.debug(strArray.length);

//            logger.debug(strs2[1]);


        }
    }

    public static void main(String[] args) {
//        dummy();
        CapitalSubwayGraph movementGraph = new CapitalSubwayGraph();
        movementGraph.loadFromFiles("datafiles/movements/capital_subway.csv");

    }
}
