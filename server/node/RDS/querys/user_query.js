const user_query = {
    addUser: (user) => {
        return `INSERT INTO "public"."rs_users" 
        ("user_Id", "email", "first_name", "last_name", "phone_Number", "image_Id", "degree", "gender", "password") VALUES 
        ('${user.user_Id}', '${user.email}', '${user.first_name}', '${user.last_name}', '${user.phone_Number}', 
        '${user.image_Id}', '${user.gender}', '${user.password}');`;
    },
    updateUser: (user) => {
        return `UPDATE "public"."rs_users" SET 
                    "email" = '${user.user_Id}', 
                    "first_name" = '${user.user_Id}', 
                    "last_name" = '${user.user_Id}', 
                    "phone_Number" = '${user.user_Id}', 
                    "image_Id" = '${user.user_Id}',
                    "degree" = '${user.user_Id}', 
                    "gender" = '${user.user_Id}', 
                    "password" = '${user.user_Id}', 
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
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    getAllUsers: () => {
        return `SELECT * FROM "public"."rs_users";`;
    },
}

export default user_query;
