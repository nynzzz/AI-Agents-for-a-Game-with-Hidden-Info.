package sneakthrough.Experiments;

import java.io.FileWriter;

import com.opencsv.CSVWriter;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

public class RandomExp {

    public static void main(String args[]) throws Exception {

        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/random.csv"));

        int numOfRuns = 1000000;

        Player white = new RandomPlayer("white");
        Player black = new RandomPlayer("black");


        for (int i = 0; i < numOfRuns; i++) {
            String iter = String.valueOf(i);

            Board board = new Board();
            Game game = new Game(board, white, black);
            String winner = game.startGameExperiments();

            String[] text = {iter, winner};
//            System.out.println(text[0] + text[1]);
            writer.writeNext(text);

        }
        writer.flush();
    }
}
