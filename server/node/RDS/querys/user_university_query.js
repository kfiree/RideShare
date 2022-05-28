const user_university_query = {
    addUsersUniversities: (usersUniversity) => {
        return `INSERT INTO "public"."rs_users_universities" 
                ("user_Id", "university_Id") 
                VALUES 
                ('${usersUniversity.user_Id}', '${usersUniversity.university_Id}');`;
    },
    updateUuniversity_Id_UsersUniversities: (usersUniversity) => {
        return `UPDATE "public"."rs_users_universities" SET 
                    "university_Id" = '${usersUniversity.university_Id}'
                WHERE "user_Id" = '${usersUniversity.user_Id}';`;
    },
    deleteUsersUniversitiesByUserId: (usersUniversity) => {
        return `DELETE FROM "public"."rs_users_universities" 
                WHERE "user_Id" = '${usersUniversity.user_Id}';`;
    },
    deleteUsersUniversitiesByUniversityId: (usersUniversity) => {
        return `DELETE FROM "public"."rs_users_universities" 
                WHERE "university_Id" = '${usersUniversity.university_Id}';`;
    },
    getUsersUniversitiesByUniversityId: (usersUniversity) => {
        return `SELECT * FROM "public"."rs_users_universities" 
                WHERE "university_Id" = '${usersUniversity.university_Id}';`;
    },
    getUsersUniversitiesByUserId: (usersUniversity) => {
        return `SELECT * FROM "public"."rs_users_universities" 
                WHERE "user_Id" = '${usersUniversity.user_Id}';`;
    },
    getAllUsersUniversities: () => {
        return `SELECT * FROM "public"."rs_users_universities";`;
    },
}

export default user_university_query;
