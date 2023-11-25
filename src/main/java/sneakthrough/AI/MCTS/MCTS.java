package sneakthrough.AI.MCTS;

import sneakthrough.AI.MCS.MCS;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.*;

public class MCTS {

    private int ITER;

    private int n_hypo;

    private String color;

    public MCTS(int ITER, int n_hypo, String color) {
        this.ITER = ITER;
        this.color = color;
        this.n_hypo = n_hypo;
    }

    public Node searchForBestMove(Node root) {

        int currentIter = 0;

        while(currentIter < ITER){

            Node selectedNode = select(root);

//            ArrayList<int[][]> selected_piecePosition = selectedNode.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
//            System.out.println("selected node: " );
//            for(int[][] play : selected_piecePosition){
//                System.out.println("Piece - " + play[0][0] + "," + play[0][1]);
//                System.out.println("Move - " + play[1][0] + "," + play[1][1]);
//            }

            Node expandedNode = expand(selectedNode);


//            ArrayList<int[][]> expanded_piecePosition = expandedNode.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
//            System.out.println("expanded node: " );
//            for(int[][] play : expanded_piecePosition){
//                System.out.println("Piece - " + play[0][0] + "," + play[0][1]);
//                System.out.println("Move - " + play[1][0] + "," + play[1][1]);
//            }

            double simulationResult = simulate(expandedNode);
//            System.out.println("simulation result: " + simulationResult);

            backpropagate(expandedNode, simulationResult);

            currentIter++;
        }

        // Choose the best move based on the statistics
        Node bestChild = select(root);
        return bestChild;
    }

    public Node select(Node node) {

//        System.out.println("selecting node");

        while (!node.getChildren().isEmpty()) {         // while node has children
//            if (node.getVisits() == 0) {                // if node has not been visited
//                System.out.println("node has not been visited");
//                System.out.println("HEEEEREEEEEEEEE-------");
//                return node; // Expand unvisited nodes  // return node
//            }

            // if node has been visited do:

            // UCT-based selection
//            System.out.println("node has been visited");
            node = UCT_basedSelection(node);  // select child node with highest UCT value
//            System.out.println("uct of selected node: " + node.getUCTValue());
        }
        return node;
    }

    public static Node UCT_basedSelection(Node node) {
        ArrayList<Node> children = node.getChildren();

//        for(Node child : children){
//            System.out.println("uct --" + child.getUCTValue());
//            System.out.println("win ration --" + child.getWinRatio());
//            System.out.println("visits --" + child.getVisits());
//        }

        // Filter out children that have never been visited
        ArrayList<Node> unvisitedChildren = new ArrayList<>();
        for (Node child : children) {
            if (child.getVisits() == 0) {
                unvisitedChildren.add(child);
            }
        }

//        System.out.println("NUMBER OF UNViSITED CHILDREN: " + unvisitedChildren.size());

        // If there are unvisited children, randomly choose one
        if (!unvisitedChildren.isEmpty()) {
            return unvisitedChildren.get(new Random().nextInt(unvisitedChildren.size()));
        }

        // Calculate the maximum UCT value among visited children
        double maxUCT = Double.NEGATIVE_INFINITY;
        Node selectedNode = null;

        for (Node child : children) {
            child.calculateUCTValue();
            double uct = child.getUCTValue();

            if (!Double.isNaN(uct) && uct > maxUCT) {
                maxUCT = uct;
                selectedNode = child;
            }
        }

        // If all UCT values are NaN among visited children, return a random child
        if (selectedNode != null) {
            return selectedNode;
        } else {
            System.out.println("all uct values are NaN among visited children");
            return getRandomChild(node);
        }
    }



