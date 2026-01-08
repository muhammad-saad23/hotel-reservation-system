module Hotel.Reservation.System {
    requires javafx.controls;
    requires javafx.graphics;
    requires java.xml;

    exports Frontend.views;
    exports Frontend.views.Dashboard;
    exports App;
    exports Frontend.views.Forms;
}