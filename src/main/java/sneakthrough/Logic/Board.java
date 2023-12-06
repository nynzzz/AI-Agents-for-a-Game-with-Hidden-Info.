package sneakthrough.Logic;

import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;

import java.util.ArrayList;

public class Board {
    private Piece[][] grid;
    private int size;
    public int numberWhiteTurns;
    public int numberBlackTurns;

    public int moveCount;

    public Board() {
        this.size = 8;
        this.grid = new Piece[size][size];
        initializeBoard();
        this.numberWhiteTurns = 0;
        this.numberBlackTurns = 0;
        this.moveCount = 0;
    }

    //copy constructor
    public Board(Board board) {
        this.size = board.size;
        this.grid = new Piece[this.size][this.size];

        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (board.grid[i][j] != null) {
                    this.grid[i][j] = new Piece(board.grid[i][j]);
                } else {
                    this.grid[i][j] = null;
                }
            }
        }
    }

    public int getMoveCount() {
        return moveCount;
    }


    private void initializeBoard() {
        // the first two and last two rows are filled with pieces
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++){
                // if upper 2 rows
                if(i < 2){
                    int[] position = {i, j};
                    grid[i][j] = new Piece("black", false, position);
                }
                // if bottom 2 rows
                else if(i > size - 3){
                    int[] position = {i, j};
                    grid[i][j] = new Piece("white", false, position);
                }
                // if middle rows
                else{
                    grid[i][j] = null;
                }
            }
        }
    }

    public int whitePiecesLeft(){
        int whitePiecesLeft = 0;
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++){
                if(grid[i][j] != null && grid[i][j].getColor().equals("white")){
                    whitePiecesLeft++;
                }
            }
        }
        return whitePiecesLeft;
    }

    public int blackPiecesLeft(){
        int blackPiecesLeft = 0;
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++){
                if(grid[i][j] != null && grid[i][j].getColor().equals("black")){
                    blackPiecesLeft++;
                }
            }
        }
        return blackPiecesLeft;
    }

    public void printBoard()
    {
        for (int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++){
                if(grid[i][j] == null){
                    System.out.print("0"+ "  ");
                }
                else{
                    // if piece is white
                    if(grid[i][j].getColor().equals("white")){
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

    public Piece[][] getGrid() {
        return grid;
    }

    public void setGrid(Piece[][] grid) {
        this.grid = grid;
    }

    public int getSize() {
        return size;
    }

    public Piece getPiece(int i, int j)
    {
        return grid[i][j] ;
    }

    public void removeCapturedPiece(Piece piece){
        int[] position = piece.getPosition();
        grid[position[0]][position[1]] = null;
    }

    // get all pieces of a player color
    public ArrayList<Piece> getPlayerPieces(String color){
        ArrayList<Piece> playerPieces = new ArrayList<Piece>();
        int index = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                // if piece is not null and is of the player color
                if(grid[i][j] != null && grid[i][j].getColor().equals(color)){
                    playerPieces.add(grid[i][j]);
                    index++;
                }
            }
        }
        return playerPieces;
    }

    public ArrayList<int[][]> getPossibleMoves(String currentPlayer) {
        ArrayList<Piece> pieces = this.getPlayerPieces(currentPlayer);
        // get all valid moves of the pieces
        ArrayList<int[][]> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            ArrayList<int[]> validMoves = piece.getValidMoves(this);
            for (int[] move : validMoves) {
                int[][] possibleMove = new int[2][2];
                possibleMove[0] = piece.getPosition();
                possibleMove[1] = move;
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;
    }

    public boolean isGameOver() {
        boolean whiteWins = isWhiteWinner();
        boolean blackWins = isBlackWinner();

        // either player reached the other side or has no pieces left
        return whiteWins || blackWins;
    }

    private boolean isBlackWinner() {
        int whitePiecesLeft = 0;
        // check if black has reached the other side
        for (int i = 0; i < size; i++) {
            if (grid[size - 1][i] != null && grid[size - 1][i].getColor().equals("black")) {
                return true;
            }
        }
        // check if white has no pieces left
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size ; j++) {
                if (grid[i][j] != null && grid[i][j].getColor().equals("white")) {
                    whitePiecesLeft++;
                }
            }
        }
        //System.out.println("white pieces left: " + whitePiecesLeft);
        return whitePiecesLeft == 0;
    }

    private boolean isWhiteWinner() {
        int blackPiecesLeft = 0;
        // check if white has reached the other side
        for (int i = 0; i < size; i++) {
            if (grid[0][i] != null && grid[0][i].getColor().equals("white")) {
                return true;
            }
        }
        // check if black has no pieces left
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size ; j++) {
                if (grid[i][j] != null && grid[i][j].getColor().equals("black")) {
                    blackPiecesLeft++;
                }
            }
        }
        //System.out.println("black pieces left: " + blackPiecesLeft);
        return blackPiecesLeft == 0;
    }

    public String getWinner() {
        if(isBlackWinner()){
            return "black";
        }
        else if(isWhiteWinner()){
            return "white";
        }
        else{
            return "none";
        }
    }
    public Board clone()
    {
        Board clonedBoard = new Board(); // Create a new Board object
            clonedBoard.size = this.size;
            clonedBoard.numberWhiteTurns = this.numberWhiteTurns;
            clonedBoard.numberBlackTurns = this.numberBlackTurns;

            // Copy the grid state
            clonedBoard.grid = new Piece[this.size][this.size];
            for (int i = 0; i < this.size; i++) {
                for (int j = 0; j < this.size; j++) {
                    if (this.grid[i][j] != null) {
                        clonedBoard.grid[i][j] = new Piece(this.grid[i][j]); // Copy the Piece objects
                    } else {
                        clonedBoard.grid[i][j] = null;
                    }
                }
            }

            return clonedBoard;
    }

}

