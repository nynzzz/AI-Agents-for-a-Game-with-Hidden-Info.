package sneakthrough.AI.MiniMax;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Logic.Piece;
import sneakthrough.Player.RandomPlayer;
import java.util.ArrayList;
import java.util.List;

public class MCS {
    private static final int childPerNode= 3;
    private static final int depth = 3;
    public List<Node> gameSampling(Board board, String startingColor){
        List<Node> gameTree = new ArrayList<>();
        Node root = new Node(null, board);
        List<Node> currentLevel = new ArrayList<>();
        currentLevel.add(root);
        String currentPlayerColor = startingColor;

        for (int i = 0; i < depth; i++) {
            List<Node> nextLevel = new ArrayList<>();
            for (Node parent: currentLevel) {
                List<Node> children = gameSimulations(parent, currentPlayerColor);
                parent.setChildren(children);
                nextLevel.addAll(children);
            }
            currentLevel = nextLevel;
            currentPlayerColor = currentPlayerColor.equals("white") ? "black" : "white";
        }
        gameTree.add(root);
        return gameTree;
    }
    public void printTree(Node node, int level) {
        System.out.println("Level " + level + ":");
        node.getState().printBoard();

        System.out.println("Win Probability: " + node.getWinProbability());

        for (Node child : node.getChildren()) {
            printTree(child, level + 1);
        }
    }

    public List<Node> gameSimulations(Node node, String currentPlayerColor){
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < childPerNode; i++) {
            Board stateOfBoard = node.getState().clone();
            if (stateOfBoard.isGameOver()) {
                break;
            }
            RandomPlayer randomPlayer = new RandomPlayer(currentPlayerColor);
            randomPlayer.makeMove(stateOfBoard);
            Node child = new Node(node, stateOfBoard.clone());

            double winProbability = winProbability(stateOfBoard.clone(), "white", 5);
            child.setWinProbability(winProbability);

            children.add(child);
        }
        return children;
    }

    public  double winProbability(Board board, String color, int simulations){
        int wins = 0;
        for (int i = 0; i < simulations; i++) {
            String winner = simulateGameForProb(board);
            if (winner.equals(color)){
                wins++;
            }
        }
        return (double) wins / simulations;
    }

    public String simulateGameForProb(Board board){
        RandomPlayer white = new RandomPlayer("white");
        RandomPlayer black = new RandomPlayer("black");

        Board newBoard = board.clone();

        Game game = new Game(newBoard, white, black);
        String winner = game.startGameExperiments();
        return winner;
    }


    public static void main(String[] args) {
        Board board = new Board();
        MCS mcs = new MCS();
        List<Node> gameTrees = mcs.gameSampling(board, "white");


        for (int i = 0; i < gameTrees.size(); i++) {
            System.out.println("Generated Tree #" + (i + 1) + ":");
            mcs.printTree(gameTrees.get(i), 0);
        }
    }
}
