package backend.Rooms;

public class DoubleRoom extends Room {

    public DoubleRoom(int RoomNumber,String RoomType,double pricePerNight)
    {
        super(RoomNumber, RoomType,pricePerNight);
    }

    @Override public double calculatePrice(int days)
    {
        return getPricePerNight() * days;
    }
}
