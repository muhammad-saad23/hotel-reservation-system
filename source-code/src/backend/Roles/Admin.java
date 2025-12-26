package backend.Roles;
import backend.Rooms.*;
import backend.RoomsManagement.*;

public class Admin extends User{

    private final String position="Admin";
    private RoomManagement manager;

    public Admin(String name, String email, String phone, String password, RoomManagement manager){
        super(name,email,phone,password);
        this.manager=manager;
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
        System.out.println("Admin " +getName()+"has full access to room management");
    }


    @Override
    public String toString() {
        return getName()+","+getEmail()+","+getPhone()+","+getPassword()+","+getPosition()+",Admin";
    }

    public void addRoom(Room room){
        manager.addRoom(room);
    }
    public void viewRooms(){
        manager.viewRooms();
    }
    public void deleteRoom(int RoomNumber){
        manager.deleteRoom(RoomNumber);
    }
    public void updateRoom(int RoomNumber,String type, Double price, Boolean available){
        manager.updateRoom(RoomNumber, type, price, available);
    }

}
