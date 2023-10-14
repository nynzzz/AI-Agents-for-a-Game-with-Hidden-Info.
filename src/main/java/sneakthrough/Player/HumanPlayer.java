package sneakthrough.Player;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class HumanPlayer implements Player{

    private static Piece pieceToMove;
    private static int[] moveToMake;

    private String color;

    public HumanPlayer(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    // ------//
    public void setPieceToMove(Piece piece){
        pieceToMove = piece;
    }
    public Piece getPieceToMove(){
        return pieceToMove;
    }

    public void setMoveToMake(int[] move){
        moveToMake = move;
    }
    public int[] getMoveToMake(){
        return moveToMake;
    }

    // ------//
    @Override
    public void makeMove(Board board) {
        // ask the human player to select a piece to move
        //Piece piece = selectPiece(board);
        Piece piece = this.pieceToMove;
//        if(piece == null){
//            // ask the human player to select a move
////            System.out.println("There is no piece at this (i,j) !!!");
////            piece = selectPiece(board);
//        }
        // ask the human player to select a move
        //int[] move = selectMove();
        int[] move = this.moveToMake;

        // check if move is valid
        if(isValidMove(board, piece, move)){
            // check if move is a capture
            if(isCaptureMove(board, piece, move)){
                System.out.println("Its a capture move");
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
            else if(isRevealMove(board, piece, move)){
                System.out.println("Its a reveal move");
                // reveal piece of the opponent
                board.getGrid()[move[0]][move[1]].setStatus(true);
                //stay at the same position
            }
            // the move is neither a capture or a reveal move
            else{
                System.out.println("Its a normal move");
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
            }
        }
        else{
            // ask the human player to select a move
//            System.out.println("This move is not valid !!!");
            makeMove(board);
        }
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
                    validMoves.add(forward);
                }
//                // if there is a black piece in the forward move with hidden (false) status
//                else if(board.getGrid()[forward[0]][forward[1]].getStatus() == false){
//                    validMoves.add(forward);
//                }
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
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == piece.getPosition()[0] - 1 && move[1] == piece.getPosition()[1] + 1){
                        // if there is a black piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("black")){
                            return true;
                        }
                    }
                }
                // if piece is black
                else if(piece.getColor().equals("black")){
                    // if move is left diagonal
                    if(move[0] == piece.getPosition()[0] + 1 && move[1] == piece.getPosition()[1] - 1){
                        // if there is a white piece to capture
                        if(board.getGrid()[move[0]][move[1]] != null && board.getGrid()[move[0]][move[1]].getColor().equals("white")){
                            return true;
                        }
                    }
                    // if move is right diagonal
                    else if(move[0] == piece.getPosition()[0] + 1 && move[1] == piece.getPosition()[1] + 1){
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
    public boolean isRevealMove(Board board, Piece piece, int[] move){
        ArrayList<int[]> validMoves = getValidMoves(board, piece);
        // first check if move is a valid orthogonal move
        for(int[] validMove : validMoves){
            if(validMove[0] == move[0] && validMove[1] == move[1]){
                // if move is valid, check if there is a hidden piece to reveal
                // if piece is white
                if(piece.getColor().equals("white")){
                    // if move is forward
                    if(move[0] == piece.getPosition()[0] - 1 && move[1] == piece.getPosition()[1]){
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
                else if(piece.getColor().equals("black")){
                    // if move is forward
                    if(move[0] == piece.getPosition()[0] + 1 && move[1] == piece.getPosition()[1]){
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

    //TODO: change with a method in UI
    // method that would ask a player to input the coordinates (i,j) of the piece they want to move
    public Piece selectPiece(Board board){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the piece you want to move (i,j): ");
        String input = scanner.nextLine();
        String[] coordinates = input.split(",");
        int i = Integer.parseInt(coordinates[0]);
        int j = Integer.parseInt(coordinates[1]);
        return board.getGrid()[i][j];
    }

    //TODO: change with a method in UI
    // method that would ask a player to input the coordinates (i,j) of the move they want to make
    public int[] selectMove(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the coordinates of the move you want to make (i,j): ");
        String input = scanner.nextLine();
        String[] coordinates = input.split(",");
        int i = Integer.parseInt(coordinates[0]);
        int j = Integer.parseInt(coordinates[1]);
        int[] move = {i, j};
        return move;
    }

}
