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



const sizeHeader = windowWidth / 4;
function MySuggestions() {
    const navigation = useNavigation();
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
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
                    <View style={styles.content2}>
                        <Text style={styles.title}>ההצעות שלי</Text>
                    </View>
                    <ScrollView style={{ marginTop: 20 }}>
                        <View style={styles.container}>
                            <View style={styles.imagePerson}>
                                <Image
                                    resizeMode="cover"
                                    source={require('./../../../images/images/woman3.jpg')}
                                    style={{
                                        height: "100%",
                                        width: "100%",
                                        padding: 5
                                    }}
                                />
                            </View>

                            <View style={styles.content}>
                                <Text style={styles.title}>בוקר טוב, נטע</Text>
                                <Text style={styles.text}>איזה הצעות תרצה לראות?</Text>
                            </View>

                            <View style={styles.pikup}>


                                <View style={styles.pikup_drive}>
                                    <TouchableOpacity style={{ width: "100%" }} onPress={() => {
                                        navigation.navigate("MySuggestionsDrive")

                                    }}>
                                        <Text numberOfLines={2} style={styles.textInput}>טרמפ</Text>
                                    </TouchableOpacity>
                                </View>



                                <View style={styles.pikup_passenger}>
                                    <TouchableOpacity style={{ width: "100%" }} onPress={() => {
                                        navigation.navigate("MySuggestionsPassenger")

                                    }}>
                                        <Text numberOfLines={2} style={styles.textInput}>נסיעה</Text>
                                    </TouchableOpacity>
                                </View>

                            </View>

                        </View>
                    </ScrollView>
                    <Footer style={{ position: "absolute", justifyContent: "flex-end" }} />
                </SafeAreaView>
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
    content: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    content2: {
        justifyContent: 'center',
        alignItems: 'center',
        // marginTop: (sizeHeader + 20),
    },
    title: {
        fontSize: 40,
        marginTop: 10,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    text: {
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center'
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
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        fontWeight: 'bold',
    },
})
export default MySuggestions;