package edu.hanyang.trip_planning.tripHTBN.potential.domain_specific;


import java.util.Arrays;

public class WeatherEntry {

    public double rainProbability[];
    public double temperature;

    public WeatherEntry(double rainProbability[], double temperature) {
        this.rainProbability = rainProbability.clone();
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "WeatherEntry{" +
                "rainProbability=" + Arrays.toString(rainProbability) +
                ", temperature=" + temperature +
                '}';
    }
}
