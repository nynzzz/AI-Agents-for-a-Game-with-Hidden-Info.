package sneakthrough.Experiments;

import java.io.FileWriter;

import com.opencsv.CSVWriter;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.ISMCTSPlayer;
import sneakthrough.Player.LSTMnISMCTSPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

public class LSTM {
    public static void main(String args[]) throws Exception {
        int expe = 100;
        int lstmWins = 0;
        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/minimax.csv")); 

        LSTMnISMCTSPlayer white = new LSTMnISMCTSPlayer("white", 1000, 0.7);
        ISMCTSPlayer black = new ISMCTSPlayer("black", 1000, 0.7);

        for (int i = 0; i < expe; i++) {
            String iter = String.valueOf(i);
            Board board = new Board();
            Game game = new Game(board, white, black);
            String winner = game.startGameExperiments();
            
            String[] text = {iter, winner};
            writer.writeNext(text);

            if(winner.equals("white")){
                lstmWins++;
            }
        }
        double lstmWinRatio = (double) lstmWins / expe * 100;
        System.out.println("Win ratio: " + lstmWinRatio);
        writer.flush();
    }
}
