import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import {
    SafeAreaView,
    Text,
    View,
    ScrollView,
    StyleSheet,
    Button,
    Alert,
} from 'react-native';
const ButtonAlert = () => {
    const { userData, setUserData, loadingPopup, setLoadingPopup, isSignedIn, setIsSignedIn } = useContext(Context)
    const [fonts] = useFonts({
        CalibriRegular: require('./../../../fonts/Calibri-Regular.ttf'),
    });
    return (
        /** Button and box in ios */
        <SafeAreaView style={styles.container}>
            <SafeAreaView>
                <Button
                    color="orange"
                    title="Click me"
                    onPress={() =>
                        Alert.prompt("My Title", "My message", (text) => console.log(text))
                    }
                />
            </SafeAreaView>

            <SafeAreaView>
                {/* <Button
                    title="Click me"
                    onPress={() => Alert.alert("My Title", "My message", [
                        {
                            text: "yes", onPress: () => console.log("yes")
                        },
                        { text: "no", onPress: () => console.log("no") },
                    ])}
                    color="blue"
                /> */}
            </SafeAreaView>

        </SafeAreaView>
    )
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
});
export default ButtonAlert
