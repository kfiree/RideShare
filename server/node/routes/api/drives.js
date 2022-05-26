const express = require('express');
const { json } = require('express/lib/response');
const client = require('../../config/DB');
const { check, validationResult } = require('express-validator');
const { v4: uuidv4 } = require('uuid');
const bcrypt = require('bcryptjs');
const config = require('../../config/default.json');
const jwt = require('jsonwebtoken');
const router = express.Router();



//@router           POST api/users
//@description      add drive
//@access           Public
router.post(
    '/',
    [
        check('user_Id', 'user_Id is required').not().isEmpty(),
        check('date', 'date is required').isDate(),
        check('AVG_Price', 'AVG_Price is required').not().isEmpty(),
        check('price', 'price is required').not().isEmpty(),
        check('seats', 'seats is required').not().isEmpty(),
        check('src', 'src is required').isObject(),
        check('dest', 'dest is required').isObject(),
        check('upcoming_Drives', 'upcoming_Drives is required').not().isEmpty()
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { user_Id, date, price, AVG_Price, seats, src, dest, upcoming_Drives } = req.body;



        try {
            //get all users

            // const queryGetAllUsers = `SELECT * FROM asaf.users;`;
            // //insert Users_Drive in db
            // await client.query(queryGetAllUsers,
            //     (err, result, fields) => {
            //         if (err) return res.status(400).json({ errors: [{ msg: err }] });
            //         else {
            //             // console.log('result', result);
            //             res.status(200).json({ msg: "msg" })
            //         }
            //     });

            const drive = {
                driveId: uuidv4(),
                user_Id,
                date,
                price,
                AVG_Price,
                seats,
                src,
                dest,
                src,
                dest,
                upcoming_Drives
            }
            drive.src['geoLocation_Id'] = uuidv4();
            drive.dest['geoLocation_Id'] = uuidv4();
            console.log("drive", drive);

            //insert geoLocation
            const createGeoLocartion = `INSERT INTO asaf.geoLocation (geoLocation_Id, latitude, latitudeDelta, longitude, longitudeDelta) 
                                    VALUES
                                        ('${drive.src.geoLocation_Id}',${drive.src.latitude}, ${drive.src.latitudeDelta}, ${drive.src.longitude}, ${drive.src.longitudeDelta}),
                                        ('${drive.dest.geoLocation_Id}',${drive.dest.latitude}, ${drive.dest.latitudeDelta}, ${drive.dest.longitude}, ${drive.src.longitudeDelta});`;
            //insert geoLocation in db
            await client.query(createGeoLocartion,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: err }] });
                    else {
                        console.log('createGeoLocartion created');
                        //insert Drive
                        const createDrive = `INSERT INTO asaf.drives ( drive_Id, geoLocationSrc_Id, geoLocationDest_Id, passengers, num_seat_available, price, AVG_Price, upcoming_Drives, createdAt) 
                        VALUES ('${drive.driveId}', '${drive.src.geoLocation_Id}', '${drive.dest.geoLocation_Id}', '', 
                        ${drive.seats}, '${drive.price}', '${drive.AVG_Price}', '${drive.upcoming_Drives}', now());`;
                        //insert Drive in db
                        await client.query(createDrive,
                            async (err, result, fields) => {
                                if (err) return res.status(400).json({ errors: [{ msg: err }] });
                                else {
                                    console.log('createDrive created');
                                    //insert Users_Drive
                                    const createUsersDrive = `INSERT INTO asaf.users_Drives (user_Id, drive_Id) 
                                                            VALUES 
                                                                ( '${drive.user_Id}', '${drive.driveId}')`;
                                    //insert Users_Drive in db
                                    await client.query(createUsersDrive,
                                        (err, result, fields) => {
                                            if (err) return res.status(400).json({ errors: [{ msg: err }] });
                                            else {
                                                console.log('createUsersDrive created');
                                                res.status(200).json({ msg: "msg" })
                                            }
                                        });
                                }
                            });
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })



