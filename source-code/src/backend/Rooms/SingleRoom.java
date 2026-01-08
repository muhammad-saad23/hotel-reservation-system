package backend.Rooms;

public class SingleRoom extends Room
{
   public SingleRoom(int RoomNumber,String description,String RoomType,double pricePerNight,boolean isAvailable)
    {
        super(RoomNumber, description, RoomType, pricePerNight, isAvailable);
    }

    @Override public double calculatePrice(int days)
    {
        return getPricePerNight() * days;
    }
}