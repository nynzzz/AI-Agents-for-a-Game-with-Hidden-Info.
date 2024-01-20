package sneakthrough.ML.AlphaZero;

public class AlphaZeroTest {
    public static void main(String[] args) {
        AlphaZeroModelClient client = new AlphaZeroModelClient();

        // Example board state and possible moves
        String boardState = "bbbbbb000bb0bbbb0w00000b0000b000000000b0000wwww0w000w0wwwwwwwww0";
        String possibleMoves = "2,1:1,0;2,1:1,2;2,1:1,1;5,3:4,2;5,3:4,4;5,3:4,3;5,4:4,3;5,4:4,5;5,4:4,4;5,5:4,4;5,5:4,6;5,5:4,5;5,6:4,5;5,6:4,7;5,6:4,6;6,0:5,1;6,0:5,0;6,6:5,7;6,7:5,7;7,0:6,1;7,1:6,2;7,1:6,1;7,2:6,1;7,2:6,3;7,2:6,2;7,3:6,2;7,3:6,3;7,4:6,3;7,4:6,5;7,5:6,5;7,6:6,5;";

        // Get the model prediction
        ModelPrediction prediction = client.getPrediction(boardState, possibleMoves);

        // Output the results
        if (prediction != null) {
            System.out.println("Value: " + prediction.getValue());
            System.out.println("Policy Map:");
            for (String move : prediction.getPolicyMap().keySet()) {
                double policyValue = prediction.getPolicyMap().get(move);
                System.out.println("Move: " + move + ", Policy Value: " + policyValue);
            }
        } else {
            System.out.println("Prediction failed.");
        }
    }
}
