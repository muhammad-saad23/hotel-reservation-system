package Frontend.views.Dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class AdminDashboard implements Dashboard {

    @Override
    public BorderPane getLayout(Stage stage) {
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: transparent;");

        Label header = new Label("System Overview");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                createStatCard("Total Revenue", "$12,450", "#fbbf24"),
                createStatCard("Active Bookings", "48", "#38bdf8"),
                createStatCard("Available Rooms", "12", "#10b981"),
                createStatCard("Pending Requests", "5", "#ef4444")
        );

        VBox recentActivity = new VBox(15);
        recentActivity.setPadding(new Insets(20));
        recentActivity.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label activityTitle = new Label("Recent System Activity");
        activityTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        recentActivity.getChildren().addAll(
                activityTitle,
                createActivityItem("New Booking: Room 102 by John Doe", "2 mins ago"),
                createActivityItem("Payment Received: #BK-9920", "15 mins ago"),
                createActivityItem("Staff Login: Sarah (SubAdmin)", "1 hour ago")
        );

        content.getChildren().addAll(header, statsRow, recentActivity);

        Layout dashboardLayout = new Layout(stage);
        return dashboardLayout.createLayout(content, "Admin", "Admin User");
    }

    private VBox createStatCard(String title, String value, String colorCode) {
        VBox card = new VBox(10);
        card.setPrefWidth(220);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Circle indicator = new Circle(5, Color.web(colorCode));
        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");

        HBox titleBox = new HBox(8, indicator, lblTitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        card.getChildren().addAll(titleBox, lblValue);
        return card;
    }

    private HBox createActivityItem(String text, String time) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));
        row.setStyle("-fx-border-color: #f1f5f9; -fx-border-width: 0 0 1 0;");

        Label lblText = new Label(text);
        lblText.setStyle("-fx-text-fill: #475569;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblTime = new Label(time);
        lblTime.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        row.getChildren().addAll(lblText, spacer, lblTime);
        return row;
    }
}