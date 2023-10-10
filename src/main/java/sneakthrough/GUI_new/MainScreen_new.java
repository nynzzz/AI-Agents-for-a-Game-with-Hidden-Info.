package sneakthrough.GUI_new;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;

import java.util.Objects;

public class MainScreen_new extends Application {

    private final int screen_width = 800;
    private final int screen_height = 600;
    private final Font font = Font.font("Arial", 24);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Group 6 - Sneakthrough");

        //title label
        Label titleLabel = new Label("Sneakthrough");
        titleLabel.setFont(font);

        // combo boxes for player selection
        ComboBox<String> player1ComboBox = new ComboBox<>();
        player1ComboBox.getItems().addAll("Human", "Random");
        player1ComboBox.setPromptText("Player 1");
        player1ComboBox.setStyle("-fx-font: 24px 'Arial';");

        ComboBox<String> player2ComboBox = new ComboBox<>();
        player2ComboBox.getItems().addAll("Human", "Random");
        player2ComboBox.setPromptText("Player 2");
        player2ComboBox.setStyle("-fx-font: 24px 'Arial';");

        // Start button
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> {
            if (player1ComboBox.getValue() == null || player2ComboBox.getValue() == null) {
                // Show a pop-up dialog to tell the user to select players
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
        vbox.getChildren().addAll(titleLabel, hbox);

        StackPane root = new StackPane();
        root.getChildren().add(vbox);

        Scene scene = new Scene(root, screen_width, screen_height);

        primaryStage.setScene(scene);
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
