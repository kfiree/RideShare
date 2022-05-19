package RDS;

import RDS.models.*;
import RDS.querys.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

import static RDS.checkQuerys.addToDB;
import static RDS.checkQuerys.connection;

public class addFromJson {
    static String ariel = "a612239d-dafb-474c-98d8-056a4282e989";
    static HashMap<Integer, geoLocation> geoLocations;
    static geoLocation geo = new geoLocation();
    static drives drives = new drives();
    static GeoLocation [] locations  = checkQuerys.getFromDB.getFromDB(connection, geo.getAllGeoLocations());
    static User user;
    static users users = new users();
    static users_drives users_drives = new users_drives();
    static int indexGeo = 0;
    static int num = 0;

    private static void parseLocationObject(JSONObject location) {
        String latitude = (String) location.get("latitude");
        String longtitude = (String) location.get("longtitude");
        String name = (String) location.get("name") + num++;

        GeoLocation geoLocation = new GeoLocation(Double.parseDouble(latitude), Double.parseDouble(longtitude), name);
        geoLocation geoLocationRDS = new geoLocation();
        System.out.println("addGeoLocation -> " + checkQuerys.addToDB.addToDB(connection,geoLocationRDS.addGeoLocation(geoLocation)));
    }

//    private static GeoLocation[] getAllGeoLocation() {
//        locations = checkQuerys.getFromDB.getFromDB(connection, geo.getAllGeoLocations());
//
//        try {
//            while (!rs.isClosed() && rs.next()) {
//                String geoLocation_Id = rs.getString("geoLocation_Id");
//                String nameLocation = rs.getString("nameLocation");
//                double latitude = rs.getDouble("latitude");
//                double longitude = rs.getDouble("longitude");
//                GeoLocation g = new GeoLocation(latitude,longitude,nameLocation);
//                locations = Arrays.copyOf(locations, locations.length + 1);
//                locations[locations.length - 1] = g;
//            }
//            return locations;
//        }catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static void parseTripsObject(JSONObject trip) {
        String driverId = (String) trip.get("driver_id");
        String src = (String) trip.get("src");
        String dest = (String) trip.get("dest");
        String id = (String) trip.get("id");
//        Date date = (Date) trip.get("date");
//        int seats = Integer.parseInt((String) trip.get("seats"));
//        double price = Double.parseDouble((String) trip.get("price"));
//        String passengers = ((String) trip.get("passengers"));
        String leaveTime = (String) trip.get("leaveTime");

        if(locations != null){
        Drive drive;
//        System.out.println("locationId -> " + locations[((++indexGeo) % locations.length)]);
        if(id == ariel){
            System.out.println("if");
            drive = new Drive(
                    locations[((++indexGeo) % locations.length)].getGeoLocationId(),
                ariel,
                "passengers",
                leaveTime,
                "date",
                4,
                15
            );
        }else{
            drive = new Drive(
                    locations[((++indexGeo) % locations.length)].getGeoLocationId(),
                    locations[((++indexGeo) % locations.length)].getGeoLocationId(),
                "passengers",
                leaveTime,
                "date",
                4,
                15
            );
        }
            System.out.println("addDrive -> " + checkQuerys.addToDB.addToDB(connection,drives.addDrive(drive)));
            String  email = "email" + (++indexGeo) + "@gmail.com";
            User user = new User("user" + (indexGeo)+ "@gmail.com" );
            System.out.println("addUser -> " + checkQuerys.addToDB.addToDB(connection,users.addUser(user)));
            System.out.println("addDrive -> " + checkQuerys.addToDB.addToDB(connection,drives.addDrive(drive)));
            System.out.println("addUsers_drives -> " + checkQuerys.addToDB.addToDB(connection,users_drives.addUsersDrives( drive,user)));
        }
    }

    public static void main(String[] args) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        //locations
//        try (FileReader reader = new FileReader("/Users/motidahari/projects/RideShare/src/main/java/RDS/locations.json")) {
//            //Read JSON file
//            Object obj = jsonParser.parse(reader);
//            JSONArray locations = (JSONArray) obj;
//            //Iterate over locations array
////            System.out.println("locations => " + locations);
//
//            locations.forEach(loc -> parseLocationObject((JSONObject) loc));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //trips
        try (FileReader reader = new FileReader("/Users/motidahari/projects/RideShare/src/main/java/RDS/trips.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray trips = (JSONArray) obj;
            //Iterate over locations array
            trips.forEach(trip -> parseTripsObject((JSONObject) trip));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
