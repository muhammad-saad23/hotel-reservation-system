package backend.Roles;

import backend.RoomsManagement.RoomManagement;

public class SubAdmin extends User {
    private RoomManagement manager;

    public SubAdmin(String name, String email, String phone, String password,String role,
                   RoomManagement manager) {
        super(name, email, phone, password,role);
        this.manager = manager;
    }

    @Override
    public String toString() {
        return getName() + "," + getEmail() + "," + getPhone() + "," + getPassword() +
                "," + ",SubAdmin";
    }

    @Override
    public void showAccess() {
        System.out.println("SubAdmin " + getName() + " Access Level: " + "Limited" );
    }

    public void viewRooms() {
        if (manager != null) {
            manager.viewRooms();
        } else {
            System.err.println("Room Management system not linked to this SubAdmin.");
        }
    }


}