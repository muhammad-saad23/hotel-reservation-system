package Frontend.views.Admin.Auth;

import FileHandling.AuthService;
import backend.Roles.Admin;
import backend.Roles.SubAdmin;
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

public class Register extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getLayout(stage), 800, 600);
        stage.setTitle("Hotel System - Admin Register");
        stage.setScene(scene);
        stage.show();
    }

    public HBox getLayout(Stage stage) {
        VBox leftSide = new VBox(10);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPrefWidth(400);
        leftSide.setStyle("-fx-background-color: linear-gradient(to bottom right, #4facfe, #00f2fe);");

        Text welcomeText = new Text("JOIN US");
        welcomeText.setFont(Font.font("System", FontWeight.BOLD, 36));
        welcomeText.setFill(Color.WHITE);

        Text subText = new Text("Create your Admin Account");
        subText.setFill(Color.WHITE);
        leftSide.getChildren().addAll(welcomeText, subText);

        VBox rightSide = new VBox(15);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(400);
        rightSide.setPadding(new Insets(30));
        rightSide.setStyle("-fx-background-color: white;");

        Label title = new Label("Admin Registration");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));


        TextField nameField = createStyledField("Full Name");
        TextField emailField = createStyledField("Email Address");
        TextField phoneField = createStyledField("Phone Number");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        styleInput(passField);


        Button regBtn = new Button("CREATE ACCOUNT");
        regBtn.setPrefWidth(300);
        regBtn.setCursor(javafx.scene.Cursor.HAND);
        regBtn.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 5;");

        regBtn.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String password = passField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Registration Error", "Please fill in all required fields.");
                return;
            }


            SubAdmin newSubAdmin = new SubAdmin(name, email, phone, password , new RoomManagement());


            if (AuthService.registerUser(newSubAdmin)) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Your Account Created Successfully!");

            }
        });

        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.setStyle("-fx-text-fill: #4facfe;");
        loginLink.setOnAction(e -> {
            Login loginPage = new Login();
            stage.getScene().setRoot(loginPage.getLayout(stage));
        });

        rightSide.getChildren().addAll(title, nameField, emailField, phoneField, passField, regBtn, loginLink);

        HBox root = new HBox();
        root.getChildren().addAll(leftSide, rightSide);
        return root;
    }

    private TextField createStyledField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        styleInput(field);
        return field;
    }

    private void styleInput(Control input) {
        input.setPrefWidth(300);
        input.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #e0e0e0; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 10;");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}