package Frontend.views.Dashboard;

import FileHandling.*;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.time.LocalDate;

public class SubAdminDashboard implements Dashboard {

    @Override
    public BorderPane getLayout(Stage stage) {
        VBox rootContainer = new VBox(25);
        rootContainer.setPadding(new Insets(35));
        rootContainer.setStyle("-fx-background-color: #f8fafc;");

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(5);
        Label mainTitle = new Label("Staff Operations Portal");
        mainTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label welcomeMsg = new Label("Operator: " + UserSession.getLoggedUserName() + " | Today: " + LocalDate.now());
        welcomeMsg.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");
        titleBox.getChildren().addAll(mainTitle, welcomeMsg);
        headerBox.getChildren().add(titleBox);

        var allUsers = AuthService.getAllUsers();
        long totalCustomers = allUsers.stream().filter(u -> u.getRole().equalsIgnoreCase("Customer")).count();
        int totalRooms = RoomService.getAllRooms().size();
        int availableRooms = AuthService.getAvailableRoomsCount();
        long todaysCheckins = BookingService.getAllBookingsAsObjects().stream()
                .filter(b -> b.getCheckIn() != null && b.getCheckIn().equals(LocalDate.now()))
                .count();

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);

        statsGrid.add(createStaffCard("Total Inventory", totalRooms + " Rooms", availableRooms + " Available", "#6366f1"), 0, 0);
        statsGrid.add(createStaffCard("Total Customers", totalCustomers + " Users", "Registered clients", "#ec4899"), 1, 0);
        statsGrid.add(createStaffCard("Today's Check-ins", String.valueOf(todaysCheckins), "Scheduled Arrivals", "#3b82f6"), 2, 0);

        HBox bottomSection = new HBox(25);
        VBox taskList = new VBox(15);
        HBox.setHgrow(taskList, Priority.ALWAYS);
        taskList.setPadding(new Insets(25));
        taskList.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label taskTitle = new Label("Operational Tasks");
        taskTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        VBox items = new VBox(10);
        items.getChildren().addAll(
                createModernTask("Room Service", "Verify available room count: " + availableRooms, "High", "#10b981"),
                createModernTask("Customer Help", "Check details for " + totalCustomers + " customers", "Normal", "#ec4899")
        );
        taskList.getChildren().addAll(taskTitle, new Separator(), items);

        VBox analytics = new VBox(20);
        analytics.setMinWidth(300);
        analytics.setPadding(new Insets(25));
        analytics.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");
        Label anaTitle = new Label("Room Statistics");
        anaTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        double occupiedRate = totalRooms > 0 ? (double) (totalRooms - availableRooms) / totalRooms : 0;
        analytics.getChildren().addAll(anaTitle, createStatusProgress("Occupancy", occupiedRate, "#ef4444"));

        bottomSection.getChildren().addAll(taskList, analytics);
        rootContainer.getChildren().addAll(headerBox, statsGrid, bottomSection);

        Layout layoutHandler = new Layout(stage);
        BorderPane finalLayout = layoutHandler.createLayout(rootContainer, UserSession.getLoggedUserRole(), UserSession.getLoggedUserName());
        ScrollPane scroll = new ScrollPane(finalLayout.getCenter());
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: #f8fafc; -fx-border-color: transparent;");
        finalLayout.setCenter(scroll);

        return finalLayout;
    }

    private VBox createStaffCard(String title, String value, String sub, String color) {
        VBox card = new VBox(10);
        card.setPrefWidth(245);
        card.setPadding(new Insets(20));
        LinearGradient grad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web(color, 0.1)), new Stop(1, Color.web(color, 0.02)));
        card.setBackground(new Background(new BackgroundFill(grad, new CornerRadii(15), Insets.EMPTY)));
        card.setStyle("-fx-border-color: " + color + "44; -fx-border-width: 1; -fx-border-radius: 15;");
        Label t = new Label(title.toUpperCase());
        t.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 10px; -fx-font-weight: bold;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 20px; -fx-font-weight: 800;");
        Label s = new Label(sub);
        s.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        card.getChildren().addAll(t, v, s);
        return card;
    }

    private HBox createModernTask(String title, String desc, String priority, String color) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12));
        row.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 12;");
        Rectangle indicator = new Rectangle(4, 35, Color.web(color));
        indicator.setArcWidth(4); indicator.setArcHeight(4);
        VBox txt = new VBox(2);
        Label t = new Label(title); t.setStyle("-fx-font-weight: bold;");
        Label d = new Label(desc); d.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        txt.getChildren().addAll(t, d);
        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
        Label p = new Label(priority);
        p.setStyle("-fx-background-color: "+color+"15; -fx-text-fill: "+color+"; -fx-padding: 4 10; -fx-background-radius: 20; -fx-font-size: 10px;");
        row.getChildren().addAll(indicator, txt, sp, p);
        return row;
    }

    private VBox createStatusProgress(String label, double val, String color) {
        VBox v = new VBox(8);
        HBox h = new HBox();
        Label l = new Label(label); l.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px;");
        Region s = new Region(); HBox.setHgrow(s, Priority.ALWAYS);
        Label p = new Label((int)(val * 100) + "%");
        h.getChildren().addAll(l, s, p);
        ProgressBar pb = new ProgressBar(val);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPrefHeight(7);
        pb.setStyle("-fx-accent: " + color + ";");
        v.getChildren().addAll(h, pb);
        return v;
    }
}