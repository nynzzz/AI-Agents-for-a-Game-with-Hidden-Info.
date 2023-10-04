package sneakthrough.GUI;

public enum PawnColor {
    BLACK(1), WHITE(-1);

    final int moveDir;

    PawnColor(int moveDir) {
        this.moveDir = moveDir;
    }
}
