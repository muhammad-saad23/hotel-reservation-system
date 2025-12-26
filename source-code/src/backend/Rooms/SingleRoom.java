package backend.Rooms;

public class SingleRoom extends Room
{
   public SingleRoom(int RoomNumber,String RoomType,double pricePerNight)
    {
        super(RoomNumber, RoomType,pricePerNight);
    }

    @Override public double calculatePrice(int days)
    {
        return getPricePerNight() * days;
    }
}