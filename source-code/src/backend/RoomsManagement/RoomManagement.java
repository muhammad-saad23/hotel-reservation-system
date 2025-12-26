package backend.RoomsManagement;

import backend.Rooms.*;
import java.util.*;

public class RoomManagement {

    private ArrayList<Room> rooms = new ArrayList<>();

    // Add Room
    public void addRoom(Room room) {
        rooms.add(room);
        System.out.println("Room added successfully!");
    }

    // View All Rooms
    public void viewRooms() {
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }

        for (Room room : rooms) {
            room.DisplayDetails();
            System.out.println("--------------------");
        }
    }

    // Delete Room
    public void deleteRoom(int roomNumber) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == roomNumber) {
                rooms.remove(i);
                System.out.println("Room deleted successfully!");
                return;
            }
        }
        System.out.println("Room not found!");
    }

    // Update Room
    public void updateRoom(int roomNumber, String roomType,Double pricePerNight, Boolean isAvailable) {

        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {

                if (roomType != null && !roomType.isEmpty()) {
                    room.setRoomType(roomType);
                }

                if (pricePerNight != null && pricePerNight > 0) {
                    room.setPricePerNight(pricePerNight);
                }

                if (isAvailable != null) {
                    room.setAvailable(isAvailable);
                }

                System.out.println("Room updated successfully!");
                return;
            }
        }
        System.out.println("Room not found!");
    }
}
