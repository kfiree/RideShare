package controller.RDS;

import controller.RDS.config.connect;
import controller.RDS.models.*;
import controller.RDS.querys.*;

import java.sql.*;
import java.util.Arrays;

public class checkQuerys {
    static Connection connection = connect.connection;
//    static user_query users = new user_query();
//    static universities_query universities = new universities_query();
//    static nodes nodes = new nodes();
    static geoLocation_query geoLocation_query = new geoLocation_query();
//    static edge_q edges = new edge_q();
//    static drives drives = new drives();

    interface getFromDB {
         GeoLocation[] getFromDB(Connection con , String query);
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
         GeoLocation [] locations  = new GeoLocation[0];

        try {
            while (!rs.isClosed() && rs.next()) {
                String geoLocation_Id = rs.getString("geoLocation_Id");
                String nameLocation = rs.getString("nameLocation");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                GeoLocation g = new GeoLocation(geoLocation_Id,latitude,longitude,nameLocation);

                locations = Arrays.copyOf(locations, locations.length + 1);
                locations[locations.length - 1] = g;
            }
            return locations;
        }catch (SQLException e) {
            e.printStackTrace();
        }




        return null;
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
//        User u1 = new User();
//        System.out.println("addUser -> " + addToDB.addToDB(connection,users.addUser(u1)));
//        System.out.println("getUserById" + getFromDB.getFromDB(connection, users.getUserById(u1)));
//        System.out.println("getUserByEmail" + getFromDB.getFromDB(connection, users.getUserByEmail(u1)));
//        System.out.println("getAllUsers" + getFromDB.getFromDB(connection, users.getAllUsers()));


        //geoLocation
//        GeoLocation g2 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g2)));
//        System.out.println("getGeoLocationById -> " + getFromDB.getFromDB(connection, geoLocation.getGeoLocationById(g2)));
//        System.out.println("getAllGeoLocations -> " + getFromDB.getFromDB(connection, geoLocation.getAllGeoLocations()));

        //universities
//        GeoLocation g2 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g2)));
//        University u = new University(g2.getGeoLocationId());
//        System.out.println("addUniversity -> " + addToDB.addToDB(connection,universities.addUniversity(u)));
//        System.out.println("getUniversityById -> " + getFromDB.getFromDB(connection,universities.getUniversityById( u)));
//        System.out.println("getAllUniversities -> " + getFromDB.getFromDB(connection,universities.getAllUniversities()));

        //drives
//        GeoLocation g3 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g3)));
//        Drive d = new Drive(g3.getGeoLocationId(), g3.getGeoLocationId());
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,drives.addDrive(d)));
//        System.out.println("getDriveById -> " + getFromDB.getFromDB(connection,drives.getDriveById( d)));
//        System.out.println("getAllUniversities -> " + getFromDB.getFromDB(connection,drives.getAllUniversities()));


        //nodes
//        Node n = new Node();
//        System.out.println("add nodes -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//        System.out.println("get node by id -> " + getFromDB.getFromDB(connection,nodes.getNodeById(n)));
        System.out.println("get all nodes -> " + getFromDB.getFromDB(connection,node_query.getAllNodes()));

        //edges
//        Node n = new Node();
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//        Edge e = new Edge(n.getNode_Id(), n.getNode_Id());
//        System.out.println("addEdge -> " + addToDB.addToDB(connection,edges.addEdge(e)));
//        System.out.println("addDrive -> " + getFromDB.getFromDB(connection,edges.getEdgeById(e)));
//        System.out.println("addDrive -> " + getFromDB.getFromDB(connection,edges.getAllEdges()));

    }

    private static void checkDeleteLambda() {
        //users
//        User u1 = new User();
//        System.out.println("addUser -> " + addToDB.addToDB(connection,users.addUser(u1)));
//        System.out.println("deleteUserById -> " + deleteFromDB.deleteFromDB(connection, users.deleteUserById(u1)));
//        System.out.println("addUser -> " + addToDB.addToDB(connection,users.addUser(u1)));
//        System.out.println("deleteUserByEmail -> " + deleteFromDB.deleteFromDB(connection, users.deleteUserByEmail(u1)));

        //geoLocation
//        GeoLocation g2 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g2)));
//        System.out.println("deleteGeoLocationById -> " + deleteFromDB.deleteFromDB(connection, geoLocation.deleteGeoLocationById(g2)));


        //universities
//        GeoLocation g2 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g2)));
//        University u = new University(g2.getGeoLocationId());
//        System.out.println("addUniversity -> " + addToDB.addToDB(connection,universities.addUniversity(u)));
//        System.out.println("deleteUniversityById -> " + deleteFromDB.deleteFromDB(connection,universities.deleteUniversityById(u)));

        //drives
//        GeoLocation g3 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g3)));
//        Drive d = new Drive(g3.getGeoLocationId(), g3.getGeoLocationId());
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,drives.addDrive(d)));
//        System.out.println("deleteDriveById -> " + deleteFromDB.deleteFromDB(connection,drives.deleteDriveById(d)));


        //nodes
