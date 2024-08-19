import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScene {

    public static Scene scene;

    public static Scene getScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 0, 2);

        Button registerButton = new Button("Register");
        grid.add(registerButton, 1, 2);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean isAuthenticated = ClientUtil.authenticate(username, password);
            if (isAuthenticated) {
                System.out.println("Login successful!");
                if(!DataManager.isAdmin)
                    ((Stage) loginButton.getScene().getWindow()).setScene(SceneManager.getUserScene());
                else
                    ((Stage) loginButton.getScene().getWindow()).setScene(SceneManager.getAdminScene());
            } else {
                System.out.println("Invalid username or password!");
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            ClientUtil.register(username, password);
        });
        return new Scene(grid, 300, 200);
//        return scene;
    }
}
