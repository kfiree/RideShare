import React, { useState, useLayoutEffect, createContext, useContext, useEffect } from 'react';
import { Context } from '../../../../../Context';
import AsyncStorage from '@react-native-async-storage/async-storage';
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
  Animated,
  Alert,
  Platform,
  StatusBar,
  CheckBox,
} from 'react-native';

import axios from "axios";
import Users from './../../../../../requests/Users'
import { useFonts } from 'expo-font';

import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view'
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import { Entypo } from '@expo/vector-icons';

import colors from '../../layout'
import SelectDropdown from 'react-native-select-dropdown'
import RegistrationPopup from "./../nested/RegistrationPopup";
import Loading from "./../nested/Loading";
import CheckRequests from "./../nested/CheckRequests";
import * as ImagePicker from 'expo-image-picker';

const genders = ["זכר", "נקבה"];
const universities = ["הטכניון", "העברית", "מכון ויצמן ", "בר-אילן", "תל אביב", "חיפה", "בן-גוריון", "הפתוחה", "אריאל", "רייכמן",];
/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;



function Registration() {

  const { sendNotificationToClient, userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
  const [fonts] = useFonts({
    CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
  });


  const { status, setStatus } = useState(null)
  const navigation = useNavigation();
  const [imageId, setImageId] = useState(null);
  const [imageUniversity, setImageUniversity] = useState(null);
  const [showPopup, setShowPopup] = React.useState(false);
  const [loading, setLoading] = React.useState(false);
  const [data, setData] = React.useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
    password: "",
    password2: "",
    gender: "",
    imageId: "",
    imageUniversity: "",
    university: "",
    degree: "",

    isValidFirstName: true,
    isValidLastName: true,
    isValidPhoneNumber: true,
    isValidEmail: true,
    isValidPassword: true,
    isValidPassword2: true,
    isValidGender: true,
    isValidUniversityImage: true,
    isValidIdImage: true,
    isValidUniversities: true,
    isValidDegree: true,
  });
  const getLibraryPermissions = async () => {
    try {
      if (Platform.OS !== 'web') {
        setStatus(await ImagePicker.requestMediaLibraryPermissionsAsync())
        if (status !== 'granted') {
          alert('Sorry, we need camera roll permissions to make this work!');
        }
      }
    } catch (error) {
    }
  }
  getLibraryPermissions()

  const pickImageUniversity = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });
    if (!result.cancelled) {
      setImageUniversity(result.uri);
    }
  };
  const pickImageId = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.All,
      allowsEditing: true,
      aspect: [4, 3],
      quality: 1,
    });
    if (!result.cancelled) {
      setImageId(result.uri);
    }
  };


  const inputChange = (name, val) => {
    switch (name) {
      case "firstName":
        setData({
          ...data,
          firstName: val
        });
        return;
      case "lastName":
        setData({
          ...data,
          lastName: val
        });
        return;
      case "phoneNumber":
        setData({
          ...data,
          phoneNumber: val
        });
        return;
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
      case "password2":
        setData({
          ...data,
          password2: val
        });
        return;
      case "gender":
        if (val === "זכר" || val === "נקבה") {
          setData({
            ...data,
            gender: val
          });
        }
        return;
      case "university":
        setData({
          ...data,
          university: val
        });
        return;
      case "degree":
        setData({
          ...data,
          degree: val
        });
        return;
      default:
        break;
    };
  }

  const handleValidUser = (name, val) => {
    switch (name) {
      case "firstName":
        if (val.length > 2) {
          setData({
            ...data,
            isValidFirstName: true
          });
        } else {
          setData({
            ...data,
            isValidFirstName: false
          });
        }
        return;
      case "lastName":
        if (val.length > 2) {
          setData({
            ...data,
            isValidLastName: true
          });
        } else {
          setData({
            ...data,
            isValidLastName: false
          });
        }
        return;
      case "phoneNumber":
        if (val.length == 10 && !isNaN(val)) {
          setData({
            ...data,
            isValidPhoneNumber: true
          });
        } else {
          setData({
            ...data,
            isValidPhoneNumber: false
          });
        }
        return;
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
      case "password2":
        if (val > 4 && data.password === val) {
          setData({
            ...data,
            isValidPassword2: true
          });
        } else {
          setData({
            ...data,
            isValidPassword2: false
          });
        }
        return;
      case "gender":
        if (val === "זכר" || val === "נקבה") {
          setData({
            ...data,
            isValidGender: true
          });
        } else {
          setData({
            ...data,
            isValidGender: false
          });
        }
        return;
      case "university":
        setData({
          ...data,
          isValidUniversities: true
        });
        return;
      case "degree":
        if (val.length > 2) {
          setData({
            ...data,
            isValidDegree: true
          });
        } else {
          setData({
            ...data,
            isValidDegree: false
          });
        }
        return;
      default:
        break;
    };
  }

  const onSubmit = async () => {
    //console.log('onSubmit');

    if (data.isValidFirstName && data.isValidLastName && data.isValidPhoneNumber && data.isValidEmail && data.isValidPassword && data.isValidPassword2 && data.isValidUniversities && data.isValidDegree) {
      //console.log("Submit Form", data);
      setLoading(true);
      const userData = {
        first_name: data.firstName,
        last_name: data.lastName,
        email: data.email,
        password: data.password,
        phone_Number: data.phoneNumber,
        gender: data.gender,
        imageUniversity: (data.imageUniversity !== null) ? data.imageUniversity : "",
        image_Id: "some image",
        // (data.imageId !== null) ? data.imageId : "some image",
        university: data.university,
        degree: data.degree
      }
      try {
        if (await Users.registration(userData)) {
          setLoading(false);
          setShowPopup(true);
          // setTimeout(() => {
          //   setShowPopup(false);
          //   navigation.navigate('Login')
          // }, 1000)
        } else {
          throw 'no registration';
        }
        await sendNotificationToClient("חיכינו לך!", `איזה כיף שנרשמת אלינו.`)
      } catch (error) {
        console.error(error)
        setLoading(false);
        setShowPopup(false);
        await sendNotificationToClient("תהליך ההרשמה נכשל", "")
      }
    } else {
      await sendNotificationToClient("בעיה בשליחת הבקשה", "לא מילאת לנו את כל הנתונים")
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
        <ScrollView>
          <View style={styles.logoContainer}>
            <Image
              source={require('../../../images/logo/mainLogo.png')}
              style={styles.logo}
            />
            <Text style={styles.title}> הרשמה</Text>
            {/* <KeyboardAwareScrollView> */}
            <KeyboardAvoidingView style={styles.form}>
              <View style={styles.inputsBox}>

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  placeholder="שם פרטי"
                  style={
                    data.isValidFirstName ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("firstName", text)}
                  onEndEditing={(e) => handleValidUser("firstName", e.nativeEvent.text)}
                />
                {data.isValidFirstName ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  placeholder="שם משפחה"
                  style={
                    data.isValidLastName ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("lastName", text)}
                  onEndEditing={(e) => handleValidUser("lastName", e.nativeEvent.text)} />
                {data.isValidLastName ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  placeholder="דוא''ל"
                  autoComplete={"email"}
                  style={
                    data.isValidEmail ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("email", text)}
                  onEndEditing={(e) => handleValidUser("email", e.nativeEvent.text)} />

                {data.isValidEmail ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  placeholder="טלפון נייד"
                  autoComplete={"phoneNumber"}
                  style={
                    data.isValidPhoneNumber ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("phoneNumber", text)}
                  onEndEditing={(e) => handleValidUser("phoneNumber", e.nativeEvent.text)}
                />
                {data.isValidPhoneNumber ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  secureTextEntry={true} placeholder="סיסמה"
                  autoComplete={"password"}
                  style={
                    data.isValidPassword ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("password", text)}
                  onEndEditing={(e) => handleValidUser("password", e.nativeEvent.text)}
                />
                {data.isValidPassword ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <TextInput
                  placeholderTextColor={colors.placeholderTextColor}
                  secureTextEntry={true}
                  placeholder="אימות סיסמה"
                  style={
                    data.isValidPassword2 ? styles.inputText : styles.errorBorder
                  }
                  onChangeText={(text) => inputChange("password2", text)}
                  onEndEditing={(e) => handleValidUser("password2", e.nativeEvent.text)} />
                {data.isValidPassword2 ? null :
                  <Animated.View style={{ width: "100%", }}>
                    <Text style={styles.validation}>אנא הכניסו ערך תקין</Text>
                  </Animated.View>
                }

                <SelectDropdown
                  data={genders}
                  buttonStyle={
                    styles.inputText
                  }
                  buttonTextStyle={styles.textInputStyle}
                  defaultButtonText={"מין"}
                  onSelect={(selectedItem, index) => {
                    //console.log("onSelect = " + selectedItem);
                    inputChange("gender", selectedItem)
                  }}
                  buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                  rowTextForSelection={(item, index) => { return item }}
                />

                <View style={styles.uploadImages}>

                  {imageId ? null :
                    <TouchableOpacity onPress={pickImageId} >
                      <View style={styles.profilePic}>
                        <Entypo name="camera" size={90} color="black" />
                        <Text style={{ textAlign: 'center' }}>הוספת</Text>
                        <Text style={{ textAlign: 'center' }}> תמונות פרופיל</Text>
                      </View>
                    </TouchableOpacity>}
                  {imageId && <Image source={{ uri: imageId }} style={styles.profilePic} />}



                  {imageUniversity ? null :
                    <TouchableOpacity onPress={pickImageUniversity}>
                      <View style={styles.IDpic}>
                        <Entypo name="camera" size={90} color="black" />
                        <Text style={{ textAlign: 'center' }}>צילום תעודת</Text>
                        <Text style={{ textAlign: 'center' }}>סטודנט/אישור לימודים</Text>
                      </View>
                    </TouchableOpacity>}
                  {imageUniversity && <Image source={{ uri: imageUniversity }} style={styles.IDpic} />}
                </View>



                <View style={styles.lastInput}>
                  <SelectDropdown
                    data={universities}
                    buttonStyle={styles.inputText}
                    buttonTextStyle={styles.textInputStyle}
                    defaultButtonText={"מוסד לימודים"}
                    onSelect={(selectedItem, index) => { inputChange("university", selectedItem) }}
                    buttonTextAfterSelection={(selectedItem, index) => { return selectedItem }}
                    rowTextForSelection={(item, index) => { return item }} />

                  <TextInput
                    placeholderTextColor={colors.placeholderTextColor}
                    placeholder="סוג תואר"
                    style={
                      data.isValidDegree ? styles.inputText : styles.errorBorder
                    }
                    onChangeText={(text) => inputChange("degree", text)}
                    onEndEditing={(e) => handleValidUser("degree", e.nativeEvent.text)} />
                  {data.isValidDegree ? null :
                    <Animated.View style={{ width: "100%", }}>
                      <Text style={{ textAlign: 'center', color: colors.importantNotification, fontSize: 20 }}>אנא הכניסו ערך תקין</Text>
                    </Animated.View>}
                </View>

              </View>
              <View style={{ marginBottom: 120 }}>
                <View style={styles.submitForm}>
                  <TouchableOpacity style={styles.submitInput}
                    onPress={() =>
                      onSubmit()
                    } >
                    <View style={{
                      justifyContent: 'center',
                      alignItems: 'center',
                      alignContent: 'center'
                    }}>
                      <Text style={styles.btnSubmit}>הרשמה</Text>
                    </View>
                  </TouchableOpacity>
                </View>

                <View style={styles.submitForm}>
                  <TouchableOpacity style={styles.submitInput}
                    onPress={() => {
                      navigation.navigate('Login');
                    }}>
                    <View style={{
                      justifyContent: 'center',
                      alignItems: 'center',
                      alignContent: 'center',
                    }}>
                      <Text style={styles.btnSubmit}>התחברות</Text>
                    </View>
                  </TouchableOpacity>
                </View>
              </View>
            </KeyboardAvoidingView>
          </View>
        </ScrollView>

        {/*
            popup1 - 
      */ }
        {!showPopup ? null :
          <View style={{ width: "100%", height: "100%", position: 'absolute' }}>
            <RegistrationPopup />
          </View>}


        {!loading ? null :
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
    marginTop: 20,
    fontWeight: 'bold',
    fontFamily: "CalibriRegular"
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
    borderWidth: 1,
    backgroundColor: colors.input,
    marginTop: 10,
    borderRadius: 15,
    textAlignVertical: "top",
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
  textInputStyle: {
    alignItems: 'center',
    textAlignVertical: "top",
    fontSize: 30,
    fontFamily: "CalibriRegular",
    textAlign: 'center',
    color: colors.placeholderTextColor
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
    marginStart: 20
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
  submitForm: {
    marginBottom: 20,
    justifyContent: 'flex-end',
    alignItems: 'center',

  },
  submitInput: {
    width: (windowWidth - 100),
    height: 50,
    fontFamily: "CalibriRegular",
    fontSize: 30,
    color: colors.black,
    fontWeight: 'bold',
    textAlign: 'center',
    justifyContent: 'center',
    alignItems: 'center',
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
    // textAlignVertical: "top",
  },
  uploadImages: {
    height: 100,
    marginBottom: 55,
    flexDirection: "row",
    justifyContent: 'space-between',
    width: "100%",
    marginTop: 5,
  },
  lastInput: {
    height: 100,
    marginBottom: 50,
    width: "100%",
  },
  profilePic: {
    width: ((windowWidth - 60) / 2),
    height: 150,
    // fontSize: 30,
    fontFamily: "CalibriRegular",
    textAlign: 'center',
    borderColor: "black",
    borderLeftWidth: 1,
    borderRightWidth: 1,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    backgroundColor: colors.input,
    marginTop: 5,
    borderBottomLeftRadius: 15,
    borderBottomRightRadius: 15,
    borderTopLeftRadius: 15,
    borderTopRightRadius: 15,
    textAlignVertical: "top",
    alignItems: 'center',
  },
  IDpic: {
    width: ((windowWidth - 60) / 2),
    height: 150,
    fontSize: 30,
    fontFamily: "CalibriRegular",
    textAlign: 'center',
    borderColor: "black",
    borderLeftWidth: 1,
    borderRightWidth: 1,
    borderTopWidth: 1,
    borderBottomWidth: 1,
    backgroundColor: colors.input,
    marginTop: 5,
    borderBottomLeftRadius: 15,
    borderBottomRightRadius: 15,
    borderTopLeftRadius: 15,
    borderTopRightRadius: 15,
    textAlignVertical: "top",
    alignItems: 'center',
  },
  btnSubmit: {
    fontWeight: 'bold',
    fontSize: 30,
    fontFamily: "CalibriRegular",
    // marginTop: 7,
  },
  validation: {
    textAlign: 'center',
    color: colors.importantNotification,
    fontSize: 20,
    fontFamily: "CalibriRegular",
  }
})

const printData = () => {
  //console.log("firstName = " + data.firstName);
  //console.log("lastName = " + data.lastName);
  //console.log("phoneNumber = " + data.phoneNumber);
  //console.log("email = " + data.email);
  //console.log("password = " + data.password);
  //console.log("password2 = " + data.password2);
  //console.log("gender = " + data.gender);
  //console.log("gender = " + data.gender);
  //console.log("idImage = " + data.idImage);
  //console.log("UniversityImage = " + data.UniversityImage);
  //console.log("university = " + data.university);
  //console.log("degree = " + data.degree);
}


export default Registration;
