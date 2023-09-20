package sneakthrough.GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MainScreen extends Application {

    final int ScreenWIDTH = 1920;
    final int ScreenHEIGHT = 1080;

    final int boardSize = 8;

    @Override
    public void start(Stage stage) throws Exception{

        stage.setTitle("Sneakthrough game - Group 6");

        Group group = new Group();

        Scene mainMenu = new Scene(group, ScreenWIDTH, ScreenHEIGHT, true);
        mainMenu.setFill(Color.LIGHTBLUE);

        //single player
        Group singleGroup = new Group();
        Scene singlePlayerScene = new Scene(singleGroup, ScreenWIDTH, ScreenHEIGHT);
        singlePlayerScene.setFill(Color.BLACK);

        //multiplayer
        Group multiplayerGroup = new Group();
        Scene multiPLayerScene = new Scene(multiplayerGroup, ScreenWIDTH, ScreenHEIGHT);
        multiPLayerScene.setFill(Color.LIGHTSEAGREEN);

        GridPane chessboard = new GridPane();
        chessboard.setLayoutX(570);
        chessboard.setLayoutY(150);

        for (int row = 0; row < boardSize; row++)
        {
            for (int col = 0; col < boardSize; col++) {
                Rectangle square = new Rectangle(100, 100);
                if ((row + col) % 2 == 0) {
                    square.setFill(Color.WHITE);
                } else {
                    square.setFill(Color.BLACK);
                }
                chessboard.add(square, col, row);

                if (row == 1 || row == 0)
                {
                    Circle pawn = new Circle(30, Color.BROWN);
                    pawn.setTranslateX(20);
                    chessboard.add(pawn, col , row);
                }

                if (row == 6 || row == 7)
                {
                    Circle pawn = new Circle(30, Color.SANDYBROWN);
                    pawn.setTranslateX(20);
                    chessboard.add(pawn, col , row);
                }
            }
        }


        // labels for main menu
        Label gameName = new Label("Sneakthrough") ;
        Label groupNumber = new Label("Made by Group 6");

        // Define the Font here
        Font font = Font.font("Helvetica Neue", FontWeight.NORMAL, 30);
        gameName.setFont(font);
        groupNumber.setFont(font);

        gameName.setTextAlignment(TextAlignment.RIGHT);
        groupNumber.setTextAlignment(TextAlignment.RIGHT);

        gameName.setTextFill(Color.BLACK);
        groupNumber.setTextFill(Color.BLACK);

        gameName.setLayoutX(880);
        gameName.setLayoutY(200);

        groupNumber.setLayoutX(860);
        groupNumber.setLayoutY(240);

        //buttons for main menu

        Button multiPlayer = new Button("Multiplayer - Human vs Human");
        multiPlayer.setPrefWidth(200);
        multiPlayer.setLayoutX(880);
        multiPlayer.setLayoutY(390);
        multiPlayer.setOnAction(e ->
        {
            stage.setScene(multiPLayerScene);
        });

        Button singlePlayer = new Button("Single player");
        singlePlayer.setPrefWidth(200);
        singlePlayer.setLayoutX(880);
        singlePlayer.setLayoutY(340);
        singlePlayer.setOnAction(e ->
        {
            stage.setScene(singlePlayerScene);
        });

        group.getChildren().addAll(gameName,groupNumber,singlePlayer,multiPlayer) ;
        multiplayerGroup.getChildren().addAll(chessboard);

        stage.setScene(mainMenu);
        stage.show();
        stage.setMaximized(true);
    }

}
