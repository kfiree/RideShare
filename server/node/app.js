const moment = require('moment');
const fs = require('fs');
class User {
    constructor(id, first_name, last_name, phone_number, password, email, gender, Image_Id, user_Avatar, degree, university) {
        this.id = id;
        this.first_name = first_name; //String
        this.last_name = last_name; //String
        this.phone_number = phone_number; //String
        this.password = password; //String
        this.email = email; //String
        this.gender = gender; //String
        this.Image_Id = Image_Id; //String
        this.user_Avatar = user_Avatar; //TODO: Need to cehck
        this.degree = degree; //TODO: Need to check
        this.university = university; //User_University Object
        this.rating = 1; //Number
        this.drives_offered = []; //List of User_Drives.id
        this.drives_taken = []; //List of User_Drives.id
    }
}

class University {
    constructor(university_id, university_name, university_location) {
        this.university_id = university_id;
        this.university_name = university_name;
        this.university_location = university_location; //Geo_Location object
    }
}

class Drive {
    constructor(id, src, dest, dateAndTime, seats, price, driver_id, type) {
        this.id = id;
        this.src = src; //Geo_Location id
        this.dest = dest; //Geo_Location id
        this.date = dateAndTime; //Date object
        this.seats = seats; //Number
        this.price = price; //Number
        this.driver_id = driver_id; //User.id
        this.passengers = []; //List of User_Drives.id
        this.path_id = ''; //Should be changed when inserting to DB
        this.type = type; //String
    }
}

class Geo_Location {
    constructor(geo_loaction_id, latitude, longtitude, name) {
        this.geo_loaction_id = geo_loaction_id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
    }
}

const data = [
    {
        "role": "Passenger",
        "daysPerWeek": "5",
        "leaveAvg": "07:00",
        "backAvg": "18:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "09:00",
        "backAvg": "20:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "07:00",
        "backAvg": "17:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:00",
        "backAvg": "16:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "07:15",
        "backAvg": "20:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "07:15",
        "backAvg": "19:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "2",
        "leaveAvg": "18:30",
        "backAvg": "13:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "13:00",
        "backAvg": "16:30",
        "avgScoreGiven": "3"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "07:00",
        "backAvg": "20:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "10:00",
        "backAvg": "08:30",
        "avgScoreGiven": "1"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "06:30",
        "backAvg": "18:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "08:00",
        "backAvg": "19:00",
        "avgScoreGiven": "3"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "07:45",
        "backAvg": "15:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "09:00",
        "backAvg": "17:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:20",
        "backAvg": "18:30",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "08:00",
        "backAvg": "15:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "5",
        "leaveAvg": "07:40",
        "backAvg": "19:00",
        "avgScoreGiven": "3"
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:30",
        "backAvg": "20:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "2",
        "leaveAvg": "10:00",
        "backAvg": "18:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "07:00",
        "backAvg": "16:30",
        "avgScoreGiven": "3"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "07:10",
        "backAvg": "18:30",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:00",
        "backAvg": "13:30",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "07:25",
        "backAvg": "20:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:30",
        "backAvg": "18:15",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "07:00",
        "backAvg": "16:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "20:00",
        "backAvg": "19:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "4",
        "leaveAvg": "07:40",
        "backAvg": "16:30",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "18:00",
        "backAvg": "21:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "07:30",
        "backAvg": "18:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "07:30",
        "backAvg": "15:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "07:15",
        "backAvg": "15:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "09:00",
        "backAvg": "17:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "10:00",
        "backAvg": "14:30",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "11:00",
        "backAvg": "17:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "22:30",
        "backAvg": "19:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "5",
        "leaveAvg": "08:00",
        "backAvg": "20:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "2",
        "leaveAvg": "07:30",
        "backAvg": "18:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "07:30",
        "backAvg": "18:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "4",
        "leaveAvg": "07:30",
        "backAvg": "21:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "08:15",
        "backAvg": "20:15",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "08:00",
        "backAvg": "17:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "07:00",
        "backAvg": "12:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "4",
        "leaveAvg": "07:30",
        "backAvg": "20:00",
        "avgScoreGiven": "3"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "07:30",
        "backAvg": "16:30",
        "avgScoreGiven": "2"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "1",
        "leaveAvg": "08:00",
        "backAvg": "21:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Driver",
        "daysPerWeek": "2",
        "leaveAvg": "09:00",
        "backAvg": "17:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "3",
        "leaveAvg": "08:15",
        "backAvg": "16:00",
        "avgScoreGiven": "4"
    },
    {
        "role": "Passenger",
        "daysPerWeek": "2",
        "leaveAvg": "08:00",
        "backAvg": "18:00",
        "avgScoreGiven": "5"
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "07:00",
        "backAvg": "17:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Passenger",
        "daysPerWeek": "5",
        "leaveAvg": "08:00",
        "backAvg": "18:00",
        "avgScoreGiven": "3"
    },
    {
        "role": "Driver",
        "daysPerWeek": "2",
        "leaveAvg": "08:00",
        "backAvg": "14:30",
        "avgScoreGiven": "4"
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "10:00",
        "backAvg": "16:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "1",
        "leaveAvg": "22:00",
        "backAvg": "22:00",
        "avgScoreGiven": ""
    },
    {
        "role": "Driver",
        "daysPerWeek": "3",
        "leaveAvg": "07:00",
        "backAvg": "18:00",
        "avgScoreGiven": "3"
    }
]

