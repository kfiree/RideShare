const express = require('express');
const app = express();
const cors = require('cors');

const connection = require('./config/DB');
const { check, validationResult } = require('express-validator');
//Init Middleware
app.use(cors())
app.use(express.json({ extended: false }));

app.get('/', (req, res) => res.send('API Running'));
app.use('/api/users', require('./routes/api/users'));
app.use('/api/drives', require('./routes/api/drives'));
app.use('/api/universities', require('./routes/api/universities'));
app.use('/api/geoLocation', require('./routes/api/geoLocation'));

const PORT = process.env.PORT || 5002;
app.listen(PORT, async () => {
  console.log(`Server started on port ${PORT}`)
});


//install server dependencies
//npm i express express-validator bcrypt.js config gravatar jsonwebtoken request mysql
//not installed mongose
//install dev-tools
//npm i -D nodemon concurrently

//install client dependencies
//npm i axios react-router-dom uuid redux react-redux redux-thunk redux-devtools-extension moment react-moment

//run sql server:
//mysql.server start
//connect to DB
//mysql -u root -p

//run server:
//npm start

//stop mysql server:
//mysql.server stop; 