//@router           POST api/users
//@description      Delete drive
//@access           Public
router.post(
    '/login',
    [
        check('email', 'Please include a valid Email').isEmail(),
        check('password', 'Please enter a password with 6  or more characters').isLength({ min: 6 })
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { email, password } = req.body;
        // console.log("login");
        try {
            const query = `SELECT * FROM asaf.users WHERE email = '${email.toLowerCase()}';`;
            //Check if user is exist
            await client.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: err });
                    else if (result.length > 0) {
                        if (result[0].permission === '') {
                            return res.status(400).json({ errors: 'the administrator has not yet authorized this user, please contact your organization manager' });
                        }
                        const user = {
                            userId: result[0].user_Id,
                            password: result[0].password,
                            email: result[0].email.toLowerCase(),
                            first_name: result[0].first_name,
                            last_name: result[0].last_name,
                            phone_Number: result[0].phone_Number,
                            gender: result[0].gender,
                            user_Avatar: result[0].user_Avatar,
                            degree: result[0].degree,
                            token: "",
                        };
                        //Encrypt password
                        const salt = await bcrypt.genSalt(10);
                        const encryptedPassword = await bcrypt.hash(password, salt);
                        if (result[0].email === email.toLowerCase() && bcrypt.compareSync(password, result[0].password)) {
                            console.log('User Login successfully');
                            //Return jsonwebToken
                            const payLoad = { user: { id: result[0].userId } };
                            jwt.sign(
                                payLoad,
                                config["jwtSecret"],
                                { expiresIn: 360000 },
                                (err, token) => {
                                    if (err) return res.status(400).json({ errors: err });
                                    else {
                                        user.token = token;
                                        return res.json({ user })
                                    };
                                }
                            );
                        } else {
                            // console.log('User authentication failed');
                            return res.status(400).json({ errors: "User authentication failed" });
                        }
                    } else {
                        return res.status(400).json({ errors: 'The user is not exist' });
                    }
                });
        } catch (err) {
            //console.error(err.message);
            res.status(500).send('Server error');
        }
    })


//@router           DELETE api/users
//@description      Get all drives by user ID
//@access           Private
router.delete(
    '/',
    [
        check('userId', 'Please include a valid userId').not().isEmpty(),
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { userId } = req.body;
        try {
            //Check if user is exist
            await client.query(`DELETE FROM asaf.users WHERE user_Id = '${userId}'`, async (err, result, fields) => {
                if (err) return res.status(400).json({ err });
                else if (result.affectedRows > 0) {
                    res.json({ msg: "User deleted" });
                    await createLogsUsers(req, res, userId, "User deleted in DB", { msg: "User deleted" });
                } else {
                    console.log(result)
                    return res.status(400).json({ errors: "User is not founded" });
                }
            });
        } catch (err) {
            //console.error(err.message);
            res.status(500).send('Server error');
        }
    })




//@router           PUT api/users
//@description      Confirm Drive by passenger
//@access           Public
router.put(
    '/',
    [
        check('userId', 'userId is required').not().isEmpty(),
        check('last_name', 'last_name is required').not().isEmpty(),
        check('first_name', 'first_name is required').not().isEmpty(),
        check('phone_Number', 'phone_Number is required').not().isEmpty(),
        check('email', 'Please include a valid Email').isEmail(),
        check('password', 'Please enter a password with 6  or more characters').isLength({ min: 6 }),
        check('gender', 'gender is required').not().isEmpty(),
        check('user_Avatar', 'user_Avatar is required').not().isEmpty(),
        check('degree', 'degree is required').not().isEmpty()
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { userId, last_name, first_name, phone_Number, email, password, gender, user_Avatar, degree } = req.body;
        try {
            //Check if user is exist
            await client.query(`SELECT * FROM asaf.users WHERE user_Id = '${userId}'; `,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: 'Error when get user by userId from DB' }] });
                    else if (result.length > 0 && result[0].email === email.toLowerCase()) {
                        //Encrypt password
                        const salt = await bcrypt.genSalt(10);
                        const encryptedPassword = await bcrypt.hash(password, salt);
                        const query = `
                        UPDATE asaf.users SET
                        first_name = '${first_name}',
                        last_name = '${last_name}',
                        phone_Number = '${phone_Number}',
                        email = '${email.toLowerCase()}',
                        password = '${encryptedPassword}',
                        gender = '${gender}',
                        user_Avatar = '${user_Avatar}',
                        degree = '${degree}'
                        WHERE (user_Id = '${userId}') AND (email = '${email.toLowerCase()}');`;
                        // console.log("query", query);
                        //Update user in DB
                        await client.query(query, (err, result, fields) => {
                            if (err) return res.status(400).json({ errors: [{ msg: err }] });
                            else {
                                return res.send({ msg: 'User updated in DB' });
                            }
                        });
                    } else {
                        return res.status(400).json({ errors: 'User is not exist' });
                    }
                });
        } catch (err) {
            //console.error(err.message);
            res.status(500).send('Server error');
        }
    })



module.exports = router;