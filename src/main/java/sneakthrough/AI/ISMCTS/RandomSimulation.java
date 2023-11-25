package sneakthrough.AI.ISMCTS;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

public class RandomSimulation {

    private Board board;

    public RandomSimulation(Board board) {
        this.board = board;
    }

    public String run() {

        Player white = new RandomPlayer("white");
        Player black = new RandomPlayer("black");
        Game game = new Game(board, white, black);

        String winner = game.startGameExperiments();

        return winner;
    }
}
