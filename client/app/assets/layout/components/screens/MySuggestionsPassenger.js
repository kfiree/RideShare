import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
import { Gravatar, GravatarApi } from 'react-native-gravatar';

import {
    SafeAreaView,
    TouchableWithoutFeedback,
    TouchableOpacity,
    TouchableHighlight,
    TouchableNativeFeedback,
    StyleSheet,
    KeyboardAvoidingView,
    Text,
    View,
    Image,
    ScrollView,
    TextInput,
    ImageBackground,
    Dimensions,
    Button,
    Alert,
    Platform,
    StatusBar,
    CheckBox,
} from 'react-native';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import Loading from "./../nested/Loading";
import token from './../../../../../logicApp';
import { useFonts } from 'expo-font';
import drives from './../../../../../requests/Drives';
import moment from 'moment';


import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import CounterOfferPassengerPopup from "../nested/CounterOfferPassengerPopup";
import ConfirmDrivePassengerPopup from "../nested/ConfirmDrivePassengerPopup";
import NoResultDrivesMySuggestionsPassenger from "../nested/NoResultDrivesMySuggestionsPassenger";


import { Colors } from 'react-native/Libraries/NewAppScreen';
/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
function MySuggestionsPassenger() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    const [counterOffer, setCounterOffer] = useState(0);
    const [driveIdForOffer, setDriveIdForOffer] = useState("");
    const {
        sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn,
        cities, universities, checkCityForUniversity, searchDrive, setSearchDrive, search, setSearch
    } = useContext(Context)


    /**
     * popups
     */
    // setConfirmDrivePopup(false)
    const [noResultPopup, setNoResultPopup] = useState(false);
    const [title, setTitle] = useState("ההצעות שלי");
    const [confirmDrivePopup, setConfirmDrivePopup] = useState(false);
    const [counterOfferPopup, setCounterOfferPopup] = useState(false);
    const [otherOffer, setOtherOffer] = useState(0);
    const [drivePrice, setDrivePrice] = useState(0);
    const [dataConfirmDrive, setDataConfirmDrive] = useState({
        name: "",
        email: ""
    });
    useEffect(async () => {
        if (!search) {
            setTitle("ההצעות שלי")
            // try {
            //     if (isSignedIn && userData.token.length > 0) {
            //         setLoadingPopup(true);
            //         const res = await drives.getPassengerOffers(userData.token);
            //         // console.log("res = " + JSON.stringify(res));
            //         setSearchDrive(res);
            //         setLoadingPopup(false);
            //         if (res.length > 0) {
            //             setLoadingPopup(false);
            //         } else {
            //             setLoadingPopup(false);
            //             setNoResultPopup(true);
            //         }
            //     } else {
            //         throw "user is no login"
            //     }
            // } catch (error) {
            //     setLoadingPopup(false);
            //     navigation.navigate("Registration")
            // }
        } else {
            setTitle("תוצאות חיפוש")
        }
    }, [])
    const sendOffer = (driveId, price) => {
        setCounterOfferPopup(true);
        setDriveIdForOffer(driveId);
        setDrivePrice(price);
    }

    const delOfferDrive = (driveID) => {
        setLoadingPopup(true);
        const newArr = searchDrive.filter((drive) => {
            return drive.driveID !== driveID;
        });
        setSearchDrive(newArr);
        setLoadingPopup(false);
    }
    const confirmDrive = async (driveID, price) => {
        try {
            setLoadingPopup(true)
            if (userData.token !== "") {
                const offer = {
                    driveID,
                    price,
                }

                if (await drives.sendOfferToDriver(offer, userData.token)) {
                    const newArr = searchDrive.filter((drive) => {
                        return drive.driveID !== driveID;
                    });
                    setSearchDrive(newArr);
                    setLoadingPopup(false);
                    setConfirmDrivePopup(true)
                } else {
                    setLoadingPopup(false);
                }
            } else {
                throw "no token"
            }
        } catch (error) {
            //console.log(error)
            setLoadingPopup(false)
        }

    }
    if (!fonts) {
        return null;
    } else {
        return (
            <ContextOtherOffer.Provider value={{
                otherOffer, setOtherOffer,
                driveIdForOffer, setDriveIdForOffer,
                drivePrice, setDrivePrice,
                counterOfferPopup, setCounterOfferPopup,
                noResultPopup, setNoResultPopup,
                confirmDrivePopup, setConfirmDrivePopup
            }}>

                <ImageBackground
                    style={styles.background}
                    source={require('../../../images/images/cover.png')}
                >
                    <SafeAreaView>
                        <Header />
                        <ScrollView >
                            {title === "תוצאות חיפוש" ? null :
                                <View style={styles.rowCenter}>
                                    <TouchableOpacity onPress={() => {
                                        navigation.navigate("MySuggestionsDrive")
                                    }}>
                                        <View style={styles.switch}>
                                            <View style={styles.circleSwitch}>
                                                <MaterialCommunityIcons name="seat-passenger" size={24} color="black" />
                                            </View>
                                        </View>

                                    </TouchableOpacity>
                                </View>
                            }
                            <View style={styles.content}>
                                <Text style={styles.title}>{title}</Text>
                            </View>




                            {searchDrive && searchDrive.length <= 0 ? null :
                                searchDrive.map((drive) => {
                                    return (
                                        <View key={drive.driveID} style={styles.container}>
                                            <TouchableOpacity onPress={() => {
                                                //console.log("red")
                                                delOfferDrive(drive.driveID, drive.price);
                                            }}>
                                                <Image
                                                    style={{ height: 30, width: 40 }}
                                                    resizeMode='cover'
                                                    source={require('./../../../images/images/redCircle.png')} />


                                            </TouchableOpacity>
                                            <View style={styles.suggestions}>
                                                <View style={styles.boxPrice}>
                                                    <Text style={styles.price}>₪{drive.price}</Text>
                                                </View>
                                                {/* <Text>{drive.driveID}</Text> */}
                                                <View style={{
                                                    height: "100%",
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    marginRight: 10,
                                                    width: "25%"
                                                }}>
                                                    <View style={{ flexDirection: 'row', width: "100%", height: "15%", marginTop: 5 }}>
                                                        <View style={styles.circle}>
                                                            <Text style={styles.Rating2}>1</Text>
                                                        </View>
                                                        <View style={{ flexDirection: 'row', justifyContent: 'flex-end', alignItems: 'flex-end', width: "65%" }}>
                                                            <Text style={{ fontSize: 13, marginRight: 3 }}>{drive.from}</Text>
                                                        </View>

                                                    </View>
                                                    <View style={{ flexDirection: 'row', width: "100%", height: "18%" }}>
                                                        <Image
                                                            style={{ position: 'absolute', right: 6, top: 0 }}
                                                            source={require('./../../../images/images/blackCircle.png')}></Image>
                                                    </View>
                                                    <View style={{ flexDirection: 'row', width: "100%", height: "15%", marginBottom: 20 }}>
                                                        <View style={styles.circle}>
                                                            <Text style={styles.Rating2}>1</Text>
                                                        </View>
                                                        <View style={{ flexDirection: 'row', justifyContent: 'flex-end', alignItems: 'flex-end', width: "65%", }}>
                                                            <Text style={{ fontSize: 13, marginRight: 3 }}>{drive.to}</Text>
                                                        </View>
                                                    </View>
                                                </View>
                                                <View style={styles.boxContent}>
                                                    <View style={styles.nameDrive}>
                                                        <Text style={styles.nameTitle}>{drive.firstName}</Text>
                                                    </View>
                                                    <View style={styles.rowWithMargin}>
                                                        <View style={styles.row2}>
                                                            <Text style={styles.Rating2}>({(drive.ratingNum) ? drive.ratingNum : 50})</Text>
                                                            <Text style={styles.Rating}>{(drive.rating) ? drive.rating : 8}</Text>
                                                            <Image style={styles.star} resizeMode='center' source={require('./../../../images/icons/fullStar.png')} />
                                                        </View>
                                                    </View>
                                                    <View style={styles.row}>
                                                        <View style={styles.row2}>

                                                            <Text style={styles.Rating2}>         </Text>
                                                            <Text style={styles.Rating2}>{(drive.numofSeats) ? drive.numofSeats : 4}</Text>
                                                            <MaterialCommunityIcons name="seat-passenger" size={24} color="black" />
                                                        </View>
                                                    </View>
                                                    <View style={styles.row}>
                                                        <View style={styles.row2}>
                                                            <Text style={styles.Rating2}> {moment(drive.time).format("HH:MM")} </Text>
                                                            <AntDesign name="clockcircleo" size={24} color="black" />
                                                        </View>
                                                    </View>

                                                    <View style={styles.submitForm}>
                                                        <TouchableOpacity style={styles.submitInput}
                                                            onPress={() => {
                                                                sendOffer(drive.driveID, drive.price)
                                                            }}>
                                                            <View style={{
                                                                justifyContent: 'center',
                                                                alignItems: 'center',
                                                                alignContent: 'center',

                                                            }}>
                                                                <Text style={styles.btnSubmit}>הגשת הצעה נגדית</Text>

                                                            </View>
                                                        </TouchableOpacity>
                                                    </View>
                                                </View>



                                                <View style={styles.boxImage}>
                                                    <View style={styles.imagePerson}>
                                                        {/* <Image
                                                        resizeMode="cover"
                                                        source={require('./../../../images/images/woman3.jpg')}
                                                        style={{
                                                            height: "100%",
                                                            width: "100%",
                                                            padding: 5
                                                        }}
                                                    /> */}
                                                        <Gravatar options={{
                                                            email: drive.email,
                                                            parameters: { "size": "200", "d": "mm" },
                                                            secure: true
                                                        }}
                                                            style={{
                                                                height: "100%",
                                                                width: "100%",
                                                                padding: 5
                                                            }} />
                                                    </View>
                                                </View>


                                            </View>
                                            <TouchableOpacity onPress={() => {
                                                //console.log("green")
                                                setDataConfirmDrive({
                                                    ...dataConfirmDrive,
                                                    name: drive.firstName,
                                                    email: drive.email,
                                                })
                                                confirmDrive(drive.driveID, drive.price);
                                            }}>
                                                <Image
                                                    style={{ height: 30, width: 40 }}
                                                    resizeMode='cover'
                                                    source={require('./../../../images/images/greenCircle.png')} />
                                            </TouchableOpacity>
                                        </View>
                                    )
                                })
                            }

                        </ScrollView>



                        {/*
                    popup - NoResultDrivesMySuggestionsPassenger
                */ }

                        {!noResultPopup ? null :
                            <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                                <NoResultDrivesMySuggestionsPassenger />
                            </View>}


                        {/*
                popup1 - ConfirmDrivePassengerPopup
                */ }
                        {!confirmDrivePopup ? null :
                            <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                                <ConfirmDrivePassengerPopup email={dataConfirmDrive.email} name={dataConfirmDrive.name} confirmDrivePopup={confirmDrivePopup} setConfirmDrivePopup={setConfirmDrivePopup} />
                            </View>}


                        {/*
                popup1 - counterOfferPassenger
                */ }
                        {!counterOfferPopup ? null :
                            <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                                <CounterOfferPassengerPopup />
                            </View>}



                        <Footer style={{ position: "absolute", justifyContent: "flex-end" }} />
                    </SafeAreaView>

                    {!loadingPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <Loading />
                        </View>}

                </ImageBackground>
            </ContextOtherOffer.Provider>
        );
    }
}



