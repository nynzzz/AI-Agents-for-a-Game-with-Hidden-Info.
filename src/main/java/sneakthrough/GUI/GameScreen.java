package sneakthrough.GUI;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;
import sneakthrough.AI.MiniMaxMain.MiniMax;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Piece;
import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.MiniMaxPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class GameScreen {
    private Piece selectedPiece;
    private int[] targetMove;
    String whitePlayerType;
    String blackPlayerType;
    private boolean isFinished = false ;
    private Button changeTurn = new Button("Make move");
    private final int screen_width = 1200;
    private final int screen_height = 800;
    private final Font font = Font.font("Arial", 24);
    private Board board = new Board();
    private int boardSize = board.getSize();
    private Piece[][] grid = board.getGrid();
    private boolean isWhiteTurn = true;
    private GridPane gameBoard = new GridPane();
    Image whitePawnImage = new Image(getClass().getResourceAsStream("/GUI/white_piece.png"));
    Image blackPawnImage = new Image(getClass().getResourceAsStream("/GUI/black_piece.png"));
    private Timeline gameTimer;
    private int elapsedTimeInSeconds;
    private Label timeLabel;
    private ArrayList<Piece> revealArray = new ArrayList<>();
    private Label whitePiecesCountLabel;
    private Label blackPiecesCountLabel;

    public void start(Stage gameStage, String player1Type, String player2Type) {


        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedTimeInSeconds++;
            updateTimerLabel();
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);

        gameTimer.play() ;

        timeLabel = new Label("Time: 00:00");
        timeLabel.setLayoutX(1025); //+25
        timeLabel.setLayoutY(500); //+50
        timeLabel.setStyle("-fx-font: 24px 'Arial';");


        gameBoard.setLayoutX(screen_width/2 - 320);
        gameBoard.setLayoutY(screen_height/2 - 300);

        changeTurn.setLayoutX(1000);
        changeTurn.setLayoutY(550);
        changeTurn.setPrefWidth(200);
        changeTurn.setStyle("-fx-font: 24px 'Arial';");

        whitePlayerType = player1Type;
        blackPlayerType = player2Type;

        gameStage.setTitle("Sneakthrough : " + player1Type + " vs " + player2Type);

        HBox centerBox = new HBox();
        centerBox.getChildren().add(gameBoard);
        centerBox.setAlignment(javafx.geometry.Pos.CENTER);
        centerBox.setPadding(new javafx.geometry.Insets(50, 0, 0, 0));

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                ImageView pieceImageView = new ImageView();
                pieceImageView.setFitWidth(65);
                pieceImageView.setFitHeight(65);
                // check if Piece
                if (grid[row][col] != null) {
                    // if piece is white
                    if (grid[row][col].getColor().equals("white")) {
                        pieceImageView.setImage(whitePawnImage);
                    }
                    // if piece is black
                    else if (grid[row][col].getColor().equals("black")){
                        pieceImageView.setImage(blackPawnImage);
                        pieceImageView.setVisible(false);
                    }
                    else pieceImageView.setImage(null);
                }

                // button with the pawn image as graphic
                Button cellButton = new Button("", pieceImageView);
                cellButton.setMinSize(80, 80);
                // background colors
                if ((row + col) % 2 == 0) {
                    cellButton.setStyle("-fx-background-color: #FFFFCC;"); // Light yellow
                } else {
                    cellButton.setStyle("-fx-background-color: #FFCC99;"); // Light orange
                }

                int finalRow = row;
                int finalCol = col;

                // if both of the players are a human player
                if (whitePlayerType.equals("Human") && blackPlayerType.equals("Human")) {

                    // create 2 human players
                    HumanPlayer whitePlayer = new HumanPlayer("white");
                    HumanPlayer blackPlayer = new HumanPlayer("black");
                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event){

                            if(!isFinished)
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

                                        for (Node node : gameBoard.getChildren())
                                        {
                                            if (node instanceof Button)
                                            {
                                                Button button = (Button) node;
                                                ImageView imgView = (ImageView)button.getGraphic();
                                                if (imgView != null && imgView.getImage() == whitePawnImage)
                                                {
                                                    button.setDisable(true);
                                                }
                                            }
                                        }

                                        if(whitePlayer.getMoveToMake()[0] == 0)
                                        {
                                            for (Node node : gameBoard.getChildren())
                                            {
                                                if (node instanceof Button)
                                                {
                                                    Button button = (Button) node ;
                                                    ImageView imgView = (ImageView)button.getGraphic();
                                                    imgView.setVisible(true);
                                                }
                                            }

                                            isFinished = true ;
                                            Alert win = new Alert(Alert.AlertType.INFORMATION);
                                            win.setHeaderText("GAME OVER!");
                                            win.setContentText("Player " + whitePlayer.getColor() + " has won!");
                                            win.setTitle("Game finished");
                                            //font from css
                                            win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                            win.getDialogPane().getStyleClass().add("dialog-pane");
                                            //win.showAndWait();

                                            Optional<ButtonType> result = win.showAndWait();
                                            if (result.get() == ButtonType.OK){
                                                gameStage.close();
                                            }
                                            if(result.get()==ButtonType.CANCEL){
                                                gameStage.close();
                                            }

                                            gameTimer.stop();
                                            //changeTurn.setDisable(true);

                                        }

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

                                        for (Node node : gameBoard.getChildren())
                                        {
                                            if (node instanceof Button)
                                            {
                                                Button button = (Button) node;
                                                ImageView imgView = (ImageView)button.getGraphic();
                                                if (imgView != null && imgView.getImage() == blackPawnImage)
                                                {
                                                    button.setDisable(true);
                                                }
                                            }
                                        }

                                        if(blackPlayer.getMoveToMake()[0] == 7)
                                        {

                                            for (Node node : gameBoard.getChildren())
                                            {
                                                if (node instanceof Button)
                                                {
                                                    Button button = (Button) node ;
                                                    ImageView imgView = (ImageView)button.getGraphic();
                                                    imgView.setVisible(true);
                                                }
                                            }

                                            isFinished = true ;
                                            Alert win = new Alert(Alert.AlertType.INFORMATION);
                                            win.setHeaderText("GAME OVER!");
                                            win.setContentText("Player " + blackPlayer.getColor() + " has won!");
                                            win.setTitle("Game finished");
                                            //font from css
                                            win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                            win.getDialogPane().getStyleClass().add("dialog-pane");
                                            //win.showAndWait();
                                            gameTimer.stop();

                                            Optional<ButtonType> result = win.showAndWait();
                                            if (result.get() == ButtonType.OK){
                                                gameStage.close();
                                            }
                                            if(result.get()==ButtonType.CANCEL){
                                                gameStage.close();
                                            }

                                            //changeTurn.setDisable(true);
                                        }
                                    }
                                }

                        }
                    });
                }
                // if player 1 is Human and Player 2 is Random or vice versa
                if(whitePlayerType.equals("Human") && blackPlayerType.equals("Random") || whitePlayerType.equals("Random") && blackPlayerType.equals("Human")){

                    // create 1 human player and 1 random player
                    HumanPlayer humanPlayer = new HumanPlayer("white");
                    Player randomPlayer = new RandomPlayer("black");
                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (selectedPiece == null) {
                                // Assign this piece as pieceToMove for the human player if its his (white) turn
                                if (isWhiteTurn) {
                                    selectedPiece = grid[finalRow][finalCol];
                                    humanPlayer.setPieceToMove(selectedPiece);
                                    System.out.println("white player piece to move: " + Arrays.toString(humanPlayer.getPieceToMove().getPosition()));
                                }
                                else{
                                    randomPlayer.makeMove(board);
                                    updateBoardScreen(board);
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("black player made a move");

                                    // check if random player has won
                                    if(((RandomPlayer) randomPlayer).hasWon(board)){
                                        isFinished = true ;
                                        Alert win = new Alert(Alert.AlertType.INFORMATION);
                                        win.setHeaderText("GAME OVER!");
                                        win.setContentText("Player " + ((RandomPlayer) randomPlayer).getColor() + " has won!");
                                        win.setTitle("Game finished");
                                        //font from css
                                        win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                        win.getDialogPane().getStyleClass().add("dialog-pane");
                                        //win.showAndWait();

                                        Optional<ButtonType> result = win.showAndWait();
                                        if (result.get() == ButtonType.OK){
                                            gameStage.close();
                                        }
                                        if(result.get()==ButtonType.CANCEL){
                                            gameStage.close();
                                        }

                                        gameTimer.stop();
                                        //changeTurn.setDisable(true);
                                    }
                                }
                            }
                            else {
                                if (isWhiteTurn) {
                                    humanPlayer.setMoveToMake(new int[]{finalRow, finalCol});
                                    System.out.println("white player move to make: " + Arrays.toString(humanPlayer.getMoveToMake()));

                                    System.out.println("PIECE " + Arrays.toString(humanPlayer.getPieceToMove().getPosition()));
                                    System.out.println("MOVE " + Arrays.toString(humanPlayer.getMoveToMake()));

                                    humanPlayer.makeMove(board);
                                    updateBoardScreen(board);

                                    selectedPiece = null;
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("white player made a move");

                                    for (Node node : gameBoard.getChildren())
                                    {
                                        if (node instanceof Button)
                                        {
                                            Button button = (Button) node;
                                            ImageView imgView = (ImageView)button.getGraphic();
                                            if (imgView != null && imgView.getImage() == whitePawnImage)
                                            {
                                                button.setDisable(true);
                                            }
                                        }
                                    }

                                    if(humanPlayer.getMoveToMake()[0] == 0)
                                    {
                                        for (Node node : gameBoard.getChildren())
                                        {
                                            if (node instanceof Button)
                                            {
                                                Button button = (Button) node ;
                                                ImageView imgView = (ImageView)button.getGraphic();
                                                imgView.setVisible(true);
                                            }
                                        }

                                        isFinished = true ;
                                        Alert win = new Alert(Alert.AlertType.INFORMATION);
                                        win.setHeaderText("GAME OVER!");
                                        win.setContentText("Player " + humanPlayer.getColor() + " has won!");
                                        win.setTitle("Game finished");
                                        //font from css
                                        win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                        win.getDialogPane().getStyleClass().add("dialog-pane");
                                        //win.showAndWait();

                                        Optional<ButtonType> result = win.showAndWait();
                                        if (result.get() == ButtonType.OK){
                                            gameStage.close();
                                        }
                                        if(result.get()==ButtonType.CANCEL){
                                            gameStage.close();
                                        }
                                        gameTimer.stop();
                                        //changeTurn.setDisable(true);
                                    }

                                } else {
                                    randomPlayer.makeMove(board);
                                    updateBoardScreen(board);

                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("black player made a move");

                                    for (Node node : gameBoard.getChildren())
                                    {
                                        if (node instanceof Button)
                                        {
                                            Button button = (Button) node;
                                            ImageView imgView = (ImageView)button.getGraphic();
                                            if (imgView != null && imgView.getImage() == blackPawnImage)
                                            {
                                                button.setDisable(true);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });


                }

                if(whitePlayerType.equals("Human") && blackPlayerType.equals("MiniMax") || whitePlayerType.equals("MiniMax") && blackPlayerType.equals("Human"))
                {
                    HumanPlayer humanPlayer = new HumanPlayer("white");
                    MiniMaxPlayer minimaxPlayer = new MiniMaxPlayer("black");
                    cellButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (selectedPiece == null) {
                                // Assign this piece as pieceToMove for the human player if its his (white) turn
                                if (isWhiteTurn) {
                                    selectedPiece = grid[finalRow][finalCol];
                                    humanPlayer.setPieceToMove(selectedPiece);
                                    System.out.println("white player piece to move: " + Arrays.toString(humanPlayer.getPieceToMove().getPosition()));
                                }
                                else{
                                    minimaxPlayer.makeMove(board);
                                    updateBoardScreen(board);
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("black player made a move");

                                    // check if random player has won
                                    if(minimaxPlayer.hasWon(board)){
                                        isFinished = true ;
                                        Alert win = new Alert(Alert.AlertType.INFORMATION);
                                        win.setHeaderText("GAME OVER!");
                                        win.setContentText("Player " + minimaxPlayer.getPlayer() + " has won!");
                                        win.setTitle("Game finished");
                                        //font from css
                                        win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                        win.getDialogPane().getStyleClass().add("dialog-pane");
                                        //win.showAndWait();

                                        Optional<ButtonType> result = win.showAndWait();
                                        if (result.get() == ButtonType.OK){
                                            gameStage.close();
                                        }
                                        if(result.get()==ButtonType.CANCEL){
                                            gameStage.close();
                                        }

                                        gameTimer.stop();
                                        //changeTurn.setDisable(true);
                                    }
                                }
                            }
                            else {
                                if (isWhiteTurn) {
                                    humanPlayer.setMoveToMake(new int[]{finalRow, finalCol});
                                    System.out.println("white player move to make: " + Arrays.toString(humanPlayer.getMoveToMake()));

                                    System.out.println("PIECE " + Arrays.toString(humanPlayer.getPieceToMove().getPosition()));
                                    System.out.println("MOVE " + Arrays.toString(humanPlayer.getMoveToMake()));

                                    humanPlayer.makeMove(board);
                                    updateBoardScreen(board);

                                    selectedPiece = null;
                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("white player made a move");

                                    for (Node node : gameBoard.getChildren())
                                    {
                                        if (node instanceof Button)
                                        {
                                            Button button = (Button) node;
                                            ImageView imgView = (ImageView)button.getGraphic();
                                            if (imgView != null && imgView.getImage() == whitePawnImage)
                                            {
                                                button.setDisable(true);
                                            }
                                        }
                                    }

                                    if(humanPlayer.getMoveToMake()[0] == 0)
                                    {
                                        for (Node node : gameBoard.getChildren())
                                        {
                                            if (node instanceof Button)
                                            {
                                                Button button = (Button) node ;
                                                ImageView imgView = (ImageView)button.getGraphic();
                                                imgView.setVisible(true);
                                            }
                                        }

                                        isFinished = true ;
                                        Alert win = new Alert(Alert.AlertType.INFORMATION);
                                        win.setHeaderText("GAME OVER!");
                                        win.setContentText("Player " + humanPlayer.getColor() + " has won!");
                                        win.setTitle("Game finished");
                                        //font from css
                                        win.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
                                        win.getDialogPane().getStyleClass().add("dialog-pane");
                                        //win.showAndWait();

                                        Optional<ButtonType> result = win.showAndWait();
                                        if (result.get() == ButtonType.OK){
                                            gameStage.close();
                                        }
                                        if(result.get()==ButtonType.CANCEL){
                                            gameStage.close();
                                        }
                                        gameTimer.stop();
                                        //changeTurn.setDisable(true);
                                    }

                                } else {
                                    minimaxPlayer.makeMove(board);
                                    updateBoardScreen(board);

                                    isWhiteTurn = !isWhiteTurn;
                                    System.out.println("black player made a move");

                                    for (Node node : gameBoard.getChildren())
                                    {
                                        if (node instanceof Button)
                                        {
                                            Button button = (Button) node;
                                            ImageView imgView = (ImageView)button.getGraphic();
                                            if (imgView != null && imgView.getImage() == blackPawnImage)
                                            {
                                                button.setDisable(true);
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    });
                }
                gameBoard.add(cellButton, col, row);
            }
        }

        changeTurn.setOnAction(e -> {
            for (Node node : gameBoard.getChildren()) {

                if (node instanceof Button) {
                    node.setDisable(false);
                    Button button = (Button) node;
                    ImageView imgView = (ImageView) button.getGraphic();
                    int row = GridPane.getRowIndex(button);
                    int col = GridPane.getColumnIndex(button);

                    if (row >= 0 && col >= 0) {
                        Piece piece = grid[row][col];
                        if (piece != null)
                        { if (revealArray.contains(piece)) {
                            imgView.setVisible(true);
                        }else{
                            // Check the color of the piece on the logical board
                            if ((piece.getColor().equals("white") && isWhiteTurn) || (piece.getColor().equals("black") && piece.getStatus() && isWhiteTurn)) {
                                imgView.setVisible(true);
                            } else if ((piece.getColor().equals("black") && !isWhiteTurn) || (piece.getColor().equals("white") && piece.getStatus() && !isWhiteTurn)) {
                                imgView.setVisible(true);
                            } else{
                                imgView.setVisible(false);
                            }
                        }
                        }
                    }
                }
            }
        });

        // scene for the game screen
        Scene gameScene = new Scene(centerBox, screen_width, screen_height);

        Group gameGroup = new Group();
        gameScene.setRoot(gameGroup);
        gameGroup.getChildren().addAll(gameBoard,changeTurn,timeLabel);

        gameStage.setScene(gameScene);
        gameStage.show();
    }

    // helper methods

    private void updateTimerLabel() {
        int minutes = elapsedTimeInSeconds / 60;
        int seconds = elapsedTimeInSeconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
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