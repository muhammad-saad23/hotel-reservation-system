package backend.Roles;

import backend.RoomsManagement.RoomManagement;

public class Customer extends User {


    private RoomManagement manager;

    public Customer(String name, String email, String phone, String password,String role,
                    RoomManagement manager) {
        super(name, email, phone, password,role);
        this.manager = manager;
    }

    public Customer(String email, String password,String role) {
        super("Unknown", email, "N/A", password,role);
        this.manager = new RoomManagement();
    }

    public void viewRooms() {
        if (manager != null) {
            manager.viewRooms();
        }
    }

    @Override
    public void showAccess() {
        System.out.println("Customer " + getName() + " has limited access (View/Book only).");
    }

    @Override
    public String toString() {
        return getName() + "," + getEmail() + "," + getPhone() + "," + getPassword() + ",Customer";
    }
}