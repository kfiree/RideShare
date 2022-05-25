import React from 'react';
import axios from "axios";

import {
    SafeAreaView,
    TouchableWithoutFeedback,
    TouchableOpacity,
    TouchableHighlight,
    TouchableNativeFeedback,
    StyleSheet,
    Text,
    View,
    Image,
    ScrollView,
    ImageBackground,
    Dimensions,
    Button,
    Alert,
    Platform,
    StatusBar,
} from 'react-native';
import { useFonts } from 'expo-font';
import users from '../../../../../requests/Users';
import drives from '../../../../../requests/Drives';
import notifications from '../../../../../requests/Notifications';
import chats from '../../../../../requests/Chats';



function CheckRequests() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });

    const onSubmitChats = async () => {
        //console.log("onSubmit = ")
        const tokenPassenger1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNUJ5djZaTDdmRTJNQmFEWEMzT3MifSwiaWF0IjoxNjM5NDEzMjQ0LCJleHAiOjE2Mzk0MjA0NDR9.ByisZAaYC4ctYGZzWP0--ddqPWyy1SFWi9o4WfTaUIA";
        const tokenPassenger2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiSzlOcVZQQXJOcDZUdlJLTGp3WDYifSwiaWF0IjoxNjM5NDEzMjg3LCJleHAiOjE2Mzk0MjA0ODd9.F9HjI6pjH_TWFNs3eIshZt51vhZ38cdgS9SYqUERyYY";
        const tokenPassenger3 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRjJDREVGajN6Umw5ZEphQ0VEaVEifSwiaWF0IjoxNjM5NDEzMzAyLCJleHAiOjE2Mzk0MjA1MDJ9.ifaqqSQpNtHllPRRNK-ANI54jGExX_l-fUjj20WvRLE";
        const tokenPassenger4 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiN3AyU2JwY3l6R3JMejVPMGh1OHcifSwiaWF0IjoxNjM5NDEzMzE2LCJleHAiOjE2Mzk0MjA1MTZ9.JejN97hYt_mXaPVQX9WJQbGyP7YdeFG4HLGVMKUl6h0";
        const driver = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiS0xUNDZZaTVjTkE4c2N4dE04c24ifSwiaWF0IjoxNjM5NTAyNDMxLCJleHAiOjE2Mzk1MDk2MzF9.FvYxSh5LYyagl-kAEYoHrOCU0o0lykxIg_d6VfK-iks";


        // try {
        //     const offer = {
        //         offerID: "IjtcBxVFmBD370gN3GCv"
        //     }

        //     const config = {
        //         headers: {
        //             'x-auth-token': "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiUUxUVDFLNlRzRVJPY2s5WHdVak8ifSwiaWF0IjoxNjM5NTE1MDc2LCJleHAiOjE2Mzk1MjIyNzZ9.VOxmx_UNoA9dGzlkKZGFGf3To4MxF8eDfwA3pdKq3qw"
        //         }
        //     }
        //     //console.log("header : ", config.headers)
        //     //console.log("offer : ", JSON.stringify(offer))
        //     const body = JSON.stringify(offer);
        //     const res = await axios.delete("https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/deleteOfferDriver", body, config);
        //     //console.log(res.data);
        //     return true;
        // } catch (err) {
        //     console.error(err);
        //     console.error("request failed");
        //     return false;
        // }


        const driver1 = {
            firstName: "מוטי",
            lastName: "דהרי",
            email: "motidaharii@gmail.com",
            password: "123456",
            phoneNumber: "521468555",
            gender: "זכר",
            university: "אריאל",
            degree: "BSc",
        };
        users.registration(driver1);


        // const driver2 = {
        //     firstName: "driver2",
        //     lastName: "driver2",
        //     email: "driver2@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(driver2);


        // const driver3 = {
        //     firstName: "driver3",
        //     lastName: "driver3",
        //     email: "driver3@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(driver3);


        // const driver4 = {
        //     firstName: "driver4",
        //     lastName: "driver4",
        //     email: "driver4@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(driver4);

        // const passenger1 = {
        //     firstName: "passenger1",
        //     lastName: "passenger1",
        //     email: "passenger1@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger1);


        // const passenger2 = {
        //     firstName: "passenger2",
        //     lastName: "passenger2",
        //     email: "passenger2@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger2);


        // const passenger3 = {
        //     firstName: "passenger3",
        //     lastName: "passenger3",
        //     email: "passenger3@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger3);


        // const passenger4 = {
        //     firstName: "passenger4",
        //     lastName: "passenger4",
        //     email: "passenger4@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger4);

        // const passenger5 = {
        //     firstName: "passenger5",
        //     lastName: "passenger5",
        //     email: "passenger5@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger5);


        // const passenger6 = {
        //     firstName: "passenger6",
        //     lastName: "passenger6",
        //     email: "passenger6@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger6);


        // const passenger7 = {
        //     firstName: "passenger7",
        //     lastName: "passenger7",
        //     email: "passenger7@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger7);


        // const passenger8 = {
        //     firstName: "passenger8",
        //     lastName: "passenger8",
        //     email: "passenger8@mail.com",
        //     password: "123456",
        //     phoneNumber: "521468555",
        //     gender: "זכר",
        //     university: "אריאל",
        //     degree: "BSc",
        // };
        // users.registration(passenger8);




        // const user = {
        //     email: "motidaharii@mail.com",
        //     password: "123456"
        // }
        // users.login(user)


        // const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRDhheVN0U1NBUTJxakk4QXNscEkifSwiaWF0IjoxNjM5NTA4ODc5LCJleHAiOjE2Mzk1MTYwNzl9.UtUw27vx7f-msc52XnsL48aC8rdIt8IK7zAshMeU3qw"


        // // /**addDrive */
        // const driveData = {
        //     date: "12/12/2021",
        //     time: "12:00",
        //     from: "חיפה",
        //     to: "ירושלים",
        //     numofSeats: 4,
        //     price: 15,
        //     returnDrive: false,
        // }
        // drives.addDrive(driveData, token)

        // const driveData2 = {
        //     date: "12/12/2021",
        //     time: "13:00",
        //     from: "חיפה",
        //     to: "ירושלים",
        //     numofSeats: 4,
        //     price: 15,
        //     returnDrive: false,
        // }
        // drives.addDrive(driveData2, token)

        // const driveData3 = {
        //     date: "12/12/2021",
        //     time: "14:00",
        //     from: "חיפה",
        //     to: "ירושלים",
        //     numofSeats: 4,
        //     price: 15,
        //     returnDrive: false,
        // }
        // drives.addDrive(driveData3, token)



        // const driveData4 = {
        //     date: "12/12/2021",
        //     time: "15:00",
        //     from: "חיפה",
        //     to: "ירושלים",
        //     numofSeats: 4,
        //     price: 15,
        //     returnDrive: false,
        // }
        // drives.addDrive(driveData4, token)

        // const driveData5 = {
        //     date: "12/12/2021",
        //     time: "16:00",
        //     from: "חיפה",
        //     to: "ירושלים",
        //     numofSeats: 4,
        //     price: 15,
        //     returnDrive: false,
        // }
        // drives.addDrive(driveData5, token)





        /*
        create users
        */









        /**send Offer To Driver */
        // const offer = {
        //     driveID: "gIA647dVkh4YKeYQh7Id",
        //     price: 5,
        // }
        // drives.sendOfferToDriver(offer, tokenPassenger1)


        // const chatID = "sqXGHZYjq9BdUSETGZAq";
        // const chat = {
        //     chatID: chatID,
        // }
        // chats.getChat(chat, driver)





        /**get All Chats By User*/
        // chats.getAllChatsByUser(tokenPassenger)



        /**get All Message*/
        // const chat = {
        //     chatID: "ZVdD53JY3g95fVMk4jWD",
        // }
        // chats.getAllMessage( tokenPassenger)



        /**get Chat*/
        // const chat = {
        //     chatID: "ZVdD53JY3g95fVMk4jWD",
        // }
        // chats.getChat(chat, tokenPassenger)

        /**add Message To Chat*/
        // const chat = {
        //     chatID: "ZVdD53JY3g95fVMk4jWD",
        //     msg: "2הודעה מסויימת"
        // }
        // chats.addMessageToChat(chat, tokenPassenger)
    }





    if (!fonts) {
        return null
    } else {
        return (
            <View style={{ marginTop: 100, }}>

                <Button
                    onPress={() => {
                        // onSubmitUsers()
                        // onSubmitDrives()
                        // onSubmitChats()
                        // onSubmitNotification()
                        onSubmitChats()
                    }}
                    title={"שליחת בקשה"}></Button>
            </View>
        )
    }


}
const onSubmitNotification = () => {
    //console.log("onSubmit = ")
    const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNmltdDJob29WMWlnenlNa2JXSGgifSwiaWF0IjoxNjM5MzMyOTU1LCJleHAiOjE2MzkzNDAxNTV9.3BO7X6jOGjEo2hzKXQPcsafXKKeJEIxGeenQ5Rfyo_Q";
    const tokenDriver = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiVWZRdUNjS0I0TmZ5RUlhUTZ6bVgifSwiaWF0IjoxNjM5Mzk5MzExLCJleHAiOjE2Mzk0MDY1MTF9.48EYcpc4zt4W6U4VPpEEYF1sUyaeEpk5WOC4t10xBrM";
    const tokenPassenger = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiT2NrT1UxbGtqSm40YkxrNkpDYlUifSwiaWF0IjoxNjM5NDAyMjU0LCJleHAiOjE2Mzk0MDk0NTR9.VR4rYXrpbBiReAe0MXIiRPTHU3xE3eVySJPLkdL-Hng";


    /**get All Notifications*/

    // const notificationData = {
    //     notificationID: "H8Rurmo5pJT48aBWIEc5"
    // }
    // notifications.readMSG(notificationData, tokenPassenger)


    /**get All Notifications*/
    // notifications.getAllNotifications(tokenPassenger)


    /**add Notifications*/
    // const notificationData = {
    //     title: "title",
    //     text: "text",
    //     textDetails: "textDetails",
    //     type: "type"
    // }
    // notifications.addNotifications(notificationData, tokenPassenger)
}



