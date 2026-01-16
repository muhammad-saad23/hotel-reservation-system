package Frontend.views.Forms;

import FileHandling.AuthService;
import Frontend.views.Dashboard.Layout;
import backend.Roles.User;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class ManageAccess implements FormUi {
    private ComboBox<String> subAdminSelector;
    private CheckBox checkViewRooms, checkAddRooms, checkEditRooms, checkDeleteRooms;
    private CheckBox checkBookings, checkReports;
    private List<User> allUsers;

    @Override
    public Parent getLayout(Stage stage) {
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: #f1f5f9;");

        VBox card = new VBox(20);
        card.setMaxWidth(550);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("🔐 Manage Sub-Admin Access");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        allUsers = AuthService.getAllUsers();
        List<String> subAdminNames = allUsers.stream()
                .filter(u -> u.getRole().equalsIgnoreCase("SubAdmin"))
                .map(u -> u.getName() + " (ID: " + u.getId() + ")")
                .collect(Collectors.toList());

        subAdminSelector = new ComboBox<>();
        subAdminSelector.setPromptText("Select Sub-Admin to Modify");
        subAdminSelector.getItems().addAll(subAdminNames);
        subAdminSelector.setMaxWidth(Double.MAX_VALUE);
        subAdminSelector.setPrefHeight(45);

        VBox permGrid = new VBox(15);
        permGrid.setPadding(new Insets(15));
        permGrid.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 10; -fx-border-color: #e2e8f0;");

        Label roomSection = new Label("Room Management Access:");
        roomSection.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");

        checkViewRooms = new CheckBox("View Rooms (Compulsory)");
        checkViewRooms.setSelected(true);
        checkViewRooms.setDisable(true);

        checkAddRooms = new CheckBox("Add New Rooms");
        checkEditRooms = new CheckBox("Update/Edit Rooms");
        checkDeleteRooms = new CheckBox("Delete Rooms");

        Separator sep = new Separator();

        Label otherSection = new Label("Other Management Access:");
        otherSection.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");

        checkBookings = new CheckBox("Manage Bookings");
        checkReports = new CheckBox("View Financial Reports");

        permGrid.getChildren().addAll(
                roomSection, checkViewRooms, checkAddRooms, checkEditRooms, checkDeleteRooms,
                sep,
                otherSection, checkBookings, checkReports
        );

        subAdminSelector.setOnAction(e -> loadCurrentPermissions());

        Button updateBtn = new Button("UPDATE PERMISSIONS");
        updateBtn.setMaxWidth(Double.MAX_VALUE);
        updateBtn.setPrefHeight(50);
        updateBtn.setStyle("-fx-background-color: #0f172a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        updateBtn.setOnAction(e -> submit());

        card.getChildren().addAll(title, new Separator(), new Label("Select Sub-Admin Account:"), subAdminSelector, permGrid, updateBtn);
        mainContent.getChildren().add(card);

        return new Layout(stage).createLayout(mainContent, "Admin", UserSession.getLoggedUserName());
    }

    private void loadCurrentPermissions() {
        String selected = subAdminSelector.getValue();
        if (selected == null) return;
        int id = Integer.parseInt(selected.split("ID: ")[1].replace(")", ""));
        User user = allUsers.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
        if (user != null) {
            String p = user.getPermissions();
            checkAddRooms.setSelected(p.contains("AddRooms"));
            checkEditRooms.setSelected(p.contains("EditRooms"));
            checkDeleteRooms.setSelected(p.contains("DeleteRooms"));
            checkBookings.setSelected(p.contains("Bookings"));
            checkReports.setSelected(p.contains("Reports"));
        }
    }

    @Override
    public boolean validate() {
        if (subAdminSelector.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a Sub-Admin from the list.");
            return false;
        }
        return true;
    }

    @Override
    public void submit() {
        if (!validate()) return;

        String selected = subAdminSelector.getValue();
        int id = Integer.parseInt(selected.split("ID: ")[1].replace(")", ""));

        StringBuilder newPerms = new StringBuilder("ViewRooms"); // Always included

        if (checkAddRooms.isSelected()) newPerms.append("|AddRooms");
        if (checkEditRooms.isSelected()) newPerms.append("|EditRooms");
        if (checkDeleteRooms.isSelected()) newPerms.append("|DeleteRooms");
        if (checkBookings.isSelected()) newPerms.append("|Bookings");
        if (checkReports.isSelected()) newPerms.append("|Reports");

        if (AuthService.updateUserPermissions(id, newPerms.toString())) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Access levels updated for ID: " + id);
        } else {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Could not save permissions to database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}