//        Node n = new Node();
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//        System.out.println("deleteNodeById -> " + deleteFromDB.deleteFromDB(connection,nodes.deleteNodeById(n)));


        //edges
//        Node n = new Node();
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//        Edge e = new Edge(n.getNode_Id(), n.getNode_Id());
//        System.out.println("addEdge -> " + addToDB.addToDB(connection,edges.addEdge(e)));
//        System.out.println("deleteEdgeById -> " + deleteFromDB.deleteFromDB(connection,edges.deleteEdgeById(e)));

    }

    private static void checkUpdateLambda() {
        //users
//        User u = new User();
//        System.out.println("addUser -> " + addToDB.addToDB(connection,users.addUser(u)));
//        System.out.println("updateUser -> " + updateInDB.updateInDB(connection, users.updateUser(u)));

        //geoLocation
//        GeoLocation g1 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g1)));
//        System.out.println("updateGeoLocation -> " + updateInDB.updateInDB(connection, geoLocation.updateGeoLocation(g1 )));

        //universities
//        GeoLocation g2 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g2)));
//        University u = new University(g2.getGeoLocationId());
//        System.out.println("addUniversity -> " + addToDB.addToDB(connection,universities.addUniversity(u)));
//        System.out.println("updateUniversity -> " + updateInDB.updateInDB(connection,universities.updateUniversity(u)));

        //drives
//        GeoLocation g3 = new GeoLocation();
//        System.out.println("addGeoLocation -> " + addToDB.addToDB(connection,geoLocation.addGeoLocation(g3)));
//        Drive d = new Drive(g3.getGeoLocationId(), g3.getGeoLocationId());
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,drives.addDrive(d)));
//        System.out.println("updateDrive -> " + updateInDB.updateInDB(connection,drives.updateDrive(d)));



        //nodes
//        Node n = new Node();
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//        System.out.println("updateNode -> " + updateInDB.updateInDB(connection,nodes.updateNode(n)));



        //edges
//        Node src = new Node();
//        Node dest = new Node();
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(src)));
//        System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(dest)));
//        Edge e = new Edge(src.getNode_Id(), dest.getNode_Id());
//        System.out.println("addEdge -> " + addToDB.addToDB(connection,edges.addEdge(e)));
//        System.out.println("updateEdge -> " + updateInDB.updateInDB(connection,edges.updateEdge(e)));
    }

    private static void checkAddLambda() {
        for (int i = 0; i < 40; i++) {
            //users
            User u = new User();
            System.out.println("addUser -> " + addToDB.addToDB(connection,user_query.addUser(u)));

            //geoLocation
            GeoLocation g1 = new GeoLocation();
            System.out.println("addGeoLocation -> " + addToDB.addToDB(connection, geoLocation_query.addGeoLocation(g1)));


            //universities
            GeoLocation g2 = new GeoLocation();
            System.out.println("addGeoLocation -> " + addToDB.addToDB(connection, geoLocation_query.addGeoLocation(g2)));
            University un = new University(g2.getGeoLocationId());
            System.out.println("addUniversity -> " + addToDB.addToDB(connection, university_query.addUniversity(un)));


            //drives
            GeoLocation g3 = new GeoLocation();
            System.out.println("addGeoLocation -> " + addToDB.addToDB(connection, geoLocation_query.addGeoLocation(g3)));
            Drive d = new Drive(g3.getGeoLocationId(), g3.getGeoLocationId());
            System.out.println("addDrive -> " + addToDB.addToDB(connection, drive_query.addDrive(d)));

            //nodes
//            Node n = new Node();
//            System.out.println("addNode -> " + addToDB.addToDB(connection,nodes.addNode(n)));
//            System.out.println("Node -> " + n);
//
//
//            //edges
//            Node n2 = new Node();
//            System.out.println("addDrive -> " + addToDB.addToDB(connection,nodes.addNode(n2)));
//            Edge e = new Edge(n2.getNode_Id(), n2.getNode_Id());
//            System.out.println("addEdge -> " + addToDB.addToDB(connection,edges.addEdge(e)));


        }
    }



    public static void main(String[] args) {

        checkGetLambda();
//        checkAddLambda();
//        checkDeleteLambda();
//        checkUpdateLambda();
    }
}
