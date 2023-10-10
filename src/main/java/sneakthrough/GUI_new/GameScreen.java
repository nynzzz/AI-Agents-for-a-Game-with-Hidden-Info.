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



    public void start(Stage gameStage, String player1Type, String player2Type) {

        whitePlayerType = player1Type;
//        System.out.println("white player type: " + whitePlayerType);
        blackPlayerType = player2Type;
//        System.out.println("black player type: " + blackPlayerType);

        gameStage.setTitle("Game Screen");

        // Create a GridPane to represent the game board
        GridPane gameBoard = new GridPane();

        //HBox to center the game board vertically and horizontally
        HBox centerBox = new HBox();
        centerBox.getChildren().add(gameBoard);
        centerBox.setAlignment(javafx.geometry.Pos.CENTER);
        centerBox.setPadding(new javafx.geometry.Insets(50, 0, 0, 0)); // Add top padding to shift the board down

        // pawn images
        Image whitePawnImage = new Image(getClass().getResourceAsStream("/GUI/white_piece.png"));
        Image blackPawnImage = new Image(getClass().getResourceAsStream("/GUI/black_piece.png"));

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
//                if (whitePlayerType.equals("Human") && blackPlayerType.equals("Human")) {
//                    System.out.println("here");
//                    HumanPlayer whitePlayer = new HumanPlayer("white");
//                    HumanPlayer blackPlayer = new HumanPlayer("black");
//                    // Game
//                    Game game = new Game(board, whitePlayer, blackPlayer);
//                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
//                        @Override
//                        public void handle(ActionEvent event) {
//                            handlePieceButtonClick(whitePlayer,finalRow, finalCol);
//                            //handlePieceButtonClick(blackPlayer,finalRow, finalCol);
//
//                        }
//                    });
//                }
                //event handler to handle clicks on piece buttons

                // if both of the players is a human player
                if (whitePlayerType.equals("Human") && blackPlayerType.equals("Human")) {
                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            //handlePieceButtonClick(row, col);
                            System.out.println("row: " + finalRow + " column: " + finalCol);
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

    private void handlePieceButtonClick(HumanPlayer player,int row, int col) {
        // Check if the clicked cell contains a piece
        if (grid[row][col] != null) {
            // ff no piece is currently selected, set this piece as selected
            if (selectedPiece == null) {
                selectedPiece = grid[row][col];
            } else {
                // If a piece is already selected, consider this click as the target move
                targetMove = new int[]{row, col};
                System.out.println("target move: " + targetMove[0] + " " + targetMove[1]);
            }
            // Update the HumanPlayer's pieceToMove and moveToMake properties
            player.setPieceToMove(selectedPiece);
            player.setMoveToMake(targetMove);

            player.makeMove(board);
            System.out.println("Player " + player.getColor() + " made a move");
        }

//    // update the game board based on logic board
//    private void updateGameScreen() {
//
//
//    }

    }
}
