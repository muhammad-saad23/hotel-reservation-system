package Frontend.views.Customer.Auth;

import FileHandling.AuthService;
import backend.Roles.Customer;
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

public class Register extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #dde5eb;");

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

        Label title = new Label("Registration");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#333333"));

        Label subtitle = new Label("Welcome ! Please create your account.");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#777777"));
        subtitle.setPadding(new Insets(0, 0, 15, 0));

        HBox roleSelection = new HBox(10);
        roleSelection.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        applyInputStyle(usernameField);

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        applyInputStyle(phoneField);

        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        applyInputStyle(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        applyInputStyle(passwordField);

        Button regBtn = new Button("Create");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setPrefHeight(45);
        regBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");

        regBtn.setOnAction(e->{
             String name=usernameField.getText();
             String phone=phoneField.getText();
             String email=emailField.getText();
             String password=passwordField.getText();

            if (name.isEmpty() ||phone.isEmpty()||email.isEmpty()||password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR,"Registration Error","Please fill in all required fields.");
                return;

            }

            Customer newCustomer=new Customer(name,phone,email,password,new RoomManagement());
            if (AuthService.registerUser(newCustomer)) {
                showAlert(Alert.AlertType.INFORMATION,"Success","Your Account created Successfully");
            }
        });

        Text footerBase = new Text("Already have an account? ");
        Hyperlink signUpLink = new Hyperlink("Sign Up");
        signUpLink.setStyle("-fx-text-fill: #1e90ff; -fx-font-weight: bold; -fx-underline: false;");

        FlowPane footer = new FlowPane(footerBase, signUpLink);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(10, 0, 0, 0));

        loginCard.getChildren().addAll(title, subtitle, usernameField,phoneField, emailField, passwordField, regBtn, footer);

        root.getChildren().add(loginCard);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Hotel Reservation Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyInputStyle(Control input) {
        input.setPrefHeight(42);
        input.setStyle(
                "-fx-background-radius: 8; " +
                        "-fx-border-color: #ccc; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-color: white; " +
                        "-fx-padding: 0 12 0 12;"
        );

        input.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                input.setStyle(input.getStyle() + "-fx-border-color: #ff8c00;");
            } else {
                input.setStyle(input.getStyle() + "-fx-border-color: #ccc;");
            }
        });
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional: sets a header
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
