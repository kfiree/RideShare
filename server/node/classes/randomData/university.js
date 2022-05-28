const { v4: uuidv4 } = require('uuid');

module.exports = getRandomUniversity = (geoLocation_Id) => {
    return {
        university_Id: uuidv4(),
        geoLocation_Id: geoLocation_Id,
        university_name: 'university_name',
    }
}