package sneakthrough.Player;

import javafx.scene.control.Alert;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class HumanPlayer implements Player{

    private static Piece pieceToMove;
    private static int[] moveToMake;

    public String moveType ;

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

        Piece piece = pieceToMove;
        int[] move = moveToMake;

        // check if move is valid
        if(piece.isValidMove(board, move)){

//            //print valid moves
//            System.out.println("Valid moves are : ");
//            ArrayList<int[]> validMoves = getValidMoves(board, piece);
//            for(int[] validMove : validMoves){
//                System.out.println(validMove[0] + "," + validMove[1]);
//            }

            // check if move is a capture
            if(piece.isCaptureMove(board, move)){
                System.out.println("Its a capture move");
                moveType = "capture";
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
            else if(piece.isRevealMove(board, move)){
                System.out.println("Its a reveal move");
                moveType = "reveal" ;
                // reveal piece of the opponent
                board.getGrid()[move[0]][move[1]].setStatus(true);
                //stay at the same position
            }
            // the move is neither a capture or a reveal move
            else{
                System.out.println("Its a normal move");
                moveType = "normal";
                // move piece
                board.getGrid()[move[0]][move[1]] = piece;
                // remove piece from old position
                board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
                // update piece position
                piece.setPosition(move);
            }
        }
        else{
            makeMove(board);
        }
    }

    // this is if you wanna play in terminal
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
