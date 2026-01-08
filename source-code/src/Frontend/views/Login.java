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

        Label title = new Label("Log In");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        Label subtitle = new Label("Please enter your credentials to continue.");
        subtitle.setTextFill(Color.GRAY);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        applyInputStyle(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyInputStyle(passwordField);

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 10; -fx-padding: 12; -fx-cursor: hand;");

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields.");
            } else {
                backend.Roles.User user = AuthService.loginUser(email, password);

                if (user != null) {
                    UserSession.setSession(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                    );

                    String role = user.getRole().trim().toUpperCase();
                    Dashboard userDashboard = null;

                    switch (role) {
                        case "ADMIN":
                            userDashboard = new AdminDashboard();
                            break;
                        case "SUBADMIN":
                            userDashboard = new SubAdminDashboard();
                            break;
                        case "CUSTOMER":
                            userDashboard = new AdminDashboard();
                            break;
                        default:
                            showAlert(Alert.AlertType.ERROR, "Role Error", "Role '" + role + "' not recognized.");
                            return;
                    }

                    if (userDashboard != null) {
                        Parent layout = userDashboard.getLayout(stage);
                        Scene dashboardScene = new Scene(layout, 1280, 800);

                        stage.setScene(dashboardScene);
                        stage.centerOnScreen();
                        showAlert(Alert.AlertType.INFORMATION, "Login Success", "Welcome, " + user.getName());
                    }

                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                    passwordField.clear();
                }
            }
        });

        Text footerBase = new Text("Don’t have an account? ");
        Hyperlink signUpLink = new Hyperlink("Sign Up");

        signUpLink.setOnAction(e -> {
            Register register = new Register();
            stage.getScene().setRoot(register.getLayout(stage));
        });

        HBox footer = new HBox(footerBase, signUpLink);
        footer.setAlignment(Pos.CENTER);

        loginCard.getChildren().addAll(title, subtitle, emailField, passwordField, loginBtn, footer);
        root.getChildren().add(loginCard);

        return root;
    }

    private void applyInputStyle(Control input) {
        input.setPrefHeight(42);
        input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 0 12;");
        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                input.setStyle("-fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-padding: 0 12;");
            } else {
                input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 0 12;");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}