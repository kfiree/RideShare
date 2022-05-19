package RDS.querys;

import RDS.models.GeoLocation;

import java.util.UUID;

public class geoLocation {

    public String addGeoLocation(GeoLocation g) {
        return "INSERT INTO \"public\".\"rs_geoLocation\" (\"geoLocation_Id\", \"latitude\", \"longitude\", \"nameLocation\") " +
                "VALUES " +
                "('" + g.getGeoLocationId() + "', " + g.getLatitude() + ", " + g.getLongitude() + ", '" + g.getNameLocation() + "');";
    }
    public String updateGeoLocation(GeoLocation g) {
        return "UPDATE \"public\".\"rs_geoLocation\" SET " +
                "\"latitude\" = "+g.getLatitude()+", " +
                "\"longitude\" = "+g.getLongitude()+", " +
                "\"nameLocation\" = '"+g.getNameLocation()+"' " +
                "WHERE \"geoLocation_Id\" = '"+g.getGeoLocationId()+"' :: uuid;";
    }
    public String deleteGeoLocationById(GeoLocation g) {
        return "DELETE FROM \"public\".\"rs_geoLocation\" WHERE \"geoLocation_Id\" = '"+g.getGeoLocationId()+"';";
    }
    public String getGeoLocationById(GeoLocation g) {
        return "SELECT * FROM \"public\".\"rs_geoLocation\" WHERE \"geoLocation_Id\" = '"+g.getGeoLocationId()+"';";
    }
    public String getAllGeoLocations() {
        return "SELECT * FROM \"public\".\"rs_geoLocation\" ;";
    }
}
