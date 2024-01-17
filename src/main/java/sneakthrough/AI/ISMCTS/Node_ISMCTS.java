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


    // a method to print the nodes move as flattened string in format "i_old,j_old;i_new,j_new"
    public String printMoveToStringFlattened(){
        String moveString = "";
        moveString += this.move[0][0] + "," + this.move[0][1] + ":" + this.move[1][0] + "," + this.move[1][1];
        return moveString;
    }

    // a method to print the nodes children as flattened string in format "i1_old,j1_old:i1_new,j1_new,count1;..."
    public String printChildrenToStringFlattened(){
        String childrenString = "";
        for (Node_ISMCTS child : this.children) {
            childrenString += child.printMoveToStringFlattened() + "," + child.getVisits() + ";";
        }
        return childrenString;
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
//            System.out.println("children size: " + this.children.size());
//            System.out.println("child move: " + child.getMove()[0][0] + ", " + child.getMove()[0][1] + " --> " + child.getMove()[1][0] + ", " + child.getMove()[1][1]);
           if(containsArray(possibleMoves, child.getMove())){
//               System.out.println("here");
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

    // a method to add a child to this node and return it
    public Node_ISMCTS addChild(int[][] move, String player) {
        Node_ISMCTS child = new Node_ISMCTS(this, move, player);
        this.children.add(child);
        return child;
    }

    // update method for backprop
    public void update(String winnerPlayer) {
        this.visits++;
        if(winnerPlayer.equals(this.getPlayer())){
            this.totalScore += 1;
        }
    }

    // print the node
    public String toString() {
      if(this.parent == null){
        return "ROOT--PLAYER: " + this.getPlayer() + " MOVE: " + "null" + ","+ " VISITS: " + this.getVisits() + " TOTAL_SCORE: " + this.getTotalScore();
      }
        else{
            return "PLAYER: " + this.getPlayer() + " MOVE: " + this.getMove()[0][0] + ", " + this.getMove()[0][1] + " --> " + this.getMove()[1][0] + ", " + this.getMove()[1][1] + " VISITS: " + this.getVisits() + " TOTAL_SCORE: " + this.getTotalScore();
        }
    }




    // ---------helpers----------//

    public boolean containsArray(ArrayList<int[][]> list, int[][] arrayToFind) {
        for (int[][] array : list) {
            if (arraysEqual(array, arrayToFind)) {
                return true;
            }
        }
        return false;
    }

    private boolean arraysEqual(int[][] array1, int[][] array2) {
        if (array1.length != array2.length) {
            return false;
        }

        for (int i = 0; i < array1.length; i++) {
            if (array1[i].length != array2[i].length) {
                return false;
            }

            for (int j = 0; j < array1[i].length; j++) {
                if (array1[i][j] != array2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }



}
