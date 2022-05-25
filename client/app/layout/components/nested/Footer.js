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
import { Ionicons, AntDesign, FontAwesome5 } from '@expo/vector-icons';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import layout from '../../layout';
import { useFonts } from 'expo-font';


const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;

const Footer = () => {
    const { userData, setUserData, checkCityForUniversity, cities, universities, loadingPopup, setLoadingPopup } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../assets/fonts/Calibri-Regular.ttf'),
    });
    const navigation = useNavigation();

    if (!fonts) {
        return null;
    } else {
        return (
            <View style={styles.container}>
                <TouchableOpacity
                    onPress={() => {
                        setSearch(false)
                        navigation.navigate("MySuggestionsPassenger")
                    }}>
                    <View style={[styles.boxSize, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <Image
                                resizeMode='cover'
                                style={{ height: 35, width: 35 }}
                                source={require('./../../../assets/images/icons/mySuggestions.png')} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>ההצעות שלי</Text>
                        </View>
                    </View>
                </TouchableOpacity>


                <TouchableOpacity
                    onPress={() => {
                        navigation.navigate("MyBenefits")
                    }}>
                    <View style={[styles.boxSize, styles.borderLeft, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <Image
                                resizeMode='cover'
                                style={{ height: 35, width: 35 }}
                                source={require('./../../../assets/images/icons/benefits.png')} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>ההטבות שלי</Text>
                        </View>
                    </View>
                </TouchableOpacity>


                <TouchableOpacity
                    onPress={() => {
                        navigation.navigate("MyDrives")
                    }}>
                    <View style={[styles.boxSize, styles.borderLeft, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>

                            <Image
                                resizeMode='cover'
                                style={{ height: 35, width: 35 }}
                                source={require('./../../../assets/images/icons/calendar.png')} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>הנסיעות שלי</Text>
                        </View>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity
                    onPress={() => {
                        navigation.navigate("MyChats")
                    }}>
                    <View style={[styles.boxSize, styles.borderLeft, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <Image
                                resizeMode='cover'
                                style={{ height: 35, width: 35 }}
                                source={require('./../../../assets/images/icons/chats.png')} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>הצ'אטים שלי</Text>
                        </View>
                    </View>
                </TouchableOpacity>

            </View>

        );
    }
}
const styles = StyleSheet.create({
    container: {
        backgroundColor: "white",
        flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
        borderTopWidth: 1,
    },
    boxSize: {
        width: sizeHeader,
        height: (sizeHeader / 1.5),
        justifyContent: 'center',
        alignItems: 'center',
    },
    borderLeft: {
        borderLeftWidth: 1,
    },
    imageIcon: {
        justifyContent: 'center',
        alignItems: 'center',
    },
    textBlock: {
        width: "100%",
        height: "30%",
        justifyContent: 'center',
        alignItems: 'center',
    },
    Text: {
        fontSize: (Platform.OS === 'android') ? 13 : 15,
        fontFamily: "CalibriRegular",
        fontWeight: 'bold',
    },

})
export default Footer;
