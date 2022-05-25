import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';

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
import { useFonts } from 'expo-font';

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import Loading from "./../nested/Loading";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function Payments() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
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
                        <View style={styles.container}>
                            <View style={styles.logoContainer}>
                                <Text style={styles.title}>הגדרת אמצעי תשלום</Text>
                            </View>
                        </View>

                        <View style={styles.container}>
                            <TouchableOpacity style={styles.row}>
                                <View style={styles.PaymentsBox}>
                                    <View style={styles.leftBox}>
                                        <Text style={styles.textInput}>Bit</Text>
                                    </View>
                                    <View style={styles.rightBox}>
                                        <Image
                                            resizeMode='cover'
                                            style={styles.imageIcon}
                                            source={require('./../../../images/images/unnamed.png')}
                                        />
                                    </View>
                                </View>
                            </TouchableOpacity>
                        </View>

                        <View style={styles.container}>
                            <TouchableOpacity style={styles.row}>
                                <View style={styles.PaymentsBox}>
                                    <View style={styles.leftBox}>
                                        <Text style={styles.textInput}>Google Pay</Text>
                                    </View>
                                    <View style={styles.rightBox}>
                                        <Image
                                            resizeMode='cover'
                                            style={styles.imageIcon}
                                            source={require('./../../../images/images/googlepay.png')}
                                        />
                                    </View>
                                </View>
                            </TouchableOpacity>
                        </View>

                        <View style={styles.container}>
                            <TouchableOpacity style={styles.row}>
                                <View style={styles.PaymentsBox}>
                                    <View style={styles.leftBox}>
                                        <Text style={styles.textInput}>Apple Pay</Text>
                                    </View>
                                    <View style={styles.rightBox}>
                                        <Image
                                            resizeMode='cover'
                                            style={styles.imageIcon}
                                            source={require('./../../../images/images/applepay.png')}
                                        />
                                    </View>
                                </View>
                            </TouchableOpacity>
                        </View>

                        <View style={styles.container}>
                            <TouchableOpacity style={styles.row}>
                                <View style={styles.PaymentsBox}>
                                    <View style={styles.leftBox}>
                                        <Text style={styles.textInput}>כרטיס אשראי</Text>
                                    </View>
                                    <View style={styles.rightBox}>
                                        <Image
                                            resizeMode='cover'
                                            style={styles.imageIcon}
                                            source={require('./../../../images/images/creditCard.jpg')}
                                        />
                                    </View>
                                </View>
                            </TouchableOpacity>
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
    container: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 20,
        width: "100%",
    },
    row: {
        flexDirection: 'row',
    },
    logoContainer: {
        alignItems: 'center',
    },
    title: {
        fontSize: (Platform.OS === 'android') ? 30 : 40,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    PaymentsBox: {
        width: "70%",
        height: 80,
        borderRadius: 20,
        borderColor: colors.black,
        borderWidth: 1,
        backgroundColor: colors.backgroundConversationChat,
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
    },
    rightBox: {
        width: "38%",
        height: " 100%",
        // backgroundColor: "black",
        alignSelf: "flex-end",
        alignItems: 'center',
        justifyContent: 'center',

    },
    leftBox: {
        width: "62%",
        height: " 100%",
        alignSelf: "flex-start",
        alignItems: 'center',
        justifyContent: 'center',
    },
    imageIcon: {
        height: 60,
        width: 95,
    },
    textInput: {
        fontSize: (Platform.OS === 'android') ? 23 : 25,
        fontFamily: "CalibriRegular",
    },
})
export default Payments;