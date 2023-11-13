package sneakthrough.AI.MCS;

import sneakthrough.AI.MCTS.Node;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.DummyPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomForDummyPlayer;
import sneakthrough.Player.RandomPlayer;

import java.util.ArrayList;

// Monte Carlo Sampling
public class MCS {
    // number of hypothesis to sample
    private int n;

    public MCS(int n) {
        this.n = n;
    }

    // this class needs to:
    // given a node
    // 1. get the nodes sequence of plays that leads to it from the root node
    // 2. for n times do:
    //      2.1. do a random game but one player follows the sequence of plays that leads to the node
    //      2.2. other player does random moves
    //      2.3. once the sequence of plays is over, do random moves for both players
    //      2.4. if the player that follows the sequence of plays wins, add 1 to the win counter
    // 3. return the win counter divided by n

    public double winRatio(Node node) {
        int wins = 0;
        // 1. get the nodes sequence of plays that leads to it from the root node
        ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot = node.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();

        // 2. for n times do:
        for (int i = 0; i < this.n; i++) {

//            System.out.println("--------------------");
//            System.out.println("lenght of sequence: " + sequenceOfPlaysThatLeadsToThisNodeFromRoot.size());

            // 2.1. do a random game but one player follows the sequence of plays that leads to the node
            // 2.2. other player does random moves but cannot interrupt the sequence of plays
            // 2.3. once the sequence of plays is over, do random moves for both players
            // 2.4. if the player that follows the sequence of plays wins, add 1 to the win counter

            // create a dummy player
            Player dummyPlayer_white = new DummyPlayer(sequenceOfPlaysThatLeadsToThisNodeFromRoot, "white");
            Player randomPlayer_black = new RandomForDummyPlayer("black", sequenceOfPlaysThatLeadsToThisNodeFromRoot);

            // create a new game
            Board board = new Board();
            Game game = new Game(board, dummyPlayer_white, randomPlayer_black);
            String winner = game.startGameExperiments();

            if(winner.equals("white")){
                wins++;
            }
        }
//        System.out.println("wins: " + wins);
        // 3. return the win counter
        return (double) wins /n;
    }



    public static void main(String[] args) {
        // test
        MCS mcs = new MCS(1000);
        Node node = new Node();

        ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot = new ArrayList<>();
        // a sequence of plays where the player only pushes one pon to the left until the end of the game
        int[][] play1 = {{6, 0}, {5, 0}};
        int[][] play2 = {{5, 0}, {4, 0}};
        int[][] play3 = {{4, 0}, {3, 0}};
        int[][] play4 = {{3, 0}, {2, 0}};
        int[][] play5 = {{2, 0}, {1, 1}};
        int[][] play6 = {{1, 1}, {0, 2}};
        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play1);
        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play2);
        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play3);
        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play4);
        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play5);
//        sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(play6);

        node.setSequenceOfPlaysThatLeadsToThisNodeFromRoot_CUSTOM(sequenceOfPlaysThatLeadsToThisNodeFromRoot);

        double wins = mcs.winRatio(node);
        System.out.println("winRatio (probability): " + wins);
    }

}
