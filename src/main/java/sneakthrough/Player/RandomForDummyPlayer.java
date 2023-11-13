package sneakthrough.Player;

import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RandomForDummyPlayer implements Player{
    private String color;

    private ArrayList<int[][]> dummySequence;

    private int[] dummyPosition;

    public RandomForDummyPlayer(String color, ArrayList<int[][]> dummySequence) {
        this.color = color;
        this.dummySequence = dummySequence;
    }

    public String getColor(){
        return this.color;
    }

    public ArrayList<int[][]> getDummySequence() {
        return this.dummySequence;
    }

    public void setDummySequence(ArrayList<int[][]> dummySequence) {
        this.dummySequence = dummySequence;
    }

    @Override
    public void makeMove(Board board) {
        // get all pieces of the player color
        String playerColor = this.color;
        ArrayList<Piece> pieces = getPlayerPieces(board, playerColor);
        // get a random piece from the pieces
        Piece piece = getRandomPiece(pieces);
        // check if the piece has valid moves
        ArrayList<int[]> validMoves = getValidMoves_againstDummy(board, piece);
        // if valid moves is empty, choose another piece
        while(validMoves.isEmpty()){
            piece = getRandomPiece(pieces);
            validMoves = getValidMoves_againstDummy(board, piece);
        }

//        System.out.println("------------------");
//        System.out.println("Piece position: " + piece.getPosition()[0] + " " + piece.getPosition()[1]);
//        System.out.println("Valid moves: ");
//        for(int[] move : validMoves){
//            System.out.println(move[0] + " " + move[1]);
//        }

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

    public ArrayList<int[]> getValidMoves_againstDummy(Board board, Piece piece){
        String pieceColor = piece.getColor();
        int[] piecePosition = piece.getPosition();
        ArrayList<int[]> validMoves = new ArrayList<int[]>();

        ArrayList<int[]> dummyAllMoves = getAllMoves(this.dummySequence);

        if (!this.dummySequence.isEmpty()) {
            dummyPosition = this.dummySequence.get(0)[0];
        }
        else{
            dummyPosition = new int[]{-1, -1};
        }


//        System.out.println("------------------");
//        System.out.println("lenght of dummy sequence " + this.dummySequence.size());

//        System.out.println("------------------");
//        System.out.println("DUMMY SEQ: ");
//        for(int[][] move : this.dummySequence){
//            System.out.println(move[0][0] + " " + move[0][1] + " " + move[1][0] + " " + move[1][1]);
//        }

//        System.out.println("------------------");
//        System.out.println("Dummy position: " + dummyPosition[0] + " " + dummyPosition[1]);
//
//        System.out.println("------------------");
//        System.out.println("Dummy moves: ");
//        for(int[] move : dummyAllMoves){
//            System.out.println(move[0] + " " + move[1]);
//        }

        // if piece is white
        if(pieceColor.equals("white")){
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] - 1, piecePosition[1] - 1};
                // if there is no white piece in the left diagonal or its null
                if((board.getGrid()[leftDiagonal[0]][leftDiagonal[1]] == null || !Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "white")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, leftDiagonal)) && !Arrays.equals(leftDiagonal, dummyPosition)){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] - 1 >= 0 && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] - 1, piecePosition[1] + 1};
                // if there is no white piece in the right diagonal or its null
                if((board.getGrid()[rightDiagonal[0]][rightDiagonal[1]] == null || !Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "white")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, rightDiagonal)) && !Arrays.equals(rightDiagonal, dummyPosition)){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] - 1 >= 0){
                int[] forward = {piecePosition[0] - 1, piecePosition[1]};
                // if there is no white piece in the forward move or its null
                if((board.getGrid()[forward[0]][forward[1]] == null || !Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "white")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, forward)) && !Arrays.equals(forward, dummyPosition)){
//                    System.out.println("MOVE ADDED TO VALID MOVES FOR WHITE");
                    validMoves.add(forward);
                }
