package sneakthrough.Player;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DummyPlayer implements Player{

    // this player follows the sequence of moves given in the constructor
    // after the sequence is over, it makes random moves

    private List<int[][]> sequenceOfMoves;

    private String color;

    private RandomPlayer randomPlayer;

    public DummyPlayer(ArrayList<int[][]> sequenceOfMoves, String color) {
        this.color = color;
        this.randomPlayer = new RandomPlayer(color);
        this.sequenceOfMoves = sequenceOfMoves.stream().map(move -> move.clone()).collect(Collectors.toList());
    }

    @Override
    public void makeMove(Board board) {

        // makes the move from the sequence until its over
        if (!this.sequenceOfMoves.isEmpty()) {
            int[][] moveToMake = this.sequenceOfMoves.get(0);
            int[] piecePos = moveToMake[0];
//            System.out.println("PIECE POS: " + piecePos[0] + "," + piecePos[1]);
            Piece piece = board.getGrid()[piecePos[0]][piecePos[1]];
            int[] move = moveToMake[1];
//            System.out.println("move: " + move[0] + "," + move[1]);

//            System.out.println("sequence length: " + this.sequenceOfMoves.size());
//            System.out.println("piece pos: " + piecePos[0] + "," + piecePos[1]);
//            System.out.println("move: " + move[0] + "," + move[1]);

            // check if move is a capture
            if (piece.isCaptureMove(board, move)) {
//            System.out.println("Its a capture move");
                Piece capturedPiece = board.getGrid()[move[0]][move[1]];
                // remove captured piece
                board.removeCapturedPiece(capturedPiece);
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
                // reveal capturing piece to the opponent
                piece.setStatus(true);
                this.sequenceOfMoves.remove(0);
            }
            // check if move is a reveal move
            // since dummy player must follow the action sequence, it is illegal to interrupt a sequence with a reveal move
            // we need to move the piece to this position anyway
            // if there is an opponent piece, it will be moved to another position without being revealed
            else if (piece.isRevealMove(board, move)) {
//            System.out.println("Its a reveal move");


                this.sequenceOfMoves.remove(0);
            }
            // the move is neither a capture or a reveal move
            else {
//            System.out.println("Its a normal move");
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
                this.sequenceOfMoves.remove(0);
            }
//            this.sequenceOfMoves.remove(0);
        }

        // ---------- THE RANDOM COMES HERE -------------
        // if the sequence is over, make random moves

        else {
            // get all pieces of the player color
            String playerColor = this.color;
            ArrayList<Piece> pieces = randomPlayer.getPlayerPieces(board, playerColor);
            // get a random piece from the pieces
            Piece piece = randomPlayer.getRandomPiece(pieces);
            // check if the piece has valid moves
            ArrayList<int[]> validMoves = piece.getValidMoves(board);
            // if valid moves is empty, choose another piece
            while (validMoves.isEmpty()) {
                piece = randomPlayer.getRandomPiece(pieces);
                validMoves = piece.getValidMoves(board);
            }
            // get a random move from the valid moves
            int[] move = randomPlayer.getRandomMove(validMoves);
            // check if move is a capture
            if (piece.isCaptureMove(board, move)) {
                //            System.out.println("Its a capture move");
                Piece capturedPiece = board.getGrid()[move[0]][move[1]];
                // remove captured piece
                board.removeCapturedPiece(capturedPiece);
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
                // reveal capturing piece to the opponent
                piece.setStatus(true);
            }
            // check if move is a reveal move
            else if (piece.isRevealMove(board, move)) {
                //            System.out.println("Its a reveal move");
                // reveal piece of the opponent
                board.getGrid()[move[0]][move[1]].setStatus(true);
                //stay at the same position
            }
            // the move is neither a capture or a reveal move
            else {
                //            System.out.println("Its a normal move");
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
            }
        }
    }
}
