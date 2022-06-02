module.exports = class Drive {
    constructor(drive) {
        this.drive_Id = drive.drive_Id;//uuid
        this.geoLocationSrc_Id = drive.geoLocationSrc_Id;//uuid
        this.geoLocationDest_Id = drive.geoLocationDest_Id;//uuid
        this.path_Id = drive.path_Id;//uuid
        this.leaveTime = drive.leaveTime;//moment
        this.createdAt = drive.createdAt;//moment
        this.passengers = drive.passengers;//text
        this.type = drive.type;//text
        this.upcoming_Drives = drive.upcoming_Drives;//text
        this.price = drive.price;//numeric
        this.AVG_Price = drive.AVG_Price;//numeric
        this.num_seat_available = drive.num_seat_available;//numeric [1-4]
    }
    getDrive() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}

