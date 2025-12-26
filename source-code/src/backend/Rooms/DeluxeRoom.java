package backend.Rooms;

public class DeluxeRoom extends Room{
    public DeluxeRoom(int RoomNumber,String RoomType,double pricePerNight)
    {
        super(RoomNumber, RoomType,pricePerNight);
    }

    @Override
    public double calculatePrice(int days)
    {
        return getPricePerNight() * days;
    }
}
