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
import Loading from "./../nested/Loading";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function Charges() {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
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
                            <Text style={styles.title}>חיובים</Text>
                        </View>
                        <View style={{ height: 500 }}></View>
                        <View style={styles.submitForm}>

                            <TouchableOpacity style={styles.submitInput}
                                onPress={() => {

                                    navigation.navigate("WelcomeScreen")
                                }
                                }>
                                <View style={{
                                    justifyContent: 'center',
                                    alignItems: 'center',
                                    alignContent: 'center'
                                }}>
                                    <Text style={styles.btnSubmit}>התחברות</Text>

                                </View>


                            </TouchableOpacity>
                        </View>
                    </ScrollView>

                    <Footer />
                </SafeAreaView>
                {!loadingPopup ? null :
                    <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                        <Loading />
                    </View>}v
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
        top: 150,
        alignItems: 'center',
    },
    title: {
        fontSize: 50,
        fontFamily: "CalibriRegular",
        marginTop: 20,
        fontWeight: 'bold',
    },
    logo: {
        width: 250,
        height: 200,
    },
    form: {
        width: (windowWidth - 40),
        marginTop: 20,
        flexBasis: 400,
        justifyContent: 'center',
        alignItems: 'center',
    },
    inputsBox: {
        width: "100%",
    },
    inputText: {
        width: (windowWidth - 40),
        height: 50,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.input,
        marginTop: 15,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        textAlignVertical: "top",
        alignItems: 'center',
    },
    checkBox: {
        color: colors.black,
        marginTop: 20,
        width: "100%",
        flexDirection: "row",
        justifyContent: 'center',
        alignItems: 'center',
    },
    checkBoxInput: {
        marginStart: 20,
    },
    checkBoxText: {
        color: colors.black,
        fontSize: 25,
        fontFamily: "CalibriRegular",
    },
    link: {
        marginTop: 20,
        color: "#000000",
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textDecorationLine: 'underline',
    },
    social: {
        marginTop: 40,
        flexDirection: 'row',
    },
    facebookImg: {
        marginRight: 10,
        marginBottom: 20,
        width: 100,
        height: 80,
    },
    googleImg: {
        marginLeft: 10,
        marginBottom: 20,
        width: 100,
        height: 80,
    },
    submitForm: {
        marginTop: 20,
        justifyContent: 'center',
        alignItems: 'center',
    },
    submitInput: {
        width: (windowWidth - 100),
        height: 50,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        marginBottom: 100,
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.submitForm,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
    },
    btnSubmit: {
        fontWeight: 'bold',
        fontSize: 30,
        fontFamily: "CalibriRegular",
        width: (windowWidth - 100),
        height: 50,
    }
})
export default Charges;