package backend.Rooms;

public class DoubleRoom extends Room {

    public DoubleRoom(int RoomNumber,String description,String RoomType,double pricePerNight,boolean isAvailable)
    {
        super(RoomNumber,description, RoomType,pricePerNight,isAvailable);
    }

    @Override public double calculatePrice(int days)
    {
        return getPricePerNight() * days;
    }
}
