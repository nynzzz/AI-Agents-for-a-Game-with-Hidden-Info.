package sneakthrough.Player;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Objects;

public class RandomPlayer implements Player{

    private String color;

    public RandomPlayer(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }

    @Override
    public void makeMove(Board board) {
        // get all pieces of the player color
        String playerColor = this.color;
        ArrayList<Piece> pieces = getPlayerPieces(board, playerColor);
        // get a random piece from the pieces
        Piece piece = getRandomPiece(pieces);
        // check if the piece has valid moves
        ArrayList<int[]> validMoves = piece.getValidMoves(board);
        // if valid moves is empty, choose another piece
        while(validMoves.isEmpty()){
            piece = getRandomPiece(pieces);
            validMoves = piece.getValidMoves(board);
        }
        // get a random move from the valid moves
        int[] move = getRandomMove(validMoves);
        // check if move is a capture
        if(piece.isCaptureMove(board, move)){
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
        else if(piece.isRevealMove(board, move)){
//            System.out.println("Its a reveal move");
            // reveal piece of the opponent
            board.getGrid()[move[0]][move[1]].setStatus(true);
            //stay at the same position
        }
        // the move is neither a capture or a reveal move
        else{
//            System.out.println("Its a normal move");
            // move piece
            board.getGrid()[move[0]][move[1]] = piece;
            // remove piece from old position
            board.getGrid()[piece.getPosition()[0]][piece.getPosition()[1]] = null;
            // update piece position
            piece.setPosition(move);
        }
    }

    // get pieces of the player color
    public ArrayList<Piece> getPlayerPieces(Board board, String color){
        ArrayList<Piece> playerPieces = new ArrayList<Piece>();
        for(int i = 0; i < board.getSize(); i++){
            for(int j = 0; j < board.getSize(); j++){
                if(board.getGrid()[i][j] != null && board.getGrid()[i][j].getColor().equals(color)){
                    playerPieces.add(board.getGrid()[i][j]);
                }
            }
        }
        return playerPieces;
    }

    // get a random piece to move
    public Piece getRandomPiece(ArrayList<Piece> pieces){
        int randomIndex = (int) (Math.random() * pieces.size());
        return pieces.get(randomIndex);
    }

    // get a random move from the valid moves
    public int[] getRandomMove(ArrayList<int[]> validMoves){
        int randomIndex = (int) (Math.random() * validMoves.size());
        return validMoves.get(randomIndex);
    }

    //check if player has reached the other side
    public boolean hasWon(Board board){
        //if player is white
        if(this.color.equals("white")){
            for(int i = 0; i < board.getSize(); i++){
                if(board.getGrid()[0][i] != null && board.getGrid()[0][i].getColor().equals("white")){
                    return true;
                }
            }
        }
        //if player is black
        else{
            for(int i = 0; i < board.getSize(); i++){
                if(board.getGrid()[board.getSize() - 1][i] != null && board.getGrid()[board.getSize() - 1][i].getColor().equals("black")){
                    return true;
                }
            }
        }
        return false;
    }
}
