package backend.Roles;

import backend.Rooms.*;
import backend.RoomsManagement.*;

public class Admin extends User {

    private final String position = "Admin";
    private RoomManagement manager;

    public Admin(int id, String name, String email, String phone, String password, String role, String permissions, RoomManagement manager) {
        super(id, name, email, phone, password, "Admin", "ViewRooms|AddRooms|EditRooms|DeleteRooms|Bookings|Reports");
        this.manager = manager;
    }

    public String getPosition() {
        return position;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public void showAccess() {
        System.out.println("Access Level: FULL CONTROL");
        System.out.println("User: " + getName() + " (ID: " + getId() + ")");
        System.out.println("Authorized Modules: " + getPermissions());
    }

    @Override
    public String toString() {
        return getId() + "," + getName() + "," + getEmail() + "," + getPhone() + "," + getPassword() + "," + getRole() + "," + getPermissions();
    }

    // Room Management connection through Manager
    public void addRoom(Room room) {
        if (getPermissions().contains("AddRooms")) {
            manager.addRoom(room);
        } else {
            System.out.println("Error: Insufficient permissions to add room.");
        }
    }

    public void viewRooms() {
        if (getPermissions().contains("ViewRooms")) {
            manager.viewRooms();
        }
    }

    public void deleteRoom(int roomNumber) {
        if (getPermissions().contains("DeleteRooms")) {
            manager.deleteRoom(roomNumber);
        } else {
            System.out.println("Error: Insufficient permissions to delete room.");
        }
    }

    public void updateRoom(int roomNumber, String description, String type, Double price, Boolean isAvailable) {
        if (getPermissions().contains("EditRooms")) {
            manager.updateRoom(roomNumber, description, type, price, isAvailable);
        } else {
            System.out.println("Error: Insufficient permissions to update room.");
        }
    }
}