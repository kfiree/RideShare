// const { User } = require('./classes/User');
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('../config/default.json');
const client = require('../config/DB');




const user_query = require('../config/querys/user_query');
const getRandomUser = require('../classes/randomData/user');
let User = require('../classes/User');
let user = new User(getRandomUser());
// console.log('user', user);
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
const updateUser = async () => {
    try {
        let query = user_query.getUserByEmail(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    //Encrypt password
                    const salt = await bcrypt.genSalt(10);
                    const encryptedPassword = await bcrypt.hash(user.password, salt);
                    user = {
                        ...user,
                        password: encryptedPassword
                    }
                    query = user_query.updateUser(user)

                    //Save user in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('User updated');
                            } else {
                                console.log('user not deleted by Id');
                            }
                        });
                } else {
                    return console.log('User is not exist updateUser()');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUserById = async () => {
    try {
        let query = user_query.getUserById(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('User not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUserByEmail = async () => {

    try {
        let query = user_query.getUserByEmail(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('User not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllUsers = async () => {
    try {
        let query = user_query.getAllUsers();
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows);
                    return result.rows;
                } else {
                    console.log('User not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteUserById = async () => {
    try {
        let query = user_query.getUserById(user);

        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    // console.log('result.rows', result.rows[0]);
                    await client.query(user_query.deleteUserById(user),
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('user deleted by Id');
                            } else {
                                console.log('user not deleted by Id');
                            }
                        });
                } else {
                    console.log('User not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteUserByEmail = async () => {

    try {
        let query = user_query.getUserByEmail(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    // console.log('result.rows', result.rows[0]);
                    await client.query(user_query.deleteUserByEmail(user),
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('user deleted by Email');
                            } else {
                                console.log('user not deleted by Email');
                            }
                        });
                } else {
                    console.log('User not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const run = async () => {
    try {
        console.log('********************************************addUser******************************************** ');
        await addUser();
        setTimeout(async () => {
            console.log('********************************************updateUser******************************************** ');
            await updateUser();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getUserByEmail******************************************** ');
            await getUserByEmail();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getUserById******************************************** ');
            await getUserById();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************getAllUsers******************************************** ');
            await getAllUsers();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************deleteUserById******************************************** ');
            await deleteUserById();
        }, 10000)
        setTimeout(async () => {
            console.log('********************************************addUser******************************************** ');
            await addUser();
        }, 16000)
        setTimeout(async () => {
            console.log('********************************************deleteUserByEmail******************************************** ');
            await deleteUserByEmail();
        }, 18000)

    } catch (error) {
        console.log('error', error);
    }
}

run();











