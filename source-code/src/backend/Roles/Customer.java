package backend.Roles;

import backend.RoomsManagement.RoomManagement;

public class Customer extends User {

    private RoomManagement manager;

    public Customer(int id, String name, String email, String phone, String password, String role,
                    String permissions,RoomManagement manager) {
        super(id, name, email, phone, password, role,permissions);
        this.manager = manager;
    }

    public Customer(int id, String email, String password, String role,String permissions) {
        super(id, "Unknown", email, "N/A", password, role,permissions);
        this.manager = new RoomManagement();
    }

    public static Customer createGuest(int id, String name, String email, String phone) {
        return new Customer(id, name, email, phone, null, "Customer", null,new RoomManagement());
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
        String pass = (getPassword() == null) ? "GUEST_USER" : getPassword();
        return getId() + "," + getName() + "," + getEmail() + "," + getPhone() + "," + pass + ",Customer";
    }
}