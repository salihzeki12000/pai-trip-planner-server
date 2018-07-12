package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import au.com.bytecode.opencsv.CSVReader;

import edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI.DaumPoiIO;
import edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI.ItemConverter;
import edu.hanyang.trip_planning.tripData.mapAPI.DaumLocalAPI.ItemList;
import edu.hanyang.trip_planning.tripData.poi.BasicPOI;
import edu.hanyang.trip_planning.tripData.poi.InterfacePOI;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 6. 29
 * Time: 오후 4:18
 * To change this template use File | Settings | File Templates.
 */
public class SubwayList {
    private static Logger logger = Logger.getLogger(SubwayList.class);

    public static void subway2nd() {
        DaumPoiIO daumPOIWriter = new DaumPoiIO("datafiles/pois/daum/daum_pois.json");
        ItemList itemList = daumPOIWriter.readItemSet("datafiles/pois/daum/daum_pois.json");
        Set<BasicPOI> poiSet = ItemConverter.getPOISet(itemList);

//        Set<Item> itemSet = itemList.getItemList();
        int i = 0;
        for (InterfacePOI poi : poiSet) {
            if (poi.getPoiType().category.equals("교통,수송")) {
                if (poi.getPoiType().subSubCategory != null) {
                    if (poi.getPoiType().subSubCategory.equals("수도권지하철경의중앙선")) {
                        System.out.println(poi.getTitle() + '\t' + poi.getPoiType());
                        i++;
                    }
                }
            }
        }
    }


    public static void subwayGraphTest() throws IOException {
        Graph<String, String> g = new UndirectedSparseGraph<String, String>();

        CSVReader csvReader = new CSVReader(new FileReader("datafiles/movements/capital_subway.csv"), '\t');
        List<String[]> rowStrList = csvReader.readAll();

        // node adding
        for (String[] strs : rowStrList) {
            if (strs[0].charAt(0) == '#') {
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

        // graph draw

        logger.debug(g);

        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<Integer, String> layout = new CircleLayout(g);
        layout.setSize(new Dimension(300, 300)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension(350, 350)); //Sets the viewing area size

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);


    }

    public static void main(String[] args) {
        subway2nd();
//        try {
//            subwayGraphTest();
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
    }
}
