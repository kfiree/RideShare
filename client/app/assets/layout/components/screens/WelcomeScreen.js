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

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Header from "./../nested/Header";
import Footer from "./../nested/Footer";

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;
import CheckRequests from '../nested/CheckRequests';

import { useFonts } from 'expo-font';


// const windowWidth = Dimensions.get('screen').width;
// const windowHeight = Dimensions.get('screen').height;

function Setting() {
  const [fonts] = useFonts({
    CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
  });
  const navigation = useNavigation();
  const { userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)


  if (!fonts) {
    return null;
  } else {
    return (<ImageBackground
      style={styles.background}
      source={require('../../../images/images/cover.png')}
    >
      <SafeAreaView>
        <Header />
        <View style={{ height: "70%" }}>
          <CheckRequests />
        </View>
        <Footer />
      </SafeAreaView>
    </ImageBackground>)
  }

}



const styles = StyleSheet.create({
  background: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logoContainer: {
    top: 150,
    alignItems: 'center',
  },
  title: {
    fontSize: 50,
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
    marginTop: 20,
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
    fontSize: 30,
    fontFamily: "CalibriRegular",
    textAlign: 'center',
    borderColor: "black",
    borderLeftWidth: 1,
    borderRightWidth: 1,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    backgroundColor: colors.input,
    marginTop: 15,
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
    marginStart: 20,
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
  social: {
    marginTop: 40,
    flexDirection: 'row',
  },
  facebookImg: {
    marginRight: 10,
    marginBottom: 20,
    width: 100,
    height: 80,
  },
  googleImg: {
    marginLeft: 10,
    marginBottom: 20,
    width: 100,
    height: 80,
  },
  submitForm: {
    marginTop: 20,
    justifyContent: 'center',
    alignItems: 'center',
  },
  submitInput: {
    width: (windowWidth - 100),
    height: 50,
    color: colors.black,
    fontSize: 30,
    fontFamily: "CalibriRegular",
    marginBottom: 100,
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
  },
  btnSubmit: {
    fontWeight: 'bold',
    fontSize: 30,
    fontFamily: "CalibriRegular",
    width: (windowWidth - 100),
    height: 50,
  }
})
export default Setting;