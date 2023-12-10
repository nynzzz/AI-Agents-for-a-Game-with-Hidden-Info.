package sneakthrough.AI.MiniMax;

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
    int size ;
    

    public BoardState(Board board, String currentPlayer)
    {
        this.board = board;
        this.grid = board.getGrid();
        this.currentPlayer = currentPlayer;
        this.possibleMoves = findPossibleMoves(currentPlayer);
        this.isGameOver = board.isGameOver();
        this.winner = board.getWinner();
        this.size = 8 ;
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

    public ArrayList<int[][]> getPossibleMoves() {
        return findPossibleMoves(this.currentPlayer);
    }

    public void setPossibleMoves(ArrayList<int[][]> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }

    public int getSize() { return this.size;}

    public Piece getPiece(int i, int j) { return this.grid[i][j];}

    // a method to do a move and update the available moves
    public void makeMove(int[][] move) {
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

    public BoardState clone() {
        // Create a deep clone of the board
        Board clonedBoard = new Board();

        // Clone the grid with deep copy of each Piece
        Piece[][] clonedGrid = new Piece[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.grid[i][j] != null) {
                    clonedGrid[i][j] = new Piece(this.grid[i][j]);
                } else {
                    clonedGrid[i][j] = null;
                }
            }
        }

        // Set the cloned grid in the cloned board
        clonedBoard.setGrid(clonedGrid);

        // Create a new BoardState instance with the cloned board
        BoardState clonedState = new BoardState(clonedBoard, this.currentPlayer);

        clonedState.isGameOver = this.isGameOver;
        clonedState.winner = this.winner;
        clonedState.size = this.size;

        return clonedState;
    }

    public void printBoard(){
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++){
                if(this.grid[i][j] == null){
                    System.out.print("0"+ "  ");
                }
                else{
                    // if piece is white
                    if(this.grid[i][j].getColor().equals("white")){
                        System.out.print("w"+ "  ");
                    }
                    // if piece is black
                    else{
                        System.out.print("b"+ "  ");
                    }
                }
            }
            System.out.println();
        }
    }

}