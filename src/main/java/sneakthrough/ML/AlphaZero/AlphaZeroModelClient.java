package sneakthrough.ML.AlphaZero;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import
        org.json.JSONObject;
import java.util.HashMap;

public class AlphaZeroModelClient {

    private static final String SERVER_URL = "http://localhost:5001/predict";

    public ModelPrediction getPrediction(String boardState, String possibleMoves) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = "{\"board_state\": \"" + boardState + "\", \"possible_moves\": \"" + possibleMoves + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                return parseResponse(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ModelPrediction parseResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject policy = jsonObject.getJSONObject("policy");
        double value = jsonObject.getDouble("value");

        HashMap<String, Double> policyMap = new HashMap<>();
        for (String key : policy.keySet()) {
            double policyValue = policy.getDouble(key);
            policyMap.put(key, policyValue);
        }

        return new ModelPrediction(policyMap, value);
    }
}
