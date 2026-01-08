package FileHandling;

import backend.Roles.*;
import backend.RoomsManagement.RoomManagement;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private static final String FILE_PATH = "users.txt";
    private static RoomManagement roomManager = new RoomManagement();

    public static boolean registerUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (user instanceof SubAdmin) {
                writer.write(user.toString());
            } else {
                writer.write(String.format("%d,%s,%s,%s,%s,%s",
                        user.getId(), user.getName(), user.getEmail(),
                        user.getPhone(), user.getPassword(), user.getRole()));
            }
            writer.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static User loginUser(String email, String password) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] d = line.split(",");
                if (d.length < 6) continue;

                String fEmail = d[2].trim();
                String fPass = d[4].trim();

                if (fEmail.equalsIgnoreCase(email.trim()) && fPass.equals(password.trim())) {
                    int id = Integer.parseInt(d[0].trim());
                    String name = d[1].trim();
                    String phone = d[3].trim();
                    String role = d[5].trim();

                    if (role.equalsIgnoreCase("SubAdmin")) {
                        String perms = (d.length >= 7) ? d[6].trim() : "None";
                        return new SubAdmin(id, name, fEmail, phone, fPass, role, roomManager, perms);
                    }

                    return switch (role.toLowerCase()) {
                        case "admin" -> new Admin(id, name, fEmail, phone, fPass, role, roomManager);
                        case "customer" -> new Customer(id, name, fEmail, phone, fPass, role, roomManager);
                        default -> null;
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<SubAdmin> getAllSubAdmins() {
        List<SubAdmin> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6 && d[5].trim().equalsIgnoreCase("SubAdmin")) {
                    int id = Integer.parseInt(d[0].trim());
                    String perms = (d.length >= 7) ? d[6].trim() : "None";
                    list.add(new SubAdmin(id, d[1].trim(), d[2].trim(), d[3].trim(), d[4].trim(), d[5].trim(), roomManager, perms));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean isEmailExists(String email) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[2].trim().equalsIgnoreCase(email.trim())) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public static int getNextId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0) {
                    try {
                        int id = Integer.parseInt(data[0].trim());
                        if (id > maxId) maxId = id;
                    } catch (Exception e) {}
                }
            }
        } catch (Exception e) {}
        return maxId + 1;
    }
}