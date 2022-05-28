const user_university_query = {
    addUsersUniversities: (user, university) => {
        return `INSERT INTO "public"."rs_users_universities" 
                ("user_Id", "university_Id") 
                VALUES 
                ('${user.user_Id}', '${university.university_Id}');`;
    },
    updateUsersUniversities: (user, university) => {
        return `UPDATE "public"."rs_users_universities" SET 
                    "university_Id" = '${university.university_Id}'
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUsersUniversitiesByUserId: (user) => {
        return `DELETE FROM "public"."rs_users_universities" 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUsersUniversitiesByUniversityId: (university) => {
        return `DELETE FROM "public"."rs_users_universities" 
                WHERE "university_Id" = '${university.university_Id}';`;
    },
    getUsersUniversitiesByUniversityId: (university) => {
        return `SELECT * FROM "public"."rs_users_universities" 
                WHERE "university_Id" = '${university.university_Id}';`;
    },
    getUsersUniversitiesByUserId: (user) => {
        return `SELECT * FROM "public"."rs_users_universities" 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    getAllUsersUniversities: () => {
        return `SELECT * FROM "public"."rs_users_universities";`;
    },
}

module.exports = user_university_query;
