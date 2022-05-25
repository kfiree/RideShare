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
import colors from '../../layout'
import { useFonts } from 'expo-font';

import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import { Ionicons, Feather } from '@expo/vector-icons';

const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;
function Header() {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    if (!fonts) {
        return null;
    } else {
        return (
            <SafeAreaView>
                <View style={{
                    marginTop: (Platform.OS === "android") ? StatusBar.currentHeight : 0,
                    flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
                }}>

                    <TouchableOpacity
                        onPress={() => {
                            navigation.navigate("Notification")
                        }}>
                        <View style={{
                            // backgroundColor: "blue",
                            width: sizeHeader,
                            height: sizeHeader,
                            alignItems: 'center',
                            justifyContent: 'center'
                        }}>
                            <Ionicons name="ios-notifications" size={40} color="black" />
                        </View>
                    </TouchableOpacity>

                    <TouchableOpacity
                        onPress={() => {
                            navigation.navigate("HomePage");

                        }}>
                        <View style={styles.parentLogo} >
                            <Image
                                resizeMode="stretch"
                                source={require('./../../../images/logo/mainLogo.png')}
                                style={styles.logo}
                            />
                        </View>
                    </TouchableOpacity>


                    <TouchableOpacity
                        onPress={() => {
                            navigation.navigate("Menu");
                        }}
                    >
                        <View style={{
                            // backgroundColor: "tomato",
                            width: sizeHeader,
                            height: sizeHeader,
                            alignItems: 'center',
                            justifyContent: 'center'
                        }} >
                            <Feather name="menu" size={40} color="black" />
                        </View>
                    </TouchableOpacity>

                </View>



            </SafeAreaView>
        );
    }
}
const styles = StyleSheet.create({

    parentLogo: {
        width: (sizeHeader * 2),
        height: sizeHeader,
        alignSelf: "stretch",
        justifyContent: "center",
        alignItems: "center",
    },
    logo: {
        height: 60,
        width: 120,
    },
})
export default Header;
