package Frontend.views.Dashboard;

import Frontend.views.List.RoomList;
import Frontend.views.List.SubAdminList;
import Frontend.views.Login;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Layout implements Dashboard{
    private Stage stage;

    public Layout(Stage stage) {
        this.stage = stage;
    }

    @Override
    public BorderPane getLayout(Stage stage) {
        return createLayout(new VBox(new Label("Welcome")), "Guest", "User");
    }

    public BorderPane createLayout(VBox mainContent, String role, String name) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8fafc;");

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setPrefWidth(260);

        String sidebarColor;
        String brandTitle;

        if (role.equalsIgnoreCase("Admin")) {
            sidebarColor = "#0f172a";
            brandTitle = "🏨 ROYAL ADMIN";
        } else if (role.equalsIgnoreCase("Guest")) {
            sidebarColor = "#1e293b";
            brandTitle = "✨ MY STAY";
        } else {
            sidebarColor = "#134e4a";
            brandTitle = "🛎️ STAFF PORTAL";
        }

        sidebar.setStyle("-fx-background-color: " + sidebarColor + "; -fx-background-radius: 0 20 20 0;");

        Label brand = new Label(brandTitle);
        brand.setStyle("-fx-text-fill: #fbbf24; -fx-font-size: 22px; -fx-font-weight: bold; -fx-padding: 0 0 30 0;");
        sidebar.getChildren().add(brand);

        if (role.equalsIgnoreCase("Admin")) {
            addMenuButton(sidebar, "📊  Dashboard", e -> {
                AdminDashboard adminDashboard=new AdminDashboard();
                this.stage.getScene().setRoot(adminDashboard.getLayout(this.stage));
            });
            addMenuButton(sidebar, "🛌  Rooms", e -> {
                new RoomList().show(stage);
            });


            addMenuButton(sidebar, "👥 Staff", e ->{
                new SubAdminList().show(stage);
            });

        } else if (role.equalsIgnoreCase("SubAdmin")) {
            addMenuButton(sidebar, "📊  Dashboard", e -> {
                SubAdminDashboard subAdminDashboard=new SubAdminDashboard();
                this.stage.getScene().setRoot(subAdminDashboard.getLayout(this.stage));
            });
        }

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));
        sep.setOpacity(0.2);
        sidebar.getChildren().add(sep);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sidebar.getChildren().add(spacer);

        Button btnLogout = createMenuBtn("🚪  Sign Out");
        btnLogout.setStyle("-fx-background-color: #e11d48; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10; -fx-font-weight: bold;");

        btnLogout.setOnAction(e -> {
            UserSession.clearSession();
            Login login = new Login();
            login.show(this.stage);
        });
        sidebar.getChildren().add(btnLogout);

        HBox topbar = new HBox();
        topbar.setPadding(new Insets(15, 30, 15, 30));
        topbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 4);");
        topbar.setAlignment(Pos.CENTER_RIGHT);

        String displayName = UserSession.getLoggedUserName();
        if (displayName == null||displayName.isEmpty()) {
            displayName="Guest";
        }
        Label nameLabel = new Label(displayName);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155; -fx-font-size: 14px;");

        String currentRole=UserSession.getLoggedUserRole();
        String avatarColor = (currentRole!=null &&currentRole.equalsIgnoreCase("Admin"))? "#fbbf24" : "#38bdf8";

        Circle profileCircle = new Circle(18, Color.web(avatarColor));

        Label initial = new Label(displayName.substring(0, 1).toUpperCase());
        initial.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        StackPane avatar = new StackPane(profileCircle, initial);
        HBox userBox = new HBox(12, nameLabel, avatar);
        userBox.setAlignment(Pos.CENTER);
        topbar.getChildren().add(userBox);

        layout.setLeft(sidebar);
        layout.setTop(topbar);

        StackPane contentArea = new StackPane(mainContent);
        contentArea.setPadding(new Insets(25));
        layout.setCenter(contentArea);

        return layout;
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
        btn.setCursor(javafx.scene.Cursor.HAND);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 8;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: #fbbf24; -fx-padding: 12; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94a3b8; -fx-padding: 12; -fx-background-radius: 8;"));

        return btn;
    }
}