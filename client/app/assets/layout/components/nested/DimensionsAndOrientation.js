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


/*components*/
import Login from './assets/layout/components/screens/Login';
import Registration from './assets/layout/components/screens/Registration';
import ButtonAlert from './assets/layout/components/nested/ButtonAlert';
import Touchable from './assets/layout/components/nested/Touchable';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;


export default function DimensionsAndOrientation() {
  //console.log("useDimensions = ");
  //console.log(useDimensions());
  //console.log("useDeviceOrientation = ");
  //console.log(useDeviceOrientation());

  const { Orientation } = useDeviceOrientation();
  // const { Dimensions } = useDeviceOrientation();
  const [fonts] = useFonts({
    CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
  });

  if (!fonts) {
    return null;
  } else {
    return (
      <SafeAreaView style={styles.container}>
        <View style={{
          backgroundColor: "blue",
          width: "100%",
          height: Orientation ? "100%" : "30%",
        }}>


        </View>
      </SafeAreaView>
    );
  }
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    paddingTop: Platform.OS === "android" ? StatusBar.currentHeigh : 0,
  },
  imageBackground: {
    flex: 1,
    width: windowWidth,
    height: windowHeight,
    justifyContent: "center"
  },
});