/*
Create Geo_location objects by this data:
- Tel-Aviv -> lat: 32.086016, lng: 34.803611
- Givat Shmuel -> lat: 32.076275, lng: 34.852509
- Ramat-Gan -> lat: 32.064180, lng: 34.822649
- Herzelyia -> lat: 32.161159, lng: 34.829042
- Holon -> lat: 32.013242, lng: 34.784749
- Bat-Yam -> lat: 32.014408, lng: 34.753555
- Rishon LeZion -> lat: 31.962383, lng: 34.807818
- Petah Tikva -> lat: 32.085851, lng: 34.889243
- Rosh HaAyin -> lat: 32.093886, lng: 34.956945
*/
const geo_locations = [
    new Geo_Location('0', '32.319348', '34.87506', 'Netanya'),
    new Geo_Location('1', '32.086016', '34.803611', 'Tel-Aviv'),
    new Geo_Location('2', '32.076275', '34.852509', 'Givat Shmuel'),
    new Geo_Location('3', '32.064180', '34.822649', 'Ramat-Gan'),
    new Geo_Location('4', '32.161159', '34.829042', 'Herzelyia'),
    new Geo_Location('5', '32.013242', '34.784749', 'Holon'),
    new Geo_Location('6', '32.014408', '34.75355', 'Bat-Yam'),
    new Geo_Location('7', '31.962383', '34.80781', 'Rishon LeZion'),
    new Geo_Location('8', '32.085851', '34.88924', 'Petah Tikva'),
    new Geo_Location('9', '32.093886', '34.956945', 'Rosh-Ha\'Ayin'),
]

const ariel_geo_location = new Geo_Location('10', '32.1065', '35.18449', 'Ariel University');
const ariel_university = new University('1', 'Ariel', ariel_geo_location);

const users = [];
const user_objects = [];
const trips = [];
const drives = [];
const locations_created = [];
let id = 0;
let locations_id = 0;

/** 
* Create users by our given data.
* @return {Array} - Array of User object.
*/
function createUsers() {
    const id_counter = users.length;
    const num_of_users_to_create = Object.keys(data).length;

    for (let i = 0; i < num_of_users_to_create; i++) {
        let user = {
            id: i + id_counter + '',
            role: data[i].role,
            daysPerWeek: data[i].daysPerWeek,
            leaveAvg: new Date(2023, 0, 1, data[i].leaveAvg.split(":")[0], data[i].leaveAvg.split(":")[1], 0, 0, 0),
            backAvg: new Date(2023, 0, 1, data[i].backAvg.split(":")[0], data[i].backAvg.split(":")[1], 0, 0, 0),
            rateToGive: data[i].avgScoreGiven,
            driverTrips: [],
        };
        users.push(user);
        user_objects.push(new User(i + '', '' + i, '' + i, '0501234567', '123456', 'mail@mail.com', 'Male', 'Image_Id', 'avatar', 'degree', ariel_university));
    }
    return users;
}