const onSubmitUsers = () => {
    //console.log("onSubmit = ")

    // /**getAllUsers*/
    // users.getAllUsers("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRnFHb3FUT3c0c3pkZUJDc0NuQ1QifSwiaWF0IjoxNjM5MzIzOTQyLCJleHAiOjE2MzkzMzExNDJ9.OqhnDl09CCwbTzT7T4bECGYJ_bFYUB7K45UUn2Q0Kko")


    /**getUserByEmail */
    // const user = {
    //     email: "driver@mail.com",
    // }
    // users.getUserByEmail(user, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRnFHb3FUT3c0c3pkZUJDc0NuQ1QifSwiaWF0IjoxNjM5MzIzOTQyLCJleHAiOjE2MzkzMzExNDJ9.OqhnDl09CCwbTzT7T4bECGYJ_bFYUB7K45UUn2Q0Kko")


    /**getUserByToken */
    // users.getUser("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRnFHb3FUT3c0c3pkZUJDc0NuQ1QifSwiaWF0IjoxNjM5MzIzOTQyLCJleHAiOjE2MzkzMzExNDJ9.OqhnDl09CCwbTzT7T4bECGYJ_bFYUB7K45UUn2Q0Kko")


    /**updateUser */
    // const user = {
    //     avatar: "change",
    //     firstName: "change",
    //     lastName: "change",
    //     email: "change@mail.com",
    //     password: "123456",
    //     phoneNumber: "521468555",
    //     gender: "זכר",
    //     university: "אריאל",
    //     universityImage: "change",
    //     degree: "change",
    // }
    // users.updateUser(user, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiRnFHb3FUT3c0c3pkZUJDc0NuQ1QifSwiaWF0IjoxNjM5MzIzOTQyLCJleHAiOjE2MzkzMzExNDJ9.OqhnDl09CCwbTzT7T4bECGYJ_bFYUB7K45UUn2Q0Kko")


    /**deleteUser */
    // users.deleteUser("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiN1pudEJVOHkzNkt4YTdSR2ZxT1AifSwiaWF0IjoxNjM5MzIzNDA1LCJleHAiOjE2MzkzMzA2MDV9.jRVNwBoLjSMQUHQnalUSTi9FKoSZlh91T9mkuBWAv3I")


    /**registration */
    // const user = {
    //     avatar: "sdsadasdda",
    //     firstName: "user1",
    //     lastName: "y",
    //     email: "change@mail.com",
    //     password: "123456",
    //     phoneNumber: "521468555",
    //     gender: "זכר",
    //     university: "אריאל",
    //     universityImage: "Ariesaddsl",
    //     degree: "BSc",
    // }
    // users.registration(user)


    /**login */
    // const user = {
    //     "email": "driver2232@mail.com",
    //     "password": "123456"
    // }
    // users.login(user)
    // //console.log("onSubmit = ")
}


