package sneakthrough.Player;

import sneakthrough.AI.MCTS.MCTS;
import sneakthrough.AI.MCTS.Node;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

public class MCTSPlayer implements  Player{

    private String color;

    public MCTSPlayer(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }

    @Override
    public void makeMove(Board board) {
        MCTS mcts = new MCTS(1000, this.color);
        Node bestMoveFound = mcts.searchForBestMove(board);

        System.out.println("best move found - UCT : " + bestMoveFound.getUCT());
        // the array is of the form [piece position][move position]
        int[][] playThatLeadsToThisNodeFromParent = bestMoveFound.getPlayThatLeadsToThisNodeFromParent();
        int[] piecePosition = playThatLeadsToThisNodeFromParent[0];
        System.out.println("best move found - piece pos : " +  piecePosition[0] + "," + piecePosition[1]);
        int[] movePosition = playThatLeadsToThisNodeFromParent[1];
        System.out.println("best move found - move pos : " +  movePosition[0] + "," + movePosition[1]);
        Piece piece = board.getGrid()[piecePosition[0]][piecePosition[1]];
        int[] move = movePosition;

        // check if move is valid
        if(piece.isValidMove(board, move)){

            // check if move is a capture
            if(piece.isCaptureMove(board, move)){
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
            else if(piece.isRevealMove(board, move)){
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
            makeMove(board);
        }
    }
}
