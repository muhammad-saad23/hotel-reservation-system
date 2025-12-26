package Frontend.views.Admin.Auth;

import FileHandling.AuthService;
import backend.Roles.Admin;
import backend.Roles.User;
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

public class Login extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getLayout(stage), 800, 500);
        stage.setTitle("Hotel Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public HBox getLayout(Stage stage) {
        VBox leftSide = new VBox(10);
        leftSide.setAlignment(Pos.CENTER);
        leftSide.setPrefWidth(400);
        leftSide.setStyle("-fx-background-color: linear-gradient(to bottom right, #4facfe, #00f2fe);");

        Text welcomeText = new Text("WELCOME");
        welcomeText.setFont(Font.font("System", FontWeight.BOLD, 36));
        welcomeText.setFill(Color.WHITE);

        Text subText = new Text("Hotel Management System");
        subText.setFill(Color.WHITE);
        leftSide.getChildren().addAll(welcomeText, subText);

        VBox rightSide = new VBox(20);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setPrefWidth(400);
        rightSide.setPadding(new Insets(40));
        rightSide.setStyle("-fx-background-color: white;");

        Label title = new Label("Login to Account");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        TextField emailField = new TextField();
        emailField.setPromptText("Username");
        styleInput(emailField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);

        Button loginBtn = new Button("SIGN IN");
        loginBtn.setPrefWidth(300);
        loginBtn.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 5; -fx-cursor: hand;");

        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #00f2fe; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 5; -fx-cursor: hand;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12; -fx-background-radius: 5; -fx-cursor: hand;"));

        loginBtn.setOnAction(e->{
            String email=emailField.getText();
            String password=passwordField.getText();

            if (email.isEmpty() ||password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR,"login error","Please Enter your credentials ");
            }else {
                User user= AuthService.loginUser(email,password);
                if (user !=null ) {
                    if (user instanceof backend.Roles.Admin) {
                        showAlert(Alert.AlertType.INFORMATION, "Admin Login", "Welcome Master Admin panel: " + user.getName());
                    } else if (user instanceof backend.Roles.SubAdmin) {
                        showAlert(Alert.AlertType.INFORMATION, "SubAdmin Login", "Welcome Hotel Management panel: " + user.getName());
                    }

                }else{
                    showAlert(Alert.AlertType.ERROR, "Access Denied", "Invalid email or password credentials.");
                }
            }
        });

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register");
        registerLink.setStyle("-fx-text-fill: #4facfe;");

        registerLink.setOnAction(e -> {
            Register registerPage = new Register();
            stage.getScene().setRoot(registerPage.getLayout(stage));
        });


        rightSide.getChildren().addAll(title, emailField, passwordField, loginBtn, registerLink);

        HBox root = new HBox();
        root.getChildren().addAll(leftSide, rightSide);
        return root;
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