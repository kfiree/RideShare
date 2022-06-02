const user_drive_query = {
    addUsersDrives: (user, drive) => {
        return `INSERT INTO "public"."rs_users_drives" 
        ("user_Id", "drive_Id") 
        VALUES 
        ('${user.user_Id}', '${drive.drive_Id}');`;
    },
    updateUsersDrivesByDriveId: (user, drive) => {
        return `UPDATE "public"."rs_users_drives" SET 
                "user_Id" = '${user.user_Id}' 
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    updateUsersDrivesByUserId: (user, drive) => {
        return `UPDATE "public"."rs_users_drives" SET 
                "drive_Id" = '${drive.drive_Id}' 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUsersDrivesByUserId: (user) => {
        return `DELETE FROM "public"."rs_users_drives" 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    deleteUsersDrivesDriveId: (drive) => {
        return `DELETE FROM "public"."rs_users_drives" 
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    getUsersDrivesByDriveId: (drive) => {
        return `SELECT * FROM "public"."rs_users_drives" 
                WHERE "drive_Id" = '${drive.drive_Id}';`;
    },
    getUsersDrivesByUserId: (user) => {
        return `SELECT * FROM "public"."rs_users_drives" 
                WHERE "user_Id" = '${user.user_Id}';`;
    },
    getAllUsersDrives: () => {
        return `SELECT * FROM "public"."rs_users_drives";`;
    },
}

module.exports = user_drive_query;
