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
    Animated,
    Dimensions,
    Button,
    Alert,
    Platform,
    StatusBar,
    CheckBox,
} from 'react-native';
// import { AppContext } from './../../../../../App'
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { Entypo } from '@expo/vector-icons';
import axios from "axios";
import { useFonts } from 'expo-font';

import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import token from '../../../../../logicApp';
import Loading from "./../nested/Loading";
import users from "./../../../../../requests/Users";
import { Gravatar, GravatarApi } from 'react-native-gravatar';


import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import SelectDropdown from 'react-native-select-dropdown'


/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const genders = ["זכר", "נקבה"];
const modelCars = ["אאודי", "אוטוביאנקי", "אולדסמוביל", "אוסטין", "אופל", "אלפא רומיאו", "ב.מ.וו", "ביואיק", "בנלי", "ג`יאו", "דודג`", "דייהו", "דייהטסו", "הונדה", "וולוו", "טויוטה", "טלבו סימקה", "יגואר", "יונדאי", "לאדה", "לנציה", "מאזדה", "מיצובישי", "מרצדס", "ניסן", "סאאב", "סובארו", "סוזוקי", "סיאט", "סיטרואן", "סמארט", "סקודה", "פג`ו", "פולקסווגן", "פונטיאק", "פורד", "פורשה", "פיאט", "קאדילאק", "קאיה", "קרייזלר", "רובר", "רנו", "שברולט",];
const colorsCars = ["כחול", "אדום", "כתום", "צהוב", "ירוק", "סגול", "לבן", "שחור", "אדום", "חום"];


