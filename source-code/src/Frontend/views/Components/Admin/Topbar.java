package Frontend.views.Components.Admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Topbar extends HBox {

    public Topbar(String pageTitle) {
        this.setPadding(new Insets(15, 30, 15, 30));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefHeight(70);
        this.setStyle("-fx-background-color: white; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label lblTitle = new Label(pageTitle);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 20));
        lblTitle.setTextFill(Color.web("#2c3e50"));

        TextField searchField = new TextField();
        searchField.setPromptText("Search anything...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: #f4f7f6; -fx-background-radius: 20; " +
                "-fx-padding: 8 15; -fx-border-color: #e0e0e0; -fx-border-radius: 20;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox profileBox = new HBox(12);
        profileBox.setAlignment(Pos.CENTER);

        VBox adminInfo = new VBox(2);
        adminInfo.setAlignment(Pos.CENTER_RIGHT);
        Label adminName = new Label("Admin User");
        adminName.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label adminRole = new Label("Super Admin");
        adminRole.setFont(Font.font("System", 11));
        adminRole.setTextFill(Color.GRAY);
        adminInfo.getChildren().addAll(adminName, adminRole);

        Label profileCircle = new Label("A");
        profileCircle.setAlignment(Pos.CENTER);
        profileCircle.setPrefSize(40, 40);
        profileCircle.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; " +
                "-fx-background-radius: 25; -fx-font-weight: bold;");

        profileBox.getChildren().addAll(adminInfo, profileCircle);

        this.getChildren().addAll(lblTitle, searchField, spacer, profileBox);
    }
}