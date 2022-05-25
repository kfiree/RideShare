import React, { useContext, useState, useLayoutEffect, createContext } from 'react';
import { View, Text } from 'react-native'

export const AppContext = createContext(null);



const MyAppContext = () => {
    const [userData, setUserData] = useState({
        firstName: "מוטי",
        lastName: "דהרי",
        email: "moti@gmail.com",
        password: "123456",
        phoneNumber: "0525675171",
        gender: "זכר",
        imageUniversity: "id",
        imageId: "id",
        university: "אריאל",
        degree: "מדעי המחשב",
        rating: 0,
        numRating: 0,
        numDrives: 0,
        modelCar: "מאזדה 2",
        colorCar: "כחול",
        dateOfBirth: "01/08/1993",
    });

    const [isSignedIn, setIsSignedIn] = useState(true);
    const [loaded] = useFonts({
        CalibriRegular: require('./app/assets/fonts/Calibri-Regular.ttf'),
    });


    useLayoutEffect(() => {
        //console.log("token.getTokenFromStorage() = " + token.getTokenFromStorage())
        if (token != null) {
            setIsSignedIn(true)
        } else {
            setIsSignedIn(false)
        }
    }, [isSignedIn])

    const remove = () => {
        token.removeTokenFromStorage();
        setIsSignedIn(false)
    }

    return (
        <AppContext.Provider value={{ loaded, userData, setUserData, isSignedIn, setIsSignedIn }}>


        </AppContext.Provider>
    )
}

export default MyAppContext
