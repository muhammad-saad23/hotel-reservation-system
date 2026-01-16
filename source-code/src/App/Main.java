package App;

import Frontend.views.Dashboard.CustomerDashboard;
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

//        CustomerDashboard customerDashboard=new CustomerDashboard();
//        Scene scene=new Scene(customerDashboard.getLayout(stage),800,900);
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}