package RDS.querys;
import RDS.models.Drive;
import RDS.models.User;

public class users_drives {
    public String addUsersDrives(Drive d, User u) {
        return "INSERT INTO \"public\".\"rs_users_drives\" (\"user_Id\", \"drive_Id\") " +
                "VALUES " +
                "('"+u.getUser_Id()+"', '"+d.getDrive_Id()+"');";
    }
    public String updateUsersDrives(Drive d, User u) {
        return "UPDATE \"public\".\"rs_users_drives\" SET " +
                "\"user_Id\" = '"+u.getUser_Id()+"', " +
                "\"drive_Id\" = '"+d.getDrive_Id()+"'," +
                "WHERE \"drive_Id\" = '"+d.getDrive_Id()+"' || \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    public String deleteUsersDrivesByUserId(User u) {
        return "DELETE FROM \"public\".\"rs_drives\" WHERE \"user_Id\" = '"+u.getUser_Id()+"';";
    }
    public String deleteUsersDrivesDriveId(Drive d) {
        return "DELETE FROM \"public\".\"rs_drives\" WHERE \"drive_Id\" = '"+d.getDrive_Id()+"';";
    }
    public String getUsersDrivesByDriveId(Drive d) {
        return "SELECT * FROM \"rs_users_universities\" " +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"drive_Id\" = '"+d.getDrive_Id()+"';";
    }
    public String getUsersDrivesByUserId( User u) {
        return "SELECT * FROM \"rs_users_universities\" " +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"" +
                "WHERE \"rs_users_universities\".\"user_Id\" = '"+u.getUser_Id()+"';";
    }
    public String getAllUsersDrives() {
        return "SELECT * FROM \"rs_users_universities\" " +
                "INNER JOIN \"rs_users\" ON \"rs_users_universities\".\"user_Id\" = \"rs_users\".\"user_Id\"" +
                "INNER JOIN \"rs_universities\" ON \"rs_users_universities\".\"university_Id\" = \"rs_universities\".\"university_Id\"";
    }
}
