package controller.rds.models;
import controller.utils.MapUtils;

public class University {
    private String universityId, geoLocation_Id, university_name;


    public University(String geoLocation_Id, String university_name) {
        this.universityId = MapUtils.generateId();
        this.geoLocation_Id = geoLocation_Id;
        this.university_name = university_name;
    }
    public University(String geoLocation_Id) {
        this.universityId = MapUtils.generateId();
        this.geoLocation_Id = geoLocation_Id;
        this.university_name = "university_name";
    }


    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public String getGeoLocation_Id() {
        return geoLocation_Id;
    }

    public void setGeoLocation_Id(String geoLocation_Id) {
        this.geoLocation_Id = geoLocation_Id;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    @Override
    public String toString() {
        return "universities{" +
                "universityId='" + universityId + '\'' +
                ", geoLocation_Id='" + geoLocation_Id + '\'' +
                ", university_name='" + university_name + '\'' +
                '}';
    }
}
