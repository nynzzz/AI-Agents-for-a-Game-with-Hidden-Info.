package sneakthrough.Experiments;

import com.opencsv.CSVWriter;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.ISMCTSPlayer;
import sneakthrough.Player.MiniMaxPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

import java.io.FileWriter;
import java.io.IOException;

public class ismctsExp {

    public static void main(String args[]) throws Exception {

        runExperiment();

//        CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/ismctsExp/ismcts1000VSminimax.csv"));
//
//        int numOfRuns = 1000;
//
////        // numOfRuns with ismcts white vs random black
//        Player white = new ISMCTSPlayer("white", 1000, 0.7);
//        Player black = new RandomPlayer("black");
//
//        // numOfRuns with ismcts black vs random white
////        Player white = new RandomPlayer("white");
////        Player black = new ISMCTSPlayer("black", 1000);
//
////        Player white = new ISMCTSPlayer("white", 1000);
////        Player black = new MiniMaxPlayer("black");
//
//
//        for (int i = 0; i < numOfRuns; i++) {
//            String iter = String.valueOf(i);
//
//            Board board = new Board();
//            Game game = new Game(board, white, black);
//            String winner = game.startGameExperimentsISMCTS();
//
//            String[] text = {iter, winner};
////            System.out.println(text[0] + text[1]);
//            writer.writeNext(text);
//
//        }
//        writer.flush();
    }

    public static void runExperiment() throws IOException {
        double[] explorationRates = {0.1, 0.3, 0.5, 0.7, 0.9}; // Different EXPL values to test
        int numOfRuns = 1000; // Number of games per EXPL value

        try (CSVWriter writer = new CSVWriter(new FileWriter("src/main/resources/Experiments/ismctsExp/explore.csv"))) {
            writer.writeNext(new String[]{"EXPL", "Winner"});

            for (double expl : explorationRates) {
                for (int i = 0; i < numOfRuns; i++) {
                    Player white = new ISMCTSPlayer("white", 1000, expl);
                    Player black = new RandomPlayer("black");

                    Board board = new Board();
                    Game game = new Game(board, white, black);
                    String winner = game.startGameExperimentsISMCTS();

                    writer.writeNext(new String[]{String.valueOf(expl), winner});
                }
            }
        }
    }
}
