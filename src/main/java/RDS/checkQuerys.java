package RDS;
import RDS.config.*;
import RDS.models.*;
import org.json.simple.JSONObject;

import java.sql.*;
import java.util.UUID;

public class checkQuerys {
    static Connection connection = connect.connection;
    static users users = new users();
    static universities universities = new universities();
    static nodes nodes = new nodes();
    static geoLocation geoLocation = new geoLocation();
    static edges edges = new edges();
    static drives drives = new drives();

    interface getFromDB {
        ResultSet getFromDB(Connection con , String query);
    }
    interface addToDB {
        boolean addToDB(Connection con , String query);
    }
    interface updateInDB {
        boolean updateInDB(Connection con , String query);
    }
    interface deleteFromDB {
        boolean deleteFromDB(Connection con , String query);
    }

    static getFromDB getFromDB = (con , query) -> {
    try (Statement stmt = con.createStatement()) {
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    };
    static addToDB addToDB = (con , query) -> {
    try (PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
        int affectedRows = pstmt.executeUpdate();
        // check the affected rows
        if (affectedRows > 0) {
            return true;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
    return false;
    };
    static deleteFromDB deleteFromDB = (con , query) -> {
    try (PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
        int affectedRows = pstmt.executeUpdate();
        // check the affected rows
        if (affectedRows > 0) {
            return true;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
    return false;
    };
    static updateInDB updateInDB = (con , query) -> {
    try (PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
        int affectedRows = pstmt.executeUpdate();
        // check the affected rows
        if (affectedRows > 0) {
            return true;
        }
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
    return false;
    };


    private static void checkGetLambda() {
        //users
//        System.out.println("getUserById" + getFromDB.getFromDB(connection, users.getUserById("f3ede544-2a6c-4ccf-ba4c-424ea6014bae")));
//        System.out.println("getUserByEmail" + getFromDB.getFromDB(connection, users.getUserByEmail("moti@gmail.com")));
//        System.out.println("getAllUsers" + getFromDB.getFromDB(connection, users.getAllUsers()));


        //geoLocation
//        System.out.println("getGeoLocationById -> " + getFromDB.getFromDB(connection, geoLocation.getGeoLocationById("3ffbbb79-a094-4cde-9667-8bb65a82800d")));
//        System.out.println("getAllGeoLocations -> " + getFromDB.getFromDB(connection, geoLocation.getAllGeoLocations()));

        //universities
//        System.out.println("getUniversityById -> " + getFromDB.getFromDB(connection,universities.getUniversityById( "42e7c07d-20a7-43e6-8dda-1adc5f346a2a")));
//        System.out.println("getAllUniversities -> " + getFromDB.getFromDB(connection,universities.getAllUniversities()));

        //drives
//        System.out.println("getDriveById -> " + getFromDB.getFromDB(connection,drives.getDriveById( "90b949d5-125c-4b36-9fc9-5bcd3716a278")));
//        System.out.println("getAllUniversities -> " + getFromDB.getFromDB(connection,drives.getAllUniversities()));



        //edges
//            System.out.println("addDrive -> " + getFromDB.getFromDB(connection,edges.getEdgeById(123456789)));
//            System.out.println("addDrive -> " + getFromDB.getFromDB(connection,edges.getAllEdges()));


        //nodes
//            System.out.println("addDrive -> " + getFromDB.getFromDB(connection,nodes.getNodeById(123456789)));
//            System.out.println("addDrive -> " + getFromDB.getFromDB(connection,nodes.getAllNodes()));

    }

    private static void checkDeleteLambda() {
        //users
//        System.out.println("deleteUserById -> " + deleteFromDB.deleteFromDB(connection, users.deleteUserById("216c77ff-6f3d-4d6c-a3f9-c88df40c10f7")));
//        System.out.println("deleteUserByEmail -> " + deleteFromDB.deleteFromDB(connection, users.deleteUserByEmail("moti 7@gmail.com")));

        //geoLocation
//        System.out.println("deleteGeoLocationById -> " + deleteFromDB.deleteFromDB(connection, geoLocation.deleteGeoLocationById("05c7d943-94ac-4e58-b506-fb126abbd656")));


        //universities
//            System.out.println("deleteUniversityById -> " + deleteFromDB.deleteFromDB(connection,universities.deleteUniversityById("2efe0d56-e282-4996-8755-eb3397d2a4c0")));

        //drives
//            System.out.println("deleteDriveById -> " + deleteFromDB.deleteFromDB(connection,drives.deleteDriveById("90b949d0-125c-4b36-9fc9-5bcd3716a278")));

        //edges
//        System.out.println("deleteEdgeById -> " + deleteFromDB.deleteFromDB(connection,edges.deleteEdgeById(11111111)));

        //nodes
//        System.out.println("deleteNodeById -> " + deleteFromDB.deleteFromDB(connection,nodes.deleteNodeById(11111111)));

    }

    private static void checkUpdateLambda() {
        //users
//        System.out.println("updateUser -> " + updateInDB.updateInDB(connection, users.updateUser("34ce8141-744d-4310-9c3a-850bf654da2e", "moti 8@gmail.com", "בישמעק", "בישמעק", "0525699666", "גכנגנגכנגכנגכנגכנ", "גדהדגה", "דגהדהג", "123654879")));

        //geoLocation
//        System.out.println("updateGeoLocation -> " + updateInDB.updateInDB(connection, geoLocation.updateGeoLocation("05c7d943-94ac-4e58-b506-fb126abbd656", "uni changed", 22.111111,22.111111 )));

        //universities
//            System.out.println("updateUniversity -> " + updateInDB.updateInDB(connection,universities.updateUniversity("some change", "10b949d9-125c-4b36-9fc9-5bcd3716a278", "2efe0d56-e282-4996-8755-eb3397d2a4c0")));

        //drives
//            System.out.println("updateDrive -> " + updateInDB.updateInDB(connection,drives.updateDrive("0d9e3f26-2ee5-4c5f-a4f8-e1c35a91ce7e", "0d9e3f26-2ee5-4c5f-a4f8-e1c35a91ce5e", "0d9e3f26-2ee5-4c5f-a4f8-e1c35a91ce5e", "4", "type", 5, 10.5, 10.5, "upco")));


        //edges
//        System.out.println("updateEdge -> " + updateInDB.updateInDB(connection,edges.updateEdge(11111111, 11111111, 11111111, 10, 10, "namechange", "highchange")));

        JSONObject json = new JSONObject();
        json.put("name", "foo");
        //nodes
//        System.out.println("updateNode -> " + updateInDB.updateInDB(connection,nodes.updateNode(11111111, 22.3333, 22.3333, 50, json, json)));

    }

    private static void checkAddLambda() {
//        for (int i = 0; i < 9; i++) {
            //users
//            System.out.println("addUser -> " + addToDB.addToDB(connection,users.addUser("moti "+i+"@gmail.com", "sdvn", "dvsdsvdsv", "0525675171", "sdvsdvdsvdvsvdsvds", "sdvdsv", "vddvs", "sdvsdvdsvsdv")));

            //geoLocation
//            System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(23.55555, 23.55555, "UNI")));


            //universities
//            System.out.println("addUniversity -> " + addToDB.addToDB(connection,universities.addUniversity("12a39bd2-3763-467d-8aa0-e6b1cc2212de", "some name")));


            //drives
//            System.out.println("addDrive -> " + addToDB.addToDB(connection,drives.addDrive("90b949d"+i+"-125c-4b36-9fc9-5bcd3716a278", "10b949d9-125c-4b36-9fc9-5bcd3716a278", "10b949d9-125c-4b36-9fc9-5bcd3716a278", "some", "some", 5, 5.5, 5.5, "some", "some", "some")));


            //edges
//            System.out.println("addEdge -> " + addToDB.addToDB(connection,edges.addEdge(11111111, 1254789, 1254789, 50, 50, "name", "high")));

            JSONObject json = new JSONObject();
            json.put("name", "foo");
            //nodes
//            System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(11111111, 22.3333, 22.3333, 50, json, json)));

//        }
    }



    public static void main(String[] args) {
        checkGetLambda();
        checkAddLambda();
        checkDeleteLambda();
        checkUpdateLambda();

    }
}
