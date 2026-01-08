package backend.Roles;
import backend.Rooms.*;
import backend.RoomsManagement.*;

public class Admin extends User{

    private final String position="Admin";
    private RoomManagement manager;

    public Admin(int id,String name, String email, String phone, String password,String role, RoomManagement manager){
        super(id,name,email,phone,password,role);
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
        return  getId()+","+getName()+","+getEmail()+","+getPhone()+","+getPassword()+","+getPosition()+",Admin";
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
    public void updateRoom(int RoomNumber,String description,String type, Double price, Boolean isAvailable){
        manager.updateRoom(RoomNumber,description, type, price, isAvailable);
    }

}
