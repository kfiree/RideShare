const { v4: uuidv4 } = require('uuid');
const moment = require('moment-timezone');

module.exports = getRandomUser = () => {
    return {
        user_Id: uuidv4(),//uuid
        phone_Number: '052-0000000',//text size 15
        createdAt: moment().format(),//moment
        email: `user${parseInt(Math.random() * 100000000)}@gmail.com`,//text
        first_name: 'first_name',//text
        last_name: 'last_name',//text
        image_Id: 'image_Id',//text
        degree: 'degree',//text
        gender: 'gender',//text
        password: '$2a$10$x2VOOzS1/8yUI9p598ddpeZ2Tk3YDLoqJZsek.5mL/BPeTlY00Dhy',//text
        drives_offered: [], //List of User_Drives.id
        drives_taken: [], //List of User_Drives.id
    }
}