import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
import { useFonts } from 'expo-font';
// import { RNDateTimePicker } from '@react-native-community/datetimepicker';
// import DatePicker from 'react-native-date-picker'


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
// import { GooglePlacesAutocomplete } from 'react-native-google-places-autocomplete';
import { Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import CreateDrivePopup from "./../nested/CreateDrivePopup";
// import DataTimePicker from './../nested/DataTimePicker'
// import DatePickerInput from './../nested/DatePickerInput'
import SelectDropdown from 'react-native-select-dropdown'
import requests from './../../../../../request';
import axios from "axios";
import drives from './../../../../../requests/Drives'

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;
import Loading from "./../nested/Loading";

import token from './../../../../../logicApp';


const sizeHeader = windowWidth / 4;
function CreateDrive() {
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn, cities, universities, checkCityForUniversity, suggestionsDriver, setSuggestionsDriver } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });

    const [date, setDate] = useState(new Date())
    const [open, setOpen] = useState(false)



    const navigation = useNavigation();
    const [showPopup, setShowPopup] = useState(false);
    const [validation, setValidation] = useState({
        validDate: false,
        validHour: false,
        validSrc: false,
        validDest: false,
        validNumSeats: true,
        validPrice: true,
    });
    const [srcLocation, setSrcLocation] = useState("אריאל");
    const [dstLocation, setDstLocation] = useState("אריאל");
    const [numSeats, setNumSeats] = useState(4);
    const [price, setPrice] = useState(5);
    const [src, setSrc] = useState("אריאל");

    const [srcType, setSrcType] = useState("city");

    const [dest, setDest] = useState("אריאל");
    const [destType, setDestType] = useState("city");

    // const [date, setDate] = useState();
    const [validDate, setValidDate] = useState(true);

    const [hour, setHour] = useState();
    const [validHour, setValidHour] = useState(true);

    const [citySrc, setCitySrc] = useState(true);
    const [cityDst, setCityDst] = useState(true);

    const [srcText, setSrcText] = useState("עיר");
    const [dstText, setDstText] = useState("עיר");

    const changeSrcType = () => {
        setCitySrc(!citySrc)
        citySrc ? setSrcText("אוניברסיטה") : setSrcText("עיר")
    }

    const changeDstType = () => {
        setCityDst(!cityDst)
        cityDst ? setDstText("אוניברסיטה") : setDstText("עיר")
    }

    const upNum = (name) => {
        if (name === "numSeats") {
            let newVal = numSeats + 1;
            setNumSeats(newVal)
            setValidation({
                ...validation,
                validNumSeats: true,
            });
        } else {
            let newVal = price + 1;
            setPrice(newVal);
            setValidation({
                ...validation,
                validPrice: true,
            });
        }
    }
    const downNum = (name) => {
        if (name === "numSeats") {
            if (numSeats > 0) {
                let newVal = numSeats - 1;
                setNumSeats(newVal);
                setValidation({
                    ...validation,
                    validNumSeats: true,
                });
            }
        } else {
            if (price > 0) {
                let newVal = price - 1;
                setPrice(newVal)
                setValidation({
                    ...validation,
                    validPrice: true,
                });
            }
        }
    }
    const handleChangedDate = (val) => {
        const regexddmmyyyy = /^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$/;

        if (regexddmmyyyy.test(val)) {
            setValidDate(true);
            setDate(val);
            setValidation({
                ...validation,
                validDate: true,
            });
        } else {
            setValidDate(false);
            setValidation({
                ...validation,
                validDate: false,
            });
        }
    };
    const handleChangedHour = (val) => {
        const regexhhmm = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;

        if (regexhhmm.test(val)) {
            setValidHour(true);
            setValidation({
                ...validation,
                validHour: true,
            });
            setHour(val);
        } else {
            setValidHour(false);
            setValidation({
                ...validation,
                validHour: false,
            });
            setHour(val);
        }
    };
    // const srcInputChange = (value) => {
    //     if (srcType === 'university') {
    //         setSrc(checkCityForUniversity(value))
    //         setValidation({
    //             ...validation,
    //             validSrc: true,
    //         });
    //         return;
    //     }
    //     setSrc(value)
    //     setValidation({
    //         ...validation,
    //         validSrc: true,
    //     });
    // }
    // const destInputChange = (value) => {
    //     if (destType === 'university') {
    //         setDest(checkCityForUniversity(value))
    //         setValidation({
    //             ...validation,
    //             validDest: true,
    //         });
    //         return;
    //     }
    //     setDest(value)
    //     setValidation({
    //         ...validation,
    //         validDest: true,
    //     });
    //     //console.log("dest after change = ", dest)
    // }

    const onSubmit = async () => {
        if (validation.validDate && validation.validHour && validation.validSrc && validation.validDest && validation.validNumSeats && validation.validPrice) {

            try {
                setLoadingPopup(true)
                if (userData.token !== "") {
                    const DriveData = {
                        date: date,
                        time: hour,
                        from: src,
                        to: dest,
                        numofSeats: numSeats,
                        price: price,
                        returnDrive: false,
                    }
                    if (await drives.addDrive(DriveData, userData.token)) {
                        setLoadingPopup(false);
                        setShowPopup(true)
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
        } else {
            await sendNotificationToClient("השדות שלך לא תקינים או ריקים")
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
                        <View style={styles.content}>
                            <Text style={styles.title}>הוספת נסיעה</Text>
                        </View>
                        <View style={styles.formBlock}>
                            <View style={styles.textAndInput}>
                                <View style={
                                    validDate ? styles.leftBlock : styles.leftBlockError
                                }>
                                    <TextInput placeholder="DD/MM/YYYY" style={styles.inputText}
                                        onEndEditing={(e) => handleChangedDate(e.nativeEvent.text)}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            console.log("get date");
                                        }}>
                                        <Entypo name="calendar" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                </View>

                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>מתי?</Text>
                                </View>
                            </View>
                            <View style={styles.textAndInput}>
                                <View style={
                                    validHour ? styles.leftBlock : styles.leftBlockError
                                }>
                                    <TextInput placeholder="HH/MM" style={styles.inputText}
                                        onEndEditing={(e) => handleChangedHour(e.nativeEvent.text)}
                                    />
                                    <View style={styles.logoInput}>
                                        <View style={styles.topIcon}>
                                            <AntDesign
                                                style={{ alignSelf: 'center' }}
                                                name="caretup" size={18}
                                                color={colors.search} />
                                        </View>
                                        <View style={styles.bottomIcon}>
                                            <AntDesign
                                                style={{ alignSelf: 'center' }}
                                                name="caretdown"
                                                size={18}
                                                color={colors.search} />
                                        </View>
                                    </View>
                                </View>
                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>באיזו שעה?</Text>
                                </View>
                            </View>

                            <View style={styles.textAndInput}>
                                <View style={styles.leftBlock}>
                                    <TouchableOpacity style={styles.logoInputLeft}
                                        onPress={() => { changeSrcType() }}>
                                        {citySrc ?
                                            <Entypo name="location-pin" size={35} color={colors.search} />
                                            : <FontAwesome name="university" size={30} color={colors.search} />
                                        }

                                    </TouchableOpacity>
                                    <SelectDropdown
                                        data={
                                            citySrc ? cities : universities
                                        }
                                        buttonStyle={styles.inputTextWithTwoLogos}
                                        defaultButtonText={srcText}
                                        onSelect={(selectedItem, index) => {
                                            citySrc ?
                                                setSrc(selectedItem)
                                                : setSrc(checkCityForUniversity(selectedItem))

                                            setSrcLocation(selectedItem)

                                            setValidation({
                                                ...validation,
                                                validSrc: true,
                                            });
                                        }}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            // navigation.navigate("MapRide", {
                                            //     isDriver: true,
                                            //     Location: srcLocation,
                                            // })
                                        }}
                                    >
                                        <Entypo name="map" size={35} color={colors.search}
                                        // onPress={() => { navigation.navigate("MapRide", { isDriver: true, Location: srcLocation, }) }}
                                        />
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>מאיפה?</Text>
                                </View>
                            </View>
                            <View style={styles.textAndInput}>
                                <View style={styles.leftBlock}>
                                    <TouchableOpacity style={styles.logoInputLeft}
                                        onPress={() => { changeDstType() }}>
                                        {cityDst ?
                                            <Entypo name="location-pin" size={35} color={colors.search} />
                                            : <FontAwesome name="university" size={30} color={colors.search} />
                                        }

                                    </TouchableOpacity>
                                    <SelectDropdown
                                        data={
                                            cityDst ? cities : universities
                                        }
                                        buttonStyle={styles.inputTextWithTwoLogos}
                                        defaultButtonText={dstText}
                                        onSelect={(selectedItem, index) => {
                                            cityDst ?
                                                setDest(selectedItem)
                                                : setDest(checkCityForUniversity(selectedItem))

                                            setDstLocation(selectedItem)

                                            setValidation({
                                                ...validation,
                                                validDest: true,
                                            });
                                        }}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            // navigation.navigate("MapRide", {
                                            //     isDriver: true,
                                            //     Location: dstLocation,
                                            // })
                                        }}
                                    >
                                        <Entypo name="map" size={35} color={colors.search} />
                                        {/* onPress={() => {navigation.navigate("MapRide", {isDriver: true,})}}
                                        /> */}
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>לאן?</Text>
                                </View>
                            </View>
                            {/* <View style={styles.textAndInput}>
                                <View style={styles.leftBlock}>
                                    <TouchableOpacity style={styles.logoInputLeft}
                                        onPress={() => {
                                            setDestType("university")
                                        }}>
                                        <FontAwesome name="university" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                    <SelectDropdown
                                        data={
                                            destType === "university" ? universities : cities
                                        }
                                        buttonStyle={styles.inputTextWithTwoLogos}
                                        defaultButtonText={"עיר/אונ"}
                                        onSelect={(selectedItem, index) => {
                                            if (destType === 'university') {
                                                setDest(checkCityForUniversity(selectedItem))
                                            } else {
                                                setDest(selectedItem)
                                            }
                                            setValidation({
                                                ...validation,
                                                validDest: true,
                                            });
                                        }}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            setDestType("city")
                                        }}>
                                        <Entypo name="location-pin" size={35} color={colors.search} />
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>לאן?</Text>
                                </View>
                            </View> */}
                            <View style={styles.textAndInput}>
                                <View style={styles.leftBlockSmall}>
                                    <TouchableOpacity style={styles.logoInputSmallRight}
                                        onPress={() => {
                                            downNum("numSeats")
                                        }}>
                                        <Entypo name="minus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                    <View style={styles.inputTextSmall}>
                                        <Text style={styles.textNumber}>{numSeats}</Text>
                                    </View>

                                    <TouchableOpacity style={styles.logoInputSmallLeft}
                                        onPress={() => {
                                            upNum("numSeats")
                                        }}
                                    >
                                        <AntDesign name="plus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.tBlockBig}>
                                    <Text style={styles.text}>מס' מושבים?</Text>
                                </View>
                            </View>

                            <View style={styles.textAndInput}>
                                <View style={styles.leftBlockSmall}>
                                    <TouchableOpacity style={styles.logoInputSmallRight}
                                        onPress={() => {
                                            downNum("price")
                                        }}>
                                        <Entypo name="minus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                    <View style={styles.inputTextSmall}>
                                        <Text style={styles.textNumber}>{price}</Text>
                                    </View>
                                    <TouchableOpacity style={styles.logoInputSmallLeft}
                                        onPress={() => {
                                            upNum("price")
                                        }}
                                    >
                                        <AntDesign name="plus" size={30} color={colors.search} />
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.tBlockBig}>
                                    <Text style={styles.text}>כמה עולה לנוסע?</Text>
                                </View>
                            </View>
                            <View style={styles.marginTop}>
                                <Text style={styles.text}>מחיר ממוצע לנסיעה דומה: 7 ש"ח</Text>
                            </View>
                            <View style={styles.checkBox}>
                                <Text style={styles.checkBoxText}>זכור נסיעה זו כנסיעה קבועה</Text>
                                <BouncyCheckbox
                                    size={25}
                                    fillColor={colors.success}
                                    unfillColor={colors.white}
                                    style={styles.checkBoxInput}
                                    iconStyle={{
                                        borderColor: colors.search
                                    }}
                                    textStyle={{
                                        color: colors.black,
                                        textDecorationLine: "none",
                                    }}
                                    onPress={() => { }} />
                            </View>
                            <View style={styles.submitForm}>
                                <TouchableOpacity style={styles.submitInput}
                                    onPress={() => {
                                        onSubmit()

                                    }}>
                                    <View style={{
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        alignContent: 'center'
                                    }}>
                                        <Text style={styles.btnSubmit}>פרסם נסיעה</Text>
                                    </View>
                                </TouchableOpacity>
                            </View>


                        </View>
                    </ScrollView>

                    {!showPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <CreateDrivePopup showPopup={showPopup} setShowPopup={setShowPopup} />
                        </View>
                    }

                    {!loadingPopup ? null :
                        <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
                            <Loading />
                        </View>}

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
    content: {
        marginTop: 10,
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        marginTop: 10,
        fontWeight: 'bold',
    },
    text: {
        color: colors.black,
        fontSize: 24,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        fontWeight: '400'
    },
    formBlock: {
        width: "100%",
        marginTop: 10,
        height: (windowHeight - (2 * sizeHeader)),
        maxHeight: (windowHeight - (4 * sizeHeader)),
        maxWidth: (windowWidth - 60),
        marginLeft: 30,
    },
    textAndInput: {
        flexDirection: "row",
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        height: 40,
        marginTop: 10,
        width: "100%",
    },
    textAndInputSmall: {
        flexDirection: "row",
        justifyContent: 'center',
        alignItems: 'center',
        textAlign: 'center',
        height: 50,
        marginTop: 15,
        width: "90%",
    },
    inputText: {
        width: "75%",
        height: "100%",
        fontSize: Platform.OS === "android" ? 14 : 16,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        textAlignVertical: 'center',
        alignItems: 'flex-start',
    },
    inputTextWithTwoLogos: {
        width: "50%",
        height: "100%",
        fontSize: Platform.OS === "android" ? 14 : 16,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        justifyContent: 'center',
        textAlignVertical: "center",
    },
    inputTextDate: {
        width: "75%",
        height: "100%",
        fontSize: Platform.OS === "android" ? 14 : 16,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        textAlignVertical: 'center',
        justifyContent: 'center',
        alignItems: 'center'
    },
    inputTextSmall: {
        width: "50%",
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: colors.white
    },
    textNumber: {
        textAlignVertical: "top",
        fontSize: 20,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
    },
    inputTextIMG: {
        width: "30%",
        height: "100%",
    },
    checkBoxText: {
        width: "40%",
        height: "100%",
        color: colors.black,
        fontSize: 15,
        fontFamily: "CalibriRegular",
        alignSelf: 'flex-end',
        backgroundColor: colors.black
    },
    leftBlock: {
        borderColor: colors.search,
        borderLeftWidth: 2,
        borderRightWidth: 2,
        borderTopWidth: 2,
        borderBottomWidth: 2,
        borderBottomLeftRadius: 5,
        borderBottomRightRadius: 5,
        borderTopLeftRadius: 5,
        borderTopRightRadius: 5,
        backgroundColor: colors.white,
        flexDirection: "row",
        width: "60%",
        height: "100%",
    },
    leftBlockError: {
        borderColor: colors.error,
        borderLeftWidth: 2,
        borderRightWidth: 2,
        borderTopWidth: 2,
        borderBottomWidth: 2,
        borderBottomLeftRadius: 5,
        borderBottomRightRadius: 5,
        borderTopLeftRadius: 5,
        borderTopRightRadius: 5,
        backgroundColor: colors.white,
        flexDirection: "row",
        width: "60%",
        height: "100%",
    },
    logoInputLeft: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderRightWidth: 2,
        paddingTop: 1,
        paddingBottom: 1,
    },
    leftBlockSmall: {
        borderColor: colors.search,
        borderLeftWidth: 2,
        borderRightWidth: 2,
        borderTopWidth: 2,
        borderBottomWidth: 2,
        borderBottomLeftRadius: 5,
        borderBottomRightRadius: 5,
        borderTopLeftRadius: 5,
        borderTopRightRadius: 5,
        backgroundColor: colors.white,
        flexDirection: "row",
        width: "40%",
        height: "100%",
        justifyContent: 'flex-start',
    },
    rightBlock: {
        justifyContent: 'center',
        alignItems: 'flex-end',
        width: "40%",
        height: "100%",
    },
    tBlockBig: {
        justifyContent: 'center',
        alignItems: 'flex-end',
        width: "60%",
        height: "100%",
    },
    logoInput: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderLeftWidth: 2,
        paddingTop: 1,
        paddingBottom: 1,
    },
    logoInputSmallRight: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderRightWidth: 2,
        paddingTop: 1,
        paddingBottom: 1,
    },
    logoInputSmallLeft: {
        justifyContent: 'center',
        alignItems: 'center',
        height: "100%",
        width: "25%",
        borderColor: colors.search,
        borderLeftWidth: 2,
        paddingTop: 1,
        paddingBottom: 1,
    },
    topIcon: {
        width: "100%",
        height: "50%",
        borderColor: colors.search,
        borderBottomWidth: 2,
    },
    bottomIcon: {
        width: "100%",
        height: "50%",
    },
    alignCenter: {
        width: "100%",
        justifyContent: "center",
        alignItems: "center",
    },
    text2: {
        marginTop: 30,
        fontStyle: 'italic',
        color: "#000000",
        fontSize: 22,
        fontFamily: "CalibriRegular",
    },
    link: {
        color: "#000000",
        fontSize: 22,
        fontFamily: "CalibriRegular",
        textDecorationLine: 'underline',
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 500,
    },
    submitInput: {
        width: (windowWidth - 120),
        height: 50,
        marginTop: 45,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
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
        alignItems: 'center',
        alignContent: "center",
    },
    btnSubmit: {
        fontWeight: 'bold',
        fontSize: 30,
        fontFamily: "CalibriRegular",
    },
    checkBox: {
        color: colors.black,
        marginTop: 10,
        width: "100%",
        flexDirection: "row",
        justifyContent: 'center',
        alignItems: 'center',
    },
    checkBoxText: {
        color: colors.black,
        fontSize: 21,
        fontFamily: "CalibriRegular",
    },
    checkBoxInput: {
        marginStart: 10,
    },
    marginTop: {
        marginTop: 20,
        justifyContent: 'center',
        alignItems: 'center'
    },
    popup: {
        height: 300,
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
        marginTop: (windowWidth / 1.6),
        justifyContent: 'center',
        alignItems: 'center',
    },
    titlePopup: {
        fontSize: (Platform.OS === 'android') ? 35 : 40,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    textPopup: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        marginTop: 5,
    },
    confirmButton: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
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
        width: "60%",
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

})
export default CreateDrive;