import requests from "./request";
import axios from "axios";
import moment from "moment";
// import token from './../logicApp';


const drives = {
    addDrive: async (driveData) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    // 'x-auth-token': token,
                }
            }
            const body = JSON.stringify(driveData);
            const res = await axios.post(requests.Drives.add, body, config);
            return true;
        } catch (err) {
            console.error(err);
            console.error("request failed request");
            return false;
        }
    },
    getDriveByID: async (driveData) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    // 'x-auth-token': token
                },
            }
            const body = JSON.stringify(driveData);
            const res = await axios.put(requests.Drives.getDriveByID, body, config);
            return res.data
        } catch (err) {
            return []
        }
    },
};
export default drives;
