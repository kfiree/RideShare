const path = `http://localhost:3001/`;
const requests = {
    Users: {
        registration: `${path}api/users`,
        getUser: `${path}/api/getuser`,
        getOtherUser: `${path}/api/getotheruser`,
        getAll: `${path}/api/getallusers`,
        update: `${path}/api/updateuser`,
        delete: `${path}/api/deleteuser`,
        login: `${path}api/users/login`,
    },
    Drives: {
        add: `${path}/api/addDrive`,
        delete: `${path}/api/deletedrive`,
        getDrivesOffers: `${path}/api/getoffers`,
        getPassengerOffers: `${path}/api/getmyOffersPassenger`,
        sendPassengerOffer: `${path}/api/sendOffer`,
        getDriverOffers: `${path}/api/myOffers`,
        getDriveByID: `${path}/api/getDrive`,
        sendOfferToPassengerFromDriver: `${path}/api/sendOfferDriver`,
        passengerPermit: `${path}/api/passengerPermit`,
        getMyDrives: `${path}/api/getMyDrives`,
        getAll: `${path}/api/getalldrives`,
        deleteOfferFromSuggestionsDrive: `${path}/api/deleteOfferDriver`,
    },
    Notification: {
        add: `${path}/api/addNotification`,
        getAll: `${path}/api/getAllNotifications`,
        setAsRead: `${path}/api/setAsRead`,
    },
    Chat: {
        sendMessage: `${path}/api/sendMessage`,
        getChat: `${path}/api/getChat`,
        getAllMessage: `${path}/api/getAllMessage`,
        getMyChats: `${path}/api/getMyChats`,
    },
};

export default requests;
