package Frontend.views.Dashboard;

import Frontend.views.List.CustomerRoomList;
import backend.Sessions.UserSession;
import Frontend.views.List.RoomList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CustomerDashboard implements Dashboard {
    private Stage stage;

    @Override
    public BorderPane getLayout(Stage stage) {
        this.stage = stage;

        String displayName = UserSession.getLoggedUserName() != null ? UserSession.getLoggedUserName() : "Guest";

        VBox mainContent = new VBox(30);
        mainContent.setPadding(new Insets(30));
        mainContent.setAlignment(Pos.TOP_LEFT);
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        VBox welcomeCard = new VBox(15);
        welcomeCard.setPadding(new Insets(40));
        welcomeCard.setStyle("-fx-background-color: linear-gradient(to right, #6d28d9, #9333ea); " +
                "-fx-background-radius: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 10);");
        welcomeCard.setAlignment(Pos.CENTER_LEFT);

        Label welcomeText = new Label("Welcome, " + displayName + "!");
        welcomeText.setFont(Font.font("Poppins", FontWeight.BOLD, 36));
        welcomeText.setTextFill(Color.WHITE);

        Label subText = new Label(UserSession.isLoggedIn() ? "Ready for your next stay?" : "Book your first luxury stay with us today!");
        subText.setFont(Font.font("Poppins", 16));
        subText.setTextFill(Color.web("#e0e7ff"));

        Button exploreBtn = new Button("✨ Explore & Book Rooms");
        exploreBtn.setStyle("-fx-background-color: #fcd34d; -fx-text-fill: #6d28d9; -fx-font-weight: bold; " +
                "-fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-font-size: 16px;");
        exploreBtn.setOnAction(e -> new CustomerRoomList().show(stage));

        welcomeCard.getChildren().addAll(welcomeText, subText, exploreBtn);

        HBox statsContainer = new HBox(20);
        statsContainer.setAlignment(Pos.CENTER_LEFT);

        if (UserSession.isLoggedIn()) {
            statsContainer.getChildren().add(createStatCard("Upcoming Bookings", "1", "Check-in: 24 May", "#a78bfa"));
            statsContainer.getChildren().add(createStatCard("Reward Points", "5,200", "Tier: Gold", "#60a5fa"));
        } else {
            statsContainer.getChildren().add(createStatCard("Quick Booking", "Fast", "No account needed", "#a78bfa"));
            statsContainer.getChildren().add(createStatCard("Offers", "10% OFF", "On your first stay", "#60a5fa"));
        }

        VBox featuredRoomsSection = new VBox(15);
        Label featuredTitle = new Label("Our Best Rooms");
        featuredTitle.setFont(Font.font("Poppins", FontWeight.BOLD, 24));

        HBox roomCardsContainer = new HBox(20);
        roomCardsContainer.getChildren().addAll(
//                createRoomPreviewCard("Deluxe Suite", "Spacious comfort", "4500"),
//                createRoomPreviewCard("Executive Room", "Modern amenities", "3000"),
//                createRoomPreviewCard("Standard Double", "Cozy & affordable", "2000")
        );

        featuredRoomsSection.getChildren().addAll(featuredTitle, roomCardsContainer);
        mainContent.getChildren().addAll(welcomeCard, statsContainer, featuredRoomsSection);

        return new Layout(stage).createLayout(mainContent, "Customer", displayName);
    }

    private VBox createStatCard(String title, String value, String subText, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 15;");

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.WHITE);
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.WHITE);
        Label subLabel = new Label(subText);
        subLabel.setTextFill(Color.web("rgba(255,255,255,0.7)"));

        card.getChildren().addAll(titleLabel, valueLabel, subLabel);
        return card;
    }

    private VBox createRoomPreviewCard(String roomName, String description, String price) {
        VBox card = new VBox(10);
        card.setPrefWidth(250);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        VBox placeholder = new VBox();
        placeholder.setPrefSize(250, 150);
        placeholder.setAlignment(Pos.CENTER);
        placeholder.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 15 15 0 0;");
        placeholder.getChildren().add(new Label("Room Image"));

        VBox textContent = new VBox(8);
        textContent.setPadding(new Insets(15));
        Label name = new Label(roomName);
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        Label pr = new Label("Rs. " + price + " / night");
        pr.setStyle("-fx-text-fill: #6d28d9; -fx-font-weight: bold;");

        Button bookNow = new Button("Book Now");
        bookNow.setMaxWidth(Double.MAX_VALUE);
        bookNow.setStyle("-fx-background-color: #4f46e5; -fx-text-fill: white; -fx-background-radius: 8; -fx-cursor: hand;");
        bookNow.setOnAction(e -> new CustomerRoomList().show(stage));

        textContent.getChildren().addAll(name, new Label(description), pr, bookNow);
        card.getChildren().addAll(placeholder, textContent);
        return card;
    }
}