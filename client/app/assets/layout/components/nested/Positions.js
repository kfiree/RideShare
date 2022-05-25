import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';


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
import Login from '../screens/Login';
import Registration from '../screens/Registration';
import ButtonAlert from './ButtonAlert';
import Touchable from './Touchable';

/*size Screen*/
const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;


export default function Positions() {



  return (
    /*

    justifyContent ->  center
    alignItems ->  center

    */
    // <View style={{
    //   backgroundColor: "white",
    //   flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
    //   justifyContent: "center",// by main [center, flex-start, flex-end, space-around, space-evenly, space-between ]
    //   alignItems: "center",// by main [center, baseline, flex-start, flex-end, stretch]
    //   flex: 1,
    // }}>

    //   <View style={{
    //     backgroundColor: "blue",
    //     width: 100,
    //     height: 300,
    //     // alignSelf: "flex-start",
    //   }} />

    //   <View style={{
    //     backgroundColor: "gold",
    //     width: 100,
    //     height: 200,
    //   }} />

    //   <View style={{
    //     backgroundColor: "tomato",
    //     width: 100,
    //     height: 100,
    //   }} />
    // </View>





    /*

     alignItems ->  stretch

    */
    // <View style={{
    //   backgroundColor: "white",
    //   flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
    //   justifyContent: "center",// by main [center, flex-start, flex-end, space-around, space-evenly, space-between ]
    //   alignItems: "stretch",// by main [center, baseline, flex-start, flex-end, stretch]
    //   flex: 1,
    // }}>

    //   <View style={{
    //     backgroundColor: "blue",
    //     width: 100,
    //     // height: 300,
    //     // alignSelf: "flex-start",
    //   }} />

    //   <View style={{
    //     backgroundColor: "gold",
    //     width: 100,
    //     height: 200,
    //     alignSelf: "center",

    //   }} />

    //   <View style={{
    //     backgroundColor: "tomato",
    //     width: 100,
    //     height: 100,
    //     alignSelf: "center",
    //   }} />
    // </View>



    /*

      flexWrap

    */
    // <View style={{
    //   backgroundColor: "white",
    //   flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
    //   justifyContent: "center",// by main [center, flex-start, flex-end, space-around, space-evenly, space-between ]
    //   alignItems: "center",// by main [center, baseline, flex-start, flex-end, stretch]
    //   alignContent: "center",// work with wrap only
    //   flexWrap: "wrap",
    //   flex: 1,
    // }}>

    //   <View style={{
    //     backgroundColor: "blue",
    //     width: 80,
    //     height: 300,
    //   }} />

    //   <View style={{
    //     backgroundColor: "gold",
    //     width: 80,
    //     height: 80,

    //   }} />

    //   <View style={{
    //     backgroundColor: "tomato",
    //     width: 80,
    //     height: 80,
    //   }} />

    //   <View style={{
    //     backgroundColor: "green",
    //     width: 80,
    //     height: 80,
    //   }} />

    //   <View style={{
    //     backgroundColor: "greenyellow",
    //     width: 80,
    //     height: 80,
    //   }} />


    // </View>





    /*

    flexBasis -> 100 // width or height
    flexShrink -> 1 // overFlow Fix On Screens
    flex -> -1 // overFlow Fix On Screens
    
    */
    // <View style={{
    //   backgroundColor: "white",
    //   flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
    //   justifyContent: "center",// by main [center, flex-start, flex-end, space-around, space-evenly, space-between ]
    //   alignItems: "center",// by main [center, baseline, flex-start, flex-end, stretch]
    //   flex: 1,
    // }}>

    //   <View style={{
    //     backgroundColor: "blue",
    //     // flexBasis: 100, // width or height
    //     // flex: -1, == flexShrink: 1,
    //     flex: -1,
    //     width: 400,
    //     // flexShrink: 1,
    //     height: 100,
    //   }} />

    //   <View style={{
    //     backgroundColor: "gold",
    //     width: 100,
    //     height: 100,

    //   }} />

    //   <View style={{
    //     backgroundColor: "tomato",
    //     width: 100,
    //     height: 100,
    //   }} />

    // </View>






    /*

    basic position is relative
    position -> relative/absolute
    */

    <View style={{
      backgroundColor: "white",
      flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
      justifyContent: "center",// by main [center, flex-start, flex-end, space-around, space-evenly, space-between ]
      alignItems: "center",// by main [center, baseline, flex-start, flex-end, stretch]
      alignContent: "center",
      flex: 1,
    }}>

      <View style={{
        backgroundColor: "blue",
        width: 100,
        height: 100,
      }} />

      <View style={{
        backgroundColor: "gold",
        width: 100,
        height: 100,
        top: "50%",
        right: 20,
        position: "absolute",
        alignSelf: "center",
      }} />

      <View style={{
        backgroundColor: "tomato",
        width: 100,
        height: 100,
      }} />

    </View>


  );
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
