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
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserScene {

    public static Scene scene;

    private static class Book {
        private String name;
        private Date date1;
        private Date date2;

        public Book(String name, Date date1, Date date2) {
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

    public static Scene getScene() {
        Label label1 = new Label("User : " + DataManager.user);
        Label label3 = new Label("Number of Books : ");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        gridPane.add(label1, 0, 0);
        gridPane.add(label3, 0, 2);

        ObservableList<Book> items = FXCollections.observableArrayList(
                new Book("Book", new Date(), new Date()),
                new Book("This is a super long book title", new Date(), new Date()),
                new Book("Normal Book", new Date(), new Date()));

        ListView<Book> listView = new ListView<>(items);
        listView.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new ListCell<Book>() {
                    @Override
                    protected void updateItem(Book item, boolean empty) {
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
}
