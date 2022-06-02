const user_query = {
    addUser: (user) => {
        return `INSERT INTO "public"."rs_users" 
        ("user_Id", "email", "first_name", "last_name", "phone_Number", "image_Id", "degree", "gender", "password") VALUES 
        ('${user.user_Id}', '${user.email}', '${user.first_name}', '${user.last_name}', '${user.phone_Number}', 
        '${user.image_Id}', '${user.degree}', '${user.gender}', '${user.password}');`;
    },
    updateUser: (user) => {
        return `UPDATE "public"."rs_users" SET 
                    "email" = '${user.email}', 
                    "first_name" = '${user.first_name}', 
                    "last_name" = '${user.last_name}', 
                    "phone_Number" = '${user.phone_Number}', 
                    "image_Id" = '${user.image_Id}',
                    "degree" = '${user.degree}', 
                    "gender" = '${user.gender}', 
                    "password" = '${user.password}'
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUserById: (user) => {
        return `DELETE FROM "public"."rs_users" 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUserByEmail: (user) => {
        return `DELETE FROM "public"."rs_users" 
                WHERE "email" = '${user.email}';`;
    },
    getUserById: (user) => {
        return `SELECT * FROM "public"."rs_users" 
                 WHERE "user_Id" = '${user.user_Id}';`;
    },
    getUserByEmail: (user) => {
        return `SELECT * FROM "public"."rs_users" 
                WHERE "email" = '${user.email}';`;
    },
    getAllUsers: () => {
        return `SELECT * FROM "public"."rs_users";`;
    },
}
module.exports = user_query;
