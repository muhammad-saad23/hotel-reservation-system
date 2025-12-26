package Frontend.views.Admin.Room;

import backend.Roles.Admin;
import backend.Rooms.Room;
import backend.RoomsManagement.RoomManagement;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RoomForm extends Application {
    private Admin admin;

    public RoomForm(Admin admin) {
        this.admin = admin;
    }

    public RoomForm() {
        this.admin = new Admin("Saad", "admin@hotel.com", "000", "pass", new RoomManagement());
    }

    @Override
    public void start(Stage stage) {
        // BorderPane is best for Sidebar and Topbar
        BorderPane root = new BorderPane();

        // 1. Sidebar (Left)
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        // 2. Topbar (Top)
        HBox topbar = createTopbar();
        root.setTop(topbar);

        // 3. Main Form Content (Center)
        VBox formContent = getForm();
        root.setCenter(formContent);

        Scene scene = new Scene(root, 1100, 700);
        stage.setTitle("Hotel Management - Admin Room Management");
        stage.setScene(scene);
        stage.show();
    }

    // --- Sidebar Implementation ---
    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label logo = new Label("LUXE HOTEL");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("System", FontWeight.BOLD, 22));
        logo.setPadding(new Insets(0, 0, 30, 0));
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);

        Button btnDash = createSideButton("📊 Dashboard");
        Button btnRooms = createSideButton("🏨 Manage Rooms"); // Active button
        btnRooms.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 12 15; -fx-border-color: #3498db; -fx-border-width: 0 0 0 5;");

        Button btnBookings = createSideButton("📅 Bookings");
        Button btnUsers = createSideButton("👥 Staff");

        sidebar.getChildren().addAll(logo, btnDash, btnRooms, btnBookings, btnUsers);
        return sidebar;
    }

    // --- Topbar Implementation ---
    private HBox createTopbar() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label lblHeader = new Label("Manage Rooms");
        lblHeader.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblHeader.setTextFill(Color.web("#2c3e50"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label adminName = new Label("Admin: " + admin.getName());
        adminName.setStyle("-fx-font-weight: bold; -fx-text-fill: #7f8c8d;");

        header.getChildren().addAll(lblHeader, spacer, adminName);
        return header;
    }

    public VBox getForm() {
        VBox root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f4f7f6;");
        root.setAlignment(Pos.TOP_CENTER);

        VBox formCard = new VBox(15);
        formCard.setMaxWidth(500);
        formCard.setPadding(new Insets(35));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 15, 0, 0, 5);");

        Label title = new Label("Add New Room");
        title.setFont(Font.font("System", FontWeight.BOLD, 22));
        title.setPadding(new Insets(0,0,10,0));

        TextField roomNum = createStyledField("Enter Room Number");
        TextField roomPrice = createStyledField("Enter Price per Night");

        ComboBox<String> roomType = new ComboBox<>();
        roomType.getItems().addAll("Single", "Double", "Suite", "Deluxe");
        roomType.setPromptText("Select Room Type");
        roomType.setMaxWidth(Double.MAX_VALUE);
        roomType.setPrefHeight(40);

        Button submitBtn = new Button("Add Room");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

//        submitBtn.setOnAction(e -> {
//            try {
//                int num = Integer.parseInt(roomNum.getText());
//                double price = Double.parseDouble(roomPrice.getText());
//                String type = roomType.getValue();
//                if (type == null) throw new Exception("Please select a room type.");
//
//                // Admin components call
//                admin.addRoom(new Room(num, type, price, true));
//
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Room Added Successfully!");
//                alert.show();
//            } catch (Exception ex) {
//                new Alert(Alert.AlertType.ERROR, "Invalid Input: " + ex.getMessage()).show();
//            }
//        });

        formCard.getChildren().addAll(title, new Label("Room Number"), roomNum,
                new Label("Type"), roomType,
                new Label("Price"), roomPrice, submitBtn);

        root.getChildren().add(formCard);
        return root;
    }

    private Button createSideButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-padding: 12 15; -fx-cursor: hand; -fx-font-size: 14px;");
        return btn;
    }

    private TextField createStyledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");
        return tf;
    }

    public static void main(String[] args) {
        launch(args);
    }
}