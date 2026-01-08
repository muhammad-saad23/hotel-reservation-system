package Frontend.views.Forms;

import FileHandling.AuthService;
import Frontend.views.Dashboard.Layout;
import backend.Roles.SubAdmin;
import backend.RoomsManagement.RoomManagement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;

public class SubAdminForm implements FormUi {

    @Override
    public Parent getLayout(Stage stage) {
        VBox formArea = getFormContent();
        Layout layoutHandler = new Layout(stage);
        return layoutHandler.createLayout(formArea, "Admin", "Admin User");
    }

    private VBox getFormContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        VBox formCard = new VBox(15);
        formCard.setMaxWidth(550);
        formCard.setPadding(new Insets(35));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.12), 20, 0, 0, 8);");

        Label title = new Label("👤 Create Sub-Admin");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #0f172a;");

        TextField nameField = createField("Full Name");
        TextField emailField = createField("Email Address (e.g. name@gmail.com)");
        TextField phoneField = createField("Phone Number (Digits only)");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().matches("[0-9]*")) return change;
            return null;
        };
        phoneField.setTextFormatter(new TextFormatter<>(filter));

        PasswordField passField = new PasswordField();
        passField.setPromptText("Assign Password");
        passField.setPrefHeight(45);
        passField.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

        Label permLabel = new Label("Access Permissions");
        permLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b;");

        CheckBox checkRooms = new CheckBox("Manage Rooms");
        CheckBox checkBookings = new CheckBox("Manage Bookings");
        CheckBox checkReports = new CheckBox("View Reports");
        VBox checks = new VBox(10, checkRooms, checkBookings, checkReports);
        checks.setPadding(new Insets(5, 0, 15, 0));

        Button createBtn = new Button("Confirm & Create Account");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setPrefHeight(50);
        createBtn.setStyle("-fx-background-color: #134e4a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");

        createBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = passField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
                return;
            }
            else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)\\.com$")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Email must contain '@' and end with '.com'");
                return;
            }
            else if (AuthService.isEmailExists(email)) {
                showAlert(Alert.AlertType.WARNING, "Duplicate Email", "This email is already registered. Please use a different one.");
                return;
            }


            StringBuilder perms = new StringBuilder();
            if (checkRooms.isSelected()) perms.append("Rooms");
            if (checkBookings.isSelected()) {
                if (perms.length() > 0) perms.append("|");
                perms.append("Bookings");
            }
            if (checkReports.isSelected()) {
                if (perms.length() > 0) perms.append("|");
                perms.append("Reports");
            }
            String finalPermissions = perms.toString().isEmpty() ? "None" : perms.toString();

            SubAdmin newSubAdmin = new SubAdmin(
                    AuthService.getNextId(),
                    name,
                    email,
                    phone,
                    password,
                    "SubAdmin",
                    new RoomManagement(),
                    finalPermissions
            );

            if (AuthService.registerUser(newSubAdmin)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Sub-Admin created successfully!");
                nameField.clear(); emailField.clear(); phoneField.clear(); passField.clear();
                checkRooms.setSelected(false); checkBookings.setSelected(false); checkReports.setSelected(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save user due to a system error.");
            }
        });

        formCard.getChildren().addAll(
                title, new Separator(),
                createLabel("Identity"), nameField,
                createLabel("Contact Info"), emailField, phoneField,
                createLabel("Security"), passField,
                new Separator(),
                permLabel, checks,
                createBtn
        );

        root.getChildren().add(formCard);
        return root;
    }

    private TextField createField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(45);
        tf.setStyle("-fx-background-color: #f9fafb; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");
        return tf;
    }

    private Label createLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #64748b; -fx-font-weight: bold; -fx-font-size: 12px;");
        return l;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override public void submit() {}
    @Override public boolean validate() { return true; }
}