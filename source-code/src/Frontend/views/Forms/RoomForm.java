package Frontend.views.Forms;

import Frontend.views.Dashboard.Layout;
import backend.Roles.Admin;
import backend.Rooms.*;
import backend.RoomsManagement.RoomManagement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RoomForm implements FormUi {
    private Admin admin;
    private RoomManagement roomManager;

    public RoomForm() {
        this.roomManager = new RoomManagement();
    }

    @Override
    public Parent getLayout(Stage stage) {
        VBox formArea = getFormContent();
        Layout layoutHandler = new Layout(stage);
        return layoutHandler.createLayout(formArea, "Admin", "Admin User");
    }

    private VBox getFormContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        VBox formCard = new VBox(12);
        formCard.setMaxWidth(600);
        formCard.setPadding(new Insets(30));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("🏨 Room Configuration");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #1e293b;");

        TextField numField = createStyledField("Enter Room Number (e.g. 101)");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Single","Double","Deluxe", "Suite");
        typeBox.setPromptText("Select Category");
        styleControl(typeBox);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter room amenities...");
        descArea.setPrefHeight(80);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-control-inner-background: #f9fafb; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

        TextField priceField = createStyledField("Price per Night");

        ToggleButton statusBtn = new ToggleButton("Active");
        statusBtn.setSelected(true);
        statusBtn.setMaxWidth(Double.MAX_VALUE);
        statusBtn.setPrefHeight(40);
        statusBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold;");

        statusBtn.textProperty().bind(javafx.beans.binding.Bindings.when(statusBtn.selectedProperty()).then("Active").otherwise("Inactive"));
        statusBtn.selectedProperty().addListener((obs, old, val) -> {
            statusBtn.setStyle(val ? "-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold;" : "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold;");
        });

        Button submitBtn = new Button("Register Room");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setPrefHeight(50);
        submitBtn.setStyle("-fx-background-color: #fbbf24; -fx-text-fill: #0f172a; -fx-font-weight: bold; -fx-background-radius: 8; -fx-font-size: 16px;");

        submitBtn.setOnAction(e -> {
            try {
                int rNum = Integer.parseInt(numField.getText());
                String type = typeBox.getValue();
                String desc = descArea.getText();
                double price = Double.parseDouble(priceField.getText());
                boolean available = statusBtn.isSelected();

                Room room;
                if ("Suite".equalsIgnoreCase(type)) {
                    room = new SuiteRoom(rNum, desc, type, price, available);
                } else {
                    room = new DeluxeRoom(rNum, desc, type, price, available);
                }

                roomManager.addRoom(room);

                numField.clear();
                descArea.clear();
                priceField.clear();

                new Alert(Alert.AlertType.INFORMATION, "Room ID: " + rNum + " saved successfully!").show();

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid Input! Please check Room ID, Number and Price.").show();
            }
        });

        formCard.getChildren().addAll(
                title, new Separator(),
                createLabel("Room Number"), numField,
                createLabel("Room Type"), typeBox,
                createLabel("Description"), descArea,
                createLabel("Pricing"), priceField,
                createLabel("Availability"), statusBtn,
                new Region(), submitBtn
        );

        root.getChildren().add(formCard);
        return root;
    }

    private void styleControl(Control c) {
        c.setMaxWidth(Double.MAX_VALUE);
        c.setPrefHeight(45);
        c.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #64748b; -fx-font-weight: bold; -fx-font-size: 13px;");
        return l;
    }

    private TextField createStyledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        styleControl(tf);
        return tf;
    }

    @Override public void submit() {}
    @Override public boolean validate() { return true; }
}