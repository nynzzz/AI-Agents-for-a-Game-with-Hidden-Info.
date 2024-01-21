package sneakthrough.ML.RNN;


public class LSTMTest {
    public static void main(String[] args) {
        LSTMModelClient client = new LSTMModelClient(); 

        // Example board state
        //String boardState = "bbbbbb000bb0bbbb0w00000b0000b000000000b0000wwww0w000w0wwwwwwwww0";
        String boardState = "bbbbbbbbbbbbbbbb00000000000000000000000000000000wwwwwwwwwwwwwwww";
        // Get the model prediction
        ModelPrediction boardEvaluation = client.getPrediction(boardState);

        // Output the results
        if (boardEvaluation != null) {
            System.out.println("Board Evaluation: " + boardEvaluation);
        } else {
            System.out.println("Prediction failed.");
        }
    }
}
