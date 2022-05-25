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

const MsgUser = ({ nameUser, time, msg }) => {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    if (!fonts) {
        return null;
    } else {
        return (
            <View style={{ marginTop: 7.5, marginBottom: 7.5, }}>
                <View style={{ width: "100%", }}>
                    <View style={styles.headerChatUserMsg}>
                        <View style={styles.hourChatUserMsg}>
                            <Text style={styles.hourChatTextUserMsg}>{time}</Text>
                        </View>
                    </View>
                    <View style={styles.msgUser}>
                        <Text numberOfLines={50} style={styles.chatText}>{msg} </Text>
                    </View>
                </View>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    headerChatUserMsg: {
        width: "60%",
        // height: "20%",
        alignSelf: 'flex-start',
        flexDirection: 'row',
    },
    hourChatUserMsg: {
        width: "100%",
        height: 20,
        justifyContent: 'center',
        alignItems: 'center',
        paddingLeft: 15,
    },
    hourChatTextUserMsg: {
        width: "100%",
        textAlign: "right",
        color: colors.silverText,
        marginRight: 10,
        fontWeight: "700",
        fontSize: (Platform.OS === 'android' ? 12 : 13),
        fontFamily: "CalibriRegular",
    },
    msgUser: {
        width: "60%",
        backgroundColor: colors.myMessageChat,
        borderRadius: 15,
        marginRight: 5,
        borderWidth: 1,
        borderColor: colors.black,
        alignItems: 'flex-start',
        justifyContent: 'flex-start',
        alignSelf: 'flex-start',
        padding: 5,
        marginLeft: 5,
    },
    chatText: {
        fontWeight: "400",
        fontSize: (Platform.OS === 'android' ? 12 : 15),
        fontFamily: "CalibriRegular",
        width: "100%",
        textAlign: 'right',
    },
})
export default MsgUser
