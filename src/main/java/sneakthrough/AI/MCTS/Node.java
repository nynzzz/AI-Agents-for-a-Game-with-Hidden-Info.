package sneakthrough.AI.MCTS;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {

    private Node parent;
    private ArrayList<Node> children;
    private double winRatio;
    private int visits;
    private float UCTValue;
    private int depth;

    private boolean isRoot;

    // the play that leads to this node (board state). Eg. [piece position, move position]
    private int[][] playThatLeadsToThisNodeFromParent;

    // sequence of plays from the root node to this node
    private ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot;


    // exploration parameter
    private final double c = 1.41;

    public Node(Node parent) {
        this.parent = parent;
        this.children = new ArrayList<>();
        this.winRatio = 0.0;
        this.visits = 0;
        this.UCTValue = 0;
        this.depth = parent.getDepth() + 1;
        this.isRoot = false;
        this.playThatLeadsToThisNodeFromParent = new int[2][2];
        this.sequenceOfPlaysThatLeadsToThisNodeFromRoot = new ArrayList<>();
    }

    // root node constructor
    public Node() {
        this.parent = null;
        this.children = new ArrayList<>();
        this.winRatio = 0.0;
        this.visits = 0;
        this.UCTValue = 0;
        this.depth = 0;
        this.isRoot = true;
        this.playThatLeadsToThisNodeFromParent = new int[2][2];
        this.sequenceOfPlaysThatLeadsToThisNodeFromRoot = new ArrayList<>();
    }

    public void setPlayThatLeadsToThisNodeFromParent(int[][] play) {
        this.playThatLeadsToThisNodeFromParent = play;
    }

    public int[][] getPlayThatLeadsToThisNodeFromParent() {
        return this.playThatLeadsToThisNodeFromParent;
    }

  public void setSequenceOfPlaysThatLeadsToThisNodeFromRoot(Node parent) {
        if (!parent.isRoot) {
            this.sequenceOfPlaysThatLeadsToThisNodeFromRoot.addAll(parent.getSequenceOfPlaysThatLeadsToThisNodeFromRoot());
            this.sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(this.getPlayThatLeadsToThisNodeFromParent());
        }
        else {
            this.sequenceOfPlaysThatLeadsToThisNodeFromRoot.add(this.getPlayThatLeadsToThisNodeFromParent());
        }
    }

    public void setSequenceOfPlaysThatLeadsToThisNodeFromRoot_CUSTOM(ArrayList<int[][]> sequenceOfPlaysThatLeadsToThisNodeFromRoot) {
        this.sequenceOfPlaysThatLeadsToThisNodeFromRoot = sequenceOfPlaysThatLeadsToThisNodeFromRoot;
    }

    public ArrayList<int[][]> getSequenceOfPlaysThatLeadsToThisNodeFromRoot() {
        return this.sequenceOfPlaysThatLeadsToThisNodeFromRoot;
    }

    public int getDepth() {
        return this.depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public void addChild(Node child) {
        this.children.add(child);
        child.setParent(this);
    }

    public void removeChild(Node child) {
        this.children.remove(child);
        child.setParent(null);
    }

    public ArrayList<Node> getChildren() {
        return this.children;
    }

    public void updateWinRatio(double winRatio) {
        this.winRatio += winRatio;
        if (this.visits > 0 ) {
            this.winRatio = this.winRatio / this.visits;
        }
    }

    public void updateVisits() {
        this.visits += 1;
    }

    public double getWinRatio() {
        return this.winRatio;
    }

    public int getVisits() {
        return this.visits;
    }

    public void updateStats(){
        this.updateVisits();
        this.updateWinRatio(this.winRatio);
    }

    public void calculateUCTValue() {
        this.UCTValue = calculateUCT();
    }

    public float getUCTValue() {
        return this.UCTValue;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return this.parent;
    }

    // calculate UCT value
    public float calculateUCT() {
        return (float) (this.getWinRatio() / this.getVisits() + c * Math.sqrt(Math.log(this.getParent().getVisits()) / this.getVisits()));
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean containsMove(int[][] move) {
        // current positions taken from root
        ArrayList<int[]> currentPositionsTakenFromRoot = new ArrayList<>();
        ArrayList<int[]> currentMovesTakenFromRoot = new ArrayList<>();
        ArrayList<int[][]> currentPlaysTakenFromRoot = this.sequenceOfPlaysThatLeadsToThisNodeFromRoot;
        for (int[][] play : currentPlaysTakenFromRoot) {
            currentPositionsTakenFromRoot.add(play[0]);
            currentMovesTakenFromRoot.add(play[1]);
        }
        // if move is in current positions taken from root
        if (currentPositionsTakenFromRoot.contains(move[0])) {
            return true;
        }
        // if a move was already been taken to the same position and not manipulated later
        if (currentMovesTakenFromRoot.contains(move[1]) && !currentPositionsTakenFromRoot.contains(move[1])) {
            return true;
        }
        return false;
    }

    public String getUCT() {
        return String.format("%.2f", this.UCTValue);
    }

    public void setUCT(double uct) {
        this.UCTValue = (float) uct;
    }
}
