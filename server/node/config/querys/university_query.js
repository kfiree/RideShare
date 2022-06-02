const university_query = {
    addUniversity: (university) => {
        return `INSERT INTO "public"."rs_universities" 
        ("university_Id", "university_name", "geoLocation_Id")
        VALUES 
        ('${university.university_Id}', '${university.university_name}', '${university.geoLocation_Id}');`;
    },
    updateUniversity: (university) => {
        return `UPDATE "public"."rs_universities" SET 
                    "university_name" = '${university.university_name}',
                    "geoLocation_Id" = '${university.geoLocation_Id}' 
                WHERE "university_Id" = '${university.university_Id}';`;
    },
    deleteUniversityById: (university) => {
        return `DELETE FROM "public"."rs_universities" 
        WHERE "university_Id" = '${university.university_Id}';`;
    },
    deleteUniversityByGeoLocationId: (university) => {
        return `DELETE FROM "public"."rs_universities" 
        WHERE "geoLocation_Id" = '${university.geoLocation_Id}';`;
    },
    getUniversityById: (university) => {
        return `SELECT * FROM "public"."rs_universities"
                WHERE "university_Id" = '${university.university_Id}';`;
    },
    getUniversityByGeoLocationId: (university) => {
        return `SELECT * FROM "public"."rs_universities"
                WHERE "geoLocation_Id" = '${university.geoLocation_Id}';`;
    },
    getAllUniversities: () => {
        return `SELECT * FROM "public"."rs_universities";`;
    },
}

module.exports = university_query;

