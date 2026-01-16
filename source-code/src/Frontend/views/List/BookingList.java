package Frontend.views.List;

import Frontend.views.Dashboard.Layout;
import backend.Booking.Booking;
import FileHandling.BookingService;
import backend.Sessions.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookingList implements ListUi {
    private String userRole;
    private String userName;
    private VBox listContainer;
    private List<Booking> allBookings;
    private String currentSearch = "";
    private String currentStatusFilter = "All Status";

    public BookingList() {
        this.userRole = UserSession.getLoggedUserRole();
        this.userName = UserSession.getLoggedUserName();
    }

    @Override
    public void show(Stage stage) {
        VBox root = new VBox(25);
        root.setPadding(new Insets(35));
        root.setStyle("-fx-background-color: #f8fafc;");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(5);
        Label title = new Label(userRole.equalsIgnoreCase("Admin") || userRole.equalsIgnoreCase("SubAdmin") ? "Reservation Hub" : "My Reservations");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #0f172a;");
        Label subTitle = new Label("Monitor guest stays and booking lifecycle.");
        subTitle.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14px;");
        titleBox.getChildren().addAll(title, subTitle);
        header.getChildren().add(titleBox);

        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchBar = new TextField();
        searchBar.setPromptText("🔍 Search by Customer Name or Email...");
        searchBar.setPrefHeight(45);
        searchBar.setPrefWidth(400);
        searchBar.setStyle("-fx-background-radius: 12; -fx-border-color: #e2e8f0; -fx-border-radius: 12; -fx-padding: 0 15; -fx-background-color: white;");
        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            currentSearch = newVal;
            applyFilters();
        });

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Confirmed", "Cancelled");
        statusFilter.setValue("All Status");
        statusFilter.setPrefHeight(45);
        statusFilter.setStyle("-fx-background-radius: 12; -fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 12;");
        statusFilter.setOnAction(e -> {
            currentStatusFilter = statusFilter.getValue();
            applyFilters();
        });

        filterBar.getChildren().addAll(searchBar, statusFilter);

        listContainer = new VBox(15);
        listContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(listContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(650);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");

        applyFilters();

        root.getChildren().addAll(header, filterBar, scrollPane);
        stage.getScene().setRoot(new Layout(stage).createLayout(root, userRole, userName));
    }

    private void applyFilters() {
        listContainer.getChildren().clear();
        allBookings = BookingService.getAllBookingsAsObjects();

        if (allBookings == null || allBookings.isEmpty()) {
            listContainer.getChildren().add(createEmptyState());
            return;
        }

        List<Booking> filteredList = allBookings.stream().filter(b -> {
            boolean isAdminOrStaff = userRole.equalsIgnoreCase("Admin") || userRole.equalsIgnoreCase("SubAdmin");
            boolean isOwner = b.getCustomerName().equalsIgnoreCase(userName);

            boolean matchesSearch = b.getCustomerName().toLowerCase().contains(currentSearch.toLowerCase()) ||
                    b.getCustomerEmail().toLowerCase().contains(currentSearch.toLowerCase());

            boolean matchesStatus = currentStatusFilter.equals("All Status") ||
                    b.getStatus().equalsIgnoreCase(currentStatusFilter);

            return (isAdminOrStaff || isOwner) && matchesSearch && matchesStatus;
        }).collect(Collectors.toList());

        if (filteredList.isEmpty()) {
            listContainer.getChildren().add(createEmptyState());
        } else {
            for (Booking b : filteredList) {
                listContainer.getChildren().add(createBookingCard(b));
            }
        }
    }

    private HBox createBookingCard(Booking b) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));

        boolean isCancelled = b.getStatus().equalsIgnoreCase("Cancelled");
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5); " +
                "-fx-border-color: " + (isCancelled ? "#fee2e2" : "#f1f5f9") + "; -fx-border-width: 1;");

        VBox guestInfo = new VBox(4);
        guestInfo.setPrefWidth(250);
        Label guestName = new Label(b.getCustomerName());
        guestName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label guestMail = new Label(b.getCustomerEmail());
        guestMail.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        Label bookingId = new Label("ID: #" + b.getBookingid());
        bookingId.setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #6366f1; -fx-padding: 2 8; -fx-background-radius: 5; -fx-font-size: 10px; -fx-font-weight: bold;");
        guestInfo.getChildren().addAll(bookingId, guestName, guestMail);

        VBox roomInfo = new VBox(4);
        roomInfo.setPrefWidth(150);
        Label rNum = new Label("Room " + b.getSelectedRoom().getRoomNumber());
        rNum.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");
        Label rType = new Label(b.getSelectedRoom().getRoomType());
        rType.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        roomInfo.getChildren().addAll(rNum, rType);

        VBox stayInfo = new VBox(4);
        stayInfo.setPrefWidth(200);
        Label stayTitle = new Label("STAY PERIOD");
        stayTitle.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 9px; -fx-font-weight: bold;");
        Label dates = new Label(b.getCheckIn() + " - " + b.getCheckOut());
        dates.setStyle("-fx-font-size: 12px; -fx-text-fill: #475569;");
        stayInfo.getChildren().addAll(stayTitle, dates);

        Label statusLabel = new Label(b.getStatus().toUpperCase());
        String statusColor = isCancelled ? "#ef4444" : "#10b981";
        statusLabel.setStyle("-fx-background-color: " + statusColor + "15; -fx-text-fill: " + statusColor + "; -fx-padding: 5 15; -fx-background-radius: 10; -fx-font-size: 10px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnView = new Button("👁 Details");
        btnView.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-cursor: hand;");
        btnView.setOnAction(e -> showBookingDetailPopup(b));

        if (userRole.equalsIgnoreCase("Admin")) {
            Button btnDel = new Button("🗑");
            btnDel.setStyle("-fx-background-color: #fff1f2; -fx-text-fill: #be123c; -fx-cursor: hand; -fx-background-radius: 8;");
            btnDel.setOnAction(e -> handleDelete(b));
            actions.getChildren().add(btnDel);
        }

        actions.getChildren().add(0, btnView);

        card.getChildren().addAll(guestInfo, roomInfo, stayInfo, statusLabel, spacer, actions);
        return card;
    }

    private void handleDelete(Booking b) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Booking Management");
        alert.setHeaderText("Remove record #" + b.getBookingid());
        alert.setContentText("Choose removal type:");
        ButtonType soft = new ButtonType("Mark Cancelled");
        ButtonType hard = new ButtonType("Permanent Delete");
        ButtonType cancel = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(soft, hard, cancel);
        alert.showAndWait().ifPresent(type -> {
            if (type == soft) BookingService.deleteBooking(b.getBookingid(), false);
            else if (type == hard) BookingService.deleteBooking(b.getBookingid(), true);
            applyFilters();
        });
    }

    private void showBookingDetailPopup(Booking b) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Details");
        alert.setHeaderText(null);
        VBox vb = new VBox(10);
        vb.getChildren().addAll(
                new Label("Guest: " + b.getCustomerName()),
                new Label("Room: " + b.getSelectedRoom().getRoomNumber() + " (" + b.getSelectedRoom().getRoomType() + ")"),
                new Label("Check-in: " + b.getCheckIn()),
                new Label("Check-out: " + b.getCheckOut()),
                new Label("Amount: Rs. " + b.getTotalAmount()),
                new Label("Requests: " + (b.getRequest().isEmpty() ? "None" : b.getRequest()))
        );
        alert.getDialogPane().setContent(vb);
        alert.showAndWait();
    }

    private VBox createEmptyState() {
        VBox empty = new VBox(15);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(80));
        Label l = new Label("Empty Result");
        l.setStyle("-fx-font-size: 20px; -fx-text-fill: #94a3b8; -fx-font-weight: bold;");
        empty.getChildren().add(l);
        return empty;
    }
}