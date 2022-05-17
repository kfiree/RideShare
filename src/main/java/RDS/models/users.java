package RDS.models;
import java.util.UUID;
import java.util.Random;
import RDS.utils.*;

public class users {
    Random rand = new Random();

    public String addUser(String email, String first_name, String last_name, String phone_Number, String Image_Id, String degree, String gender, String password) {
        int rand_int1 = rand.nextInt(1000);
        return "INSERT INTO \"public\".\"rs_users\" (\"user_Id\", \"email\", \"first_name\", \"last_name\", \"phone_Number\", \"Image_Id\", \"degree\", \"gender\", \"password\") VALUES " +
                "('" + UUID.randomUUID().toString() + "', '" + email + "', '" + first_name + "', '" + last_name + "', '" + phone_Number + "', '" + Image_Id + "', '" + degree + "', '" + gender + "', '" + utils.EncryptPassword(password) + "');";
    }
    public String updateUser(String user_Id,String email, String first_name,String last_name,String phone_Number,String Image_Id,String degree, String gender,String password) {
        return "UPDATE \"public\".\"rs_users\" SET " +
                "\"first_name\" = '"+first_name+"', " +
                "\"last_name\" = '"+last_name+"', " +
                "\"phone_Number\" = '"+phone_Number+"', " +
                "\"Image_Id\" = '"+Image_Id+"', " +
                "\"degree\" = '"+degree+"', " +
                "\"gender\" = '"+gender+"', " +
                "\"password\" = '"+utils.EncryptPassword(password)+"' " +
                "WHERE \"user_Id\" = '"+user_Id+"' :: uuid " +
                 " AND \"email\" = '"+email+"'";
    }
    public String deleteUserById(String userId) {
        return "DELETE FROM \"public\".\"rs_users\" WHERE \"user_Id\" = '"+userId+"';";
    }
    public String deleteUserByEmail(String email) {
        return "DELETE FROM \"public\".\"rs_users\" WHERE \"email\" = '"+email+"';";
    }
    public String getUserById(String userId) {
        return "SELECT * FROM \"public\".\"rs_users\" WHERE \"user_Id\" = '"+userId+"';";
    }
    public String getUserByEmail(String email) {
        return "SELECT * FROM \"public\".\"rs_users\" WHERE \"email\" = '"+email+"';";
    }
    public String getAllUsers() {
        return "SELECT * FROM rs_users;";
    }

}