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

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function BenefitsPurchased({ setShowBenefitsPurchased }) {
    const { userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    if (!fonts) {
        return null;
    } else {
        return (
            <View style={styles.popup}>
                <View style={styles.rowBox}>
                    <Text style={styles.titlePopup}>ההטבה מומשה בהצלחה!</Text>

                </View>
                <View style={styles.rowBox}>
                    <Text style={styles.textPopup}>הציגו את הברקוד בבית העסק:</Text>
                </View>
                <View style={styles.rowBox}>
                    <Image
                        source={require('./../../../images/images/qr.png')}
                        style={styles.imageQR}
                        resizeMode='cover'
                    />
                </View>
                <View style={styles.ButtonConfirm}>
                    <TouchableOpacity onPress={() => {
                        setShowBenefitsPurchased(false)
                        Alert.alert("get benefits", "need to check if for the user have enough money")
                    }}>
                        <Text style={styles.confirmButton}>אישור</Text>
                    </TouchableOpacity>
                </View>
            </View>
        );
    }
}



const styles = StyleSheet.create({
    popup: {
        height: (Platform.OS === 'android') ? (windowHeight - (windowWidth)) : (windowHeight - (windowWidth / 1.2)),
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
        marginTop: (windowWidth / 1.8),
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
        fontSize: 22,
        fontFamily: "CalibriRegular",
        marginTop: 15,
        fontWeight: 'bold',
    },
    textPopup: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        marginTop: 5,
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
    imageQR: {
        height: 150,
        width: 150,
        marginTop: 20,
    },
    confirmButton: {
        fontSize: 30,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },
})
export default BenefitsPurchased;