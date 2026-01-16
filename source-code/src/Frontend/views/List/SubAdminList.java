package Frontend.views.List;

import Frontend.views.Dashboard.Layout;
import Frontend.views.Forms.ManageAccess;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubAdminList implements ListUi {

    private TableView<String[]> table = new TableView<>();
    private ObservableList<String[]> masterData = FXCollections.observableArrayList();
    private FilteredList<String[]> filteredData;

    @Override
    public void show(Stage stage) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8fafc;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("System Sub-Admins");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label sub = new Label("Manage staff accounts, block/unblock access and permissions");
        sub.setStyle("-fx-text-fill: #64748b;");
        titleBox.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        header.getChildren().addAll(titleBox, spacer);

        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by name or email...");
        searchField.setPrefWidth(350);
        searchField.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #e2e8f0;");

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "SubAdmin", "Blocked");
        statusFilter.setValue("All Status");
        statusFilter.setPrefWidth(150);
        statusFilter.setStyle("-fx-background-radius: 10; -fx-padding: 5; -fx-border-color: #e2e8f0;");

        controls.getChildren().addAll(searchField, statusFilter);

        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #e2e8f0;");

        // Columns setup
        TableColumn<String[], String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty("SA-" + d.getValue()[0]));

        TableColumn<String[], String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));

        TableColumn<String[], String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        TableColumn<String[], String> colStatus = new TableColumn<>("Role/Status");
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[5]));

        TableColumn<String[], String> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAccess = new Button("Access");
            private final Button btnBlock = new Button("Block");
            private final HBox container = new HBox(10, btnAccess, btnBlock);

            {
                btnAccess.setStyle("-fx-background-color: #ecfdf5; -fx-text-fill: #059669; -fx-font-weight: bold; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                btnAccess.setOnAction(e -> {
                    stage.getScene().setRoot(new ManageAccess().getLayout(stage));
                });

                btnBlock.setOnAction(e -> {
                    String[] data = getTableView().getItems().get(getIndex());
                    handleBlockToggle(data, stage);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    String[] data = getTableView().getItems().get(getIndex());
                    if (data[5].equalsIgnoreCase("Blocked")) {
                        btnBlock.setText("Unblock");
                        btnBlock.setStyle("-fx-background-color: #eff6ff; -fx-text-fill: #1d4ed8; -fx-cursor: hand;");
                    } else {
                        btnBlock.setText("Block");
                        btnBlock.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #991b1b; -fx-cursor: hand;");
                    }
                    setGraphic(container);
                }
            }
        });

        table.getColumns().addAll(colId, colName, colEmail, colStatus, colActions);

        masterData.setAll(fetchSubAdminsFromFile());
        filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, old, newValue) -> updateFilter(newValue, statusFilter.getValue()));
        statusFilter.valueProperty().addListener((obs, old, newValue) -> updateFilter(searchField.getText(), newValue));

        table.setItems(filteredData);
        content.getChildren().addAll(header, controls, table);

        stage.getScene().setRoot(new Layout(stage).createLayout(content, "Admin", "Admin User"));
    }

    private void updateFilter(String searchText, String status) {
        filteredData.setPredicate(data -> {
            boolean matchesSearch = searchText == null || searchText.isEmpty() ||
                    data[1].toLowerCase().contains(searchText.toLowerCase()) ||
                    data[2].toLowerCase().contains(searchText.toLowerCase());

            boolean matchesStatus = status.equals("All Status") || data[5].equalsIgnoreCase(status);

            return matchesSearch && matchesStatus;
        });
    }

    private void handleBlockToggle(String[] data, Stage stage) {
        String id = data[0];
        String name = data[1];
        boolean isCurrentlyBlocked = data[5].equalsIgnoreCase("Blocked");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Account Management");
        alert.setHeaderText(isCurrentlyBlocked ? "Unblock " + name : "Block " + name);
        alert.setContentText("Select action for this user:");

        ButtonType actionType = new ButtonType(isCurrentlyBlocked ? "Unblock User" : "Block Access");
        ButtonType btnDelete = new ButtonType("Remove");
        ButtonType btnCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(actionType, btnDelete, btnCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == actionType) {
                updateUserStatus(id, isCurrentlyBlocked ? "SubAdmin" : "Blocked", false);
            } else if (result.get() == btnDelete) {
                updateUserStatus(id, "", true);
            }
            masterData.setAll(fetchSubAdminsFromFile());
        }
    }

    private boolean updateUserStatus(String id, String newStatus, boolean permanent) {
        File inputFile = new File("users.txt");
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(id)) {
                    if (permanent) continue;
                    d[5] = newStatus;
                    lines.add(String.join(",", d));
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { return false; }

        try (PrintWriter writer = new PrintWriter(new FileWriter(inputFile))) {
            for (String l : lines) writer.println(l);
            return true;
        } catch (IOException e) { return false; }
    }

    private List<String[]> fetchSubAdminsFromFile() {
        List<String[]> list = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6 && (d[5].trim().equalsIgnoreCase("SubAdmin") || d[5].trim().equalsIgnoreCase("Blocked"))) {
                    list.add(new String[]{ d[0], d[1], d[2], d[3], d[4], d[5].trim() });
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }
}