package Frontend.views;

import FileHandling.AuthService;
import backend.Sessions.UserSession;
import Frontend.views.Dashboard.Layout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Profile {

    public BorderPane getLayout(Stage stage) {
        VBox content = new VBox(30);
        content.setPadding(new Insets(40));
        content.setAlignment(Pos.TOP_CENTER);
        content.setStyle("-fx-background-color: #f8fafc;");

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Circle profileImg = new Circle(50, Color.web("#6366f1"));
        Label lblInitial = new Label(UserSession.getLoggedUserName().substring(0, 1).toUpperCase());
        lblInitial.setStyle("-fx-font-size: 40px; -fx-text-fill: white; -fx-font-weight: bold;");

        StackPane photoContainer = new StackPane(profileImg, lblInitial);

        Label nameLabel = new Label(UserSession.getLoggedUserName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Label roleLabel = new Label(UserSession.getLoggedUserRole().toUpperCase());
        roleLabel.setStyle("-fx-background-color: #e0e7ff; -fx-text-fill: #4338ca; -fx-padding: 5 15; -fx-background-radius: 20; -fx-font-size: 12px; -fx-font-weight: bold;");

        headerBox.getChildren().addAll(photoContainer, nameLabel, roleLabel);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(20);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setMaxWidth(600);
        formGrid.setPadding(new Insets(30));
        formGrid.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        TextField txtUsername = createStyledField(UserSession.getLoggedUserName(), "Username");
        txtUsername.setEditable(false);

        PasswordField txtOldPass = createStyledPasswordField("Current Password");
        PasswordField txtNewPass = createStyledPasswordField("New Password");
        PasswordField txtConfirmPass = createStyledPasswordField("Confirm New Password");

        formGrid.add(new Label("Account Username:"), 0, 0);
        formGrid.add(txtUsername, 1, 0);
        formGrid.add(new Label("Current Password:"), 0, 1);
        formGrid.add(txtOldPass, 1, 1);
        formGrid.add(new Label("New Password:"), 0, 2);
        formGrid.add(txtNewPass, 1, 2);
        formGrid.add(new Label("Confirm Password:"), 0, 3);
        formGrid.add(txtConfirmPass, 1, 3);

        Button btnUpdate = new Button("Update Profile");
        btnUpdate.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 30; -fx-background-radius: 8; -fx-cursor: hand;");

        btnUpdate.setOnAction(e -> {
            if(txtNewPass.getText().equals(txtConfirmPass.getText()) && !txtNewPass.getText().isEmpty()) {
                boolean success = AuthService.updateUserPassword(UserSession.getLoggedUserName(), txtOldPass.getText(), txtNewPass.getText());
                if(success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Incorrect current password!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "Passwords do not match!");
            }
        });

        content.getChildren().addAll(headerBox, formGrid, btnUpdate);

        Layout layoutHandler = new Layout(stage);
        return layoutHandler.createLayout(content, UserSession.getLoggedUserRole(), UserSession.getLoggedUserName());
    }

    private TextField createStyledField(String value, String prompt) {
        TextField field = new TextField(value);
        field.setPromptText(prompt);
        field.setPrefWidth(300);
        field.setStyle("-fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #e2e8f0; -fx-border-radius: 5;");
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.setPrefWidth(300);
        field.setStyle("-fx-padding: 10; -fx-background-radius: 5; -fx-border-color: #e2e8f0; -fx-border-radius: 5;");
        return field;
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}