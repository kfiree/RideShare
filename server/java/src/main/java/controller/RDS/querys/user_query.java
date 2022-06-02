package controller.RDS.querys;
import java.util.Random;

import controller.RDS.models.User;
import controller.RDS.utils.*;

public class user_query {
    static private Random rand = new Random();
    static public String addUser(User u) {
        int rand_int1 = rand.nextInt(1000);
        return "INSERT INTO \"public\".\"rs_users\" (\"user_Id\", \"email\", \"first_name\", \"last_name\", \"phone_Number\", \"Image_Id\", \"degree\", \"gender\", \"password\") VALUES " +
                "('" + u.getUser_Id() + "', '" + u.getEmail() + "', '" + u.getFirst_name() + "', '" + u.getLast_name() + "', '" + u.getPhone_Number() + "', '" + u.getImage_Id() + "', '" + u.getDegree() + "', '" + u.getGender() + "', '" + utils.EncryptPassword(u.getPassword()) + "');";
    }
    static public String updateUser(User u) {
        return "UPDATE \"public\".\"rs_users\" SET " +
                "\"first_name\" = '"+u.getFirst_name()+"', " +
                "\"last_name\" = '"+u.getLast_name()+"', " +
                "\"phone_Number\" = '"+u.getPhone_Number()+"', " +
                "\"Image_Id\" = '"+u.getImage_Id()+"', " +
                "\"degree\" = '"+u.getDegree()+"', " +
                "\"gender\" = '"+u.getGender()+"', " +
                "\"password\" = '"+utils.EncryptPassword(u.getPassword())+"' " +
                "WHERE \"user_Id\" = '"+u.getUser_Id()+"' :: uuid " +
                 " AND \"email\" = '"+u.getEmail()+"'";
    }
    static public String deleteUserById(User u) {
        return "DELETE FROM \"public\".\"rs_users\" WHERE \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    static public String deleteUserByEmail(User u) {
        return "DELETE FROM \"public\".\"rs_users\" WHERE \"email\" = '"+u.getEmail()+"';";
    }
    static public String getUserById(User u) {
        return "SELECT * FROM \"public\".\"rs_users\" WHERE \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    static public String getUserByEmail(User u) {
        return "SELECT * FROM \"public\".\"rs_users\" WHERE \"email\" = '"+u.getEmail()+"';";
    }
    static public String getAllUsers() {
        return "SELECT * FROM rs_users;";
    }

}