package sneakthrough.AI.ISMCTS;

import sneakthrough.Logic.Piece;

import java.util.ArrayList;

public class Determinization {

    private BoardState currentBoardState;



    public Determinization(BoardState currentBoardState) {
        this.currentBoardState = currentBoardState;
    }

//    public BoardState determinize() {
//        BoardState determinized = currentBoardState.clone();
//        int movesMade = determinized.getMoveCount();
//        ArrayList<Piece> oponentsPieces = determinized.getOpponentPieces(determinize().getCurrentPlayer());
//
//
//
//       return null;
//    }




}