/**
 * Create trips based on our given data, and spread it randomly on a week.
 * @param {int} numOfDay Day of the week.
 * @param {Geo_Location} location Location to create trips around.
 * @returns {Array} - Array of Trip object.
 */
function createTripsPerWeek(numOfDay, location) {
    for (let i = 0; i < users.length; i++) {
        // if (users[i].role === 'Driver') {
            let userTripsPerWeek = users[i].daysPerWeek;
            let homeAddress = generateRandomPoint({ 'lat': location.latitude, 'lng': location.longtitude }, 2000);
            locations_created.push(homeAddress);

            let userLeaveArr = generateRandomDates(new Date(users[i].leaveAvg).setDate(numOfDay), new Date(users[i].leaveAvg).setDate(numOfDay), userTripsPerWeek);
            let userBackArr = generateRandomDates(new Date(users[i].backAvg).setDate(numOfDay), new Date(users[i].backAvg).setDate(numOfDay), userTripsPerWeek);
            for (let j = 0; j < userTripsPerWeek; j++) {
                let dayOfTrip = 0; //Sunday
                let tripTo = {
                    id: id + '',
                    owner: users[i].id,
                    passengers: [],
                    leaveTime: userLeaveArr[j],
                    day: dayOfTrip, //Sun-0, Mon-1, Tue-2, Wed-3, Thu-4
                };
                users[i].driverTrips.push(tripTo);
                drives.push(new Drive(
                    id + '',
                    homeAddress.geo_loaction_id,
                    ariel_geo_location.geo_loaction_id,
                    tripTo.leaveTime,
                    3,
                    15,
                    user_objects[i].id,
                    users[i].role,
                ));
                id += 1;
                let tripFrom = {
                    id: id + '',
                    owner: users[i].id,
                    passengers: [],
                    leaveTime: userBackArr[j],
                    day: dayOfTrip, //Sun-0, Mon-1, Tue-2, Wed-3, Thu-4
                };
                users[i].driverTrips.push(tripFrom);
                drives.push(new Drive(
                    id + '',
                    ariel_geo_location.geo_loaction_id,
                    homeAddress.geo_loaction_id,
                    tripFrom.leaveTime,
                    3,
                    15,
                    user_objects[i].id,
                    users[i].role,
                ));
                id += 1;
            }

            // Spread the driver trips over the week
            spreadDays(users[i].driverTrips);

            users[i].driverTrips.forEach(trip => {
                trips.push(trip);
                drives.find(drive => drive.id === trip.id).leaveTime = trip.leaveTime;
            });

            // delete all trips from driver trips list
            users[i].driverTrips = [];
        // }
    }
    return trips;
}
/**
 * Create users and trips from a given city based on our data.
 * @param {string} city - City name.
 */
function createUsersAndTrips(city) {
    //Get the city geo location
    let city_geo_location = geo_locations.find(geo_location => geo_location.name === city);

    //Create users
    createUsers();

    //Create trips for each week.
    createTripsPerWeek(1, city_geo_location);
    // createTripsPerWeek(8, city_geo_location);
    // createTripsPerWeek(15, city_geo_location);
    // createTripsPerWeek(22, city_geo_location);
}

//Create users and trips for each city
geo_locations.forEach(geo_location => {
    createUsersAndTrips(geo_location.name);
});

// Adjust hours to be in GMT+3, Israel time.
trips.forEach(trip => {
    trip.leaveTime = moment(trip.leaveTime).add(3, 'hours').toDate();
});
// Create the JSON file to hold the data we have created.
fs.writeFile('drives.json', JSON.stringify(drives), (err) => {
    if (err) throw err;
    //console.log('File has been created');
});

fs.writeFile('locations.json', JSON.stringify(locations_created), (err) => {
    if (err) throw err;
    //console.log('File has been created');
});

