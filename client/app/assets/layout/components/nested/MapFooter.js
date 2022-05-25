import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
// import { Context } from '../../../../Context';
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
import { Ionicons, AntDesign, FontAwesome } from '@expo/vector-icons';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import layout from './../../layout';
import { useFonts } from 'expo-font';
import colors from '../../layout'


const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;



const MapFooter = ({ data }) => {

    // const { userData, setUserData, pages, setPages, location, setLocation, listUniversities, setListUniversities, checkCityForUniversity, cities, universities, loadingPopup, setLoadingPopup } = useContext(Context)

    // const [fonts] = useFonts({
    //     CalibriRegular: require('./../../../assets/fonts/Calibri-Regular.ttf'),
    // });
    const navigation = useNavigation();
    const finish = (Pos) => {
        // setUserMarker2(Pos);
        // if (Pos)
        //     data.isDriver ?
        //         navigation.navigate("CreateDrive")
        //         : navigation.navigate("SearchDrive")
        // else {
        //     data.isDriver
        //         ? navigation.navigate("CreateDrive", { Pos: Pos })
        //         : navigation.navigate("MapRide", { Pos: Pos })
        // }


    }

    if (!fonts) {
        return null;
    } else {
        return (
            <View style={styles.container}>
                <TouchableOpacity
                    onPress={() => {
                        console.log('finish');
                        // finish(data.Pos)
                    }}>
                    <View style={[styles.boxSize, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <FontAwesome name="map-marker" size={30} color={colors.success} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>בחר</Text>
                        </View>
                    </View>
                </TouchableOpacity>


                <TouchableOpacity>
                    <View style={[styles.boxSize, styles.borderLeft, { backgroundColor: "white" }]} >

                    </View>
                </TouchableOpacity>


                <TouchableOpacity
                    onPress={() => {
                        // navigation.navigate("MyDrives")
                    }}>
                    <View style={[styles.boxSize, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <Image
                                resizeMode='cover'
                                style={{ height: 35, width: 35 }}
                            />
                        </View>
                    </View>
                </TouchableOpacity>

                <TouchableOpacity
                    onPress={() => {
                        console.log('TouchableOpacity')
                        // data.isDriver ? navigation.navigate("CreateDrive") : navigation.navigate("SearchDrive")
                    }}>

                    <View style={[styles.boxSize, styles.borderLeft, { backgroundColor: "white" }]} >
                        <View style={styles.imageIcon}>
                            <FontAwesome name="ban" size={30} color={colors.error} />
                        </View>
                        <View style={styles.textBlock}>
                            <Text style={styles.Text}>בחר</Text>
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
export default MapFooter;
