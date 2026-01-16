package FileHandling;

import backend.Booking.Booking;
import backend.Rooms.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private static final String FILE_PATH = "bookings.txt";

    public static boolean saveBooking(Booking booking) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(booking.toString());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }

    public static List<Booking> getAllBookingsAsObjects() {
        List<Booking> bookingList = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return bookingList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] d = line.split(",");
                if (d.length >= 11) {
                    try {
                        int bId = Integer.parseInt(d[0].trim());
                        int cId = Integer.parseInt(d[1].trim());
                        String name = d[2].trim();
                        String email = d[3].trim();
                        String phone = d[4].trim();
                        int rNum = Integer.parseInt(d[5].trim());
                        LocalDate in = LocalDate.parse(d[6].trim());
                        LocalDate out = LocalDate.parse(d[7].trim());
                        double total = Double.parseDouble(d[8].trim());
                        String req = d[9].trim();
                        String payment = d[10].trim();
                        String status = (d.length >= 12) ? d[11].trim() : "Active";
                        Room room = new SingleRoom(rNum, "Standard", "Single", 0, false);

                        bookingList.add(new Booking(cId, name, email, phone, bId, room, in, out, total, req, payment, status));
                    } catch (Exception e) {
                        System.err.println("Error parsing CSV line: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    public static boolean deleteBooking(int bookingId, boolean permanent) {
        List<Booking> allBookings = getAllBookingsAsObjects();
        boolean found = false;

        if (permanent) {
            found = allBookings.removeIf(b -> b.getBookingid() == bookingId);
        } else {
            for (Booking b : allBookings) {
                if (b.getBookingid() == bookingId) {
                    b.setStatus("Cancelled");
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            return saveAllBookings(allBookings);
        }
        return false;
    }

    private static boolean saveAllBookings(List<Booking> bookings) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            for (Booking b : bookings) {
                pw.println(b.toString());
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error rewriting file: " + e.getMessage());
            return false;
        }
    }

    public static int getTotalBookingsCount() {
        int count = 0;
        File file = new File("bookings.txt");
        if (!file.exists()) return 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void saveReceiptToFile(String bookingId, String summary) {
        try {
            File folder = new File("Receipts");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, "Receipt_" + bookingId + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(summary);
                writer.flush();
            }
            System.out.println("Receipt generated at: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("File Error: " + e.getMessage());
        }
    }
}