package App;

import Frontend.views.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Login loginPage = new Login();

        Scene scene = new Scene(loginPage.getLayout(stage), 400, 500);

        stage.setTitle("Hotel Management System - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}