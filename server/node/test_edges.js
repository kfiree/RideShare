const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const config = require('./../config/default.json');
const client = require('./../config/DB');



const edge_query = require('./../config/querys/edge_query');
const getRandomEdge = require('./../classes/randomData/edge');
let Edge = require('./../classes/Edge');
let edge = new Edge(getRandomEdge());
console.log('edge', edge);



const addEdge = async () => {
    try {
        let query = edge_query.getEdgeById(edge);
        //Check if edge is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log('Edge exist');
                } else {
                    query = edge_query.addEdge(edge);
                    //Save edge in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Edge created');
                            } else {
                                console.log('Edge not created');
                            }
                        });
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const updateEdge = async () => {
    try {
        let query = edge_query.getEdgeById(edge);
        //Check if edge is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                // console.log('result', result.rows);
                if (result.rows.length > 0) {
                    query = edge_query.updateEdge(edge);
                    //Save edge in DB
                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Edge updated');
                            } else {
                                console.log('Edge not updated');
                            }
                        });
                } else {
                    return console.log('Edge is not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getEdgeById = async () => {
    try {
        let query = edge_query.getEdgeById(edge);
        //Check if edge is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    console.log(result.rows[0]);
                    return result.rows[0];
                } else {
                    console.log('Edge not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const getAllEdges = async () => {
    try {
        let query = edge_query.getAllEdges();
        //Check if edge is exist
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
const deleteEdgeById = async () => {
    try {
        let query = edge_query.getEdgeById(edge);
        //Check if edge is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = edge_query.deleteEdgeById(edge);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Edge deleted by Id');
                            } else {
                                console.log('Edge not deleted by Id');
                            }
                        });
                } else {
                    console.log('Edge not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}

const deleteEdgeByOSMId = async () => {
    try {
        let query = edge_query.getEdgeById(edge);
        //Check if edge is exist
        await client.query(query,
            async (err, result, fields) => {
                if (err) console.log('err', err);
                if (result.rows.length > 0) {
                    query = edge_query.deleteEdgeByOSMId(edge);

                    await client.query(query,
                        async (err, result, fields) => {
                            if (err) console.log('err', err);
                            else if (result.rowCount === 1) {
                                console.log('Edge deleted by Id');
                            } else {
                                console.log('Edge not deleted by OSM Id');
                            }
                        });
                } else {
                    console.log('Edge not exist');
                }
            });
    } catch (err) {
        console.error(err.message);
    }
}
const run = async () => {
    try {
        console.log('********************************************addEdge******************************************** ');
        await addEdge();
        setTimeout(async () => {
            console.log('********************************************updateEdge******************************************** ');
            await updateEdge();
        }, 2000)
        setTimeout(async () => {
            console.log('********************************************getEdgeById******************************************** ');
            await getEdgeById();
        }, 4000)

        setTimeout(async () => {
            console.log('********************************************getAllEdges******************************************** ');
            await getAllEdges();
        }, 6000)

        setTimeout(async () => {
            console.log('********************************************deleteEdgeById******************************************** ');
            await deleteEdgeById();
        }, 8000)

        setTimeout(async () => {
            console.log('********************************************getAllEdges******************************************** ');
            await getAllEdges();
        }, 10000)
    } catch (error) {
        console.log('error', error);
    }
}
run();