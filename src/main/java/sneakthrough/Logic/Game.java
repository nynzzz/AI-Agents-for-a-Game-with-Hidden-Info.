package sneakthrough.Logic;

import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;

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
        while (true) {
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

    // main method to play the game in console
    public static void main(String[] args) {
        Player player1 = new HumanPlayer("white");
        Player player2 = new HumanPlayer("black");
        Game game = new Game(player1, player2);
        game.startGame();
    }
}
