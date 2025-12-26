package backend.Rooms;

public abstract class Room {

    private int RoomNumber;
    private String RoomType;
    private double pricePerNight;
    private boolean isAvailable;


    public Room(int RoomNumber, String RoomType, double pricePerNight) {
        this.RoomNumber = RoomNumber;
        this.RoomType = RoomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public int getRoomNumber() {
        return RoomNumber;
    }
    public double getPricePerNight() {
        return pricePerNight;
    }
    public String getRoomType() {
        return RoomType;
    }
    public boolean isAvailable() {
        return isAvailable;
    }


    public void setRoomType(String roomType) {
        RoomType = roomType;
    }
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
    public void setAvailable(boolean status) {
        this.isAvailable = status;
    }

    public void DisplayDetails(){
        System.out.println("RoomNumber: " +this.RoomNumber +"\n" +"RoomType: " + this.RoomType + "\n" + "Total price: " + this.pricePerNight);
    }

    public abstract double calculatePrice(int days);


}