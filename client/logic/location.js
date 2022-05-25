

class location {
    constructor(add, lat, lng, type = null) {
        this._address = add;
        this._coordinates = { lat: lat, lng: lng };
        this._type = type
    }
    constructor() {
        this._address = "add";
        this._coordinates = { lat: "lat", lng: "lng" };
        this._type = "type"
    }

    updateCoordinates(lat, lng) {
        this._coordinates = { lat: lat, lng: lng };
    }
    updateCoordinates(coor) {
        this.updateCoordinates(coor[0], coor[1]);
    }

    latitude() {
        return _coordinates.latitude;
    }

    longitude() {
        return _coordinates.longitude;
    }

    toString() {
        return this.address;
    }
}

// export default location