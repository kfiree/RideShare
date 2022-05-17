package RDS.models;
import RDS.utils.utils;
import java.util.UUID;
import RDS.utils.*;

public class universities {
    public String addUniversity( String geoLocation_Id,String university_name) {
        return "INSERT INTO \"public\".\"rs_universities\" (\"university_Id\", \"geoLocation_Id\", \"university_name\") " +
                "VALUES " +
                "('"+UUID.randomUUID().toString()+"', '"+geoLocation_Id+"', '"+university_name+"');";
    }
    public String updateUniversity(String university_name,String geoLocation_Id, String university_Id) {
        return "UPDATE \"public\".\"rs_universities\" SET " +
                "\"university_name\" = '"+university_name+"', " +
                "\"geoLocation_Id\" = '"+geoLocation_Id+"' " +
                "WHERE \"university_Id\" = '"+university_Id+"';";
    }
    public String deleteUniversityById(String university_Id) {
        return "DELETE FROM \"public\".\"rs_universities\" WHERE \"university_Id\" = '"+university_Id+"';";
    }
    public String getUniversityById(String university_Id) {
        return "SELECT * FROM \"public\".\"rs_universities\" WHERE \"geoLocation_Id\" = '"+university_Id+"';";
    }
    public String getAllUniversities() {
        return "SELECT * FROM \"public\".\"rs_universities\";";
    }
}
