import requests from "./request";
import axios from "axios";
import token from './../logicApp';


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
            await token.addTokenToStorage(res.data.token)

            return true;
        } catch (err) {
            console.error(err.response.data);
            console.error("request failed");
            return false;
        }
    },
    getUser: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const res = await axios.get(requests.Users.getUser, config);
            return res.data;
        } catch (err) {
            console.error(err);
            console.error("request failed");
            return null;
        }
    },
    getUserByEmail: async (userData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(userData);
            const res = await axios.post(requests.Users.getOtherUser, body, config);
        } catch (err) {
            // console.error(err.response.data);
            console.error("request failed");
        }
    },
    getAllUsers: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const res = await axios.get(requests.Users.getAll, config);
        } catch (err) {
            // console.error(err.response.data);
            console.error("request failed");
        }
    },
    updateUser: async (userData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }

            const body = JSON.stringify(userData);
            const res = await axios.put(requests.Users.update, body, config);
            console.log("res.data = " + JSON.stringify(res.data));
            return res.data;
        } catch (err) {
            console.error(err.response.data);
            console.error("request failed");
            return null;
        }
    },
    deleteUser: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const res = await axios.delete(requests.Users.delete, config);
        } catch (err) {
            // console.error(err);
            console.error("request failed");
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
            await token.addTokenToStorage(res.data["token"])
            return res.data["token"];
        } catch (err) {
            console.error(err);
            console.error("request failed");
            return "";
        }
    },
};
export default users;
