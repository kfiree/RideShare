
const path = `http://localhost:5002/api/`;
const users = `users`;
const drives = `drives`;
const universities = `universities`;
const geoLocation = `geoLocation`;

const requests = {
    Users: {
        registration: path + users,
        login: path + users,
    },
    Drives: {
        add: path + drives,
        delete: path + drives,
    },
    Universities: {
        get: path + universities,
        delete: path + universities,
    },
    GeoLocation: {
        get: path + geoLocation,
        delete: path + geoLocation,
    },
};

export default requests;
