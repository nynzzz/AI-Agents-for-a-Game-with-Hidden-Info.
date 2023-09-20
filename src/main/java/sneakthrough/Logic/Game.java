package sneakthrough.Logic;

import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private boolean turn;

    public Game(Player player1, Player player2) {
        this.board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        this.turn = true;
    }

    public void startGame() {
        while (!isGameOver()) {
            if (turn) {
                System.out.println("White's turn");
                player1.makeMove(board);
                board.printBoard();
                turn = false;
            } else {
                System.out.println("Black's turn");
                player2.makeMove(board);
                board.printBoard();
                turn = true;
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    // stopping criteria for the game
    // game ends when one of the players arrives at the other side of the board or when one of the players has no pieces left
    public boolean isGameOver() {
        Piece[][] grid = board.getGrid();
        boolean white = false;
        boolean black = false;
        // check if one of the players arrived at the other side of the board
        for (int i = 0; i < grid.length; i++) {
            if (grid[0][i] != null && grid[0][i].getColor().equals("white")) {
                white = true;
            }
            if (grid[grid.length - 1][i] != null && grid[grid.length - 1][i].getColor().equals("black")) {
                black = true;
            }
        }
        // check if one of the players has no pieces left
        int whitePieces = 0;
        int blackPieces = 0;
        for (int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid.length; j++){
                if(grid[i][j] != null){
                    if(grid[i][j].getColor().equals("white")){
                        whitePieces++;
                    }
                    else{
                        blackPieces++;
                    }
                }
            }
        }
        if(whitePieces == 0 || blackPieces == 0){
            return true;
        }
        return false;
    }


    // main method to play the game in console
    public static void main(String[] args) {
        Player player1 = new RandomPlayer("white");
        Player player2 = new RandomPlayer("black");
        Game game = new Game(player1, player2);
        game.startGame();
    }
}
