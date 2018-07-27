package edu.hanyang.trip_planning.tripHTBN.potential.satisfaction;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * Created by wykwon on 2015-10-20.
 */
public class WeatherSuitabilityCPT {

    private static Logger logger = Logger.getLogger(WeatherSuitabilityCPT.class);
    private int theCardinality;
    private int rainCardinality;
    private int tempHumiCardinality;
    private double potential[][][];


    public WeatherSuitabilityCPT() {
        this.rainCardinality = 3;
        this.tempHumiCardinality = 5;
        this.theCardinality = 2;
        potential = new double[rainCardinality][tempHumiCardinality][theCardinality];
    }


    public WeatherSuitabilityCPT(int rainCardinality, int tempHumiCardinality, int theCardinality) {
        this.rainCardinality = rainCardinality;
        this.tempHumiCardinality = tempHumiCardinality;
        potential = new double[rainCardinality][tempHumiCardinality][theCardinality];
    }


    public void setProbability(int rain, int temperature, int suitability, double probability) {
        potential[rain][temperature][suitability] = probability;
    }

    public double[] getProbability(int rain, int temperature) {
        return potential[rain][temperature];
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        for (int i = rainCardinality - 1; i >= 0; i--) {
            for (int j = tempHumiCardinality - 1; j >= 0; j--) {
                strbuf.append("rain=" + i + " temp=" + j + "==" + Arrays.toString(potential[i][j]) + "\n");
            }
        }
        return strbuf.toString();
    }
}
