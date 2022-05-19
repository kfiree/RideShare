package RDS.models;

import java.util.Random;
import java.util.UUID;

public class User {
    private String user_Id, email , first_name , last_name , phone_Number , Image_Id , degree , gender , password;

    public User(String email, String first_name, String last_name, String phone_Number, String image_Id, String degree, String gender, String password) {
        this.user_Id = UUID.randomUUID().toString();
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_Number = phone_Number;
        this.Image_Id = image_Id;
        this.degree = degree;
        this.gender = gender;
        this.password = password;
    }
    public User(String email) {
        Random r = new Random(10000000);
        this.user_Id = UUID.randomUUID().toString();;
        this.email = email;
        this.first_name = "first_name";
        this.last_name = "last_name";
        this.phone_Number = "phone_Number";
        this.Image_Id = "image_Id";
        this.degree = "degree";
        this.gender = "gender";
        this.password = "123456789";
    }
    public User() {
        Random r = new Random(10000000);
        this.user_Id = UUID.randomUUID().toString();;
        this.email = "email" + r.nextDouble(1000000000) + "@gmail.com";
        this.first_name = "first_name";
        this.last_name = "last_name";
        this.phone_Number = "phone_Number";
        this.Image_Id = "image_Id";
        this.degree = "degree";
        this.gender = "gender";
        this.password = "123456789";
    }

    public String getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(String user_Id) {
        this.user_Id = user_Id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_Number() {
        return phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        this.phone_Number = phone_Number;
    }

    public String getImage_Id() {
        return Image_Id;
    }

    public void setImage_Id(String image_Id) {
        Image_Id = image_Id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "users{" +
                "user_Id='" + user_Id + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone_Number='" + phone_Number + '\'' +
                ", Image_Id='" + Image_Id + '\'' +
                ", degree='" + degree + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}