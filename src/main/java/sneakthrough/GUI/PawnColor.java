package sneakthrough.GUI;

public enum PawnColor {
    BLACK(1), WHITE(-1);

    final int moveDirection;

    PawnColor(int moveDirection) {
        this.moveDirection = moveDirection;
    }
}
