package RDS.models;

import java.util.UUID;

public class drives {
    public String addDrive( String drive_Id, String geoLocationSrc_Id, String geoLocationDest_Id,
                            String passengers, String type, int num_seat_available, double price,
                            double AVG_Price, String upcoming_Drives, String geoLocation_Id, String Drive_name) {
        return "INSERT INTO \"public\".\"rs_drives\" (\"drive_Id\", \"geoLocationSrc_Id\", \"geoLocationDest_Id\"," +
                " \"passengers\", \"type\", \"num_seat_available\", \"price\", \"AVG_Price\", \"upcoming_Drives\") " +
                "VALUES " +
                "('"+drive_Id+"' :: uuid, '"+geoLocationSrc_Id+"' :: uuid, '"+geoLocationDest_Id+"' :: uuid," +
                " '"+passengers+"', '"+type+"', "+num_seat_available+",'"+price+"', '"+AVG_Price+"', '"+upcoming_Drives+"');";
    }
    public String updateDrive( String drive_Id, String geoLocationSrc_Id, String geoLocationDest_Id,
                               String passengers, String type, int num_seat_available, double price,
                               double AVG_Price, String upcoming_Drives) {
        return "UPDATE \"public\".\"rs_drives\" SET " +
                "\"geoLocationSrc_Id\" = '"+geoLocationSrc_Id+"', " +
                "\"geoLocationDest_Id\" = '"+geoLocationDest_Id+"'," +
                " \"passengers\" = '"+passengers+"', " +
                "\"type\" = '"+type+"', " +
                "\"num_seat_available\" = "+num_seat_available+", " +
                "\"price\" = '"+price+"', " +
                "\"AVG_Price\" = '"+AVG_Price+"', " +
                "\"upcoming_Drives\" = '"+upcoming_Drives+"' " +
                "WHERE \"drive_Id\" = '"+drive_Id+"';";
    }
    public String deleteDriveById(String drive_Id) {
        return "DELETE FROM \"public\".\"rs_drives\" WHERE \"drive_Id\" = '"+drive_Id+"';";
    }
    public String getDriveById(String drive_Id) {
        return "SELECT * FROM \"public\".\"rs_drives\" WHERE \"drive_Id\" = '"+drive_Id+"';";
    }
    public String getAllUniversities() {
        return "SELECT * FROM \"public\".\"rs_drives\";";
    }
}
