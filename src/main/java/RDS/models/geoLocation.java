package RDS.models;

import RDS.utils.utils;

import java.util.UUID;

public class geoLocation {

    public String addGeoLocation(double latitude, double longitude, String nameLocation) {
        return "INSERT INTO \"public\".\"rs_geoLocation\" (\"geoLocation_Id\", \"latitude\", \"longitude\", \"nameLocation\") " +
                "VALUES " +
                "('" + UUID.randomUUID().toString() + "', " + latitude + ", " + longitude + ", '" + nameLocation + "');";
    }
    public String updateGeoLocation(String geoLocation_Id,String nameLocation, double longitude, double latitude) {
        return "UPDATE \"public\".\"rs_geoLocation\" SET " +
                "\"latitude\" = "+latitude+", " +
                "\"longitude\" = "+longitude+", " +
                "\"nameLocation\" = '"+nameLocation+"' " +
                "WHERE \"geoLocation_Id\" = '"+geoLocation_Id+"' :: uuid;";
    }
    public String deleteGeoLocationById(String geoLocation_Id) {
        return "DELETE FROM \"public\".\"rs_geoLocation\" WHERE \"geoLocation_Id\" = '"+geoLocation_Id+"';";
    }
    public String getGeoLocationById(String geoLocation_Id) {
        return "SELECT * FROM \"public\".\"rs_geoLocation\" WHERE \"geoLocation_Id\" = '"+geoLocation_Id+"';";
    }
    public String getAllGeoLocations() {
        return "SELECT * FROM \"public\".\"rs_geoLocation\" ;";
    }
}
