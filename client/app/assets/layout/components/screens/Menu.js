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
} from 'react-native';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import MenuOpen from "./../nested/MenuOpen";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;


const sizeHeader = windowWidth / 4;


function Menu() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)

    const navigation = useNavigation();

    //
    if (!fonts) {
        return null;
    } else {
        return (
            <ImageBackground
                style={styles.background}
                source={require('../../../images/images/cover.png')}>
                <Header />
                <TouchableOpacity style={styles.bigBox}
                    onPress={() => {
                        navigation.navigate('HomePage');
                    }}>

                </TouchableOpacity>
                <MenuOpen />
                <Footer style={{ marginButton: 50, backgroundColor: "black" }} />
            </ImageBackground>

        );
    }
}



const styles = StyleSheet.create({
    background: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    bigBox: {
        height: (windowHeight - (2.5 * sizeHeader)),
        width: "100%",
    },
})
export default Menu;