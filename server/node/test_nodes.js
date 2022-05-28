const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./config/default.json');
const client = require('./config/DB');



const node_query = require('./config/querys/node_query');
const getRandomNode = require('./classes/randomData/node');
let Node = require('./classes/Node');
let node = new Node(getRandomNode());
console.log('node', node);



const addNode = async () => {
    try {
        let query = node_query.getNodeById(node);
        //Check if node is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('Node exist');
                } else {
                    query = node_query.addNode(node);
                    //Save node in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Node created');
                            } else {
                                console.log('Node not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const updateNode = async () => {
    try {
        let query = node_query.getNodeById(node);
        //Check if node is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    query = node_query.updateNode(node);
                    //Save node in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Node updated');
                            } else {
                                console.log('Node not updated');
                            }
                        });
                } else {
                    return console.log('Node is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getNodeById = async () => {
    try {
        let query = node_query.getNodeById(node);
        //Check if node is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('Node not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllNodes = async () => {
    try {
        let query = node_query.getAllNodes();
        //Check if node is exist
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
const deleteNodeById = async () => {
    try {
        let query = node_query.getNodeById(node);
        //Check if node is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = node_query.deleteNodeById(node);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Node deleted by Id');
                            } else {
                                console.log('Node not deleted by Id');
                            }
                        });
                } else {
                    console.log('Node not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const deleteNodeByOSMId = async () => {
    try {
        let query = node_query.getNodeById(node);
        //Check if node is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = node_query.deleteNodeByOSMId(node);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Node deleted by Id');
                            } else {
                                console.log('Node not deleted by OSM Id');
                            }
                        });
                } else {
                    console.log('Node not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const run = async () => {
    try {
        console.log('********************************************addNode******************************************** ');
        await addNode();
        setTimeout(async () => {
            console.log('********************************************updateNode******************************************** ');
            await updateNode();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getNodeById******************************************** ');
            await getNodeById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getAllNodes******************************************** ');
            await getAllNodes();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************deleteNodeById******************************************** ');
            await deleteNodeById();
        }, 8000)
        setTimeout(async () => {
            console.log('********************************************addNode******************************************** ');
            await addNode();
        }, 10000)
        setTimeout(async () => {
            console.log('********************************************deleteNodeByOSMId******************************************** ');
            await deleteNodeByOSMId();
        }, 12000)
    } catch (error) {
        console.log('error', error);
    }
}
run();