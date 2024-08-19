import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminScene {
    public static Scene getScene() {
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
        return new Scene(root, 600, 400);
    }
}
