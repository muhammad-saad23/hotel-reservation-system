package Frontend.views.List;

import FileHandling.AuthService;
import Frontend.views.Dashboard.Layout;
import Frontend.views.Forms.SubAdminForm;
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

public class SubAdminList implements ListUi {

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
        Label sub = new Label("Manage sub-admin staff accounts and system access");
        sub.setStyle("-fx-text-fill: #64748b;");
        titleBox.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnAdd = new Button("➕ Add Sub-Admin");
        btnAdd.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");

        btnAdd.setOnAction(e -> {
           SubAdminForm subAdminForm= new SubAdminForm();
           stage.getScene().setRoot(subAdminForm.getLayout(stage));
        });

        header.getChildren().addAll(titleBox, spacer, btnAdd);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by name or email...");
        searchField.setStyle("-fx-background-radius: 10; -fx-padding: 10; -fx-border-color: #e2e8f0; -fx-background-color: white;");

        TableView<String[]> table = new TableView<>();
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #e2e8f0;");

        TableColumn<String[], String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new SimpleStringProperty("SA-" + d.getValue()[0]));

        TableColumn<String[], String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));

        TableColumn<String[], String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        TableColumn<String[], String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        TableColumn<String[], String> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnUpdate = new Button("Edit");
            private final Button btnBlock = new Button("Block");
            private final HBox container = new HBox(10, btnUpdate, btnBlock);

            {
                btnUpdate.setStyle("-fx-background-color: #f0f9ff; -fx-text-fill: #0369a1; -fx-cursor: hand;");
                btnBlock.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #991b1b; -fx-cursor: hand;");
                container.setAlignment(Pos.CENTER);

                btnBlock.setOnAction(e -> {
                    String[] data = getTableView().getItems().get(getIndex());
                    System.out.println("Blocking: " + data[2]);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        table.getColumns().addAll(colId, colName, colEmail, colPhone, colActions);


        ObservableList<String[]> masterData = fetchSubAdminsFromFile();
        FilteredList<String[]> filteredData = new FilteredList<>(masterData, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(row -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return row[1].toLowerCase().contains(lower) || row[2].toLowerCase().contains(lower);
            });
        });

        table.setItems(filteredData);
        table.setPlaceholder(new Label("No sub-admins found."));

        content.getChildren().addAll(header, searchField, table);

        Layout layoutManager = new Layout(stage);
        stage.getScene().setRoot(layoutManager.createLayout(content, "Admin", "Admin User"));
    }

    private ObservableList<String[]> fetchSubAdminsFromFile() {
        ObservableList<String[]> list = FXCollections.observableArrayList();
        File file = new File("users.txt");
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6 && d[5].trim().equalsIgnoreCase("SubAdmin")) {
                    list.add(new String[]{ d[0], d[1], d[2], d[3], d[5] });
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }
}