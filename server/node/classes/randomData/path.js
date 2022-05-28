const { v4: uuidv4 } = require('uuid');

module.exports = getRandomPath = () => {
    return {
        path_Id: uuidv4(),//uuid
        edges: JSON.stringify({ key: 'val' }),//json
    }
}