package Frontend.views;

import FileHandling.AuthService;
import backend.Roles.Customer;
import backend.RoomsManagement.RoomManagement;
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
import javafx.scene.control.Alert.AlertType;

public class Register {

    public Parent getLayout(Stage stage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #dde5eb;");

        VBox regCard = new VBox(10);
        regCard.setMaxWidth(380);
        regCard.setMaxHeight(Region.USE_PREF_SIZE);
        regCard.setPadding(new Insets(40));
        regCard.setAlignment(Pos.CENTER);
        regCard.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 10);");

        Label title = new Label("Customer Registration");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));

        Label subtitle = new Label("Join us! Create your account to book rooms.");
        subtitle.setTextFill(Color.web("#777777"));
        subtitle.setPadding(new Insets(0, 0, 15, 0));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Full Name");
        applyInputStyle(usernameField);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        applyInputStyle(phoneField);

        phoneField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                phoneField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        applyInputStyle(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyInputStyle(passwordField);

        Button regBtn = new Button("Create Account");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setPrefHeight(45);
        regBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-background-radius: 10; -fx-cursor: hand;");

        regBtn.setOnAction(e -> {
            String name = usernameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(AlertType.ERROR, "Registration Error", "Please fill in all fields.");
                return;
            }

            Customer newCustomer = new Customer(name, phone, email, password, "Customer", new RoomManagement());

            if (AuthService.registerUser(newCustomer)) {
                showAlert(AlertType.INFORMATION, "Success", "Customer account created successfully!");
                Login login = new Login();
                stage.getScene().setRoot(login.getLayout(stage));
            } else {
                showAlert(AlertType.ERROR, "Failed", "Registration failed. Please try again.");
            }
        });

        Text footerBase = new Text("Already have an account? ");
        Hyperlink loginLink = new Hyperlink("Log In");
        loginLink.setStyle("-fx-text-fill: #1e90ff; -fx-font-weight: bold;");

        loginLink.setOnAction(e -> {
            Login login = new Login();
            stage.getScene().setRoot(login.getLayout(stage));
        });

        HBox footer = new HBox(footerBase, loginLink);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));

        regCard.getChildren().addAll(title, subtitle, usernameField, phoneField, emailField, passwordField, regBtn, footer);
        root.getChildren().add(regCard);

        return root;
    }

    private void applyInputStyle(Control input) {
        input.setPrefHeight(42);
        input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; " +
                "-fx-background-color: white; -fx-padding: 0 12;");

        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                input.setStyle("-fx-background-radius: 8; -fx-border-color: #ff8c00; -fx-border-radius: 8; -fx-padding: 0 12;");
            } else {
                input.setStyle("-fx-background-radius: 8; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 0 12;");
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}