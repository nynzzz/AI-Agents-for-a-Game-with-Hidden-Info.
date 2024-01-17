package sneakthrough.ML.AlphaZero;

import com.opencsv.CSVWriter;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Logic.GameISMCTS;
import sneakthrough.Player.ISMCTSPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataGeneration {


    public void initializeCsv(String filename) throws IOException {
        // Open the CSV file
        FileWriter fileWriter = new FileWriter(filename);
        CSVWriter csvWriter = new CSVWriter(fileWriter);

        // Write the header
        String[] header = {"Game Number", "Board State", "Player to Move", "Visit Counts", "Move to Take", "Value", "Game Result"};
        csvWriter.writeNext(header);

        csvWriter.close();
    }

    public void playGames(String filename, int numberOfGames) throws IOException {
        initializeCsv(filename);
        for (int i = 0; i < numberOfGames; i++) {
            ISMCTSPlayer white = new ISMCTSPlayer("white", 1000, 0.7);
            ISMCTSPlayer black = new ISMCTSPlayer("black", 1000, 0.7);

            Board board = new Board();
            GameISMCTS game = new GameISMCTS(board, white, black);
            game.startGameData(i, filename);
            // print data

        }
    }

    public static void main(String[] args) throws IOException {
        DataGeneration dataGeneration = new DataGeneration();
        dataGeneration.playGames("src/main/resources/ML/AlphaZero/training.csv", 1);
    }


}
