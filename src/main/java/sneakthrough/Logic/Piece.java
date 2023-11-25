package sneakthrough.Logic;

import java.util.ArrayList;
import java.util.Objects;

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
    //copy constructor
    public Piece(Piece piece) {
        this.color = piece.color;
        this.status = piece.status;
        this.position = new int[] {piece.position[0], piece.position[1]};
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


    public ArrayList<int[]> getValidMoves(Board board){
        String pieceColor = this.getColor();
        int[] piecePosition = this.getPosition();
        ArrayList<int[]> validMoves = new ArrayList<int[]>();

        // if piece is white
        if(pieceColor.equals("white")){
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] - 1, piecePosition[1] - 1};
                // if there is no white piece in the left diagonal or its null
                if(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]] == null || !Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "white")){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] - 1, piecePosition[1] + 1};
                // if there is no white piece in the right diagonal or its null
                if(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]] == null || !Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "white")){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] - 1 >= 0){
                int[] forward = {piecePosition[0] - 1, piecePosition[1]};
                // if there is no white piece in the forward move or its null
                if(board.getGrid()[forward[0]][forward[1]] == null || !Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "white")){
//                    System.out.println("MOVE ADDED TO VALID MOVES FOR WHITE");
                    validMoves.add(forward);
                }
//                // if there is a black piece in the forward move with hidden (false) status
//                else if(board.getGrid()[forward[0]][forward[1]].getStatus() == false){
//                    validMoves.add(forward);
//                }
                // if there is a black piece in the forward move with reviled (true) status
                if(board.getGrid()[forward[0]][forward[1]] != null && (board.getGrid()[forward[0]][forward[1]].getColor().equals("black") && board.getGrid()[forward[0]][forward[1]].getStatus())){
                    // do not add it to valid moves and continue
                    validMoves.remove(forward);
                }
            }
        }

        // if piece is black
        else{
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] + 1, piecePosition[1] - 1};
                // if there is no black piece in the left diagonal or its null
                if(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]] == null || !Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "black") ){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] + 1, piecePosition[1] + 1};
                // if there is no black piece in the right diagonal or its null
                if(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]] == null || !Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "black")){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] + 1 < board.getSize()){
                int[] forward = {piecePosition[0] + 1, piecePosition[1]};
                // if there is no black piece in the forward move or its null
                if(board.getGrid()[forward[0]][forward[1]] == null || !Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "black")){
                    validMoves.add(forward);
                }
//                // if there is a white piece in the forward move with hidden (false) status
//                else if(board.getGrid()[forward[0]][forward[1]].getStatus() == false){
//                    validMoves.add(forward);
//                }
                // if there is a white piece in the forward move with reviled (true) status
                if(board.getGrid()[forward[0]][forward[1]] != null && (board.getGrid()[forward[0]][forward[1]].getColor().equals("white") && board.getGrid()[forward[0]][forward[1]].getStatus())){
                    // do not add it to valid moves and continue
                    validMoves.remove(forward);
                }
            }
        }
        return validMoves;
    }

    public boolean isValidMove(Board board, int[] move){
        ArrayList<int[]> validMoves = this.getValidMoves(board);
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                return true;
            }
        }
        return false;
    }

    // pieces can capture opponent pieces by moving diagonally to the left or right. When a capture is made, the capturing piece is revealed to the opponent.
    // method to check if a move is a capture
    public boolean isCaptureMove(Board board, int[] move){
        ArrayList<int[]> validMoves = this.getValidMoves(board);
        // first check if move is a valid diagonal move
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                // if move is valid, check if there is a piece to capture
                // if piece is white
                if(this.getColor().equals("white")){
                    // if move is left diagonal
                    if(move[0] == this.getPosition()[0] - 1 && move[1] == this.getPosition()[1] - 1){
                        // if there is a black piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == this.getPosition()[0] - 1 && move[1] == this.getPosition()[1] + 1){
                        // if there is a black piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                }
                // if piece is black
                else if(this.getColor().equals("black")){
                    // if move is left diagonal
                    if(move[0] == this.getPosition()[0] + 1 && move[1] == this.getPosition()[1] - 1){
                        // if there is a white piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("white")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == this.getPosition()[0] + 1 && move[1] == this.getPosition()[1] + 1){
                        // if there is a white piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("white")){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //If an orthogonal move is attempted to a spot with a hidden piece of the opponent,
    // the move is not completed and the enemy piece is revealed

    // method to check if a move is an orthogonal move to a hidden piece
    public boolean isRevealMove(Board board, int[] move){
        ArrayList<int[]> validMoves = this.getValidMoves(board);
        // first check if move is a valid orthogonal move
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                // if move is valid, check if there is a hidden piece to reveal
                // if piece is white
                if(this.getColor().equals("white")){
                    // if move is forward
                    if(move[0] == this.getPosition()[0] - 1 && move[1] == this.getPosition()[1]){
                        // if there is no piece (null)
                        if(board.getGrid()[move[0]][move[1]] == null){
                            return false;
                        }
                        // if there is a black piece to reveal
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("black") && board.getGrid()[move[0]][move[1]].getStatus() == false){
                            return true;
                        }
                    }
                }
                // if piece is black
                else if(this.getColor().equals("black")){
                    // if move is forward
                    if(move[0] == this.getPosition()[0] + 1 && move[1] == this.getPosition()[1]){
                        // if there is no piece (null)
                        if(board.getGrid()[move[0]][move[1]] == null){
                            return false;
                        }
                        // if there is a white piece to reveal
                        if(board.getGrid()[move[0]][move[1]].getColor().equals("white") && board.getGrid()[move[0]][move[1]].getStatus() == false){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
