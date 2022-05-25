import requests from "./request";
import axios from "axios";
import moment from "moment";
import token from './../logicApp';


const drives = {
    addDrive: async (driveData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(driveData);
            const res = await axios.post(requests.Drives.add, body, config);
            return true;
        } catch (err) {
            // console.error(err);
            console.error("request failed request");
            return false;
        }
    },
    /** Does not work */
    deleteDrive: async (driveData, token) => {
        // const DriveData = {
        //     driveID: "4561651651",
        // }

        try {
            const config = {
                headers: {
                    // 'Content-Type': 'application/json',
                    'x-auth-token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiZzlVTGRrdHd2d3R4ejF4VGRMaEsifSwiaWF0IjoxNjM5MzMxMzcwLCJleHAiOjE2MzkzMzg1NzB9.PyPZtf9Lp3iV49fRKzdKSVDEp-8hLHmKUvXLqkJilP8'
                }
            }
            //console.log("headers = ", config.headers);
            //console.log("driveData = ", JSON.stringify(driveData));
            const body = JSON.stringify(driveData);
            const res = await axios.delete(requests.Drives.delete, body, config);
            //console.log("deleted drive")
        } catch (err) {
            console.error(err.response.data);
            console.error("request failed");
        }
    },
    /** Does not work */
    getAllDrives: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const res = await axios.get(requests.Drives.getAll, config);
        } catch (err) {
            // console.error(err);
            console.error("request failed request");
        }
    },
    searchDriveAsPassenger: async (searchData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            const body = JSON.stringify(searchData);
            const res = await axios.post(requests.Drives.getDrivesOffers, body, config);
            return res.data;
        } catch (err) {
            // console.error(err.response.data);
            console.error("request failed");
            return [];
        }
    },
    getPassengerOffers: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            // console.log("header : ", config.headers)
            const res = await axios.get(requests.Drives.getPassengerOffers, config);
            // console.log(res.data);
            return res.data
        } catch (err) {
            // console.error(err);
            console.error("request failed");
        }
    },
    sendOfferToPassengerFromDriver: async (offer, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }
            const body = JSON.stringify(offer);
            const res = await axios.post(requests.Drives.sendOfferToPassengerFromDriver, body, config);
            return true;
        } catch (err) {
            console.error("request failed");
            return false;
        }
    },
    getDriveByID: async (driveData, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }
            const body = JSON.stringify(driveData);
            const res = await axios.put(requests.Drives.getDriveByID, body, config);
            return res.data
        } catch (err) {
            return []
        }
    },
    sendOfferToDriver: async (offer, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                },
            }
            const body = JSON.stringify(offer);
            const res = await axios.post(requests.Drives.sendPassengerOffer, body, config);
            return true;
        } catch (err) {
            // console.error(err);
            console.error("request failed");
            return false;
        }

    },

    getDriverOffers: async (token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                },
            }
            const res = await axios.get(requests.Drives.getDriverOffers, config);
            return res.data;
        } catch (err) {
            // console.error(err.response.data);
            console.error("request failed");
            return [];
        }

    },
    confirmPassengersToDrive: async (offer, token) => {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token
                },
            }

            const body = JSON.stringify(offer);
            const res = await axios.put(requests.Drives.passengerPermit, body, config);
            return true;
        } catch (err) {
            // console.error(err.response.data);
            console.log("request failed");
            return false;
        }
    },
    getUserDrives: async (token) => {
        try {
            const config = {
                headers: {
                    'x-auth-token': token,
                }
            }
            const res = await axios.get(requests.Drives.getMyDrives, config);
            return res.data;
        } catch (err) {
            console.error(err.response.data);
            console.error("request failed");
            return [];
        }
    },
    deleteOfferFromSuggestionsDrive: async (offer, token) => {
        try {
            const config = {
                headers: {
                    'x-auth-token': token
                }
            }
            const body = JSON.stringify(offer);
            const res = await axios.delete(requests.Drives.deleteOfferFromSuggestionsDrive, body, config);
            return true;
        } catch (err) {
            console.error("request failed");
            return false;
        }
    },
};
export default drives;
