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
    Animated,
    Alert,
    Platform,
    StatusBar,
    CheckBox,
} from 'react-native';
import colors from '../../layout'
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;





const Loading = () => {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
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
                        <Image
                            style={{ width: 100, height: 100 }}
                            source={require('./../../../images/images/wheel.gif')}
                        />
                    </View>

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
        alignSelf: 'center',
        marginTop: (windowWidth / 2),
        justifyContent: 'center',
        alignItems: 'center',
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    titlePopup: {
        fontSize: (Platform.OS === 'android') ? 22 : 26,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    textPopup: {
        fontSize: (Platform.OS === 'android') ? 20 : 24,
        fontFamily: "CalibriRegular",
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.confirmButton,
        width: "50%",
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
    confirmButton: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },









})
export default Loading
