// const { User } = require('./classes/User');
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./config/default.json');
const client = require('./config/DB');

const user_university_query = require('./config/querys/user_university_query');


const user_query = require('./config/querys/user_query');
const getRandomUser = require('./classes/randomData/user');
let User = require('./classes/User');
let user = new User(getRandomUser());
console.log('user', user);


const university_query = require('./config/querys/university_query');
const getRandomUniversity = require('./classes/randomData/university');
let University = require('./classes/University');
let university = new University(getRandomUniversity('296755c4-518b-4b9f-804f-471a14406ab6'));
console.log('university', university);


const addUser = async () => {

    try {
        let query = user_query.getUserByEmail(user);

        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('User exist');
                } else {
                    //Encrypt password
                    const salt = await bcrypt.genSalt(10);
                    const encryptedPassword = await bcrypt.hash(user.password, salt);
                    user = {
                        ...user,
                        password: encryptedPassword
                    }
                    query = user_query.addUser(user);

                    //Save user in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('User created');
                                //Return jsonwebToken
                                const payLoad = { user: { id: user.id } };
                                jwt.sign(
                                    payLoad,
                                    config["jwtSecret"],
                                    { expiresIn: 360000 },
                                    (err, token) => {
                                        user.token = token;
                                        if (err) throw err;
                                        // console.log('user', user);
                                    }
                                );
                            } else {
                                console.log('User is not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
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
const addUsersUniversities = async () => {
    try {
        let query = user_university_query.getUsersUniversitiesByUserId(user);
        //Check if user_university is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('user university exist');
                } else {
                    query = user_university_query.addUsersUniversities(user, university);
                    //Save drive in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersUniversities created');
                            } else {
                                console.log('UsersUniversities not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const updateUsersUniversities = async () => {
    try {
        let query = user_university_query.getUsersUniversitiesByUserId(user);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    query = user_university_query.updateUsersUniversities(user, university);
                    //Save drive in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersUniversities updated');
                            } else {
                                console.log('UsersUniversities not updated');
                            }
                        });
                } else {
                    return console.log('UsersUniversities is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUsersUniversitiesByUserId = async () => {
    try {
        let query = user_university_query.getUsersUniversitiesByUserId(user);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('UsersUniversities not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUsersUniversitiesByUniversityId = async () => {
    try {
        let query = user_university_query.getUsersUniversitiesByUniversityId(university);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('UsersUniversities not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllUsersUniversities = async () => {
    try {
        let query = user_university_query.getAllUsersUniversities();
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
const deleteUsersUniversitiesByUserId = async () => {
    try {
        let query = user_university_query.getUsersUniversitiesByUserId(user);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = user_university_query.deleteUsersUniversitiesByUserId(user);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersUniversities deleted by User Id');
                            } else {
                                console.log('UsersUniversities not deleted by User Id');
                            }
                        });
                } else {
                    console.log('UsersUniversities by User_Id is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteUsersUniversitiesByUniversityId = async () => {

    try {
        let query = user_university_query.getUsersUniversitiesByUniversityId(university);
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = user_university_query.deleteUsersUniversitiesByUniversityId(university);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersUniversities deleted by University Id');
                            } else {
                                console.log('UsersUniversities not deleted by University Id');
                            }
                        });
                } else {
                    console.log('UsersUniversities by University_Id is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);

    }
}
const run = async () => {
    try {
        setTimeout(async () => {
            console.log('********************************************addUniversity******************************************** ');
            await addUniversity();
        }, 0)

        setTimeout(async () => {
            console.log('********************************************addUser******************************************** ');
            await addUser();
        }, 1000)

        setTimeout(async () => {
            console.log('********************************************addUsersUniversities******************************************** ');
            await addUsersUniversities();
        }, 3000)

        setTimeout(async () => {
            console.log('********************************************updateUsersUniversities******************************************** ');
            await updateUsersUniversities();
        }, 5000)

        setTimeout(async () => {
            console.log('********************************************getUsersUniversitiesByUniversityId******************************************** ');
            await getUsersUniversitiesByUniversityId();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************getUsersUniversitiesByUserId******************************************** ');
            await getUsersUniversitiesByUserId();
        }, 7000)

        setTimeout(async () => {
            console.log('********************************************getAllUsersUniversities******************************************** ');
            await getAllUsersUniversities();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************deleteUsersUniversitiesByUserId******************************************** ');
            await deleteUsersUniversitiesByUserId();
        }, 9000)

        setTimeout(async () => {
            console.log('********************************************addUsersUniversities******************************************** ');
            await addUsersUniversities();
        }, 11000)

        setTimeout(async () => {
            console.log('********************************************deleteUsersUniversitiesByUniversityId******************************************** ');
            await deleteUsersUniversitiesByUniversityId();
        }, 13000)

    } catch (error) {
        console.log('error', error);
    }
}

run();