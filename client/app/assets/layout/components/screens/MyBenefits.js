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
import { MaterialCommunityIcons, Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";
import BenefitsPurchased from "./BenefitsPurchased";
import { Colors } from 'react-native/Libraries/NewAppScreen';
import Loading from "./../nested/Loading";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;


const sizeHeader = windowWidth / 4;
function MyBenefits() {
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();
    const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [dataBenefits, setDataBenefits] = useState({
        rating: userData.rating,
    });
    const [showBenefitsPurchased, setShowBenefitsPurchased] = useState(false)
    const [priceBenefit, setPriceBenefit] = useState(0)



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
                        <View style={styles.context}>
                            <Text style={styles.title}>היי {userData.firstName},</Text>
                            <View style={styles.row}>
                                <Text style={styles.titlenNew}>צברת</Text>
                                <Text style={styles.titleBold}> {userData.rating} </Text>
                                <Text style={styles.titlenNew}>נקודות</Text>
                            </View>
                            <View style={styles.submitForm}>
                                <TouchableOpacity style={styles.submitInput}
                                    onPress={() => {
                                        Alert.alert("Submit Form", "Search Benefits", [
                                            {
                                                text: "Ok", onPress: () => console.log("yes")
                                            },
                                            { text: "no", onPress: () => console.log("no") },
                                        ]);
                                    }
                                    }>
                                    <View style={{
                                        flexDirection: 'row',
                                        justifyContent: 'center',
                                        alignItems: 'center',
                                        alignContent: 'center'
                                    }}>
                                        <Text style={styles.searchBenefits}>חיפוש הטבות</Text>
                                        <FontAwesome name="search" size={24} color={colors.white} />
                                    </View>
                                </TouchableOpacity>
                            </View>
                        </View>


                        <View>
                            <View style={styles.titleTypeBenefits}>
                                <Text style={styles.titleBenefitsText}>הטבות שקרובות אליך</Text>
                            </View>
                            <View style={styles.container}>
                                <ScrollView style={styles.benefits}
                                    horizontal={true}
                                    onContentSizeChange={(contentWidth, contentHeight) => {

                                    }}>

                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>מכנס ב-100 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/renuar.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>20 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(20)
                                                    if (userData.rating >= 20) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>


                                            </TouchableOpacity>
                                        </View>
                                    </View><View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>סושי ב-15 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/japanika.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>35 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(35)
                                                    if (userData.rating >= 35) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>

                                </ScrollView>
                            </View>
                        </View>



                        <View>
                            <View style={styles.titleTypeBenefits}>
                                <Text style={styles.titleBenefitsText}>הטבות פופולריות</Text>
                            </View>
                            <View style={styles.container}>
                                <ScrollView style={styles.benefits}
                                    horizontal={true}
                                    onContentSizeChange={(contentWidth, contentHeight) => {

                                    }}>

                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>מכנס ב-100 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/renuar.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>20 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(20)
                                                    if (userData.rating >= 20) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>


                                            </TouchableOpacity>
                                        </View>
                                    </View><View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>סושי ב-15 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/japanika.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>35 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(35)
                                                    if (userData.rating >= 35) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>

                                </ScrollView>
                            </View>
                        </View>



                        <View>
                            <View style={styles.titleTypeBenefits}>
                                <Text style={styles.titleBenefitsText}>הטבות משתלמות במיוחד</Text>
                            </View>
                            <View style={styles.container}>
                                <ScrollView style={styles.benefits}
                                    horizontal={true}
                                    onContentSizeChange={(contentWidth, contentHeight) => {

                                    }}>

                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>מכנס ב-100 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/renuar.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>20 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(20)
                                                    if (userData.rating >= 20) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>


                                            </TouchableOpacity>
                                        </View>
                                    </View><View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>סושי ב-15 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/japanika.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>35 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(35)
                                                    if (userData.rating >= 35) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>

                                </ScrollView>
                            </View>
                        </View>



                        <View>
                            <View style={styles.titleTypeBenefits}>
                                <Text style={styles.titleBenefitsText}>הטבות מומלצות מאיתנו</Text>
                            </View>
                            <View style={styles.container}>
                                <ScrollView style={styles.benefits}
                                    horizontal={true}
                                    onContentSizeChange={(contentWidth, contentHeight) => {

                                    }}>

                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>מכנס ב-100 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/renuar.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>20 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(20)
                                                    if (userData.rating >= 20) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>


                                            </TouchableOpacity>
                                        </View>
                                    </View><View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>סושי ב-15 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/japanika.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>35 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(35)
                                                    if (userData.rating >= 35) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.boxBenefit}>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitItem}>הטבה של 50 ש"ח</Text>
                                        </View>
                                        <View style={styles.boxImage}>
                                            <View style={styles.imagePerson}>
                                                <Image
                                                    resizeMode="cover"
                                                    source={require('./../../../images/icons/benefits.jpg')}
                                                    style={{
                                                        height: "100%",
                                                        width: "100%",
                                                        padding: 5
                                                    }}
                                                />
                                            </View>
                                        </View>
                                        <View style={styles.benefitTitle}>
                                            <Text style={styles.benefitPrice}>45 נקודות</Text>
                                        </View>
                                        <View style={styles.submitForm}>
                                            <TouchableOpacity style={styles.useBenefits}
                                                onPress={() => {
                                                    setPriceBenefit(45)
                                                    if (userData.rating >= 45) {
                                                        setShowBenefitsPurchased(true);
                                                    } else {
                                                        Alert.alert("הרכישה אינה בוצעה", "אין לך מספיק נקודות לרכישת ההטבה", [
                                                            { text: "Ok", onPress: () => console.log("Ok") },
                                                        ]);
                                                    }
                                                }}>
                                                <View style={{
                                                    justifyContent: 'center',
                                                    alignItems: 'center',
                                                    alignContent: 'center'
                                                }}>
                                                    <Text style={styles.btnSubmit}>מימוש ההטבה</Text>

                                                </View>
                                            </TouchableOpacity>
                                        </View>
                                    </View>

                                </ScrollView>
                            </View>
                        </View>




                    </ScrollView>
                    {
                        !showBenefitsPurchased ? null :
                            <BenefitsPurchased setShowBenefitsPurchased={setShowBenefitsPurchased} />
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
        flexDirection: 'row',
        flex: 1,
        marginTop: 10,
        justifyContent: 'center',
        alignItems: 'center',
        marginBottom: 20
    },
    boxImage: {
        justifyContent: "center",
        alignItems: "center",
    },
    boxContent: {
        height: "100%",
        width: "25%",
        justifyContent: 'flex-start',
        marginRight: 10
    },
    boxContent2: {
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
        width: "25%",
    },

    context: {
        marginTop: 20,
        justifyContent: 'center',
        alignItems: 'center',
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    rowBox: {
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 2,
    },
    title: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        marginTop: 10,
    },
    titleBold: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        marginTop: 10,
        fontWeight: 'bold',
    },
    titlenNew: {
        fontSize: 40,
        fontFamily: "CalibriRegular",
        marginTop: 17,
    },
    titleTypeBenefits: {
        width: (windowWidth - 10),
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 20,
    },
    titleBenefitsText: {
        fontSize: 25,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold'
    },
    benefits: {
        paddingLeft: 10,
        paddingRight: 10,
        flexDirection: 'row',
        width: "90%",
        marginRight: 10,
        marginLeft: 10,
        height: 220,
        backgroundColor: colors.backgroundConversationChat,
        borderColor: colors.black,
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderRadius: 35,
    },
    imagePerson: {
        width: 80,
        height: 80,
        borderRadius: 110 / 2,
        overflow: "hidden",
        borderWidth: 1,
        borderColor: colors.black
    },
    submitForm: {
        justifyContent: 'center',
        alignItems: 'center',

    },
    submitInput: {
        width: 200,
        height: 50,
        marginTop: 5,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.confirmButton,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    useBenefits: {
        width: "100%",
        height: 20,
        marginTop: 5,
        color: colors.black,
        fontSize: 30,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        borderColor: "black",
        borderLeftWidth: 1,
        borderRightWidth: 1,
        borderTopWidth: 1,
        borderBottomWidth: 1,
        backgroundColor: colors.primary,
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    btnSubmit: {
        fontWeight: 'bold',
        fontFamily: "CalibriRegular",
        fontSize: Platform.OS === "android" ? 12 : 15,
    },
    searchBenefitsForm: {
        justifyContent: 'center',
        alignItems: 'center',
        marginTop: 5,
    },
    searchBenefitsInput: {
        width: 180,
        height: 50,
        marginTop: 5,
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
        borderBottomLeftRadius: 25,
        borderBottomRightRadius: 25,
        borderTopLeftRadius: 25,
        borderTopRightRadius: 25,
        textAlignVertical: "top",
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: "center",
    },
    searchBenefits: {
        fontSize: (Platform.OS === 'android') ? 20 : 25,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },
    confirmButton: {
        fontSize: 30,
        fontFamily: "CalibriRegular",
        color: colors.white,
        marginRight: 10,
        textAlign: 'center',
    },
    boxBenefit: {
        width: 110,
        height: "100%",
        marginRight: 10,
        marginLeft: 10,
        paddingLeft: 5,
        paddingRight: 5,
    },
    boxArrow: {
        width: 40,
        height: "100%",
        justifyContent: 'center',
        alignItems: 'center',
    },
    benefitTitle: {
        marginTop: 15,
    },
    benefitItem: {
        fontSize: (Platform.OS === 'android') ? 13 : 16,
        height: 40,
        // backgroundColor: "black",
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        fontWeight: 'bold',
    },
    benefitPrice: {
        fontSize: 13,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
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
    imageQR: {
        height: 150,
        width: 150,
        marginTop: 20,
    },
    popup: {
        height: (Platform.OS === 'android') ? (windowHeight - (windowWidth)) : (windowHeight - (windowWidth / 1.2)),
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
        marginTop: (windowWidth / 1.8),
        justifyContent: 'center',
        alignItems: 'center',
    },
    titlePopup: {
        fontSize: 22,
        fontFamily: "CalibriRegular",
        marginTop: 15,
        fontWeight: 'bold',
    },
    textPopup: {
        fontSize: 20,
        fontFamily: "CalibriRegular",
        marginTop: 5,
    },

})
export default MyBenefits;