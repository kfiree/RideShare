import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';


import {
    SafeAreaView,
    Text,
    View,
    Image,
    ScrollView,
    StyleSheet,
    Button,
    TouchableWithoutFeedback,
    TouchableOpacity,
    TouchableHighlight,
    TouchableNativeFeedback,
} from 'react-native';
const Touchable = () => {
    return (

        <SafeAreaView >
            <ScrollView>
                <Button title="Click me" onPress={() => console.log("Button tapped")} />
                <Text>Hello React Native</Text>


                <TouchableWithoutFeedback onPrees={() => { console.log("image tapped TouchableWithoutFeedback"); }}>
                    <Image
                        source={{
                            width: 200,
                            height: 300,
                            uri: "https://picsum.photos/200/300",
                        }}>
                    </Image>
                </TouchableWithoutFeedback>


                <TouchableOpacity onPrees={() => { console.log("image tapped TouchableWithoutFeedback"); }}>
                    <Image
                        source={{
                            width: 200,
                            height: 300,
                            uri: "https://picsum.photos/200/300",
                        }}>
                    </Image>
                </TouchableOpacity>

                <TouchableHighlight onPrees={() => { console.log("image tapped TouchableHighlight"); }}>
                    <Image
                        source={{
                            width: 200,
                            height: 300,
                            uri: "https://picsum.photos/200/300",
                        }}>
                    </Image>
                </TouchableHighlight >

                {/* work on View in android*/}
                < TouchableNativeFeedback onPrees={() => { console.log("image tapped TouchableNativeFeedback"); }}>
                    <View style={{
                        width: 200, height: 200, backgroundColor: "blue",
                    }}
                        source={{
                            width: 200,
                            height: 300,
                            uri: "https://picsum.photos/200/300",
                        }}>
                    </View>
                </TouchableNativeFeedback >
            </ScrollView >
        </SafeAreaView >

    )
}

export default Touchable
