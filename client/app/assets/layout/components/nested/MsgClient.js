import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
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
import colors from '../../layout'

const MsgClient = ({ nameUser, time, msg }) => {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    if (!fonts) {
        return null;
    } else {
        return (
            <View style={{ width: "100%", marginTop: 7.5, marginBottom: 7.5, }}>
                <View style={styles.headerChat}>
                    <View style={styles.hourChat}>
                        <Text style={styles.hourChatText}>{time}</Text>
                    </View>
                    <View style={styles.NameChat}>
                        <Text style={styles.NameChatText}>{nameUser}</Text>
                    </View>
                </View>
                <View style={styles.msgClients}>

                    <Text numberOfLines={50} style={styles.chatText}>{msg}</Text>
                </View>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    headerChat: {
        width: "60%",
        height: 18,
        alignSelf: 'flex-end',
        flexDirection: 'row',
        marginBottom: (Platform.OS === 'android') ? 3 : 0,
    },
    hourChat: {
        width: "50%",
        justifyContent: 'center',
        alignItems: 'center',
        paddingLeft: 15,
        height: 20,

    },
    hourChatText: {
        alignSelf: 'flex-start',
        marginBottom: 5,
        width: "60%",
        textAlign: "left",
        color: colors.silverText,
        fontWeight: "700",
        fontSize: 12,
        fontFamily: "CalibriRegular",
    },
    NameChatText: {
        alignSelf: 'flex-end',
        width: "80%",
        // backgroundColor: "black",
        textAlign: "left",
        color: colors.importantNotification,
        marginRight: 10,
        fontWeight: "700",
        fontSize: (Platform.OS === 'android' ? 12 : 15),
        fontFamily: "CalibriRegular",
    },
    msgClients: {
        width: "60%",
        backgroundColor: colors.white,
        borderRadius: 12,
        marginRight: 5,
        borderWidth: 1,
        borderColor: colors.black,
        alignItems: 'flex-start',
        justifyContent: 'flex-start',
        alignSelf: 'flex-end',
        padding: 5,
    },
    NameChat: {
        width: "50%",
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
        // backgroundColor: "green",
        // marginRight: 1,
    },
    chatText: {
        fontWeight: "400",
        fontSize: (Platform.OS === 'android' ? 12 : 15),
        fontFamily: "CalibriRegular",
        width: "100%",
        textAlign: 'right',
    },
})
export default MsgClient
