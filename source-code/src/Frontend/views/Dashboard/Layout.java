package Frontend.views.Dashboard;

import Frontend.views.List.BookingList;
import Frontend.views.List.CustomerRoomList;
import Frontend.views.List.RoomList;
import Frontend.views.List.SubAdminList;
import Frontend.views.Login;
import Frontend.views.Profile;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Layout implements Dashboard {
    private Stage stage;

    public Layout(Stage stage) {
        this.stage = stage;
    }

    @Override
    public BorderPane getLayout(Stage stage) {
        String role = UserSession.getLoggedUserRole();
        String name = UserSession.getLoggedUserName();
        return createLayout(new VBox(), role, name);
    }

    public BorderPane createLayout(VBox mainContent, String role, String name) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8fafc;");

        if (role == null || role.equalsIgnoreCase("Customer")) {
            setupCustomerView(layout, mainContent);
        } else {
            setupStaffView(layout, mainContent, role, name);
        }

        return layout;
    }

    private void setupStaffView(BorderPane layout, VBox mainContent, String role, String name) {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setPrefWidth(260);

        boolean isAdmin = role.equalsIgnoreCase("Admin");
        sidebar.setStyle("-fx-background-color: " + (isAdmin ? "#0f172a" : "#1e293b") + "; -fx-background-radius: 0 25 25 0;");

        Label brand = new Label(isAdmin ? "🏨 ADMIN" : "🛎️ STAFF");
        brand.setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 30 0;");
        sidebar.getChildren().add(brand);

        addMenuButton(sidebar, "📊  Dashboard", e -> {
            if (isAdmin) this.stage.getScene().setRoot(new AdminDashboard().getLayout(this.stage));
            else this.stage.getScene().setRoot(new SubAdminDashboard().getLayout(this.stage));
        });

        String permissions = UserSession.getLoggedUserPermissions() != null ? UserSession.getLoggedUserPermissions() : "";
        if (isAdmin || permissions.toLowerCase().contains("rooms")) addMenuButton(sidebar, "🔑  Rooms Inventory", e -> new RoomList().show(this.stage));
        if (isAdmin || permissions.toLowerCase().contains("bookings")) addMenuButton(sidebar, "📅  Bookings List", e -> new BookingList().show(this.stage));

        if (isAdmin) {
            sidebar.getChildren().add(new Separator());
            addMenuButton(sidebar, "👥  Manage Staff", e -> new SubAdminList().show(this.stage));
        }

//        addMenuButton(sidebar, "🌐  View Website", e -> this.stage.getScene().setRoot(new CustomerDashboard().getLayout(this.stage)));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().addAll(spacer);

        addMenuButton(sidebar, "👤  My Profile", e -> this.stage.getScene().setRoot(new Profile().getLayout(this.stage)));
        Button btnLogout = createMenuBtn("🚪  Logout");
        btnLogout.setStyle("-fx-background-color: #e11d48; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 12;");
        btnLogout.setOnAction(e -> { UserSession.clearSession(); new Login().show(this.stage); });
        sidebar.getChildren().add(btnLogout);

        HBox topbar = new HBox(15);
        topbar.setPadding(new Insets(15, 30, 15, 30));
        topbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        topbar.setAlignment(Pos.CENTER_RIGHT);

        // Single Bold Back Button
        Button btnBack = createSingleBackButton(role);

        Region topSpacer = new Region();
        HBox.setHgrow(topSpacer, Priority.ALWAYS);

        String displayName = (name == null || name.isEmpty()) ? "User" : name;
        Label nameLabel = new Label(displayName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");

        topbar.getChildren().addAll(btnBack, topSpacer, nameLabel, createAvatar(displayName, isAdmin));

        layout.setLeft(sidebar);
        layout.setTop(topbar);
        layout.setCenter(mainContent);
    }

    private void setupCustomerView(BorderPane layout, VBox mainContent) {
        HBox topNav = new HBox(25);
        topNav.setPadding(new Insets(15, 80, 15, 80));
        topNav.setAlignment(Pos.CENTER_LEFT);
        topNav.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 2);");

        Button btnBack = createSingleBackButton("Customer");
        Label brand = new Label("✨ ROYAL HOTEL");
        brand.setStyle("-fx-text-fill: #1e1b4b; -fx-font-size: 24px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox menuLinks = new HBox(30);
        menuLinks.setAlignment(Pos.CENTER);
        addTopMenuButton(menuLinks, "Home", e -> this.stage.getScene().setRoot(new CustomerDashboard().getLayout(this.stage)));
        addTopMenuButton(menuLinks, "Rooms", e -> new CustomerRoomList().show(this.stage));

        topNav.getChildren().addAll(btnBack, brand, spacer, menuLinks);
        layout.setTop(topNav);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        layout.setCenter(scrollPane);
    }

    private Button createSingleBackButton(String role) {
        Button btn = new Button("←");
        String style = "-fx-background-color: #1e293b; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-min-width: 55; -fx-min-height: 45; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);";

        btn.setStyle(style);
        btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-background-color: #4f46e5; -fx-scale-x: 1.05; -fx-scale-y: 1.05;"));
        btn.setOnMouseExited(e -> btn.setStyle(style));

        btn.setOnAction(e -> handleBackNavigation(role));
        return btn;
    }

    private void handleBackNavigation(String role) {
        if (role == null) return;
        switch (role.toLowerCase()) {
            case "admin": this.stage.getScene().setRoot(new AdminDashboard().getLayout(this.stage)); break;
            case "subadmin": this.stage.getScene().setRoot(new SubAdminDashboard().getLayout(this.stage)); break;
            default: this.stage.getScene().setRoot(new CustomerDashboard().getLayout(this.stage)); break;
        }
    }

    private StackPane createAvatar(String name, boolean isAdmin) {
        StackPane avatar = new StackPane();
        Circle circle = new Circle(18, Color.web(isAdmin ? "#fbbf24" : "#38bdf8"));
        Label label = new Label(getInitials(name));
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        avatar.getChildren().addAll(circle, label);
        avatar.setCursor(javafx.scene.Cursor.HAND);
        avatar.setOnMouseClicked(e -> this.stage.getScene().setRoot(new Profile().getLayout(this.stage)));
        return avatar;
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty() || name.equals("User")) return "U";
        String[] p = name.trim().split("\\s+");
        return (p.length > 1) ? (p[0].substring(0,1) + p[p.length-1].substring(0,1)).toUpperCase() : p[0].substring(0,1).toUpperCase();
    }

    private void addTopMenuButton(HBox container, String text, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-font-weight: 600; -fx-cursor: hand;");
        btn.setOnAction(event);
        container.getChildren().add(btn);
    }

    private void addMenuButton(VBox container, String text, javafx.event.EventHandler<javafx.event.ActionEvent> event) {
        Button btn = createMenuBtn(text);
        btn.setOnAction(event);
        container.getChildren().add(btn);
    }

    private Button createMenuBtn(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-padding: 12; -fx-background-radius: 8; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: #fbbf24; -fx-padding: 12; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-padding: 12; -fx-background-radius: 8;"));
        return btn;
    }
}