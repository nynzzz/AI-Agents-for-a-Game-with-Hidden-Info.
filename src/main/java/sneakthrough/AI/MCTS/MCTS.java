package sneakthrough.AI.MCTS;

import sneakthrough.AI.MCS.MCS;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.*;

public class MCTS {

    private int ITER;

    private String color;

    public MCTS(int ITER, String color) {
        this.ITER = ITER;
        this.color = color;
    }

    public Node searchForBestMove(Board board) {
        Node root = new Node();

        int currentIter = 0;

        while(currentIter < ITER){

            Node selectedNode = select(root);
//            int[] selected_piecePosition = selectedNode.getPlayThatLeadsToThisNodeFromParent()[0];
//            int[] selected_movePosition = selectedNode.getPlayThatLeadsToThisNodeFromParent()[1];
//            System.out.println("selected node: " );
//            System.out.println("Piece - " + selected_piecePosition[0] + "," + selected_piecePosition[1]);
//            System.out.println("Move - " + selected_movePosition[0] + "," + selected_movePosition[1]);
            ArrayList<int[][]> selected_piecePosition = selectedNode.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
            System.out.println("selected node: " );
            for(int[][] play : selected_piecePosition){
                System.out.println("Piece - " + play[0][0] + "," + play[0][1]);
                System.out.println("Move - " + play[1][0] + "," + play[1][1]);
            }

            Node expandedNode = expand(selectedNode, board);
//            int[] expanded_piecePosition = expandedNode.getPlayThatLeadsToThisNodeFromParent()[0];
//            int[] expanded_movePosition = expandedNode.getPlayThatLeadsToThisNodeFromParent()[1];
//            System.out.println("expanded node: " );
//            System.out.println("Piece - " + expanded_piecePosition[0] + "," + expanded_piecePosition[1]);
//            System.out.println("Move - " + expanded_movePosition[0] + "," + expanded_movePosition[1]);
            ArrayList<int[][]> expanded_piecePosition = expandedNode.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
            System.out.println("expanded node: " );
            for(int[][] play : expanded_piecePosition){
                System.out.println("Piece - " + play[0][0] + "," + play[0][1]);
                System.out.println("Move - " + play[1][0] + "," + play[1][1]);
            }

            double simulationResult = simulate(expandedNode);
            System.out.println("simulation result: " + simulationResult);

            backpropagate(expandedNode, simulationResult);

            currentIter++;
        }

        // Choose the best move based on the statistics
        Node bestChild = select(root);
        return bestChild;
    }

    public Node select(Node node) {

        System.out.println("selecting node");

        while (!node.getChildren().isEmpty()) {
            if (node.getVisits() == 0) {
                System.out.println("node has not been visited");
                return node; // Expand unvisited nodes
            }

            // UCT-based selection
            System.out.println("node has been visited");
            node = UCT_basedSelection(node);
            System.out.println("uct of selected node: " + node.getUCTValue());
        }
        return node;
    }

    public Node UCT_basedSelection(Node node) {
        ArrayList<Node> children = node.getChildren();

        double explorationWeight = 1.0;

        // Calculate the maximum UCT value, excluding NaN values
        double maxUCT = Double.NEGATIVE_INFINITY;
        Node selectedNode = null;

        for (Node child : children) {
            child.calculateUCTValue();
//            double uct = child.getUCTValue() +
//                    explorationWeight * Math.sqrt(Math.log(node.getVisits()) / child.getVisits());
//            child.setUCT(uct);

            double uct = child.getUCTValue();

            if (!Double.isNaN(uct) && uct > maxUCT) {
                maxUCT = uct;
                selectedNode = child;
            }
        }

        if (selectedNode != null) {
            return selectedNode;
        } else {
            // If all UCT values are NaN, just return a random child
            System.out.println("all uct values are NaN");
            return getRandomChild(node);
        }
    }


    public Node expand(Node node, Board board) {
        System.out.println("expanding node");
        List<int[][]> possibleMoves = getPossibleMoves(board);

        // Remove moves that have already been made with the same piece in the current simulation
        ArrayList<int[][]> filteredMoves = new ArrayList<>();
        for (int[][] move : possibleMoves) {
            if (!node.containsMove(move)) {
                System.out.println("--------------------");
                System.out.println("move added to filtered moves");
                System.out.println("Piece - " + move[0][0] + "," + move[0][1]);
                System.out.println("Move - " + move[1][0] + "," + move[1][1]);
                filteredMoves.add(move);
            }
        }


        //TODO:
        // add the previous move positions to possible piece positions for the next move


        for (int[][] move : filteredMoves) {
            Node child = new Node(node);
            child.setPlayThatLeadsToThisNodeFromParent(move);
            child.setSequenceOfPlaysThatLeadsToThisNodeFromRoot(node);
            node.addChild(child);
            child.setParent(node);
        }

        // Check if there are children before trying to get a random child
        if (!node.getChildren().isEmpty()) {
            // Randomly select and return one of the newly created child nodes
            return getRandomChild(node);
        } else {
            // If there are no children, return the node itself
            return node;
        }
    }


    public List<int[][]> getPossibleMoves(Board board) {
        // board grid
        Piece[][] grid = board.getGrid();
        // get all pieces of the according color
        String  color = this.getColor();
        ArrayList<Piece> pieces = board.getPlayerPieces(color);
        // get all valid moves of the pieces
        ArrayList<int[][]> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            ArrayList<int[]> validMoves = piece.getValidMoves(board);
            for (int[] move : validMoves) {
                int[][] possibleMove = new int[2][2];
                possibleMove[0] = piece.getPosition();
                possibleMove[1] = move;
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;
    }

    public double simulate(Node node) {
        System.out.println("simulating node");
        MCS mcs = new MCS(10);
        return mcs.winRatio(node);
    }

    private static void backpropagate(Node node, double result) {
        System.out.println("backpropagating nodes");
        // Update the statistics of all nodes along the path from the expanded node to the root
        while (node != null) {
            node.updateVisits();
            node.updateWinRatio(result);
            node = node.getParent();
        }
    }

    private static Node getRandomChild(Node node) {
        // Randomly select and return one of the child nodes
        Random random = new Random();
        return node.getChildren().get(random.nextInt(node.getChildren().size()));
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
}
