import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JFXClient extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Authentication");
        Scene scene = SceneManager.getLoginScene();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Scene user = SceneManager.getUserScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
