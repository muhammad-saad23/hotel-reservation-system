package Frontend.views;

import FileHandling.AuthService;
import Frontend.views.Dashboard.*;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class Login {

    public void show(Stage stage) {
        Scene scene = new Scene(getLayout(stage), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Royal Hotel - Login");
        stage.centerOnScreen();
        stage.show();
    }

    public Parent getLayout(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #dde5eb;");

        VBox loginCard = new VBox(15);
        loginCard.setMaxWidth(380);
        loginCard.setPadding(new Insets(40));
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

        Label title = new Label("Staff Login");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        Label subtitle = new Label("Enter your credentials to manage hotel.");
        subtitle.setTextFill(Color.web("#777777"));

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        applyInputStyle(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyInputStyle(passwordField);

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle("-fx-background-color: #134e4a; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
                return;
            }

            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address (e.g. user@example.com).");
                return;
            }

            backend.Roles.User user = AuthService.loginUser(email, password);

            if (user != null) {
                String role = user.getRole().trim().toUpperCase();

                if (role.equalsIgnoreCase("BLOCKED")) {
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "You are blocked! Kindly contact with Admin.");
                    return;
                }

                if (role.equalsIgnoreCase("CUSTOMER")) {
                    showAlert(Alert.AlertType.ERROR, "Not Allowed", "Customers are not allowed to access the Staff Portal.");
                    return;
                }

                UserSession.setSession(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole(), user.getPermissions());
                navigateToDashboard(stage, user.getRole());

            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
            }
        });

        Text footerBase = new Text("Don’t have an account? ");
        Hyperlink signUpLink = new Hyperlink("Sign Up");
        signUpLink.setStyle("-fx-text-fill: #1e90ff; -fx-font-weight: bold;");
        signUpLink.setOnAction(e -> stage.getScene().setRoot(new Register().getLayout(stage)));

        HBox signUpFooter = new HBox(footerBase, signUpLink);
        signUpFooter.setAlignment(Pos.CENTER);

        Separator sep = new Separator();
        Hyperlink viewWebsite = new Hyperlink("🌐 View Website (Guest Access)");
        viewWebsite.setStyle("-fx-text-fill: #134e4a; -fx-font-weight: bold;");
        viewWebsite.setOnAction(e -> openGuestWebsite(stage));

        loginCard.getChildren().addAll(title, subtitle, emailField, passwordField, loginBtn, signUpFooter, sep, viewWebsite);
        root.getChildren().add(loginCard);
        return root;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    private void openGuestWebsite(Stage stage) {
        UserSession.setSession(0, "Guest", "guest@hotel.com", "0000", "CUSTOMER", null);
        stage.getScene().setRoot(new CustomerDashboard().getLayout(stage));
    }

    private void navigateToDashboard(Stage stage, String role) {
        Dashboard db = role.equalsIgnoreCase("ADMIN") ? new AdminDashboard() : new SubAdminDashboard();
        stage.getScene().setRoot(db.getLayout(stage));
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