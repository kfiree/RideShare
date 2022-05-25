import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { useFonts } from 'expo-font';
import moment from 'moment';
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
import Loading from "./../nested/Loading";

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import { FontAwesome5, Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import chats from './../../../../../requests/Chats'

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function MyChats() {
    const navigation = useNavigation();
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn, detailsDrive, seDetailsDrive } = useContext(Context)
    const [chatsArr, setChatsArr] = useState([]);
    const [countdown, setCountdown] = useState();
    let index = 1;
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });

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


    useEffect(async () => {
        try {
            if (isSignedIn && userData.token.length > 0) {
                setLoadingPopup(true);
                const res = await chats.getAllChatsByUser(userData.token)
                if (res.length > 0) {
                    setChatsArr(res);
                    setLoadingPopup(false);
                } else {
                    setChatsArr([]);
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
    const goToChatDrive = (drive) => {
        seDetailsDrive(drive)
        navigation.navigate('ChatDrive')
    }
    if (!fonts) {
        return null;
    } else {
        return (
            <ImageBackground
                style={styles.background}
                source={require('../../../images/images/cover.png')}
            >
                <SafeAreaView>
                    <Header />
                    <ScrollView>
                        <View style={{ width: "100%" }} >
                            <View style={styles.logoContainer}>
                                <Text style={styles.title}>הצ'אטים שלי</Text>
                            </View>
                            {chatsArr.map((obj) => {
                                return (
                                    <TouchableOpacity key={obj.driveData.driveID} style={styles.container}
                                        onPress={() =>
                                            goToChatDrive(obj)
                                        }>

                                        <View style={styles.rowCenter}>

                                            <View style={styles.circle} >
                                                <Text style={styles.Rating}>{index++}</Text>
                                            </View>

                                            <View style={styles.bokLeft}>


                                                <View style={styles.item}>
                                                    <View style={styles.boxText}>
                                                        <Text style={styles.text}>יום {getDay(obj.driveData.day)}, {obj.driveData.date}</Text>
                                                    </View>
                                                    <View style={styles.icon}>
                                                        <Entypo name="calendar" size={25} color="black" />
                                                    </View>
                                                </View>

                                                <View style={styles.item}>
                                                    <View style={styles.boxText}>
                                                        <Text style={styles.text}>{moment(obj.driveData.time).format("HH:MM")}</Text>
                                                    </View>
                                                    <View style={styles.icon}>
                                                        <AntDesign name="clockcircleo" size={25} color="black" />
                                                    </View>
                                                </View>


                                                <View style={styles.item}>
                                                    <View style={styles.boxText}>
                                                        <Text style={styles.text}>{obj.driveData.to}</Text>
                                                        <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                        <Text style={styles.text}>{obj.driveData.from}</Text>

                                                    </View>
                                                    <View style={styles.icon}>
                                                        <Entypo style={{ marginLeft: 3 }} name="location-pin" size={36} color="black" />
                                                    </View>
                                                </View>


                                                <View style={styles.item}>
                                                    <View style={styles.boxText}>
                                                        <Text style={styles.text}>{moment(obj.driveData.time).endOf('hour').fromNow()}</Text>
                                                        <Text style={styles.text}>זמן האיסוף: </Text>
                                                    </View>

                                                    <View style={styles.icon}>
                                                        <FontAwesome5 name="hourglass-end" size={24} color="black" />
                                                    </View>
                                                </View>
                                            </View>
                                            <View style={styles.boxRight}>
                                                <View style={{ flexDirection: 'row', marginTop: 15, }}>
                                                    {obj.participants.length >= 1 && obj.participants[0].email !== userData.email ?
                                                        <Gravatar options={{
                                                            email: obj.participants[0].email,
                                                            parameters: { "size": "200", "d": "mm" },
                                                            secure: true
                                                        }}
                                                            style={styles.imagePerson} />
                                                        : null}

                                                    {obj.participants.length >= 2 && obj.participants[1].email !== userData.email ?
                                                        <Gravatar options={{
                                                            email: obj.participants[1].email,
                                                            parameters: { "size": "200", "d": "mm" },
                                                            secure: true
                                                        }}
                                                            style={styles.imagePerson} ></Gravatar>
                                                        : null}
                                                </View>

                                                <View style={{ flexDirection: 'row' }}>
                                                    {obj.participants.length >= 3 && obj.participants[2].email !== userData.email ?
                                                        <Gravatar options={{
                                                            email: obj.participants[2].email,
                                                            parameters: { "size": "200", "d": "mm" },
                                                            secure: true
                                                        }}
                                                            style={styles.imagePerson} />
                                                        : null}

                                                    {obj.participants.length >= 4 && obj.participants[3].email !== userData.email ?
                                                        <Gravatar options={{
                                                            email: obj.participants[3].email,
                                                            parameters: { "size": "200", "d": "mm" },
                                                            secure: true
                                                        }}
                                                            style={styles.imagePerson} ></Gravatar>
                                                        : null}
                                                </View>
                                            </View>
                                        </View>
                                    </TouchableOpacity>
                                );
                            })
                            }

                        </View>
                    </ScrollView>

                    {!loadingPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <Loading />
                        </View>}

                    <Footer />
                </SafeAreaView>
            </ImageBackground>
        );
    }
}



const styles = StyleSheet.create({
    background: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    logoContainer: {
        alignItems: 'center',
    },
    title: {
        fontSize: 40,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    rowCenter: {
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row',
        height: 200,
        width: "95%",
        backgroundColor: colors.backgroundConversationChat,
        borderRadius: 60,
        borderWidth: 1,
        borderColor: colors.black,
    },
    row: {
        flexDirection: 'row',
        width: "100%",
    },
    container: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 20,
        flexDirection: 'row',
        width: "100%",
    },
    boxRight: {
        padding: 5,
        width: "55%",
        height: "100%",
        marginRight: 20,
    },
    bokLeft: {
        justifyContent: 'center',
        alignItems: 'center',
        width: "70%",
        height: "100%",
    },
    imagePerson: {
        margin: 5,
        width: 60,
        height: 60,
        borderRadius: 70 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    Rating: {
        fontSize: 18,
        fontFamily: "CalibriRegular",
        fontWeight: '700',
    },

    circle: {
        width: 40,
        height: 40,
        backgroundColor: colors.primary,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderRadius: 40,
        alignSelf: 'flex-start',
        justifyContent: 'center',
        alignItems: 'center',
        position: 'absolute',
        left: 0,
    },
    item: {
        flexDirection: 'row',
        width: "100%",
        height: 35,
        justifyContent: 'center',
        marginTop: 10,
    },
    icon: {
        width: "12%",
        height: "100%",
        alignSelf: 'center',
        flexDirection: 'row',
        justifyContent: 'flex-end',
    },
    boxText: {
        width: "90%",
        height: "100%",
        alignSelf: 'flex-start',
        justifyContent: 'flex-end',
        marginTop: 5,
        flexDirection: 'row',
    },
    text: {
        fontSize: 14,
        fontFamily: "CalibriRegular",
        textAlign: 'right',
    },
    fontAwesome: {
        marginRight: 5,
        marginLeft: 5,
    },
})
export default MyChats;