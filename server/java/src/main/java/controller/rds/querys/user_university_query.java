package controller.rds.querys;

import controller.rds.models.Drive;
import controller.rds.models.University;
import controller.rds.models.User;

public class user_university_query {
    static public String addUsersUniversities(University u1, User u2) {
        return "INSERT INTO \"public\".\"rs_users_universities\" (\"user_Id\", \"university_Id\") " +
                "VALUES " +
                "('"+u2.getUser_Id()+"', '"+u1.getUniversityId()+"');";
    }
    static public String updateUsersUniversities(University u1, User u2) {
        return "UPDATE \"public\".\"rs_users_universities\" SET " +
                "\"user_Id\" = '"+u2.getUser_Id()+"', " +
                "\"university_Id\" = '"+u1.getUniversityId()+"'," +
                "WHERE \"university_Id\" = '"+u1.getUniversityId()+"' || \"user_Id\" = '"+u2.getUser_Id()+"';";
    }
    static public String deleteUsersUniversitiesByUserId(User u) {
        return "DELETE FROM \"public\".\"rs_users_universities\" WHERE \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    static public String deleteUsersUniversitiesByUniversityId(University u) {
        return "DELETE FROM \"public\".\"rs_users_universities\" WHERE \"university_Id\" = '"+u.getUniversityId()+"';";
    }
    static public String getUsersUniversitiesByUniversityId(Drive d) {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"university_Id\" = '"+d.getDrive_Id()+"';";
    }
    static public String getUsersUniversitiesByUserId(User u) {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"user_Id\" = '"+u.getUser_Id()+"';";
    }
    static public String getAllUsersUniversities() {
        return "SELECT * FROM \"rs_users_universities\"" +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"";
    }
}