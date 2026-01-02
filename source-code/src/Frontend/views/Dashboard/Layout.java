package Frontend.views.Dashboard;

import Frontend.views.Login; // Login view ko import karna zaroori hai
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Layout {

    public BorderPane getLayout(Stage stage, String role, Node centerContent) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f7fa;");

        VBox sidebar = createSidebar(stage, role);
        HBox topbar = createTopbar(role);

        // Sidebar aur Topbar ke darmiyan content area
        VBox mainContent = new VBox(topbar, centerContent);
        VBox.setVgrow(centerContent, Priority.ALWAYS);

        root.setLeft(sidebar);
        root.setCenter(mainContent);

        return root;
    }

    private VBox createSidebar(Stage stage, String role) {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #1e293b;"); // Professional Dark Slate

        Label logo = new Label("LUXE STAY");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        logo.setPadding(new Insets(0, 0, 40, 0));
        logo.setAlignment(Pos.CENTER);
        logo.setMaxWidth(Double.MAX_VALUE);

        VBox navContainer = new VBox(10);

        // Common Buttons
        navContainer.getChildren().add(createNavButton("📊  Dashboard", true));
        navContainer.getChildren().add(createNavButton("🏨  Manage Rooms", false));
        navContainer.getChildren().add(createNavButton("📅  Bookings", false));

        // Role based buttons
        if (role.equalsIgnoreCase("ADMIN")) {
            navContainer.getChildren().add(createNavButton("👥  Staff Members", false));
            navContainer.getChildren().add(createNavButton("⚙️  Settings", false));
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Logout Button Logic
        Button btnLogout = new Button("🚪 Logout");
        btnLogout.setMaxWidth(Double.MAX_VALUE);
        btnLogout.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 10; -fx-cursor: hand;");

        btnLogout.setOnAction(e -> {
            // Wapas Login Screen par jane ke liye
            Login loginView = new Login();
            stage.getScene().setRoot(loginView.getLayout(stage));
        });

        sidebar.getChildren().addAll(logo, navContainer, spacer, btnLogout);
        return sidebar;
    }

    private HBox createTopbar(String role) {
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(15, 30, 15, 30));
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setMinHeight(80);
        topbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label pageTitle = new Label(role + " Portal");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        pageTitle.setTextFill(Color.web("#334155"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox profile = new HBox(12);
        profile.setAlignment(Pos.CENTER);

        Label name = new Label("Welcome, " + role);
        name.setStyle("-fx-font-weight: bold; -fx-text-fill: #475569;");

        Circle avatar = new Circle(20, Color.web("#cbd5e1"));

        profile.getChildren().addAll(name, avatar);
        topbar.getChildren().addAll(pageTitle, spacer, profile);

        return topbar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        String baseStyle = "-fx-background-radius: 10; -fx-padding: 12 15; -fx-font-size: 14px; -fx-cursor: hand;";
        String activeStyle = baseStyle + "-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold;";
        String idleStyle = baseStyle + "-fx-background-color: transparent; -fx-text-fill: #94a3b8;";
        String hoverStyle = baseStyle + "-fx-background-color: #334155; -fx-text-fill: white;";

        if (isActive) {
            btn.setStyle(activeStyle);
        } else {
            btn.setStyle(idleStyle);
            // Hover Effects
            btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(idleStyle));
        }

        return btn;
    }
}