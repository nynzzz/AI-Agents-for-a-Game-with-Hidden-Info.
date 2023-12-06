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

    public int[][] chooseBestMove(BoardState state, int depth)
    {
        int bestValue = Integer.MIN_VALUE;
        int[][] bestMove = null ;

        ArrayList<int[][]> possibleMoves = state.getPossibleMoves(); // Assuming a method to generate possible moves

        System.out.println(state.getCurrentPlayer() + " possible moves: ");
        for (int[][] move : possibleMoves) {
            // Assuming each move array has two elements: the starting and ending position
            int[] startPos = move[0]; // Start position of the move
            int[] endPos = move[1];   // End position of the move

            // Printing the move
            System.out.println("Move from (" + startPos[0] + "," + startPos[1] + ") to (" + endPos[0] + "," + endPos[1] + ")");
        }


        for (int[][] move : possibleMoves) {
            BoardState newState = state.clone(); // Assuming a clone method in Board
            newState.makeMove(move); // Assuming a method to apply moves to the board

            int moveValue = minimax(newState, depth - 1, false);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }
        }
        //bestMove = new int[][]{new int[]{6,5}, new int[]{4,4}};
        return bestMove;
    }

    // The Minimax function
    private int minimax(BoardState state, int depth, boolean isMaximizingPlayer)
    {
        if (depth == 0 || state.getIsGameOver()) { // Assuming a method to check if game is over
            return evaluate(state);
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (int[][] move : state.findPossibleMoves(state.getCurrentPlayer())) {
                BoardState newState = state.clone();
                newState.makeMove(move);

                bestValue = Math.max(bestValue, minimax(newState, depth - 1, false));
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int[][] move : state.findPossibleMoves(state.getCurrentPlayer())) {
                BoardState newState = state.clone();
                newState.makeMove(move);

                bestValue = Math.min(bestValue, minimax(newState, depth - 1, true));
            }
            return bestValue;
        }
    }

    // Evaluate the board state based on Sneakthrough rules
    private int evaluate(BoardState state) {
        int score = 0;

        // Evaluate the progress of pieces towards the opponent's side
        score += evaluatePieceProgress(state);

        // Evaluate mobility and control of the board
        score += evaluateMobilityAndControl(state);

        return score;
    }

    // Evaluate the progress of the pieces towards the opponent's side
    private int evaluatePieceProgress(BoardState state) {
        int score = 0;
        int boardSize = state.getSize();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = state.getPiece(i, j);
                if (piece != null && piece.getColor().equals(state.getCurrentPlayer())) {
                    // Reward advancing towards the opponent's side
                    int progressScore = (state.getCurrentPlayer().equals("white") ? (boardSize - i) : i);
                    score += progressScore;

                    // Keep some pieces for defense on the backline
                    if ((state.getCurrentPlayer().equals("white") && i == 0) ||
                            (state.getCurrentPlayer().equals("black") && i == boardSize - 1)) {
                        score += 2; // Backline defense bonus
                    }
                }
            }
        }

        return score;
    }

    // Evaluate mobility and control of the board
    private int evaluateMobilityAndControl(BoardState state) {
        int score = 0;
        ArrayList<int[][]> possibleMoves = state.getPossibleMoves();

        for (int[][] move : possibleMoves) {
            int[] from = move[0];
            int[] to = move[1];

            // Encourage forward movement
            if ((state.getCurrentPlayer().equals("white") && to[0] > from[0]) ||
                    (state.getCurrentPlayer().equals("black") && to[0] < from[0])) {
                score += 1;
            }

            // Control of central columns
            if (to[1] > 1 && to[1] < state.getSize() - 2) {
                score += 1;
            }
        }

        return score;
    }

    // Placeholder for applying a move to the board
//        public Board makeMove(int[][] move, Board board) {
//
//            // Extract the source and destination coordinates for the move
//            int x1 = move[0][0];
//            int y1 = move[0][1];
//            int x2 = move[1][0];
//            int y2 = move[1][1];
//
//            // Move the piece from the source square to the destination square on the new board
//            Piece pieceToMove = board.getGrid()[x1][y1];
//            board.getGrid()[x2][y2] = pieceToMove;
//            board.getGrid()[x1][y1] = null;
//
//            return board;
//        }

    public static void main(String[] args) {
        Board board = new Board();
        BoardState whiteState = new BoardState(board, "white");
        BoardState blackState = new BoardState(board, "black");

        int depth = 3;
        MiniMax miniMax = new MiniMax();

        int[][] bestMove = null;
        int[][] bestMoveBlack = null ;

        for(int i = 0; i < 5 ; i++)
        {

            bestMove = miniMax.chooseBestMove(whiteState, depth);
            System.out.println("\nWHITE BEST MOVE TO MAKE: " + Arrays.deepToString(bestMove));

            // Apply the best move to the board
            whiteState.makeMove(bestMove);

            // Print the updated game board after the AI's move
            System.out.println("\nUpdated Game Board after AI's Move WHITE:");
            whiteState.printBoard();

            bestMoveBlack = miniMax.chooseBestMove(blackState, depth);
            System.out.println("\nBLACK BEST MOVE TO MAKE: " + Arrays.deepToString(bestMoveBlack));

            // Apply the best move to the board
            blackState.makeMove(bestMoveBlack);

            // Print the updated game board after the AI's move
            System.out.println("\nUpdated Game Board after AI's Move BLACK:");
            blackState.printBoard();

        }
    }
}