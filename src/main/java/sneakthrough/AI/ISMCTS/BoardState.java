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
    private int moveCount;

    public BoardState(Board board, String currentPlayer) {
        this.board = board;
        this.grid = board.getGrid();
        this.currentPlayer = currentPlayer;
        this.possibleMoves = findPossibleMoves(currentPlayer);
        this.isGameOver = board.isGameOver();
        this.winner = board.getWinner();
        this.moveCount = board.getMoveCount();
    }


    // determinization method for black. for moveCount times do a random move for black
    public BoardState determinize() {

        // create a copy of the grid
        Piece[][] determinizedGrid = new Piece[this.grid.length][this.grid.length];
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                if (this.grid[i][j] != null) {
                    determinizedGrid[i][j] = new Piece(this.grid[i][j]);
                } else {
                    determinizedGrid[i][j] = null;
                }
            }
        }

        // if the current player is white, keep white pieces at their current position and put black pieces at their starting position
        if (this.currentPlayer.equals("white")) {
            for (int i = 0; i < determinizedGrid.length; i++) {
                for (int j = 0; j < determinizedGrid.length; j++) {
                    if (determinizedGrid[i][j] != null && determinizedGrid[i][j].getColor().equals("black")) {
                        // remove the piece from this position
                        determinizedGrid[i][j] = null;
                    }
                }
            }
            // fill the first 2 rows with black pieces
            for (int i = 0; i < determinizedGrid.length; i++) {
                for (int j = 0; j < determinizedGrid.length; j++) {
                    if (i < 2) {
                        int[] position = {i, j};
                        determinizedGrid[i][j] = new Piece("black", false, position);
                    }
                }
            }

            // create a new board with the determinized grid
            Board determinizedBoard = new Board();
            determinizedBoard.setGrid(determinizedGrid);

            // for moveCount times do a random move for black
            for (int i = 0; i < this.moveCount; i++) {
                // get a random piece of black
                ArrayList<Piece> blackPieces = determinizedBoard.getPlayerPieces("black");
                int randomIndex = (int) (Math.random() * blackPieces.size());
                Piece randomBlackPiece = blackPieces.get(randomIndex);
                // get a random valid move for this piece
                ArrayList<int[]> validMoves = randomBlackPiece.getValidMoves(determinizedBoard);
                // while valid moves are empty, get a new random piece
                while (validMoves.isEmpty()) {
                    randomIndex = (int) (Math.random() * blackPieces.size());
                    randomBlackPiece = blackPieces.get(randomIndex);
                    validMoves = randomBlackPiece.getValidMoves(determinizedBoard);
                }
                // get a random move
                int randomMoveIndex = (int) (Math.random() * validMoves.size());
                int[] randomMove = validMoves.get(randomMoveIndex);
                // do the move
                int[] from = randomBlackPiece.getPosition();
                int[] to = randomMove;
                Piece piece = determinizedGrid[from[0]][from[1]];
                determinizedGrid[from[0]][from[1]] = null;
                determinizedGrid[to[0]][to[1]] = piece;
                piece.setPosition(to);
            }
            return new BoardState(determinizedBoard, this.currentPlayer);
        }

        // if the current player is black, keep black pieces at their current position and put white pieces at their starting position
        else {
            for (int i = 0; i < determinizedGrid.length; i++) {
                for (int j = 0; j < determinizedGrid.length; j++) {
                    if (determinizedGrid[i][j] != null && determinizedGrid[i][j].getColor().equals("white")) {
                        // remove the piece from this position
                        determinizedGrid[i][j] = null;
                    }
                }
            }
            // fill the last 2 rows with white pieces
            for (int i = 0; i < determinizedGrid.length; i++) {
                for (int j = 0; j < determinizedGrid.length; j++) {
                    if (i > 5) {
                        int[] position = {i, j};
                        determinizedGrid[i][j] = new Piece("white", false, position);
                    }
                }
            }

            // create a new board with the determinized grid
            Board determinizedBoard = new Board();
            determinizedBoard.setGrid(determinizedGrid);

            // for moveCount + 1 times do a random move for white
            for (int i = 0; i < this.moveCount+1; i++) {
                // get a random piece of white
                ArrayList<Piece> whitePieces = determinizedBoard.getPlayerPieces("white");
                int randomIndex = (int) (Math.random() * whitePieces.size());
                Piece randomWhitePiece = whitePieces.get(randomIndex);
                // get a random valid move for this piece
                ArrayList<int[]> validMoves = randomWhitePiece.getValidMoves(determinizedBoard);
                // while valid moves are empty, get a new random piece
                while (validMoves.isEmpty()) {
                    randomIndex = (int) (Math.random() * whitePieces.size());
                    randomWhitePiece = whitePieces.get(randomIndex);
                    validMoves = randomWhitePiece.getValidMoves(determinizedBoard);
                }
                // get a random move
                int randomMoveIndex = (int) (Math.random() * validMoves.size());
                int[] randomMove = validMoves.get(randomMoveIndex);
                // do the move
                int[] from = randomWhitePiece.getPosition();
                int[] to = randomMove;
                Piece piece = determinizedGrid[from[0]][from[1]];
                determinizedGrid[from[0]][from[1]] = null;
                determinizedGrid[to[0]][to[1]] = piece;
                piece.setPosition(to);
            }
            return new BoardState(determinizedBoard, this.currentPlayer);
        }
    }


    // method to check if there is a win condition, if yes return the move that leads to it
    public int[][] makeWinningMoveIfExists(){
        // if player is white, check if there is a white piece at row 1
        if(this.currentPlayer.equals("white")) {
            for (int i = 0; i < this.grid.length; i++) {
                if (this.grid[1][i] != null && this.grid[1][i].getColor().equals("white")) {
                    // check if there is a valid diagonal move to row 0
                    ArrayList<int[]> validMoves = this.grid[1][i].getValidMoves(this.board);
                    for (int[] move : validMoves) {
                        if (move[0] == 0) {
                            int[][] winningMove = new int[2][2];
                            winningMove[0] = this.grid[1][i].getPosition();
                            winningMove[1] = move;
                            return winningMove;
                        }
                    }
                }
            }
        }
        // if player is black, check if there is a black piece at row 6
        else{
            for (int i = 0; i < this.grid.length; i++) {
                if (this.grid[6][i] != null && this.grid[6][i].getColor().equals("black")) {
                    // check if there is a valid diagonal move to row 7
                    ArrayList<int[]> validMoves = this.grid[6][i].getValidMoves(this.board);
                    for (int[] move : validMoves) {
                        if (move[0] == 7) {
                            int[][] winningMove = new int[2][2];
                            winningMove[0] = this.grid[6][i].getPosition();
                            winningMove[1] = move;
                            return winningMove;
                        }
                    }
                }
            }
        }
        return null;
    }



    // board state to string
    public void StatetoString(){
        Board board = new Board();
        board.setGrid(this.grid);
        board.printBoard();
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
        return findPossibleMoves(this.currentPlayer);
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

    public int getMoveCount() {
        return this.moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
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
        this.moveCount++;
    }

    // method to get find available moves
    public ArrayList<int[][]> findPossibleMoves(String currentPlayer) {
        ArrayList<Piece> pieces = this.board.getPlayerPieces(currentPlayer);

        // get all valid moves of the pieces
        ArrayList<int[][]> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            ArrayList<int[]> validMoves = piece.getValidMoves(this.board);
            for (int[] move : validMoves) {
//                System.out.println("valid move: " + move[0] + move[1]);
                int[][] possibleMove = new int[2][2];
                possibleMove[0] = piece.getPosition();
                possibleMove[1] = move;
                possibleMoves.add(possibleMove);
            }
        }
        return possibleMoves;
    }


    // method to return opponents pieces
    public ArrayList<Piece> getOpponentPieces(String currentPlayer) {
        ArrayList<Piece> opponentPieces = new ArrayList<>();
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid.length; j++) {
                Piece piece = this.grid[i][j];
                if (piece != null && !piece.getColor().equals(currentPlayer)) {
                    opponentPieces.add(piece);
                }
            }
        }
        return opponentPieces;
    }

}
