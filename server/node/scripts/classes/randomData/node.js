const { v4: uuidv4 } = require('uuid');

module.exports = getRandomUniversity = (geoLocation_Id) => {
    return {
        osm_Id: parseInt(Math.random() * 100000000),//int8 -> bigint
        node_Id: uuidv4(),//uuid
        latitude: 25.55555,//numeric
        longitude: 25.55555,//numeric
        degree: 100,//numeric
        edges: JSON.stringify({ key: 'val' }),//json
        tags: JSON.stringify({ key: 'val' }),//json
    }
}