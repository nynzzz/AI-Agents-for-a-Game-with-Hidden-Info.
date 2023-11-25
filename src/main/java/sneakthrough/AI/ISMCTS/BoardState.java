package sneakthrough.AI.ISMCTS;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;

public class BoardState {

    private Board board;

    private Piece[][] grid;
    private String currentPlayer;
    private ArrayList<int[][]> possibleMoves;
    private boolean isGameOver;
    private String winner;

    public BoardState(Board board, String currentPlayer) {
        this.board = board;
        this.grid = board.getGrid();
        this.currentPlayer = currentPlayer;
        this.possibleMoves = findPossibleMoves(currentPlayer);
        this.isGameOver = board.isGameOver();
        this.winner = board.getWinner();
    }

    public Piece[][] getGrid() {
        return this.grid;
    }

    public void setGrid(Piece[][] grid) {
        this.grid = grid;
    }

    public String getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public ArrayList<int[][]> getPossibleMoves() {
        return this.possibleMoves;
    }

    public void setPossibleMoves(ArrayList<int[][]> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    // a method to do a move and update the available moves
    public void doMove(int[][] move) {
        int[] from = move[0];
        int[] to = move[1];
        Piece piece = this.grid[from[0]][from[1]];
        this.grid[from[0]][from[1]] = null;
        this.grid[to[0]][to[1]] = piece;
        piece.setPosition(to);

        // update available moves
        this.possibleMoves = findPossibleMoves(this.currentPlayer);
    }

    // method to get find available moves
    public ArrayList<int[][]> findPossibleMoves(String currentPlayer) {
        ArrayList<Piece> pieces = this.board.getPlayerPieces(currentPlayer);
        // get all valid moves of the pieces
        ArrayList<int[][]> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            ArrayList<int[]> validMoves = piece.getValidMoves(this.board);
            for (int[] move : validMoves) {
                int[][] possibleMove = new int[2][2];
                possibleMove[0] = piece.getPosition();
                possibleMove[1] = move;
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;
    }



}
