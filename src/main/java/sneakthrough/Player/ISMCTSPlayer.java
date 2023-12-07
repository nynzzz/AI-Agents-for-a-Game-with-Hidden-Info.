package sneakthrough.Player;

import sneakthrough.AI.ISMCTS.BoardState;
import sneakthrough.AI.ISMCTS.ISMCTS;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;

public class ISMCTSPlayer implements Player {

    private String color;
    private int iterationLimit;

    private ArrayList<int[][]> movesDoneSoFar = new ArrayList<>();

    public ISMCTSPlayer(String color, int iterationLimit) {
        this.color = color;
        this.iterationLimit = iterationLimit;
    }

    @Override
    public void makeMove(Board board) {
        // Convert the current board state to a BoardState for ISMCTS
        BoardState currentState = new BoardState(board, this.color);

        // Initialize the ISMCTS algorithm with the current state and iteration limit
        ISMCTS ismcts = new ISMCTS(currentState, iterationLimit);

        // Run the ISMCTS algorithm to get the best move
        int[][] bestMove = ismcts.runISMCTS().getMove();

        // Get the piece to move and the move to make
        Piece piece = board.getGrid()[bestMove[0][0]][bestMove[0][1]];
//        System.out.println("Piece: " + piece.getPosition()[0] + piece.getPosition()[1]);
        int[] move = {bestMove[1][0], bestMove[1][1]};
//        System.out.println("Move: " + move[0] + move[1]);



        // make the move
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

        board.moveCount++;

    }

    public String getColor() {
        return this.color;
    }

    public int getIterationLimit() {
        return this.iterationLimit;
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
