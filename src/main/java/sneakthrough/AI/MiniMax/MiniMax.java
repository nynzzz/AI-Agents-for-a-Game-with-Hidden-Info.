package sneakthrough.AI.MiniMax;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import java.util.ArrayList;
import java.util.Arrays;

public class MiniMax {

    public int[][] chooseBestMove(BoardState state, int depth) {
        int bestValue = Integer.MIN_VALUE;
        int[][] bestMove = null;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        ArrayList<int[][]> possibleMoves = state.getPossibleMoves();
        for (int[][] move : possibleMoves) {
            BoardState newState = state.clone();
            newState.makeMove(move);

            int moveValue = minimax(newState, depth - 1, false, alpha, beta);
            if (moveValue > bestValue) {
                bestValue = moveValue;
                bestMove = move;
            }

            alpha = Math.max(alpha, bestValue);
        }
        return bestMove;
    }

    private int minimax(BoardState state, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (depth == 0 || state.getIsGameOver()) {
            return evaluate(state);
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (int[][] move : state.findPossibleMoves(state.getCurrentPlayer())) {
                BoardState newState = state.clone();
                newState.makeMove(move);

                bestValue = Math.max(bestValue, minimax(newState, depth - 1, false, alpha, beta));
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break; // beta cutoff
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int[][] move : state.findPossibleMoves(state.getCurrentPlayer())) {
                BoardState newState = state.clone();
                newState.makeMove(move);

                bestValue = Math.min(bestValue, minimax(newState, depth - 1, true, alpha, beta));
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break; // alpha cutoff
                }
            }
            return bestValue;
        }
    }

    private int evaluate(BoardState state) {
        int score = 0;

        // evaluate progress of pieces towards the opponent side
        score += evaluatePieceProgress(state);

        // evaluate mobility and control of board
        score += evaluateMobilityAndControl(state);

        return score;
    }

    // evaluate progress of the pieces towards the opponent side
    private int evaluatePieceProgress(BoardState state) {
        int score = 0;
        int boardSize = state.getSize();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = state.getPiece(i, j);
                if (piece != null && piece.getColor().equals(state.getCurrentPlayer())) {
                    // reward advancing towards the opponent side
                    int progressScore = (state.getCurrentPlayer().equals("white") ? (boardSize - i) : i);
                    score += progressScore;

                    // keep some pieces for defense on the backline
                    if ((state.getCurrentPlayer().equals("white") && i == 0) ||
                            (state.getCurrentPlayer().equals("black") && i == boardSize - 1)) {
                        score += 2; // backline defense bonus
                    }
                }
            }
        }

        return score;
    }

    // evaluate mobility and control of the board
    private int evaluateMobilityAndControl(BoardState state) {
        int score = 0;
        ArrayList<int[][]> possibleMoves = state.getPossibleMoves();

        for (int[][] move : possibleMoves) {
            int[] from = move[0];
            int[] to = move[1];

            // encourage forward movement
            if ((state.getCurrentPlayer().equals("white") && to[0] > from[0]) ||
                    (state.getCurrentPlayer().equals("black") && to[0] < from[0])) {
                score += 1;
            }

            // control of central columns
            if (to[1] > 1 && to[1] < state.getSize() - 2) {
                score += 1;
            }
        }

        return score;
    }

    public static void main(String[] args) {
        Board board = new Board();
        BoardState state = new BoardState(board,"black");
        MiniMax minimax = new MiniMax();
        int[][] move = minimax.chooseBestMove(state, 3);
        System.out.println(Arrays.deepToString(move));
    }

}