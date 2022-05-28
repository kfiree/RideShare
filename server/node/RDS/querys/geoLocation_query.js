const geoLocation_query = {
    addGeoLocation: (geoLocation) => {
        return `INSERT INTO "public"."rs_geoLocation" 
        ("geoLocation_Id", "latitude", "longitude") 
        VALUES 
        ('${geoLocation.geoLocation_Id}', ${geoLocation.latitude}, ${geoLocation.longitude});`;
    },
    updateGeoLocation: (geoLocation) => {
        return `UPDATE "public"."rs_geoLocation" SET 
                    "latitude" = ${geoLocation.latitude}, 
                    "longitude" = ${geoLocation.longitude}, 
                    "nameLocation" = '${geoLocation.nameLocation}' 
                WHERE 
                    "geoLocation_Id" = '${geoLocation.geoLocation_Id}';`;
    },
    deleteGeoLocationById: (geoLocation) => {
        return `DELETE FROM "public"."rs_geoLocation" 
        WHERE "geoLocation_Id" = '${geoLocation.geoLocation_Id}';`;
    },
    getGeoLocationById: (geoLocation) => {
        return `SELECT * FROM "public"."rs_geoLocation" 
                WHERE "geoLocation_Id" = '${geoLocation.geoLocation_Id}';`;
    },
    getAllGeoLocations: () => { return `SELECT * FROM "public"."rs_geoLocation";`; },
}

export default geoLocation_query;
