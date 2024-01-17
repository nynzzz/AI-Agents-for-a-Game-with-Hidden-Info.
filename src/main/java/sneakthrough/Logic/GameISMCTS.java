package sneakthrough.Logic;

import com.opencsv.CSVWriter;
import sneakthrough.AI.ISMCTS.BoardState;
import sneakthrough.Player.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GameISMCTS {
    private Board board;
    private ISMCTSPlayer player1;
    private ISMCTSPlayer player2;

    public ArrayList<String> gameData = new ArrayList<>();
    private boolean turn;

    public GameISMCTS(Board board, ISMCTSPlayer player1, ISMCTSPlayer player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = true;
    }


    public void startGameData(int gameID, String filename) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(filename, true))) { // Append mode
            while (!isGameOver()) {
                String boardState = board.printBoardtoStringFlattened();
                String playerToMove = turn ? "w" : "b";
                String visitCounts, moveToTake;

                if (turn) {
                    player1.makeMove(board); // Make the move
                    visitCounts = player1.visitCounts;
                    moveToTake = player1.moveToTake;

                    turn = false;
                } else {
                    player2.makeMove(board);
                    visitCounts = player2.visitCounts;
                    moveToTake = player2.moveToTake;

                    turn = true;
                }

                String value = ""; // Value is empty for now
                String[] dataToWrite = new String[] {
                        String.valueOf(gameID),
                        boardState,
                        playerToMove,
                        visitCounts,
                        moveToTake,
                        value
                };
                csvWriter.writeNext(dataToWrite);
            }

            // Add the game result at the end of the CSV for this game
            String winner = isWhiteWinner() ? "w" : (isBlackWinner() ? "b" : "draw");
            csvWriter.writeNext(new String[] {String.valueOf(gameID), "", "", "", "", "", winner});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    // ---------------------------- //

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    // STOPPING CRITERIA
    // game ends when one of the players arrives at the other side of the board or when one of the players has no pieces left
    public boolean isGameOver() {
        boolean whiteWins = isWhiteWinner();
        boolean blackWins = isBlackWinner();

        // either player reached the other side or has no pieces left
        return whiteWins || blackWins;
    }

    // check if white has won
    private boolean isWhiteWinner() {
        int blackPiecesLeft = 0;
        Piece[][] grid = board.getGrid();
        // check if white has reached the other side
        for (int i = 0; i < grid.length; i++) {
            if (grid[0][i] != null && grid[0][i].getColor().equals("white")) {
                return true;
            }
        }
        // check if black has no pieces left
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length ; j++) {
                if (grid[i][j] != null && grid[i][j].getColor().equals("black")) {
                    blackPiecesLeft++;
                }
            }
        }
        //System.out.println("black pieces left: " + blackPiecesLeft);
        return blackPiecesLeft == 0;
    }

    // check if black has won
    private boolean isBlackWinner() {
        int whitePiecesLeft = 0;
        Piece[][] grid = board.getGrid();
        // check if black has reached the other side
        for (int i = 0; i < grid.length; i++) {
            if (grid[grid.length - 1][i] != null && grid[grid.length - 1][i].getColor().equals("black")) {
                return true;
            }
        }
        // check if white has no pieces left
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length ; j++) {
                if (grid[i][j] != null && grid[i][j].getColor().equals("white")) {
                    whitePiecesLeft++;
                }
            }
        }
        //System.out.println("white pieces left: " + whitePiecesLeft);
        return whitePiecesLeft == 0;
    }
}
