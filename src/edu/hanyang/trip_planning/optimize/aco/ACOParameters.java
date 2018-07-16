package edu.hanyang.trip_planning.optimize.aco;

public class ACOParameters {
    private double alpha = 1.0;           // 휴리스틱 가중
    private double beta = 0.5;            // 페르몬 가중
    private double evaporation = 0.1;     // 휘발정도
    private int numberOfIteration = 1000;  // 개미 수

    public ACOParameters() {
    }

    public ACOParameters(double alpha, double beta, double evaporation, int numberOfIteration){
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.numberOfIteration = numberOfIteration;
    }

    public double getAlpha() {
        return alpha;
    }
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    public double getBeta() {
        return beta;
    }
    public void setBeta(double beta) {
        this.beta = beta;
    }
    public double getEvaporation() {
        return evaporation;
    }
    public void setEvaporation(double evaporation) {
        this.evaporation = evaporation;
    }
    public void setNumberOfIteration(int numberOfIteration){
        this.numberOfIteration = numberOfIteration;
    }
    public int getNumberOfIteration(){
        return numberOfIteration;
    }
}
