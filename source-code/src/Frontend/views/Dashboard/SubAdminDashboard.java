package Frontend.views.Dashboard;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class SubAdminDashboard implements Dashboard {

    @Override
    public BorderPane getLayout(Stage stage) {

        Layout layout = new Layout();
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        Label welcomeLabel = new Label("Staff Portal - Daily Operations");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        content.getChildren().add(welcomeLabel);

        return layout.getLayout(stage, "SUBADMIN", content);
    }
}