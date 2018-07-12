package edu.hanyang.trip_planning.tripData.navigation.capitalSubway;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 14
 * Time: 오후 2:30
 * To change this template use File | Settings | File Templates.
 */
public class GraphVisualizer {
    public static void draw(Graph<String, MovementEdge> graph) {
        // The Layout<V, E> is parameterized by the vertex and edge types
        Layout<String, MovementEdge> layout = new KKLayout<String, MovementEdge>(graph);
        layout.setSize(new Dimension(800, 600)); // sets the initial size of the layout space
        // The BasicVisualizationServer<V,E> is parameterized by the vertex and edge types
        VisualizationViewer<String, MovementEdge> vv = new VisualizationViewer<String, MovementEdge>(layout);
        vv.setPreferredSize(new Dimension(800, 600)); //Sets the viewing area size


        vv.getRenderContext().setVertexFillPaintTransformer(new NodePaintTransformer());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm);

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        CapitalSubwayGraph movementGraph = new CapitalSubwayGraph();
        movementGraph.loadFromFiles("datafiles/movements/capital_subway.csv");
        GraphVisualizer.draw(movementGraph.getGraph());

    }
}
