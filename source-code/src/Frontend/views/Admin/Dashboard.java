package Frontend.views.Admin;

import Frontend.views.Admin.Auth.Login;
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

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getLayout(stage), 1100, 700);
        stage.setTitle("Hotel Management - Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane getLayout(Stage stage) {
        BorderPane root = new BorderPane();


        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label logo = new Label("LUXE HOTEL");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("System", FontWeight.BOLD, 22));
        logo.setPadding(new Insets(0, 0, 30, 0));
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);

        Button btnDash = createSideButton("📊 Dashboard", true);
        Button btnRooms = createSideButton("🏨 Manage Rooms", false);
        Button btnBookings = createSideButton("📅 Bookings", false);
        Button btnUsers = createSideButton("👥 Staff Members", false);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("🚪 Logout");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        btnLogout.setOnAction(e -> {
            Login loginPage = new Login();
            stage.getScene().setRoot(loginPage.getLayout(stage));
        });

        sidebar.getChildren().addAll(logo, btnDash, btnRooms, btnBookings, btnUsers, spacer, btnLogout);

        HBox header = new HBox();
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(70);
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label lblHeader = new Label("Dashboard Overview");
        lblHeader.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblHeader.setTextFill(Color.web("#2c3e50"));

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Search data...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-background-color: #f4f7f6; -fx-background-radius: 15; -fx-padding: 8 12; -fx-border-color: #e0e0e0; -fx-border-radius: 15;");

        header.getChildren().addAll(lblHeader, headerSpacer, searchField);

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f4f7f6;");

        HBox statsRow = new HBox(25);
        statsRow.getChildren().addAll(
                createStatCard("Total Bookings", "1,240", "#3498db"),
                createStatCard("Available Rooms", "12", "#2ecc71"),
                createStatCard("Pending Requests", "5", "#e67e22"),
                createStatCard("Monthly Revenue", "$15,250", "#9b59b6")
        );

        VBox tableContainer = new VBox(15);
        tableContainer.setPadding(new Insets(20));
        tableContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label tableLabel = new Label("Recent Activity");
        tableLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<Object> table = new TableView<>();
        table.setPlaceholder(new Label("No recent data available."));
        table.setPrefHeight(350);

        tableContainer.getChildren().addAll(tableLabel, table);
        content.getChildren().addAll(statsRow, tableContainer);


        root.setLeft(sidebar);
        root.setTop(header);
        root.setCenter(content);

        return root;
    }

    private Button createSideButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        String idleStyle = "-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-padding: 12 15; -fx-cursor: hand; -fx-font-size: 14px;";
        String activeStyle = "-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 12 15; -fx-cursor: hand; -fx-font-size: 14px; -fx-border-color: #3498db; -fx-border-width: 0 0 0 5;";

        btn.setStyle(active ? activeStyle : idleStyle);


        if (!active) {
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 12 15;"));
            btn.setOnMouseExited(e -> btn.setStyle(idleStyle));
        }

        return btn;
    }

    private VBox createStatCard(String title, String value, String colorHex) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 15, 0, 0, 5);");

        Label lblTitle = new Label(title);
        lblTitle.setTextFill(Color.GRAY);
        lblTitle.setFont(Font.font("System", 14));

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("System", FontWeight.BOLD, 26));
        lblValue.setTextFill(Color.web(colorHex));

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}