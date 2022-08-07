const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');

const geoLocation_query = require('./../config/querys/geoLocation_query');
const getRandomGeoLocation = require('./../classes/randomData//geoLocation');
let GeoLocation = require('./../classes/GeoLocation');

// Read Synchrously
var fs = require("fs");
var content = fs.readFileSync("locations.json");
// console.log('content', JSON.parse(content));
const locations = JSON.parse(content);
const arr = {
    arr: []
};



const addGeoLocation = async () => {
    try {
        locations.forEach(async (location) => {
            // console.log('location', location);
            let geoLocation = new GeoLocation(location);
            arr.arr.push(geoLocation.geoLocation_Id);
            query = geoLocation_query.addGeoLocation(geoLocation);
            //Save geoLocation in DB
            await client.query(query,
                async (err, result, fields) => {
                    if (err) console.log('err', err);
                    else if (result.rowCount !== 1) {
                        console.log('GeoLocation not created');
                    }
                });
        });
    } catch (err) {
        console.error(err.message);
    }
}

const run = async () => {
    try {

        await addGeoLocation();
        console.log('GeoLocation created');
        fs.writeFile('drivesLocations.json', JSON.stringify(arr), (err) => {
            if (err) throw err;
            console.log('File has been created');
        });

    } catch (error) {
        console.log('error', error);
    }
}
run();