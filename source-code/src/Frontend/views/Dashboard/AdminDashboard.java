package Frontend.views.Dashboard;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AdminDashboard implements Dashboard {
    @Override
    public BorderPane getLayout(Stage stage) {
        Layout layout = new Layout();

        // Admin specific content
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.getChildren().add(new Label("Admin Overview: System-wide Stats"));

        return layout.getLayout(stage, "ADMIN", content);
    }
}