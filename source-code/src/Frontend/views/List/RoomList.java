package Frontend.views.List;

import FileHandling.RoomService;
import Frontend.views.Dashboard.Layout;
import Frontend.views.Forms.RoomForm;
import backend.Rooms.Room;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class RoomList implements ListUi {

    @Override
    public void show(Stage stage) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8fafc;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("Hospital Room Directory");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label sub = new Label("Real-time room availability and management");
        sub.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");
        titleBox.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAddRoom = new Button("➕ Add New Room");
        btnAddRoom.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");

        btnAddRoom.setOnAction(e -> {
             RoomForm roomForm =new RoomForm();
             stage.getScene().setRoot(roomForm.getLayout(stage));
        });

        header.getChildren().addAll(titleBox, spacer, btnAddRoom);

        // --- Search Bar Section ---
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by Room No, Type or Status...");
        searchField.setPrefWidth(400);
        searchField.setStyle("-fx-background-radius: 10; -fx-padding: 12; -fx-border-color: #e2e8f0; -fx-background-color: white;");

        TableView<String[]> table = new TableView<>();
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e2e8f0;");

        TableColumn<String[], String> colNo = new TableColumn<>("Room No");
        colNo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));

        TableColumn<String[], String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));

        TableColumn<String[], String> colPrice = new TableColumn<>("Price/Day");
        colPrice.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        TableColumn<String[], String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        colStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("Available")) {
                        setStyle("-fx-text-fill: #10b981; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
                    }
                }
            }
        });

        table.getColumns().addAll(colNo, colType, colPrice, colStatus);

        List<Room> rooms = RoomService.loadRooms();
        ObservableList<String[]> tableData = FXCollections.observableArrayList();

        for (Room r : rooms) {
            String status = r.isAvailable() ? "Available" : "Occupied";
            tableData.add(new String[]{
                    String.valueOf(r.getRoomNumber()),
                    r.getRoomType(),
                    "Rs. " + r.getPricePerNight(),
                    status
            });
        }

        FilteredList<String[]> filteredData = new FilteredList<>(tableData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(rowData -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return rowData[0].toLowerCase().contains(lowerCaseFilter) ||
                        rowData[1].toLowerCase().contains(lowerCaseFilter) ||
                        rowData[3].toLowerCase().contains(lowerCaseFilter);
            });
        });

        table.setItems(filteredData);
        table.setPlaceholder(new Label("No rooms matching your search."));

        content.getChildren().addAll(header, searchField, table);

        Layout layoutManager = new Layout(stage);
        stage.getScene().setRoot(layoutManager.createLayout(content, "Admin", "Admin User"));
    }
}