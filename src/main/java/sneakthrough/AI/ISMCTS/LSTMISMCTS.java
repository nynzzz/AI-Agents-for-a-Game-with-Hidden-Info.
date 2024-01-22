package sneakthrough.AI.ISMCTS;

import java.util.ArrayList;
import java.util.List;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import sneakthrough.ML.RNN.LSTMModelClient;
import sneakthrough.ML.RNN.ModelPrediction;

public class LSTMISMCTS{
    private BoardState rootState;
    private BoardState currentState;
    private ArrayList<int[][]> possibleMoves;
    private Node_ISMCTS currentNode;

    private LSTMModelClient lstmModelClient;

    private int iterLimit;
    private double EXPL;


    public LSTMISMCTS(BoardState rootState, int iterLimit, double EXPL) {
        this.rootState = rootState;
        this.iterLimit = iterLimit;
        this.EXPL = EXPL;
        lstmModelClient = new LSTMModelClient();
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

            int[][] winingMove = rootState.makeWinningMoveIfExists();
            if(winingMove != null){
                Node_ISMCTS winningNode = new Node_ISMCTS(rootNode, winingMove, rootState.getCurrentPlayer());
//                System.out.println("child visits: " + rootNode.printChildrenToStringFlattened());
//                System.out.println("move to take: " + winningNode.printMoveToStringFlattened());
                return winningNode;
            }

            this.currentState = rootState.determinize();
            this.possibleMoves = currentState.getPossibleMoves();

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

//        System.out.println("child visits: " + rootNode.printChildrenToStringFlattened());
//        System.out.println("move to take: " + best.printMoveToStringFlattened());

        return best;
    }



    private void selectChild(){
        while((!possibleMoves.isEmpty() && !this.currentState.getIsGameOver()) && this.currentNode.getUntriedMoves(this.possibleMoves).isEmpty()){
            String boardStateSequence = this.currentState.toStringFlattened();
            ModelPrediction prediction = lstmModelClient.getPrediction(boardStateSequence);
            double adjustedEXPL = adjustExploration(EXPL, prediction);
            
            this.currentNode = this.currentNode.getMostPromisingChild(this.possibleMoves, adjustedEXPL);
            this.currentState.doMove(this.currentNode.getMove());
            this.possibleMoves = this.currentState.getPossibleMoves();
        }
    }

    

    private double adjustExploration(double originalExploration, ModelPrediction prediction) {
        final double WIN_PROBABILITY_THRESHOLD = 0.7; 
        final double EXPLORATION_BOOST_FACTOR = 1.5; 
    
        if (prediction.getWinProbability() > WIN_PROBABILITY_THRESHOLD) {
            return originalExploration * EXPLORATION_BOOST_FACTOR;
        }
    
        final double LOSS_PROBABILITY_THRESHOLD = 0.7; 
        final double EXPLORATION_REDUCTION_FACTOR = 0.5; 
        if (prediction.getLossProbability() > LOSS_PROBABILITY_THRESHOLD) {
            return originalExploration * EXPLORATION_REDUCTION_FACTOR;
        }
    
        return originalExploration;
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

}