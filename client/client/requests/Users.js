import requests from "./request";
import axios from "axios";
// import token from './../logicApp';


const users = {
    registration: async (userData) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            const body = JSON.stringify(userData);
            const res = await axios.post(requests.Users.registration, body, config);
            // await token.addTokenToStorage(res.data.token)

            return true;
        } catch (err) {
            console.error(err.response.data);
            console.error("request failed");
            return false;
        }
    },
    login: async (userData) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
            const body = JSON.stringify(userData);
            const res = await axios.post(requests.Users.login, body, config);
            // await token.addTokenToStorage(res.data["token"])
            return res.data["token"];
        } catch (err) {
            console.error(err);
            return "request failed";
        }
    },
};
export default users;
