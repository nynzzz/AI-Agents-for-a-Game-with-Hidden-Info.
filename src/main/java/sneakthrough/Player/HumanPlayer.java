package sneakthrough.Player;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class HumanPlayer implements Player{


    private String color;

    public HumanPlayer(String color) {
        this.color = color;
    }

    @Override
    public void makeMove(Board board, Piece piece, int[] move) {
        // check if move is valid
        if(isValidMove(board, piece, move)){
            // check if move is a capture
            if(isCaptureMove(board, piece, move)){
                Piece capturedPiece = board.getGrid()[move[0]][move[1]];
                // remove captured piece
                board.removeCapturedPiece(capturedPiece);
                // reveal capturing piece to the opponent
                piece.setStatus(true);
            }
            // move piece
            board.getGrid()[move[0]][move[1]] = piece;
            // remove piece from old position
            board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
            // update piece position
            piece.setPosition(move);
        }
    }

    public String getColor() {
        return this.color;
    }

    public ArrayList<int[]> getValidMoves(Board board, Piece piece){
        String pieceColor = piece.getColor();
        int[] piecePosition = piece.getPosition();
        ArrayList<int[]> validMoves = new ArrayList<int[]>();

        // if piece is white
        if(pieceColor.equals("white")){
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] - 1, piecePosition[1] - 1};
                // if there is no white piece in the left diagonal
                if(!Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "white")){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] - 1, piecePosition[1] + 1};
                // if there is no white piece in the right diagonal
                if(!Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "white")){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] - 1 >= 0){
                int[] forward = {piecePosition[0] - 1, piecePosition[1]};
                // if there is no white piece in the forward move
                if(!Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "white")){
                    validMoves.add(forward);
                }
            }
        }

        // if piece is black
        else{
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] + 1, piecePosition[1] - 1};
                // if there is no black piece in the left diagonal
                if(!Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "black")){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] + 1, piecePosition[1] + 1};
                // if there is no black piece in the right diagonal
                if(!Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "black")){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] + 1 < board.getSize()){
                int[] forward = {piecePosition[0] + 1, piecePosition[1]};
                // if there is no black piece in the forward move
                if(!Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "black")){
                    validMoves.add(forward);
                }
            }
        }
        return validMoves;
    }

    public boolean isValidMove(Board board, Piece piece, int[] move){
        ArrayList<int[]> validMoves = getValidMoves(board, piece);
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                return true;
            }
        }
        return false;
    }

    // pieces can capture opponent pieces by moving diagonally to the left or right. When a capture is made, the capturing piece is revealed to the opponent.
    // method to check if a move is a capture
    public boolean isCaptureMove(Board board, Piece piece, int[] move){
        ArrayList<int[]> validMoves = getValidMoves(board, piece);
        // first check if move is a valid diagonal move
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                // if move is valid, check if there is a piece to capture
                // if piece is white
                if(piece.getColor().equals("white")){
                    // if move is left diagonal
                    if(move[0] == piece.getPosition()[0] - 1 && move[1] == piece.getPosition()[1] - 1){
                        // if there is a black piece to capture
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == piece.getPosition()[0] - 1 && move[1] == piece.getPosition()[1] + 1){
                        // if there is a black piece to capture
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                }
                // if piece is black
                else if(piece.getColor().equals("black")){
                    // if move is left diagonal
                    if(move[0] == piece.getPosition()[0] + 1 && move[1] == piece.getPosition()[1] - 1){
                        // if there is a white piece to capture
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("white")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == piece.getPosition()[0] + 1 && move[1] == piece.getPosition()[1] + 1){
                        // if there is a white piece to capture
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("white")){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
