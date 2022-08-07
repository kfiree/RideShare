const { v4: uuidv4 } = require('uuid');

module.exports = getRandomEdge = () => {
    return {
        edge_Id: uuidv4(),
        startNodeId: '77e72f74-9686-4cba-913c-3a4f9eaffd7f',
        endNodeId: '77e72f74-9686-4cba-913c-3a4f9eaffd7f',
        distance: 10,
        weight: 10,
        name: 'name',
        highwayType: 'highwayType',
    }
}