const onSubmitDrives = () => {
    //console.log("onSubmit = ")
    const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNmltdDJob29WMWlnenlNa2JXSGgifSwiaWF0IjoxNjM5MzMyOTU1LCJleHAiOjE2MzkzNDAxNTV9.3BO7X6jOGjEo2hzKXQPcsafXKKeJEIxGeenQ5Rfyo_Q";
    const tokenDriver = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiVWZRdUNjS0I0TmZ5RUlhUTZ6bVgifSwiaWF0IjoxNjM5Mzk5MzExLCJleHAiOjE2Mzk0MDY1MTF9.48EYcpc4zt4W6U4VPpEEYF1sUyaeEpk5WOC4t10xBrM";
    const tokenPassenger = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiT2NrT1UxbGtqSm40YkxrNkpDYlUifSwiaWF0IjoxNjM5Mzk5MzM2LCJleHAiOjE2Mzk0MDY1MzZ9.sYt4ldiCgxCJXYRh1Az5-oqAjJn0KsvlPVpLA3B6j_s";

    // const user = {
    //     "email": "passenger@mail.com",
    //     "password": "123456"
    // }
    // users.login(user)

    /**send Offer To Passenger From Driver*/
    // const offer = {
    //     offerID: "CNUsLXLPN1KWBgDLviJh",
    //     price: 20
    // }
    // drives.sendOfferToPassengerFromDriver(offer, tokenDriver)

    /**get offer as driver dont work */
    // const offer = {
    //     "offerID": "1oPg30KK74mwivynMPWx"
    // }
    // drives.confirmPassengersToDrive(offer, tokenDriver)

    /**get offer as driver */
    // drives.getDriverOffers(tokenDriver)

    /**send Offer To Driver */
    // const offer = {
    //     driveID: "QMbqqFBJcwaR7OuJMMTv",
    //     price: 5,
    // }
    // drives.sendOfferToDriver(offer, tokenPassenger)

    /**get Passenger Offers */
    // drives.getPassengerOffers(tokenPassenger)

    /**search Drive As Passenger */
    // const searchData = {
    //     date: "29/01/2021",
    //     time: "15:00",
    //     from: "אריאל",
    //     to: "אריאל",
    // }
    // drives.searchDriveAsPassenger(searchData, tokenPassenger)

    /**addDrive */
    // const driveData = {
    //     date: "29/01/2021",
    //     time: "15:30",
    //     from: "אריאל",
    //     to: "אריאל",
    //     numofSeats: 5,
    //     price: 15,
    //     returnDrive: false,
    // }
    // drives.addDrive(driveData, tokenDriver)


    /**get User Drives */
    // drives.getUserDrives(token);

    /**delete Drive dont work*/
    // const driveData = {
    //     driveID: "DdWCncLjFG1mk14RH7oH"
    // };
    // drives.deleteDrive(driveData, token);

}



