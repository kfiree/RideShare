import React, { useState, useLayoutEffect, createContext, useContext } from 'react';
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
import { Ionicons, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import { useFonts } from 'expo-font';


import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import { Colors } from 'react-native/Libraries/NewAppScreen';
import Loading from "./../nested/Loading";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



const sizeHeader = windowWidth / 4;
function Rating() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)

    const [numStars, setNumStars] = useState(100);
    const [textToInput, setTextToInput] = useState("יש לכם מה להוסיף? מוזמנים לכתוב כאן...");

    const handleStar = (num) => {
        setNumStars(num);
        //console.log("num: ", numStars);
    }
    const handleTextToInput = (text) => {
        setTextToInput(text);
    }

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
                    <ScrollView >

                        <View style={styles.content}>
                            <Text style={styles.title}>איך הייתה הנסיעה שלך?</Text>
                        </View>

                        <View style={styles.container}>
                            <View style={styles.rowCenter}>
                                <View style={styles.stars}>
                                    <TouchableOpacity onPress={() => {
                                        handleStar(1)
                                    }}>
                                        <Image source={require('./../../../images/images/fullStar.png')} />
                                    </TouchableOpacity>

                                    <TouchableOpacity onPress={() => {
                                        handleStar(2)
                                    }}>
                                        <Image source={require('./../../../images/images/fullStar.png')} />
                                    </TouchableOpacity>

                                    <TouchableOpacity onPress={() => {
                                        handleStar(3)
                                    }}>
                                        <Image source={require('./../../../images/images/fullStar.png')} />
                                    </TouchableOpacity>

                                    <TouchableOpacity onPress={() => {
                                        handleStar(4)
                                    }}>
                                        <Image source={require('./../../../images/images/fullStar.png')} />
                                    </TouchableOpacity>

                                    <TouchableOpacity onPress={() => {
                                        handleStar(5)
                                    }}>
                                        <Image source={require('./../../../images/images/fullStar.png')} />
                                    </TouchableOpacity>


                                </View>
                            </View>

                            <View style={styles.rowCenter}>
                                <View style={styles.greatBox}>
                                    <Text style={styles.greatText}>נפלא!</Text>
                                </View>
                            </View>

                            <View style={styles.rowCenter}>
                                <View style={styles.greatBox}>
                                    <Text style={styles.complimentText}>שלחו מחמאות לנהג/ת </Text>
                                </View>
                            </View>


                            <View style={styles.rowCenter}>
                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInput}
                                        onPress={() => {
                                            handleTextToInput("נוהג/ת בצורה בטיחותית");
                                        }}>
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>נוהג/ת בצורה בטיחותית</Text>

                                        </View>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInput}
                                        onPress={() => {
                                            handleTextToInput("זמינות גבוהה באפליקציה");
                                        }}>
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>זמינות גבוהה באפליקציה</Text>

                                        </View>
                                    </TouchableOpacity>
                                </View>
                            </View>


                            <View style={styles.rowCenter}>
                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInputSmall}
                                        onPress={() => {
                                            handleTextToInput("הרכב נקי ומסודר");
                                        }}>
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>הרכב נקי ומסודר</Text>

                                        </View>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInputSmall}
                                        onPress={() => {
                                            handleTextToInput("מוזיקה טובה");
                                        }}>
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>מוזיקה טובה</Text>

                                        </View>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInputSmall}
                                        onPress={() => {
                                            handleTextToInput("הגיע/ה בזמן");
                                        }}>
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>הגיע/ה בזמן</Text>

                                        </View>
                                    </TouchableOpacity>
                                </View>
                            </View>


                            <View style={styles.rowCenter}>
                                <View style={{ marginTop: 20, borderBottomColor: colors.silverText, borderBottomWidth: 1, width: "100%" }}>
                                    <TextInput
                                        placeholder={textToInput}
                                        style={styles.addComplimentText} />
                                    {/* <Text style={styles.addComplimentText}></Text> */}
                                </View>
                            </View>


                            <View style={styles.rowCenter}>
                                <View style={styles.ButtonConfirm}>
                                    <TouchableOpacity onPress={() => {
                                        Alert.alert("Submit Form", "Need to check values with DB and alert MSG to client", [
                                            { text: "yes", onPress: () => console.log("yes") },
                                            { text: "no", onPress: () => console.log("no") },
                                        ]);
                                        navigation.navigate("HomePage")
                                    }}>
                                        <Text style={styles.confirmButton}>שלח דירוג</Text>
                                    </TouchableOpacity>
                                </View>
                            </View>

                            <View style={styles.rowCenter}>
                                <TouchableOpacity onPress={() => {
                                    Alert.alert("Submit Form", "Need to check values with DB and alert MSG to client", [
                                        {
                                            text: "yes", onPress: () => console.log("yes")
                                        },
                                        { text: "no", onPress: () => console.log("no") },
                                    ]);
                                    navigation.navigate("HomePage")
                                }}>
                                    <Text style={styles.cancelButton}>ביטול</Text>
                                </TouchableOpacity>
                            </View>
                        </View>
                    </ScrollView>


                    <Footer style={{ position: "absolute", justifyContent: "flex-end" }} />
                </SafeAreaView>
                {
                    !loadingPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <Loading />
                        </View>
                }
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
        flex: 1,
        marginTop: 25,
        justifyContent: 'center',
        alignItems: 'center'
    },
    content: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
    },
    rowCenter: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 15,
    },
    stars: {
        width: "90%",
        height: 80,
        flexDirection: 'row',
        justifyContent: 'space-between',
        // backgroundColor: "black"
    },
    title: {
        fontSize: (Platform.OS === 'android') ? 25 : 35,
        marginTop: 10,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    greatBox: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    greatText: {
        fontSize: (Platform.OS === 'android') ? 20 : 25,
        // marginTop: 10,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    complimentText: {
        fontSize: (Platform.OS === 'android') ? 20 : 25,
        fontFamily: "CalibriRegular",
        // marginTop: 10,
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: 10,
    },
    submitInput: {
        height: 35,
        marginTop: 5,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderWidth: 1,
        backgroundColor: colors.confirmButton,
        borderRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    submitInputSmall: {
        height: 35,
        marginTop: 5,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderWidth: 1,
        backgroundColor: colors.confirmButton,
        borderRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    btnSubmit: {
        padding: 5,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
        color: colors.white,
        fontSize: (Platform.OS === "android") ? 14 : 17,
    },
    addComplimentText: {
        fontWeight: 'bold',
        fontSize: (Platform.OS === "android") ? 14 : 14,
        fontFamily: "CalibriRegular",
        color: colors.silverText,
        borderColor: colors.silverText,
        textAlign: 'center'
    },
    ButtonConfirm: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        marginTop: 20,
        backgroundColor: colors.primary,
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
    confirmButton: {
        fontSize: (Platform.OS === "android") ? 25 : 30,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
        color: colors.black,
        marginRight: 10,
        textAlign: 'center',
    },
    cancelButton: {
        fontSize: (Platform.OS === "android") ? 25 : 30,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },
})
export default Rating;