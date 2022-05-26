import React, { useState, useLayoutEffect, useRef, useEffect, createContext, useReducer, useContext } from 'react';
import { Context } from '../../../../../Context';
import {
  View,
  StyleSheet,
  Text,
  Alert,
  TouchableOpacity,
  Dimensions
} from 'react-native';
import _Styles from '../src/utils/_Styles';
import MapView, { Marker } from 'react-native-maps';
import { Octicons, FontAwesome } from '@expo/vector-icons';
import colors from '../../layout'
import { useNavigation, NavigationContainer } from '@react-navigation/native';
import Geocoder from 'react-native-geocoding';
import { GooglePlacesAutocomplete } from 'react-native-google-places-autocomplete';
import MapFooter from "./../nested/MapFooter";
import { SafeAreaView } from 'react-native-safe-area-context';


const windowWidth = Dimensions.get('screen').width;
const windowHeight = Dimensions.get('screen').height;

const sizeHeader = windowWidth / 4;

/** 0 = mapRetroStyle, 1 = mapNightStyle */
const mapStyles = [[
  {
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#ebe3cd"
      }
    ]
  },
  {
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#523735"
      }
    ]
  },
  {
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#f5f1e6"
      }
    ]
  },
  {
    "featureType": "administrative",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#c9b2a6"
      }
    ]
  },
  {
    "featureType": "administrative.land_parcel",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#dcd2be"
      }
    ]
  },
  {
    "featureType": "administrative.land_parcel",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "administrative.land_parcel",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#ae9e90"
      }
    ]
  },
  {
    "featureType": "landscape.natural",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#dfd2ae"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#dfd2ae"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#93817c"
      }
    ]
  },
  {
    "featureType": "poi.business",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#a5b076"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#447530"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#f5f1e6"
      }
    ]
  },
  {
    "featureType": "road.arterial",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#fdfcf8"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#f8c967"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#e9bc62"
      }
    ]
  },
  {
    "featureType": "road.highway.controlled_access",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#e98d58"
      }
    ]
  },
  {
    "featureType": "road.highway.controlled_access",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#db8555"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#806b63"
      }
    ]
  },
  {
    "featureType": "transit.line",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#dfd2ae"
      }
    ]
  },
  {
    "featureType": "transit.line",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#8f7d77"
      }
    ]
  },
  {
    "featureType": "transit.line",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#ebe3cd"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#dfd2ae"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry.fill",
    "stylers": [
      {
        "color": "#b9d3c2"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#92998d"
      }
    ]
  }
], [
  {
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#242f3e"
      }
    ]
  },
  {
    "featureType": "administrative.land_parcel",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "administrative.locality",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "poi.business",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#263c3f"
      }
    ]
  },
  {
    "featureType": "poi.park",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#6b9a76"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#38414e"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#212a37"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.icon",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "road",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#9ca5b3"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#746855"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "geometry.stroke",
    "stylers": [
      {
        "color": "#1f2835"
      }
    ]
  },
  {
    "featureType": "road.highway",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#f3d19c"
      }
    ]
  },
  {
    "featureType": "road.local",
    "elementType": "labels",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "transit",
    "stylers": [
      {
        "visibility": "off"
      }
    ]
  },
  {
    "featureType": "transit",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#2f3948"
      }
    ]
  },
  {
    "featureType": "transit.station",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#d59563"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "geometry",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.fill",
    "stylers": [
      {
        "color": "#515c6d"
      }
    ]
  },
  {
    "featureType": "water",
    "elementType": "labels.text.stroke",
    "stylers": [
      {
        "color": "#17263c"
      }
    ]
  }
]];
const uniLocations = {
  "רייכמן": (32.17588740000071, 34.837644153970544),
  "אריאל": {
    latitude: 32.08947627870616, longitude: 34.85937582287804
  },
  "הפתוחה": {
    latitude: 32.18891487243344, longitude: 34.88751333862955
  },
  "בן-גוריון": {
    latitude: 31.26461748848722, longitude: 34.803245319464644
  },
  "חיפה": {
    latitude: 32.76135742227855, longitude: 35.01977589205568
  },
  "תל אביב": {
    latitude: 32.11734877512516, longitude: 34.80584682164881
  },
  "בר-אילן": {
    latitude: 32.069671666950626, longitude: 34.842915938629545
  },
  "מכון ויצמן": {
    latitude: 31.903772297455504, longitude: 34.80805295484482
  },
  "העברית": {
    latitude: 31.794594256857227, longitude: 35.24201244080252
  },
  "הטכניון": {
    latitude: 32.77702183834821, longitude: 35.0223760787042
  }
};


Geocoder.init("AIzaSyAd5hzjCJvSsInyFrQ45-XZB5mfqdDmFYQ");