// const onSubmitChats = () => {
//     //console.log("onSubmit = ")
//     const token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiNmltdDJob29WMWlnenlNa2JXSGgifSwiaWF0IjoxNjM5MzMyOTU1LCJleHAiOjE2MzkzNDAxNTV9.3BO7X6jOGjEo2hzKXQPcsafXKKeJEIxGeenQ5Rfyo_Q";
//     const tokenDriver = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoiVWZRdUNjS0I0TmZ5RUlhUTZ6bVgifSwiaWF0IjoxNjM5Mzk5MzExLCJleHAiOjE2Mzk0MDY1MTF9.48EYcpc4zt4W6U4VPpEEYF1sUyaeEpk5WOC4t10xBrM";
//     const tokenPassenger1 = "";

//     /**get All Chats By User*/
//     // chats.getAllChatsByUser(tokenPassenger)



//     /**get All Message*/
//     // const chat = {
//     //     chatID: "ZVdD53JY3g95fVMk4jWD",
//     // }
//     // chats.getAllMessage( tokenPassenger)



//     /**get Chat*/
//     // const chat = {
//     //     chatID: "ZVdD53JY3g95fVMk4jWD",
//     // }
//     // chats.getChat(chat, tokenPassenger)

//     /**add Message To Chat*/
//     // const chat = {
//     //     chatID: "ZVdD53JY3g95fVMk4jWD",
//     //     msg: "2הודעה מסויימת"
//     // }
//     // chats.addMessageToChat(chat, tokenPassenger)
// }


export default CheckRequests
