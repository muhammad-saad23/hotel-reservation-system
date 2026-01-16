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
                    String perms = (d.length >= 7) ? d[6].trim() : "None";

                    if (role.equalsIgnoreCase("SubAdmin")) {
                        return new SubAdmin(id, name, fEmail, phone, fPass, role, roomManager, perms);
                    } else if (role.equalsIgnoreCase("Admin")) {
                        return new Admin(id, name, fEmail, phone, fPass, role, "All", roomManager);
                    } else {
                        return new Customer(id, name, fEmail, phone, fPass, role, null, roomManager);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 6) {
                    int id = Integer.parseInt(d[0].trim());
                    String name = d[1].trim();
                    String email = d[2].trim();
                    String phone = d[3].trim();
                    String pass = d[4].trim();
                    String role = d[5].trim();
                    String perms = (d.length >= 7) ? d[6].trim() : "None";

                    if (role.equalsIgnoreCase("SubAdmin")) {
                        list.add(new SubAdmin(id, name, email, phone, pass, role, roomManager, perms));
                    } else if (role.equalsIgnoreCase("Admin")) {
                        list.add(new Admin(id, name, email, phone, pass, role,"All" ,roomManager));
                    } else {
                        list.add(new Customer(id, name, email, phone, pass, role, null,roomManager));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
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
        File file = new File(FILE_PATH);
        if (!file.exists()) return 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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

    public static boolean updateUserPermissions(int id, String newPermissions) {
        List<User> users = getAllUsers();
        boolean found = false;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User u : users) {
                if (u.getId() == id) {
                    u.setPermissions(newPermissions);
                    found = true;
                }
                bw.write(u.getId() + "," + u.getName() + "," + u.getEmail() + "," +
                        u.getPhone() + "," + u.getPassword() + "," + u.getRole() + "," +
                        u.getPermissions());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    private boolean blockSubAdmin(String id, boolean permanent) {
        File inputFile = new File("users.txt");
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].trim().equals(id)) {
                    found = true;
                    if (permanent) {

                    } else {
                        d[5] = "Blocked";
                        updatedLines.add(String.join(",", d));
                    }
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(inputFile, false))) {
            for (String updatedLine : updatedLines) {
                writer.println(updatedLine);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String getUserPermissions(String userId) {
        File file = new File("users.txt");
        if (!file.exists()) return "";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].trim().equals(userId)) {
                    return (data.length > 6) ? data[6].trim() : "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static int calculateTotalRevenue() {
        double total = 0;
        File file = new File("bookings.txt");
        if (!file.exists()) return 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9) {
                    try {
                        total += Double.parseDouble(data[8].trim());
                    } catch (NumberFormatException e) {}
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (int) total;
    }

    public static int getCountByFilter(String fileName, String filter) {
        int count = 0;
        File file = new File(fileName);
        if (!file.exists()) return 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(filter.toLowerCase())) {
                    count++;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }

    public static int getAvailableRoomsCount() {
        int count = 0;
        File file = new File("rooms.txt");
        if (!file.exists()) return 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[3].trim().equalsIgnoreCase("Available")) {
                    count++;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return count;
    }


    public static boolean updateUserPassword(String name, String oldPassword, String newPassword) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File("users_temp.txt");
        boolean isUpdated = false;
        if (!inputFile.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] d = line.split(",");
                if (d.length >= 6) {
                    String fName = d[1].trim();
                    String fPass = d[4].trim();

                    if (fName.equals(name.trim()) && fPass.equals(oldPassword.trim())) {
                        d[4] = newPassword;
                        line = String.join(",", d);
                        isUpdated = true;
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) { return false; }

        if (isUpdated) {
            if (inputFile.delete()) {
                return tempFile.renameTo(inputFile);
            }
        }
        tempFile.delete();
        return isUpdated;
    }

    public static boolean isPhoneExists(String phone) {
        File file = new File("users.txt");
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[3].trim().equals(phone.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}