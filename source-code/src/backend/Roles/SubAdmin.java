package backend.Roles;

import backend.Rooms.*;
import backend.RoomsManagement.*;

public class SubAdmin extends User {

    private RoomManagement manager;
    private String status;

    public SubAdmin(int id, String name, String email, String phone, String password, String role, RoomManagement manager, String permissions) {
        super(id, name, email, phone, password, role, permissions);
        this.manager = manager;
        this.status = "Active";
    }

    @Override
    public void showAccess() {
        System.out.println("--- Sub-Admin Access Profile ---");
        System.out.println("Name: " + getName());
        System.out.println("Role: " + getRole());
        System.out.println("Allowed Permissions: " + (getPermissions().equals("None") ? "No specialized access" : getPermissions()));
    }

    @Override
    public String toString() {
        return getId() + "," + getName() + "," + getEmail() + "," + getPhone() + "," + getPassword() + "," + getRole() + "," + getPermissions();
    }

    public void addRoom(Room room) {
        if (getPermissions().contains("AddRooms")) {
            manager.addRoom(room);
        } else {
            System.out.println("Access Denied: You do not have permission to ADD rooms.");
        }
    }

    public void updateRoom(int roomNumber, String description, String type, Double price, Boolean isAvailable) {
        if (getPermissions().contains("EditRooms")) {
            manager.updateRoom(roomNumber, description, type, price, isAvailable);
        } else {
            System.out.println("Access Denied: You do not have permission to EDIT rooms.");
        }
    }

    public void deleteRoom(int roomNumber) {
        if (getPermissions().contains("DeleteRooms")) {
            manager.deleteRoom(roomNumber);
        } else {
            System.out.println("Access Denied: You do not have permission to DELETE rooms.");
        }
    }

    public void viewRooms() {
        if (getPermissions().contains("ViewRooms")) {
            manager.viewRooms();
        } else {
            System.out.println("Access Denied: Room inventory view restricted.");
        }
    }
}