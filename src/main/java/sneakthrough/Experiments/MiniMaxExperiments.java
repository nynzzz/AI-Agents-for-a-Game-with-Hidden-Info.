package sneakthrough.Experiments;

import com.opencsv.CSVWriter;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.ISMCTSPlayer;
import sneakthrough.Player.MiniMaxPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

import java.io.FileWriter;

public class MiniMaxExperiments {

    public static void main(String args[]) throws Exception {

        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/minimax.csv"));

        int numOfRuns = 1000;
        float winswhite = 0;

        //Player white = new MiniMaxPlayer("white");
        //Player black = new RandomPlayer("black");

        Player white = new ISMCTSPlayer("white",1000,0.7);
        Player black = new MiniMaxPlayer("black");

        // Start time measurement
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numOfRuns; i++) {
            String iter = String.valueOf(i);

            Board board = new Board();
            Game game = new Game(board, white, black);
            String winner = game.startGameExperiments();

            String[] text = {iter, winner};
            if(text[1].equals("white")) winswhite++;
            writer.writeNext(text);
        }

        // End time measurement
        long endTime = System.currentTimeMillis();

        // Calculate runtime in minutes
        long runtimeMillis = endTime - startTime;
        double runtimeMinutes = runtimeMillis / 60000.0;

        writer.writeNext(new String[]{"Win Ratio for MiniMax in games against Random: " + (winswhite / numOfRuns) * 100});
        writer.flush();

        // Print runtime in the terminal
        System.out.println("Total runtime: " + runtimeMinutes + " minutes");
    }
}
