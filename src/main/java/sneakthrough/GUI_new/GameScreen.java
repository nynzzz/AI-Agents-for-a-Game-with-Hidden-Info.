package sneakthrough.GUI_new;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Logic.Piece;
import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;

import java.util.Arrays;

public class GameScreen {

    private Piece selectedPiece;
    private int[] targetMove;

    String whitePlayerType;
    String blackPlayerType;

    private final int screen_width = 800;
    private final int screen_height = 600;
    private final Font font = Font.font("Arial", 24);

    // Create a new board
    private Board board = new Board();
    // Get the board size
    private int boardSize = board.getSize();
    // Get the board grid
    private Piece[][] grid = board.getGrid();

    private boolean isWhiteTurn = true;

    // Create a GridPane to represent the game board
    private GridPane gameBoard = new GridPane();

    // pawn images
    Image whitePawnImage = new Image(getClass().getResourceAsStream("/GUI/white_piece.png"));
    Image blackPawnImage = new Image(getClass().getResourceAsStream("/GUI/black_piece.png"));



    public void start(Stage gameStage, String player1Type, String player2Type) {

        whitePlayerType = player1Type;
//        System.out.println("white player type: " + whitePlayerType);
        blackPlayerType = player2Type;
//        System.out.println("black player type: " + blackPlayerType);

        gameStage.setTitle("Game Screen");


        //HBox to center the game board vertically and horizontally
        HBox centerBox = new HBox();
        centerBox.getChildren().add(gameBoard);
        centerBox.setAlignment(javafx.geometry.Pos.CENTER);
        centerBox.setPadding(new javafx.geometry.Insets(50, 0, 0, 0)); // Add top padding to shift the board down


        // Create buttons with pawn images and a different background color
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                ImageView pieceImageView = new ImageView();
                pieceImageView.setFitWidth(50);
                pieceImageView.setFitHeight(50);
                // check if Piece
                if (grid[row][col] != null) {
                    // if piece is white
                    if (grid[row][col].getColor().equals("white")) {
                        pieceImageView.setImage(whitePawnImage);
                    }
                    // if piece is black
                    else {
                        pieceImageView.setImage(blackPawnImage);
                    }
                }

                // button with the pawn image as graphic
                Button cellButton = new Button("", pieceImageView);
                cellButton.setMinSize(50, 50);
                // background colors
                if ((row + col) % 2 == 0) {
                    cellButton.setStyle("-fx-background-color: #FFFFCC;"); // Light yellow
                } else {
                    cellButton.setStyle("-fx-background-color: #FFCC99;"); // Light orange
                }


                int finalRow = row;
                int finalCol = col;

                // if both of the players is a human player
                if (whitePlayerType.equals("Human") && blackPlayerType.equals("Human")) {

                    // create 2 human players
                    HumanPlayer whitePlayer = new HumanPlayer("white");
                    HumanPlayer blackPlayer = new HumanPlayer("black");
                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (selectedPiece == null) {
                                // Assign this piece as pieceToMove for the current player
                                if (isWhiteTurn) {
                                    selectedPiece = grid[finalRow][finalCol];
                                    whitePlayer.setPieceToMove(selectedPiece);
                                    System.out.println("white player piece to move: " + Arrays.toString(whitePlayer.getPieceToMove().getPosition()));
                                } else {
                                    selectedPiece = grid[finalRow][finalCol];
                                    blackPlayer.setPieceToMove(selectedPiece);
                                    System.out.println("black player piece to move: " + Arrays.toString(blackPlayer.getPieceToMove().getPosition()));
                                }
                            } else {
                                if (isWhiteTurn) {
                                    whitePlayer.setMoveToMake(new int[]{finalRow, finalCol});
                                    System.out.println("white player move to make: " + Arrays.toString(whitePlayer.getMoveToMake()));

                                    System.out.println("PIECE " + Arrays.toString(whitePlayer.getPieceToMove().getPosition()));
                                    System.out.println("MOVE " + Arrays.toString(whitePlayer.getMoveToMake()));

                                    whitePlayer.makeMove(board);
                                    updateBoardScreen(board);
                                    selectedPiece = null;
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("white player made a move");

                                } else {
                                    blackPlayer.setMoveToMake(new int[]{finalRow, finalCol});
                                    System.out.println("black player move to make: " + Arrays.toString(blackPlayer.getMoveToMake()));

                                    System.out.println("PIECE " + Arrays.toString(blackPlayer.getPieceToMove().getPosition()));
                                    System.out.println("MOVE " + Arrays.toString(blackPlayer.getMoveToMake()));

                                    blackPlayer.makeMove(board);
                                    updateBoardScreen(board);
                                    selectedPiece = null;
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("black player made a move");
                                }
                            }
                        }
                    });
                }
            gameBoard.add(cellButton, col, row);
        }
    }


        // scene for the game screen
        Scene gameScene = new Scene(centerBox, screen_width, screen_height);
        gameStage.setScene(gameScene);
        gameStage.show();
    }


    // helper methods

    // method to assign the selected piece and target move, perform the move, update the board
    private void handlePieceButtonClick(HumanPlayer player, int row, int col) {
        System.out.println("player: " + player.getColor() + " moves");

        // Check if the clicked cell contains a piece
        if (player.getPieceToMove() == null) {
            if (grid[row][col] != null && grid[row][col].getColor().equals(player.getColor())) {
                // Set the selected piece
                selectedPiece = grid[row][col];
                // Assign the piece
                player.setPieceToMove(selectedPiece);
                System.out.println("Piece selected: " + selectedPiece.getColor() + " " + selectedPiece.getPosition()[0] + " " + selectedPiece.getPosition()[1]);
            } else {
                // Handle the case when the user clicks an empty cell without a selected piece
                System.out.println("No piece selected or invalid piece selected.");
            }
        } else {
            // If a piece is already selected, this click represents a target move
            targetMove = new int[]{row, col};
            System.out.println("TARGET MOVE: " + targetMove[0] + " " + targetMove[1]);
            player.setMoveToMake(targetMove);
        }
        player.makeMove(board);
        // Update the game board based on the logic board
        updateBoardScreen(board);
    }



        private void updateBoardScreen (Board board){
            Piece[][] updatedGrid = board.getGrid();

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    Button cellButton = (Button) gameBoard.getChildren().get(row * boardSize + col);
                    ImageView pieceImageView = (ImageView) cellButton.getGraphic();

                    if (updatedGrid[row][col] != null) {
                        // If there's a piece on the logical board, update the image
                        if (updatedGrid[row][col].getColor().equals("white")) {
                            pieceImageView.setImage(whitePawnImage);
                        } else {
                            pieceImageView.setImage(blackPawnImage);
                        }
                    } else {
                        // If there's no piece, clear the image
                        pieceImageView.setImage(null);
                    }
                }
            }
        }
}
