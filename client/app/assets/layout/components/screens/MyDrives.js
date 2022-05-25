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
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn, detailsDrive, seDetailsDrive } = useContext(Context)
    const [myDrives, setMyDrives] = useState(null);
    const [run, setRun] = useState(false);
    let key1 = 0;
    const [ScrollToBottomX, setScrollToBottomX] = useState(100)


    const scrollViewRefX = useRef(null);


    useEffect(async () => {
        try {
            if (isSignedIn && userData.token.length > 0) {
                setLoadingPopup(true);
                const res = await drives.getUserDrives(userData.token);
                if (res !== null) {
                    setMyDrives(res);
                    setLoadingPopup(false);
                } else {
                    setLoadingPopup(false);
                }
            } else {
                throw "user is no login"
            }
        } catch (error) {
            // console.log("error = " + error)
            setLoadingPopup(false);
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
    const goToDriveChat = async (driveId) => {
        // Alert.alert("Submit Form", driveId, [
        //     {
        //         text: "Ok", onPress: () => console.log("yes")
        //     }
        // ]);
        // seDetailsDrive(
        //     {
        //         chatMsg: [

        //         ],
        //         driveData: {
        //             chatID: "aaYDnntfE6e4OObWuoqn",
        //             date: "12/12/2021",
        //             day: "0",
        //             driveID: "se8K3cWkKfd6SzOMXXuo",
        //             from: "חיפה",
        //             time: "2021-12-12T12:00:00+00:00",
        //             to: "ירושלים",
        //         },
        //         participants: [
        //             {
        //                 avatar: "//www.gravatar.com/avatar/83bf538d40cbe9dcd10281f58b8e99d9?s=200&r=pg&d=mm",
        //                 email: "driver@mail.com",
        //                 name: "driver",
        //             },
        //             {
        //                 avatar: "//www.gravatar.com/avatar/2eb49f5ca80bb11bbd511a28d02dc594?s=200&r=pg&d=mm",
        //                 email: "passenger4@mail.com",
        //                 name: "passenger4",
        //             },
        //         ],
        //     }
        // )
        // navigation.navigate('ChatDrive')
        try {
            setLoadingPopup(true)
            if (userData.token !== "") {
                const drive = {
                    driveID: driveId
                }
                const res = await drives.getDriveByID(drive, userData.token)
                if (res !== null) {
                    setLoadingPopup(false);
                    seDetailsDrive(res)
                    navigation.navigate('ChatDrive')
                } else {
                    throw "getDrive failed";
                }
            } else {
                throw "no token"
            }
        } catch (error) {
            //console.log(error)
            setLoadingPopup(false)
        }

    }
    // setLoadingPopup(false)
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
                                        {/* console.log("1 drive") */ }
                                        const drive = obj[1][0]
                                        return (
                                            <View key={drive.driveID} style={styles.container}>
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
                                                                <Text style={{ color: colors.white, }}>{drive.date.split("/")[0]}</Text>
                                                            </View>
                                                        </View>
                                                        <View style={styles.centerInBlock}>

                                                            <TouchableOpacity onPress={() => {
                                                                goToDriveChat(drive.driveID)
                                                            }} style={styles.rightBlock}>
                                                                <View style={styles.hours}>
                                                                    <Text style={styles.hoursText}>{moment(drive.time).format("HH:MM")}</Text>
                                                                </View>
                                                                <View style={styles.location}>
                                                                    <Text style={styles.locationText}>{drive.from}</Text>
                                                                    <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                                    <Text style={styles.locationText}>{drive.to}</Text>
                                                                </View>
                                                            </TouchableOpacity>
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
                                        const runBy = obj[1]
                                        return (
                                            <View key={drive1.driveID} style={styles.container}>
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
                                                            <ScrollView horizontal={true}
                                                                ref={scrollViewRefX}

                                                                onContentSizeChange={(contentWidth, contentHeight) => {
                                                                    setScrollToBottomX((0))
                                                                    if (scrollViewRefX.current !== null) {
                                                                        scrollViewRefX.current.scrollTo({
                                                                            y: 0,
                                                                            animated: true,
                                                                        });
                                                                    }
                                                                }}>
                                                                {
                                                                    runBy.map((drives) => {
                                                                        return (
                                                                            <TouchableOpacity key={drives.driveID} onPress={() => {
                                                                                goToDriveChat(drives.driveID)
                                                                            }}
                                                                                style={{ width: 150, right: 0, alignSelf: 'flex-start', paddingBottom: 10 }}>
                                                                                <View style={styles.hours}>
                                                                                    <Text style={styles.hoursText}>{moment(drives.time).format("HH:MM")}</Text>
                                                                                </View>
                                                                                <View style={styles.location}>
                                                                                    <Text style={styles.locationText}>{drives.from}</Text>
                                                                                    <FontAwesome5 style={styles.fontAwesome} name="arrow-left" size={15} color={colors.silverTextDrive} />
                                                                                    <Text style={styles.locationText}>{drives.to}</Text>
                                                                                </View>
                                                                            </TouchableOpacity>
                                                                        )
                                                                    })
                                                                }
                                                            </ScrollView>
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
                                } else {
                                    const date = obj[0]
                                    const time = date.split("-")
                                    return (
                                        <View key={key1++} style={styles.container}>
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
    centerInBlock2: {
        alignSelf: 'center',
        width: "80%",
        height: 100,
        flexDirection: 'row',
    },
    centerInBlock: {
        alignSelf: 'center',
        width: "80%",
        // backgroundColor: "black",
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row',
    },
    leftInBlock: {
        alignSelf: 'flex-end',
        width: "10%",
        height: "100%",
        justifyContent: 'center'
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
        marginTop: 15,
        color: colors.white,
        fontSize: (Platform.OS === 'android') ? 20 : 25,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold'
    },
    locationText: {
        color: colors.silverTextDrive,
        fontSize: (Platform.OS === 'android') ? 18 : 20,
        fontFamily: "CalibriRegular",
    },
    fontAwesome: {
        marginRight: 5,
        marginLeft: 5,
    },
})
export default MyDrives;