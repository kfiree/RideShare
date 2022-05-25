package RDS.querys;

import RDS.models.Drive;
import RDS.models.University;
import RDS.models.User;
import RDS.utils.utils;

import java.util.Random;

public class users_universities {
    public String addUsersUniversities(University u1, User u2) {
        return "INSERT INTO \"public\".\"rs_users_universities\" (\"user_Id\", \"university_Id\") " +
                "VALUES " +
                "('"+u2.getUser_Id()+"', '"+u1.getUniversityId()+"');";
    }
    public String updateUsersUniversities(University u1, User u2) {
        return "UPDATE \"public\".\"rs_users_universities\" SET " +
                "\"user_Id\" = '"+u2.getUser_Id()+"', " +
                "\"university_Id\" = '"+u1.getUniversityId()+"'," +
                "WHERE \"university_Id\" = '"+u1.getUniversityId()+"' || \"user_Id\" = '"+u2.getUser_Id()+"';";
    }
    public String deleteUsersUniversitiesByUserId(User u) {
        return "DELETE FROM \"public\".\"rs_users_universities\" WHERE \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    public String deleteUsersUniversitiesByUniversityId(University u) {
        return "DELETE FROM \"public\".\"rs_users_universities\" WHERE \"university_Id\" = '"+u.getUniversityId()+"';";
    }
    public String getUsersUniversitiesByUniversityId(Drive d) {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"university_Id\" = '"+d.getDrive_Id()+"';";
    }
    public String getUsersUniversitiesByUserId(User u) {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"user_Id\" = '"+u.getUser_Id()+"';";
    }
    public String getAllUsersUniversities() {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"";
    }
}