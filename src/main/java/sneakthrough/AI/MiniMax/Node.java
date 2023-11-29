package sneakthrough.AI.MiniMax;
import sneakthrough.Logic.Board;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node parent;
    private List<Node> children;
    private Board state;

    public Node(Node parent, Board state)
    {
        this.parent = parent;
        this.children = new ArrayList<Node>();
        this.state = state;
    }

    public Node getParent(){
        return parent;
    }

    public List<Node> getChildren(){
        return children;
    }

    public Board getState(){
        return state;
    }

    public void setChildren(List<Node> children){
        this.children = children;
    }
}
