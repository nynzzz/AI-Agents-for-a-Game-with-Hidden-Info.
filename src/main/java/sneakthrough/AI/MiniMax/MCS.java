package sneakthrough.AI.MiniMax;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import sneakthrough.Player.RandomPlayer;
import java.util.ArrayList;
import java.util.List;

public class MCS {
    private static final int childPerNode= 1;
    private static final int depth = 100;
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
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println("Level " + level + ":");
        node.getState().printBoard();
        for (Node child : node.getChildren()) {
            printTree(child, level + 1);
        }
    }

    public List<Node> gameSimulations(Node node, String currentPlayerColor){
        List<Node> children = new ArrayList<>();
        for (int i = 0; i < childPerNode; i++) {
            Board stateOfBoard = new Board(node.getState());
            if (isGameOver(stateOfBoard)) {
                break;
            }
            RandomPlayer randomPlayer = new RandomPlayer(currentPlayerColor);
            randomPlayer.makeMove(stateOfBoard);
            Node child = new Node(node, new Board(stateOfBoard));
            children.add(child);
        }
        return children;
    }


    public boolean isGameOver(Board stateOfBoard){
        boolean whiteWins = isWhiteWinner(stateOfBoard);
        boolean blackWins = isBlackWinner(stateOfBoard);
        return whiteWins || blackWins;
    }

    private boolean isWhiteWinner(Board board) {
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
        return blackPiecesLeft == 0;
    }

    private boolean isBlackWinner(Board board) {
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
        return whitePiecesLeft == 0;
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
