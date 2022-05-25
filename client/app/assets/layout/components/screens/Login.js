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
  Animated,
  CheckBox,
} from 'react-native';
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import axios from "axios";

import colors from '../../layout'
import BouncyCheckbox from "react-native-bouncy-checkbox";
import Loading from "./../nested/Loading";
import LoginPopup from "./../nested/LoginPopup";
import token from '../../../../../logicApp';
import Users from './../../../../../requests/Users'

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;




function Login() {
  const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
  const [fonts] = useFonts({
    CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
  });
  const navigation = useNavigation();
  const [showPopup, setShowPopup] = React.useState(false);
  const [data, setData] = React.useState({
    email: "",
    password: "",

    isValidEmail: true,
    isValidPassword: true,
  });


  const inputChange = (name, val) => {
    switch (name) {
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
      default:
        break;
    };
  }

  const handleValidUser = (name, val) => {
    switch (name) {
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
      default:
        break;

    };
  }
  const getUserDetailsByToken = async (token) => {

    try {
      const res = await Users.getUser(token)
      if (res !== null) {
        //console.log(JSON.stringify(res));
        setUserData({
          ...userData,
          token: token,
          id: res.email,
          firstName: res.firstName,
          lastName: res.lastName,
          email: res.email,
          phoneNumber: res.phoneNumber,
          gender: res.gender,
          imageUniversity: res.universityImage,
          imageId: res.avatar,
          university: res.university,
          degree: res.degree,
          rating: res.rating,
          numRating: res.ratingNum,
          numDrives: 0,
          modelCar: (res.carModel !== null) ? res.carModel : "",
          colorCar: (res.colorCar !== null) ? res.colorCar : "",
          dateOfBirth: (res.carColor !== null) ? res.carColor : "",
        })
        setLoadingPopup(false);
        setShowPopup(true);
        setIsSignedIn(true);
        setTimeout(() => {
          setShowPopup(false);
          navigation.navigate('HomePage')
        }, 1000)
        setLoadingPopup(false)
      } else {
        throw "can not get user details"
      }
      await sendNotificationToClient("התחברות הצליחה", "התחברת בהצלחה למערכת שלנו")
    } catch (err) {
      console.error(err);
      console.error("request failed");
      setLoadingPopup(false);
      setIsSignedIn(false);
      setShowPopup(false);
      await sendNotificationToClient("התחברות נכשלה", "משהו לא תקין בנתונים שהבאת לנו")

    }
  };


  const onSubmit = async () => {
    //console.log("Submit Form");
    if (data.isValidEmail && data.isValidPassword) {
      setLoadingPopup(true);
      const userData = {
        email: data.email,
        password: data.password,
      }
      try {
        const result = await Users.login(userData);
        if (result.length > 0) {
          setUserData({
            ...userData,
            token: result,
          })
          setIsSignedIn(false);
          await getUserDetailsByToken(result)
          setLoadingPopup(false)
        } else {
          throw 'no login';
        }
      } catch (error) {
        console.error(error)
        setLoadingPopup(false);
        setIsSignedIn(false);
        setShowPopup(false);
      }
    }
  }

  // const onSubmit = async (e) => {
  //   //console.log("Submit Form");
  //   setLoadingPopup(true);

  //   const userData = {
  //     email: data.email,
  //     password: data.password,
  //   }

  //   try {
  //     const config = {
  //       headers: {
  //         'Content-Type': 'application/json',
  //       }
  //     }
  //     const body = JSON.stringify(userData);
  //     const res = await axios.post('https://tsqzar32wa.execute-api.us-east-1.amazonaws.com/production/api/login', body, config);
  //     token.addTokenToStorage(res.data["token"])
  //     // //console.log("token = " + res.data["token"])

  //     setLoadingPopup(false)
  //   } catch (err) {
  //     console.error(err.response.data);
  //     console.error("request failed");
  //     setLoadingPopup(false);
  //     setIsSignedIn(false);
  //     setShowPopup(false);
  //   }
  // }
  if (!fonts) {
    return null;
  } else {
    return (
      <ImageBackground
        style={styles.background}
        source={require('../../../images/images/cover.png')}
      >
        <ScrollView>
          <View style={styles.logoContainer}>
            <Image
              source={require('../../../images/logo/mainLogo.png')}
              style={styles.logo}
            />
            <Text style={styles.title}>התחברות</Text>
            <KeyboardAvoidingView style={styles.form}>
              <View style={styles.inputsBox}>
                <TextInput
                  onChangeText={(text) => inputChange("email", text)}
                  onEndEditing={(e) => handleValidUser("email", e.nativeEvent.text)}
                  placeholder="דוא''ל"
                  style={
                    data.isValidEmail ? styles.inputText : styles.errorBorder
                  } />
                {data.isValidEmail ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={{
                      textAlign: 'center', color: colors.importantNotification, fontSize: 20, fontFamily: "CalibriRegular"
                    }}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }


                <TextInput
                  onChangeText={(text) => inputChange("password", text)}
                  onEndEditing={(e) => handleValidUser("password", e.nativeEvent.text)}
                  autoComplete={"password"}
                  secureTextEntry={true}
                  placeholder="סיסמא"
                  style={
                    data.isValidPassword ? styles.inputText : styles.errorBorder
                  } />
                {data.isValidPassword ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={{
                      textAlign: 'center', color: colors.importantNotification, fontSize: 20, fontFamily: "CalibriRegular"
                    }}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

              </View>

              <View style={styles.checkBox}>
                <Text style={styles.checkBoxText}>זכור אותי</Text>
                <BouncyCheckbox
                  size={30}
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
              {/* <Text style={styles.link} onPress={() => Linking.openURL(forgotPasswordLink)}>שכחתי סיסמא אחי</Text> */}

              <View style={styles.social}>
                {/* <View style={styles.facebookImg}> */}
                <TouchableOpacity
                  style={styles.facebookImg}>
                  <Image
                    source={require('./../../../images/images/facebook.png')}
                    fadeDuration={1500}
                    resizeMode="cover"
                    style={{ height: 80, width: 80 }}
                  />
                </TouchableOpacity>
                {/* </View> */}
                <View style={styles.googleImg}>
                  <TouchableOpacity
                    style={styles.facebookImg}>
                    <Image
                      source={require('./../../../images/images/google.png')}
                      fadeDuration={1500}
                      style={{ height: 70, width: 70 }}
                      resizeMode="cover"
                    />
                  </TouchableOpacity>
                </View>
              </View>

              <View style={styles.submitForm}>

                <TouchableOpacity style={styles.submitInput}
                  onPress={() => {
                    onSubmit();
                  }
                  }>
                  <View style={{
                    justifyContent: 'center',
                    alignItems: 'center',
                    alignContent: 'center'
                  }}>
                    <Text style={styles.btnSubmit}>התחברות</Text>

                  </View>
                </TouchableOpacity>
                <TouchableOpacity style={styles.submitInput}
                  onPress={() => {
                    navigation.navigate("Registration")
                  }
                  }>
                  <View style={{
                    justifyContent: 'center',
                    alignItems: 'center',
                    alignContent: 'center'
                  }}>
                    <Text style={styles.btnSubmit}>הרשמה</Text>

                  </View>
                </TouchableOpacity>
              </View>

            </KeyboardAvoidingView>
          </View>
        </ScrollView>

        {/*
            popup1 - 
      */ }
        {!showPopup ? null :
          <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
            <LoginPopup />
          </View>}


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
    top: 70,
    alignItems: 'center',
  },
  title: {
    fontSize: 50,
    fontFamily: "CalibriRegular",
    marginTop: 20,
    fontWeight: 'bold',
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
    alignItems: 'center',
  },
  errorBorder: {
    width: (windowWidth - 40),
    height: 50,
    fontSize: 30,
    fontFamily: "CalibriRegular",
    textAlign: 'center',
    borderColor: colors.importantNotification,
    borderWidth: 2,
    backgroundColor: colors.input,
    marginTop: 10,
    borderRadius: 15,
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
    justifyContent: 'center',
    alignItems: 'center',
  },
  googleImg: {
    marginLeft: 10,
    marginBottom: 20,
    width: 100,
    height: 80,
    justifyContent: 'center',
    alignItems: 'center',
  },
  submitForm: {
    marginTop: 20,
    marginBottom: 100,
    justifyContent: 'center',
    alignItems: 'center',
    alignContent: 'center',
  },
  submitInput: {
    width: (windowWidth - 100),
    height: 50,
    color: colors.black,
    marginBottom: 15,
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
    alignContent: "center"
  },
  btnSubmit: {
    fontWeight: 'bold',
    fontSize: 30,
    fontFamily: "CalibriRegular",
  },
})
export default Login;