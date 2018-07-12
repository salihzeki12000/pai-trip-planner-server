package edu.hanyang.trip_planning.optimize.aco.dummyProblem;

import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wykwon on 2016-10-31.
 */
public class RandomGenerateOP {
    private int numNode;
    private static Logger logger = Logger.getLogger(RandomGenerateOP.class);
    List<double[]> nodeList;

    public RandomGenerateOP(int numNode) {
        this.numNode = numNode;
    }

    public void generate() {
        nodeList = new ArrayList<>();
        for (int i = 0; i < numNode; i++) {
            nodeList.add(randomNode());
        }
    }

    public void writeFile(String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            DecimalFormat df = new DecimalFormat("###.###");
            for (double node[]: nodeList){
                String s= df.format(node[0])+ ","+df.format(node[1])+ ","+df.format(node[2])+","+df.format(node[3])+"\n";
                fw.write(s);
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * node의 x,y좌표 만들것
     * 30km x 30km 반경에서 만들면 될듯
     *
     * @return double array[3]
     * 0: x position [0,30]
     * 1: y position [0,30]
     * 2: satisfication [0,1]
     * 3: spending hour [
     */
    public double[] randomNode() {
        double pos[] = new double[4];
        pos[0] = 30 * Math.random();
        pos[1] = 30 * Math.random();
        pos[2] = Math.random();
        pos[3] = 2*Math.random();
        return pos;
    }

    public static void main(String[] args) {
        RandomGenerateOP rop = new RandomGenerateOP(50);
        rop.generate();
        rop.writeFile("datafile/nodes.csv");
    }

}
