package Frontend.views.Customer.Auth;

import FileHandling.AuthService;
import backend.Roles.User;
import backend.RoomsManagement.RoomManagement;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #dde5eb;");

        // --- Card Container ---
        VBox loginCard = new VBox(10);
        loginCard.setMaxWidth(380);
        loginCard.setMaxHeight(Region.USE_PREF_SIZE);
        loginCard.setPadding(new Insets(40));
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 20; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);"
        );


        Label title = new Label("Log In");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#333333"));

        Label subtitle = new Label("Please enter your credentials to continue.");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#777777"));
        subtitle.setPadding(new Insets(0, 0, 15, 0));


        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        applyInputStyle(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyInputStyle(passwordField);

        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");


        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Please fill in all fields.");
            } else {
                backend.Roles.User user = AuthService.loginUser(email, password);

                if (user != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Welcome, " + user.getName());

                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
                }
            }
        });


        Text footerBase = new Text("Don’t have an account? ");
        Hyperlink signUpLink = new Hyperlink("Sign Up");
        signUpLink.setStyle("-fx-text-fill: #1e90ff; -fx-font-weight: bold; -fx-underline: false;");

        // Logic for Sign Up Link
        signUpLink.setOnAction(e -> {
            // Navigate to Register view
            new Register().start(primaryStage);
        });

        FlowPane footer = new FlowPane(footerBase, signUpLink);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));

        loginCard.getChildren().addAll(title, subtitle, emailField, passwordField, loginBtn, footer);
        root.getChildren().add(loginCard);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Hotel Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyInputStyle(Control input) {
        input.setPrefHeight(42);
        input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-color: white; -fx-padding: 0 12 0 12;");
        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                input.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-background-color: white; -fx-padding: 0 12 0 12; -fx-border-color: #ff8c00;");
            } else {
                input.setStyle("-fx-background-radius: 8; -fx-border-radius: 8; -fx-background-color: white; -fx-padding: 0 12 0 12; -fx-border-color: #ccc;");
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

    public static void main(String[] args) {
        launch(args);
    }
}