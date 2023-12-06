package sneakthrough.AI.ISMCTS;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Arrays;
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

            this.currentState = rootState.determinize();
            this.possibleMoves = currentState.getPossibleMoves();

            // print current state
//            currentState.StatetoString();


            // print possible moves
//            System.out.println("----------POSSIBLE MOVES-----------");
//            for (int[][] move : possibleMoves) {
//                System.out.println(Arrays.deepToString(move));
//            }


            // if possible move contains a move that leads to a win for the current player, return the node that leads to that move
            for (int[][] move : possibleMoves) {
                if (currentState.isWinningMove(move)) {
                    Node_ISMCTS winningNode = new Node_ISMCTS(null, move, currentState.getCurrentPlayer());
                    winningNode.setParent(rootNode);
                    return winningNode;
                }
            }

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
        return best;
    }



    private void selectChild(){
        while((!possibleMoves.isEmpty() && !this.currentState.getIsGameOver()) && this.currentNode.getUntriedMoves(this.possibleMoves).isEmpty()){
            this.currentNode = this.currentNode.getMostPromisingChild(this.possibleMoves, EXPL);
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






    public static void main(String[] args){
        Board board = new Board();
        BoardState boardState = new BoardState(board, "white");
        ISMCTS ismcts = new ISMCTS(boardState, 1000);
        Node_ISMCTS rootNode = new Node_ISMCTS(null, null, boardState.getCurrentPlayer());
//        System.out.println("root node: " + rootNode.toString());
        Node_ISMCTS newRoot= ismcts.runISMCTS();
        System.out.println("parent: " + newRoot.getParent().toString());

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