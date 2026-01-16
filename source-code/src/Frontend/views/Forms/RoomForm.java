package Frontend.views.Forms;

import Frontend.views.Dashboard.Layout;
import Frontend.views.List.RoomList;
import backend.Rooms.*;
import backend.RoomsManagement.RoomManagement;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RoomForm implements FormUi {
    private RoomManagement roomManager;
    private Room existingRoom = null;

    public RoomForm() {
        this.roomManager = new RoomManagement();
    }

    public RoomForm(Room room) {
        this();
        this.existingRoom = room;
    }

    @Override
    public Parent getLayout(Stage stage) {
        VBox formArea = getFormContent(stage);
        Layout layoutHandler = new Layout(stage);

        String role = UserSession.getLoggedUserRole();
        String name = UserSession.getLoggedUserName();

        return layoutHandler.createLayout(formArea, role, name);
    }

    private VBox getFormContent(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        VBox formCard = new VBox(12);
        formCard.setMaxWidth(600);
        formCard.setPadding(new Insets(30));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label(existingRoom == null ? "🏨 Register New Room" : "✏️ Update Room Details");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #1e293b;");

        TextField numField = createStyledField("Enter Room Number");
        numField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Single", "Double", "Deluxe", "Suite");
        typeBox.setPromptText("Select Category");
        styleControl(typeBox);

        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter amenities...");
        descArea.setPrefHeight(80);
        descArea.setWrapText(true);
        descArea.setStyle("-fx-control-inner-background: #f9fafb; -fx-border-color: #e2e8f0;");

        TextField priceField = createStyledField("Price per Night");
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });

        ToggleButton statusBtn = new ToggleButton("Active");
        statusBtn.setSelected(true);
        styleToggleButton(statusBtn);

        if (existingRoom != null) {
            numField.setText(String.valueOf(existingRoom.getRoomNumber()));
            numField.setEditable(false);
            numField.setStyle("-fx-background-color: #e2e8f0; -fx-border-color: #cbd5e1;");
            typeBox.setValue(existingRoom.getRoomType());
            descArea.setText(existingRoom.getDescription());
            priceField.setText(String.valueOf(existingRoom.getPricePerNight()));
            statusBtn.setSelected(existingRoom.isAvailable());
        }

        Button submitBtn = new Button(existingRoom == null ? "CREATE ROOM" : "SAVE CHANGES");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setPrefHeight(50);
        submitBtn.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        submitBtn.setOnAction(e -> {
            String role = UserSession.getLoggedUserRole();
            String perms = UserSession.getLoggedUserPermissions();
            if (perms == null) perms = "";

            boolean isAdmin = role.equalsIgnoreCase("Admin");

            if (existingRoom == null) {
                if (!isAdmin && !perms.contains("AddRooms")) {
                    new Alert(Alert.AlertType.ERROR, "Access Denied: You do not have permission to add rooms.").show();
                    return;
                }
            } else {
                if (!isAdmin && !perms.contains("EditRooms")) {
                    new Alert(Alert.AlertType.ERROR, "Access Denied: You do not have permission to edit rooms.").show();
                    return;
                }
            }

            try {
                if (numField.getText().isEmpty() || typeBox.getValue() == null || priceField.getText().isEmpty()) {
                    new Alert(Alert.AlertType.WARNING, "Warning: Please fill all required fields!").show();
                    return;
                }

                int rNum = Integer.parseInt(numField.getText());
                String type = typeBox.getValue();
                String desc = descArea.getText();
                double price = Double.parseDouble(priceField.getText());
                boolean available = statusBtn.isSelected();

                if (existingRoom == null) {
                    Room room;
                    if ("Suite".equalsIgnoreCase(type)) room = new SuiteRoom(rNum, desc, type, price, available);
                    else if ("Deluxe".equalsIgnoreCase(type)) room = new DeluxeRoom(rNum, desc, type, price, available);
                    else room = new SingleRoom(rNum, desc, type, price, available);

                    roomManager.addRoom(room);
                } else {
                    roomManager.updateRoom(rNum, desc, type, price, available);
                }

                new Alert(Alert.AlertType.INFORMATION, "Success: Room " + rNum + " saved successfully!").show();
                new RoomList().show(stage);

            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Error: Invalid input values!").show();
            }
        });

        formCard.getChildren().addAll(
                title, new Separator(),
                createLabel("Room Number"), numField,
                createLabel("Room Type"), typeBox,
                createLabel("Description"), descArea,
                createLabel("Pricing (Rs.)"), priceField,
                createLabel("Status"), statusBtn,
                new Region(), submitBtn
        );

        root.getChildren().add(formCard);
        return root;
    }

    private void styleToggleButton(ToggleButton btn) {
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(40);
        btn.setCursor(javafx.scene.Cursor.HAND);
        btn.textProperty().bind(javafx.beans.binding.Bindings.when(btn.selectedProperty()).then("Available").otherwise("Occupied"));
        btn.selectedProperty().addListener((obs, old, val) -> {
            btn.setStyle(val ? "-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;" :
                    "-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
        });
        btn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");
    }

    private void styleControl(Control c) {
        c.setMaxWidth(Double.MAX_VALUE);
        c.setPrefHeight(45);
        c.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-padding: 0 10;");
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #475569; -fx-font-weight: bold; -fx-font-size: 13px;");
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