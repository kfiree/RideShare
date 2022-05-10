package SQL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class connect {
//        final static String query = "CALL `myProcedure`(param)";
        final static String Driver = "com.mysql.cj.jdbc.Driver";
        final static String path_DB = "jdbc:mysql://localhost/northwind?serverTimezone=UTC";
        final static String userName = "root";
        final static String passUser = "123456789";
//        static String ColNames = "OrderID\t\t|\t\tProfit/Loss\t\t|\t\tAmount\n_____________________________________________________________";
        Connection con = null;
        static Statement stmt = null;
//        static int numOfColumns;



        /**
         * connectDB - The function connects to a database and then calls a query,
         * and prints the returned results from the database
         * */
        private static void connectDB() {
            try {
                Class.forName(Driver);
                try (Connection con = DriverManager.getConnection(path_DB, userName, passUser)) {
                    stmt = con.createStatement();
                    String query = "SELECT 1 + 1";
                    ResultSet rs = stmt.executeQuery(query);
//                    System.out.println("\n" + ColNames);
//                    while (rs.next()) {
//                        System.out.println(rs.getInt(1) + "\t\t|\t\t" + rs.getString(2) + "\t\t\t|\t\t" + fixString(rs.getFloat(3)) + "\t\t|\t\t");
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void main(String[] args) {
            connectDB();
        }


}
