const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');



const path_query = require('./../config/querys/path_query');
const getRandomPath = require('./../classes/randomData/path');
let Path = require('./../classes/Path');
let path = new Path(getRandomPath());
console.log('path', path);



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
const updatePath = async () => {
    try {
        let query = path_query.getPathById(path);
        //Check if path is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    query = path_query.updatePath(path);
                    //Save path in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Path updated');
                            } else {
                                console.log('Path not updated');
                            }
                        });
                } else {
                    return console.log('Path is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getPathById = async () => {
    try {
        let query = path_query.getPathById(path);
        //Check if path is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('Path not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllPaths = async () => {
    try {
        let query = path_query.getAllPaths();
        //Check if path is exist
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
const deletePathById = async () => {
    try {
        let query = path_query.getPathById(path);
        //Check if path is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = path_query.deletePathById(path);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Path deleted by Id');
                            } else {
                                console.log('Path not deleted by Id');
                            }
                        });
                } else {
                    console.log('Path not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const deletePathByOSMId = async () => {
    try {
        let query = path_query.getPathById(path);
        //Check if path is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = path_query.deletePathByOSMId(path);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Path deleted by Id');
                            } else {
                                console.log('Path not deleted by OSM Id');
                            }
                        });
                } else {
                    console.log('Path not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const run = async () => {
    try {
        console.log('********************************************addPath******************************************** ');
        await addPath();
        setTimeout(async () => {
            console.log('********************************************updatePath******************************************** ');
            await updatePath();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getPathById******************************************** ');
            await getPathById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getAllPaths******************************************** ');
            await getAllPaths();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************deletePathById******************************************** ');
            await deletePathById();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************addPath******************************************** ');
            await addPath();
        }, 10000)
    } catch (error) {
        console.log('error', error);
    }
}
run();