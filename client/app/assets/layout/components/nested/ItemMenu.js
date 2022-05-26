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
import { MaterialCommunityIcons, Entypo, MaterialIcons, FontAwesome5, FontAwesome, AntDesign, EvilIcons, Ionicons, Feather } from '@expo/vector-icons';
import token from './../../../../../logicApp'

const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;
const checkForIcon = (name) => {

    switch (name) {

        case 'HomePage':
            return icons.HomePage
            break;
        case 'ChatDrive':
            return icons.ChatDrive
            break;
        case 'CreateDrive':
            return icons.CreateDrive
            break;
        case 'Login':
            return icons.Login
            break;
        case 'MyBenefits':
            return icons.MyBenefits
            break;
        case 'MyDrives':
            return icons.MyDrives
            break;
        case 'MyChats':
            return icons.MyChats
            break;
        case 'MySuggestions':
            return icons.MySuggestions
            break;

        case 'MySuggestionsPassenger':
            return icons.MySuggestionsPassenger
            break;
        case 'Notification':
            return icons.Notification
            break;
        case 'Payments':
            return icons.Payments
            break;
        case 'Profile':
            return icons.Profile
            break;
        case 'Rating':
            return icons.Rating
            break;
        case 'Registration':
            return icons.Registration
            break;
        case 'SearchDrive':
            return icons.SearchDrive
            break;
        case 'Setting':
            return icons.Setting
            break;
        case 'WelcomeScreen':
            return icons.WelcomeScreen
            break;
        default:
            return icons.HomePage

    }

}
const icons = {
    HomePage: <FontAwesome5 name="home" size={45} color={colors.black} />,
    ChatDrive: <Entypo name="chat" size={45} color={colors.black} />,
    CreateDrive: <FontAwesome name="bus" size={45} color={colors.black} />,
    Login: <AntDesign name="login" size={45} color={colors.black} />,
    MyBenefits: <FontAwesome5 name="gift" size={45} color={colors.black} />,
    MyDrives: <FontAwesome name="list-alt" size={45} color={colors.black} />,
    MyChats: <AntDesign name="wechat" size={45} color={colors.black} />,
    MySuggestions: <MaterialCommunityIcons name="offer" size={45} color={colors.black} />,
    MySuggestionsPassenger: <MaterialCommunityIcons name="seat-passenger" size={45} color={colors.black} />,
    Notification: <Ionicons name="notifications" size={45} color={colors.black} />,
    Payments: <MaterialIcons name="payment" size={45} color={colors.black} />,
    Profile: <Ionicons name="person-circle" size={45} color={colors.black} />,
    Rating: <Feather name="star" size={45} color={colors.black} />,
    Registration: <MaterialIcons name="app-registration" size={45} color={colors.black} />,
    SearchDrive: <FontAwesome name="search" size={45} color={colors.black} />,
    Setting: <AntDesign name="setting" size={45} color={colors.black} />,
    WelcomeScreen: <FontAwesome5 name="home" size={45} color={colors.black} />,
};


const ItemMenu = ({ namePage, navigateToPage, icon }) => {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const navigation = useNavigation();
    if (!fonts) {
        return null;
    } else {
        return (
            <TouchableOpacity onPress={() => {

                if (namePage === "התנתקות") {
                    token.removeTokenFromStorage();
                }
                navigation.navigate(navigateToPage)
            }}>
                <View style={styles.item}>
                    <View style={styles.menuItem}>
                        <Text style={styles.textMenuItem}>{namePage}</Text>
                    </View>

                    <View style={styles.iconItem}>
                        {checkForIcon(navigateToPage)}
                    </View>
                </View>
            </TouchableOpacity>
        );
    }
}
const styles = StyleSheet.create({
    item: {
        width: "100%",
        height: 50,
        borderBottomColor: colors.black,
        borderBottomWidth: 2,
        flexDirection: 'row',
    },
    menuItem: {
        width: "80%",
        height: "100%",
        paddingRight: 10,
        justifyContent: 'center',
        alignItems: 'flex-end',
    },
    iconItem: {
        width: "20%",
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
    },
    textMenuItem: {
        color: colors.black,
        fontSize: (Platform.OS === 'android') ? 25 : 30,
        fontFamily: "CalibriRegular",
    },
})
export default ItemMenu
