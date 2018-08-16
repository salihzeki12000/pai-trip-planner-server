package tripPlanning.optimize.aco;

public class ACOParameters {
    private double alpha = 1.0;           // 휴리스틱 가중
    private double beta = 0.5;            // 페르몬 가중
    private double evaporation = 0.1;     // 휘발정도
    private int numberOfIteration = 1000;  // 개미 수

    public ACOParameters() {
    }

    public ACOParameters(double alpha, double beta, double evaporation, int numberOfIteration) {
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.numberOfIteration = numberOfIteration;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getEvaporation() {
        return evaporation;
    }

    public int getNumberOfIteration() {
        return numberOfIteration;
    }
}
