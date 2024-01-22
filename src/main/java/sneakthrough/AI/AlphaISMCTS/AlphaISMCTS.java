package sneakthrough.AI.AlphaISMCTS;

import sneakthrough.AI.ISMCTS.BoardState;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import sneakthrough.ML.AlphaZero.AlphaZeroModelClient;
import sneakthrough.ML.AlphaZero.ModelPrediction;

import java.util.*;

public class AlphaISMCTS {

    private BoardState rootState;
    private BoardState currentState;
    private ArrayList<int[][]> possibleMoves;
    private Node_AlphaISMCTS currentNode;
    AlphaZeroModelClient client = new AlphaZeroModelClient();
    private int iterLimit;
    private double policyWeight;


    public AlphaISMCTS(BoardState rootState, int iterLimit, double policyWeight) {
        this.rootState = rootState;
        this.iterLimit = iterLimit;
        this.policyWeight = policyWeight;
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

    public Node_AlphaISMCTS runAlphaISMCTS(){

        Node_AlphaISMCTS rootNode = new Node_AlphaISMCTS(null, null, 1.0, rootState.getCurrentPlayer());

        for (int i = 0; i < iterLimit; i++) {

            this.currentNode = rootNode;

            int[][] winingMove = rootState.makeWinningMoveIfExists();
            if(winingMove != null){
                Node_AlphaISMCTS winningNode = new Node_AlphaISMCTS(rootNode, winingMove, 1.0, rootState.getCurrentPlayer());
                return winningNode;
            }

            this.currentState = rootState.determinize();
            this.possibleMoves = currentState.getPossibleMoves();

            // selection
            selectChild();

            // expansion
            double value = expand();

            // backpropagation
            backpropagate(value);

        }

        Node_AlphaISMCTS best = rootNode.getChildren().get(0);
        for (Node_AlphaISMCTS child : rootNode.getChildren()){
            if (child.getVisits() > best.getVisits()){
                best = child;
            }
        }

//        System.out.println("child visits: " + rootNode.printChildrenToStringFlattened());
//        System.out.println("move to take: " + best.printMoveToStringFlattened());

        return best;
    }



    private void selectChild(){
        while((!possibleMoves.isEmpty() && !this.currentState.getIsGameOver()) && this.currentNode.getUntriedMoves(this.possibleMoves).isEmpty()){
            this.currentNode = this.currentNode.getMostPromisingChild(this.possibleMoves, policyWeight);
            this.currentState.doMove(this.currentNode.getMove());
            this.possibleMoves = this.currentState.getPossibleMoves();
        }
    }

    private double expand(){

        // board state as flattened string
        String boardState = this.currentState.toStringFlattened();
//        System.out.println("board state: " + boardState);
        // untried moves as flattened string
        String possibleMoves = arrayListToString(this.possibleMoves);
//        System.out.println("possible moves: " + possibleMoves);
        // get prediction from model
        ModelPrediction prediction = client.getPrediction(boardState, possibleMoves);
        // get the pred value
        double value = prediction.getValue();
//        System.out.println("value: " + value);
        // get the policy map
        HashMap<String, Double> policyMap = prediction.getPolicyMap();
        // print the policy map
//        System.out.println("policy map:");
//        for (String move : policyMap.keySet()) {
//            double policyValue = policyMap.get(move);
//            System.out.println("Move: " + move + ", Policy Value: " + policyValue);
//        }

        if(!this.possibleMoves.isEmpty()){
            for (int[][] move : this.possibleMoves) {
                String moveStr = moveToString(move);
                double policyValue = policyMap.getOrDefault(moveStr, 0.0);
                this.currentNode.addChild_notReturn(move, policyValue, this.currentState.getCurrentPlayer());
            }
        }
        return value;
    }

    private void backpropagate(double value){
        while(this.currentNode != null){
            this.currentNode.update(value);
            this.currentNode = this.currentNode.getParent();
        }
    }



    public static void main(String[] args){
        Board board = new Board();
        BoardState boardState = new BoardState(board, "white");
        AlphaISMCTS ismcts = new AlphaISMCTS(boardState, 1000, 0.7);
        Node_AlphaISMCTS rootNode = new Node_AlphaISMCTS(null, null, 1.0,boardState.getCurrentPlayer());
//        System.out.println("root node: " + rootNode.toString());
        Node_AlphaISMCTS newRoot= ismcts.runAlphaISMCTS();
        System.out.println("parent: " + newRoot.getParent().toString());

    }



    // ---------helpers----------//

    // a method that takes as input an int[][] and returns a string representation of it, as ((7, 1), (6, 2))
    public String moveToString(int[][] move){
        String string = "((" + move[0][0] + ", " + move[0][1] + "), (" + move[1][0] + ", " + move[1][1] + "))";
        return string;
    }


    // a method that takes as input an ArrayList of int[][] and returns a string representation of it, as 2,1:1,0;...
    public String arrayListToString(ArrayList<int[][]> arrayList){
        String string = "";
        for (int[][] array : arrayList){
            string += array[0][0] + "," + array[0][1] + ":" + array[1][0] + "," + array[1][1] + ";";
        }
        return string;
    }

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
