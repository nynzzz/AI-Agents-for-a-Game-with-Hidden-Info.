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

//TODO
// fill in empty spaces next to the board with e.g. history of movements or anything really useful for the user
// use board instance for creating the board
// use piece instance for the pawns


public class MainScreen extends Application {
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

        //single player
        Group singleGroup = new Group();
        Scene blackplayerScene = new Scene(singleGroup, screenWidth, screenHeight);
        blackplayerScene.setFill(Color.BLACK);

        //whitePlayer
        Group whitePlayerGroup = new Group();
        Scene whitePlayerScene = new Scene(whitePlayerGroup, screenWidth, screenHeight);
        whitePlayerScene.setFill(Color.LIGHTSEAGREEN);

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

        Label white = new Label("White player");
        Label black = new Label("Black player");

        white.setLayoutX(screenWidth / 2 - 250);
        white.setLayoutY(screenHeight/2 - 30);

        black.setLayoutX(screenWidth/2 + 50);
        black.setLayoutY(screenHeight/2 - 30);


        //buttons for main menu

        ObservableList<String> playerOptions =
                FXCollections.observableArrayList(
                        "Human",
                        "Random Bot"
                );

        ComboBox whitePlayer = new ComboBox(playerOptions);
        whitePlayer.setPrefWidth(200);
        whitePlayer.setLayoutX(screenWidth/2 - 250);
        whitePlayer.setLayoutY(screenHeight/2);
//        whitePlayer.setOnAction(e ->
//        {
//            HumanPlayer player1 = new HumanPlayer("white");
//            HumanPlayer player2 = new HumanPlayer("black");
//            Game game = new Game(player1,player2);
//            //game.startGame();
//            stage.setScene(whitePlayerScene);
//        });

        ComboBox blackPlayer = new ComboBox(playerOptions);
        blackPlayer.setPrefWidth(200);
        blackPlayer.setLayoutX(screenWidth/2 + 50);
        blackPlayer.setLayoutY(screenHeight/2);
//        blackplayer.setOnAction(e ->
//        {
//            stage.setScene(blackplayerScene);
//        });

//        whitePlayerScene.setOnMouseClicked(e ->
//        {
//            System.out.println();
//        });

        group.getChildren().addAll(gameName,groupNumber,blackPlayer,whitePlayer,white,black) ;
        whitePlayerGroup.getChildren().addAll(chessboard);

        stage.setScene(mainMenu);
        stage.show();
        stage.setMaximized(true);
        System.out.println(gameName.getPrefWidth());

    }

}