package sneakthrough.ML.RNN;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class LSTMModelClient {
    private static final String SERVER_URL = "http://localhost:5000/evaluate";

    public ModelPrediction getPrediction(String boardState) {
        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);


            String jsonInputString = "{\"board_state\": \"" + boardState + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            //handle the response
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
        double winProbability = jsonObject.getDouble("winProbability");
        double lossProbability = jsonObject.getDouble("lossProbability");

        return new ModelPrediction(winProbability, lossProbability);
    }
    
}
