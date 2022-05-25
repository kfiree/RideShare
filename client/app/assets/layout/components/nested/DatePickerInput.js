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
import { Entypo, AntDesign, FontAwesome } from '@expo/vector-icons';
import colors from '../../layout'
import DateTimePicker from '@react-native-community/datetimepicker';

const DatePickerInput = () => {
    const { userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)

    const [date, setDate] = useState(new Date(1598051730000));
    const [mode, setMode] = useState('date');
    const [show, setShow] = useState(false);

    const onChange = (event, selectedDate) => {
        const currentDate = selectedDate || date;
        setShow(Platform.OS === 'ios');
        setDate(currentDate);
    };

    const showMode = (currentMode) => {
        setShow(true);
        setMode(currentMode);
    };

    const showDatepicker = () => {
        showMode('date');
    };

    const showTimepicker = () => {
        showMode('time');
    };
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    if (!fonts) {
        return null;
    } else {
        return (

            <View style={styles.inputTextDate}>
                <View style={styles.inputTextDate}>
                    <DatePickerInput />
                </View>
                <View style={styles.logoInput}>
                    <Entypo name="calendar" size={30} color={colors.search} />
                </View>
            </View>

        );
    }
};

const styles = StyleSheet.create({
    inputTextDate: {
        width: "75%",
        height: "100%",
        fontSize: Platform.OS === "android" ? 14 : 16,
        fontFamily: "CalibriRegular",
        textAlign: 'center',
        textAlignVertical: 'center',
        justifyContent: 'center',
        alignItems: 'center'
        // alignItems: 'flex-start',
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
})

export default DatePickerInput