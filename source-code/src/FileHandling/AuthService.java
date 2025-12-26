package FileHandling;

import backend.Roles.*;
import backend.RoomsManagement.RoomManagement;
import java.io.*;

public class AuthService {
    private static final String FILE_PATH = "users.txt";
    private static RoomManagement roomManager = new RoomManagement();


    public static boolean registerUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.toString());
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
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");

                if (d.length >= 5) {
                    String role = d[d.length - 1].trim();
                    String fileEmail = "";
                    String filePassword = d[3].trim(); // Password hamesha index 3 par hai

                    // Role-based Email Index selection
                    if (role.equalsIgnoreCase("Admin") || role.equalsIgnoreCase("SubAdmin")) {
                        fileEmail = d[1].trim();
                    } else {
                        fileEmail = d[2].trim();
                    }

                    if (fileEmail.equalsIgnoreCase(email.trim()) && filePassword.equals(password.trim())) {
                        return switch (role) {
                            case "Admin" -> new Admin(d[0], d[1], d[2], d[3], roomManager);
                            case "SubAdmin" -> new SubAdmin(d[0], d[1], d[2], d[3], roomManager);
                            case "Customer" -> new Customer(d[0], d[2], d[1], d[3], roomManager);
                            default -> null;
                        };
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static User createUserByRole(String[] d, String role) {
        if (role.equalsIgnoreCase("Admin")) {
            return new Admin(d[0], d[1], d[2], d[3], roomManager);
        } else if (role.equalsIgnoreCase("Customer")) {
            return new Customer(d[0], d[1], d[2], d[3], roomManager);
        }
        return null;
    }
}