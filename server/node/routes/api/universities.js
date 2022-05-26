const express = require('express');
const { json } = require('express/lib/response');
const client = require('../../config/DB');
const { check, validationResult } = require('express-validator');
const { v4: uuidv4 } = require('uuid');
const bcrypt = require('bcryptjs');
const config = require('../../config/default.json');
const jwt = require('jsonwebtoken');
const router = express.Router();



//@router           POST api/universities
//@description      Add university 
//@access           Public
router.post(
    '/',
    [
        check('universityName', 'universityName is required').not().isEmpty(),
        check('geoLocationId', 'geoLocationId is required').not().isEmpty(),
    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { universityName, geoLocationId } = req.body;
        try {
            const University = {
                universityName,
                geoLocationId,
                universityId: uuidv4()
            }
            const query = `INSERT INTO asaf.universities 
                            (university_Id, university_name, geoLocation_Id) 
                            VALUES 
                            ('${University.universityId}', '${University.universityName}', 
                            '${University.geoLocationId}');`;

            //console.log(query);
            await client.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: err }] });
                    else {
                        res.send({ msg: 'new university added to DB' });
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })



//@router           POST api/universities
//@description      Delete university by id
//@access           Public
router.delete(
    '/',
    [
        check('universityId', 'universityId is required').not().isEmpty(),
    ],
    async (req, res) => {
        const { universityId } = req.body;
        try {
            const query = `DELETE FROM asaf.universities 
                            WHERE university_Id = ('${universityId}');`;
            await client.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: err }] });
                    else {
                        res.send({ msg: 'university deleted from DB' });
                    }
                });
        } catch (err) {
            console.error(err.message);
            res.status(500).send('Server error');
        }
    })


//@router           DELETE api/users
//@description      Get all university 
//@access           Private
router.get(
    '/',
    async (req, res) => {
        try {
            //console.log(1);
            const query = `SELECT * FROM asaf.universities
                            INNER JOIN asaf.geoLocation
                            ON asaf.geoLocation.geoLocation_Id = asaf.universities.geoLocation_Id;`;
            //console.log(2);

            await client.query(query,
                async (err, result, fields) => {
                    if (err) {
                        //console.log(3);
                        return res.status(400).json({ errors: [{ msg: err }] });
                    }
                    else {
                        //console.log(4);

                        return res.send(result);
                    }
                });
        } catch (err) {
            //console.log(5);

            console.error(err.message);
            res.status(500).send({ errors: 'Server error' });
        }
    })




//@router           PUT api/users
//@description      Update university
//@access           Public
router.put(
    '/',
    [
        check('universityName', 'universityName is required').not().isEmpty(),
        check('geoLocationId', 'geoLocationId is required').not().isEmpty(),
        check('universityId', 'universityId is required').not().isEmpty(),

    ],
    async (req, res) => {
        const errors = validationResult(req);
        if (!(errors.isEmpty())) {
            return res.status(400).json({ errors: errors.array() });
        }
        const { universityId, universityName, geoLocationId } = req.body;
        try {

            const query = `UPDATE asaf.universities SET 
            university_name = '${universityName}', 
            geoLocation_Id = '${geoLocationId}' 
                WHERE 
                university_Id = '${universityId}';`;
            await client.query(query,
                async (err, result, fields) => {
                    if (err) return res.status(400).json({ errors: [{ msg: err }] });
                    else {
                        res.send({ msg: 'university updated' })
                    }
                });
        } catch (err) {
            //console.error(err.message);
            res.status(500).send('Server error');
        }
    })



module.exports = router;