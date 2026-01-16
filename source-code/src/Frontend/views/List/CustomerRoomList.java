package Frontend.views.List;

import FileHandling.RoomService;
import Frontend.views.Dashboard.Layout;
import Frontend.views.Forms.BookingForm;
import backend.Rooms.Room;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerRoomList implements ListUi {

    private FlowPane cardContainer;
    private List<Room> allRooms;
    private TextField searchField;
    private ComboBox<String> typeFilter;
    private VBox emptyState;

    @Override
    public void show(Stage stage) {
        VBox content = new VBox(25);
        content.setPadding(new Insets(40, 80, 40, 80));
        content.setStyle("-fx-background-color: #f8fafc;");

        VBox headText = new VBox(10);
        Label title = new Label("Choose Your Perfect Stay");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        Label sub = new Label("Discover luxury and comfort tailored to your needs.");
        sub.setStyle("-fx-text-fill: #64748b; -fx-font-size: 16px;");
        headText.getChildren().addAll(title, sub);

        HBox controls = new HBox(20);
        controls.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("🔍 Search by room number or type...");
        searchField.setPrefWidth(350);
        searchField.setStyle("-fx-background-radius: 10; -fx-padding: 12; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-background-color: white;");

        typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Categories", "Single", "Double", "Deluxe", "Suite");
        typeFilter.setValue("All Categories");
        typeFilter.setPrefWidth(200);
        typeFilter.setStyle("-fx-background-radius: 10; -fx-padding: 7; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-background-color: white;");

        controls.getChildren().addAll(searchField, typeFilter);

        cardContainer = new FlowPane(30, 30);
        cardContainer.setAlignment(Pos.TOP_LEFT);

        // Initialize Empty State UI
        emptyState = new VBox(15);
        emptyState.setAlignment(Pos.CENTER);
        emptyState.setPadding(new Insets(50));
        Label emptyIcon = new Label("🚫");
        emptyIcon.setStyle("-fx-font-size: 50px;");
        Label emptyText = new Label("No Rooms Found");
        emptyText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #94a3b8;");
        emptyState.getChildren().addAll(emptyIcon, emptyText);
        emptyState.setVisible(false);
        emptyState.setManaged(false);

        allRooms = RoomService.loadRooms().stream()
                .filter(Room::isAvailable)
                .collect(Collectors.toList());

        searchField.textProperty().addListener((obs, old, newValue) -> applyFilters(stage));
        typeFilter.valueProperty().addListener((obs, old, newValue) -> applyFilters(stage));

        updateCards(allRooms, stage);

        StackPane displayArea = new StackPane();
        ScrollPane sp = new ScrollPane(cardContainer);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        displayArea.getChildren().addAll(sp, emptyState);

        content.getChildren().addAll(headText, controls, displayArea);

        stage.getScene().setRoot(new Layout(stage).createLayout(content, "Customer", "Guest"));
    }

    private void applyFilters(Stage stage) {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedType = typeFilter.getValue();

        List<Room> filtered = allRooms.stream()
                .filter(r -> (selectedType.equals("All Categories") || r.getRoomType().equalsIgnoreCase(selectedType)))
                .filter(r -> String.valueOf(r.getRoomNumber()).contains(searchText) ||
                        r.getRoomType().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            cardContainer.setVisible(false);
            emptyState.setVisible(true);
            emptyState.setManaged(true);
        } else {
            cardContainer.setVisible(true);
            emptyState.setVisible(false);
            emptyState.setManaged(false);
            updateCards(filtered, stage);
        }
    }

    private void updateCards(List<Room> rooms, Stage stage) {
        cardContainer.getChildren().clear();
        for (Room r : rooms) {
            cardContainer.getChildren().add(createLuxuryCard(r, stage));
        }
    }

    private VBox createLuxuryCard(Room r, Stage stage) {
        VBox card = new VBox(15);
        card.setPrefWidth(300);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 15, 0, 0, 10);");

        Label type = new Label(r.getRoomType().toUpperCase());
        type.setStyle("-fx-text-fill: #6366f1; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label roomNo = new Label("Elite Suite #" + r.getRoomNumber());
        roomNo.setFont(Font.font("System", FontWeight.BOLD, 22));

        Label price = new Label("Rs. " + String.format("%,.0f", r.getPricePerNight()));
        price.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        Button btnBook = new Button("Reserve Now");
        btnBook.setMaxWidth(Double.MAX_VALUE);
        btnBook.setPadding(new Insets(12));
        btnBook.setStyle("-fx-background-color: #1e1b4b; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 12; -fx-cursor: hand;");

        btnBook.setOnAction(e -> stage.getScene().setRoot(new BookingForm(r).getLayout(stage)));

        card.getChildren().addAll(type, roomNo, price, new Separator(), btnBook);
        return card;
    }
}