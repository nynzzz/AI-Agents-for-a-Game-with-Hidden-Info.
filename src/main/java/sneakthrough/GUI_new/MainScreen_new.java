package sneakthrough.GUI_new;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.util.Objects;

public class MainScreen_new extends Application {

    //Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    //final double screenWidth = primaryScreenBounds.getWidth();
    final double screenWidth = 800;

    //final double screenHeight = primaryScreenBounds.getHeight();
    final double screenHeight = 600;
    private final Font font = Font.font("Arial", 24);

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Sneakthrough game - Group 6");
        Group mainMenuGroup = new Group();

        Scene mainMenu = new Scene(mainMenuGroup, screenWidth, screenHeight, true);
        mainMenu.setFill(Color.SANDYBROWN);
        mainMenu.setRoot(mainMenuGroup);

        //labels for main menu
        Label gameName = new Label("Sneakthrough");
        gameName.setTextAlignment(TextAlignment.CENTER);

        Label groupNumber = new Label("Made by Group 6");
        groupNumber.setTextAlignment(TextAlignment.CENTER);

        // Define the Font here
        Font font = Font.font("Arial", FontWeight.NORMAL, 30);
        gameName.setFont(font);
        groupNumber.setFont(font);

        gameName.setTextFill(Color.BLACK);
        groupNumber.setTextFill(Color.BLACK);

        gameName.setLayoutX(screenWidth / 2 -100);
        gameName.setLayoutY(screenHeight/6);

        groupNumber.setLayoutX(screenWidth / 2 - 100);
        groupNumber.setLayoutY(screenHeight/6 + 100);

        // combo boxes for player selection
        ComboBox<String> player1ComboBox = new ComboBox<>();
        player1ComboBox.getItems().addAll("Human", "Random");
        player1ComboBox.setPromptText("Player 1");
        player1ComboBox.setStyle("-fx-font: 24px 'Arial';");
        player1ComboBox.setPrefWidth(200);
        player1ComboBox.setLayoutX(screenWidth/2 - 250);
        player1ComboBox.setLayoutY(screenHeight/2);

        ComboBox<String> player2ComboBox = new ComboBox<>();
        player2ComboBox.getItems().addAll("Human", "Random");
        player2ComboBox.setPromptText("Player 2");
        player2ComboBox.setStyle("-fx-font: 24px 'Arial';");
        player2ComboBox.setPrefWidth(200);
        player2ComboBox.setLayoutX(screenWidth/2 + 50);
        player2ComboBox.setLayoutY(screenHeight/2);

        // Start button
        Button startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(200);
        startGameButton.setLayoutX(screenWidth/2 - 100);
        startGameButton.setLayoutY(screenHeight/2 + 100);

        startGameButton.setOnAction(e -> {
            if (player1ComboBox.getValue() == null || player2ComboBox.getValue() == null) {
                showPlayerSelectionErrorDialog();
            } else {
                startGame(player1ComboBox.getValue(), player2ComboBox.getValue());
                primaryStage.close();
            }
        });
        startGameButton.setStyle("-fx-font: 24px 'Arial';");

        //layout for the main screen
        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(20, 10, 10, 20));
        hbox.getChildren().addAll(player1ComboBox, player2ComboBox, startGameButton);

        //layout for the title and content
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 10, 10, 20));
        vbox.getChildren().addAll(gameName, hbox);

        StackPane root = new StackPane();
        root.getChildren().add(vbox);

        mainMenuGroup.getChildren().addAll(gameName,groupNumber, player1ComboBox, player2ComboBox,startGameButton);

        primaryStage.setScene(mainMenu);
        primaryStage.show();
    }

    // method to start the game
    private void startGame(String player1Type, String player2Type) {
        System.out.println("Starting the game with Player 1: " + player1Type + " and Player 2: " + player2Type);

        // Create a new stage for the game screen
        Stage gameStage = new Stage();

        // Initialize and start the game screen
        GameScreen gameScreen = new GameScreen();
        gameScreen.start(gameStage, player1Type, player2Type);

    }

    // Method to show a pop-up dialog for player selection error
    private void showPlayerSelectionErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Player Selection Error");
        alert.setHeaderText("ALERT");
        alert.setContentText("Please select players first");
        //font from css
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/GUI/font.css")).toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}