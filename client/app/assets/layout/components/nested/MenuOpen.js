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
import colors from '../../layout'

import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import { FontAwesome5, EvilIcons, Ionicons, Feather } from '@expo/vector-icons';
import ItemMenu from './ItemMenu';
import { Gravatar, GravatarApi } from 'react-native-gravatar';

const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;
const MenuOpen = () => {
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
                <SafeAreaView>
                    <View style={styles.ProfileDetails}>
                        <View style={styles.closeMenu}>
                            <TouchableOpacity onPress={() => {
                                navigation.navigate("HomePage");
                            }}>
                                <EvilIcons name="close" size={50} color={colors.silverTextDrive} />
                            </TouchableOpacity>
                        </View>
                        <TouchableOpacity style={styles.imageUser}
                            onPress={() => {
                                navigation.navigate("Profile");
                            }}>
                            <View style={styles.profileBox}>
                                <View style={styles.nameUser}>
                                    <Text style={styles.namePerson}>{userData.firstName} {userData.lastName}</Text>
                                </View>


                                {/* <View > */}
                                <View style={styles.imagePerson}>
                                    <Gravatar options={{
                                        email: userData.email,
                                        parameters: { "size": "200", "d": "mm" },
                                        secure: true
                                    }}
                                        style={{
                                            height: "100%",
                                            width: "100%",
                                            padding: 5
                                        }} />
                                </View>
                                {/* </View> */}
                            </View>
                        </TouchableOpacity>
                    </View>


                    <View style={styles.menuItems}>
                        <ScrollView style={{ marginBottom: 50 }}>

                            <ItemMenu namePage={"עמוד הבית"} navigateToPage={"HomePage"} />
                            <ItemMenu namePage={"הנסיעות שלי"} navigateToPage={"MyDrives"} />
                            <ItemMenu namePage={"הוספת נסיעה"} navigateToPage={"CreateDrive"} />
                            <ItemMenu namePage={"חיפוש נסיעה"} navigateToPage={"SearchDrive"} />
                            <ItemMenu namePage={"ההצעות שלי"} navigateToPage={"MySuggestions"} />

                            {/* <ItemMenu namePage={"ההצעות שלי 2"} navigateToPage={"MySuggestionsPassenger"} /> */}
                            {/* <ItemMenu namePage={"צ'אט נסיעה"} navigateToPage={"ChatDrive"} />
                            <ItemMenu namePage={"התחברות"} navigateToPage={"Login"} />
                            <ItemMenu namePage={"ההטבות שלי"} navigateToPage={"MyBenefits"} />
                            <ItemMenu namePage={"הצאטים שלי"} navigateToPage={"MyChats"} />
                            <ItemMenu namePage={"התראות"} navigateToPage={"Notification"} />
                            <ItemMenu namePage={"תשלומים"} navigateToPage={"Payments"} />
                            <ItemMenu namePage={"פרופיל"} navigateToPage={"Profile"} />
                            <ItemMenu namePage={"דירוג"} navigateToPage={"Rating"} />
                            <ItemMenu namePage={"הרשמה"} navigateToPage={"Registration"} />
                            <ItemMenu namePage={"הגדרות"} navigateToPage={"Setting"} />
                            <ItemMenu namePage={"מסך עמודים"} navigateToPage={"WelcomeScreen"} />
                            <ItemMenu namePage={"התנתקות"} navigateToPage={"Registration"} /> */}
                        </ScrollView>
                    </View>
                </SafeAreaView>
            </View>
        );
    }
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        position: 'absolute',
        width: (windowWidth - sizeHeader),
        height: (windowHeight),
        top: 0,
        right: 0,
        backgroundColor: colors.menuOpen,
        zIndex: 100000,
    },
    ProfileDetails: {
        width: "100%",
        height: 220,
        borderBottomColor: colors.black,
        borderBottomWidth: 2,
    },
    closeMenu: {
        width: "100%",
        height: 50,
        marginTop: 50,
    },
    profileBox: {
        width: "100%",
        flexDirection: 'row',
    },
    menuItems: {
        width: "100%",
        height: 500,
    },

    imageUser: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    nameUser: {
        width: "70%",
        height: 100,
        justifyContent: 'flex-end',
        alignItems: 'center',
        flexDirection: 'row',
    },
    imagePerson: {
        width: 80,
        height: 80,
        borderRadius: 40,
        overflow: "hidden",
        borderWidth: 2,
        borderColor: colors.black,

    },
    namePerson: {
        fontSize: (Platform.OS === 'android') ? 28 : 32,
        fontFamily: "CalibriRegular",
        color: colors.black,
        fontWeight: 'bold',
        marginRight: 25,
    },
})
export default MenuOpen
