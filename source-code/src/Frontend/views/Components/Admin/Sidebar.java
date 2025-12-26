package Frontend.views.Components.Admin;

import Frontend.views.Admin.Auth.Login;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Sidebar extends VBox {

    public Sidebar(Stage stage) {
        this.setPrefWidth(240);
        this.setPadding(new Insets(20));
        this.setSpacing(10);
        this.setStyle("-fx-background-color: #2c3e50;");


        Label brandName = new Label("HOTEL ADMIN");
        brandName.setTextFill(Color.WHITE);
        brandName.setFont(Font.font("System", FontWeight.BOLD, 22));
        brandName.setPadding(new Insets(0, 0, 30, 0));
        brandName.setMaxWidth(Double.MAX_VALUE);
        brandName.setAlignment(Pos.CENTER);


        Button btnDashboard = createNavButton("📊 Dashboard");
        Button btnRooms = createNavButton("🏨 Manage Rooms");
        Button btnBookings = createNavButton("📅 Bookings");
        Button btnUsers = createNavButton("👥 Staff Members");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("🚪 Logout");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");

        btnLogout.setOnAction(e -> {
            Login login = new Login();
            stage.getScene().setRoot(login.getLayout(stage));
        });

        this.getChildren().addAll(brandName, btnDashboard, btnRooms, btnBookings, btnUsers, spacer, btnLogout);
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        String idleStyle = "-fx-background-color: transparent; -fx-text-fill: #ecf0f1; " +
                "-fx-font-size: 14px; -fx-padding: 12 15; -fx-background-radius: 5; -fx-cursor: hand;";

        String hoverStyle = "-fx-background-color: #34495e; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 12 15; -fx-background-radius: 5; -fx-cursor: hand;";

        btn.setStyle(idleStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(idleStyle));

        return btn;
    }
}