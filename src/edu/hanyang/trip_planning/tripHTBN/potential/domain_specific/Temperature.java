package edu.hanyang.trip_planning.tripHTBN.potential.domain_specific;

public class Temperature {
    private static Temperature instance = new Temperature();

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
}
