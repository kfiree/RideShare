const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./config/default.json');
const client = require('./config/DB');



const geoLocation_query = require('./../config/querys/geoLocation_query');
const getRandomGeoLocation = require('./../classes/randomData//geoLocation');
let GeoLocation = require('./../classes/Geo_Location');
let geoLocation = new GeoLocation(getRandomGeoLocation());
console.log('geoLocation', geoLocation);



const addGeoLocation = async () => {
    try {
        let query = geoLocation_query.getGeoLocationById(geoLocation);
        //Check if geoLocation is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('GeoLocation exist');
                } else {
                    query = geoLocation_query.addGeoLocation(geoLocation);
                    //Save geoLocation in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('GeoLocation created');
                            } else {
                                console.log('GeoLocation not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const updateGeoLocation = async () => {
    try {
        let query = geoLocation_query.getGeoLocationById(geoLocation);
        //Check if geoLocation is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = geoLocation_query.updateGeoLocation(geoLocation);
                    //Save geoLocation in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('GeoLocation updated');
                            } else {
                                console.log('GeoLocation not updated');
                            }
                        });
                } else {
                    return console.log('GeoLocation is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getGeoLocationById = async () => {
    try {
        let query = geoLocation_query.getGeoLocationById(geoLocation);
        //Check if geoLocation is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('GeoLocation not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllGeoLocations = async () => {
    try {
        let query = geoLocation_query.getAllGeoLocations();
        //Check if geoLocation is exist
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
const deleteGeoLocationById = async () => {
    try {
        let query = geoLocation_query.getGeoLocationById(geoLocation);
        //Check if geoLocation is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = geoLocation_query.deleteGeoLocationById(geoLocation);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('GeoLocation deleted by Id');
                            } else {
                                console.log('GeoLocation not deleted by Id');
                            }
                        });
                } else {
                    console.log('GeoLocation not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteGeoLocationByGeoLocation = async () => {

    try {
        let query = geoLocation_query.getGeoLocationByGeoLocationId(geoLocation);
        //Check if geoLocation is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = geoLocation_query.deleteGeoLocationByGeoLocationId(geoLocation);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('GeoLocation deleted by Id');
                            } else {
                                console.log('GeoLocation not deleted by Id');
                            }
                        });
                } else {
                    console.log('GeoLocation not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const run = async () => {
    try {
        console.log('********************************************addGeoLocation******************************************** ');
        await addGeoLocation();
        setTimeout(async () => {
            console.log('********************************************updateGeoLocation******************************************** ');
            await updateGeoLocation();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getGeoLocationById******************************************** ');
            await getGeoLocationById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getAllUniversities******************************************** ');
            await getAllGeoLocations();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************deleteGeoLocationById******************************************** ');
            await deleteGeoLocationById();
        }, 8000)

    } catch (error) {
        console.log('error', error);
    }
}
run();