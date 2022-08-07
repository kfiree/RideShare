module.exports = class User {
    constructor(user) {
        this.user_Id = user.user_Id;
        this.phone_Number = user.phone_Number;
        this.createdAt = user.createdAt;
        this.email = user.email;
        this.first_name = user.first_name;
        this.last_name = user.last_name;
        this.image_Id = user.image_Id;
        this.degree = user.degree;
        this.degree = user.degree;
        this.gender = user.gender;
        this.password = user.password;
        this.user_Id = user.user_Id;
        this.drives_offered = []; //List of User_Drives.id
        this.drives_taken = []; //List of User_Drives.id
    }
    getUser() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}


