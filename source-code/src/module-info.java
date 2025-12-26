module Hotel.Reservation.System {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.xml;
    // fxml is optional now since we are using pure Java code

    exports Frontend.views.Components.Admin;
//    exports Frontend.views.Components.Customer;
    exports Frontend.views.Admin.Auth;
    exports Frontend.views.Admin.Room;
    exports Frontend.views.Customer.Auth;
}