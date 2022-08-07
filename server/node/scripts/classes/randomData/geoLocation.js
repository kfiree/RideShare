const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');

module.exports = getRandomGeoLocation = () => {
    return {
        geoLocation_Id: uuidv4(),
        latitude: 25.555555,
        longitude: 25.555555,
        nameLocation: `nameLocation`,
    }
}