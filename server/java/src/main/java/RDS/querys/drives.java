package RDS.querys;

import RDS.models.Drive;

public class drives {
    static public String addDrive(Drive d) {
        return "INSERT INTO \"public\".\"rs_drives\" (\"drive_Id\", \"geoLocationSrc_Id\", \"geoLocationDest_Id\"," +
                " \"passengers\", \"type\", \"num_seat_available\", \"price\", \"AVG_Price\", \"upcoming_Drives\") " +
                "VALUES " +
                "('"+d.getDrive_Id()+"' :: uuid, '"+d.getGeoLocationSrc_Id()+"' :: uuid, '"+d.getGeoLocationDest_Id()+"' :: uuid," +
                " '"+d.getPassengers()+"', '"+d.getType()+"', "+d.getNum_seat_available()+",'"+d.getPrice()+"', '"+d.getAVG_Price()+"', '"+d.getUpcoming_Drives()+"');";
    }
    static public String updateDrive( Drive d) {
        return "UPDATE \"public\".\"rs_drives\" SET " +
                "\"geoLocationSrc_Id\" = '"+d.getGeoLocationSrc_Id()+"', " +
                "\"geoLocationDest_Id\" = '"+d.getGeoLocationDest_Id()+"'," +
                " \"passengers\" = '"+d.getPassengers()+"', " +
                "\"type\" = '"+d.getType()+"', " +
                "\"num_seat_available\" = "+d.getNum_seat_available()+", " +
                "\"price\" = '"+d.getPrice()+"', " +
                "\"AVG_Price\" = '"+d.getAVG_Price()+"', " +
                "\"upcoming_Drives\" = '"+d.getUpcoming_Drives()+"' " +
                "WHERE \"drive_Id\" = '"+d.getDrive_Id()+"';";
    }
    static public String deleteDriveById(Drive d) {
        return "DELETE FROM \"public\".\"rs_drives\" WHERE \"drive_Id\" = '"+d.getDrive_Id()+"';";
    }
    static public String getDriveById(Drive d) {
        return "SELECT * FROM \"public\".\"rs_drives\" WHERE \"drive_Id\" = '"+d.getDrive_Id()+"';";
    }
    static public String getAllUniversities() {
        return "SELECT * FROM \"public\".\"rs_drives\";";
    }
}
