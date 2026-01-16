package backend.Booking;

import backend.Rooms.Room;
import java.time.LocalDate;

public class Booking {
    private int customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private int bookingid;
    private Room selectedRoom;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double totalAmount;
    private String Request;
    private String paymentMethod;
    private String status;

    public Booking(int customerId, String customerName, String customerEmail, String customerPhone, int bookingid,
                   Room selectedRoom, LocalDate checkIn, LocalDate checkOut, double totalAmount,
                   String Request, String paymentMethod, String status) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.bookingid = bookingid;
        this.selectedRoom = selectedRoom;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
        this.Request = Request;
        this.paymentMethod = paymentMethod;
        this.status = (status == null || status.isEmpty()) ? "Active" : status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBookingid() { return bookingid; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public Room getSelectedRoom() { return selectedRoom; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }
    public String getRequest() { return Request; }
    public String getPaymentMethod() { return paymentMethod; }
    public int getCustomerId() { return customerId; }

    @Override
    public String toString() {
        return bookingid + "," + customerId + "," + customerName + "," + customerEmail + "," +
                customerPhone + "," + selectedRoom.getRoomNumber() + "," + checkIn + "," +
                checkOut + "," + totalAmount + "," + (Request.isEmpty() ? "None" : Request) + "," +
                paymentMethod + "," + status;
    }

      public String getDetailedSummary() {
        return "--- Booking Summary ---\n" +
                "Status:         " + status.toUpperCase() + "\n" +
                "Booking ID:     " + bookingid + "\n" +
                "Customer:       " + customerName +"\n" +
                "Contact:        " + customerEmail + "\n" +
                "Room Number:    " + selectedRoom.getRoomNumber() + "\n" +
                "Stay Period:    " + checkIn + " to " + checkOut + "\n" +
                "Payment:        " + paymentMethod + "\n" +
                "-----------------------\n" +
                "TOTAL AMOUNT:   Rs. " + totalAmount + "\n" +
                "-----------------------";
    }
}