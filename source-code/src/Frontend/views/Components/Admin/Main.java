package Frontend.views.Components.Admin;

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

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getMainLayout(stage, "Dashboard Overview"), 1100, 700);
        stage.setTitle("Hotel Management System - Admin");
        stage.setScene(scene);
        stage.show();
    }

    public BorderPane getMainLayout(Stage stage, String pageTitle) {
        BorderPane root = new BorderPane();

        root.setLeft(createSidebar(stage));

        root.setTop(createTopbar(pageTitle));


        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(30));
        centerContent.setStyle("-fx-background-color: #f4f7f6;");

        Label welcome = new Label("Welcome back, Admin!");
        welcome.setFont(Font.font("System", FontWeight.BOLD, 22));
        centerContent.getChildren().add(welcome);

        root.setCenter(centerContent);

        return root;
    }


    private VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");

        Label brand = new Label("LUXE HOTEL");
        brand.setTextFill(Color.WHITE);
        brand.setFont(Font.font("System", FontWeight.BOLD, 22));
        brand.setPadding(new Insets(0, 0, 30, 0));


        sidebar.getChildren().addAll(brand,
                createNavBtn("📊 Dashboard"),
                createNavBtn("🏨 Rooms"),
                createNavBtn("📅 Bookings"),
                createNavBtn("👥 Staff")
        );

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("🚪 Logout");
        logout.setMaxWidth(Double.MAX_VALUE);
        logout.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-cursor: hand;");
        logout.setOnAction(e -> {
            Login login = new Login();
            stage.getScene().setRoot(login.getLayout(stage));
        });

        sidebar.getChildren().addAll(spacer, logout);
        return sidebar;
    }

    private HBox createTopbar(String title) {
        HBox topbar = new HBox();
        topbar.setPadding(new Insets(15, 30, 15, 30));
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField search = new TextField();
        search.setPromptText("Search...");
        search.setStyle("-fx-background-radius: 15; -fx-background-color: #f1f3f4; -fx-padding: 8;");

        topbar.getChildren().addAll(lblTitle, spacer, search);
        return topbar;
    }

    private Button createNavBtn(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-padding: 12; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-padding: 12;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-padding: 12;"));
        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}