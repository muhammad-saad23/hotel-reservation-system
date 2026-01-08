package backend.Roles;

import backend.RoomsManagement.RoomManagement;

public class SubAdmin extends User {
    private RoomManagement manager;
    private String permissions;

    public SubAdmin(int id, String name, String email, String phone, String password, String role,
                    RoomManagement manager, String permissions) {
        super(id, name, email, phone, password, role);
        this.manager = manager;
        this.permissions = permissions;
    }

    public String getPermissions() { return permissions; }

    @Override
    public String toString() {
        return getId() + "," +
                getName() + "," +
                getEmail() + "," +
                getPhone() + "," +
                getPassword() + "," +
                getRole() + "," +
                permissions;
    }

    @Override
    public void showAccess() {
        System.out.println("SubAdmin Access: " + permissions);
    }

    public void viewRooms() {
        if (manager != null) manager.viewRooms();
    }
}