fs.writeFile('users.json', JSON.stringify(users), (err) => {
    if (err) throw err;
    //console.log('File has been created');
});

//Print some data to the console.
console.log('Number of users: ' + users.length);
console.log('Number of trips in 4 weeks period: ' + trips.length);

console.log(drives.length + 'Drives: ')

/**
 * Function to generate an array of random Dates around the current date
 * @param {Date} start Date to start from. 
 * @param {Date} end Date to end to.
 * @param {int} count Number of dates to generate.
 * @returns {Array} - Array of dates.
 */
function generateRandomDates(start, end, count) {
    let startDate = moment(new Date(start)).add(15, 'minutes').toDate();
    let endDate = moment(new Date(end)).subtract(15, 'minutes').toDate();
    let dates = [];

    for (let i = 0; i < count; i++) {
        let randomDate = new Date(startDate.getTime() + Math.random() * (endDate.getTime() - startDate.getTime()));
        dates.push(randomDate);
    }
    return dates;
}

/**
 * Function to spread the days of Dates array on a week according to the number of tripsPerWeek
 * @param {Array} trips Array of Trip object. 
 * @returns {Array} - Array of Trip object. Only now, the trips are spreaded.
 */
function spreadDays(trips) {
    let days = [1, 2, 3, 4, 5];
    let dates = [];

    trips.forEach(trip => {
        dates.push(trip.leaveTime);
    });

    for (let i = 0; i < dates.length / 2;) {
        let randomDay = Math.floor(Math.random() * (days.length - 1));

        dates[i] = moment(dates[i]).add((days[randomDay] - 1), 'days').toDate();

        dates[i + 1] = moment(dates[i + 1]).add((days[randomDay] - 1), 'days').toDate();

        days.splice(days.indexOf(days[randomDay]), 1);
        i++;
        i++;
    }

    let i = 0;
    trips.forEach(trip => {
        trip.leaveTime = dates[i];
        i += 1;
    });
    return dates;
}

/**
 * Function to get the day of the week of a date.
 * Sun-0, Mon-1, Tue-2, Wed-3, Thu-4, Fri-5, Sat-6.
 * @param {Date} date Date to get the day of the week.
 * @returns {int} - Day of the week.
 */
function getDay(date) {
    let day = date.getDay() % 7;
    return day;
}

// function to change the day in an array of dates
/**
 * 
 * @param {Array} dates Array of Date objects.
 * @param {int} day days to add to the dates.
 * @returns {Array} - Array of Date objects.
 */
function changeDay(dates, day) {
    for (let i = 0; i < dates.length; i++) {
        dates[i].setDate(dates[i].getDate() + day);
    }
    return dates;
}

/**
* Generates number of random geolocation points given a center and a radius.
* @param  {Object} center A JS object with lat and lng attributes.
* @param {number} count Number of points to generate.
* @return {array} Array of Objects with lat and lng attributes.
*/
function generateRandomPoints(center, count) {
    var points = [];
    for (var i = 0; i < count; i++) {
        points.push(generateRandomPoint(center, 2000));
    }
    return points;
}

/**
* Generates number of random geolocation points given a center and a radius.
* Reference URL: http://goo.gl/KWcPE.
* @param  {Object} center A JS object with lat and lng attributes.
* @param  {number} radius Radius in meters.
* @return {Object} The generated random points as JS object with lat and lng attributes.
*/
function generateRandomPoint(center, radius) {
    var x0 = center.lng;
    var y0 = center.lat;
    // Convert Radius from meters to degrees.
    var rd = radius / 111300;

    var u = Math.random();
    var v = Math.random();

    var w = rd * Math.sqrt(u);
    var t = 2 * Math.PI * v;
    var x = w * Math.cos(t);
    var y = w * Math.sin(t);

    var xp = x / Math.cos(y0);

    // Resulting point.
    let ans = new Geo_Location(locations_id + '', '' + (y + parseFloat(y0)), '' + (xp + parseFloat(x0)), 'User Address');
    locations_id++;
    return ans;
    // return { 'lat': y + y0, 'lng': xp + x0 };
}