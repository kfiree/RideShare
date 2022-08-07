// const { User } = require('./classes/User');
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');

const getRandomUser = require('./../classes/randomData/user');
let User = require('./../classes/User');
let user = new User(getRandomUser());
console.log('user', user);



const university_query = require('./../config/querys/university_query');
const getRandomUniversity = require('./../classes/randomData/university');
let University = require('./../classes/University');

let university = new University(getRandomUniversity('296755c4-518b-4b9f-804f-471a14406ab6'));
console.log('university', university);


const addUniversity = async () => {
    try {
        let query = university_query.getUniversityById(university);
        //Check if university is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('University exist');
                } else {
                    query = university_query.addUniversity(university);
                    //Save university in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('University created');
                            } else {
                                console.log('University not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const updateUniversity = async () => {
    try {
        let query = university_query.getUniversityById(university);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {

                    query = university_query.updateUniversity(university)

                    //Save user in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('University updated');
                            } else {
                                console.log('University not updated');
                            }
                        });
                } else {
                    return console.log('University is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUniversityById = async () => {
    try {
        let query = university_query.getUniversityById(university);
        //Check if university is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('University not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUniversityByGeoLocation = async () => {

    try {
        let query = university_query.getUniversityByGeoLocationId(university);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('University not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllUniversities = async () => {
    try {
        let query = university_query.getAllUniversities();
        //Check if user is exist
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
const deleteUniversityById = async () => {
    try {
        let query = university_query.getUniversityById(university);
        //Check if university is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = university_query.deleteUniversityById(university);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('University deleted by Id');
                            } else {
                                console.log('University not deleted by Id');
                            }
                        });
                } else {
                    console.log('University not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteUniversityByGeoLocation = async () => {

    try {
        let query = university_query.getUniversityByGeoLocationId(university);
        //Check if university is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = university_query.deleteUniversityByGeoLocationId(university);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('University deleted by Id');
                            } else {
                                console.log('University not deleted by Id');
                            }
                        });
                } else {
                    console.log('University not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const run = async () => {
    try {
        console.log('********************************************addUniversity******************************************** ');
        await addUniversity();
        setTimeout(async () => {
            console.log('********************************************updateUniversity******************************************** ');
            await updateUniversity();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getUniversityById******************************************** ');
            await getUniversityById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getUniversityByGeoLocation******************************************** ');
            await getUniversityByGeoLocation();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************getAllUniversities******************************************** ');
            await getAllUniversities();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************deleteUniversityById******************************************** ');
            await deleteUniversityById();
        }, 10000)
        setTimeout(async () => {
            console.log('********************************************addUniversity******************************************** ');
            await addUniversity();
        }, 16000)
        setTimeout(async () => {
            console.log('********************************************deleteUniversityByGeoLocation******************************************** ');
            await deleteUniversityByGeoLocation();
        }, 18000)

    } catch (error) {
        console.log('error', error);
    }
}

run();