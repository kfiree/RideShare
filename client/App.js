import React, { useState, useContext, useLayoutEffect, useEffect, useRef, createContext } from 'react';
import { Context } from './Context'
import {
  SafeAreaView,
  TouchableWithoutFeedback,
  TouchableOpacity,
  TouchableHighlight,
  TouchableNativeFeedback,
  StyleSheet,
  Text,
  View,
  Image,
  ScrollView,
  ImageBackground,
  Dimensions,
  Button,
  Alert,
  Platform,
  StatusBar,
} from 'react-native';
import { useNavigationState } from '@react-navigation/native';


import { useDimensions, useDeviceOrientation } from '@react-native-community/hooks';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import config from './react-native.config';

import CreateDrive from './app/layout/components/screens/CreateDrive';
import HomePage from './app/layout/components/screens/HomePage';
import MapRide from './app/layout/components/screens/MapRide';
import * as Location from 'expo-location';

import universities from './requests/Universities'
const Stack = createNativeStackNavigator();

export default function App() {
  const [loadingPopup, setLoadingPopup] = React.useState(false);
  const [pages, setPages] = React.useState({
    prev: '',
    curr: 'app'
  });

  console.log('pages', pages)
  const [listUniversities, setListUniversities] = React.useState([
    {
      id: 23234,
      university_Id: "26e55964-0fe3-46db-bb00-b0bedd5e4f9f",
      university_name: "אוניברסיטת תל אביב",
      geoLocation_Id: "35ba8c1d-78b9-4646-abd6-ee4afa1f2b10",
      latitude: "32.11734877512516",
      longitude: "34.80584682164881",
      nameLocation: "אוניברסיטת תל אביב"
    },
    {
      id: 23235,
      university_Id: "5dcb3d62-c5d2-46fd-92f1-5396e9fba1dd",
      university_name: "אוניברסיטת אריאל",
      geoLocation_Id: "9e3b3ba4-f9b2-4334-81f0-d8d7e1d6efa1",
      latitude: "32.103218414047376",
      longitude: "35.207883124670346",
      nameLocation: "אוניברסיטת אריאל"
    },
    {
      id: 23236,
      university_Id: "d7c88497-2cba-405b-90f3-0e2d96ec3421",
      university_name: "אוניברסיטת בן גוריון",
      geoLocation_Id: "4d6e4f19-4611-4d87-9931-e1dabb48b9ae",
      latitude: "31.26080236320762",
      longitude: "34.803846134261214",
      nameLocation: "אוניברסיטת בן גוריון"
    },
    {
      id: 23237,
      university_Id: "016b32eb-9c2c-407a-9889-7530c52543c5",
      university_name: "האוניברסיטה העברית",
      geoLocation_Id: "e3f395ed-7570-4370-976d-e3627bca4cb9",
      latitude: "31.797038106474233",
      longitude: "35.239984693693806",
      nameLocation: "האוניברסיטה העברית"
    }
  ]);
  const [userData, setUserData] = useState({
    token: "",
    id: "",
    firstName: "",
    lastName: "",
    email: "",
    phoneNumber: "",
    gender: "",
    imageUniversity: "",
    imageId: "",
    university: "",
    degree: "",
    rating: "",
    numRating: "",
    numDrives: "",
    modelCar: "",
    colorCar: "",
    dateOfBirth: "",
  });

  const [currentLocation, setCurrentLocation] = useState(null);
  const cities = ["אופקים", "אור יהודה", "אור עקיבא", "אילת", "אלעד", "אריאל", "אשדוד", "אשקלון", "באר שבע", "בית שאן", "בית שמש", "ביתר עילית", "בני ברק", "בת ים", "גבעת שמואל", "גבעתיים", "דימונה", "הוד השרון", "הרצליה", "זכרון יעקב", "חדרה", "חולון", "חיפה", "טבריה", "טירת כרמל", "יבנה", "יהוד-מונוסון", "יפו", "יקנעם", "ירושלים", "כפר סבא", "כרמיאל", "לוד", "מגדל העמק", "מודיעין-מכבים-רעות", "מודיעין עילית", "מטולה", "מעלה אדומים", "מעלות-תרשיחא", "נהריה", "נס ציונה", "נצרת עילית", "נשר", "נתיבות", "נתניה", "עכו", "עפולה", "ערד", "פתח תקווה", "צפת", "קריית אונו", "קריית ארבע", "קריית אתא", "קריית ביאליק", "קריית גת", "קריית ים", "קריית מוצקין", "קריית מלאכי", "קריית שמונה", "קריית טבעון", "ראש העין", "ראש פינה", "ראשון לציון", "רחובות", "רמלה", "רמת גן", "רמת השרון", "רעננה", "שדרות", "תל אביב"];



  //location
  const [location, setLocation] = useState(null);
  useEffect(() => {

    (async () => {
      let { status } = await Location.requestForegroundPermissionsAsync();
      if (status !== 'granted') {
        setErrorMsg('Permission to access location was denied');
        return;
      }
      let location = await Location.getCurrentPositionAsync({});
      setLocation({
        ...location,
        Address: "Current location from the device itself",
      });
    })();
  }, []);

  return (
    <Context.Provider value={{
      userData, setUserData,
      pages, setPages,
      location, setLocation,
      listUniversities, setListUniversities,
      cities, checkCityForUniversity,
      setLoadingPopup, loadingPopup
    }}>
      <NavigationContainer>
        <Stack.Navigator>
          <Stack.Screen name="HomePage" component={HomePage} options={{ headerShown: true }} />
          <Stack.Screen name="CreateDrive" component={CreateDrive} options={{ headerShown: true }} />
          <Stack.Screen name="MapRide" component={MapRide} options={{ headerShown: true }} />
        </Stack.Navigator>
      </NavigationContainer>
    </Context.Provider>
  );



  const checkCityForUniversity = (universityName) => {
    switch (universityName) {
      case "רייכמן":
        return "הרצליה";
        break;
      case "אריאל":
        return "אריאל";
        break;
      case "הפתוחה":
        return "רעננה";
        break;
      case "בן-גוריון":
        return "באר שבע";
        break;
      case "חיפה":
        return "חיפה";
        break;
      case "תל אביב":
        return "תל אביב";
        break;
      case "בר-אילן":
        return "רמת גן";
        break;
      case "מכון ויצמן":
        return "רחובות";
        break;
      case "העברית":
        return "ירושלים";
        break;
      case "הטכניון":
        return "חיפה";
        break;
      default:
        break;
    }
  }
}


