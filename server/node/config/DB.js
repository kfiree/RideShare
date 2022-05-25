const mysql = require('mysql');

const connection = mysql.createConnection({
    host:   '127.0.0.1',
    database: 'asaf',
    user: 'root',
    password: '123456789',
});

connection.connect(async (err) => {
    if (err) throw err;
    console.log('database is connected!');
    // const query = `INSERT INTO asaf.users(id, user_Id, first_name, last_name, phone_Number, email, password, gender, Image_Id, user_Avatar, degree)
    // VALUES('242', '234234243', '234234234', '234234', '0525675171', 'dfvvd33fvfd@gmail.com', '123456', 'vascc', 'dsvsdvsdv', 'sdvsdvsdv', 'svdsdvs');`;
    // await connection.query(query,
    //     (err, result, fields) => {
    //         if (err) console.log(err);
    //         else console.log(result);
    //     });
})

module.exports = connection;