export default function MapRide({ route }) {
  const Driver = route.params?.isDriver;
  const markerLocation = route.params?.Location;
  // console.log('route.params', route.params);
  const { userData, setUserData, pages, setPages, location, setLocation, listUniversities, setListUniversities, checkCityForUniversity, cities, universities, loadingPopup, setLoadingPopup } = useContext(Context)
  useEffect(() => {
    setPages({
      ...pages,
      curr: 'MapRide',
    })
  }, []);
  //console.log('pages', pages)

  const [userMarker, setUserMarker] = useState(null);
  if (location) {
    setLocation(location)
  }

  /**
   * marker location
   */

  const onUserLocationChange = async (newLocation) => {

    var addStr = await coordinatesToAddress(newLocation);
    // setUserMarker({ coordinates: { latitude: newLocation.latitude, longitude: newLocation.longitude }, Address: addStr })
  }

  /**
   * @param location as coordinates
   * @returns address string 
   */
  const coordinatesToAddress = async (location) => {
    const addStr = await Geocoder.from({
      latitude: location.latitude,
      longitude: location.longitude
    });
    return addStr.results[0].formatted_address;
  }

  /**
   * showed region on map
   */
  const [Region, setRegion] = useState(null);
  const onRegionChange = (region) => {
    //console.log('new-region', region)
    setRegion({ region })
  }


  const navigation = useNavigation();

  return (
    <View style={styles.body}>

      {/* <GooglePlacesAutocomplete
        placeholder={'userMarker.Address'}
        minLength={2}
        autoFocus={false}
        returnKeyType={'default'}
        listViewDisplayed="auto"
        fetchDetails={true}
        onPress={(data, details = null) => {

          // Alert.alert('data', JSON.stringify(data.description), [
          //   { text: "Ok", onPress: () => console.log("Ok") },
          // ]);

          // Alert.alert('details', JSON.stringify(details.geometry.location), [
          //   { text: "Ok", onPress: () => console.log("Ok") },
          // ]);

          // let loc = new location();
          // console.log('loc', loc.toString())
          setUserMarker({
            Address: location.Address,
            coordinates: {
              longitude: location.coords.longitude,
              latitude: location.coords.latitude
            }
          })
        }}
        getDefaultValue={() => {
          return '';
        }}
        query={{
          // available options: https://developers.google.com/places/web-service/autocomplete
          key: 'AIzaSyAd5hzjCJvSsInyFrQ45-XZB5mfqdDmFYQ',
          language: 'en',
        }}
        styles={{
          container: {
            marginTop: 50,
            zIndex: 9990,
            position: 'absolute',
            width: '100%'
          },
          // textInputContainer: {
          //     backgroundColor: 'rgba(0,0,0,0)',
          //     borderTopWidth: 0,
          //     borderBottomWidth: 0,
          //     height: 200,
          // },
          textInput: {
            marginLeft: 5,
            marginRight: 5,
            color: '#5d5d5d',
            fontSize: 24,
            height: 60,
          },
          predefinedPlacesDescription: {
            color: '#1faadb'
          },
          // listView: {
          //     flex: 1,
          //     position: 'absolute',
          //     top: 40,
          //     backgroundColor: 'red',
          //     marginHorizontal: 5,
          //     width: width,
          //     minHeight: 160
          // },
          listView: {
            position: 'absolute',
            zIndex: 9990,
            top: 40
          },
          row: {
            height: 40
          },
          poweredContainer: {
            display: 'none'
          },
          powered: {
            display: 'none'
          }
        }}
      /> */}


      <Text style={[
        _Styles.CustomFont,
        styles.smallText,
      ]}>

      </Text>
      <MapView
        style={styles.map}
        CustomMapStyle={mapStyles[1]}
        region={
          Region ?
            Region :
            {
              longitude: location?.coords?.longitude || 0,
              latitude: location?.coords?.latitude || 0,
              latitudeDelta: 0.0922,
              longitudeDelta: 0.0421,
            }
        }
        onRegionChange={onRegionChange}
        showsUserLocation={true}
        onPress={(e) => { onUserLocationChange(e.nativeEvent.coordinate) }}
      >
        {listUniversities.map((university) => {
          //console.log('longitude', parseFloat(university.longitude))
          //console.log('latitude', parseFloat(university.latitude))


          return <Marker
            key={university.university_Id}
            coordinate={{
              longitude: parseFloat(university.longitude),
              latitude: parseFloat(university.longitude)
            }}
            title={university.nameLocation}
            description={university.university_name}
            image={require('./../../../assets/images/icons/UniversityMarker.png')} />

          return <Marker
            key={index}
            coordinate={item.coordinates}
            title={item.title}
            description={item.description}
            image={require('./../../../assets/images/icons/UniversityMarker.png')}
          />

        })}

        {/* <Marker coordinate={userMarker.coordinates} image={Driver ? require('./../../../assets/images/icons/carMarker.png') : require('./../../../assets/images/icons/userMarker.png')}></Marker> */}

      </MapView>
      {/* <MapFooter isDriver={true} Pos={'userMarker'} style={{ position: "absolute", justifyContent: "flex-end", flex: 1 }} /> */}
    </View>

  );
}


const styles = StyleSheet.create({
  body: {
    flex: 1,
    alignItems: 'center',
  },
  text: {
    fontSize: 40,
    margin: 10,
  },
  smallText: {
    fontSize: 8,
  },
  map: {
    width: '99%',
    height: '90%',
    zIndex: 0
  },
  row: {
    backgroundColor: "white",
    flexDirection: "row",//[row, row-reverse -> {horizontal}], [column, column-reverse -> {horizontal}]
    borderTopWidth: 1,
  },
  listView: {
    position: 'absolute',
    zIndex: 9999,
    top: 40
  },
  boxSize: {
    width: sizeHeader,
    height: (sizeHeader / 1.5),
    position: 'absolute',
    bottom: 30,
    justifyContent: 'center',
    alignItems: 'center',
    zIndex: 100
  },
  AddText: {
    fontSize: (Platform.OS === 'android') ? 13 : 15,
    fontFamily: "CalibriRegular",
    alignItems: 'center',
    fontWeight: 'bold',
  },
  myButton: {
    flex: 1,
    flexDirection: 'row',
    position: 'absolute',
    bottom: 10,
    alignSelf: "center",
    justifyContent: "space-between",
    backgroundColor: "transparent",
    borderWidth: 0.5,
    borderRadius: 20
  },
})
