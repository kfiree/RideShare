// const { User } = require('./classes/User');
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');

const user_drive_query = require('./../config/querys/user_drive_query');


const user_query = require('./../config/querys/user_query');
const getRandomUser = require('./../classes/randomData/user');
let User = require('./../classes/User');
let user = new User(getRandomUser());
console.log('user', user);


const drive_query = require('./../config/querys/drive_query');
const getRandomDrive = require('./../classes/randomData/drive');
let Drive = require('./../classes/Drive');
let drive = new Drive(getRandomDrive());
console.log('drive', drive);



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
const addUsersDrives = async () => {
    try {
        let query = user_query.getUserById(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                else if (result.rows.length > 0) {
                    query = drive_query.getDriveById(drive);
                    //Check if drive is exist
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rows.length > 0) {

                                query = user_drive_query.addUsersDrives(user, drive);
                                //Save drive in DB
                                await client.query(query,
                                    async (err, result, fields) => {
                                        if (err) console.log('err', err);
                                        else if (result.rowCount === 1) {
                                            console.log('UsersDrives created');
                                        } else {
                                            console.log('UsersDrives not created');
                                        }
                                    });
                            } else {
                                console.log('Drive is not exist');
                            }
                        })
                } else {
                    console.log('User is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const updateUsersDrivesByDriveId = async () => {
    try {
        let query = user_query.getUserById(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                else if (result.rows.length > 0) {
                    query = drive_query.getDriveById(drive);
                    //Check if drive is exist
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rows.length > 0) {
                                query = user_drive_query.updateUsersDrivesByDriveId(user, drive);
                                //Save drive in DB
                                await client.query(query,
                                    async (err, result, fields) => {
                                        if (err) console.log('err', err);
                                        else if (result.rowCount === 1) {
                                            console.log('updateUsersDrives By Drive_Id updated');
                                        } else {
                                            console.log('updateUsersDrives By Drive_Id is not updated');
                                        }
                                    });
                            } else {
                                console.log('Drive is not exist');
                            }
                        })
                } else {
                    console.log('User is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const updateUsersDrivesByUserId = async () => {
    try {
        let query = user_query.getUserById(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                else if (result.rows.length > 0) {
                    query = drive_query.getDriveById(drive);
                    //Check if drive is exist
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rows.length > 0) {
                                query = user_drive_query.updateUsersDrivesByUserId(user, drive);
                                //Save drive in DB
                                await client.query(query,
                                    async (err, result, fields) => {
                                        if (err) console.log('err', err);
                                        else if (result.rowCount === 1) {
                                            console.log('UsersDrives By User_Id updated');
                                        } else {
                                            console.log('UsersDrives By User_Id is not updated');
                                        }
                                    });
                            } else {
                                console.log('Drive is not exist');
                            }
                        })
                } else {
                    console.log('User is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUsersDrivesByUserId = async () => {
    try {
        query = user_drive_query.getUsersDrivesByUserId(user);
        //get UsersDrives in DB
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                else if (result.rows.length > 0) {
                    console.log(result.rows);
                    return result.rows[0];
                } else {
                    console.log('UsersDrives not exist for the user');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getUsersDrivesByDriveId = async () => {
    try {
        let query = user_drive_query.getUsersDrivesByDriveId(drive);
        //get users_drives by Drive_Id
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('UsersDrives for drive_Id is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllUsersDrives = async () => {
    try {
        let query = user_drive_query.getAllUsersDrives();
        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows);
                    return result.rows;
                } else {
                    console.log('UsersDrives not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const deleteUsersDrivesByUserId = async () => {
    try {
        let query = user_query.getUserById(user);
        //Check if user is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = user_drive_query.deleteUsersDrivesByUserId(user);
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersDrives deleted by User_Id');
                            } else {
                                console.log('UsersDrives not deleted by User_Id');
                            }
                        });
                } else {
                    console.log('User by User_Id is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const deleteUsersDrivesDriveId = async () => {
    try {
        let query = drive_query.getDriveById(drive);

        //Check if drive is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = user_drive_query.deleteUsersDrivesDriveId(drive);
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('UsersDrives deleted by Drive_Id');
                            } else {
                                console.log('UsersDrives is not deleted by Drive_Id');
                            }
                        });
                } else {
                    console.log('Drive by Drive_Id is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);

    }
}
const run = async () => {
    try {
        setTimeout(async () => {
            console.log('********************************************addUser******************************************** ');
            await addUser();
        }, 500)

        setTimeout(async () => {
            console.log('********************************************addDrive******************************************** ');
            await addDrive();
        }, 1000)

        setTimeout(async () => {
            console.log('********************************************addUsersDrives******************************************** ');
            await addUsersDrives();
        }, 2000)

        setTimeout(async () => {
            console.log('********************************************updateUsersDrivesByDriveId******************************************** ');
            await updateUsersDrivesByDriveId();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************updateUsersDrivesByUserId******************************************** ');
            await updateUsersDrivesByUserId();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************getUsersDrivesByUserId******************************************** ');
            await getUsersDrivesByUserId();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************getUsersDrivesByDriveId******************************************** ');
            await getUsersDrivesByDriveId();
        }, 10000)

        setTimeout(async () => {
            console.log('********************************************getAllUsersDrives******************************************** ');
            await getAllUsersDrives();
        }, 12000)

        setTimeout(async () => {
            console.log('********************************************deleteUsersDrivesDriveId******************************************** ');
            await deleteUsersDrivesDriveId();
        }, 14000)

        setTimeout(async () => {
            console.log('********************************************addUsersDrives******************************************** ');
            await addUsersDrives();
        }, 16000)

        setTimeout(async () => {
            console.log('********************************************deleteUsersDrivesByUserId******************************************** ');
            await deleteUsersDrivesByUserId();
        }, 18000)

    } catch (error) {
        console.log('error', error);
    }
}

run();