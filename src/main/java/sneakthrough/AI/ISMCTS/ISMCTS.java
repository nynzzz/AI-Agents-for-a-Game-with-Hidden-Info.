package sneakthrough.AI.ISMCTS;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ISMCTS {

    private BoardState rootState;
    private BoardState currentState;
    private ArrayList<int[][]> possibleMoves;
    private Node_ISMCTS currentNode;

    private int iterLimit;
    private static final double EXPL = 0.7;


    public ISMCTS(BoardState rootState, int iterLimit) {
        this.rootState = rootState;
        this.iterLimit = iterLimit;
    }

    public BoardState getRootState() {
        return this.rootState;
    }

    public void setRootState(BoardState rootState) {
        this.rootState = rootState;
    }

    public int getIterLimit() {
        return this.iterLimit;
    }

    public Node_ISMCTS runISMCTS(){

        Node_ISMCTS rootNode = new Node_ISMCTS(null, null, rootState.getCurrentPlayer());

        for (int i = 0; i < iterLimit; i++) {

            this.currentNode = rootNode;

            System.out.println("Current node: " + currentNode.toString());

            this.currentState = determinize(rootState);
            this.possibleMoves = currentState.getPossibleMoves();

//            System.out.println("possible moves size: " + possibleMoves.size());

            // selection
            selectChild();

            // expansion
            expand();

            // simulation
            String result = simulate();

            // backpropagation
            backpropagate(result);

        }

        Node_ISMCTS best = rootNode.getChildren().get(0);
        for (Node_ISMCTS child : rootNode.getChildren()){
            if (child.getVisits() > best.getVisits()){
                best = child;
            }
        }

        System.out.println("----------BEST CHILD-----------" + best.toString());
//        return best.getMove();
        return best;
    }



    private void selectChild(){
        while((!possibleMoves.isEmpty() && !this.currentState.getIsGameOver()) && this.currentNode.getUntriedMoves(this.possibleMoves).isEmpty()){
//            System.out.println("Possible moves: " + possibleMoves.size());
//            for (int[][] move : possibleMoves){
//                System.out.println("" + move[0][0] + move[0][1] + " ---> "  + move[1][0] + move[1][1]);
//            }
            this.currentNode = this.currentNode.getMostPromisingChild(this.possibleMoves, EXPL);
//            System.out.println("Current node: " + currentNode.toString());
            this.currentState.doMove(this.currentNode.getMove());
            this.possibleMoves = this.currentState.getPossibleMoves();
        }
    }

    private void expand(){
        ArrayList<int[][]> untriedMoves = this.currentNode.getUntriedMoves(this.possibleMoves);
        if(!untriedMoves.isEmpty()){
            int[][] move = untriedMoves.get((int) (Math.random() * untriedMoves.size()));
            this.currentNode = this.currentNode.addChild(move, this.currentState.getCurrentPlayer());
            this.currentState.doMove(move);

        }
    }

    private String simulate(){
        Piece[][] currentGrid = this.currentState.getGrid();
        Board currentBoard = new Board();
        currentBoard.setGrid(currentGrid);

        RandomSimulation randomSimulation = new RandomSimulation(currentBoard);
        String result = randomSimulation.run();

        return result;
    }

    private void backpropagate(String result){
        while(this.currentNode != null){
            this.currentNode.update(result);
            this.currentNode = this.currentNode.getParent();
        }
    }



    public BoardState determinize(BoardState rootState){
        // sequence of moves from root to current node
        ArrayList<int[][]> moves = new ArrayList<>();
        Node_ISMCTS currentNode = this.currentNode;

        while (currentNode.getParent() != null){
            moves.add(currentNode.getMove());
            currentNode = currentNode.getParent();
        }

        Board board = new Board();

        System.out.println("moves size: " + moves.size());
        // print out the moves
        for (int[][] move : moves){
            System.out.println("move: " + move[0][0] + move[0][1] + " --> " + move[1][0] + move[1][1]);
        }

        // opponnent taken moves
        ArrayList<int[][]> opponentMoves = new ArrayList<>();

        // moves thaqt we did
        ArrayList<int[][]> didMoves = new ArrayList<>();


        // apply moves to board, in according order
        // for every move also make a random opponent move
        // once all moves are applied = determinized board
        // moves should bew taken in FIFO order
        for (int i=0; i<moves.size(); i++){
            // apply move for current player
            int[][] move = moves.get(moves.size() - 1 - i);
            System.out.println("-MOVE about to be taken: " + move[0][0] + move[0][1] + " --> " + move[1][0] + move[1][1]);
            int[] from = move[0];
            int[] to = move[1];
            Piece piece = board.getGrid()[from[0]][from[1]];
            board.getGrid()[from[0]][from[1]] = null;
            board.getGrid()[to[0]][to[1]] = piece;
            piece.setPosition(to);

            didMoves.add(move);


//            System.out.println(" taken move: " + move[0][0] + move[0][1] + " --> " + move[1][0] + move[1][1]);

            // apply random move for opponent
//            ArrayList<Piece> opponentPieces = rootState.getOpponentPieces(rootState.getCurrentPlayer());

            ArrayList<Piece> opponentPieces = new ArrayList<>();
            if(rootState.getCurrentPlayer().equals("white")){
                opponentPieces = board.getPlayerPieces("black");
            }
            else{
                opponentPieces = board.getPlayerPieces("white");
            }
            System.out.println("opponent pieces size: " + opponentPieces.size());
            Piece randomPiece = opponentPieces.get((int) (Math.random() * opponentPieces.size()));
            ArrayList<int[]> validMoves = randomPiece.getValidMoves(board);

            // ensure the chosen piece has valid moves
            while (validMoves.isEmpty()) {
                randomPiece = opponentPieces.get((int) (Math.random() * opponentPieces.size()));
                validMoves = randomPiece.getValidMoves(board);
            }

            int[] randomMove = validMoves.get((int) (Math.random() * validMoves.size()));

            // ensure the opponent move is not already taken
            while ((containsFirstPart(opponentMoves, randomPiece.getPosition()) && !containsSecondPart(opponentMoves, randomPiece.getPosition())) && (containsFirstPart(moves, randomMove) && !containsFirstPart(didMoves, randomMove)) ){
                System.out.println("Opponent move already taken!!!");
                randomPiece = opponentPieces.get((int) (Math.random() * opponentPieces.size()));
                validMoves = randomPiece.getValidMoves(board); // Corrected this line
                while (validMoves.isEmpty()) {
                    randomPiece = opponentPieces.get((int) (Math.random() * opponentPieces.size()));
                    validMoves = randomPiece.getValidMoves(board);
                }
                randomMove = validMoves.get((int) (Math.random() * validMoves.size()));
            }

            // store the move as a performed move in this determinized board
            opponentMoves.add(new int[][]{randomPiece.getPosition(), randomMove});

            System.out.println("random OPPONENT piece: " + randomPiece.getPosition()[0] + randomPiece.getPosition()[1]);
            System.out.println("random OPPONENT move: " + randomMove[0] + randomMove[1]);
            int[] from2 = randomPiece.getPosition();
            int[] to2 = randomMove;
            Piece piece2 = board.getGrid()[from2[0]][from2[1]];
            board.getGrid()[from2[0]][from2[1]] = null;
            board.getGrid()[to2[0]][to2[1]] = piece2;
            piece2.setPosition(to2);
        }

        BoardState determinizedBoardState = new BoardState(board, rootState.getCurrentPlayer());
        return determinizedBoardState;
    }


    public static void main(String[] args){
        Board board = new Board();
        BoardState boardState = new BoardState(board, "white");
        ISMCTS ismcts = new ISMCTS(boardState, 10000);
        Node_ISMCTS rootNode = new Node_ISMCTS(null, null, boardState.getCurrentPlayer());
//        System.out.println("root node: " + rootNode.toString());
        Node_ISMCTS newRoot= ismcts.runISMCTS();

//        int i = 10;
//        while (i > 0){
//            Node_ISMCTS newRoot= ismcts.runISMCTS();
//            rootNode = newRoot;
//            i--;
////            System.out.println("move: " + move[0][0] + move[0][1] + move[1][0] + move[1][1]);
//        }

    }



    // ---------helpers----------//

    public boolean containsFirstPart(ArrayList<int[][]> list, int[] arrayToFind) {
        for (int[][] array : list) {
            if (arraysPartEqual(array[0], arrayToFind)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsSecondPart(ArrayList<int[][]> list, int[] arrayToFind) {
        for (int[][] array : list) {
            if (arraysPartEqual(array[1], arrayToFind)) {
                return true;
            }
        }
        return false;
    }

    private boolean arraysPartEqual(int[] array1, int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }

        return true;
    }



}
