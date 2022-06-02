module.exports = class GeoLocation {
    constructor(geoLocation) {
        this.geoLocation_Id = geoLocation.geoLocation_Id;
        this.latitude = geoLocation.latitude;
        this.longitude = geoLocation.longitude;
        this.nameLocation = geoLocation.nameLocation;
    }
    getGeoLocation() {
        return this;
    }
    toString() {
        return JSON.stringify(this);
    }
}
