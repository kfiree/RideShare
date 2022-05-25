import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
import token from './../../../../../logicApp';
import requests from './../../../../../request';
import axios from "axios";
import { useFonts } from 'expo-font';
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
import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import MySuggestionsDrivePopup from "./../nested/MySuggestionsDrivePopup";
import Loading from "./../nested/Loading";
import NoResultDrivesMySuggestionsPassenger from "./../nested/NoResultDrivesMySuggestionsPassenger";
import drives from './../../../../../requests/Drives'
import ConfirmDriveDriverPopup from "./../nested/ConfirmDriveDriverPopup";



import { Colors } from 'react-native/Libraries/NewAppScreen';
import moment from 'moment';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
function MySuggestionsDrive() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const {
        sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn,
        cities, universities, checkCityForUniversity, suggestionsDriver, setSuggestionsDriver
    } = useContext(Context)


    const navigation = useNavigation();
    const [noResultPopup, setNoResultPopup] = useState(false);
    const [showPopupNoResult, setShowPopupNoResult] = useState(false);
    const [confirmDrivePopup, setConfirmDrivePopup] = useState(null);
    const [price, setPrice] = useState(0);
    const [dataConfirmDrive, setDataConfirmDrive] = useState({
        name: "",
        email: ""
    });


    useEffect(async () => {
        try {
            if (isSignedIn && userData.token.length > 0) {
                setLoadingPopup(true);
                const res = await drives.getDriverOffers(userData.token);
                setSuggestionsDriver(res);
                // //console.log("setMySuggestions(res);" + JSON.stringify(res))
                if (res.length > 0) {
                    setLoadingPopup(false);
                } else {
                    setLoadingPopup(false);
                    setNoResultPopup(true);
                }
            } else {
                throw "user is no login"
            }
        } catch (error) {
            setLoadingPopup(false);
            navigation.navigate("Registration")
        }
    }, [])


    const getDay = (day) => {
        switch (day) {
            case "0":
                return "א"
                break;
            case "1":
                return "ב"
                break;
            case "2":
                return "ג"
                break;
            case "3":
                return "ד"
                break;
            case "4":
                return "ה"
                break;
            case "5":
                return "ו"
                break;
            case "6":
                return "ש"
                break;
            default:
                break;
        }
    }

    const onSubmit = async (price, offerID) => {
        console.log("offerID = " + offerID)
        try {
            setLoadingPopup(true)
            if (userData.token !== "") {
                const offerDrive = {
                    offerID,
                    price
                }
                if (await drives.sendOfferToPassengerFromDriver(offerDrive, userData.token)) {
                    const newArr = suggestionsDriver.filter((drive) => { return drive.offerID != offerID });
                    setSuggestionsDriver(newArr)
                    setLoadingPopup(false);
                    setToPrice(0);
                } else {
                    throw "addDrive failed";
                }
                setLoadingPopup(false)
            } else {
                throw "no token"
            }
        } catch (error) {
            //console.log(error)
            setLoadingPopup(false)
        }
    }

    const confirmPassenger = async (offerID) => {
        //console.log("offerID = " + offerID)

        try {
            setLoadingPopup(true)
            if (userData.token !== "") {
                const offerDrive = {
                    offerID
                }
                if (await drives.confirmPassengersToDrive(offerDrive, userData.token)) {
                    const newArr = suggestionsDriver.filter((drive) => { return drive.offerID !== offerDrive.offerID });
                    setSuggestionsDriver(newArr)
                    setSuggestionsDriver(newArr)
                    //console.log("suggestionsDriver after = ", JSON.stringify(suggestionsDriver))
                    setLoadingPopup(false);
                    setConfirmDrivePopup(true)
                    // navigation.navigate("MySuggestionsDrive");
                } else {
                    throw "addDrive failed";
                }
                setLoadingPopup(false)
            } else {
                throw "no token"
            }
        } catch (error) {
            //console.log(error)
            setLoadingPopup(false)
        }
    }
    const setToPrice = (val) => {
        setPrice(val);
        //console.log("price = val = ", price)
    }
    const upNum = () => {
        setPrice(price + 1);
        //console.log("price++ = ", price)
    }
    const downNum = () => {
        setPrice(price - 1);
        //console.log("price-- = ", price)
    }


    const delOfferDrive = (offerId) => {
        setLoadingPopup(true);
        const newArr = suggestionsDriver.filter((drive) => { return drive.offerID !== offerId });
        setSuggestionsDriver(newArr);
        setLoadingPopup(false);
        setToPrice(0);
    }
    const confirmDrive = (driveId) => {
        //console.log("driveId = ", driveId);
    }


    if (!fonts) {
        return null;
    } else {
        return (
            <ContextOtherOffer.Provider value={{
                noResultPopup, setNoResultPopup
            }}>
                <ImageBackground
                    style={styles.background}
                    source={require('../../../images/images/cover.png')}
                >
                    <SafeAreaView>
                        <Header />
                        <ScrollView >
                            <View style={styles.rowCenter}>
                                <TouchableOpacity onPress={() => {
                                    navigation.navigate("MySuggestionsPassenger")
                                }}>
                                    <View style={styles.switch}>
                                        <View style={styles.circleSwitch}>
                                            <Ionicons name="car-sharp" size={24} color="black" />
                                        </View>
                                    </View>
                                </TouchableOpacity>
                            </View>
                            <View style={styles.content}>
                                <Text style={styles.title}>ההצעות שלי</Text>
                            </View>

                            {suggestionsDriver.length <= 0 ? null :

                                suggestionsDriver.map((drive) => {
                                    return (
                                        <View key={drive.offerID} style={styles.container}>
                                            <TouchableOpacity onPress={() => {
                                                //console.log("red")
                                                delOfferDrive(drive.offerID)
                                            }}>
                                                <Image
                                                    style={{ height: 30, width: 50 }}
                                                    resizeMode='cover'
                                                    source={require('./../../../images/images/redCircle.png')} />
                                            </TouchableOpacity>
                                            <View style={styles.suggestions}>
                                                <View style={styles.imagePerson}>
                                                    <Gravatar options={{
                                                        email: drive.email,
                                                        parameters: { "s": "2048", "d": "mm" },
                                                        secure: true
                                                    }}
                                                        style={{
                                                            height: "100%",
                                                            width: "100%",
                                                            padding: 5
                                                        }} />

                                                    {/* <Image
                                                    resizeMode="cover"
                                                    source={{ uri: drive.avatar }}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                /> */}
                                                </View>

                                                <View style={styles.row}>
                                                    <Text style={styles.nameTitle}>{drive.firstName} הציעה</Text>
                                                </View>
                                                <View style={styles.row}>
                                                    <Text style={styles.price}>{drive.price} ש"ח</Text>
                                                </View>
                                                <View style={styles.row}>
                                                    <Text style={styles.text}>עבור נסיעה מ{drive.from} ל{drive.to}</Text>
                                                </View>
                                                <View style={styles.row}>
                                                    <Text style={styles.text}>יום {getDay(drive.day)}' {drive.date} {moment(drive.time).format("HH:MM")}</Text>
                                                </View>
                                                <View style={styles.textAndInput}>
                                                    <View style={styles.leftBlockSmall}>
                                                        <TouchableOpacity
                                                            style={styles.logoInputSmallRight}
                                                            onPress={() => {
                                                                downNum()
                                                            }}>
                                                            <Entypo name="minus" size={25} color={colors.search} />
                                                        </TouchableOpacity>
                                                        <View style={{ justifyContent: 'center', alignItems: 'center', width: "50%" }}>
                                                            <Text style={styles.inputTextSmall}>{price}</Text>
                                                        </View>
                                                        <TouchableOpacity
                                                            style={styles.logoInputSmallLeft}
                                                            onPress={() => {
                                                                upNum()
                                                            }}>
                                                            <AntDesign name="plus" size={25} color={colors.search} />
                                                        </TouchableOpacity>

                                                    </View>

                                                </View>

                                                <View style={styles.row}>
                                                    <TouchableOpacity style={styles.CloseToThePrice}
                                                        onPress={() => {
                                                            setToPrice((parseInt(drive.price) + 1));
                                                        }}>
                                                        <Text style={styles.anotherPrice}>₪{((parseInt(drive.price) + 1))}</Text>
                                                    </TouchableOpacity>

                                                    <TouchableOpacity style={styles.CloseToThePrice}
                                                        onPress={() => {
                                                            setToPrice((parseInt(drive.price) + 2));
                                                        }}>
                                                        <Text style={styles.anotherPrice}>₪{((parseInt(drive.price) + 2))}</Text>
                                                    </TouchableOpacity>

                                                    <TouchableOpacity style={styles.CloseToThePrice}
                                                        onPress={() => {
                                                            setToPrice((parseInt(drive.price) + 3));
                                                        }}>
                                                        <Text style={styles.anotherPrice}>₪{(parseInt(drive.price) + 3)}</Text>
                                                    </TouchableOpacity>
                                                </View>
                                                <View style={styles.submitForm}>
                                                    <TouchableOpacity style={styles.submitInput}
                                                        onPress={() => {
                                                            onSubmit(price, drive.offerID)
                                                        }}
                                                    >
                                                        <View style={{
                                                            justifyContent: 'center',
                                                            alignItems: 'center',
                                                            alignContent: 'center'
                                                        }}>
                                                            <Text style={styles.btnSubmit}>הגשת הצעה נגדית</Text>

                                                        </View>
                                                    </TouchableOpacity>
                                                </View>
                                            </View>
                                            <TouchableOpacity onPress={() => {
                                                //console.log("green")
                                                setDataConfirmDrive({
                                                    ...dataConfirmDrive,
                                                    name: drive.firstName,
                                                    email: drive.email,
                                                })
                                                confirmPassenger(drive.offerID)
                                            }}>
                                                <Image
                                                    style={{ height: 30, width: 50 }}
                                                    resizeMode='cover'
                                                    source={require('./../../../images/images/greenCircle.png')} />
                                            </TouchableOpacity>
                                        </View>
                                    )
                                })
                            }
                        </ScrollView>



                        <Footer style={{ position: "absolute", justifyContent: "flex-end" }} />
                    </SafeAreaView>

                    {!noResultPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <NoResultDrivesMySuggestionsPassenger />
                        </View>}

                    {!confirmDrivePopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <ConfirmDriveDriverPopup email={dataConfirmDrive.email} name={dataConfirmDrive.name} confirmDrivePopup={confirmDrivePopup} setConfirmDrivePopup={setConfirmDrivePopup} />
                        </View>}

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
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 20,
    },
    imagePerson: {
        width: 110,
        height: 110,
        borderRadius: 110 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    content: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    nameTitle: {
        fontSize: 30,
        marginTop: 5,
        fontWeight: '400',
        fontFamily: "CalibriRegular",
    },
    price: {
        fontSize: 30,
        marginTop: 10,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    text: {
        color: colors.black,
        fontSize: (Platform.OS === 'android') ? 18 : 22,
        marginTop: 10,
        fontFamily: "CalibriRegular",
        textAlign: 'center'
    },
    title: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        marginTop: 10,
        fontWeight: 'bold',
    },
    switch: {
        backgroundColor: colors.switchSilver,
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
        justifyContent: 'flex-end',
        marginLeft: 15,
        alignItems: 'center',
        flexDirection: 'row',
    },
    circleSwitch: {
        width: 30,
        height: 30,
        marginRight: 3,
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
    suggestions: {
        width: (windowWidth - 100),
        marginTop: 20,
        height: Platform.OS === "android" ? 550 : 500,
        backgroundColor: colors.backgroundConversationChat,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 60,
        borderBottomRightRadius: 60,
        borderTopLeftRadius: 60,
        borderTopRightRadius: 60,
        justifyContent: 'center',
        alignItems: 'center',
    },
    textAndInput: {
        flexDirection: "row",
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        height: 40,
        marginTop: 10,
        width: "100%",
    },
    rowCenter: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
        marginTop: 2,
    },
    leftBlockSmall: {
        borderColor: colors.search,
        borderLeftWidth: 2,
        borderRightWidth: 2,
        borderTopWidth: 2,
        borderBottomWidth: 2,
        borderBottomLeftRadius: 5,
        borderBottomRightRadius: 5,
        borderTopLeftRadius: 5,
        borderTopRightRadius: 5,
        backgroundColor: colors.white,
        flexDirection: "row",
        width: "40%",
        height: "100%",
        justifyContent: 'flex-start',
    },
    logoInputSmallRight: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderRightWidth: 2,
        paddingTop: 1,
        paddingBottom: 1,
    },
    inputTextSmall: {
        width: "100%",
        fontFamily: "CalibriRegular",
        fontSize: 25,
        textAlign: 'center',
        alignSelf: 'center',
        backgroundColor: colors.white
    },
    logoInputSmallLeft: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderLeftWidth: 2,
    },
    tBlockBig: {
        justifyContent: 'center',
        alignItems: 'flex-end',
        width: "60%",
        height: "100%",
    },
    CloseToThePrice: {
        height: 50,
        width: 70,
        backgroundColor: colors.confirmButton,
        borderColor: colors.search,
        borderLeftWidth: 2,
        borderRightWidth: 2,
        borderTopWidth: 2,
        borderBottomWidth: 2,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        marginTop: 25,
        marginLeft: 5,
        marginRight: 5,
        justifyContent: 'center',
        alignItems: 'center',
    },
    anotherPrice: {
        fontSize: 15,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    submitInput: {
        width: 200,
        height: 50,
        marginTop: 15,
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
        fontFamily: "CalibriRegular",
        fontSize: Platform.OS === "android" ? 18 : 22,
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.confirmButton,
        width: "80%",
        height: 50,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
    },
    popup: {
        height: 350, //(Platform.OS === 'android') ? (windowHeight - (windowWidth)) : (windowHeight - (windowWidth / 1.2)),
        width: (windowWidth - 80),
        position: 'absolute',
        backgroundColor: 'black',
        alignSelf: 'center',
        backgroundColor: colors.popupBackground,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 40,
        borderBottomRightRadius: 40,
        borderTopLeftRadius: 40,
        borderTopRightRadius: 40,
        marginTop: (windowWidth / 1.5),
        justifyContent: 'center',
        alignItems: 'center',
    },
    titlePopup: {
        fontSize: (Platform.OS === 'android') ? 32 : 36,
        marginBottom: 50,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    textPopup: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        marginTop: 5,
    },
    confirmButton: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
})
export default MySuggestionsDrive;