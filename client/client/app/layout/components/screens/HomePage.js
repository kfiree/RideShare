import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../Context';
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
import Header from "../nested/Header";
import Footer from "../nested/Footer";
// import MenuOpen from "./../n ested/MenuOpen";
import { Gravatar, GravatarApi } from 'react-native-gravatar';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;


const sizeHeader = windowWidth / 4;
function HomePage() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../assets/fonts/Calibri-Regular.ttf'),
    });
    const { userData, setUserData, setPages, pages, location, setLocation, userMarker2, setUserMarker2, checkCityForUniversity, cities, universities, loadingPopup, setLoadingPopup } = useContext(Context)
    useEffect(() => {
        setPages({
            ...pages,
            curr: 'HomePage',
        })
    }, []);
    console.log('pages', pages)
    const navigation = useNavigation();
    console.log('location HomePage', location);
    if (!fonts) {
        return null;
    } else {
        return (

            <SafeAreaView>
                <Header />
                <ScrollView style={{ marginTop: 40 }}>
                    <View style={styles.container}>
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
                        <View style={styles.context}>
                            <Text style={styles.title}>בוקר טוב, {userData.firstName}</Text>
                            <Text style={styles.text}>איך נוסעים היום?</Text>
                        </View>
                        <View style={styles.pikup}>
                            <View style={styles.pikup_drive}>
                                <TouchableOpacity style={{ width: "100%" }} onPress={() => {
                                    setPages({
                                        ...pages,
                                        prev: 'HomePage',
                                    })
                                    navigation.navigate("SearchDrive")
                                }}>
                                    <Text numberOfLines={2} style={styles.textInput}>רוצה שיאספו אותי</Text>
                                </TouchableOpacity>
                            </View>
                            <View style={styles.pikup_passenger}>
                                <TouchableOpacity style={{ width: "100%" }} onPress={() => {
                                    setPages({
                                        ...pages,
                                        prev: 'HomePage',
                                    })
                                    navigation.navigate("CreateDrive")
                                }}>
                                    <Text numberOfLines={2} style={styles.textInput}>רוצה לאסוף מישהו</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </View>
                </ScrollView>
                {/* <Footer style={{ position: "absolute", justifyContent: "flex-end" }} /> */}
            </SafeAreaView>
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
        flex: 2,
        justifyContent: 'center',
        alignItems: 'center'
    },
    imagePerson: {
        width: 150,
        height: 150,
        borderRadius: 150 / 2,
        overflow: "hidden",
        borderWidth: 1,
        borderColor: colors.black
    },
    context: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 40,
        // fontFamily: "CalibriRegular",
        marginTop: 10,
        fontWeight: 'bold',
    },
    text: {
        color: colors.black,
        fontSize: 30,
        // fontFamily: "CalibriRegular",
        textAlign: 'center',
    },
    pikup: {
        flexDirection: "row",
        justifyContent: 'space-around',
        width: (windowWidth),
        height: 250,
        marginTop: 30,
    },
    pikup_drive: {
        justifyContent: 'center',
        alignItems: 'center',
        width: '45%',
        height: 130,
        backgroundColor: colors.white,
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlign: 'center',
        opacity: 0.9,
    },
    pikup_passenger: {
        justifyContent: 'center',
        alignItems: 'center',
        width: '45%',
        height: 130,
        backgroundColor: colors.white,
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlign: 'center',
        opacity: 0.9,
    },
    textInput: {
        color: colors.black,
        fontSize: Platform.OS === "android" ? 25 : 30,
        // fontFamily: "CalibriRegular",
        textAlign: 'center',
        fontWeight: 'bold',
    },
})
export default HomePage;