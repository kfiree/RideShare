import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
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
import { Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import colors from '../../layout'




/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
const NoResultNotifications = () => {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const { noResultPopup, setNoResultPopup } = useContext(ContextOtherOffer)
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
                        <Text style={styles.titlePopup}>אין התראות</Text>
                    </View>
                    <TouchableOpacity
                        style={styles.ButtonConfirm}
                        onPress={() => {
                            navigation.navigate('HomePage')
                        }}>
                        <Text style={styles.confirmButton}>למעבר לעמוד הבית</Text>
                    </TouchableOpacity>

                    <TouchableOpacity
                        style={styles.ButtonConfirm}
                        onPress={() => {
                            setNoResultPopup(false)
                        }}>
                        <Text style={styles.confirmButton}>סגור</Text>
                    </TouchableOpacity>


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
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
    },
    titlePopup: {
        fontSize: (Platform.OS === 'android') ? 32 : 36,
        fontFamily: "CalibriRegular",
        marginBottom: 70,
        fontWeight: 'bold',
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.primary,
        width: "70%",
        height: 40,
        borderColor: colors.black,
        borderWidth: 1,
        borderRadius: 25,
    },
    confirmButton: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        color: colors.black,
        marginRight: 10,
        textAlign: 'center',
    },
});

export default NoResultNotifications;
