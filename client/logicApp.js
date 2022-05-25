import AsyncStorage from '@react-native-async-storage/async-storage';


const token = {
    addTokenToStorage: async function (token) {
        try {
            await AsyncStorage.setItem('@token', token)
            //console.log("token is saved")
            //console.log("token = ", token)
        } catch (e) {
            //console.log("token is not save")
            //console.log("error", e)
        }
    },

    getTokenFromStorage: async function () {
        try {
            const token = await AsyncStorage.getItem('@token')
            if (token !== null) {
                return token
            } else {
                return "";
            }
        } catch (e) {
            //console.log("empty val")
            //console.log("error", e)
            return "";
        }
    },


    removeTokenFromStorage: async function () {
        try {
            await AsyncStorage.removeItem('@token')
            //console.log('removed')
        } catch (e) {
            //console.log('error hen remove token')
            //console.log('error', e)
        }
    }
}

export default token;
