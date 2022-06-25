package controller.rds.models;
import controller.utils.MapUtils;

public class Drive {

    private String drive_Id , geoLocationSrc_Id , geoLocationDest_Id , passengers , type ,upcoming_Drives, date ,leaveTime;
    private int num_seat_available;
    private double price, AVG_Price;

    public Drive(String geoLocationSrc_Id, String geoLocationDest_Id, String passengers,  String leaveTime, String date, int num_seat_available, double price) {
        this.drive_Id = MapUtils.generateId();
        this.geoLocationSrc_Id = geoLocationSrc_Id;
        this.geoLocationDest_Id = geoLocationDest_Id;
        this.passengers = passengers;
        this.type = "type";
        this.upcoming_Drives = "upcoming_Drives";
        this.num_seat_available = num_seat_available;
        this.price = price;
        this.AVG_Price = 0;
        this.leaveTime = leaveTime;
        this.date = date;
    }
    public Drive(String geoLocationSrc_Id, String geoLocationDest_Id, String passengers, String type, String upcoming_Drives, int num_seat_available, double price, double AVG_Price) {
        this.drive_Id = MapUtils.generateId();
        this.geoLocationSrc_Id = geoLocationSrc_Id;
        this.geoLocationDest_Id = geoLocationDest_Id;
        this.passengers = passengers;
        this.type = type;
        this.upcoming_Drives = upcoming_Drives;
        this.num_seat_available = num_seat_available;
        this.price = price;
        this.AVG_Price = AVG_Price;
    }

    public Drive(String geoLocationSrc_Id, String geoLocationDest_Id) {
        this.drive_Id = MapUtils.generateId();
        this.geoLocationSrc_Id = geoLocationSrc_Id;
        this.geoLocationDest_Id = geoLocationDest_Id;
        this.passengers = "passengers";
        this.type = "type";
        this.upcoming_Drives = "upcoming_Drives";
        this.num_seat_available = 4;
        this.price = 0;
        this.AVG_Price = 0;
    }

    public String getDrive_Id() {
        return drive_Id;
    }

    public void setDrive_Id(String drive_Id) {
        this.drive_Id = drive_Id;
    }

    public String getGeoLocationSrc_Id() {
        return geoLocationSrc_Id;
    }

    public void setGeoLocationSrc_Id(String geoLocationSrc_Id) {
        this.geoLocationSrc_Id = geoLocationSrc_Id;
    }

    public String getGeoLocationDest_Id() {
        return geoLocationDest_Id;
    }

    public void setGeoLocationDest_Id(String geoLocationDest_Id) {
        this.geoLocationDest_Id = geoLocationDest_Id;
    }

    public String getPassengers() {
        return passengers;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpcoming_Drives() {
        return upcoming_Drives;
    }

    public void setUpcoming_Drives(String upcoming_Drives) {
        this.upcoming_Drives = upcoming_Drives;
    }

    public int getNum_seat_available() {
        return num_seat_available;
    }

    public void setNum_seat_available(int num_seat_available) {
        this.num_seat_available = num_seat_available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAVG_Price() {
        return AVG_Price;
    }

    public void setAVG_Price(double AVG_Price) {
        this.AVG_Price = AVG_Price;
    }

    @Override
    public String toString() {
        return "drives{" +
                "drive_Id='" + drive_Id + '\'' +
                ", geoLocationSrc_Id='" + geoLocationSrc_Id + '\'' +
                ", geoLocationDest_Id='" + geoLocationDest_Id + '\'' +
                ", passengers='" + passengers + '\'' +
                ", type='" + type + '\'' +
                ", upcoming_Drives='" + upcoming_Drives + '\'' +
                ", num_seat_available=" + num_seat_available +
                ", price=" + price +
                ", AVG_Price=" + AVG_Price +
                '}';
    }
}
