package controller.rds.config;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connect {
    public static Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(config.URI);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
//            return DriverManager.getConnection(dbUrl, username, password);
            connection = DriverManager.getConnection(dbUrl, username, password);
            if(connection != null){
                System.out.println("database is connected!");
            }else{
                System.out.println("database is not connected!");
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
