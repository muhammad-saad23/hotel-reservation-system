package backend.Rooms;

public class DeluxeRoom extends Room {
    public DeluxeRoom(int RoomNumber, String description, String RoomType, double pricePerNight, boolean isAvailable) {
        super(RoomNumber, description, RoomType, pricePerNight, isAvailable);
    }

    @Override
    public double calculatePrice(int days) {
        return getPricePerNight() * days;
    }
}