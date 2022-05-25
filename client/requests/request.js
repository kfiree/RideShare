const requests = {
    Users: {
        registration:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/adduser",
        // @route   POST api/adduser
        // @desc    add new user
        // @body    *firstName* *lastName*, *email*, *phoneNumber*, *password*, avatar, gender, university, universityImage, degree
        // @header  none
        getUser:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getuser",
        // @route   GET api/getuser
        // @desc    get user's details
        // @body     none
        // @header  *token*
        getOtherUser:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getotheruser",
        // @route   POST api/getotheruser
        // @desc    get other user details
        // @body    *email*
        // @header  *token*
        getAll:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getallusers",
        // @route   GET api/getallusers
        // @desc    get array of all users
        // @body    none
        // @header  none
        update:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/updateuser",
        // @route   PUT api/updateuser
        // @desc    update user's details
        // @body    fields to be update
        // @header  *token*
        delete:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/deleteuser",
        // @route   DELETE api/deleteuser
        // @desc    delete user
        // @body     none
        // @header  *token*
        login:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/login",
        // @route   POST api/login
        // @desc    get token from exist user
        // @body    *email*, *password*
        // @header  none

    },
    Drives: {
        add: "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/addDrive",
        // @route   POST api/addDrive
        // @desc    add new drive to user
        // @body    *date*, *time*, *from*, *to*, *numofSeats*, *price*
        // @header  *token*
        delete:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/deletedrive",
        // @route   DELETE api/deletedrive
        // @desc    delete spcesific drive
        // @body    *driveID*
        // @header  *token*
        getDrivesOffers:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getoffers",
        // @route   POST api/getoffers
        // @desc    get offers to all drive's that matchs to the request (as passenger)
        // @body    *date*, *time*, *where*, *to*
        // @header  *token*
        getPassengerOffers:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getmyOffersPassenger",
        // @route   GET api/getmyOffersPassenger
        // @desc    get all my offers as a passenger
        // @body    none
        // @header  *token*
        sendPassengerOffer:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/sendOffer",
        // @route   POST api/sendOffer
        // @desc    send offer to driver
        // @body    *driveID*, *price*
        // @header  *token*
        getDriverOffers:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/myOffers",
        // @route   GET api/myOffers
        // @desc    get all my offers as a driver
        // @body    none
        // @header  *token*
        getDriveByID:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getDrive",
        // @route   PUT api/getDrive
        // @desc    get specific drive details
        // @params  driveID
        // @header  token
        sendOfferToPassengerFromDriver:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/sendOfferDriver",
        // @route   POST api/sendOfferDriver
        // @desc    send offer to passenger
        // @body    *offerID*, *price*
        // @header  *token*
        passengerPermit:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/passengerPermit",
        // @route   PUT api/passengerPermit
        // @desc    add passenger to the drive
        // @body    *offerID*
        // @header  *token*
        getMyDrives:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getMyDrives",
        // @route   GET api/getMyDrives
        // @desc    get all user's drives
        // @body     none
        // @header  *token*
        getAll:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getalldrives",
        // @route   GET api/getalldrives
        // @desc    get array of all drives
        // @body    none
        // @header  *token*
        deleteOfferFromSuggestionsDrive:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/deleteOfferDriver",
        // @route   DELETE api/getMyDrives
        // @desc    delete offer from Suggestions as driver
        // @body     OfferID
        // @header  *token*

    },
    Notification: {
        add: "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/addNotification",
        // @route   POST api/addNotification
        // @desc    add new notification
        // @body    *title*, *text*, *textDetails*, *type*
        // @header  *token*
        getAll:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getAllNotifications",
        // @route   GET api/getAllNotifications
        // @desc    get array of all notifications
        // @body    none
        // @header  *token*
        setAsRead:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/setAsRead",
        // @route   POST api/setAsRead
        // @desc    set notification as read
        // @body    *notificationID*
        // @header  *token*
    },
    Chat: {
        sendMessage:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/sendMessage",
        // @route   POST api/sendMessage
        // @desc    add new message
        // @body    *chatID*, *Message*
        // @header  *token*
        getChat:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getChat",
        // @route   GET api/getChat
        // @desc    get chat details
        // @body    *chatID*
        // @header  *token*
        getAllMessage:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getAllMessage",
        // @route   GET api/getAllMessage
        // @desc    get array of all message
        // @body    *chatID*
        // @header  *token*
        getMyChats:
            "https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/getMyChats",
        // @route   GET api/getMyChats
        // @desc    get all user's chats
        // @body     none
        // @header  *token*
    },
};

export default requests;
