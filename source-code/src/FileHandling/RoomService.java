package FileHandling;

import backend.Rooms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private static final String FILE_PATH = "rooms.txt";

    public static boolean saveRooms(List<Room> rooms) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Room room : rooms) {
                writer.println(room.getRoomNumber() + "|" +
                        room.getRoomType() + "|" +
                        room.getPricePerNight() + "|" +
                        room.isAvailable() + "|" +
                        room.getDescription().replace("\n", " "));
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return rooms;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    try {
                        int number = Integer.parseInt(parts[0].trim());
                        String type = parts[1].trim();
                        double price = Double.parseDouble(parts[2].trim());
                        boolean available = Boolean.parseBoolean(parts[3].trim());
                        String desc = parts[4].trim();

                        if (type.equalsIgnoreCase("Suite")) {
                            rooms.add(new SuiteRoom(number, desc, type, price, available));
                        } else if (type.equalsIgnoreCase("Deluxe")) {
                            rooms.add(new DeluxeRoom(number, desc, type, price, available));
                        } else {
                            rooms.add(new SingleRoom(number, desc, type, price, available));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing numeric data: " + line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static List<Room> getAllRooms() {
        return loadRooms();
    }

    public static boolean deleteRoom(int roomNumber) {
        List<Room> rooms = loadRooms();

        boolean removed = rooms.removeIf(room -> room.getRoomNumber() == roomNumber);

        if (removed) {
            return saveRooms(rooms);
        }
        return false;
    }

    public static boolean updateRoom(Room updatedRoom) {
        List<Room> rooms = loadRooms();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == updatedRoom.getRoomNumber()) {
                rooms.set(i, updatedRoom);
                return saveRooms(rooms);
            }
        }
        return false;
    }
}