const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');


module.exports = getRandomDrive = () => {
    return {
        drive_Id: uuidv4(),//uuid
        geoLocationSrc_Id: 'bac67885-cf5e-45b3-ba98-7d6dc36f26f1',//uuid
        geoLocationDest_Id: 'bac67885-cf5e-45b3-ba98-7d6dc36f26f1',//uuid
        path_Id: '6493448f-e599-4b43-8951-5bc5809e2bfa',//uuid
        leaveTime: moment().format(),//moment
        createdAt: moment().format(),//moment
        passengers: 'passengers',//text
        type: 'type',//text
        upcoming_Drives: 'upcoming_Drives',//text
        price: 10,//numeric
        AVG_Price: 10,//numeric
        num_seat_available: 4,//numeric -> [1-4]
    }
}