package util;

import java.util.Random;

/**
 * The roulette wheel selection algorithm is a commonly known appreciated
 * technique for selection.
 */
public class WeightedRandomSelector {
    Random random = new Random();

    public int select(double[] weight) {
        // calculate the total weight
        double weight_sum = 0;
        for (int i = 0; i < weight.length; i++) {
            weight_sum += weight[i];
        }
        // get a random value
        double value = randUniformPositive() * weight_sum;
        // locate the random value based on the weights
        for (int i = 0; i < weight.length; i++) {
            value -= weight[i];
            if (value <= 0) return i;
        }
        // when rounding errors occur, we return the last item's index
        return weight.length - 1;
    }

    private double randUniformPositive() {
        // easiest implementation
        return random.nextDouble();
    }
}