    public Node expand(Node node) {
//        System.out.println("expanding node");
        List<int[][]> possibleMoves = getPossibleMoves(node);

        // Remove moves that have already been made with the same piece in the current simulation
        ArrayList<int[][]> filteredMoves = new ArrayList<>();
        for (int[][] move : possibleMoves) {
            if (!node.containsMove(move)) {
//                System.out.println("--------------------");
//                System.out.println("move added to filtered moves");
//                System.out.println("Piece - " + move[0][0] + "," + move[0][1]);
//                System.out.println("Move - " + move[1][0] + "," + move[1][1]);
                filteredMoves.add(move);
            }
        }

//        System.out.println("--------------------");
//        System.out.println("filtered moves size: " + filteredMoves.size());
//        System.out.println("filtered moves: ");
//        for(int[][] move : filteredMoves){
//            System.out.println("Piece - " + move[0][0] + "," + move[0][1]);
//            System.out.println("Move - " + move[1][0] + "," + move[1][1]);
//        }


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


    public List<int[][]> getPossibleMoves(Node node) {
        Board board = new Board();
        Piece[][] grid = board.getGrid();
        // update grid according to the played sequence of moves
        ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot = node.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
        for(int[][] play : sequenceOfPlaysThatLeadsToThisNodeFromRoot){
            int[] piecePos = play[0];
            int[] move = play[1];
            // move piece from position picePos to position move
            grid[move[0]][move[1]] = grid[piecePos[0]][piecePos[1]];
            grid[piecePos[0]][piecePos[1]] = null;
            // update piece position
            grid[move[0]][move[1]].setPosition(move);
        }


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
//        System.out.println("simulating node");
        MCS mcs = new MCS(n_hypo);
        return mcs.winRatio(node);
    }

    private static void backpropagate(Node node, double result) {
//        System.out.println("backpropagating nodes");
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

    // check if node hits a win condition (assuming player white, need to reach row 0)
    public boolean isWin(Node node){
        ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot = node.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
        if(!sequenceOfPlaysThatLeadsToThisNodeFromRoot.isEmpty()){
            int[] lastMove = sequenceOfPlaysThatLeadsToThisNodeFromRoot.get(sequenceOfPlaysThatLeadsToThisNodeFromRoot.size()-1)[1];
            if(lastMove[0] == 0){
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args){


        MCTS mcts = new MCTS(100, 100,"white");
        Node root = new Node();

        while(!mcts.isWin(root)){
            Node bestMoveFound =  mcts.searchForBestMove(root);

            System.out.println("--------------------");
            System.out.println("root depth: " + root.getDepth());
            System.out.println("BEST move found - UCT : " + bestMoveFound.getUCTValue());
            System.out.println("BEST move found - parent UCT : " + bestMoveFound.getParent().getUCTValue());
            System.out.println("BEST move found - sequence from root : " );
            for(int[][] play : bestMoveFound.getSequenceOfPlaysThatLeadsToThisNodeFromRoot()){
                System.out.println("Piece - " + play[0][0] + "," + play[0][1]);
                System.out.println("Move - " + play[1][0] + "," + play[1][1]);
            }
            System.out.println("BEST move depth : " + bestMoveFound.getDepth());
            // the array is of the form [piece position][move position]
            int[][] playThatLeadsToThisNodeFromParent = bestMoveFound.getPlayThatLeadsToThisNodeFromParent();
            System.out.println("BEST move found - move from parent : ");
            System.out.println("Piece - " + playThatLeadsToThisNodeFromParent[0][0] + "," + playThatLeadsToThisNodeFromParent[0][1]);
            System.out.println("Move - " + playThatLeadsToThisNodeFromParent[1][0] + "," + playThatLeadsToThisNodeFromParent[1][1]);
            ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot = bestMoveFound.getSequenceOfPlaysThatLeadsToThisNodeFromRoot();
            // get the last element of the sequence of plays that leads to this node from the root (the first move)
            int[][] firstMoveInSequence = sequenceOfPlaysThatLeadsToThisNodeFromRoot.get(0);
            int[] piecePosition = firstMoveInSequence[0];
            System.out.println("BEST move found - piece pos : " +  piecePosition[0] + "," + piecePosition[1]);
            int[] movePosition = firstMoveInSequence[1];
            System.out.println("BEST move found - move pos : " +  movePosition[0] + "," + movePosition[1]);


            System.out.println("!!!!!!!!!----NEW ROOT----!!!!!!!!!!!!");

            Node newRoot = UCT_basedSelection(root);
            System.out.println("new root depth: " + newRoot.getDepth());
            System.out.println("new root UCT: " + newRoot.getUCTValue());
            System.out.println("new root visits: " + newRoot.getVisits());
            System.out.println("new root win ratio: " + newRoot.getWinRatio());
            System.out.println("move that leads to new root: ");
            int[][] playThatLeadsToNewRootFromParent = newRoot.getPlayThatLeadsToThisNodeFromParent();
            System.out.println("Piece - " + playThatLeadsToNewRootFromParent[0][0] + "," + playThatLeadsToNewRootFromParent[0][1]);
            System.out.println("Move - " + playThatLeadsToNewRootFromParent[1][0] + "," + playThatLeadsToNewRootFromParent[1][1]);

            root = newRoot;
        }

    }
}