const styles = StyleSheet.create({
    background: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    container: {
        flexDirection: 'row',
        flex: 1,
        marginTop: 25,

        justifyContent: 'center',
        alignItems: 'center'
    },
    boxImage: {
        height: "100%",
        width: "25%",
        justifyContent: 'flex-start',
        marginTop: 50,
        alignItems: 'center',
    },
    boxImage2: {
        height: "100%",
        width: "25%",
        justifyContent: 'flex-start',
        alignItems: 'center',
    },
    boxContent: {
        height: "100%",
        width: "25%",
        justifyContent: 'flex-start',
        marginRight: 10
    },
    boxContent2: {
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: 5,
        width: "25%",
    },
    boxPrice: {
        height: "100%",
        width: "15%",
        justifyContent: 'center',
        alignItems: 'center',
    },
    content: {
        // marginTop: 110,
        justifyContent: 'center',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
    },
    rowNew: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
        marginRight: 10,
    },
    rowCenter: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
        marginTop: 2,
    },
    row2: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 7,
    },
    rowWithMargin: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginRight: 5,
    },
    star: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginRight: - 5,
        height: 24,
        width: 24,
    },
    rowWithMargin2: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'flex-end',
    },
    nameDrive: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
        marginBottom: 10,
    },
    switch: {
        backgroundColor: colors.confirmButton,
        width: 80,
        height: 40,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'flex-start',
        alignItems: 'center',
        flexDirection: 'row',
        marginLeft: 15,
    },
    circleSwitch: {
        width: 30,
        height: 30,
        marginLeft: 3,
        backgroundColor: colors.white,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 40,
        marginTop: 10,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    suggestions: {
        flexDirection: 'row',
        width: (windowWidth - 80),
        marginTop: 20,
        height: 200,
        backgroundColor: colors.backgroundConversationChat,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
    },
    imagePerson: {
        width: 70,
        height: 70,
        borderRadius: 110 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    imagePerson2: {
        width: 120,
        height: 120,
        borderRadius: 120 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    nameTitle: {
        fontSize: 24,
        fontFamily: "CalibriRegular",
        marginTop: 5,
        fontWeight: '600',

    },
    Rating: {
        fontSize: 13,
        fontFamily: "CalibriRegular",
        fontWeight: '700',

    },
    Rating2: {
        fontSize: 14,
        fontFamily: "CalibriRegular",

    },
    circle: {
        width: 30,
        height: 30,
        backgroundColor: colors.white,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
        position: 'absolute',
        right: 0,

    },
    circleHidden: {
        width: 30,
        height: 30,
        justifyContent: 'center',
        alignItems: 'center',
    },
    price: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        marginTop: 10,
        fontWeight: 'bold',
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 5,
        marginRight: 50,
    },
    submitInput: {
        width: 180,
        height: 35,
        marginTop: 5,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.submitForm,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    btnSubmit: {
        fontWeight: 'bold',
        fontSize: Platform.OS === "android" ? 18 : 22,
        fontFamily: "CalibriRegular",
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.primary,
        width: "70%",
        height: 40,
        borderColor: colors.black,
        borderWidth: 1,
        borderRadius: 25,

    },



})
export default MySuggestionsPassenger;