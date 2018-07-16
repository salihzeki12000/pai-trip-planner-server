package edu.hanyang.trip_planning.tripHTBN.potential.domain_specific;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.log4j.Logger;
import util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wykwon on 2015-12-07.
 */
public class Temperature {

    private static Temperature instance = new Temperature();
    private static Logger logger = Logger.getLogger(Temperature.class);
    private Map<Integer, Pair<Double, Double>> temperatureMap = new HashMap<Integer, Pair<Double, Double>>();

    private Temperature() {
    }


    public static Temperature getInstance() {
        if (instance == null) {
            instance = new Temperature();
        }
        return instance;
    }

    public static int condition(double temp)
    {
        if (temp > 30) {
            return 4;
        } else if (temp > 24) {
            return 3;
        } else if (temp > 15) {
            return 2;
        } else if (temp > 5) {
            return 1;
        } else {
            return 0;
        }
    }
    private void readCSV(String filename, char separator) throws IOException {
//logger.debug("읽어");
        CSVReader csvReader = new CSVReader(new FileReader(filename), separator);
        List<String[]> strArrayList = csvReader.readAll();
        for (String[] strArray : strArrayList) {
            if (strArray.length == 0) {
                continue;
            } else if (strArray[0].charAt(0) == '#') {
                continue;
            }
            int month = Integer.parseInt(strArray[0].trim());
            int day = Integer.parseInt(strArray[1].trim());
            double temp = Double.parseDouble(strArray[2].trim());
            double temp_sd = Double.parseDouble(strArray[3].trim());
            int key = month * 100 + day;
            temperatureMap.put(key, new Pair<Double, Double>(temp, temp_sd));
            System.out.println(Arrays.toString(strArray));
        }
    }

    public Pair<Double, Double> getTemperature(int monthOfYear, int dayOfMonth) {
        int key = monthOfYear * 100 + dayOfMonth;
        return temperatureMap.get(key);
    }

    public int getTemperatureCondition(int monthOfYear, int dayOfMonth) {
/**
 * 온도
 hot
 4
 warm
 3
 mild
 2
 cold
 1
 freezing
 0
 */
        int key = monthOfYear * 100 + dayOfMonth;
        Pair<Double, Double> pair = temperatureMap.get(key);
        double temp = pair.first();

        if (temp > 30) {
            return 4;
        } else if (temp > 24) {
            return 3;
        } else if (temp > 15) {
            return 2;
        } else if (temp > 5) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        logger.debug(Temperature.getInstance().getTemperature(12, 15));
    }

}
