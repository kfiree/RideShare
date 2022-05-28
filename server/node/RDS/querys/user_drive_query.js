const user_drive_query = {
    addUsersDrives: (usersDrive) => {
        return `INSERT INTO "public"."rs_users_drives" 
        ("user_Id", "drive_Id") 
        VALUES 
        ('${usersDrive.user_Id}', '${usersDrive.drive_Id}');`;
    },
    updateUser_Id_UsersDrives: (usersDrive) => {
        return `UPDATE "public"."rs_users_drives" SET 
                "user_Id" = '${usersDrive.user_Id}' 
                WHERE "drive_Id" = '${usersDrive.drive_Id}';`;
    },
    updateDrive_Id_UsersDrives: (usersDrive) => {
        return `UPDATE "public"."rs_users_drives" SET 
                "drive_Id" = '${usersDrive.drive_Id}' 
                WHERE "user_Id" = '${usersDrive.user_Id}';`;
    },
    deleteUsersDrivesByUserId: (usersDrive) => {
        return `DELETE FROM "public"."rs_users_drives" 
                WHERE "drive_Id" = '${usersDrive.drive_Id}';`;
    },
    deleteUsersDrivesDriveId: (usersDrive) => {
        return `DELETE FROM "public"."rs_users_drives" 
                WHERE "user_Id" = '${usersDrive.user_Id}';`;
    },
    getUsersDrivesByDriveId: (usersDrive) => {
        return `SELECT * FROM "public"."rs_users_drives" 
                WHERE "drive_Id" = '${usersDrive.drive_Id}';`;
    },
    getUsersDrivesByUserId: (usersDrive) => {
        return `SELECT * FROM "public"."rs_users_drives" 
                WHERE "user_Id" = '${usersDrive.user_Id}';`;
    },
    getAllUsersDrives: () => {
        return `SELECT * FROM "public"."rs_users_drives";`;
    },
}

export default user_drive_query;
