import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextDriveChat } from '../../../../../ContextDriveChat';
import { useFonts } from 'expo-font';
import drives from './../../../../../requests/Drives';



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
import { FontAwesome5, Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import moment, { parseTwoDigitYear } from 'moment';
import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import MsgClient from "./../nested/MsgClient";
import MsgUser from "./../nested/MsgUser";
import Loading from "./../nested/Loading";
import CheckRequests from '../nested/CheckRequests';
import { Gravatar, GravatarApi } from 'react-native-gravatar';
import chats from './../../../../../requests/Chats'



import { Colors } from 'react-native/Libraries/NewAppScreen';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



function ChatDrive({ driveData }) {


    const [scrollToBottomY, setScrollToBottomY] = useState(100)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn, detailsDrive, seDetailsDrive } = useContext(Context)
    const scrollViewRefY = useRef(null);
    const navigation = useNavigation();

    const [valueText, seValueText] = useState("הודעה....");
    let key = 1;
    const timeToReload = 10000;





    useLayoutEffect(() => {
        if (scrollViewRefY.current !== null) {
            scrollViewRefY.current.scrollTo({
                y: scrollToBottomY * 50,
                animated: true,
            });
        }
    }, [scrollToBottomY]);

    useEffect(() => {
        const interval = setInterval(async () => {
            //console.log("scrollToBottomY = " + scrollToBottomY)
            try {
                if (isSignedIn && userData.token.length > 0) {
                    const Data = {
                        chatID: detailsDrive.driveData.chatID
                    }
                    const res = await chats.getAllMessage(Data, userData.token);
                    if (res.length > 0) {
                        seDetailsDrive({
                            ...detailsDrive,
                            chatMsg: res
                        })
                        if (scrollViewRefY.current !== null) {
                            scrollViewRefY.current.scrollTo({
                                y: scrollToBottomY * 50,
                                animated: true,
                            });
                        }
                    } else {
                        seDetailsDrive({
                            ...detailsDrive,
                            chatMsg: []
                        })
                    }
                } else {
                    throw "user is no login"
                }
            } catch (error) {
                //console.log("error = " + error)
            }
        }, timeToReload);
        return () => clearInterval(interval);
    }, [])


    const addMsgToChatBox = (val) => {
        let newMsgArr = detailsDrive.chatMsg;
        const item = {
            idUser: userData,
            name: userData.firstName,
            msg: val,
            hour: moment().utcOffset('+02:00').format('HH:MM')
        }
        newMsgArr.push(item)
        return newMsgArr
    }
    setLoadingPopup(false);
    const onSubmit = async (msg, email, firstName, chatID) => {
        if (msg !== "הודעה....") {
            try {
                setLoadingPopup(true)
                if (userData.token !== "") {
                    const chatMsg = {
                        chatID,
                        msg
                    }
                    // //console.log("chatMsg = ", JSON.stringify(chatMsg));
                    const res = await chats.addMessageToChat(chatMsg, userData.token)
                    if (res !== null) {
                        let newMsgArr = detailsDrive.chatMsg;
                        const item = {
                            email: email,
                            msg: msg,
                            name: firstName,
                            time: "" + moment().utcOffset('+02:00').format('HH:MM')
                        }
                        newMsgArr.push(item)
                        seDetailsDrive({
                            ...detailsDrive,
                            chatMsg: newMsgArr
                        });
                        seValueText("הודעה....")
                        setLoadingPopup(false);
                        if (scrollViewRefY.current !== null) {
                            scrollViewRefY.current.scrollTo({
                                y: scrollToBottomY * 50,
                                animated: true,
                            });
                        }
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

    }

    const request = (token, val) => {
        if (val.length != 0) {
            seDetailsDrive({
                ...detailsDrive,
                chatMsg: addMsgToChatBox(val)
            });

        }

    }

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

    const title = `יום ${getDay(detailsDrive.driveData.day)}', ${detailsDrive.driveData.date}, ${moment(detailsDrive.driveData.time).format("HH:MM")},\n  מ${detailsDrive.driveData.from} ל${detailsDrive.driveData.to}`;




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

                        <View style={styles.logoContainer}>
                            <Text style={styles.title}>{title}</Text>
                        </View>

                        <View style={styles.rowBox}>
                            <View style={styles.passengersImages}>
                                {detailsDrive &&
                                    detailsDrive.participants.filter((participant) => {
                                        return participant.email !== userData.email;
                                    }).map((obj) => {
                                        return (obj.email == userData.email ? null :
                                            <View key={++key} style={styles.boxImage}>
                                                <View style={styles.imagePerson}>
                                                    <Gravatar options={{
                                                        email: obj.email,
                                                        parameters: { "size": "200", "d": "mm" },
                                                        secure: true
                                                    }}
                                                        style={{
                                                            height: "100%",
                                                            width: "100%",
                                                            padding: 5
                                                        }} />
                                                </View>
                                                <Text style={styles.namePerson}>{obj.name}</Text>
                                            </View>)
                                    })
                                }
                            </View>
                        </View>

                        <View style={styles.rowBox}>
                            <View >
                                <View style={styles.item}>
                                    <View style={styles.boxText}>
                                        <Text style={styles.text}>{moment(detailsDrive.driveData.time).fromNow()}</Text>
                                    </View>
                                    <View style={styles.icon}>
                                        <FontAwesome5 name="hourglass-end" size={26} color="black" />
                                    </View>
                                </View>

                            </View>
                        </View>

                        <View style={styles.rowBox}>
                            <View style={styles.conversation}>
                                <ScrollView
                                    onContentSizeChange={(contentWidth, contentHeight) => {
                                        setScrollToBottomY((contentHeight + 100))

                                    }}
                                    ref={scrollViewRefY}
                                    style={styles.conversationBox}
                                    onLayout={(event) => {
                                        const { y, height } = event.nativeEvent.layout;
                                        setScrollToBottomY((y + 100))
                                    }}>
                                    {detailsDrive &&
                                        detailsDrive.chatMsg.map((obj) => {
                                            if (obj.email === userData.email) {
                                                return <MsgUser key={++key} msg={obj.msg} time={obj.time} />
                                            } else {
                                                return <MsgClient key={++key} msg={obj.msg} nameUser={obj.name} time={obj.time} />
                                            }
                                        })
                                    }
                                </ScrollView>

                                <View style={styles.msgBox}>
                                    <View style={styles.row}>

                                        <TouchableOpacity onPress={() => {
                                            seValueText("יצאתי לדרך")
                                        }}>
                                            <View style={styles.offerBlock}>
                                                <Text style={styles.textOffer}>יצאתי לדרך</Text>
                                            </View>
                                        </TouchableOpacity>

                                        <TouchableOpacity onPress={() => {
                                            seValueText("מתעכב/ת ב-5 דק'")
                                        }}>
                                            <View style={styles.offerBlock}>
                                                <Text style={styles.textOffer}>מתעכב/ת</Text>
                                                <Text style={styles.textOffer}>ב-5 דק'</Text>
                                            </View>
                                        </TouchableOpacity>

                                        <TouchableOpacity onPress={() => {
                                            seValueText("איפה אתה?")
                                        }}>
                                            <View style={styles.offerBlock}>
                                                <Text style={styles.textOffer}>איפה אתה?</Text>
                                            </View>
                                        </TouchableOpacity>

                                        <TouchableOpacity onPress={() => {
                                            seValueText("אני פה")
                                        }}>
                                            <View style={styles.offerBlock}>
                                                <Text style={styles.textOffer}>אני פה</Text>
                                            </View>
                                        </TouchableOpacity>

                                    </View>
                                </View>


                                <View style={styles.inputMsg}>
                                    <View style={{
                                        width: "20%",
                                        height: "100%",
                                        alignItems: 'center',
                                        justifyContent: 'center'
                                    }}>
                                        <TouchableOpacity onPress={() => {
                                            onSubmit(valueText, userData.email, userData.firstName, detailsDrive.driveData.chatID);
                                        }}>
                                            <Ionicons style={styles.transform} name="ios-send-sharp" size={40} color={colors.sendMsg} />
                                        </TouchableOpacity>
                                    </View>
                                    <View style={{ width: "80%", height: "100%" }}>
                                        <TextInput placeholder={valueText} style={styles.inputText}
                                            onEndEditing={(e) => {
                                                seValueText(e.nativeEvent.text);
                                            }}
                                            placeholderTextColor={colors.placeholderTextColor}
                                        />
                                    </View>
                                </View>
                            </View>
                        </View>

                    </ScrollView>
                    <Footer />

                </SafeAreaView>

                {!loadingPopup ? null :
                    <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                        <Loading />
                    </View>}

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
        fontSize: (Platform.OS === 'android') ? 20 : 25,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
        textAlign: 'center',
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
    },
    boxImage: {
        justifyContent: 'flex-start',
        alignItems: 'center',
    },
    imagePerson: {
        width: 70,
        height: 70,
        borderRadius: 70 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    passengersImages: {
        width: "80%",
        marginTop: 20,
        justifyContent: 'space-between',
        flexDirection: 'row'
    },
    namePerson: {
        fontSize: 18,
        fontFamily: "CalibriRegular",
        marginTop: 10,
    },
    item: {
        flexDirection: 'row',
        width: "100%",
        height: 35,
        justifyContent: 'center',
        marginTop: 10,
    },
    boxText: {
        height: "100%",
        alignSelf: 'center',
        justifyContent: 'center',
        marginTop: 5,
        flexDirection: 'row',
    },
    text: {
        fontSize: 18,
        fontFamily: "CalibriRegular",
        textAlign: 'right',
    },
    icon: {
        height: "100%",
        alignSelf: 'center',
        flexDirection: 'row',
        justifyContent: 'center',
        marginLeft: 10,
    },
    conversation: {
        width: (windowWidth - 40),
        justifyContent: 'center',
        alignItems: 'center',
        height: 420,
        backgroundColor: colors.backgroundConversationChat,
        marginTop: 10,
        borderRadius: 20,
        marginBottom: 50,
    },
    conversationBox: {
        width: "90%",
        height: 50,
        borderWidth: 1,
        marginTop: 15,
        marginRight: 15,
        marginLeft: 15,
        borderColor: colors.search,
    },
    msgBox: {
        width: "90%",
        height: 50,

        marginTop: 15,
        marginRight: 15,
        marginLeft: 15,
    },
    inputMsg: {
        width: "90%",
        height: 50,
        marginTop: 15,
        marginRight: 15,
        marginLeft: 15,
        marginBottom: 20,
        flexDirection: 'row',
    },

    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    textOffer: {
        fontSize: 12,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
        textAlign: 'center',
        color: colors.white,
    },
    offerBlock: {
        height: 45,
        backgroundColor: colors.confirmButton,
        borderColor: colors.black,
        borderRadius: 10,
        borderWidth: 1,
        padding: 5,
    },
    inputText: {
        width: "100%",
        height: 50,
        fontSize: (Platform.OS === 'android') ? 22 : 25,
        fontFamily: "CalibriRegular",
        textAlign: 'right',
        paddingRight: 5,
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.white,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        // textAlignVertical: "top",
        alignItems: 'center',
    },
    transform: {
        transform: [{ rotate: '180deg' }],

    }
})
export default ChatDrive;