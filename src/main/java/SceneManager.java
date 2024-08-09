import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SceneManager {
    private static boolean authenticate(String username, String password) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username:"+username);
            obj.put("password:"+password);
            String login = obj.toString();

            String out = HTTPUtil.write(DataManager.serverIp+"/login", "POST", login);
            String token = out.split(",")[0].split(":")[1].replace("\"", "").replace("}", "");
            Boolean isAdmin = Boolean.parseBoolean(out.split(",")[1].split(":")[1].replace("\"", "").replace("}", ""));

            DataManager.isAdmin = isAdmin;
            DataManager.session = token;
            DataManager.user = username;

            return !out.contains("error");
        }
        catch(ConnectException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            return false;
        }
        return false;
    }

    private static void register(String username, String password) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("username:"+username);
            obj.put("password:"+password);
            String login = obj.toString();

            String response = HTTPUtil.write(DataManager.serverIp+"/register", "POST", login);
            if(!response.contains("error")) {
                System.out.println("User registered successfully");
            }
            else {
                System.out.println(response);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getAllUsers(){
        try{
//            HTTPUtil registerConnection = new HTTPUtil(DataManager.serverIp+"/register", "GET");
        }catch(Exception e){
        }
        return null;
    }

    public static Scene getLoginScene(){
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

            boolean isAuthenticated = authenticate(username, password);
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
            register(username, password);
        });

        return new Scene(grid, 300, 200);
    }

    private static class MyObject {
        private String name;
        private Date date1;
        private Date date2;

        public MyObject(String name, Date date1, Date date2) {
            this.name = name;
            this.date1 = date1;
            this.date2 = date2;
        }

        public String getName() {
            return name;
        }

        public Date getDate1() {
            return date1;
        }

        public Date getDate2() {
            return date2;
        }
    }
    public static Scene getUserScene(){
        Label label1 = new Label("User : " + DataManager.user);
        Label label3 = new Label("Number of Books : ");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        gridPane.add(label1, 0, 0);
        gridPane.add(label3, 0, 2);

        ObservableList<MyObject> items = FXCollections.observableArrayList(
                new MyObject("Book", new Date(), new Date()),
                new MyObject("This is a super long book title", new Date(), new Date()),
                new MyObject("Normal Book", new Date(), new Date()));

        ListView<MyObject> listView = new ListView<>(items);
        listView.setCellFactory(new Callback<ListView<MyObject>, ListCell<MyObject>>() {
            @Override
            public ListCell<MyObject> call(ListView<MyObject> param) {
                return new ListCell<MyObject>() {
                    @Override
                    protected void updateItem(MyObject item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(10);
                            hbox.setAlignment(Pos.CENTER_LEFT);

                            Text nameText = new Text(item.getName());
                            nameText.setTextAlignment(TextAlignment.LEFT);
                            nameText.setWrappingWidth(150);
                            Text date1Text = new Text(new SimpleDateFormat("yyyy-MM-dd").format(item.getDate1()));
                            Text date2Text = new Text(new SimpleDateFormat("yyyy-MM-dd").format(item.getDate2()));

                            HBox.setMargin(nameText, new javafx.geometry.Insets(0, 10, 0, 0));

                            hbox.getChildren().addAll(nameText, date1Text, date2Text);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });
        listView.setPrefWidth(325);
        listView.setPrefHeight(300);
        listView.setFocusTraversable(false);

        Button button1 = new Button("Refresh");
        button1.setPrefSize(80, 100);
        button1.setFocusTraversable(false);
        button1.setShape(new Rectangle(80, 100));
        Button button2 = new Button("Expand");
        button2.setPrefSize(80, 100);
        button2.setFocusTraversable(false);
        button2.setShape(new Rectangle(80, 100));

        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(button1, button2);
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(listView, buttons);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(gridPane, hbox);

        return new Scene(vbox, 400, 300);
    }

    public static Scene getAdminScene(){
        Label label1 = new Label("Username: ");
        Label label2 = new Label("Books in stock: ");
        Label label3 = new Label("Number of users: ");

        // Create ListView components
        ListView<String> listView1 = new ListView<>(FXCollections.observableArrayList("Item 1", "Item 2", "Item 3"));
        listView1.setPrefSize(300, 350);
        ListView<String> listView2 = new ListView<>(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        listView2.setPrefSize(300, 350);

        // Create Buttons
        Button button1 = new Button("Issue Book");
        button1.setPrefSize(75, 50);
        Button button2 = new Button("Add Book");
        button2.setPrefSize(75, 50);
        Button button3 = new Button("Make Admin");
        button3.setPrefSize(75, 50);
        Button button4 = new Button("Refresh");
        button4.setPrefSize(75, 50);

        // Create an HBox for labels
        VBox labelsBox = new VBox(label1, label2, label3);

        // Create an HBox for buttons
        HBox buttonsBox = new HBox(button1, button2, button3, button4);

        // Create a VBox for the left side of the scene (Labels and ListView 1)
        VBox leftVBox = new VBox(labelsBox, listView1);
        leftVBox.setPrefWidth(300);

        // Create a VBox for the right side of the scene (Buttons and ListView 2)
        VBox rightVBox = new VBox(buttonsBox, listView2);
        rightVBox.setPrefWidth(300);

        // Create an HBox to hold both the left and right VBox
        HBox root = new HBox(leftVBox, rightVBox);

        // Create the scene
        Scene scene = new Scene(root, 600, 400);
        return scene;
    }
}
