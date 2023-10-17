package sneakthrough.Logic;

public class Piece {
    private String color;
    // true if piece is visible, false if piece is hidden from the opponent
    private boolean status;
    private int[] position;

    public Piece(String color, boolean status, int[] position) {
        this.color = color;
        this.status = status;
        this.position = position;
    }
    public String getColor() {
        return this.color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public int[] getPosition() {
        return position;
    }
    public void setPosition(int[] position) {
        this.position = position;
    }
}
