const express = require('express');
const { json } = require('express/lib/response');
const connection = require('../../config/DB');
const { check, validationResult } = require('express-validator');
const { v4: uuidv4 } = require('uuid');
const bcrypt = require('bcryptjs');
const config = require('../../config/default.json');
const jwt = require('jsonwebtoken');
const router = express.Router();


//@router           POST api/geoLocation
//@description      Add geoLocation 
//@access           Public
router.post(
    '/',
    [
        check('geoLocation', 'src is geoLocation').isObject(),
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { nameLocation, latitude, longitude } = req.body.geoLocation;
        try {
            const geo = {
                geoLocationId: uuidv4(),
                nameLocation,
                latitude,
                longitude
            }
            const query = `INSERT INTO asaf.geoLocation 
                            (geoLocation_Id, latitude, longitude, nameLocation) 
                            VALUES 
                            ('${geo.geoLocationId}',
                            '${geo.latitude}', 
                            '${geo.longitude}', 
                            '${geo.nameLocation}');`;

            //Check if user is exist
            await connection.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: 'error when create new geoLocation' }] });
                    else {
                        res.send({ msg: 'new geoLocation added to DB' });
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })



//@router           DELETE api/geoLocation
//@description      Delete geoLocation by id
//@access           Public
router.delete(
    '/',
    [
        check('geoLocationId', 'geoLocationId is required').not().isEmpty(),
    ],
    async (req, res) => {
        const { geoLocationId } = req.body;
        try {
            const query = `DELETE FROM asaf.universities 
                            WHERE geoLocation_Id = ('${geoLocationId}');`;
            await connection.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: 'error when delete geoLocation by id' }] });
                    else {
                        res.send({ msg: 'geoLocation deleted from DB' });
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })


//@router           GET api/geoLocation
//@description      Get all geoLocations 
//@access           Private
router.get(
    '/',
    async (req, res) => {
        try {

            const query = `SELECT * FROM asaf.geoLocation;`;
            await connection.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ err: err, msg: 'error when get all geoLocation' }] });
                    else {
                        res.send(result);
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })




//@router           PUT api/geoLocation
//@description      Update geoLocation
//@access           Public
router.put(
    '/',
    [
        check('geoLocation', 'geoLocation is required').isObject(),

    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { nameLocation, latitude, longitude, geoLocationId } = req.body.geoLocation;
        try {

            const query = `
            
            UPDATE asaf.geoLocation SET 
            latitude = '${latitude}', 
            longitude = '${longitude}',
             nameLocation = '${nameLocation}' 
             WHERE 
             geoLocation_Id = '${geoLocationId}';`;
            await connection.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: 'Error when update geoLocation by geoLocationId in DB' }] });
                    else {
                        res.send({ msg: 'geoLocation updated' })
                    }
                });
        } catch (err) {
            //console.error(err.message);
            res.status(500).send('Server error');
        }
    })



module.exports = router;