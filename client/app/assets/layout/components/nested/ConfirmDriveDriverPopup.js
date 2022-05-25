import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { useFonts } from 'expo-font';
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
} from 'react-native'
import { Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import colors from '../../layout'




/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
const ConfirmDrivePassengerPopup = ({ name, email, setConfirmDrivePopup, confirmDrivePopup }) => {
    const { userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
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

                <View style={styles.popup2}>
                    <ImageBackground
                        style={styles.background}
                        source={require('../../../images/images/confetigif.gif')}
                    >
                        <View style={styles.rowBox2}>
                            <View style={styles.boxImage2}>
                                <View style={styles.imagePerson2}>
                                    {/* <Image
                                    resizeMode="cover"
                                    source={require('./../../../images/images/woman3.jpg')}
                                    style={{
                                        height: "100%",
                                        width: "100%",
                                        padding: 5
                                    }}
                                /> */}
                                    <Gravatar options={{
                                        email: email,
                                        parameters: { "size": "200", "d": "mm" },
                                        secure: true
                                    }}
                                        style={{
                                            height: "100%",
                                            width: "100%",
                                            padding: 5
                                        }} />


                                </View>
                            </View>
                        </View>
                        <View style={styles.rowBox2}>
                            <Text style={styles.textPopup2}>
                                הנסיעה עם {name}
                            </Text>
                        </View>
                        <View style={styles.rowBox2}>
                            <Text style={styles.textPopup2}>אושרה בהצלחה</Text>
                        </View>
                        {/* <View style={styles.ButtonConfirm2}> */}
                        <TouchableOpacity
                            style={styles.ButtonConfirm2}
                            onPress={() => {
                                setConfirmDrivePopup(false)

                                // navigation.navigate('ChatDrive')
                            }}>
                            <Text style={styles.confirmButton2}>חזרה</Text>
                        </TouchableOpacity>
                        {/* </View> */}
                    </ImageBackground>
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
        // flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    popup2: {
        height: (Platform.OS === 'android') ? (windowHeight - (1.2 * windowWidth)) : (windowHeight - (1.2 * windowWidth)),
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
        marginTop: (windowWidth / 1.7),
        justifyContent: 'center',
        alignItems: 'center',
    },
    titlePopup2: {
        fontSize: (Platform.OS === 'android') ? 32 : 36,
        fontFamily: "CalibriRegular",
        marginTop: 15,
        fontWeight: 'bold',
    },
    textPopup2: {
        paddingRight: 2,
        paddingLeft: 2,
        fontSize: (Platform.OS === 'android') ? 23 : 25,
        fontFamily: "CalibriRegular",
        marginTop: 5,
        fontWeight: 'bold',
    },
    confirmButton2: {
        fontSize: (Platform.OS === 'android') ? 23 : 25,
        width: "100%",
        fontFamily: "CalibriRegular",
        color: colors.white,
        textAlign: 'center',
    },
    rowBox2: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    boxImage2: {
        height: "100%",
        width: "25%",
        justifyContent: 'flex-start',
        alignItems: 'center',
    },
    imagePerson2: {
        width: 120,
        height: 120,
        borderRadius: 120 / 2,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black
    },
    ButtonConfirm2: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.confirmButton,
        width: "65%",
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
});

export default ConfirmDrivePassengerPopup;
