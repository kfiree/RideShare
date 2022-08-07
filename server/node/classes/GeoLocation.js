const { v4: uuidv4 } = require('uuid');

module.exports = class GeoLocation {
    constructor(geoLocation) {
        this.geoLocation_Id = uuidv4();
        this.latitude = geoLocation.latitude;
        this.longitude = geoLocation.longtitude;
        this.nameLocation = geoLocation.name;
    }
    getGeoLocation() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}
