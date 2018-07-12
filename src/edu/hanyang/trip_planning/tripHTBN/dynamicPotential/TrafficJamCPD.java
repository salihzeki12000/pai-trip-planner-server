package edu.hanyang.trip_planning.tripHTBN.dynamicPotential;

import au.com.bytecode.opencsv.CSVReader;
import cntbn.common.NodeDictionary;
import edu.hanyang.trip_planning.tripHTBN.poi.SubsetPOIs;
import edu.hanyang.trip_planning.tripHTBN.potential.InterfaceSoftmaxCPD;
import org.apache.log4j.Logger;
import wykwon.common.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wykwon on 2015-10-08.
 */
public class TrafficJamCPD implements InterfaceSoftmaxCPD {
    private static Logger logger = Logger.getLogger(TrafficJamCPD.class);
    private String nodename;
    private SubsetPOIs subsetPOIs;
    private List<Pair<Double, double[]>> timeTable = new ArrayList<Pair<Double, double[]>>();

    public TrafficJamCPD(String nodename, SubsetPOIs subsetPOIs) {
        this.nodename = nodename;

        this.subsetPOIs = subsetPOIs;
        try {
            readFile("datafiles/movements/jeju_traffic_jam.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void readFile(String filename) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
        String header[] = csvReader.readNext();
        String valueNames[] = Arrays.copyOfRange(header, 1, header.length);
        NodeDictionary.getInstance().putValues(nodename, valueNames);
//        logger.debug(Arrays.toString(header));
//        logger.debug(Arrays.toString(valueNames));
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }

            double time = Double.parseDouble(strArray[0]);
            double probs[] = new double[valueNames.length];

            for (int i = 0; i < valueNames.length; i++) {
                probs[i] = Double.parseDouble(strArray[i + 1]);
            }

            timeTable.add(new Pair<Double, double[]>(time, probs));
        }
//        logger.debug(timeTable);


    }


    @Override
    public double[] getDistribution(int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        return getTableEntry(continuousParentValue);
    }

    @Override
    public double getProbability(int theValue, int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        return getTableEntry(continuousParentValue)[theValue];

    }

    @Override
    public double[] getDistributionFromSuperset(int[] discreteParentIndices, int[] discreteParentValues, double continuousParentValue) {
        return getTableEntry(continuousParentValue);
    }

    private double[] getTableEntry(double refTime) {
        for (int i = 0; i < timeTable.size(); i++) {
            Pair<Double, double[]> entry = timeTable.get(i);
            if (refTime <= entry.first()) {
//                logger.debug("refTIme=" +refTime + "\t timetable=" +entry.first());
                return timeTable.get(i).second();
            }
        }
        throw new RuntimeException("Error");
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (double t = 0; t < 23.9; t=t+1) {

            strbuf.append(t + "\t" + Arrays.toString(getTableEntry(t)) + "\n");
        }
        return strbuf.toString();
    }



    public static void main(String[] args) {
//        test();

    }
}
