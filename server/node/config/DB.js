// const mysql = require('mysql');

// const connection = mysql.createConnection({
//     host:   '127.0.0.1',
//     database: 'asaf',
//     user: 'root',
//     password: '123456789',
// });

// connection.connect(async (err) => {
//     if (err) throw err;
//     console.log('database is connected!');
//     // const query = `INSERT INTO asaf.users(id, user_Id, first_name, last_name, phone_Number, email, password, gender, Image_Id, user_Avatar, degree)
//     // VALUES('242', '234234243', '234234234', '234234', '0525675171', 'dfvvd33fvfd@gmail.com', '123456', 'vascc', 'dsvsdvsdv', 'sdvsdvsdv', 'svdsdvs');`;
//     // await connection.query(query,
//     //     (err, result, fields) => {
//     //         if (err) console.log(err);
//     //         else console.log(result);
//     //     });
// })

// module.exports = connection;

const { Client } = require('pg');
const URL = "postgres://jrtfpxkjouseuk:ee06e29a206ed56595fb998c7ab814f2b0ebce8a46a6cc088d614255c64c7ec4@ec2-34-246-227-219.eu-west-1.compute.amazonaws.com:5432/de3tg1mpkboq65";
const client = new Client({
    connectionString: URL,
    ssl: {
        rejectUnauthorized: false
    }
});

client.connect();
console.log('database connected...');



module.exports = client;
