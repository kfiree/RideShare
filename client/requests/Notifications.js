import requests from "./request";
import axios from "axios";
import token from './../logicApp';

const notifications = {
    addNotifications: async (notificationData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }
            const body = JSON.stringify(notificationData);
            const res = await axios.post(requests.Notification.add, body, config);
        } catch (err) {
            // console.error(err);
            console.error("request failed");
        }
    },
    getAllNotifications: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }
            const res = await axios.get(requests.Notification.getAll, config);
            return res.data;
        } catch (err) {
            // console.error(err);
            console.error("request failed");
            return [];
        }
    },
    readMSG: async (notificationData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }
            const body = JSON.stringify(notificationData);
            const res = await axios.post(requests.Notification.setAsRead, body, config);
        } catch (err) {
            // console.error(err);
            console.error("request failed");
        }
    },
};
export default notifications;
