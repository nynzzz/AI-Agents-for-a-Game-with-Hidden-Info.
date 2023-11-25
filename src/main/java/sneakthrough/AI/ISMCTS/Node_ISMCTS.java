package sneakthrough.AI.ISMCTS;

import java.util.ArrayList;

public class Node_ISMCTS {

    private Node_ISMCTS parent;
    private ArrayList<Node_ISMCTS> children;
    private int[][] move;   // the move that leads to this node from the parent node. Eg [[old_i,old_j],[new_i,new_j]]
    private String player; // player who is doing the move to get to this node, the starting player for root
    private double totalScore; // sum of all simulation results from this node
    private int visits;
    private int considerations;

    public Node_ISMCTS(Node_ISMCTS parent, int[][] move, String player) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.move = move;
        this.player = player;
        this.totalScore = 0.0;
        this.visits = 0;
        this.considerations = 1;
    }

    public Node_ISMCTS getParent() {
        return this.parent;
    }

    public void setParent(Node_ISMCTS parent) {
        this.parent = parent;
    }

    public ArrayList<Node_ISMCTS> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<Node_ISMCTS> children) {
        this.children = children;
    }

    public int[][] getMove() {
        return this.move;
    }

    public void setMove(int[][] move) {
        this.move = move;
    }

    public String getPlayer() {
        return this.player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public double getTotalScore() {
        return this.totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getVisits() {
        return this.visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public int getConsiderations() {
        return this.considerations;
    }

    public void setConsiderations(int considerations) {
        this.considerations = considerations;
    }

    // a method to return untried moves from a list of all possible moves from this node
    public ArrayList<int[][]> getUntriedMoves(ArrayList<int[][]> possibleMoves) {
        ArrayList<int[][]> untriedMoves = new ArrayList<>();
        for (int[][] move : possibleMoves) {
            boolean isUntried = true;
            for (Node_ISMCTS child : this.children) {
                if (child.getMove()[0][0] == move[0][0] && child.getMove()[0][1] == move[0][1] && child.getMove()[1][0] == move[1][0] && child.getMove()[1][1] == move[1][1]) {
                    isUntried = false;
                }
            }
            if (isUntried) {
                untriedMoves.add(move);
            }
        }
        return untriedMoves;
    }

    // a method to return the most promising child of this node (based on UCT)
    public Node_ISMCTS getMostPromisingChild(ArrayList<int[][]> possibleMoves, double explorationRate) {
        Node_ISMCTS mostPromisingChild = null;
        double bestUCT = -1.0;

        for (Node_ISMCTS child : this.children) {
           if(possibleMoves.contains(child.getMove())){
                double UCT = calculateUCT(child, explorationRate);
                if (UCT > bestUCT) {
                    bestUCT = UCT;
                    mostPromisingChild = child;
                }
                child.setConsiderations(child.getConsiderations() + 1);
           }
        }
        return mostPromisingChild;
    }

    // UCT
    private double calculateUCT(Node_ISMCTS node, double explorationRate) {
        return ( node.getTotalScore() / node.getVisits() ) + ( explorationRate * Math.sqrt(Math.log(node.getConsiderations()) / node.getVisits()) );
    }

    // a method to add a child to this node
    public void addChild(int[][] move, String player) {
        Node_ISMCTS child = new Node_ISMCTS(this, move, player);
        this.children.add(child);
    }

    // update method for backprop
    public void update(String winnerPlayer,double score) {
        this.visits++;
        if(winnerPlayer.equals(this.getPlayer())){
            this.totalScore += score;
        }
    }

    // print the node
    public String toString() {
      if(this.parent == null){
        return "ROOT--PLAYER: " + this.getPlayer() + " MOVE: " + this.getMove()[0][0] + ", " + this.getMove()[0][1] + " --> " + this.getMove()[1][0] + ", " + this.getMove()[1][1] + " VISITS: " + this.getVisits() + " TOTAL_SCORE: " + this.getTotalScore();
      }
        else{
            return "PLAYER: " + this.getPlayer() + " MOVE: " + this.getMove()[0][0] + ", " + this.getMove()[0][1] + " --> " + this.getMove()[1][0] + ", " + this.getMove()[1][1] + " VISITS: " + this.getVisits() + " TOTAL_SCORE: " + this.getTotalScore();
        }
    }


}
