import React, { useState, useLayoutEffect, createContext, useContext, useEffect } from 'react';
import { Context } from '../../../../../Context';
import { ContextOtherOffer } from '../../../../../ContextOtherOffer';
import axios from "axios";

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
import { Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import colors from '../../layout'
import requests from './../../../../../request';
import { useFonts } from 'expo-font';
import drives from './../../../../../requests/Drives'
import token from './../../../../../logicApp';
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import Loading from "./../nested/Loading";
import DateTimePicker from '@react-native-community/datetimepicker';
import SelectDropdown from 'react-native-select-dropdown'

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;
function SearchDrive() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    const {
        sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn,
        cities, universities, checkCityForUniversity, searchDrive, setSearchDrive, search, setSearch
    } = useContext(Context)

    const [src, setSrc] = useState("");
    const [srcType, setSrcType] = useState("university");

    const [dest, setDest] = useState("");
    const [destType, setDestType] = useState("university");

    const [date, setDate] = useState("");
    const [validDate, setValidDate] = useState(true);

    const [hour, setHour] = useState("");
    const [validHour, setValidHour] = useState(true);
    function isNum(val) {
        return !isNaN(val)
    }
    const handleChangedDate = (val) => {
        if (val.length == 2 && isNum(val)) {
            //console.log("val = " + val)
            val += "/";
        }
        const regexddmmyyyy = /^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\d\d$/;

        if (regexddmmyyyy.test(val)) {
            setValidDate(true);
            setDate(val);
        } else {
            setValidDate(false);
            setDate(val);
        }
    };

    const handleChangedHour = (val) => {
        const regexhhmm = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;

        if (regexhhmm.test(val)) {
            setValidHour(true);
            setHour(val);
        } else {
            setValidHour(false);
            setHour(val);
        }
    };



    const srcInputChange = (value) => {
        if (destType === 'university') {
            setSrc(checkCityForUniversity(value))
            return;
        }
        setSrc(value)
    }

    const destInputChange = (value) => {
        if (srcType === 'university') {
            setDest(checkCityForUniversity(value))
            return;
        }
        setDest(value)
    }



    const onSubmit = async () => {
        if (validHour && validDate && src !== "" && dest !== "") {
            try {
                setLoadingPopup(true)
                if (userData.token !== "") {
                    const searchData = {
                        date: date,
                        time: hour,
                        from: src,
                        to: dest,
                    }
                    const res = await drives.searchDriveAsPassenger(searchData, userData.token)
                    setSearchDrive(res)
                    setLoadingPopup(false);
                    setSearch(true);
                    navigation.navigate("MySuggestionsPassenger");
                } else {
                    throw "no token"
                }
            } catch (error) {
                //console.log(error)
                setLoadingPopup(false)
                setSearch(false);
            }
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
                    <ScrollView style={styles.bigBox}>
                        <View style={styles.content}>
                            <Text style={styles.title}>חיפוש נסיעה</Text>
                        </View>

                        <View style={styles.formBlock}>

                            <View style={styles.textAndInput}>
                                <View style={
                                    validDate ? styles.leftBlock : styles.leftBlockError
                                }>
                                    <TextInput placeholder="DD/MM/YYYY" style={styles.inputText}
                                        onEndEditing={(e) => handleChangedDate(e.nativeEvent.text)}
                                    />
                                    <View style={styles.logoInput}>
                                        <Entypo name="calendar" size={30} color={colors.search} />
                                    </View>
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
                                        onPress={() => {
                                            setSrcType("university")
                                            // navigation.navigate("MapRide", {
                                            //     isDriver: false,
                                            // })
                                        }}
                                    >
                                        <FontAwesome name="university" size={30} color={colors.search} />
                                    </TouchableOpacity>

                                    <SelectDropdown
                                        data={
                                            srcType === "university" ? universities : cities
                                        }
                                        buttonStyle={styles.inputTextWithTwoLogos}
                                        defaultButtonText={"עיר/אונ"}
                                        onSelect={(selectedItem, index) => {
                                            srcInputChange(selectedItem)
                                        }}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            setSrcType("city")
                                            // navigation.navigate("MapRide", {
                                            //     isDriver: false,
                                            // })
                                        }}
                                    >
                                        <Entypo name="location-pin" size={35} color={colors.search} />
                                    </TouchableOpacity>
                                </View>

                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>מאיפה?</Text>
                                </View>
                            </View>


                            <View style={styles.textAndInput}>

                                <View style={styles.leftBlock}>
                                    <TouchableOpacity style={styles.logoInputLeft}
                                        onPress={() => {
                                            setDestType("university")
                                        }}
                                    >
                                        <FontAwesome name="university" size={30} color={colors.search} />
                                    </TouchableOpacity>

                                    <SelectDropdown
                                        data={
                                            destType === "university" ? universities : cities
                                        }
                                        buttonStyle={styles.inputTextWithTwoLogos}
                                        defaultButtonText={"עיר/אונ"}
                                        onSelect={(selectedItem, index) => {
                                            destInputChange(selectedItem)
                                        }}
                                    />
                                    <TouchableOpacity style={styles.logoInput}
                                        onPress={() => {
                                            setDestType("city")
                                        }}
                                    >
                                        <Entypo name="location-pin" size={35} color={colors.search} />
                                    </TouchableOpacity>
                                </View>

                                <View style={styles.rightBlock}>
                                    <Text style={styles.text}>לאן?</Text>
                                </View>
                            </View>
                            <View style={styles.submitForm}>
                                <TouchableOpacity style={styles.submitInput}
                                    onPress={() => {
                                        onSubmit()
                                    }}
                                >
                                    <View style={{
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        alignContent: 'center'
                                    }}>
                                        <Text style={styles.btnSubmit}>חפש לי נסיעה</Text>

                                    </View>
                                </TouchableOpacity>
                            </View>


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
        marginTop: 10,
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 40,
        marginTop: 10,
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
    },
    text: {
        color: colors.black,
        fontSize: 24,
        textAlign: 'center',
        fontFamily: "CalibriRegular",
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
        textAlignVertical: "center",
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
    inputTextSmall: {
        width: "50%",
        height: "100%",
        fontSize: 20,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        textAlignVertical: "top",
        alignItems: 'flex-start',
        backgroundColor: colors.white
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
        fontFamily: "CalibriRegular",
        color: "#000000",
        fontSize: 22,
    },
    link: {
        color: "#000000",
        fontSize: 22,
        textDecorationLine: 'underline',
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',
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
    // checkBoxText: {
    //     color: colors.black,
    //     fontSize: 21,
    //     fontFamily: "CalibriRegular",
    // },
    checkBoxInput: {
        marginStart: 10,
    },
    marginTop: {
        marginTop: 20,
        justifyContent: 'center',
        alignItems: 'center'
    },

})
export default SearchDrive;