import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
import { useFonts } from 'expo-font';

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
} from 'react-native'
import { Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import requests from './../../../../../request';
import axios from "axios";
import colors from '../../layout'
import token from './../../../../../logicApp';




/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
const CounterOfferPassengerPopup = () => {
    const {
        userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn,
        cities, universities, checkCityForUniversity, searchDrive, setSearchDrive
    } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { otherOffer, setOtherOffer, driveIdForOffer, setDriveIdForOffer, drivePrice, confirmDrivePopup, setConfirmDrivePopup, setDrivePrice, counterOfferPopup, setCounterOfferPopup } = useContext(ContextOtherOffer)
    // //console.log()
    const navigation = useNavigation();
    const [counterOffer, setCounterOffer] = useState(0);
    const upNum = (name) => {
        setCounterOffer(counterOffer + 1);
    }
    const downNum = (name) => {
        if (counterOffer > 0) {
            setCounterOffer(counterOffer - 1);
        }
    }
    const setToPrice = (val) => {
        setCounterOffer(val);
    }

    const onSubmit = async (offer) => {
        setLoadingPopup(true)
        if (counterOffer >= 0) {
            //console.log("driveIdForOffer = ", driveIdForOffer)
            //console.log("counterOffer = ", counterOffer)
            //  setLoadingPopup(true)
            //console.log("Submit Form");
            await token.getTokenFromStorage()
                .then((response) => {
                    const userToken = response;
                    if (userToken.length > 0) {
                        request(userToken, offer);
                    } else {
                        throw 'no token here';
                    }
                })
                .catch((err) => {
                    console.error(err)
                    setLoadingPopup(false)
                });
        }
    }
    // setConfirmDrivePopup(false)

    const request = async (token, offer) => {
        //console.log("driveIdForOffer = " + driveIdForOffer)
        //console.log("counterOffer = " + counterOffer)
        const offerToDrive = {
            driveID: driveIdForOffer,
            price: offer
        }
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'x-auth-token': token,
                }
            }
            //console.log("header : ", config.headers)
            //console.log("offerToDrive : ", JSON.stringify(offerToDrive))

            const body = JSON.stringify(offerToDrive);
            const res = await axios.post(requests.Drives.sendPassengerOffer, body, config);
            //console.log(res.data);
            setCounterOfferPopup(false);
            setLoadingPopup(false)
            // setConfirmDrivePopup(true)
            setSearchDrive(searchDrive.filter((drive) => { return drive.driveID !== driveIdForOffer }))
            // setLoadingPopup(false);
        } catch (err) {
            console.error(err);
            console.error("request failed");
            setLoadingPopup(false)
        }
    }


    if (!fonts) {
        return null;
    } else {
        return (
            <View style={styles.container}>
                <View style={{ width: "100%", height: "100%", position: 'absolute', opacity: 0.5 }}>
                    <ImageBackground
                        style={styles.background}
                        source={require('../../../images/images/cover.png')}>
                    </ImageBackground>
                </View>

                <View style={styles.popup}>
                    <View style={styles.rowBox}>
                        <View style={styles.inputPopupBox}>
                            <View style={styles.inputPopup}>
                                <View style={styles.logoInputSmallRight}>
                                    <TouchableOpacity
                                        onPress={() => {
                                            downNum()
                                        }}>
                                        <Entypo name="minus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.inputTextSmall}>
                                    <Text style={styles.textOffer}>{counterOffer}</Text>
                                </View>


                                <View style={styles.logoInputSmallLeft}>
                                    <TouchableOpacity
                                        onPress={() => {
                                            upNum()
                                        }}>
                                        <AntDesign name="plus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                </View>

                            </View>
                        </View>
                    </View>
                    <View style={styles.row}>
                        <TouchableOpacity style={styles.CloseToThePrice}
                            onPress={() => {
                                setToPrice((drivePrice - 1));
                            }}>
                            <Text style={styles.anotherPrice}>₪{(drivePrice - 1)}</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.CloseToThePrice}
                            onPress={() => {
                                setToPrice((drivePrice - 2));
                            }}>
                            <Text style={styles.anotherPrice}>₪{(drivePrice - 2)}</Text>
                        </TouchableOpacity>
                        <TouchableOpacity style={styles.CloseToThePrice}
                            onPress={() => {
                                setToPrice((drivePrice - 3));
                            }}>
                            <Text style={styles.anotherPrice}>₪{(drivePrice - 3)}</Text>
                        </TouchableOpacity>
                    </View>
                    {/* <View style={styles.ButtonConfirm}> */}
                    <TouchableOpacity
                        style={styles.ButtonConfirm}
                        onPress={() => {
                            onSubmit(counterOffer)
                        }}>
                        <Text style={styles.confirmButton}>הגשת הצעה נגדית</Text>
                    </TouchableOpacity>
                    {/* </View> */}
                    {/* <View style={styles.ButtonConfirm}> */}
                    <TouchableOpacity
                        style={styles.ButtonConfirm}
                        onPress={() => {
                            setCounterOfferPopup(false)
                        }}>
                        <Text style={styles.confirmButton}>סגור</Text>
                    </TouchableOpacity>
                    {/* </View> */}
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    background: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    popup: {
        height: 300, //(Platform.OS === 'android') ? (windowHeight - (windowWidth)) : (windowHeight - (windowWidth / 1.2)),
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
        marginTop: (windowWidth / 1.7),
        justifyContent: 'center',
        alignItems: 'center',
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    inputPopupBox: {
        width: "100%",
        height: 100,
        justifyContent: 'center',
        alignItems: 'center',
    },
    inputPopup: {
        width: "50%",
        height: "60%",
        flexDirection: 'row',
        borderRadius: 5,
        borderWidth: 1,
        borderColor: colors.search,
        backgroundColor: colors.white,
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
        width: "50%",
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
        // marginTop: (Platform.OS === 'android') ? 10 : 0,
    },
    textOffer: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        textAlignVertical: "top",
        textAlign: 'center',
    },
    logoInputSmallLeft: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderLeftWidth: 2,
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
        marginTop: 5,
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
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
    },
    ButtonConfirm: {
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.primary,
        width: "75%",
        height: 40,
        borderColor: colors.black,
        borderWidth: 1,
        borderRadius: 25,

    },
    confirmButton: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        color: colors.black,
        textAlign: 'center',
    },
});

export default CounterOfferPassengerPopup;
