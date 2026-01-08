package FileHandling;

import backend.Rooms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private static final String FILE_PATH = "rooms.txt";

    public static void saveRooms(List<Room> rooms) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Room room : rooms) {
                writer.println(room.getRoomNumber() + "|" +
                        room.getRoomType() + "|" +
                        room.getPricePerNight() + "|" +
                        room.isAvailable() + "|" +
                        room.getDescription().replace("\n", " "));
            }
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    public static List<Room> loadRooms() {
        List<Room> rooms = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) return rooms;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 5) continue;

                int number = Integer.parseInt(parts[0]);
                String type = parts[1];
                double price = Double.parseDouble(parts[2]);
                boolean available = Boolean.parseBoolean(parts[3]);
                String desc = parts[4];

                if (type.equalsIgnoreCase("Suite")) {
                    rooms.add(new SuiteRoom(number, desc, type, price, available));
                } else {
                    rooms.add(new DeluxeRoom(number, desc, type, price, available));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
        }
        return rooms;
    }

}