package backend.Rooms;

public abstract class Room {
    private int RoomNumber;
    private String description;
    private String RoomType;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(int RoomNumber, String description, String RoomType, double pricePerNight, boolean isAvailable) {
        this.RoomNumber = RoomNumber;
        this.description = description;
        this.RoomType = RoomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
    }

    public int getRoomNumber() { return RoomNumber; }
    public String getDescription() { return description; }
    public double getPricePerNight() { return pricePerNight; }
    public String getRoomType() { return RoomType; }
    public boolean isAvailable() { return isAvailable; }

    public void setDescription(String description) { this.description = description; }
    public void setRoomType(String roomType) { this.RoomType = roomType; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setAvailable(boolean status) { this.isAvailable = status; }

    public void DisplayDetails() {
        System.out.println("Room: " + RoomNumber + " | Type: " + RoomType + " | Price: $" + pricePerNight + " | Status: " + (isAvailable ? "Available" : "Occupied"));
    }

    public abstract double calculatePrice(int days);
}