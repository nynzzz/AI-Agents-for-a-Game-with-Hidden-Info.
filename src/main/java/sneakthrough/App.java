package sneakthrough;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        var javaVersion = System.getProperty("java.version");
        var javafxVersion = System.getProperty("javafx.version");

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        // Define the Font here
        Font font = Font.font("Helvetica Neue", FontWeight.NORMAL, 12);
        label.setFont(font);

        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
