import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDate;

public class AdminScene {
    private static HBox root;
    private static ObservableList<String> users;

    private static ObservableList<Book> books;

    private static Label numBooksLabel;

    private static Label numUsersLabel;

    private static Scene scene;

    public static Scene getScene() {
        initializeLists();
        populateUI();
        scene = new Scene(root, 600, 400);
        return scene;
    }

    private static void populateUI() {
        Label label1 = new Label("Username: " + DataManager.user);
        numBooksLabel = new Label("Books in stock: " + DataManager.numBooks);
        numUsersLabel = new Label("Number of users: " + DataManager.numUsers);
        VBox labelsBox = new VBox(label1, numBooksLabel, numUsersLabel);

        ListView<String> listView1 = new ListView<>(users);
        listView1.setPrefSize(300, 350);

        VBox leftVBox = new VBox(labelsBox, listView1);
        leftVBox.setPrefWidth(300);

        Button button1 = new Button("Issue Book");
        button1.setPrefSize(75, 50);

        Button button2 = new Button("Add Book");
        button2.setPrefSize(75, 50);
        button2.setOnAction(e -> addBookPopup(scene.getWindow()));

        Button button3 = new Button("Make Admin");
        button3.setPrefSize(75, 50);
        button3.setOnAction(e -> makeAdmin(listView1.getSelectionModel().getSelectedItem()));

        Button button4 = new Button("Refresh");
        button4.setPrefSize(75, 50);
        button4.setOnAction(e -> {
            refreshUserList();
            refreshBookList();
            refreshLabels();
        });

        HBox buttonsBox = new HBox(button1, button2, button3, button4);

        ListView<Book> listView2 = new ListView<>(books);
        listView2.setCellFactory(AdminScene::generateCellFactory);
        listView2.setPrefSize(300, 350);

        button1.setOnAction(e -> issueBook(
                listView1.getSelectionModel().getSelectedItem(),
                listView2.getSelectionModel().getSelectedItem().getId()));

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
                    setText(item.getName() + " ( id: " + item.getId() + " )");
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
        users.clear();
        try {
            String response[] = HTTPUtil.write(
                    DataManager.serverIp + "/users",
                    "GET",
                    "").replace("\"", "").split("\\|\\|");

            for (String user : response) {
                user = user.trim().substring(1, user.length()-1);
                String[] parts = user.split(",");
                String username = parts[1].split(":")[1].trim();
                users.add(username);
            }
        }
        catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        DataManager.numUsers = users.size();
    }

    private static void refreshBookList() {
        books.clear();
        try {
            String response[] = HTTPUtil.write(
                    DataManager.serverIp + "/books/available",
                    "GET",
                    "").replace("\"", "").split("\\|\\|");

            for (String book : response) {
                if (book.length() == 0) {
                    continue;
                }
                book = book.trim().substring(1, book.length()-1);
                String[] parts = book.split(",");
                int id = Integer.valueOf(parts[0].split(":")[1].trim());
                String title = parts[1].split(":")[1].trim();
                String date = parts[3].split(":")[1].trim();
                if (!date.equals("null")) {
                    LocalDate issued = LocalDate.parse(date.split("T")[0]);
                    LocalDate due = issued.plusDays(15);
                    books.add(new Book(id, title, issued, due));
                }
                else {
                    books.add(new Book(id, title, null, null));
                }
            }
        }
        catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        DataManager.numBooks = books.size();
    }

    private static void refreshLabels() {
        numBooksLabel = new Label("Books in stock: " + DataManager.numBooks);
        numUsersLabel = new Label("Number of users: " + DataManager.numUsers);
    }

    private static void makeAdmin(String username) {
        try {
            JSONObject data = new JSONObject();
            data.put("username:" + username);
            HTTPUtil.write(DataManager.serverIp + "/admin", "POST", data.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void issueBook(String username, int id) {
        try {
            JSONObject data = new JSONObject();
            data.put("username:" + username);
            data.put("id:" + id);
            HTTPUtil.write(DataManager.serverIp + "/issueBook", "POST", data.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addBookPopup(Window owner) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(owner);

        popup.setTitle("Add new book");
        popup.showAndWait();
    }
}
