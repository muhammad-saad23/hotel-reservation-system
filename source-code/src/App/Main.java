package App;

import backend.Rooms.*;
import backend.RoomsManagement.RoomManagement;
import backend.Roles.*;

public class Main {

    public static void main(String[] args) {

        // 1. Initialize the Room Management System
        RoomManagement manager = new RoomManagement();


        Admin admin = new Admin("Admin", "admin@gmail.com", "03001234567", "admin123",manager);

        admin.showAccess();

        // Admin adds rooms
        admin.addRoom(new SingleRoom(101, "Single", 3000.0));
        admin.addRoom(new DoubleRoom(201, "Double", 5000.0));;

        System.out.println("\n--- ROOMS LIST (Admin View) ---");
        admin.viewRooms();

        // 3. Customer Initialization
        // FIXED: Removed "Lahore" (Address) because your Customer class now only takes:
        // name, email, phone, password, customerId, paymentMethod, manager (7 total)

//
//        customer.showAccess();
//
//        System.out.println("\n--- ROOMS LIST (Customer View) ---");
//        customer.viewRooms();
//
//        // 4. Manual Login Logic Check
//        if(customer.login("sara@gmail.com", "cust456")) {
//            System.out.println("\nLogin Status: Customer logged in successfully!");
//        } else {
//            System.out.println("\nLogin Status: Authentication failed.");
//        }
    }
}