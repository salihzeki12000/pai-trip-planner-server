package tripPlanning.tripHTBN.potential.domain_specific;

import au.com.bytecode.opencsv.CSVReader;
import javolution.util.FastMap;
import util.Erf;
import util.MyArrays;
import util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by wykwon on 2015-10-20.
 * <p/>
 * 날짜별 일기 예보를 획득한다.
 */
public class WeatherProbability {

    private static WeatherProbability instance = new WeatherProbability();
    private Map<Integer, Map<Integer, WeatherEntry>> weatherProbMap = new FastMap<>();
    private Map<Integer, List<Pair<Integer, double[]>>> probTableMap = new FastMap<>();

    private WeatherProbability() {
    }

    public static WeatherProbability getInstance() {
        return instance;
    }

    public Map<Integer, WeatherEntry> readCSV(String filename) {
        Map<Integer, WeatherEntry> dailyProbMap = new FastMap<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(filename), '\t');
            List<String[]> strArrayList = csvReader.readAll();
            for (String[] strArray : strArrayList) {
                if (strArray.length == 0) {
                    continue;
                } else if (strArray[0].charAt(0) == '#') {
                    continue;
                }

                int hour = Integer.parseInt(strArray[0]);
                double rainProb = Double.parseDouble(strArray[1]);
                double amountOfRain = Double.parseDouble(strArray[2]);
                double amountOfRainSD = Double.parseDouble(strArray[3]);
                double temperature = Double.parseDouble((strArray[4]));
                double prob[] = convertRainInfo(rainProb, amountOfRain, amountOfRainSD);
                WeatherEntry weatherEntry = new WeatherEntry(prob, temperature);
                dailyProbMap.put(hour, weatherEntry);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dailyProbMap;
    }

    private double[] convertRainInfo(double rainProb, double amountOfRain, double amountSD) {
//        logger.debug(amountOfRain);
        /**
         *
         * 비 조금 5mm 미만
         *
         * 비 많음 강수량 20mm 이상
         *
         *
         * 기준
         *  0 ~ 1 mm : 비 안온다
         *  1 ~ 20 mm :조금
         *  20 mm ~ 이상 : 많음
         *
         */

//        if (amountOfRain < 1.00) {
//            logger.debug("비 거의 안옴");
//        } else if (amountOfRain < 20.00) {
//            logger.debug("비 약하게 옴 ");
//        } else {
//            logger.debug("비 강하게 옴");
//        }
        double variance = amountSD * amountSD;
        double noReference = 1;
        double lowReference = 20;

        double noProbability = 0.5 * (1 + Erf.erf((noReference - amountOfRain) / Math.sqrt(2 * variance)));
        double lowProbability = 0.5 * (1 + Erf.erf((lowReference - amountOfRain) / Math.sqrt(2 * variance))) - noProbability;
        double heavyProbability = 1 - noProbability - lowProbability;
//        logger.debug(noProbability);
//        logger.debug(lowProbability);
//        logger.debug(heavyProbability);

        double ret[] = new double[3];
        ret[0] = noProbability;
        ret[1] = lowProbability * rainProb;
        ret[2] = heavyProbability * rainProb;
        MyArrays.normalize(ret);
//        logger.debug(Arrays.toString(ret));
        return ret;
    }

//    public List<Pair<Double, double[]>> readCSV(String filename) {
//        List<Pair<Double, double[]>> probTable = new ArrayList<Pair<Double, double[]>>();
//        CSVReader csvReader = null;
//        try {
//            csvReader = new CSVReader(new FileReader(filename), '\t');
//            //        String valueNames[] = Arrays.copyOfRange(header, 1, header.length);
////        logger.debug(Arrays.toString(header));
////        logger.debug(Arrays.toString(valueNames));
//            List<String[]> strArrayList = csvReader.readAll();
//            for (String[] strArray : strArrayList) {
//                if (strArray.length == 0) {
//                    continue;
//                } else if (strArray[0].charAt(0) == '#') {
//                    continue;
//                }
//
//                double time = Double.parseDouble(strArray[0]);
//                // binary random variable
//                double probs[] = new double[2];
//                probs[0] = Double.parseDouble(strArray[1]);
//                probs[1] = 1 - probs[0];
//
//                probTable.add(new Pair<Double, double[]>(time, probs));
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        logger.debug(probTable);
//        return probTable;
//    }

//    public double[] getWeatherEntry(int year, int month, int day, double hour) {
//        int key = year * 10000 + month * 100 + day;
//        List<Pair<Double, double[]>> probTable = probTableMap.get(key);
//        if (probTable == null) {
//            probTable = readCSV("datafiles/weather/" + year + "-" + month + "-" + day + ".csv");
//            probTableMap.put(key, probTable);
//        }
//
//        logger.debug(toString(probTable));
//
//
////        WeatherProbability={Heavy, Light, No}
//        return null;
//    }

    public WeatherEntry getWeatherEntry(int year, int month, int day, int hour) {
        int key = year * 10000 + month * 100 + day;
        Map<Integer, WeatherEntry> dailyProbMap = weatherProbMap.get(key);
        if (dailyProbMap == null) {
            String fileName = "datafiles/weather/" + year + "-" + month + "-" + day + ".csv";
            if (!checkFile(fileName)) {
                fileName = "datafiles/weather/" + year + "-" + month + ".csv";
                if (!checkFile(fileName)) {
                    fileName = "datafiles/weather/" + year + ".csv";
                }
            }

            if (dailyProbMap == null) {
                dailyProbMap = readCSV(fileName);
                weatherProbMap.put(key, dailyProbMap);
            }
        }

        return dailyProbMap.get(hour);
    }

    private double[] getTableEntry(List<Pair<Double, double[]>> probTable, double refTime) {
        for (int i = 0; i < probTable.size(); i++) {
            Pair<Double, double[]> entry = probTable.get(i);
            if (refTime <= entry.first()) {
                return probTable.get(i).second();
            }
        }
        throw new RuntimeException("Error");
    }

    public String toString(List<Pair<Double, double[]>> probTable) {
        StringBuffer strbuf = new StringBuffer();
        for (double t = 0; t < 23.9; t = t + 1) {
            strbuf.append(t + "\t" + Arrays.toString(getTableEntry(probTable, t)) + "\n");
        }
        return strbuf.toString();
    }

    private boolean checkFile(String filename) {
        File f = new File(filename);
        return f.isFile();
    }
}
