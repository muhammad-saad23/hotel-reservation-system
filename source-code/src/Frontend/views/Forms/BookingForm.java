package Frontend.views.Forms;

import Frontend.views.Dashboard.Layout;
import Frontend.views.Dashboard.CustomerDashboard;
import backend.Rooms.Room;
import backend.Sessions.UserSession;
import backend.Booking.Booking;
import FileHandling.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingForm implements FormUi {
    private Room selectedRoom;
    private DatePicker checkIn, checkOut;
    private TextArea txtRequest;
    private TextField txtName, txtEmail, txtPhone;
    private ComboBox<String> comboPayment;
    private CheckBox cbDining, cbLaundry, cbWifi;
    private Label totalLabel;

    private final double DINING_COST = 1500.0;
    private final double LAUNDRY_COST = 500.0;
    private final double WIFI_PREMIUM = 300.0;

    public BookingForm(Room room) {
        this.selectedRoom = room;
    }

    @Override
    public Parent getLayout(Stage stage) {
        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(20, 50, 40, 50));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: #f1f5f9;");

        HBox roomPreview = new HBox(20);
        roomPreview.setAlignment(Pos.CENTER_LEFT);
        roomPreview.setPadding(new Insets(20));
        roomPreview.setMaxWidth(850);
        roomPreview.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-color: #d1d5db;");

        Label lblIcon = new Label("🏨");
        lblIcon.setFont(Font.font(35));

        VBox roomDetails = new VBox(5);
        Label lblRoomName = new Label("Room Type: " + selectedRoom.getRoomType() + " (Room #" + selectedRoom.getRoomNumber() + ")");
        lblRoomName.setFont(Font.font("System", FontWeight.BOLD, 18));
        lblRoomName.setTextFill(Color.BLACK);

        Label lblDesc = new Label(selectedRoom.getDescription());
        lblDesc.setTextFill(Color.web("#4b5563"));

        Label lblPrice = new Label("Base Rate: Rs. " + selectedRoom.getPricePerNight() + " / night");
        lblPrice.setStyle("-fx-font-weight: bold; -fx-text-fill: #2563eb;");

        roomDetails.getChildren().addAll(lblRoomName, lblDesc, lblPrice);
        roomPreview.getChildren().addAll(lblIcon, roomDetails);

        VBox card = new VBox(20);
        card.setMaxWidth(850);
        card.setPadding(new Insets(30));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Reservation Details");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));

        HBox personalFields = new HBox(15);
        txtName = createStyledTextField("Full Name", "Enter Name");
        txtEmail = createStyledTextField("Email Address", "Enter Email");
        txtPhone = createStyledTextField("Phone Number", "Enter Phone");

        if(UserSession.isLoggedIn()){
            txtName.setText(UserSession.getLoggedUserName());
            txtEmail.setText(UserSession.getLoggedUserEmail());
            txtPhone.setText(UserSession.getLoggedUserPhone());
            txtName.setEditable(false);
            txtEmail.setEditable(false);
        }

        personalFields.getChildren().addAll(
                createFieldGroup("Full Name", txtName),
                createFieldGroup("Email", txtEmail),
                createFieldGroup("Phone", txtPhone)
        );

        HBox dateSection = new HBox(20);
        checkIn = new DatePicker();
        checkOut = new DatePicker();
        checkOut.setDisable(true);
        disablePastDates(checkIn);
        dateSection.getChildren().addAll(createInputGroup("Check-In Date", checkIn), createInputGroup("Check-Out Date", checkOut));

        HBox detailRow = new HBox(30);
        VBox servicesCol = new VBox(15);
        servicesCol.setPrefWidth(300);

        cbDining = createStyledCheckBox("Dining Service", DINING_COST);
        cbLaundry = createStyledCheckBox("Laundry Service", LAUNDRY_COST);
        cbWifi = createStyledCheckBox("High-Speed WiFi", WIFI_PREMIUM);
        servicesCol.getChildren().addAll(cbDining, cbLaundry, cbWifi);

        VBox paymentCol = new VBox(10);
        comboPayment = new ComboBox<>();
        comboPayment.getItems().addAll("Credit/Debit Card", "JazzCash / EasyPaisa", "Pay at Hotel");
        comboPayment.setPromptText("Select Payment Method");
        comboPayment.setMaxWidth(Double.MAX_VALUE);
        comboPayment.setPrefHeight(40);

        txtRequest = new TextArea();
        txtRequest.setPromptText("Special requests...");
        txtRequest.setPrefHeight(100);
        paymentCol.getChildren().addAll(comboPayment, txtRequest);
        HBox.setHgrow(paymentCol, Priority.ALWAYS);
        detailRow.getChildren().addAll(servicesCol, paymentCol);

        totalLabel = new Label("Total: Rs. 0.00");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 26));
        totalLabel.setTextFill(Color.web("#059669"));

        Button btnConfirm = new Button("Confirm Booking");
        btnConfirm.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 40; -fx-background-radius: 8; -fx-cursor: hand;");

        btnConfirm.setOnAction(e -> {
            if (validate()) {
                submitAndNotify(stage);
            }
        });

        HBox footer = new HBox(totalLabel, new Region(){{HBox.setHgrow(this, Priority.ALWAYS);}}, btnConfirm);
        footer.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(title, new Separator(), personalFields, dateSection, new Separator(), detailRow, new Separator(), footer);
        mainContent.getChildren().addAll(roomPreview, card);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setupLogic();
        return new Layout(stage).createLayout(new VBox(scrollPane), "Customer", UserSession.getLoggedUserName());
    }

    private TextField createStyledTextField(String label, String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setStyle("-fx-border-color: #d1d5db; -fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 10 5 10; -fx-text-fill: black;");

        if (label.equals("Phone Number")) {
            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tf.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
        }
        return tf;
    }

    private CheckBox createStyledCheckBox(String text, double cost) {
        CheckBox cb = new CheckBox(text + " (+Rs. " + cost + ")");
        cb.setStyle("-fx-text-fill: black; -fx-font-size: 13px;");
        return cb;
    }

    private VBox createFieldGroup(String label, TextField tf) {
        VBox v = new VBox(5);
        Label l = new Label(label);
        l.setStyle("-fx-font-weight: bold; -fx-text-fill: #374151;");
        v.getChildren().addAll(l, tf);
        HBox.setHgrow(v, Priority.ALWAYS);
        return v;
    }

    private VBox createInputGroup(String labelText, DatePicker picker) {
        VBox box = new VBox(8);
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-weight: bold;");
        picker.setMaxWidth(Double.MAX_VALUE);
        picker.setPrefHeight(40);
        box.getChildren().addAll(lbl, picker);
        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    private void setupLogic() {
        Runnable calc = () -> {
            if (checkIn.getValue() != null && checkOut.getValue() != null) {
                long nights = ChronoUnit.DAYS.between(checkIn.getValue(), checkOut.getValue());
                if (nights > 0) {
                    double total = (nights * selectedRoom.getPricePerNight());
                    if (cbDining.isSelected()) total += DINING_COST;
                    if (cbLaundry.isSelected()) total += LAUNDRY_COST;
                    if (cbWifi.isSelected()) total += WIFI_PREMIUM;
                    totalLabel.setText("Total: Rs. " + String.format("%,.2f", total));
                }
            }
        };
        checkIn.setOnAction(e -> {
            if (checkIn.getValue() != null) {
                checkOut.setDisable(false);
                updateCheckOutLimit(checkIn.getValue());
            }
            calc.run();
        });
        checkOut.setOnAction(e -> calc.run());
        cbDining.setOnAction(e -> calc.run());
        cbLaundry.setOnAction(e -> calc.run());
        cbWifi.setOnAction(e -> calc.run());
    }

    private void submitAndNotify(Stage stage) {
        long nights = ChronoUnit.DAYS.between(checkIn.getValue(), checkOut.getValue());
        double total = (nights * selectedRoom.getPricePerNight());
        if (cbDining.isSelected()) total += DINING_COST;
        if (cbLaundry.isSelected()) total += LAUNDRY_COST;
        if (cbWifi.isSelected()) total += WIFI_PREMIUM;

        int userId = UserSession.isLoggedIn() ? UserSession.getLoggedUserId() : AuthService.getNextId();
        int bookingId = (int)(System.currentTimeMillis() % 100000);

        Booking newBooking = new Booking(userId, txtName.getText(), txtEmail.getText(), txtPhone.getText(), bookingId, selectedRoom, checkIn.getValue(), checkOut.getValue(), total, txtRequest.getText(), comboPayment.getValue(), "Active");

        if (BookingService.saveBooking(newBooking)) {
            String bIdStr = String.valueOf(newBooking.getBookingid());
            BookingService.saveReceiptToFile(bIdStr, newBooking.getDetailedSummary());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Booking Success");
            alert.setHeaderText(null);
            alert.setContentText("Room Booked Successfully!");
            alert.showAndWait();
            stage.getScene().setRoot(new CustomerDashboard().getLayout(stage));
        }
    }

    @Override
    public boolean validate() {
        if (txtName.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() ||
                checkIn.getValue() == null || checkOut.getValue() == null || comboPayment.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill all required fields.");
            return false;
        }
        return true;
    }

    @Override public void submit() {}

    private void updateCheckOutLimit(LocalDate startDate) {
        checkOut.setDayCellFactory(p -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && (item.isBefore(startDate.plusDays(1)))) setDisable(true);
            }
        });
    }

    private void disablePastDates(DatePicker picker) {
        picker.setDayCellFactory(p -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isBefore(LocalDate.now())) setDisable(true);
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}