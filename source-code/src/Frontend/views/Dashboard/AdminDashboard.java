package Frontend.views.Dashboard;

import FileHandling.*;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AdminDashboard implements Dashboard {

    @Override
    public BorderPane getLayout(Stage stage) {
        VBox rootContainer = new VBox(30);
        rootContainer.setPadding(new Insets(35));
        rootContainer.setStyle("-fx-background-color: #f8fafc;");

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(5);
        Label mainTitle = new Label("Management Console");
        mainTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label welcomeMsg = new Label("Welcome back, " + UserSession.getLoggedUserName() + " | System Overview");
        welcomeMsg.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");
        titleBox.getChildren().addAll(mainTitle, welcomeMsg);
        headerBox.getChildren().add(titleBox);

        var allUsers = AuthService.getAllUsers();
        long totalCustomers = allUsers.stream().filter(u -> u.getRole().equalsIgnoreCase("Customer")).count();
        int totalRooms = RoomService.getAllRooms().size();
        long subAdminCount = allUsers.stream().filter(u -> u.getRole().equalsIgnoreCase("SubAdmin")).count();

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);

        statsGrid.add(createModernCard("Revenue", "Rs " + AuthService.calculateTotalRevenue(), "Total Earnings", "#4f46e5"), 0, 0);
        statsGrid.add(createModernCard("Total Rooms", totalRooms + " Rooms", "Full Inventory", "#06b6d4"), 1, 0);
        statsGrid.add(createModernCard("Total Customers", String.valueOf(totalCustomers), "Active Clients", "#ec4899"), 2, 0);
        statsGrid.add(createModernCard("Bookings", String.valueOf(BookingService.getTotalBookingsCount()), "Total Records", "#f59e0b"), 3, 0);

        VBox activityFeed = new VBox(20);
        activityFeed.setPadding(new Insets(25));
        activityFeed.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label feedTitle = new Label("System Live Feed");
        feedTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox itemsContainer = new VBox(10);
        itemsContainer.getChildren().addAll(
                createModernActivity("Inventory Sync", "Total rooms: " + totalRooms, "Room Data", "#6366f1"),
                createModernActivity("User Audit", "Total SubAdmins: " + subAdminCount, "Staff Data", "#8b5cf6"),
                createModernActivity("Customer Log", "Total Customers: " + totalCustomers, "User Data", "#ec4899")
        );

        activityFeed.getChildren().addAll(feedTitle, new Separator(), itemsContainer);
        rootContainer.getChildren().addAll(headerBox, statsGrid, activityFeed);

        Layout layout = new Layout(stage);
        BorderPane finalLayout = layout.createLayout(rootContainer, UserSession.getLoggedUserRole(), UserSession.getLoggedUserName());

        ScrollPane scroll = new ScrollPane(finalLayout.getCenter());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: #f8fafc; -fx-border-color: transparent;");
        finalLayout.setCenter(scroll);

        return finalLayout;
    }

    private VBox createModernCard(String title, String value, String subtext, String hexColor) {
        VBox card = new VBox(12);
        card.setPrefWidth(220);
        card.setPadding(new Insets(20));
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web(hexColor, 0.1)), new Stop(1, Color.web(hexColor, 0.02)));
        card.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(15), Insets.EMPTY)));
        card.setStyle("-fx-border-color: " + hexColor + "33; -fx-border-width: 1; -fx-border-radius: 15;");
        Label lblTitle = new Label(title.toUpperCase());
        lblTitle.setStyle("-fx-text-fill: " + hexColor + "; -fx-font-size: 11px; -fx-font-weight: bold;");
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 22px; -fx-font-weight: 800; -fx-text-fill: #0f172a;");
        Label lblSub = new Label(subtext);
        lblSub.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        card.getChildren().addAll(lblTitle, lblValue, lblSub);
        return card;
    }

    private HBox createModernActivity(String title, String desc, String tag, String color) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 12;");
        Rectangle bar = new Rectangle(4, 35, Color.web(color));
        bar.setArcWidth(4); bar.setArcHeight(4);
        VBox txt = new VBox(2);
        Label t = new Label(title); t.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label d = new Label(desc); d.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        txt.getChildren().addAll(t, d);
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
        Label tg = new Label(tag); tg.setStyle("-fx-background-color: "+color+"15; -fx-text-fill: "+color+"; -fx-padding: 4 8; -fx-background-radius: 5; -fx-font-size: 10px;");
        row.getChildren().addAll(bar, txt, s, tg);
        return row;
    }
}