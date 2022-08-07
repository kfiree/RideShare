const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');



const user_query = require('./../config/querys/user_query');
const getRandomUser = require('./../classes/randomData/user');
let User = require('./../classes/User');
let user = new User(getRandomUser());
// console.log('user', user);


const drive_query = require('./config/querys/drive_query');
const getRandomDrive = require('./classes/randomData/drive');
let Drive = require('./classes/Drive');
let drive = new Drive(getRandomDrive());
// console.log('drive', drive);


const addDrive = async () => {
    try {
        let query = drive_query.getDriveById(drive);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('Drive exist');
                } else {
                    query = drive_query.addDrive(drive);
                    //Save drive in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Drive created');
                            } else {
                                console.log('Drive not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const updateDrive = async () => {
    try {
        let query = drive_query.getDriveById(drive);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    query = drive_query.updateDrive(drive);
                    //Save drive in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Drive updated');
                            } else {
                                console.log('Drive not updated');
                            }
                        });
                } else {
                    return console.log('Drive is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getDriveById = async () => {
    try {
        let query = drive_query.getDriveById(drive);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('Drive not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllDrives = async () => {
    try {
        let query = drive_query.getAllUniversities();
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows);
                    return result.rows;
                } else {
                    console.log('Universities not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteDriveById = async () => {
    try {
        let query = drive_query.getDriveById(drive);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = drive_query.deleteDriveById(drive);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Drive deleted by Id');
                            } else {
                                console.log('Drive not deleted by Id');
                            }
                        });
                } else {
                    console.log('Drive not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteDriveByGeoLocation = async () => {

    try {
        let query = drive_query.getDriveByGeoLocationId(drive);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = drive_query.deleteDriveByGeoLocationId(drive);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Drive deleted by Id');
                            } else {
                                console.log('Drive not deleted by Id');
                            }
                        });
                } else {
                    console.log('Drive not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const run = async () => {
    try {
        console.log('********************************************addDrive******************************************** ');
        await addDrive();
        setTimeout(async () => {
            console.log('********************************************updateDrive******************************************** ');
            await updateDrive();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getDriveById******************************************** ');
            await getDriveById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getAllUniversities******************************************** ');
            await getAllDrives();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************deleteDriveById******************************************** ');
            await deleteDriveById();
        }, 8000)

    } catch (error) {
        console.log('error', error);
    }
}
run();