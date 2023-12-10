package sneakthrough.Experiments;

import com.opencsv.CSVWriter;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.MiniMaxPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

import java.io.FileWriter;

public class MiniMaxExperiments {

    public static void main(String args[]) throws Exception {

        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/minimax.csv"));

        int numOfRuns = 1500;
        float winswhite = 0 ;
        float winsblack = 0 ;

        Player white = new MiniMaxPlayer("white");
        Player black = new RandomPlayer("black");


        for (int i = 0; i < numOfRuns; i++) {
            String iter = String.valueOf(i);

            Board board = new Board();
            Game game = new Game(board, white, black);
            String winner = game.startGameExperiments();

            String[] text = {iter, winner};
            if(text[1].equals("white")) winswhite++;
            else winsblack++;
            writer.writeNext(text);

        }

        writer.writeNext(new String[]{"Win Ratio for MiniMax in games against Random: " + (winswhite / numOfRuns)*100});
        writer.flush();
    }
}
