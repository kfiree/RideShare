import React, { useState, useLayoutEffect, useEffect, useRef, createContext } from 'react';
import CheckRequests from './app/assets/layout/components/nested/CheckRequests';
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

import { useDimensions, useDeviceOrientation } from '@react-native-community/hooks';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useFonts } from 'expo-font';
import AsyncStorage from '@react-native-async-storage/async-storage';
import config from './react-native.config';

/*components*/
import BenefitsPurchased from './app/assets/layout/components/screens/BenefitsPurchased';
import Charges from './app/assets/layout/components/screens/Charges';
import ChatDrive from './app/assets/layout/components/screens/ChatDrive';
import CreateDrive from './app/assets/layout/components/screens/CreateDrive';
import HomePage from './app/assets/layout/components/screens/HomePage';
import Login from './app/assets/layout/components/screens/Login';
import MyBenefits from './app/assets/layout/components/screens/MyBenefits';
import MyDrives from './app/assets/layout/components/screens/MyDrives';
import MySuggestions from './app/assets/layout/components/screens/MySuggestions';
import Notification from './app/assets/layout/components/screens/Notification';
import Payments from './app/assets/layout/components/screens/Payments';
import Profile from './app/assets/layout/components/screens/Profile';
// import MapRide from './app/assets/layout/components/screens/MapRide';
import Registration from './app/assets/layout/components/screens/Registration';
import SearchDrive from './app/assets/layout/components/screens/SearchDrive';
import Setting from './app/assets/layout/components/screens/Setting';
import MySuggestionsDrive from './app/assets/layout/components/screens/MySuggestionsDrive';
import MySuggestionsPassenger from './app/assets/layout/components/screens/MySuggestionsPassenger';
import WelcomeScreen from './app/assets/layout/components/screens/WelcomeScreen';
import MyChats from './app/assets/layout/components/screens/MyChats';
import Rating from './app/assets/layout/components/screens/Rating';
import Menu from './app/assets/layout/components/screens/Menu';
import ButtonAlert from './app/assets/layout/components/nested/ButtonAlert';
import Touchable from './app/assets/layout/components/nested/Touchable';
// import NotificationLogic from './NotificationLogic';
import token from './logicApp'
import { Context } from './Context'
import Constants from 'expo-constants';
import * as Notifications from 'expo-notifications';
const ReactNative = require('react-native');

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;
const Stack = createNativeStackNavigator();
Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: false,
    shouldSetBadge: false,
  }),
});
export default function App() {

  const [detailsDrive, seDetailsDrive] = useState([]);
  const [searchDrive, setSearchDrive] = useState([]);
  const [search, setSearch] = useState(false);
  const [suggestionsDriver, setSuggestionsDriver] = useState([]);
  const [loadingPopup, setLoadingPopup] = React.useState(false);
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
  const [isSignedIn, setIsSignedIn] = useState(true);//*

  // useLayoutEffect(() => {
  //   if (userData.token !== "") {
  //     setIsSignedIn(true)
  //   } else {
  //     setIsSignedIn(false)
  //   }
  // }, [isSignedIn])
  useEffect(() => {
    /**
 * disable rtl
 */
    try {
      ReactNative.I18nManager.allowRTL(false);
    } catch (e) {
      //console.log(e);
    }
  })

  const remove = () => {
    token.removeTokenFromStorage();
    setIsSignedIn(false)
  }

  const [fonts] = useFonts({//*
    CalibriRegular: require('./app/assets/fonts/Calibri-Regular.ttf'),
  });

  const cities = ["אופקים", "אור יהודה", "אור עקיבא", "אילת", "אלעד", "אריאל", "אשדוד", "אשקלון", "באר שבע", "בית שאן", "בית שמש", "ביתר עילית", "בני ברק", "בת ים", "גבעת שמואל", "גבעתיים", "דימונה", "הוד השרון", "הרצליה", "זכרון יעקב", "חדרה", "חולון", "חיפה", "טבריה", "טירת כרמל", "יבנה", "יהוד-מונוסון", "יפו", "יקנעם", "ירושלים", "כפר סבא", "כרמיאל", "לוד", "מגדל העמק", "מודיעין-מכבים-רעות", "מודיעין עילית", "מטולה", "מעלה אדומים", "מעלות-תרשיחא", "נהריה", "נס ציונה", "נצרת עילית", "נשר", "נתיבות", "נתניה", "עכו", "עפולה", "ערד", "פתח תקווה", "צפת", "קריית אונו", "קריית ארבע", "קריית אתא", "קריית ביאליק", "קריית גת", "קריית ים", "קריית מוצקין", "קריית מלאכי", "קריית שמונה", "קריית טבעון", "ראש העין", "ראש פינה", "ראשון לציון", "רחובות", "רמלה", "רמת גן", "רמת השרון", "רעננה", "שדרות", "תל אביב"];
  const universities = ["הטכניון", "העברית", "מכון ויצמן ", "בר-אילן", "תל אביב", "חיפה", "בן-גוריון", "הפתוחה", "אריאל", "רייכמן",];
  // const universities = {"רייכמן":(32.17588740000071, 34.837644153970544), 
  //   "אריאל":(32.10360956032626,35.20770243498428),
  //   "הפתוחה":(32.18891487243344, 34.88751333862955),
  //   "בן-גוריון":(31.26461748848722, 34.803245319464644),
  //   "חיפה":(32.76135742227855, 35.01977589205568),
  //   "תל אביב":(32.11734877512516, 34.80584682164881),
  //   "בר-אילן":(32.069671666950626, 34.842915938629545),
  //   "מכון ויצמן":(31.903772297455504, 34.80805295484482),
  //   "העברית":(31.794594256857227, 35.24201244080252),
  //   "הטכניון":(32.77702183834821, 35.0223760787042)};

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

  /**
   * disable rtl
   */
  try {
    ReactNative.I18nManager.allowRTL(false);
  } catch (e) {
    //console.log(e);
  }
  const [expoPushToken, setExpoPushToken] = useState('');
  const [notification, setNotification] = useState(false);
  const notificationListener = useRef();
  const responseListener = useRef();

  useEffect(() => {
    registerForPushNotificationsAsync().then(token => setExpoPushToken(token));

    notificationListener.current = Notifications.addNotificationReceivedListener(notification => {
      setNotification(notification);
    });

    responseListener.current = Notifications.addNotificationResponseReceivedListener(response => {
      console.log(response);
    });

    return () => {
      Notifications.removeNotificationSubscription(notificationListener.current);
      Notifications.removeNotificationSubscription(responseListener.current);
    };
  }, []);
  const sendNotificationToClient = async (title, msg) => {
    await schedulePushNotification(title, msg);
  }

  return (
    <Context.Provider value={{
      userData, setUserData,
      loadingPopup, setLoadingPopup,
      isSignedIn, setIsSignedIn,
      cities, universities, checkCityForUniversity,
      searchDrive, setSearchDrive,
      suggestionsDriver, setSuggestionsDriver,
      detailsDrive, seDetailsDrive,
      search, setSearch,
      sendNotificationToClient
    }}>
      <NavigationContainer>
        <Stack.Navigator>
          {/* {!isSignedIn ? (
            <>
              <Stack.Screen name="Login" component={Login} options={{ headerShown: false }} />
              <Stack.Screen name="Registration" component={Registration} options={{ headerShown: false }} />
            </>) :
            (<> */}


          <Stack.Screen name="HomePage" component={HomePage} options={{ headerShown: false }} />
          <Stack.Screen name="ChatDrive" component={ChatDrive} options={{ headerShown: false }} />
          <Stack.Screen name="MySuggestionsDrive" component={MySuggestionsDrive} options={{ headerShown: false }} />
          <Stack.Screen name="CreateDrive" component={CreateDrive} options={{ headerShown: false }} />
          <Stack.Screen name="SearchDrive" component={SearchDrive} options={{ headerShown: false }} />
          <Stack.Screen name="Notification" component={Notification} options={{ headerShown: false }} />
          <Stack.Screen name="Rating" component={Rating} options={{ headerShown: false }} />
          <Stack.Screen name="Profile" component={Profile} options={{ headerShown: false }} />
          <Stack.Screen name="MySuggestionsPassenger" component={MySuggestionsPassenger} options={{ headerShown: false }} />
          <Stack.Screen name="Menu" component={Menu} options={{ headerShown: false }} />
          <Stack.Screen name="Payments" component={Payments} options={{ headerShown: false }} />
          <Stack.Screen name="MyChats" component={MyChats} options={{ headerShown: false }} />
          <Stack.Screen name="MyBenefits" component={MyBenefits} options={{ headerShown: false }} />
          <Stack.Screen name="WelcomeScreen" component={WelcomeScreen} options={{ headerShown: false }} />
          <Stack.Screen name="BenefitsPurchased" component={BenefitsPurchased} options={{ headerShown: false }} />
          <Stack.Screen name="Charges" component={Charges} options={{ headerShown: false }} />
          <Stack.Screen name="MyDrives" component={MyDrives} options={{ headerShown: false }} />
          <Stack.Screen name="MySuggestions" component={MySuggestions} options={{ headerShown: false }} />
          <Stack.Screen name="Setting" component={Setting} options={{ headerShown: false }} />
          <Stack.Screen name="Login" component={Login} options={{ headerShown: false }} />
          <Stack.Screen name="Registration" component={Registration} options={{ headerShown: false }} />
          {/* <Stack.Screen name="MapRide" component={MapRide} options={{ headerShown: false }} /> */}
          {/* </>)
          } */}
        </Stack.Navigator>
      </NavigationContainer>
    </Context.Provider>
  );
}
const styles = StyleSheet.create({
  background: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    fontFamily: "CalibriRegular"
  },
});

async function schedulePushNotification(title, msg) {
  await Notifications.scheduleNotificationAsync({
    content: {
      title: title,
      body: msg,
      // data: { data: 'goes here' },
    },
    trigger: { seconds: 2 },
  });
}

async function registerForPushNotificationsAsync() {
  let token;
  if (Constants.isDevice) {
    const { status: existingStatus } = await Notifications.getPermissionsAsync();
    let finalStatus = existingStatus;
    if (existingStatus !== 'granted') {
      const { status } = await Notifications.requestPermissionsAsync();
      finalStatus = status;
    }
    if (finalStatus !== 'granted') {
      alert('Failed to get push token for push notification!');
      return;
    }
    token = (await Notifications.getExpoPushTokenAsync()).data;
    // console.log(token);
  } else {
    alert('Must use physical device for Push Notifications');
  }

  if (Platform.OS === 'android') {
    Notifications.setNotificationChannelAsync('default', {
      name: 'default',
      importance: Notifications.AndroidImportance.MAX,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: '#FF231F7C',
    });
  }

  return token;
}

