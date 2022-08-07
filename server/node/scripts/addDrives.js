// const { User } = require('./classes/User');
var fs = require("fs");
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const config = require('./../config/default.json');
const client = require('./../config/DB');

const drive_query = require('./../config/querys/drive_query');
const user_drive_query = require('./../config/querys/user_drive_query');
const path_query = require('./../config/querys/path_query');

const getRandomPath = require('./../classes/randomData/path');
let Path = require('./../classes/Path');
let Drive = require('./../classes/Drive');

//create new path
let path = new Path(getRandomPath());
const addPath = async () => {
    try {
        let query = path_query.getPathById(path);
        //Check if path is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('Path exist');
                } else {
                    query = path_query.addPath(path);
                    //Save path in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Path created');
                            } else {
                                console.log('Path not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
addPath();

//create new user
var createdUsers = fs.readFileSync("createdUsers.json");
let users = JSON.parse(createdUsers).arr;

//create new geoLocation
var addLocations = fs.readFileSync("drivesLocations.json");
let locations = JSON.parse(addLocations).arr;

const arr = {
    arr: []
};

// Read Synchrously
var content = fs.readFileSync("drives.json");
const drives = JSON.parse(content);

const addDrives = async () => {
    for (let i = 0; i < drives.length; i++) {
        let drive = drives[i];
        let query = '';
        try {
            //create new drive
            drive = new Drive(drive);
            const userId = users[drives[i].driver_id];
            const startNode = locations[drive.geoLocationSrc_Id];
            drive.geoLocationSrc_Id = startNode;
            const endNode = locations[drive.geoLocationDest_Id];
            drive.geoLocationDest_Id = endNode;
            query = drive_query.addDrive(drive);
            //Save user_drive
            query += ' ' + user_drive_query.addUsersDrives({ user_Id: userId }, drive);
            // console.log(query);
            await client.query(query,
                async (err, result, fields) => {
                    if (err) console.log('err', err);
                    else if (result.length === 2 && result[0].rowCount === 1 && result[1].rowCount === 1) {
                        console.log('Drive created');
                    } else {
                        console.log('UsersDrives not created');
                    }
                });
        } catch (err) {
            console.error(err.message);
        }

    }
}
const run = async () => {
    try {
        await addDrives();
    } catch (error) {
        console.log('error', error);
    }
}



const addUsersDrives = async (query) => {
    try {
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                else if (result.rowCount !== 1) {
                    console.log('UsersDrives not created');
                } else {
                    console.log('UsersDrives created');
                }
            });
    } catch (error) {
        console.log('error', error);
    }
}
run();











