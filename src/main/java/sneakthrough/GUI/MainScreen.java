package sneakthrough.GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sneakthrough.Logic.Board;
import sneakthrough.Logic.Game;
import sneakthrough.Player.HumanPlayer;
import sneakthrough.Player.Player;
import sneakthrough.Player.RandomPlayer;

//TODO
// fill in empty spaces next to the board with e.g. history of movements or anything really useful for the user
// use board instance for creating the board
// use piece instance for the pawns


public class MainScreen extends Application {

    Player whitePlayer ;

    Player blackPlayer ;
    Circle pawn;
    Board mainBoard = new Board() ;
    Rectangle[][] chessMatrix = new Rectangle[8][8] ;
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    final double screenWidth = primaryScreenBounds.getWidth();
    final double screenHeight = primaryScreenBounds.getHeight();
    int boardSize = mainBoard.getSize() ;

    @Override
    public void start(Stage stage) throws Exception{

        stage.setTitle("Sneakthrough game - Group 6");
        Group group = new Group();

        Scene mainMenu = new Scene(group, screenWidth, screenHeight, true);
        mainMenu.setFill(Color.LIGHTBLUE);
        mainMenu.setRoot(group);

        Group gameGroup = new Group();

        Scene gameScene = new Scene(gameGroup, screenWidth, screenHeight);
        gameScene.setFill(Color.LIGHTSEAGREEN);

        GridPane chessboard = new GridPane();
        chessboard.setLayoutX(screenWidth/2 - 280);
        chessboard.setLayoutY(screenHeight/2 -280);

        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++) {
                Rectangle square = new Rectangle(70, 70);
                if ((row + col) % 2 == 0) {
                    square.setFill(Color.WHITE);
                } else {
                    square.setFill(Color.BLACK);
                }
                chessMatrix[row][col] = square;
                chessboard.add(square, col, row);

                if (row == 1 || row == 0)
                {
                    pawn = new Circle(20, Color.BROWN);
                    pawn.setTranslateX(15);
                    chessboard.add(pawn, col , row);
                }

                if (row == 6 || row == 7)
                {
                    pawn = new Circle(20, Color.SANDYBROWN);
                    pawn.setTranslateX(15);
                    chessboard.add(pawn, col , row);
                }

                int finalRow = row;
                int finalCol = col;

                pawn.setOnMouseClicked(e ->
                {
                    System.out.println("Pawn clicked at row " + finalRow + ", column " + finalCol);
                });

            }

        }

        // labels for main menu
        Label gameName = new Label("Sneakthrough");
        //gameName.setPrefSize(300, 100);
        gameName.setTextAlignment(TextAlignment.CENTER);

        Label groupNumber = new Label("Made by Group 6");
       // groupNumber.setPrefSize(300, 80); // Changed from gameName.setPrefSize
        groupNumber.setTextAlignment(TextAlignment.CENTER);

        // Define the Font here
        Font font = Font.font("Helvetica Neue", FontWeight.NORMAL, 30);
        gameName.setFont(font);
        groupNumber.setFont(font);

        gameName.setTextFill(Color.BLACK);
        groupNumber.setTextFill(Color.BLACK);

        gameName.setLayoutX(screenWidth / 2 -100);
        gameName.setLayoutY(screenHeight/6);

        groupNumber.setLayoutX(screenWidth / 2 - 100);
        groupNumber.setLayoutY(screenHeight/6 + 100);

        Label whitePlayerLabel = new Label("White player");
        whitePlayerLabel.setLayoutX(screenWidth / 2 - 250);
        whitePlayerLabel.setLayoutY(screenHeight/2 - 30);
        whitePlayerLabel.setTextAlignment(TextAlignment.CENTER);
        whitePlayerLabel.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 15));

        Label blackPlayerLabel = new Label("Black player");
        blackPlayerLabel.setLayoutX(screenWidth/2 + 50);
        blackPlayerLabel.setLayoutY(screenHeight/2 - 30);
        blackPlayerLabel.setTextAlignment(TextAlignment.CENTER);
        blackPlayerLabel.setFont(Font.font("Helvetica Neue", FontWeight.NORMAL, 15));


        ObservableList<String> playerOptions =
                FXCollections.observableArrayList("Human", "Random Bot");

        ComboBox whitePlayerCB = new ComboBox(playerOptions);
        whitePlayerCB.setPrefWidth(200);
        whitePlayerCB.setLayoutX(screenWidth/2 - 250);
        whitePlayerCB.setLayoutY(screenHeight/2);

        ComboBox blackPlayerCB = new ComboBox(playerOptions);
        blackPlayerCB.setPrefWidth(200);
        blackPlayerCB.setLayoutX(screenWidth/2 + 50);
        blackPlayerCB.setLayoutY(screenHeight/2);

        Button startGameButton = new Button("Start game");
        startGameButton.setPrefWidth(200);
        startGameButton.setLayoutX(screenWidth/2 - 100);
        startGameButton.setLayoutY(screenHeight/2 + 100);

        startGameButton.setOnAction(e ->
        {
            if (whitePlayerCB.getValue() == "Human") whitePlayer = new HumanPlayer("white") ;
            else whitePlayer = new RandomPlayer("white");

            if (blackPlayerCB.getValue() == "Human") blackPlayer = new HumanPlayer("black") ;
            else blackPlayer = new RandomPlayer("black");

            Game game = new Game(mainBoard, whitePlayer, blackPlayer);

            game.startGame();

            stage.setScene(gameScene);


        });

        //seiojofsfsj

        group.getChildren().addAll(gameName,groupNumber,blackPlayerCB,whitePlayerCB,whitePlayerLabel,blackPlayerLabel, startGameButton) ;
        gameGroup.getChildren().addAll(chessboard);

        stage.setScene(mainMenu);
        stage.show();
        stage.setMaximized(true);
        System.out.println(gameName.getPrefWidth());

    }

}