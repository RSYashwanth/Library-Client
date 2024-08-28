import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserScene {
    private static GridPane grid;

    private static VBox vbox;

    private static ObservableList<Book> items;

    private static Label numBooksLabel;

    public static Scene getScene() {
        createGrid();
        initializeBookList();
        populateUI();
        refreshNumBooks();

        Scene scene = new Scene(vbox, 400, 300);
        return scene;
    }

    private static void createGrid() {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
    }

    private static void populateUI() {
        Label label1 = new Label("User : " + DataManager.user);
        numBooksLabel = new Label("Number of Books : " + DataManager.numBooks);

        grid.add(label1, 0, 0);
        grid.add(numBooksLabel, 0, 2);

        ListView<Book> listView = new ListView<>(items);
        listView.setCellFactory(UserScene::generateCellFactory);
        listView.setPrefWidth(325);
        listView.setPrefHeight(300);
        listView.setFocusTraversable(false);

        Button button1 = new Button("Refresh");
        button1.setPrefSize(80, 100);
        button1.setFocusTraversable(false);
        button1.setShape(new Rectangle(80, 100));
        button1.setOnAction((e) -> {
            refreshBookList();
            refreshNumBooks();
        });

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

        vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(grid, hbox);
    }

    private static ListCell<Book> generateCellFactory(ListView<Book> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                }
                else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    Text nameText = new Text(item.getName());
                    nameText.setTextAlignment(TextAlignment.LEFT);
                    nameText.setWrappingWidth(150);

                    Text date1Text = new Text(item.getDate1().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    Text date2Text = new Text(item.getDate2().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

                    HBox.setMargin(nameText, new Insets(0, 10, 0, 0));
                    hbox.getChildren().addAll(nameText, date1Text, date2Text);
                    setGraphic(hbox);
                }
            }
        };
    }

    private static void initializeBookList() {
        items = FXCollections.observableArrayList();
        refreshBookList();
    }

    private static void refreshBookList() {
        items.clear();
        try {
            String books[] = HTTPUtil.write(
                    DataManager.serverIp + "/books/" + DataManager.user,
                    "GET",
                    "").replace("\"", "").split("\\|\\|");

            for (String book : books) {
                if (book.length() == 0) {
                    continue;
                }
                book = book.trim().substring(1, book.length()-1);
                String[] parts = book.split(",");
                int id = Integer.parseInt(parts[0].split(":")[1].trim());
                String title = parts[1].split(":")[1].trim();
                String date = parts[3].split(":")[1].trim();
                if (!date.equals("null")) {
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

    private static void refreshNumBooks() {
        numBooksLabel.setText("Number of Books: " + DataManager.numBooks);
    }
}
