package Frontend.views.Dashboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SubAdminDashboard implements Dashboard {

    @Override
    public BorderPane getLayout(Stage stage) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: transparent;"); // Background layout handle karega

        Label welcomeLabel = new Label("Staff Operations Portal");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox quickActions = new HBox(20);
        quickActions.getChildren().addAll(
                createStatusCard("Rooms to Clean", "8", "#3b82f6"),
                createStatusCard("Today's Check-ins", "14", "#10b981"),
                createStatusCard("Today's Check-outs", "6", "#6366f1")
        );

        HBox tablesRow = new HBox(20);
        VBox.setVgrow(tablesRow, Priority.ALWAYS);

        VBox taskList = new VBox(15);
        taskList.setPadding(new Insets(20));
        HBox.setHgrow(taskList, Priority.ALWAYS);
        taskList.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label taskTitle = new Label("Operational Tasks");
        taskTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        taskList.getChildren().addAll(
                taskTitle,
                createTaskItem("Verify Room 302 Maintenance", "High Priority"),
                createTaskItem("Restock Mini-bar Supplies", "Normal"),
                createTaskItem("Update Laundry Logs", "Routine"),
                createTaskItem("Inspect Suite 501", "Urgent")
        );

        VBox roomOverview = new VBox(15);
        roomOverview.setMinWidth(300);
        roomOverview.setPadding(new Insets(20));
        roomOverview.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label roomTitle = new Label("Room Status Summary");
        roomTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #334155;");

        roomOverview.getChildren().addAll(
                roomTitle,
                createStatusIndicator("Occupied", 0.75, "#10b981"),
                createStatusIndicator("Cleaning", 0.15, "#3b82f6"),
                createStatusIndicator("Maintenance", 0.10, "#ef4444")
        );

        tablesRow.getChildren().addAll(taskList, roomOverview);
        content.getChildren().addAll(welcomeLabel, quickActions, tablesRow);
        Layout layoutHandler = new Layout(stage);
        return layoutHandler.createLayout(content, "Staff", "Staff Member");
    }

    private VBox createStatusCard(String title, String count, String color) {
        VBox card = new VBox(5);
        card.setPrefWidth(200);
        card.setPadding(new Insets(15, 20, 15, 20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-border-color: " + color + "; -fx-border-width: 0 0 0 4; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 2);");

        Label lblTitle = new Label(title);
        lblTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");

        Label lblCount = new Label(count);
        lblCount.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        card.getChildren().addAll(lblTitle, lblCount);
        return card;
    }

    private HBox createTaskItem(String task, String priority) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 15, 10, 15));
        row.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8;");

        CheckBox cb = new CheckBox();
        Label lblTask = new Label(task);
        lblTask.setStyle("-fx-text-fill: #334155;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lblPri = new Label(priority);
        String priColor = priority.contains("Urgent") || priority.contains("High") ? "#fee2e2" : "#e2e8f0";
        String txtColor = priority.contains("Urgent") || priority.contains("High") ? "#ef4444" : "#475569";

        lblPri.setStyle("-fx-font-size: 11px; -fx-padding: 2 8; -fx-background-color: " + priColor + "; " +
                "-fx-background-radius: 10; -fx-text-fill: " + txtColor + "; -fx-font-weight: bold;");

        row.getChildren().addAll(cb, lblTask, spacer, lblPri);
        return row;
    }

    private VBox createStatusIndicator(String label, double progress, String color) {
        VBox container = new VBox(5);
        HBox textRow = new HBox();
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label pct = new Label((int)(progress * 100) + "%");
        pct.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        textRow.getChildren().addAll(lbl, sp, pct);

        ProgressBar pb = new ProgressBar(progress);
        pb.setMaxWidth(Double.MAX_VALUE);
        pb.setPrefHeight(8);
        pb.setStyle("-fx-accent: " + color + ";");

        container.getChildren().addAll(textRow, pb);
        return container;
    }
}