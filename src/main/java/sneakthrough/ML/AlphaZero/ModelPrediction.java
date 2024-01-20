package sneakthrough.ML.AlphaZero;

import java.util.HashMap;

public class ModelPrediction {
    private HashMap<String, Double> policyMap;
    private double value;

    public ModelPrediction(HashMap<String, Double> policyMap, double value) {
        this.policyMap = policyMap;
        this.value = value;
    }

    public HashMap<String, Double> getPolicyMap() {
        return policyMap;
    }

    public double getValue() {
        return value;
    }
}