function Profile() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn, cities, universities, checkCityForUniversity } = useContext(Context)
    const [validDate, setValidDate] = useState(true);
    const navigation = useNavigation();

    const [data, setData] = React.useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        phoneNumber: "",
        gender: "",
        university: "",
        degree: "",
        modelCar: "",
        dateOfBirth: "",
        colorCar: "",


        isValidFirstName: true,
        isValidLastName: true,
        isValidPhoneNumber: true,
        isValidEmail: true,
        isValidPassword: true,
        isValidPassword2: true,
        isValidGender: true,
        isValidUniversity: true,
        isValidDegree: true,
        isValidModelCar: true,
        isValidDateOfBirth: true,
        isValidColorCar: true
    });

    const inputChange = (name, val) => {
        switch (name) {
            case "firstName":
                setData({
                    ...data,
                    firstName: val
                });
                return;
            case "lastName":
                setData({
                    ...data,
                    lastName: val
                });
                return;
            case "phoneNumber":
                setData({
                    ...data,
                    phoneNumber: val
                });
                return;
            case "email":
                setData({
                    ...data,
                    email: val
                });
                return;
            case "password":
                setData({
                    ...data,
                    password: val
                });
                return;
            case "password2":
                setData({
                    ...data,
                    password2: val
                });
                return;
            case "gender":
                if (val === "זכר" || val === "נקבה") {
                    setData({
                        ...data,
                        gender: val
                    });
                }
                return;
            case "university":
                setData({
                    ...data,
                    university: checkCityForUniversity(val)
                });
                return;
            case "degree":
                setData({
                    ...data,
                    degree: val
                });
                return;
            case "modelCar":
                setData({
                    ...data,
                    modelCar: val
                });
                return;
            case "colorCar":
                setData({
                    ...data,
                    colorCar: val
                });
                return;
            default:
                break;
        };
    }

    const handleValidUser = (name, val) => {
        switch (name) {
            case "firstName":
                if (val.length > 2) {
                    setData({
                        ...data,
                        isValidFirstName: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidFirstName: false
                    });
                }
                return;
            case "lastName":
                if (val.length > 2) {
                    setData({
                        ...data,
                        isValidLastName: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidLastName: false
                    });
                }
                return;
            case "phoneNumber":
                if (val.length == 10 && !isNaN(val)) {
                    setData({
                        ...data,
                        isValidPhoneNumber: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidPhoneNumber: false
                    });
                }
                return;
            case "email":
                if (val.length > 4) {
                    setData({
                        ...data,
                        isValidEmail: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidEmail: false
                    });
                }
                return;
            case "password":
                if (val.length > 4) {
                    setData({
                        ...data,
                        isValidPassword: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidPassword: false
                    });
                }
                return;
            case "password2":
                if (val > 4 && data.password === val) {
                    setData({
                        ...data,
                        isValidPassword2: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidPassword2: false
                    });
                }
                return;
            case "gender":
                if (genders.includes(val)) {
                    setData({
                        ...data,
                        isValidGender: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidGender: false
                    });
                }
                return;
            case "university":
                setData({
                    ...data,
                    isValidUniversities: true
                });
                return;
            case "degree":
                if (val.length > 2) {
                    setData({
                        ...data,
                        isValidDegree: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidDegree: false
                    });
                }
                return;
            case "modelCar":
                if (modelCars.includes(val)) {
                    setData({
                        ...data,
                        isValidModelCar: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidModelCar: false
                    });
                }
                return;

            case "colorCar":
                if (colorsCars.includes(val)) {
                    setData({
                        ...data,
                        isValidColorCar: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidColorCar: false
                    });
                }
                return;
            case "dateOfBirth":
                if (val.length > 0) {
                    setData({
                        ...data,
                        isValidDateOfBirth: true
                    });
                } else {
                    setData({
                        ...data,
                        isValidDateOfBirth: false
                    });
                }
                return;
            default:
                break;
        };
    }



    const handleChangedDate = (val) => {
        const regexddmmyyyy = /^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$/;

        if (regexddmmyyyy.test(val)) {
            setValidDate(true);
            setData({
                ...data,
                isValidDateOfBirth: true
            })
        } else {
            // setValidDate(false);
            setData({
                ...data,
                isValidDateOfBirth: false
            })
        }
    };



    const onSubmit = async () => {
        try {
            setLoadingPopup(true)
            if (userData.token !== "") {
                const user = {
                    firstName: (data.firstName),
                    lastName: data.lastName,
                    email: data.email,
                    password: data.password,
                    phoneNumber: data.phoneNumber,
                    gender: data.gender,
                    university: data.university,
                    degree: data.degree,
                    modelCar: data.modelCar,
                    dateOfBirth: data.dateOfBirth,
                    colorCar: data.colorCar
                }
                const res = await users.updateUser(user, userData.token)
                if (res !== null) {
                    setUserData({
                        ...userData,
                        id: res.email,
                        firstName: res.firstName,
                        lastName: res.lastName,
                        email: res.email,
                        phoneNumber: res.phoneNumber,
                        gender: res.gender,
                        imageUniversity: (res.universityImage) ? res.universityImage : "",
                        imageId: res.avatar,
                        university: res.university,
                        degree: res.degree,
                        rating: res.rating,
                        numRating: res.ratingNum,
                        numDrives: 0,
                        modelCar: (res.carModel !== "") ? res.carModel : "",
                        colorCar: (res.colorCar !== "") ? res.colorCar : "",
                        dateOfBirth: (res.carColor !== "") ? res.carColor : "",
                    })
                    setLoadingPopup(false);
                } else {
                    throw "addDrive failed";
                }
            } else {
                throw "no token"
            }
        } catch (error) {
            //console.log(error)
            setLoadingPopup(false)
        }
    }

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
                            <Text style={styles.title}>פרופיל אישי</Text>


                            <View style={styles.boxImage}>
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
                                    {/* <Image
                                        resizeMode="cover"
                                        source={require('./../../../images/images/men3.jpg')}
                                        style={{
                                            height: "100%",
                                            width: "100%",
                                        }}
                                    /> */}
                                </View>
                            </View>

                            <View style={styles.rowWithMargin}>
                                <View style={styles.row2}>
                                    <Text style={styles.Rating2}>({userData.numRating})</Text>
                                    <Text style={styles.Rating}>{userData.rating}</Text>
                                    <Image
                                        style={styles.star}
                                        resizeMode='center'
                                        source={require('./../../../images/icons/fullStar.png')} />
                                </View>
                            </View>



                            <View style={styles.rowBox}>
                                <Text style={styles.textPopup2}>נסיעות</Text>
                                <Text style={styles.textPopupBold}> {userData.numDrives} </Text>
                                <Text style={styles.textPopup}>הידדת {userData.firstName}! ביצעת כבר</Text>
                            </View>

                            <KeyboardAvoidingView style={styles.form}>
                                <View style={styles.inputsBox}>

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        // placeholder={userData.firstName}
                                        placeholder={(userData.firstName === "") ? "מספר טל'" : userData.firstName}
                                        style={data.isValidFirstName ? styles.inputText : styles.errorBorder}
                                        onChangeText={(text) => inputChange("firstName", text)}
                                        onEndEditing={(e) => handleValidUser("firstName", e.nativeEvent.text)}
                                    />
                                    {data.isValidFirstName ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        // placeholder={userData.lastName}
                                        placeholder={(userData.lastName === "") ? "מספר טל'" : userData.lastName}
                                        style={
                                            data.isValidLastName ? styles.inputText : styles.errorBorder
                                        }
                                        onChangeText={(text) => inputChange("lastName", text)}
                                        onEndEditing={(e) => handleValidUser("lastName", e.nativeEvent.text)}
                                    />
                                    {data.isValidLastName ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        // placeholder={userData.email}
                                        placeholder={(userData.email === "") ? "מספר טל'" : userData.email}
                                        style={
                                            data.isValidEmail ? styles.inputText : styles.errorBorder
                                        }
                                        autoComplete={"email"}
                                        onChangeText={(text) => inputChange("email", text)}
                                        onEndEditing={(e) => handleValidUser("email", e.nativeEvent.text)}
                                    />
                                    {data.isValidEmail ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        // placeholder={userData.phoneNumber}
                                        autoComplete={"phoneNumber"}
                                        style={
                                            data.isValidPhoneNumber ? styles.inputText : styles.errorBorder
                                        }
                                        placeholder={(userData.phoneNumber === "") ? "מספר טל'" : userData.phoneNumber}
                                        onChangeText={(text) => inputChange("phoneNumber", text)}
                                        onEndEditing={(e) => handleValidUser("phoneNumber", e.nativeEvent.text)}
                                    />
                                    {data.isValidPhoneNumber ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }
                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        secureTextEntry={true} placeholder="סיסמה"
                                        autoComplete={"password"}
                                        style={
                                            data.isValidPassword ? styles.inputText : styles.errorBorder
                                        }
                                        onChangeText={(text) => inputChange("password", text)}
                                        onEndEditing={(e) => handleValidUser("password", e.nativeEvent.text)}
                                    />
                                    {data.isValidPassword ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        secureTextEntry={true} placeholder="סיסמה"
                                        autoComplete={"password"}
                                        style={
                                            data.isValidPassword2 ? styles.inputText : styles.errorBorder
                                        }
                                        onChangeText={(text) => inputChange("password2", text)}
                                        onEndEditing={(e) => handleValidUser("password2", e.nativeEvent.text)}
                                    />
                                    {data.isValidPassword2 ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <SelectDropdown
                                        defaultValue={userData.gender}
                                        data={genders}
                                        buttonStyle={styles.inputText}
                                        buttonTextStyle={styles.textInputStyle}
                                        defaultButtonText={"מין"}
                                        onSelect={(selectedItem, index) => { inputChange("gender", selectedItem) }}
                                        buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                                        rowTextForSelection={(item, index) => { return item }} />

                                    <SelectDropdown
                                        defaultValue={userData.university}
                                        data={universities}
                                        buttonStyle={styles.inputText}
                                        buttonTextStyle={styles.textInputStyle}
                                        // defaultButtonText={"מוסד לימודים"}
                                        placeholder={(userData.university === "") ? "מוסד לימודים" : userData.university}

                                        onSelect={(selectedItem, index) => { inputChange("university", selectedItem) }}
                                        buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                                        rowTextForSelection={(item, index) => { return item }} />


                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        placeholder={userData.degree}
                                        style={
                                            data.isValidDegree ? styles.inputText : styles.errorBorder
                                        }
                                        onChangeText={(text) => inputChange("degree", text)}
                                        onEndEditing={(e) => handleValidUser("degree", e.nativeEvent.text)}
                                    />
                                    {data.isValidDegree ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }

                                    <SelectDropdown
                                        defaultValue={userData.modelCar}
                                        data={modelCars}
                                        buttonStyle={styles.inputText}
                                        buttonTextStyle={styles.textInputStyle}
                                        defaultButtonText={"סוג רכב"}
                                        onSelect={(selectedItem, index) => { inputChange("modelCar", selectedItem) }}
                                        buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                                        rowTextForSelection={(item, index) => { return item }} />

                                    <SelectDropdown
                                        defaultValue={userData.colorCar}
                                        data={colorsCars}
                                        buttonStyle={styles.inputText}
                                        buttonTextStyle={styles.textInputStyle}
                                        defaultButtonText={"צבע רכב"}
                                        onSelect={(selectedItem, index) => { inputChange("colorCar", selectedItem) }}
                                        buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                                        rowTextForSelection={(item, index) => { return item }} />

                                    <TextInput
                                        placeholderTextColor={colors.silverText}
                                        placeholder={(userData.dateOfBirth === "") ? "תאריך לידה" : userData.dateOfBirth}
                                        style={
                                            data.isValidDateOfBirth ? styles.inputText : styles.errorBorder
                                        }
                                        onChangeText={(text) => inputChange("dateOfBirth", text)}
                                        onEndEditing={(e) => handleChangedDate(e.nativeEvent.text)}
                                    // onEndEditing={(e) => handleValidUser("dateOfBirth", e.nativeEvent.text)}
                                    />
                                    {data.isValidDateOfBirth ? null :
                                        <Animated.View style={{ width: "100%", }}>
                                            <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                                        </Animated.View>
                                    }


                                </View>

                                <View style={styles.submitForm}>
                                    <TouchableOpacity style={styles.submitInput}
                                        onPress={() => {
                                            onSubmit();
                                        }}
                                    >
                                        <View style={{
                                            justifyContent: 'center',
                                            alignItems: 'center',
                                            alignContent: 'center'
                                        }}>
                                            <Text style={styles.btnSubmit}>עדכן פרטים</Text>
                                        </View>
                                    </TouchableOpacity>
                                </View>

                            </KeyboardAvoidingView>
                        </View>
                    </ScrollView>

                    <Footer style={{ position: "absolute", justifyContent: "flex-end" }} />
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
    logoContainer: {
        alignItems: 'center',
    },
    title: {
        fontSize: 35,
        marginTop: 20,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    logo: {
        width: 250,
        height: 200,
    },
    form: {
        width: (windowWidth - 40),
        marginTop: 25,
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
        fontSize: (Platform.OS === 'android') ? 25 : 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.input,
        marginTop: 10,
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
        marginStart: 20
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
    submitForm: {
        marginBottom: 60,
        marginTop: 25,
        justifyContent: 'flex-end',
        alignItems: 'center',

    },
    validation: {
        textAlign: 'center',
        color: colors.importantNotification,
        fontSize: 20,
        fontFamily: "CalibriRegular",
    },
    submitInput: {
        width: (windowWidth - 100),
        height: 50,
        fontSize: 30,
        color: colors.black,
        fontWeight: 'bold',
        textAlign: 'center',
        fontFamily: "CalibriRegular",
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
    },
    uploadImages: {
        height: 100,
        marginBottom: 55,
        flexDirection: "row",
        justifyContent: 'space-between',
        width: "100%",
        marginTop: 5,
    },
    lastInput: {
        height: 100,
        marginBottom: 50,
        width: "100%",
    },
    profilePic: {
        width: ((windowWidth - 60) / 2),
        height: 150,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.input,
        marginTop: 5,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        textAlignVertical: "top",
        alignItems: 'center',
    },
    IDpic: {
        width: ((windowWidth - 60) / 2),
        height: 150,
        fontSize: 30,
        textAlign: 'center',
        fontFamily: "CalibriRegular",
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.input,
        marginTop: 5,
        borderBottomLeftRadius: 15,
        borderBottomRightRadius: 15,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15,
        textAlignVertical: "top",
        alignItems: 'center',
    },
    btnSubmit: {
        fontWeight: 'bold',
        fontSize: 30,
        fontFamily: "CalibriRegular",
        // marginTop: 7,

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
        marginTop: (windowWidth / 2),
        justifyContent: 'center',
        alignItems: 'center',
    },
    titlePopup: {
        fontSize: (Platform.OS === 'android') ? 20 : 24,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    textPopup: {
        fontSize: (Platform.OS === 'android') ? 20 : 24,
        fontFamily: "CalibriRegular",
    },
    textPopup2: {
        fontSize: (Platform.OS === 'android') ? 20 : 24,
        fontFamily: "CalibriRegular",
    },
    textPopupBold: {
        fontSize: (Platform.OS === 'android') ? 20 : 24,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    confirmButton: {
        fontSize: 25,
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
        fontFamily: "CalibriRegular",
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
        backgroundColor: colors.confirmButton,
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
    imagePerson: {
        width: 150,
        height: 150,
        borderRadius: 150 / 2,
        overflow: "hidden",
        borderWidth: 1,
        borderColor: colors.black
    },
    boxImage: {
        justifyContent: "center",
        alignItems: "center",
        marginTop: 20,
    },
    rowWithMargin: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
    },
    row2: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 20,
        width: "50%",
        height: 50,
    },
    Rating2: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
    },
    Rating: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        fontWeight: '700',
    },
    star: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        height: 30,
        width: 30,
        marginLeft: 10,
    },
    textInputStyle: {
        alignItems: 'center',
        textAlignVertical: "top",
        fontFamily: "CalibriRegular",
        fontSize: 30,
        textAlign: 'center',
        color: colors.silverText
        // color: colors.placeholderTextColor
    },
    errorBorder: {
        width: (windowWidth - 40),
        height: 50,
        fontFamily: "CalibriRegular",
        fontSize: 30,
        textAlign: 'center',
        borderColor: colors.importantNotification,
        borderWidth: 2,
        backgroundColor: colors.input,
        marginTop: 10,
        borderRadius: 15,
        textAlignVertical: "top",
        alignItems: 'center',
    },
})
export default Profile;

