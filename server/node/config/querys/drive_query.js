
const driveQuerys = {
    addDrive: (drive) => {
        return `INSERT INTO "public"."rs_drives" 
                ("drive_Id", "geoLocationSrc_Id", "geoLocationDest_Id", "passengers", "type",
                 "num_seat_available", "price", "AVG_Price", "upcoming_Drives", "leaveTime", 
                 "path_Id", "createdAt") 
                VALUES (
                '${drive.drive_Id}', 
                '${drive.geoLocationSrc_Id}', 
                '${drive.geoLocationDest_Id}', 
                '${drive.passengers}', 
                '${drive.type}', 
                ${drive.num_seat_available}, 
                ${drive.price}, 
                ${drive.AVG_Price}, 
                '${drive.upcoming_Drives}', 
                '${drive.leaveTime}', 
                '${drive.path_Id}', 
                NOW());`;
    },
    updateDrive: (drive) => {
        return `UPDATE "public"."rs_drives" SET 
                    "geoLocationSrc_Id" = '${drive.geoLocationSrc_Id}',
                    "geoLocationDest_Id" = '${drive.geoLocationDest_Id}',
                    "passengers" = '${drive.passengers}',
                    "type" = '${drive.type}',
                    "num_seat_available" = ${drive.num_seat_available},
                    "price" = ${drive.price},
                    "AVG_Price" = ${drive.AVG_Price},
                    "upcoming_Drives" = '${drive.upcoming_Drives}',
                    "leaveTime" = '${drive.leaveTime}',
                    "path_Id" = '${drive.path_Id}'
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    deleteDriveById: (drive) => {
        return `DELETE FROM "public"."rs_drives" 
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    getDriveById: (drive) => {
        return `SELECT * FROM "public"."rs_drives"
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    getAllDrives: () => { return `SELECT * FROM "public"."rs_drives";`; },
}

module.exports = driveQuerys;
