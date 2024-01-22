package sneakthrough.ML.RNN;

public class ModelPrediction {
    private double winProbability;
    private double lossProbability;

    public ModelPrediction(double winProbability, double lossProbability) {
        this.winProbability = winProbability;
        this.lossProbability = lossProbability;
    }

    public double getWinProbability() {
        return winProbability;
    }

    public double getLossProbability() {
        return lossProbability;
    }
    @Override
    public String toString() {
        return "ModelPrediction{" +
                "winProbability=" + winProbability +
                ", lossProbability=" + lossProbability +
                '}';
    }
}