//                // if there is a black piece in the forward move with hidden (false) status
//                else if(board.getGrid()[forward[0]][forward[1]].getStatus() == false){
//                    validMoves.add(forward);
//                }
                // if there is a black piece in the forward move with reviled (true) status
                if(board.getGrid()[forward[0]][forward[1]] != null && (board.getGrid()[forward[0]][forward[1]].getColor().equals("black") && board.getGrid()[forward[0]][forward[1]].getStatus())){
                    // do not add it to valid moves and continue
                    validMoves.remove(forward);
                }
            }
        }

        // if piece is black
        else{
            // there are 3 options to move, 2 diagonals and 1 forward, check if they dont exceed the board
            //left diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] - 1 >= 0){
                int[] leftDiagonal = {piecePosition[0] + 1, piecePosition[1] - 1};
                // if there is no black piece in the left diagonal or its null
                if((board.getGrid()[leftDiagonal[0]][leftDiagonal[1]] == null || !Objects.equals(board.getGrid()[leftDiagonal[0]][leftDiagonal[1]].getColor(), "black")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, leftDiagonal)) && !Arrays.equals(leftDiagonal, dummyPosition)){
                    validMoves.add(leftDiagonal);
                }
            }
            //right diagonal
            if(piecePosition[0] + 1 < board.getSize() && piecePosition[1] + 1 < board.getSize()){
                int[] rightDiagonal = {piecePosition[0] + 1, piecePosition[1] + 1};
                // if there is no black piece in the right diagonal or its null
                if((board.getGrid()[rightDiagonal[0]][rightDiagonal[1]] == null || !Objects.equals(board.getGrid()[rightDiagonal[0]][rightDiagonal[1]].getColor(), "black")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, rightDiagonal)) && !Arrays.equals(rightDiagonal, dummyPosition)){
                    validMoves.add(rightDiagonal);
                }
            }
            //forward
            if(piecePosition[0] + 1 < board.getSize()){
                int[] forward = {piecePosition[0] + 1, piecePosition[1]};
                // if there is no black piece in the forward move or its null
                if((board.getGrid()[forward[0]][forward[1]] == null || !Objects.equals(board.getGrid()[forward[0]][forward[1]].getColor(), "black")) && !dummyAllMoves.stream().anyMatch(move -> Arrays.equals(move, forward)) && !Arrays.equals(forward, dummyPosition)){
                    validMoves.add(forward);
                }
//                // if there is a white piece in the forward move with hidden (false) status
//                else if(board.getGrid()[forward[0]][forward[1]].getStatus() == false){
//                    validMoves.add(forward);
//                }
                // if there is a white piece in the forward move with reviled (true) status
                if(board.getGrid()[forward[0]][forward[1]] != null && (board.getGrid()[forward[0]][forward[1]].getColor().equals("white") && board.getGrid()[forward[0]][forward[1]].getStatus())){
                    // do not add it to valid moves and continue
                    validMoves.remove(forward);
                }
            }
        }
        return validMoves;
    }

   //method that returns a list of orthogonal moves from dummy sequence
    public ArrayList<int[]> getOrthogonalMoves(ArrayList<int[][]> dummySequence) {
            ArrayList<int[]> orthogonalMoves = new ArrayList<int[]>();
            for (int[][] move : dummySequence) {
                int[][] moveToMake = move;
                int[] piecePos = moveToMake[0];
                int[] movePos = moveToMake[1];

                // check if move is orthogonal to the piece position
                if (piecePos[0] == movePos[0] || piecePos[1] == movePos[1]) {
                    orthogonalMoves.add(movePos);
                }
            }
            return orthogonalMoves;
    }

    //method that returns a list of all moves from dummy sequence
    public ArrayList<int[]> getAllMoves(ArrayList<int[][]> dummySequence) {
        ArrayList<int[]> allMoves = new ArrayList<int[]>();
        for (int[][] move : dummySequence) {
            int[][] moveToMake = move;
            int[] piecePos = moveToMake[0];
            int[] movePos = moveToMake[1];

            allMoves.add(movePos);
        }
        return allMoves;
    }

}
