package sneakthrough.GUI;

public class MoveResult {

    private MoveType type;

    public MoveType getType() {
        return type;
    }

    private Pawn piece;

    public Pawn getPiece() {
        return piece;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Pawn piece) {
        this.type = type;
        this.piece = piece;
    }

}
