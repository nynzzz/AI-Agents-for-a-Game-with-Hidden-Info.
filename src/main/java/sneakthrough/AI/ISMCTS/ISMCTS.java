package sneakthrough.AI.ISMCTS;

import java.util.ArrayList;

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

    public int[][] runISMCTS(){

        Node_ISMCTS rootNode = new Node_ISMCTS(null, null, rootState.getCurrentPlayer());

        for (int i = 0; i < iterLimit; i++) {

            this.currentNode = rootNode;



        }



    }
}
