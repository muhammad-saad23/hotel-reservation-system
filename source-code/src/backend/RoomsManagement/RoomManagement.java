package backend.RoomsManagement;

import FileHandling.RoomService;
import backend.Rooms.*;
import java.util.*;

public class RoomManagement {

    private ArrayList<Room> rooms;

    public RoomManagement() {
        this.rooms = new ArrayList<>(RoomService.loadRooms());
    }

    public void addRoom(Room room) {
        rooms.add(room);
        RoomService.saveRooms(rooms);
        System.out.println("Room added successfully!");
    }

    public void viewRooms() {
        this.rooms = new ArrayList<>(RoomService.loadRooms());
        if (rooms.isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }
        for (Room room : rooms) {
            room.DisplayDetails();
            System.out.println("--------------------");
        }
    }

    public void deleteRoom(int roomNumber) {
        boolean removed = rooms.removeIf(room -> room.getRoomNumber() == roomNumber);
        if (removed) {
            RoomService.saveRooms(rooms);
            System.out.println("Room deleted");
        } else {
            System.out.println("Room not found!");
        }
    }

    public void updateRoom(int roomNumber, String description, String roomType, Double pricePerNight, Boolean isAvailable) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                if (description != null) room.setDescription(description);
                if (roomType != null) room.setRoomType(roomType);
                if (pricePerNight != null && pricePerNight > 0) room.setPricePerNight(pricePerNight);
                if (isAvailable != null) room.setAvailable(isAvailable);

                RoomService.saveRooms(rooms);
                System.out.println("Room updated in file successfully!");
                return;
            }
        }
        System.out.println("Room not found!");
    }

    public ArrayList<Room> getRooms() {
        this.rooms = new ArrayList<>(RoomService.loadRooms());
        return rooms;
    }
}