package sneakthrough.GUI;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainScreen extends Application {

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    final double screenWidth = primaryScreenBounds.getWidth();
    final double screenHeight = primaryScreenBounds.getHeight();
    final int boardSize = 8;

    @Override
    public void start(Stage stage) throws Exception{

        stage.setTitle("Sneakthrough game - Group 6");



        Group group = new Group();

        Scene mainMenu = new Scene(group, screenWidth, screenHeight, true);
        mainMenu.setFill(Color.LIGHTBLUE);
        mainMenu.setRoot(group);

        //single player
        Group singleGroup = new Group();
        Scene singlePlayerScene = new Scene(singleGroup, screenWidth, screenHeight);
        singlePlayerScene.setFill(Color.BLACK);

        //multiplayer
        Group multiplayerGroup = new Group();
        Scene multiPLayerScene = new Scene(multiplayerGroup, screenWidth, screenHeight);
        multiPLayerScene.setFill(Color.LIGHTSEAGREEN);

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
                chessboard.add(square, col, row);

                if (row == 1 || row == 0)
                {
                    Circle pawn = new Circle(20, Color.BROWN);
                    pawn.setTranslateX(15);
                    chessboard.add(pawn, col , row);
                }

                if (row == 6 || row == 7)
                {
                    Circle pawn = new Circle(20, Color.SANDYBROWN);
                    pawn.setTranslateX(15);
                    chessboard.add(pawn, col , row);
                }
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

        //buttons for main menu

        Button multiPlayer = new Button("Multiplayer - Human vs Human");
        multiPlayer.setPrefWidth(200);
        multiPlayer.setLayoutX(screenWidth/2 - 250);
        multiPlayer.setLayoutY(screenHeight/2);
        multiPlayer.setOnAction(e ->
        {
            stage.setScene(multiPLayerScene);
        });

        Button singlePlayer = new Button("Single player");
        singlePlayer.setPrefWidth(200);
        singlePlayer.setLayoutX(screenWidth/2 + 50);
        singlePlayer.setLayoutY(screenHeight/2);
        singlePlayer.setOnAction(e ->
        {
            stage.setScene(singlePlayerScene);
        });

        group.getChildren().addAll(gameName,groupNumber,singlePlayer,multiPlayer) ;
        multiplayerGroup.getChildren().addAll(chessboard);

        stage.setScene(mainMenu);
        stage.show();
        stage.setMaximized(true);
        System.out.println(gameName.getPrefWidth());

    }

}