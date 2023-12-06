package sneakthrough.AI.MiniMaxMain;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import java.util.ArrayList;
import java.util.Arrays;

public class MiniMax{

    //TODO
    // possible moves dont update for white
    // moved piece doesnt appear in the new possible board (getPossibleMoves)
    // problem is after having made a move, for that piece possible moves arent taken in consideration

    public int[][] chooseBestMove(Board board, int depth,String player)
    {
        int bestValue = Integer.MIN_VALUE;
        int[][] bestMove = null ;

        ArrayList<int[][]> possibleMoves = board.getPossibleMoves(player); // Assuming a method to generate possible moves

        System.out.println(player + " Possible Moves: ");
        for (int[][] move : possibleMoves) {
            // Assuming each move array has two elements: the starting and ending position
            int[] startPos = move[0]; // Start position of the move
            int[] endPos = move[1];   // End position of the move

            // Printing the move
            System.out.println("Move from (" + startPos[0] + "," + startPos[1] + ") to (" + endPos[0] + "," + endPos[1] + ")");
        }


        for (int[][] move : possibleMoves) {
            Board newState = board.clone(); // Assuming a clone method in Board
            makeMove(move, newState); // Assuming a method to apply moves to the board

            int moveValue = minimax(newState, depth - 1, false,player);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        //bestMove = new int[][]{new int[]{6,5}, new int[]{4,4}};
        return bestMove;
    }

    // The Minimax function
    private int minimax(Board board, int depth, boolean isMaximizingPlayer, String player)
    {
        if (depth == 0 || board.isGameOver()) { // Assuming a method to check if game is over
            return evaluate(board,player);
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (int[][] move : board.getPossibleMoves(player)) {
                Board newState = board.clone();
                makeMove(move,newState);

                bestValue = Math.max(bestValue, minimax(newState, depth - 1, false,player));
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int[][] move : board.getPossibleMoves(player)) {
                Board newState = board.clone();
                makeMove(move, newState);

                bestValue = Math.min(bestValue, minimax(newState, depth - 1, true,player));
            }
            return bestValue;
        }
    }

    // Evaluate the board state based on Sneakthrough rules
    private int evaluate(Board board, String player) {
        int playerScore = 0;
        int opponentScore = 0;

        // Evaluate the board based on the position of the pieces
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if(board.getPiece(i,j)!= null)
                {
                    String colorPiece = board.getPiece(i, j).getColor();
                    if (colorPiece.equals(player)) {
                        // Add points for player pieces based on their row (proximity to the opponent's side)
                        playerScore += i;
                    } else{
                        opponentScore += (board.getSize() - 1 - i);
                    }
                }
            }
        }

        // Return the overall evaluation score (player's score - opponent's score)
        return playerScore - opponentScore;

    }

        // Placeholder for applying a move to the board
        public Board makeMove(int[][] move, Board board) {

            // Extract the source and destination coordinates for the move
            int x1 = move[0][0];
            int y1 = move[0][1];
            int x2 = move[1][0];
            int y2 = move[1][1];

            // Move the piece from the source square to the destination square on the new board
            Piece pieceToMove = board.getGrid()[x1][y1];
            board.getGrid()[x2][y2] = pieceToMove;
            board.getGrid()[x1][y1] = null;

            return board;
        }

    public static void main(String[] args) {
        Board board = new Board();
        //BoardState state = new BoardState(board, "white");

        int depth = 3;
        MiniMax miniMax = new MiniMax();

        int[][] bestMove = null;
        int[][] bestMoveBlack = null ;

        for(int i = 0; i < 5 ; i++)
        {

            bestMove = miniMax.chooseBestMove(board, depth, "white");
            System.out.println("\nWHITE BEST MOVE TO MAKE: " + Arrays.deepToString(bestMove));

            // Apply the best move to the board
            board = miniMax.makeMove(bestMove, board);

            // Print the updated game board after the AI's move
            System.out.println("\nUpdated Game Board after AI's Move WHITE:");
            board.printBoard();

            bestMoveBlack = miniMax.chooseBestMove(board, depth, "black");
            System.out.println("\nBLACK BEST MOVE TO MAKE: " + Arrays.deepToString(bestMoveBlack));

            // Apply the best move to the board
            board = miniMax.makeMove(bestMoveBlack, board);

            // Print the updated game board after the AI's move
            System.out.println("\nUpdated Game Board after AI's Move BLACK:");
            board.printBoard();

        }
    }
}