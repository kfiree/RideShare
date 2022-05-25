import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context'; drives
import drives from '../../../../../requests/Drives';
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
import { FontAwesome5, MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import Loading from "./../nested/Loading";

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import { Colors } from 'react-native/Libraries/NewAppScreen';
import moment from 'moment';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function MyDrives() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [myDrives, setMyDrives] = useState(null);
    const [run, setRun] = useState(false);
    // useLayoutEffect(async () => {
    //     try {
    //         if (isSignedIn && userData.token.length > 0) {
    //             setLoadingPopup(true);
    //             const res = await drives.getUserDrives(userData.token);
    //             setMyDrives(res);
    //             // //console.log("setMySuggestions(res);" + JSON.stringify(res))
    //             if (res.length > 0) {
    //                 setLoadingPopup(false);
    //             } else {
    //                 setLoadingPopup(false);
    //                 setNoResultPopup(true);
    //             }
    //         } else {
    //             throw "user is no login"
    //         }
    //     } catch (error) {
    //         //console.log("error = " + error)
    //         setLoadingPopup(false);
    //         // navigation.navigate("Registration")
    //     }
    // }, [])



    useEffect(async () => {
        try {
            if (isSignedIn && userData.token.length > 0) {
                setLoadingPopup(true);
                const res = await drives.getUserDrives(userData.token);
                if (res !== null) {
                    setMyDrives(res);
                    // //console.log("res = " + JSON.stringify(res))
                    // //console.log("res" + JSON.stringify(res))
                    // setRun(true)
                    setLoadingPopup(false);
                } else {
                    setLoadingPopup(false);
                }
                // //console.log("setMySuggestions(res);" + JSON.stringify(res))
                // if (res.length > 0) {

                // } else {
                //     setLoadingPopup(false);
                //     // setNoResultPopup(true);
                // }
            } else {
                throw "user is no login"
            }
        } catch (error) {
            //console.log("error = " + error)
            setLoadingPopup(false);
            // navigation.navigate("Registration")
        }
    }, [])




    const getDay = (day) => {
        switch (day) {
            case "0":
                return "ראשון"
                break;
            case "1":
                return "שני"
                break;
            case "2":
                return "שלישי"
                break;
            case "3":
                return "רביעי"
                break;
            case "4":
                return "חמישי"
                break;
            case "5":
                return "שישי"
                break;
            case "6":
                return "שבת"
                break;
            default:
                break;
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
                    <Header style={{ ImageBackground: require('../../../images/images/cover.png') }} />
                    <ScrollView>


                        <View style={styles.logoContainer}>
                            <Text style={styles.title}>הנסיעות שלי</Text>
                        </View>




                        {myDrives &&
                            myDrives.map((obj) => {
                                if (obj[1].length >= 1) {
                                    if (obj[1].length === 1) {
                                        //console.log("1 drive")
                                        const drive = obj[1][0]
                                        return (
                                            <View key={obj[0]} style={styles.container}>
                                                <View style={styles.blockEmpty}>
                                                    <View style={styles.row}>
                                                        <View >
                                                            <Text style={styles.textDay}>{getDay(drive.day)}</Text>
                                                        </View>
                                                        <View style={styles.line}></View>
                                                    </View>

                                                    <View style={styles.row}>
                                                        <View style={styles.leftInBlock}>
                                                            <View style={styles.circleBC_blue} >
                                                                <Text style={{ color: colors.white }}>{drive.date.split("/")[0]}</Text>
                                                            </View>
                                                        </View>
                                                        <View style={styles.centerInBlock}>

                                                            <View style={styles.rightBlock}>
                                                                <View style={styles.hours}>
                                                                    <Text style={styles.hoursText}>{moment(drive.time).format("HH:MM")}</Text>
                                                                </View>
                                                                <View style={styles.location}>
                                                                    <Text style={styles.locationText}>{drive.from}</Text>
                                                                    <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                                    <Text style={styles.locationText}>{drive.to}</Text>
                                                                </View>
                                                            </View>
                                                        </View>
                                                        <View style={styles.rightInBlock}>
                                                            <TouchableOpacity
                                                                style={styles.circleBC_white}
                                                                onPress={() => {
                                                                    navigation.navigate("CreateDrive");
                                                                }}>
                                                                <AntDesign name="plus" size={25} color={colors.black} />
                                                            </TouchableOpacity>
                                                        </View>
                                                    </View>
                                                </View>
                                            </View>
                                        )
                                    } else {
                                        const drive1 = obj[1][0]
                                        const drive2 = obj[1][1]
                                        return (
                                            <View key={obj[0]} style={styles.container}>
                                                <View style={styles.blockEmpty}>
                                                    <View style={styles.row}>
                                                        <View >
                                                            <Text style={styles.textDay}>{getDay(drive1.day)}</Text>
                                                        </View>
                                                        <View style={styles.line}></View>
                                                    </View>

                                                    <View style={styles.row}>
                                                        <View style={styles.leftInBlock}>
                                                            <View style={styles.circleBC_blue} >
                                                                <Text style={{ color: colors.white }}>{drive1.date.split("/")[0]}</Text>
                                                            </View>
                                                        </View>
                                                        <View style={styles.centerInBlock}>

                                                            <View style={styles.rightBlock}>
                                                                <View style={styles.hours}>
                                                                    <Text style={styles.hoursText}>{moment(drive1.time).format("HH:MM")}</Text>
                                                                </View>
                                                                <View style={styles.location}>
                                                                    <Text style={styles.locationText}>{drive1.from}</Text>
                                                                    <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                                    <Text style={styles.locationText}>{drive1.to}</Text>
                                                                </View>
                                                            </View>
                                                            <View style={styles.leftBlock}>
                                                                <View style={styles.hours}>
                                                                    <Text style={styles.hoursText}>{moment(drive2.time).format("HH:MM")}</Text>
                                                                </View>
                                                                <View style={styles.location}>
                                                                    <Text style={styles.locationText}>{drive2.from}</Text>
                                                                    <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                                    <Text style={styles.locationText}>{drive2.to}</Text>
                                                                </View>
                                                            </View>
                                                        </View>
                                                        <View style={styles.rightInBlock}>

                                                        </View>
                                                    </View>
                                                </View>
                                            </View>

                                        )
                                    }
                                } else {
                                    const date = obj[0]
                                    const time = date.split("-")
                                    return (
                                        <View key={obj[0]} style={styles.container}>
                                            <View style={styles.blockEmpty}>
                                                <View style={styles.row}>
                                                    <View style={styles.textInEndLine}>
                                                        <Text style={styles.textDay}>{getDay(moment(date, "DD-MM-YYYY").format("d"))}</Text>
                                                    </View>
                                                    <View style={styles.line}></View>
                                                </View>

                                                <View style={styles.row}>
                                                    <View style={styles.leftInBlock}>
                                                        <View style={styles.circleBC_blue} >
                                                            <Text style={{ color: colors.white }}>{date.split("-")[0]}</Text>
                                                        </View>
                                                    </View>
                                                    <View style={styles.centerInBlock}>
                                                        <View>
                                                            <Text style={styles.textEmpty}>לא נוסע היום</Text>
                                                        </View>
                                                    </View>
                                                    <View style={styles.rightInBlock}>
                                                        <TouchableOpacity
                                                            style={styles.circleBC_white}
                                                            onPress={() => {
                                                                navigation.navigate("CreateDrive");
                                                            }}>
                                                            <AntDesign name="plus" size={25} color={colors.black} />
                                                        </TouchableOpacity>
                                                    </View>
                                                </View>
                                            </View>
                                        </View>
                                    )
                                }
                            })
                        }
                        {/* 
                    <View style={styles.container}>
                        <View style={styles.blockEmpty}>
                            <View style={styles.row}>
                                <View style={styles.textInEndLine}>
                                    <Text style={styles.textDay}>חמישי</Text>
                                </View>
                                <View style={styles.line}></View>
                            </View>

                            <View style={styles.row}>
                                <View style={styles.leftInBlock}>
                                    <View style={styles.circleBC_blue} >
                                        <Text style={{ color: colors.white }}>29</Text>
                                    </View>
                                </View>
                                <View style={styles.centerInBlock}>
                                    <View>
                                        <Text style={styles.textEmpty}>לא נוסע היום</Text>
                                    </View>
                                </View>
                                <View style={styles.rightInBlock}>
                                    <TouchableOpacity
                                        style={styles.circleBC_white}
                                        onPress={() => {
                                            navigation.navigate("CreateDrive");
                                        }}>
                                        <AntDesign name="plus" size={25} color={colors.black} />
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </View>





                    <View style={styles.container}>
                        <View style={styles.blockEmpty}>
                            <View style={styles.row}>
                                <View >
                                    <Text style={styles.textDay}>חמישי</Text>
                                </View>
                                <View style={styles.line}></View>
                            </View>

                            <View style={styles.row}>
                                <View style={styles.leftInBlock}>
                                    <View style={styles.circleBC_blue} >
                                        <Text style={{ color: colors.white }}>29</Text>
                                    </View>
                                </View>
                                <View style={styles.centerInBlock}>

                                    <View style={styles.rightBlock}>
                                        <View style={styles.hours}>
                                            <Text style={styles.hoursText}>16:30</Text>
                                        </View>
                                        <View style={styles.location}>
                                            <Text style={styles.locationText}>אריאל</Text>
                                            <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                            <Text style={styles.locationText}>בית</Text>
                                        </View>
                                    </View>
                                </View>
                                <View style={styles.rightInBlock}>
                                    <TouchableOpacity
                                        style={styles.circleBC_white}
                                        onPress={() => {
                                            navigation.navigate("CreateDrive");
                                        }}>
                                        <AntDesign name="plus" size={25} color={colors.black} />
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                    </View>




                    <View style={styles.container}>
                        <View style={styles.blockEmpty}>
                            <View style={styles.row}>
                                <View >
                                    <Text style={styles.textDay}>חמישי</Text>
                                </View>
                                <View style={styles.line}></View>
                            </View>

                            <View style={styles.row}>
                                <View style={styles.leftInBlock}>
                                    <View style={styles.circleBC_blue} >
                                        <Text style={{ color: colors.white }}>29</Text>
                                    </View>
                                </View>
                                <View style={styles.centerInBlock}>

                                    <View style={styles.rightBlock}>
                                        <View style={styles.hours}>
                                            <Text style={styles.hoursText}>16:30</Text>
                                        </View>
                                        <View style={styles.location}>
                                            <Text style={styles.locationText}>אריאל</Text>
                                            <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                            <Text style={styles.locationText}>בית</Text>
                                        </View>
                                    </View>
                                    <View style={styles.leftBlock}>
                                        <View style={styles.hours}>
                                            <Text style={styles.hoursText}>16:30</Text>
                                        </View>
                                        <View style={styles.location}>
                                            <Text style={styles.locationText}>אריאל</Text>
                                            <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                            <Text style={styles.locationText}>בית</Text>
                                        </View>
                                    </View>
                                </View>
                                <View style={styles.rightInBlock}>

                                </View>
                            </View>
                        </View>
                    </View>
 */}






                    </ScrollView>

                    <Footer />
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
        // marginTop: 130,
        marginBottom: 50,
        alignItems: 'center',
    },
    container: {
        flexDirection: 'row',
        flex: 1,
        marginBottom: 10,
        justifyContent: 'center',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center',
        marginTop: 2,
    },
    title: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },
    blockFull: {
        width: "100%",
        height: 80,
        justifyContent: 'center',
        alignItems: 'center',
    },
    blockEmpty: {
        width: "100%",
        height: 80,
        justifyContent: 'center',
        alignItems: 'center',
    },
    line: {
        backgroundColor: colors.silverHR,
        width: "85%",
        height: 2,
    },
    textDay: {
        fontSize: 15,
        fontFamily: "CalibriRegular",
        color: colors.addDrive,
        fontWeight: 'bold',
        textAlign: 'right',
        marginRight: 5,
    },
    circleBC_white: {
        width: 30,
        height: 30,
        backgroundColor: colors.white,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',

    },
    circleBC_blue: {
        width: 30,
        height: 30,
        backgroundColor: colors.blueCircle,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderBottomLeftRadius: 30,
        borderBottomRightRadius: 30,
        borderTopLeftRadius: 30,
        borderTopRightRadius: 30,
        justifyContent: 'center',
        alignItems: 'center',
        marginLeft: 15,
    },
    rightInBlock: {
        alignSelf: 'center',
        justifyContent: 'center',
        alignItems: 'center',
        width: "10%",
        // backgroundColor: 'blue',
    },
    centerInBlock: {
        alignSelf: 'center',
        width: "80%",
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row',
    },
    leftInBlock: {
        alignSelf: 'flex-end',
        width: "10%",
    },
    textEmpty: {
        fontWeight: '500',
        fontSize: 25,
        fontFamily: "CalibriRegular",
        color: colors.silverText,
    },
    rightBlock: {
        width: "50%",
        height: 50,
        justifyContent: 'center',
        alignItems: 'center',
    },
    leftBlock: {
        width: "50%",
        justifyContent: 'center',
        alignItems: 'center',
        height: 50,
    },
    hours: {
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row',
    },
    location: {
        marginTop: 5,
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row',
    },
    hoursText: {
        color: colors.white,
        fontSize: 20,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold'
    },
    locationText: {
        color: colors.silverTextDrive,
        fontSize: 18,
        fontFamily: "CalibriRegular",
    },
    fontAwesome: {
        marginRight: 5,
        marginLeft: 5,
    },
})
export default MyDrives;