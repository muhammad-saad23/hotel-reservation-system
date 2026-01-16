package Frontend.views.List;

import backend.RoomsManagement.RoomManagement;
import Frontend.views.Dashboard.Layout;
import Frontend.views.Forms.RoomForm;
import backend.Rooms.Room;
import backend.Sessions.UserSession;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RoomList implements ListUi {
    private RoomManagement roomManager = new RoomManagement();
    private ObservableList<Room> masterData = FXCollections.observableArrayList();
    private FilteredList<Room> filteredData;

    @Override
    public void show(Stage stage) {
        String role = UserSession.getLoggedUserRole();
        String name = UserSession.getLoggedUserName();
        String perms = UserSession.getLoggedUserPermissions();

        if (perms == null) perms = "";
        String pUpper = perms.toUpperCase();

        boolean isAdmin = role != null && role.equalsIgnoreCase("Admin");
        boolean canAdd = isAdmin || pUpper.contains("ADDROOMS");
        boolean canEdit = isAdmin || pUpper.contains("EDITROOMS");
        boolean canDelete = isAdmin || pUpper.contains("DELETEROOMS");

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(5);
        Label title = new Label("Room Inventory Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        titleBox.getChildren().addAll(title, new Label("Manage rooms based on your assigned access."));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titleBox, spacer);

        if (canAdd) {
            Button btnAddRoom = new Button("➕ Add New Room");
            btnAddRoom.setStyle("-fx-background-color: #4338ca; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 20; -fx-font-weight: bold;");
            btnAddRoom.setOnAction(e -> stage.getScene().setRoot(new RoomForm().getLayout(stage)));
            header.getChildren().add(btnAddRoom);
        }

        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search rooms...");
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #cbd5e1;");

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Categories", "Single", "Double", "Deluxe", "Suite");
        typeFilter.setValue("All Categories");
        typeFilter.setPrefHeight(40);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Available", "Occupied");
        statusFilter.setValue("All Status");
        statusFilter.setPrefHeight(40);

        filterBar.getChildren().addAll(searchField, typeFilter, statusFilter);

        TableView<Room> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(600);

        TableColumn<Room, String> colNo = new TableColumn<>("Room #");
        colNo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getRoomNumber())));

        TableColumn<Room, String> colType = new TableColumn<>("Category");
        colType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getRoomType()));

        TableColumn<Room, String> colPrice = new TableColumn<>("Price/Night");
        colPrice.setCellValueFactory(d -> new SimpleStringProperty("Rs. " + d.getValue().getPricePerNight()));

        TableColumn<Room, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().isAvailable() ? "Available" : "Occupied"));

        table.getColumns().addAll(colNo, colType, colPrice, colStatus);

        if (canEdit || canDelete) {
            TableColumn<Room, Void> colActions = new TableColumn<>("Actions");
            colActions.setCellFactory(param -> new TableCell<>() {
                private final Button btnEdit = new Button("✏️");
                private final Button btnDelete = new Button("🗑️");
                private final HBox pane = new HBox(10, btnEdit, btnDelete);
                {
                    pane.setAlignment(Pos.CENTER);

                    btnEdit.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
                    btnEdit.setVisible(canEdit);
                    btnEdit.setManaged(canEdit);

                    btnDelete.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-background-radius: 5;");
                    btnDelete.setVisible(canDelete);
                    btnDelete.setManaged(canDelete);

                    btnDelete.setOnAction(e -> {
                        Room room = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete Room #" + room.getRoomNumber() + "?", ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(res -> {
                            if (res == ButtonType.YES) {
                                roomManager.deleteRoom(room.getRoomNumber());
                                masterData.remove(room);
                                table.refresh();
                            }
                        });
                    });

                    btnEdit.setOnAction(e -> {
                        Room room = getTableView().getItems().get(getIndex());
                        stage.getScene().setRoot(new RoomForm(room).getLayout(stage));
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : pane);
                }
            });
            table.getColumns().add(colActions);
        }

        masterData.setAll(roomManager.getRooms());
        filteredData = new FilteredList<>(masterData, p -> true);
        autoApplyFilters(searchField, typeFilter, statusFilter);
        table.setItems(filteredData);

        content.getChildren().addAll(header, filterBar, table);
        stage.getScene().setRoot(new Layout(stage).createLayout(content, role, name));
    }

    private void autoApplyFilters(TextField search, ComboBox<String> type, ComboBox<String> status) {
        Runnable filterLogic = () -> {
            filteredData.setPredicate(room -> {
                String searchText = search.getText() == null ? "" : search.getText().toLowerCase();
                String selectedType = type.getValue();
                String selectedStatus = status.getValue();

                boolean matchesSearch = String.valueOf(room.getRoomNumber()).contains(searchText) ||
                        room.getRoomType().toLowerCase().contains(searchText);
                boolean matchesType = selectedType.equals("All Categories") ||
                        room.getRoomType().equalsIgnoreCase(selectedType);
                boolean matchesStatus = selectedStatus.equals("All Status") ||
                        (selectedStatus.equals("Available") && room.isAvailable()) ||
                        (selectedStatus.equals("Occupied") && !room.isAvailable());

                return matchesSearch && matchesType && matchesStatus;
            });
        };
        search.textProperty().addListener((obs, old, newVal) -> filterLogic.run());
        type.valueProperty().addListener((obs, old, newVal) -> filterLogic.run());
        status.valueProperty().addListener((obs, old, newVal) -> filterLogic.run());
    }
}