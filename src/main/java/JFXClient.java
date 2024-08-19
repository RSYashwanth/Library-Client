import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXClient extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Authentication");
        Scene scene = LoginScene.getScene();
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
