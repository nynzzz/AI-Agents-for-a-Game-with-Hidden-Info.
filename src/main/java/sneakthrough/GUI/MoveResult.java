package sneakthrough.GUI;

public class MoveResult {

    private MoveType type;

    public MoveType getType() {
        return type;
    }

    public Pawn pawn;

    public Pawn getPawn() {
        return pawn;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Pawn pawn) {
        this.type = type;
        this.pawn = pawn;
    }

}
