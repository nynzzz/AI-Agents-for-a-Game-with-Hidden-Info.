package sneakthrough.Logic;

import sneakthrough.AI.ISMCTS.BoardState;
import sneakthrough.Player.*;

import java.util.ArrayList;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private boolean turn;

    public Game(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = true;
    }

    public void startGame() {
        while (!isGameOver()) {
            if (turn) {
//                System.out.println("--------------------");
//                System.out.println("board state: " + board.printBoardtoStringFlattened());
//                System.out.println("player to move : w ");

                player1.makeMove(board);
                System.out.println("--------------------");
                board.printBoard();

                turn = false;
            } else {
//                System.out.println("--------------------");
//                System.out.println("board state: " + board.printBoardtoStringFlattened());
//                System.out.println("player to move : b ");

                player2.makeMove(board);
                System.out.println("--------------------");
                board.printBoard();
                turn = true;
            }
        }

        if (isWhiteWinner()) {
            System.out.println("--------------------");
            System.out.println("winner: w");
        } else if (isBlackWinner()) {
            System.out.println("--------------------");
            System.out.println("winner: b");
        }
    }

    // ----------------------------- //
    // EXPERIMENTS METHOD, DONT USE //
    public String startGameExperiments() {
        while (!isGameOver()) {
            if (turn) {
                player1.makeMove(board);
//                System.out.println("--------------------");
//                board.printBoard();
//                System.out.println("--------------------");
                turn = false;
            } else {
                player2.makeMove(board);
//                System.out.println("--------------------");
//                board.printBoard();
//                System.out.println("--------------------");
                turn = true;
            }
        }

        if (isWhiteWinner()) {
            return "white";
        } else if (isBlackWinner()) {
            return "black";
        }
        return null;
    }

    // ---------------------------- //

    // same stuff but for ismcts vs random

    public String startGameExperimentsISMCTS() {
        while (!isGameOver()) {
            if (turn) {
                player1.makeMove(board);
//                System.out.println("--------------------");
//                board.printBoard();
//                System.out.println("--------------------");
                turn = false;
            } else {
                player2.makeMove(board);
//                System.out.println("--------------------");
//                board.printBoard();
//                System.out.println("--------------------");
                turn = true;
            }
        }

        if (isWhiteWinner()) {
            System.out.println("a0");
            return "Alpha_ismcts";
        } else if (isBlackWinner()) {
            System.out.println("ismcts");
            return "ismcts";
        }
        return null;
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


//     main method to play the game in console
    public static void main(String[] args) {
        Board board = new Board();
        Player player1 = new AlphaISMCTSPlayer("white", 100, 100);
//        Player player2 = new ISMCTSPlayer("black", 1000);
//        Player player2 = new ISMCTSPlayer("black", 1000, 0.7);
//        Player player2 = new MiniMaxPlayer("black");
        Player player2 = new RandomPlayer("black");
        Game game = new Game(board,player1, player2);
        game.startGame();
    }
}
