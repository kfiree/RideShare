import requests from "./request";
import axios from "axios";
import token from './../logicApp';

const chats = {
    addMessageToChat: async (messageData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(messageData);
            const res = await axios.post(requests.Chat.sendMessage, body, config);
            return res.data;
        } catch (err) {
            // console.error(err);
            console.error("request failed");
            return null;
        }
    },
    getChat: async (messageData, token) => {

        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(messageData);
            const res = await axios.put(requests.Chat.getChat, body, config);
        } catch (err) {
            // console.error(err);
            console.error("request failed");
        }

    },
    getAllMessage: async (messageData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(messageData);
            const res = await axios.put(requests.Chat.getAllMessage, body, config);
            return res.data;
        } catch (err) {
            console.error("request failed");
            return [];
        }

    },
    getAllChatsByUser: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const res = await axios.get(requests.Chat.getMyChats, config);
            console.log(res.data)
            return res.data;
        } catch (err) {
            // console.error(err);
            console.error("request failed");
            return [];
        }
    },
};
export default chats;
