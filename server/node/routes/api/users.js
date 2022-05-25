const express = require('express');
const { json } = require('express/lib/response');
const connection = require('../../config/DB');
const { check, validationResult } = require('express-validator');
const { v4: uuidv4 } = require('uuid');
const bcrypt = require('bcryptjs');
const config = require('../../config/default.json');
const jwt = require('jsonwebtoken');
const router = express.Router();



//@router           POST api/users
//@description      Register user
//@access           Public
router.post(
    '/',
    [
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
        const { last_name, first_name, phone_Number, email, password, gender, user_Avatar, degree } = req.body;
        try {
            //Check if user is exist
            await connection.query(`SELECT * FROM asaf.users WHERE email = '${email.toLowerCase()}';`,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: 'Error when get user by email from DB' }] });
                    if (result.length > 0 && result[0].email === email.toLowerCase()) {
                        console.log('User exist');
                        return res.status(400).json({ errors: [{ msg: 'User exist' }] });
                    } else {
                        //Encrypt password
                        const salt = await bcrypt.genSalt(10);
                        const encryptedPassword = await bcrypt.hash(password, salt);


                        // for (let index = 0; index < 100; index++) {
                        const emailS = email.toLowerCase().split("@");
                        let user = {
                            userId: uuidv4(),
                            password: encryptedPassword,
                            email: `${emailS[0]} ${emailS[1]}`,
                            first_name,
                            last_name,
                            phone_Number,
                            gender,
                            user_Avatar,
                            degree,
                            token: ''
                        }
                        console.log(
                            JSON.stringify(user)
                        );
                        const query = `INSERT INTO asaf.users 
                                            (user_Id, first_name, last_name, phone_Number, email, password, gender, user_Avatar, degree) 
                                        VALUES 
                                            ('${user.userId}', '${user.first_name}', '${user.last_name}', '${user.phone_Number}', 
                                            '${user.email}', '${user.password}', '${user.gender}', '${user.user_Avatar}', '${user.degree}');`;
                        // console.log("query", query);

                        //Save user in DB
                        await connection.query(query,
                            (err, result, fields) => {
                                if (err) return res.status(400).json({ errors: [{ msg: 'Error when create user' }] });
                                else {
                                    console.log('User created');
                                    //Return jsonwebToken
                                    const payLoad = { user: { id: user.id } };
                                    jwt.sign(
                                        payLoad,
                                        config["jwtSecret"],
                                        { expiresIn: 360000 },
                                        (err, token) => {
                                            user.token = token;
                                            if (err) throw err;
                                            res.json({ user });
                                        }
                                    );
                                }
                            });
                    }

                    // }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })



//@router           POST api/users
//@description      Login user
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
            await connection.query(query,
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
//@description      Delete user
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
            await connection.query(`DELETE FROM asaf.users WHERE user_Id = '${userId}'`, async (err, result, fields) => {
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
//@description      Update user
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
            await connection.query(`SELECT * FROM asaf.users WHERE user_Id = '${userId}'; `,
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
                        await connection.query(query, (err, result, fields) => {
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

/**
 TODO
 */

//@router           PUT api/users
//@description      Get all users
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
            await connection.query(`SELECT * FROM asaf.users WHERE user_Id = '${userId}'; `,
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
                        await connection.query(query, (err, result, fields) => {
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