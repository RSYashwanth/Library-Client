import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class AdminScene {
    private static HBox root;
    private static ObservableList<String> users;

    private static ObservableList<Book> books;

    private static Label numBooksLabel;

    private static Label numUsersLabel;

    public static Scene getScene() {
        initializeLists();
        populateUI();
        return new Scene(root, 600, 400);
    }

    private static void populateUI() {
        Label label1 = new Label("Username: ");
        numBooksLabel = new Label("Books in stock: ");
        numUsersLabel = new Label("Number of users: ");
        VBox labelsBox = new VBox(label1, numBooksLabel, numUsersLabel);

        ListView<String> listView1 = new ListView<>(users);
        listView1.setPrefSize(300, 350);

        VBox leftVBox = new VBox(labelsBox, listView1);
        leftVBox.setPrefWidth(300);

        Button button1 = new Button("Issue Book");
        button1.setPrefSize(75, 50);

        Button button2 = new Button("Add Book");
        button2.setPrefSize(75, 50);

        Button button3 = new Button("Make Admin");
        button3.setPrefSize(75, 50);

        Button button4 = new Button("Refresh");
        button4.setPrefSize(75, 50);

        HBox buttonsBox = new HBox(button1, button2, button3, button4);

        ListView<Book> listView2 = new ListView<>(books);
        listView2.setCellFactory(AdminScene::generateCellFactory);
        listView2.setPrefSize(300, 350);

        VBox rightVBox = new VBox(buttonsBox, listView2);
        rightVBox.setPrefWidth(300);

        root = new HBox(leftVBox, rightVBox);
    }

    private static ListCell<Book> generateCellFactory(ListView<Book> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                }
                else {
                    setText(item.getName());
                }
            }
        };
    }

    private static void initializeLists() {
        users = FXCollections.observableArrayList();
        books = FXCollections.observableArrayList();
        refreshUserList();
        refreshBookList();
    }

    private static void refreshUserList() {
        items.clear();
        try {
            String books[] = HTTPUtil.write(
                    DataManager.serverIp + "/books?username=" + DataManager.user,
                    "GET",
                    "").replace("\"", "").split("\\|\\|");

            for (String book : books) {
                book = book.trim().substring(1, book.length()-1);
                String[] parts = book.split(",");
                int id = Integer.parseInt(parts[0].split(":")[1].trim());
                String title = parts[1].split(":")[1].trim();
                String date = parts[3].split(":")[1].trim();
                if (!date.equals("null")) {
                    System.out.println(date.split("T")[0]);
                    LocalDate issued = LocalDate.parse(date.split("T")[0]);
                    LocalDate due = issued.plusDays(15);
                    items.add(new Book(id, title, issued, due));
                }
            }
        }
        catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        DataManager.numBooks = items.size();
    }

    private static void refreshBookList() {

    }
}
