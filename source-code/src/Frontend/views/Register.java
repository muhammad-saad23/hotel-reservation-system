package Frontend.views;

import FileHandling.AuthService;
import backend.Roles.SubAdmin;
import backend.RoomsManagement.RoomManagement;
import Frontend.views.Dashboard.CustomerDashboard;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class Register {

    public Parent getLayout(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #dde5eb;");

        VBox regCard = new VBox(10);
        regCard.setMaxWidth(380);
        regCard.setPadding(new Insets(40));
        regCard.setAlignment(Pos.CENTER);
        regCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

        Label title = new Label("Staff Registration");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        Label subtitle = new Label("Create a new Sub-Admin account.");
        subtitle.setTextFill(Color.web("#777777"));

        TextField nameF = new TextField();
        nameF.setPromptText("Full Name");
        applyInputStyle(nameF);

        TextField phoneF = new TextField();
        phoneF.setPromptText("Phone Number");
        applyInputStyle(phoneF);
        phoneF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneF.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TextField emailF = new TextField();
        emailF.setPromptText("Email Address");
        applyInputStyle(emailF);

        PasswordField passF = new PasswordField();
        passF.setPromptText("Password");
        applyInputStyle(passF);

        Button regBtn = new Button("Register");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setPrefHeight(45);
        regBtn.setStyle("-fx-background-color: #134e4a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");

        regBtn.setOnAction(e -> {
            String email = emailF.getText().trim();
            String name = nameF.getText().trim();
            String phone = phoneF.getText().trim();
            String pass = passF.getText().trim();

            if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Empty Fields", "Please fill all fields.");
                return;
            }

            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
                return;
            }

            if (AuthService.isEmailExists(email)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Email", "This email is already registered.");
                return;
            }

            if (AuthService.isPhoneExists(phone)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Phone", "This phone number is already registered.");
                return;
            }

            SubAdmin newStaff = new SubAdmin(AuthService.getNextId(), name, email, phone, pass, "SubAdmin", new RoomManagement(), "None");

            if (AuthService.registerUser(newStaff)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Account Created Successfully!");
                stage.getScene().setRoot(new Login().getLayout(stage));
            } else {
                showAlert(Alert.AlertType.ERROR, "System Error", "Failed to save data.");
            }
        });

        Hyperlink loginLink = new Hyperlink("Log In");
        loginLink.setStyle("-fx-text-fill: #1e90ff; -fx-font-weight: bold;");
        loginLink.setOnAction(e -> stage.getScene().setRoot(new Login().getLayout(stage)));

        HBox loginFooter = new HBox(new Text("Already have an account? "), loginLink);
        loginFooter.setAlignment(Pos.CENTER);

        Separator sep = new Separator();
        Hyperlink viewWebsite = new Hyperlink("🌐 View Website (Guest Access)");
        viewWebsite.setStyle("-fx-text-fill: #134e4a; -fx-font-weight: bold;");
        viewWebsite.setOnAction(e -> {
            UserSession.setSession(0, "Guest", "guest@hotel.com", "0000", "CUSTOMER", null);
            stage.getScene().setRoot(new CustomerDashboard().getLayout(stage));
        });

        regCard.getChildren().addAll(title, subtitle, nameF, phoneF, emailF, passF, regBtn, loginFooter, sep, viewWebsite);
        root.getChildren().add(regCard);
        return root;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    private void applyInputStyle(Control input) {
        input.setPrefHeight(42);
        input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 0 12;");
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}