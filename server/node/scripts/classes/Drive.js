const { v4: uuidv4 } = require('uuid');
module.exports = class Drive {
    constructor(drive) {
        this.drive_Id = uuidv4();//uuid
        this.driver_Id = drive.driver_id;//uuid
        this.geoLocationSrc_Id = drive.src;//uuid
        this.geoLocationDest_Id = drive.dest;//uuid
        this.path_Id = '55b480e0-b7a3-4541-b33e-04da437c1ee6';//uuid
        this.leaveTime = drive.leaveTime;//moment
        this.passengers = 'passengers';//text
        this.type = drive.type || 'Passenger';//text
        this.upcoming_Drives = 'upcoming_Drives';//text
        this.price = drive.price;//numeric
        this.AVG_Price = drive.price;//numeric
        this.num_seat_available = drive.seats;//numeric [1-4]
    }
    getDrive() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}

