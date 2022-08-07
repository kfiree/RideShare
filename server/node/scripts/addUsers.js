var fs = require("fs");
// const { User } = require('./classes/User');
const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const client = require('./../config/DB');
const config = require('./../config/default.json');

const user_query = require('./../config/querys/user_query');

const getRandomUser = require('./../classes/randomData/user');
let User = require('./../classes/User');



// Read Synchrously
var content = fs.readFileSync("users.json");
// console.log('content', JSON.parse(content));
const users = JSON.parse(content);
const arr = {
    arr: []
};
// console.log('user', user);
const addUser = async () => {
    // console.log('user', user);
    for (let index = 0; index < 600; index++) {
        let user = new User(getRandomUser());
        try {
            //Encrypt password
            const salt = await bcrypt.genSalt(10);
            const encryptedPassword = await bcrypt.hash(user.password, salt);

            user = {
                ...user,
                password: encryptedPassword
            }
            arr.arr.push(user.user_Id);
            query = user_query.addUser(user);
            //Save user in DB
            await client.query(query,
                async (err, result, fields) => {
                    if (err) console.log('err', err);
                    else if (result.rowCount === 1) {
                        console.log('User created');

                    } else {
                        console.log('User is not created');
                    }
                });

        } catch (err) {
            console.error(err.message);
        }
    }
}

const run = async () => {
    try {
        await addUser();
    } catch (error) {
        console.log('error', error);
    }
    fs.writeFile('createdUsers.json', JSON.stringify(arr), (err) => {
        if (err) throw err;
        console.log('File has been created');
    });
}

run();











