import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';

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
import { useFonts } from 'expo-font';
import notifications from './../../../../../requests/Notifications'

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import Loading from "./../nested/Loading";
import NoResultNotifications from "../nested/NoResultNotifications";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function Notification() {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [noResultPopup, setNoResultPopup] = useState(false);
    let key = 0;
    const navigation = useNavigation();
    const [notificationsArr, setNotificationsArr] = useState([]);
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });

    useEffect(async () => {
        try {
            if (isSignedIn && userData.token.length > 0) {
                setLoadingPopup(true);
                const res = await notifications.getAllNotifications(userData.token)
                setNotificationsArr(res);
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
        }
    }, [])

    const sendOfferPassenger = async () => {
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
                        <ScrollView>

                            <View style={styles.logoContainer}>
                                <Text style={styles.title}>התראות</Text>
                            </View>


                            <View style={styles.container}>
                                {notificationsArr.map((obj) => {

                                    return (
                                        <View key={key++} style={styles.boxNotification}>
                                            <View style={styles.rowBox}>
                                                <View style={{ width: "20%" }}>
                                                    {!obj.read ? <View style={styles.circle}>
                                                        <Ionicons name="notifications" size={24} color="black" />
                                                    </View> :
                                                        null
                                                    }
                                                </View>
                                                {!obj.read ?
                                                    <View style={{ width: "80%" }}>
                                                        <Text style={styles.titleNotification}>{obj.title}</Text>
                                                    </View> :

                                                    <View style={{ width: "100%" }}>
                                                        <Text style={styles.titleNotification}>{obj.title}</Text>
                                                    </View>
                                                }
                                            </View>
                                            <View style={styles.rowBox}>
                                                <View style={styles.textInNotification}>
                                                    <Text style={styles.textNotification}>{obj.text}</Text>
                                                </View>
                                            </View>
                                            <View style={styles.rowBox}>
                                                <Text style={styles.textNotification}>{obj.textDetails}</Text>
                                            </View>
                                            <View style={styles.rowBox}>
                                                <Text style={styles.textNotificationSmall}>{obj.date}</Text>
                                            </View>
                                        </View>
                                    );
                                })}

                            </View>



                        </ScrollView>

                        <Footer />
                    </SafeAreaView>
                    {!loadingPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <Loading />
                        </View>}


                    {/*
                    popup - NoResult
                */ }

                    {!noResultPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <NoResultNotifications />
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
        justifyContent: 'center',
        alignItems: 'center',
        width: windowWidth,
        // marginTop: 500,
        // height: 500,
    },
    logoContainer: {
        alignItems: 'center',
    },
    title: {
        fontSize: 50,
        marginTop: 20,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    logo: {
        width: 250,
        height: 200,
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.confirmButton,
        width: "90%",
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
    boxNotification: {
        height: 150, //(Platform.OS === 'android') ? (windowHeight - (windowWidth)) : (windowHeight - (windowWidth / 1.2)),
        width: "90%",
        backgroundColor: 'black',
        alignSelf: 'center',
        backgroundColor: colors.white,
        borderBottomLeftRadius: 20,
        borderBottomRightRadius: 20,
        borderTopLeftRadius: 20,
        borderTopRightRadius: 20,
        marginTop: 20,
        justifyContent: 'center',
        alignItems: 'center',
    },
    titleNotification: {
        fontSize: (Platform.OS === 'android') ? 18 : 23,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
        textAlign: 'center',
    },
    textNotification: {
        fontSize: (Platform.OS === 'android') ? 16 : 18,
        fontWeight: "400",
        fontFamily: "CalibriRegular",
        marginTop: 5,
        textAlign: 'center',
    },
    textNotificationSmall: {
        fontSize: 10,
        fontWeight: "800",
        fontFamily: "CalibriRegular",
        color: colors.silverHR,
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
    textInNotification: {
        width: "90%",
        justifyContent: 'center',
        alignItems: 'center',
    },
    circle: {
        width: 30,
        height: 30,
        backgroundColor: colors.importantNotification,
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
        marginLeft: 20
    },
})
export default Notification;